package org.cosmic.backend.domain.musicDNA.applications;

import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.cosmic.backend.domain.musicDNA.repository.DnaRepository;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicDNAService {
    private final DnaRepository dnaRepository;
    private UsersRepository usersRepository;

    public MusicDNAService(DnaRepository dnaRepository,UsersRepository usersRepository) {
        this.usersRepository=usersRepository;
        this.dnaRepository = dnaRepository;
    }

    public void saveDNA(String userEmail, List<String>dna)
    {
        User user=new User();
        user=usersRepository.findByEmail_Email(userEmail).get();
        if(dna.size()!=4)//꼭 4가지를 받아야함.
        {
            //오류 발생시키기
        }
        else{
            user.setMusicDNAs(dna);
            usersRepository.save(user);
        }
    }

    //모든 dna데이터들을 보내는.
    public List<MusicDna> getAllDna(){
        return dnaRepository.findAll();
    }

}
