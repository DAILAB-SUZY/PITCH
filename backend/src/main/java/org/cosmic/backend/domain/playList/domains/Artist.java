package org.cosmic.backend.domain.playList.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="Artist")
public class Artist {//
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="artist_id")
    private Long artistId ;

    @Column(name="artist_name")
    private String artistName;

    //아티스트 1 앨범 N
    @OneToMany(mappedBy = "artist")
    @Builder.Default
    private List<Album>album=new ArrayList<>();

    @OneToOne(mappedBy = "artist")
    private FavoriteArtist favoriteArtist;

    //아티스트 1 트랙 N
    @OneToMany(mappedBy = "artist")
    @Builder.Default
    private List<Track>track=new ArrayList<>();

    public Artist(String name) {
        this.artistName = name;
    }
    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}