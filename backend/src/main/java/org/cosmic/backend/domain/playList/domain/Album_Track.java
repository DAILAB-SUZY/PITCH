package org.cosmic.backend.domain.playList.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`Album_Track`")
public class Album_Track {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="trackId")
    private Track track;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="albumId")
    private Album album;

    public Album_Track(Track track, Album album) {
        this.track = track;
        this.album = album;
    }
}
