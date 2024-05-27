package org.cosmic.backend.domain.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.musicDNA.domain.MusicDna;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name="emails")
    private Email email;//fk
    @Column(nullable=false,length=255)
    private String username;
    @Column(nullable=false,length=255)
    private String password;
    @Builder.Default
    @Column(length=255)
    private String profilePicture="base";
    @Builder.Default
    @ElementCollection
    private List<String> MusicDNAs=new ArrayList<>();
    @Builder.Default
    @Column(nullable=false)
    private Instant signupDate=Instant.now();

}
