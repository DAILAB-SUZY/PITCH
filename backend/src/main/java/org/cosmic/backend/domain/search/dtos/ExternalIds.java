package org.cosmic.backend.domain.search.dtos;

public record ExternalIds(
    String isrc,
    String ean,
    String upc
) {

}
