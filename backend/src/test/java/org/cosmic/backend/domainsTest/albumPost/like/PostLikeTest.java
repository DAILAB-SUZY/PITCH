package org.cosmic.backend.domainsTest.albumPost.like;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domainsTest.albumPost.AlbumPostBaseTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class PostLikeTest extends AlbumPostBaseTest {
    final String baseUrl = super.baseUrl + "/like";

    @Test
    @Order(1)
    public void createLikeTest() throws Exception {
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl, params),null,validToken)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void giveLikeTest() throws Exception {
        mockMvcHelper(HttpMethod.GET,urlGenerator.buildUrl(baseUrl, params),null,validToken)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void deleteLikeTest() throws Exception {
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl, params),null,validToken)
                .andDo(print())
                .andExpect(status().isOk());
    }
}
