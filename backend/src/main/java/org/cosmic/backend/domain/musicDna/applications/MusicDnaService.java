package org.cosmic.backend.domain.musicDna.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.cosmic.backend.domain.musicDna.domains.User_Dna;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.musicDna.dtos.ListDna;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.musicDna.exceptions.NotMatchMusicDnaCountException;
import org.cosmic.backend.domain.musicDna.repositorys.DnaRepository;
import org.cosmic.backend.domain.musicDna.repositorys.EmotionRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MusicDnaService {
    private final DnaRepository dnaRepository;
    private final EmotionRepository emotionRepository;
    private final UsersRepository usersRepository;
    private final int MAX_DNA_SIZE = 4;

    public MusicDnaService(DnaRepository dnaRepository, EmotionRepository emotionRepository, UsersRepository usersRepository) {
        this.dnaRepository = dnaRepository;
        this.emotionRepository = emotionRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void saveDNA(Long userId, List<DnaDetail>dna) {

        if(usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if (dna.size() != MAX_DNA_SIZE) {
            throw new NotMatchMusicDnaCountException();
        }
        User user=usersRepository.findById(userId).get();
        List<User_Dna> userDNAs = dnaRepository.findAllByUser_UserId(userId);
        if(dna.size() < userDNAs.size()) {
            dnaRepository.deleteAllById(userDNAs.stream().skip(dna.size()).map(User_Dna::getId).toList());
            userDNAs = userDNAs.subList(0, dna.size());
        }
        userDNAs.addAll(Stream.generate(User_Dna::new).limit(dna.size() - userDNAs.size()).toList());
        List<MusicDna> newDNAs = emotionRepository.findAllById(dna.stream().map(DnaDetail::getDnaKey).toList());
        for (int i = 0; i < userDNAs.size(); i++) {
            userDNAs.get(i).setUser(user);
            userDNAs.get(i).setEmotion(newDNAs.get(i));
        }
        dnaRepository.saveAll(userDNAs);
    }

    @Transactional
    public List<ListDna> getAllDna(){
        return emotionRepository.findAll().stream()
            .map(dna->new ListDna(dna.getEmotionId(),dna.getEmotion()))
            .collect(Collectors.toList());
    }

    @Transactional
    public List<UserDnaResponse> getUserDna(UserDto user) {
        if(usersRepository.findById(user.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        return dnaRepository.findAllByUser_UserId(user.getUserId())
                .stream()
                .map(UserDnaResponse::new)
                .toList();
    }
}
