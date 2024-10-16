package org.cosmic.backend.domain.playList.dtos;

import java.util.List;

public record PlaylistAndRecommendDetail(
    List<PlaylistDetail> tracks,
    List<PlaylistDetail> recommends
) {

}
