package org.cosmic.backend.domain.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
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
}
