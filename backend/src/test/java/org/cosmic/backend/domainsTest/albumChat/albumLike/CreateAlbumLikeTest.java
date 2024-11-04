package org.cosmic.backend.domainsTest.albumChat.albumLike;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domainsTest.albumChat.AlbumChatBaseTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreateAlbumLikeTest extends AlbumChatBaseTest {

  private final String baseUrl = super.baseUrl;

  @Test
  @Order(1)
  public void albumLikeCreateTest() throws Exception {
    mockMvcHelper(HttpMethod.POST, urlGenerator.buildUrl(baseUrl + "/albumLike", params), null,
        validToken)
        .andExpect(status().isOk());
  }

  @Test
  @Order(2)
  public void albumLikesGiveTest() throws Exception {
    mockMvcHelper(HttpMethod.GET, urlGenerator.buildUrl(baseUrl + "/albumLike", params), null,
        validToken)
        .andExpect(status().isOk());
  }

  @Test
  @Order(3)
  public void albumlikeExistTest() throws Exception {
    mockMvcHelper(HttpMethod.POST, urlGenerator.buildUrl(baseUrl + "/albumLike", params), null,
        validToken)
        .andExpect(status().isConflict());
  }

  @Test
  @Transactional
  @Order(4)
  public void albumlikeDeleteTest() throws Exception {
    mockMvcHelper(HttpMethod.DELETE, urlGenerator.buildUrl(baseUrl + "/albumLike", params), null,
        validToken)
        .andExpect(status().isOk());
  }

}
