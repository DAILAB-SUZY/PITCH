package org.cosmic.backend.domain.user.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.domain.mail.utils.ApacheMathRandomCodeGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "emails")
@EqualsAndHashCode()
public class Email {

  @Id
  @Column(name = "email")
  private String email;
  @Builder.Default
  @Column
  private Boolean verified = false;
  @Column(nullable = false)
  @Builder.Default
  private String verificationCode = ApacheMathRandomCodeGenerator.getRandomCode();
  @Builder.Default
  @Column(nullable = false)
  private Instant createTime = Instant.now();

  @OneToOne(mappedBy = "email")
  private User user;

  public static Email from(String mailAddress) {
    return Email.builder()
        .email(mailAddress)
        .build();
  }

  @Override
  public String toString() {
    return "Email{" +
        "email='" + email + '\'' +
        ", verified=" + verified +
        ", verificationCode='" + verificationCode + '\'' +
        ", createTime=" + createTime +
        '}';
  }

  private void durationCheck(Instant now) {
    if (Duration.between(createTime, now).toSeconds() < 30) {
      throw new IntervalNotEnoughException();
    }
  }

  public boolean isUserExist() {
    return getUser() != null;
  }

  public void updateVerificationCode() {
    durationCheck(Instant.now());
    setVerificationCode(ApacheMathRandomCodeGenerator.getRandomCode());
  }

  public void validVerificationCode(String code) {
    if (!getVerificationCode().equals(code)) {
      throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
    }
    setVerified(true);
  }

  public void checkVerified() {
    if (!verified) {
      throw new IllegalArgumentException("이메일 인증이 필요합니다.");
    }
  }
}