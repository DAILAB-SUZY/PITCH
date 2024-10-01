package org.cosmic.backend.domain.user.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="emails")
@EqualsAndHashCode()
public class Email {

    @Id
    @Column(name="email")
    private String email;
    @Builder.Default
    @Column
    private Boolean verified=false;
    @Column(nullable = false)
    private String verificationCode;
    @Builder.Default
    @Column(nullable=false)
    private Instant createTime=Instant.now();

    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                ", verified=" + verified +
                ", verificationCode='" + verificationCode + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}