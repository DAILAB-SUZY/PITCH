package org.cosmic.backend.domain.bestAlbum.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BestAlbumListRequest {

  private List<BestAlbumRequest> bestalbum;

  public static BestAlbumListRequest createBestAlbumListDto(List<BestAlbumRequest> bestalbum) {
    return BestAlbumListRequest.builder()
        .bestalbum(bestalbum)
        .build();
  }
}
