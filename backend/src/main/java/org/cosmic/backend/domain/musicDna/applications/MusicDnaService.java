package org.cosmic.backend.domain.musicDna.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.cosmic.backend.domain.musicDna.domains.User_Dna;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.musicDna.dtos.ListDna;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.musicDna.exceptions.NotFoundEmotionException;
import org.cosmic.backend.domain.musicDna.exceptions.NotMatchMusicDnaCountException;
import org.cosmic.backend.domain.musicDna.repositorys.DnaRepository;
import org.cosmic.backend.domain.musicDna.repositorys.EmotionRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MusicDnaService {
    private final DnaRepository dnaRepository;
    private final EmotionRepository emotionRepository;
    private final UsersRepository usersRepository;

    public MusicDnaService(DnaRepository dnaRepository, EmotionRepository emotionRepository, UsersRepository usersRepository) {
        this.dnaRepository = dnaRepository;
        this.emotionRepository = emotionRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void saveDNA(Long key, List<DnaDetail>dna) {

        if(usersRepository.findById(key).isEmpty())
        {
            throw new NotFoundUserException();
        }
        if (dna.size() != 4) {
            throw new NotMatchMusicDnaCountException();
        }
        User user=usersRepository.findById(key).get();
        List<User_Dna>userDnas=user.getUserDnas();
        if (dnaRepository.findByUser_UserId(key).isEmpty()) {
            List<User_Dna> userdnas = new ArrayList<>();
            for (DnaDetail dnaDetail : dna) {
                if(emotionRepository.findByEmotionId(dnaDetail.getDnaKey()).isEmpty())
                {
                    throw new NotFoundEmotionException();
                }
                MusicDna dnas = emotionRepository.findByEmotionId(dnaDetail.getDnaKey()).get();
                User_Dna userDna=new User_Dna(user,dnas);
                dnaRepository.save(userDna);
                userdnas.add(userDna);
            }
            user.setUserDnas(userDnas);
        }
        else{
            dnaRepository.deleteByUser_UserId(user.getUserId());
            List<User_Dna> userDnasToSave = new ArrayList<>();
            for (DnaDetail dnaDetail : dna) {
                Optional<MusicDna> optionalEmotion = emotionRepository.findByEmotionId(dnaDetail.getDnaKey());
                if (optionalEmotion.isEmpty()) {
                    throw new NotFoundEmotionException();
                }
                MusicDna musicDna = optionalEmotion.get();
                User_Dna newUserDna = new User_Dna(user,musicDna);
                userDnasToSave.add(newUserDna);
            }
            dnaRepository.saveAll(userDnasToSave);
            user.setUserDnas(userDnasToSave);
        }
    }

    @Transactional
    public List<ListDna> getAllDna(){
        return emotionRepository.findAll().stream()
            .map(dna->new ListDna(dna.getEmotionId(),dna.getEmotion()))
            .collect(Collectors.toList());
    }

    @Transactional
    public List<UserDnaResponse> getUserDna(UserDto user) {
        if(usersRepository.findById(user.getUserId()).isEmpty())
        {
            throw new NotFoundUserException();
        }
        List<User_Dna>userDnas=dnaRepository.findByUser_UserId(user.getUserId()).get();
        List<UserDnaResponse> listDNA=new ArrayList<>();
        for(User_Dna dna:userDnas)
        {
            UserDnaResponse dna2=new UserDnaResponse(user.getUserId(),dna.getEmotion().getEmotion());
            listDNA.add(dna2);
        }
        return listDNA;
    }
}
