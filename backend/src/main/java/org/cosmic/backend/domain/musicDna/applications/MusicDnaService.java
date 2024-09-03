package org.cosmic.backend.domain.musicDna.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.cosmic.backend.domain.musicDna.domains.User_Dna;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.musicDna.dtos.ListDna;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.musicDna.exceptions.NotMatchMusicDnaCountException;
import org.cosmic.backend.domain.musicDna.repositorys.DnaRepository;
import org.cosmic.backend.domain.musicDna.repositorys.MusicDnaRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 음악 DNA와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 사용자 DNA 저장, 조회 및 전체 DNA 목록 조회 기능을 제공합니다.
 */
@Service
public class MusicDnaService {
    private final DnaRepository dnaRepository;
    private final MusicDnaRepository musicDnaRepository;
    private final UsersRepository usersRepository;
    private final int MAX_DNA_SIZE = 4;

    /**
     * MusicDnaService의 생성자입니다.
     *
     * @param dnaRepository DNA 데이터를 처리하는 리포지토리
     * @param musicDnaRepository 감정 데이터를 처리하는 리포지토리
     * @param usersRepository 사용자 데이터를 처리하는 리포지토리
     */
    public MusicDnaService(DnaRepository dnaRepository, MusicDnaRepository musicDnaRepository, UsersRepository usersRepository) {
        this.dnaRepository = dnaRepository;
        this.musicDnaRepository = musicDnaRepository;
        this.usersRepository = usersRepository;
    }

    /**
     * 주어진 사용자 ID와 DNA 데이터를 저장합니다.
     *
     * @param userId 저장할 사용자 ID
     * @param dna 사용자 DNA 데이터를 포함한 리스트
     *
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     * @throws NotMatchMusicDnaCountException DNA 데이터의 수가 지정된 수(MAX_DNA_SIZE)보다 큰 경우 발생합니다.
     */
    @Transactional
    public void saveDNA(Long userId, List<DnaDetail> dna) {
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if (dna.size() != MAX_DNA_SIZE) {
            throw new NotMatchMusicDnaCountException();
        }
        User user = usersRepository.findById(userId).get();
        List<User_Dna> userDNAs = dnaRepository.findAllByUser_UserId(userId);
        if (dna.size() < userDNAs.size()) {
            dnaRepository.deleteAllById(userDNAs.stream().skip(dna.size()).map(User_Dna::getId).toList());
            userDNAs = userDNAs.subList(0, dna.size());
        }
        userDNAs.addAll(Stream.generate(User_Dna::new).limit(dna.size() - userDNAs.size()).toList());
        List<MusicDna> newDNAs = musicDnaRepository.findAllById(dna.stream().map(DnaDetail::getDnaKey).toList());
        for (int i = 0; i < userDNAs.size(); i++) {
            userDNAs.get(i).setUser(user);
            userDNAs.get(i).setEmotion(newDNAs.get(i));
        }
        dnaRepository.saveAll(userDNAs);
    }

    /**
     * 모든 DNA 데이터를 조회합니다.
     *
     * @return 모든 DNA 데이터를 포함하는 리스트
     */
    @Transactional
    public List<ListDna> getAllDna() {
        return musicDnaRepository.findAll().stream()
                .map(dna -> new ListDna(dna.getDnaId(), dna.getName()))
                .collect(Collectors.toList());
    }

    /**
     * 주어진 사용자 ID로 사용자 DNA 데이터를 조회합니다.
     *
     * @param user 사용자 정보를 포함한 DTO 객체
     * @return 사용자 DNA 데이터를 포함한 응답 리스트
     *
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<UserDnaResponse> getUserDna(UserDto user) {
        if (usersRepository.findById(user.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        return dnaRepository.findAllByUser_UserId(user.getUserId())
                .stream()
                .map(UserDnaResponse::new)
                .toList();
    }
}
