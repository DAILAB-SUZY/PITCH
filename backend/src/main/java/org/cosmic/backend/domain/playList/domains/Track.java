package org.cosmic.backend.domain.playList.domains;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.dtos.TrackGiveDto;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="Track")
public class Track {//여기엔 모든 노래들이 담길 것임. 담길 때 앨범도 같이
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="track_id")
    private Long trackId ;

    @Column(nullable=false)
    private String genre;

    @Column(nullable=false)
    private String title;//곡 제목

    @Builder.Default
    @Column(nullable=false)
    private String Cover="base";//곡 제목

    @Builder.Default
    @Column(nullable=false,name="created_date")
    private Instant createdDate=Instant.now();//발매 일


    @OneToMany(mappedBy = "track")
    private List<Playlist_Track> playlist_track;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="album_id")
    private Album album;

    //아티스트와 1:N관계
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="artist_id")
    private Artist artist;

    public Track(String genre, String title,String cover, Artist artist, Instant createdDate,Album album){
        this.genre = genre;
        this.title = title;
        this.artist = artist;
        this.Cover=cover;
        this.createdDate = createdDate;
        this.album = album;
    }

    public static TrackGiveDto toTrackGiveDto(Track track) {
        return TrackGiveDto.builder()
                .title(track.getTitle())
                .artistName(track.getArtist().getArtistName())
                .cover(track.getCover())
                .build();
    }
}