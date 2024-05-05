package org.cosmic.backend.domain.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`user`")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name="email")
    private Email email;//fk
    @Column(nullable=false,length=255)
    private String username;
    @Column(nullable=false,length=255)
    private String password;
    @Builder.Default
    @Column(length=255)
    private String profilePicture="base";
    @Builder.Default
    @Column(nullable=false)
    private Instant signupDate=Instant.now();

}
