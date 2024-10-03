package org.cosmic.backend.domainsTest.albumPost.post;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.post.dtos.Post.CreatePost;
import org.cosmic.backend.domain.post.dtos.Post.UpdatePost;
import org.cosmic.backend.domainsTest.albumPost.AlbumPostBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {"/data/albumPost.sql",
    "/data/CreateCommentAndLike.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class PostTest extends AlbumPostBaseTest {

  final String baseUrl = "/api/album/post";

  @Autowired
  private AlbumRepository albumRepository;

  private long deletedPostId;

  @Test
  @Order(1)
  public void createPostTest() throws Exception {
    CreatePost createPost = CreatePost.builder()
        .content("밤양갱 노래 안좋다")
        .spotifyAlbumId("base")
        .build();
    mockMvcHelper(HttpMethod.POST, urlGenerator.buildUrl(baseUrl, params), createPost, validToken)
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @Order(2)
  public void searchPostByUserTest() throws Exception {
    params.put("userId", user.getUserId());
    params.put("page", 0);
    params.put("limit", 10);
    mockMvcHelper(HttpMethod.GET,
        urlGenerator.buildUrl(baseUrl + "?userId={userId}&page={page}&limit={limit}", params), null,
        validToken)
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @Order(3)
  public void searchPostByIdTest() throws Exception {
    Long postId = postRepository.findByUser_UserId(user.getUserId()).get(0).getPostId();
    params.put("postId", postId);
    mockMvcHelper(HttpMethod.GET, urlGenerator.buildUrl(baseUrl + "/{postId}", params), null,
        validToken)
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @Order(4)
  public void updatePostTest() throws Exception {
    UpdatePost updatePost = UpdatePost.builder()
        .content("밤양갱 노래 좋아졌다")
        .build();
    Long postId = postRepository.findByUser_UserId(user.getUserId()).get(0).getPostId();
    params.replace("postId", postId);
    mockMvcHelper(HttpMethod.POST, urlGenerator.buildUrl(baseUrl + "/{postId}", params), updatePost,
        validToken)
        .andExpect(status().isOk());
  }

  @Test
  @Order(5)
  public void deletePostTest() throws Exception {
    deletedPostId = postRepository.findByUser_UserId(user.getUserId()).get(0).getPostId();
    params.replace("postId", deletedPostId);
    mockMvcHelper(HttpMethod.DELETE, urlGenerator.buildUrl(baseUrl + "/{postId}", params), null,
        validToken)
        .andExpect(status().isOk());
  }

  @Test
  @Order(6)
  @DisplayName("Post삭제 시 Comment 삭제 테스트")
  public void deletePostCascadeCommentTest() throws Exception {
    params.replace("postId", deletedPostId);
    mockMvcHelper(HttpMethod.GET, urlGenerator.buildUrl("/api/album/post/{postId}/comment", params),
        null, validToken)
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(7)
  @DisplayName("Post삭제 시 Like 삭제 테스트")
  public void deletePostCascadeLikeTest() throws Exception {
    params.replace("postId", deletedPostId);
    mockMvcHelper(HttpMethod.GET, urlGenerator.buildUrl("/api/album/post/{postId}/like", params),
        null, validToken)
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}
