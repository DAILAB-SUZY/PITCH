package org.cosmic.backend.domain.musicDNA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDNA {

    private Long key;//userkey
    private String emotion;
}
