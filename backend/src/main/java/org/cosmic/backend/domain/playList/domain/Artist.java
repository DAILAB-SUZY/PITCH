package org.cosmic.backend.domain.playList.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`Artist`")
public class Artist {//
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="artistId")
    private Long artistId ;

    @Column(length=255,name="artistName")
    private String artistName;

    //아티스트 1 앨범 N
    @OneToMany(mappedBy = "artist")
    @Builder.Default
    private List<Album>album=new ArrayList<>();

    //아티스트 1 트랙 N
    @OneToMany(mappedBy = "artist")
    @Builder.Default
    private List<Track>track=new ArrayList<>();

    public Artist(String name) {
        this.artistName = name;
    }
}