package org.cosmic.backend.domain.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.applications.UserService;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.JoinRequest;
import org.cosmic.backend.domain.user.dtos.UserDetail;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private Email email;
  @InjectMocks
  private UserService userService;
  @Mock
  private UsersRepository usersRepository;
  @Mock
  private EmailRepository emailRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private TokenProvider tokenProvider;

  @BeforeEach
  public void setUp() {
    email = Email.from("test@example.com");
  }

  @Test
  @DisplayName("유저 정보 요청 테스트")
  public void userInfoTest() {
    User user = User.builder()
        .userId(1L)
        .username("testman")
        .password("1234")
        .email(email)
        .dna1(MusicDna.builder().name("rock").dnaId(1L).build())
        .dna2(MusicDna.builder().name("hip-hop").dnaId(2L).build())
        .dna3(MusicDna.builder().name("pop").dnaId(3L).build())
        .build();
    when(usersRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

    UserDetail userDetail = userService.getUserDetail(user.getUserId());

    Assertions.assertEquals("testman", userDetail.getUsername());
  }

  @Test
  @DisplayName("유저 생성 시 요청된 비밀번호와 비밀번호 확인이 다를 때 에러 처리")
  public void userSavePasswordUnMatchTest() {
    JoinRequest joinRequest = JoinRequest.builder()
        .name("testman")
        .password("1234")
        .checkPassword("1235")
        .email(email.getEmail())
        .build();

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> userService.userRegister(joinRequest));
  }

  @Test
  @DisplayName("유저 생성 시 이메일 인증 안되어 있을 때 에러 처리")
  public void userSaveEmailNotVerified() {
    JoinRequest joinRequest = JoinRequest.builder()
        .name("testman")
        .password("1234")
        .checkPassword("1234")
        .email(email.getEmail())
        .build();
    when(emailRepository.findById(email.getEmail())).thenReturn(Optional.of(email));
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> userService.userRegister(joinRequest));
  }

  @Test
  @DisplayName("유저 생성 시 이메일이 없을 때 에러 처리")
  public void userSaveEmailNotExistTest() {
    JoinRequest joinRequest = JoinRequest.builder()
        .name("testman")
        .password("1234")
        .checkPassword("1234")
        .email(email.getEmail())
        .build();
    when(emailRepository.findById(email.getEmail())).thenReturn(Optional.empty());
    Assertions.assertThrows(NotExistEmailException.class,
        () -> userService.userRegister(joinRequest));
  }

  @Test
  @DisplayName("유저 생성 시 패스워드 인코딩 확인")
  public void userSavePasswordEncodingTest() {
    email.setVerified(true);
    JoinRequest joinRequest = JoinRequest.builder()
        .name("testman")
        .password("1234")
        .checkPassword("1234")
        .email(email.getEmail())
        .build();
    when(emailRepository.findById(email.getEmail())).thenReturn(Optional.of(email));
    userService.userRegister(joinRequest);

    verify(passwordEncoder).encode("1234");
  }

  @Test
  @DisplayName("유저 생성 확인")
  public void userSaveTest() {
    email.setVerified(true);
    JoinRequest joinRequest = JoinRequest.builder()
        .name("testman")
        .password("1234")
        .checkPassword("1234")
        .email(email.getEmail())
        .build();
    when(emailRepository.findById(email.getEmail())).thenReturn(Optional.of(email));
    when(passwordEncoder.encode("1234")).thenReturn("12345678");
    Assertions.assertDoesNotThrow(() -> userService.userRegister(joinRequest));
    verify(usersRepository).save(any());
  }

  @Test
  @DisplayName("유저 로그인")
  public void userLoginTest() {
    User user = User.builder()
        .userId(1L)
        .username("testman")
        .password("encodedPassword")
        .email(email)
        .build();
    when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
    when(usersRepository.findByEmail_Email(email.getEmail())).thenReturn(Optional.of(user));
    when(tokenProvider.create(user)).thenReturn("accessToken");
    when(tokenProvider.createRefreshToken(user)).thenReturn("refreshToken");

    UserLoginDetail userLoginDetail = userService.getByCredentials(email.getEmail(), "password");

    Assertions.assertEquals("testman", userLoginDetail.getUsername());
    Assertions.assertEquals("accessToken", userLoginDetail.getToken());
    Assertions.assertEquals("refreshToken", userLoginDetail.getRefreshToken());
  }

  @Test
  @DisplayName("유저 이메일이 없을 때 예외 처리")
  public void userLoginEmailFailTest() {
    when(usersRepository.findByEmail_Email(email.getEmail())).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundUserException.class,
        () -> userService.getByCredentials(email.getEmail(), "password"));
  }

  @Test
  @DisplayName("유저 비밀번호가 다를 때 예외 처리")
  public void userLoginPasswordFailTest() {
    User user = User.builder()
        .userId(1L)
        .username("testman")
        .password("encodedPassword!")
        .email(email)
        .build();
    when(usersRepository.findByEmail_Email(email.getEmail())).thenReturn(Optional.of(user));
    when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> userService.getByCredentials(email.getEmail(), "password"));
  }

  @Test
  @DisplayName("리프레시 토큰으로 유저 정보 확인")
  public void userRefreshTokenTest() {
    User user = User.builder()
        .userId(1L)
        .username("testman")
        .password("encodedPassword")
        .email(email)
        .build();
    when(tokenProvider.validateRefreshTokenAndGetLongId("refreshToken")).thenReturn(1L);
    when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
    when(tokenProvider.create(user)).thenReturn("accessToken2");

    UserLoginDetail userLoginDetail = userService.getUserByRefreshToken("refreshToken");

    Assertions.assertEquals("testman", userLoginDetail.getUsername());
    Assertions.assertEquals("refreshToken", userLoginDetail.getRefreshToken());
    Assertions.assertEquals("accessToken2", userLoginDetail.getToken());
  }
}
