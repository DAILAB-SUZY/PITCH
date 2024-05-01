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
@Table(name="`user`")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,length=255)
    private String email;
    @Column(nullable=false,length=255)
    private String username;
    @Column(nullable=false,length=255)
    private String password;
    @Column(length=255)
    private String ProfilePicture;
    @CreationTimestamp
    @Column(nullable=false)
    private Instant signupDate;

}
