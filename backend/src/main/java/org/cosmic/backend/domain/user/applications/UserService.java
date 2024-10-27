package org.cosmic.backend.domain.user.applications;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
import org.cosmic.backend.domain.auth.exceptions.CredentialNotMatchException;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.MyUserDetails;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.JoinRequest;
import org.cosmic.backend.domain.user.dtos.UserDetail;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.exceptions.NotMatchConditionException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>사용자(User) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.</p>
 *
 * <p>이 서비스는 사용자 등록, 인증, 로그인, 리프레시 토큰 처리 기능을 제공합니다.</p>
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  @Autowired
  private TokenProvider tokenProvider;
  @Autowired
  private UsersRepository usersRepository;
  @Autowired
  private EmailRepository emailRepository;
  @Autowired
  private PlaylistRepository playlistRepository;

  /**
   * <p>사용자를 등록합니다.</p>
   *
   * @param request 회원 가입 요청 정보를 포함한 객체
   * @throws NotMatchPasswordException  비밀번호와 확인 비밀번호가 일치하지 않을 때 발생합니다.
   * @throws NotExistEmailException     이메일이 등록되지 않았거나 인증되지 않은 경우 발생합니다.
   * @throws NotMatchConditionException 비밀번호 조건이 충족되지 않을 때 발생합니다.
   */
  public void userRegister(JoinRequest request) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if (!request.getPassword().equals(request.getCheckPassword())) {
      throw new NotMatchPasswordException();
    }
    Optional<Email> emailOpt = emailRepository.findByEmail(request.getEmail());
    if (emailOpt.isEmpty() || !emailOpt.get().getVerified()) {
      throw new NotExistEmailException();
    }
    if (request.getPassword().length() < 8) {
      throw new NotMatchConditionException();
    }
    User newUser = User.builder()
        .email(emailOpt.get())
        .username(request.getName())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();
    usersRepository.save(newUser);
    Playlist playlist = Playlist.builder()
        .user(newUser).build();
    playlistRepository.save(playlist);
  }

  /**
   * <p>사용자의 이메일과 비밀번호로 인증 후 사용자 정보를 반환합니다.</p>
   *
   * @param email    사용자의 이메일
   * @param password 사용자의 비밀번호
   * @return 인증된 사용자의 로그인 정보를 포함한 객체
   * @throws CredentialNotMatchException 이메일 또는 비밀번호가 일치하지 않을 때 발생합니다.
   */
  public UserLoginDetail getByCredentials(String email, String password) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    User user = usersRepository.findByEmail_Email(email)
        .orElseThrow(CredentialNotMatchException::new);

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new CredentialNotMatchException();
    }

    return UserLoginDetail.from(user, tokenProvider.create(user),
        tokenProvider.createRefreshToken(user));
  }

  /**
   * <p>리프레시 토큰을 이용해 사용자 정보를 반환합니다.</p>
   *
   * @param refreshToken 리프레시 토큰
   * @return 리프레시 토큰을 통해 인증된 사용자의 로그인 정보를 포함한 객체
   * @throws CredentialNotMatchException 리프레시 토큰이 유효하지 않거나 일치하지 않을 때 발생합니다.
   */
  public UserLoginDetail getUserByRefreshToken(String refreshToken) {
    User user = usersRepository.findById(
            Long.parseLong(tokenProvider.validateRefreshTokenAndGetId(refreshToken)))
        .orElseThrow(NotFoundUserException::new);
    return UserLoginDetail.from(user, tokenProvider.create(user), refreshToken);
  }

  /**
   * 유저 정보를 반환합니다.
   *
   * @param userId 유저 아이디
   * @return 유저디테일
   */
  public UserDetail getUserDetail(Long userId) {
    return User.toUserDetail(
        usersRepository.findById(userId).orElseThrow(NotFoundUserException::new));
  }

  @Override
  public MyUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    return loadUserByUsername(Long.parseLong(userId));
  }
  
  public MyUserDetails loadUserByUsername(Long userId) throws UsernameNotFoundException {
    return usersRepository.findById(userId)
        .orElseThrow(NotFoundUserException::new);
  }
}
