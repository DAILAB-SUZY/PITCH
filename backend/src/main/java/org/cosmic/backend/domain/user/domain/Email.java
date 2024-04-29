package org.cosmic.backend.domain.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Email {

    @Id
    private String email;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean Verified;
    @Column(nullable = false)
    private String verificationCode;
    @CreationTimestamp
    @Column(nullable=false)
    private Instant expirationTime;
}
