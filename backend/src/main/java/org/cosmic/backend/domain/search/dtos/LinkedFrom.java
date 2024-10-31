package org.cosmic.backend.domain.search.dtos;

public record LinkedFrom(
    ExternalUrls external_urls,
    String href,
    String id,
    String type,
    String uri
) {

}
