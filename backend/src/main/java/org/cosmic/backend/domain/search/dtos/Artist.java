package org.cosmic.backend.domain.search.dtos;

import java.util.List;

public record Artist(
    ExternalUrls external_urls,
    Followers followers,
    List<String> genres,
    String href,
    String id,
    List<Image> images,
    String name,
    Long popularity,
    String type,
    String uri
) {

}
