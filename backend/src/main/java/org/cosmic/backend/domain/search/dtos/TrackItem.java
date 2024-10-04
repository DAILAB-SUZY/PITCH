package org.cosmic.backend.domain.search.dtos;

import java.util.List;

public record TrackItem(
    List<Artist> artists,
    List<String> available_markets,
    int disc_number,
    int duration_ms,
    boolean explicit,
    ExternalUrls external_urls,
    String href,
    String id,
    boolean is_playable,
    LinkedFrom linked_from,
    Restrictions restrictions,
    String name,
    String preview_url,
    int track_number,
    String type,
    String uri,
    boolean is_local
) {

}
