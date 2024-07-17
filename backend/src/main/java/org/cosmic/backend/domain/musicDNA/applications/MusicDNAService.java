package org.cosmic.backend.domain.musicDNA.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.cosmic.backend.domain.musicDNA.domain.User_Dna;
import org.cosmic.backend.domain.musicDNA.dto.DNADetail;
import org.cosmic.backend.domain.musicDNA.dto.ListDNA;
import org.cosmic.backend.domain.musicDNA.dto.UserDnaResponse;
import org.cosmic.backend.domain.musicDNA.exceptions.NotFoundEmotionException;
import org.cosmic.backend.domain.musicDNA.exceptions.NotMatchMusicDnaCountException;
import org.cosmic.backend.domain.musicDNA.repository.DnaRepository;
import org.cosmic.backend.domain.musicDNA.repository.EmotionRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.dto.userDto;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MusicDNAService {
    private final DnaRepository dnaRepository;
    private final EmotionRepository emotionRepository;
    private UsersRepository usersRepository;

    public MusicDNAService(DnaRepository dnaRepository, UsersRepository usersRepository, EmotionRepository emotionRepository) {
        this.usersRepository=usersRepository;
        this.dnaRepository = dnaRepository;
        this.emotionRepository = emotionRepository;
    }

    @Transactional
    public void saveDNA(Long key, List<DNADetail>dna) {

        if(!usersRepository.findById(key).isPresent())
        {
            throw new NotFoundUserException();
        }
        else if (dna.size() != 4) {
            //dna개수가 맞지 않을 때
            throw new NotMatchMusicDnaCountException();
        }
        else{
            System.out.println("*******HI 1");
            User user=usersRepository.findById(key).get();
            List<User_Dna>userDnas=user.getUserDnas();
            System.out.println("*******HI 1");
            if (!dnaRepository.findByUser_UserId(key).isPresent()) {
                System.out.println("*******HI 2");
                List<MusicDna> dnaKeys = new ArrayList<>();
                List<User_Dna> userdnas = new ArrayList<>();
                for (DNADetail dnaDetail : dna) {
                    if(!emotionRepository.findByEmotionId(dnaDetail.getDnaKey()).isPresent())
                    {
                        throw new NotFoundEmotionException();
                        //없는 감정일 때
                    }
                    else{
                        MusicDna dnas = emotionRepository.findByEmotionId(dnaDetail.getDnaKey()).get();
                        User_Dna userDna=new User_Dna();
                        userDna.setUser(user);
                        userDna.setEmotion(dnas);
                        dnaRepository.save(userDna);
                        userdnas.add(userDna);
                    }
                }
                user.setUserDnas(userDnas);
            }
            else {
                dnaRepository.deleteByUser_UserId(user.getUserId());

                List<User_Dna> userDnasToSave = new ArrayList<>();
                for (DNADetail dnaDetail : dna) {
                    Optional<MusicDna> optionalEmotion = emotionRepository.findByEmotionId(dnaDetail.getDnaKey());
                    if (!optionalEmotion.isPresent()) {
                        throw new NotFoundEmotionException();
                    }
                    MusicDna musicDna = optionalEmotion.get();
                    User_Dna newUserDna = new User_Dna();
                    newUserDna.setUser(user);
                    newUserDna.setEmotion(musicDna);
                    userDnasToSave.add(newUserDna);
                }
                dnaRepository.saveAll(userDnasToSave);
                user.setUserDnas(userDnasToSave);
            }
        }
    }

    @Transactional
    public List<ListDNA> getAllDna(){
        return emotionRepository.findAll().stream()
                .map(dna->new ListDNA(dna.getEmotionId(),dna.getEmotion()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserDnaResponse> getUserDna(userDto user) {
        if(!usersRepository.findById(user.getUserId()).isPresent())
        {
            throw new NotFoundUserException();
        }
        else{
            System.out.println("*******HI 1");
            List<User_Dna>userDnas=dnaRepository.findByUser_UserId(user.getUserId()).get();
            System.out.println("*******HI 2");
            List<UserDnaResponse> listDNA=new ArrayList<>();
            for(User_Dna dna:userDnas)
            {
                System.out.println("*******HI 3");
                UserDnaResponse dna2=new UserDnaResponse();
                dna2.setEmotion(dna.getEmotion().getEmotion());
                dna2.setUserId(user.getUserId());
                listDNA.add(dna2);
            }
            return listDNA;
        }
    }

}
