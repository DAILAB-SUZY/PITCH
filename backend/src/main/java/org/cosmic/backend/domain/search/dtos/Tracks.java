package org.cosmic.backend.domain.search.dtos;

import java.util.List;

public record Tracks(
    String href,
    int limit,
    String next,
    int offset,
    String previous,
    int total,
    List<TrackItem> items
) {

}
