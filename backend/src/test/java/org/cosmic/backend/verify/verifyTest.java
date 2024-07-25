package org.cosmic.backend.verify;

import org.assertj.core.api.Assertions;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.testcontainer.RepositoryBaseTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest
public class verifyTest {

@Autowired
    private EmailRepository emailRepository;
    @Autowired
    private UsersRepository usersRepository;
    private Email email;
    private User user=new User();

    /*@BeforeEach
    void setUp() {
        email=new Email();
        email.setEmail("kimjunho1231@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);
    }

    @AfterEach
    public void clean(){
        usersRepository.deleteAll();
        emailRepository.deleteAll();
    }*/

    @Test
    @DisplayName("간단한 json데이터파싱")//*
    void simpleDataParsing() {
        email=new Email();
        email.setEmail("kimjunho12315@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);
        String jsonString = "{\"email\": \"kimjunho12315@naver.com\","
                + "\"password\": \"1234\","
                + "\"name\": junho"
                + "}";

        JSONObject jsonObject=new JSONObject(jsonString);
        String emailID=jsonObject.getString("email");
        String password=jsonObject.getString("password");
        String name=jsonObject.getString("name");

        //만약 email테이블이 있다면 찾을 수 있겠지
        Email tempEmail= emailRepository.findByEmail(emailID).get();
        //findbyemail해서 repository에서 객체를 찾고 해당객체를 넣음

        emailRepository.save(tempEmail);
        user.setEmail(tempEmail);
        user.setUsername(name);
        user.setPassword(password);
        usersRepository.save(user);

        Assertions.assertThat(user.getEmail().getEmail()).isEqualTo("kimjunho12315@naver.com");//이메일이 일치하게 들어가야함
        Assertions.assertThat(user.getPassword()).isEqualTo("1234");//인증코드가 일치하게 들어가야함
        Assertions.assertThat(user.getUsername()).isEqualTo("junho");//인증코드가 일치하게 들어가야함

    }
    @Test
    @DisplayName("jsonArray파싱")//*
    void datasParsing() {
        email=new Email();
        email.setEmail("kimjunho12316@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);
        String jsonString =
                "{"
                        +   "\"posts\": ["
                        +       "{"
                        +           "\"email\": \"kimjunho12316@naver.com\","
                        +           "\"name\": \"junho\","
                        +           "\"password\": \"1234\""
                        +       "},"
                        +       "{"
                        +           "\"email\": \"kimjunho12317@google.co.kr\","
                        +           "\"name\": \"suhyeon\","
                        +           "\"password\": \"12345\""
                        +       "},"
                        +       "{"
                        +           "\"email\": \"kimjunho12318@nate.com\","
                        +           "\"name\": \"dongyul\","
                        +           "\"password\": \"123456\""
                        +       "},"
                        +   "]"
                        +"}";

        email=new Email();
        email.setEmail("kimjunho12317@google.co.kr");
        email.setVerificationCode("123456");
        emailRepository.save(email);

        email=new Email();
        email.setEmail("kimjunho12318@nate.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);
        JSONObject jsonObject=new JSONObject(jsonString);
        JSONArray jsonArray=jsonObject.getJSONArray("posts");

        // 배열의 모든 아이템을 출력합니다.
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String emailID=obj.getString("email");
            String password=obj.getString("password");
            String name=obj.getString("name");
            System.out.println(emailID);
            System.out.println(password);
            System.out.println(name);
            Email tempEmail= emailRepository.findByEmail(emailID).get();
            //findbyemail해서 repository에서 객체를 찾고 해당객체를 넣음

            emailRepository.save(tempEmail);
            user.setEmail(tempEmail);
            user.setUsername(name);
            user.setPassword(password);
            usersRepository.save(user);
        }
        Assertions.assertThat(user.getEmail().getEmail()).isEqualTo("kimjunho12318@nate.com");//이메일이 일치하게 들어가야함
        Assertions.assertThat(user.getPassword()).isEqualTo("123456");//인증코드가 일치하게 들어가야함
        Assertions.assertThat(user.getUsername()).isEqualTo("dongyul");//인증코드가 일치하게 들어가야함

    }
    @Test
    @DisplayName("null값 없는지 확인")
    void checkNull() {
        email=new Email();
        email.setEmail("kimjunho1231@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);
        String jsonString = "{\"email\": \"kimjunho1231@naver.com\","
                + "\"password\": \"1234\","
                + "\"checkpassword\": \"\","
                + "\"name\": junho"
                + "}";

        JSONObject jsonObject = new JSONObject(jsonString);
        String emailID = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String checkPassword = jsonObject.getString("checkpassword");
        String name = jsonObject.getString("name");
        //값에 넣기전에 NULL인지부터 확인함.

        assertThat(emailID).isNotNull();
        assertThat(password).isNotNull();
        assertThat(checkPassword).isNotNull();
        assertThat(name).isNotNull();
        //여기서 하나라도 오류뜨면 오류보낸다.
        //다 통과하면 다음 조건 이동
    }
    @Test
    @DisplayName("모든 조건 충족하는지")
    void checkCondition() {
        email=new Email();
        email.setEmail("kimjunho1231@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);
        String jsonString = "{\"email\": \"kimjunho1231@naver.com\","
                + "\"password\": \"1234\","
                + "\"checkpassword\": \"\","
                + "\"name\": junho"
                + "}";

        email.setVerified(true);

        JSONObject jsonObject = new JSONObject(jsonString);
        //Email에 있는 email과 verified가 true인지
        String emailID=jsonObject.getString("email");
        if(!email.getEmail().equals(emailID) && email.getVerified()!=true)
        {
            //해당 이메일이 없거나 인증된게아니라면
        }
        String password = jsonObject.getString("password");
        String checkPassword = jsonObject.getString("checkpassword");
        if(!password.equals(checkPassword))
        {
            //확인한 비번과 다르다면
        }
        String name = jsonObject.getString("name");
    }
}
