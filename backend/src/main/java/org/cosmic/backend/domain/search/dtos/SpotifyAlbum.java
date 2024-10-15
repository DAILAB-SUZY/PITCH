package org.cosmic.backend.domain.search.dtos;

import java.util.List;

public record SpotifyAlbum(
    String album_type,
    int total_tracks,
    List<String> available_markets,
    ExternalUrls external_urls,
    String href,
    String id,
    List<Image> images,
    String name,
    String release_date,
    String release_date_precision,
    Restrictions restrictions,
    String type,
    String uri,
    List<SpotifyArtist> artists,
    Tracks tracks,
    List<Copyright> copyrights,
    ExternalIds external_ids,
    List<String> genres,
    String label,
    int popularity
) {

}
