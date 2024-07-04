package org.cosmic.backend.domains.login;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class SearchPwTest {

    @Test
    public void testSearchPw() throws Exception {
        //이메일을 보내면 인증 절차 진행(그러면 이메일 보내고 받는 테이블 하나 더 있어야할듯)->
        //이메일 확인 버튼 누르면 인증 확인 진행
        //확인 완료면 200
    }

}
