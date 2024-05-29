package org.cosmic.backend.domain.musicDNA.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.cosmic.backend.domain.musicDNA.domain.User_Dna;
import org.cosmic.backend.domain.musicDNA.dto.DNADetail;
import org.cosmic.backend.domain.musicDNA.dto.ListDNA;
import org.cosmic.backend.domain.musicDNA.exceptions.NotMatchMusicDnaCountException;
import org.cosmic.backend.domain.musicDNA.repository.DnaRepository;
import org.cosmic.backend.domain.musicDNA.repository.EmotionRepository;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

        List<User_Dna> userDnas = dnaRepository.findAllByUserId(key);

        if (dna.size() != 4) {
            //dna개수가 맞지 않을 때
            throw new NotMatchMusicDnaCountException();
        }

        if (!dnaRepository.findById(key).isPresent()) {

            User user = usersRepository.findById(key).get();

            List<MusicDna> dnaKeys = new ArrayList<>();
            for (DNADetail dnaDetail : dna) {
                MusicDna dnas = emotionRepository.findByEmotionId(dnaDetail.getDnaKey()).get();
                User_Dna userDna=new User_Dna();
                userDna.setUser(user);
                userDna.setEmotion(dnas);
                dnaRepository.save(userDna);
            }

        }
        else{//이미있다면
            for(int i=0;i<dna.size();i++)
            {
                DNADetail dnaDetail = dna.get(i);
                MusicDna dans=emotionRepository.findByEmotionId(dnaDetail.getDnaKey()).get();
                User_Dna userDna=userDnas.get(i);
                userDna.setEmotion(dans);
                dnaRepository.save(userDna);
            }
        }
    }
    @Transactional
    public List<ListDNA> getAllDna(){
        return emotionRepository.findAll().stream()
                .map(dna->new ListDNA(dna.getEmotionId(),dna.getEmotion()))
                .collect(Collectors.toList())
                ;
    }

}
