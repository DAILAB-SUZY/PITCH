package org.cosmic.backend.domain.user.applications;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.auth.exceptions.CredentialNotMatchException;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.JoinRequest;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.exceptions.NotMatchConditionException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Log4j2
@Service
public class UserService {
    private final TokenProvider tokenProvider;
    private final UsersRepository usersRepository;
    private final EmailRepository emailRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PlaylistRepository playlistRepository;

    public UserService(TokenProvider tokenProvider, UsersRepository usersRepository, EmailRepository emailRepository, RedisTemplate<String, String> redisTemplate, PlaylistRepository playlistRepository) {
        this.tokenProvider = tokenProvider;
        this.usersRepository = usersRepository;
        this.emailRepository = emailRepository;
        this.redisTemplate = redisTemplate;
        this.playlistRepository = playlistRepository;
    }

    public void userRegister(JoinRequest request){
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        if (!request.getPassword().equals(request.getCheckPassword())) {
            throw new NotMatchPasswordException();
        }
        Optional<Email> emailOpt = emailRepository.findByEmail(request.getEmail());
        //이메일 인증 확인
        if (emailOpt.isEmpty() || !emailOpt.get().getVerified()) {//해당 이메일이 등록된게 아니라면
            throw new NotExistEmailException();
        }
        if(request.getPassword().length() < 8) {
            throw new NotMatchConditionException();
        }
        User newUser = new User(emailOpt.get(),request.getName(),passwordEncoder.encode(request.getPassword()));
        usersRepository.save(newUser);
        Playlist playlist = new Playlist(Instant.now(),Instant.now(),newUser);
        playlistRepository.save(playlist);
    }

    public UserLogin getByCredentials(String email, String password) {
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        User user = usersRepository.findByEmail_Email(email).orElseThrow(CredentialNotMatchException::new);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CredentialNotMatchException();
        }

        return UserLogin.builder()
            .refreshToken(tokenProvider.createRefreshToken(user))
            .token(tokenProvider.create(user))
            .email(email)
            .password(user.getPassword())
            .id(user.getUserId())
            .build();
    }

    private UserLogin getByEmail(String email){
        User user = usersRepository.findById(Long.parseLong(email)).orElseThrow(CredentialNotMatchException::new);

        return UserLogin.builder()
            .refreshToken(tokenProvider.createRefreshToken(user))
            .token(tokenProvider.create(user))
            .email(user.getEmail().getEmail())
            .id(user.getUserId())
            .build();
    }

    public UserLogin getUserByRefreshToken(String refreshToken) {
        String email = tokenProvider.validateAndGetUserId(refreshToken);
        if(email == null){
            throw new CredentialNotMatchException();
        }
        redisTemplate.opsForValue().getAndDelete(email);
        return getByEmail(email);
    }


}
