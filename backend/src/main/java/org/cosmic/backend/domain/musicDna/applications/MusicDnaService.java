package org.cosmic.backend.domain.musicDna.applications;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.musicDna.exceptions.NotMatchMusicDnaCountException;
import org.cosmic.backend.domain.musicDna.repositorys.MusicDnaRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>음악 DNA와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.</p>
 *
 * <p>이 서비스는 사용자 DNA 저장, 조회 및 전체 DNA 목록 조회 기능을 제공합니다.</p>
 *
 */
@Service
public class MusicDnaService {

    private final MusicDnaRepository musicDnaRepository;
    private final UsersRepository usersRepository;
    private final int MAX_DNA_SIZE = 4;

    /**
     * <p>MusicDnaService의 생성자입니다.</p>
     *
     * @param musicDnaRepository DNA 데이터를 처리하는 리포지토리
     * @param usersRepository 사용자 데이터를 처리하는 리포지토리
     */
    public MusicDnaService(MusicDnaRepository musicDnaRepository, UsersRepository usersRepository) {
        this.musicDnaRepository = musicDnaRepository;
        this.usersRepository = usersRepository;
    }

    /**
     * <p>주어진 사용자 ID와 DNA 데이터를 저장합니다.</p>
     *
     * @param userId 저장할 사용자 ID
     * @param dna 사용자 DNA 데이터를 포함한 리스트
     * @return 저장된 사용자 DNA 데이터를 포함한 리스트
     *
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     * @throws NotMatchMusicDnaCountException DNA 데이터의 수가 지정된 수(MAX_DNA_SIZE)보다 많거나 적을 경우 발생합니다.
     */
    @Transactional
    public List<UserDnaResponse> saveDNA(Long userId, List<Long> dna) {
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        if (dna.size() != MAX_DNA_SIZE) {
            throw new NotMatchMusicDnaCountException();
        }
        User user = usersRepository.findById(userId).get();
        user.setDNAs(musicDnaRepository.findAllById(dna));
        usersRepository.save(user);
        return usersRepository.findById(userId).get().getDNAs()
                .stream()
                .map(UserDnaResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * <p>모든 DNA 데이터를 조회합니다.</p>
     *
     * @return 모든 DNA 데이터를 포함한 리스트
     */
    @Transactional
    public List<DnaDetail> getAllDna() {
        return musicDnaRepository.findAll().stream()
                .map(dna -> new DnaDetail(dna.getDnaId(), dna.getName()))
                .collect(Collectors.toList());
    }

    /**
     * <p>주어진 사용자 ID로 사용자 DNA 데이터를 조회합니다.</p>
     *
     * @param userId 사용자 ID
     * @return 사용자 DNA 데이터를 포함한 응답 리스트
     *
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public List<UserDnaResponse> getUserDna(Long userId) {
        if (usersRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        return usersRepository.findById(userId).get().getDNAs()
                .stream()
                .map(UserDnaResponse::new)
                .collect(Collectors.toList());
    }
}
