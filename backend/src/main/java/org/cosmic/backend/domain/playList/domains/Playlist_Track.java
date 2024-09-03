package org.cosmic.backend.domain.playList.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.dtos.PlaylistGiveDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`Playlist_Track`")
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

    public Playlist_Track(Track track, Playlist playlist) {
        this.track = track;
        this.playlist = playlist;
    }

    public static PlaylistGiveDto toGiveDto(Playlist_Track playlist_track) {
        return PlaylistGiveDto.builder()
                .playlistId(playlist_track.getPlaylist().getPlaylistId())
                .trackId(playlist_track.getTrack().getTrackId())
                .userId(playlist_track.getPlaylist().getUser().getUserId())
                .title(playlist_track.getTrack().getTitle())
                .artistName(playlist_track.getTrack().getArtist().getArtistName())
                .build();
    }
}