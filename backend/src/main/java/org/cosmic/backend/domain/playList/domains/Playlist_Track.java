package org.cosmic.backend.domain.playList.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="Playlist_Track")
public class Playlist_Track {//N:M을 이어줄 연결다리

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="playlistId")
    private Playlist playlist;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="trackId")
    private Track track;

    //TODO: order 관련 서비스를 다시 만들어야 함
    @Builder.Default
    @Column(name="track_order")
    private Integer trackOrder = 0;

    public static PlaylistDetail toGiveDetail(Playlist_Track playlist_track) {
        return PlaylistDetail.builder()
                .playlistId(playlist_track.getPlaylist().getPlaylistId())
                .trackId(playlist_track.getTrack().getTrackId())
                .title(playlist_track.getTrack().getTitle())
                .artistName(playlist_track.getTrack().getArtist().getArtistName())
                .trackCover(playlist_track.getTrack().getTrackCover())
                .build();
    }
}