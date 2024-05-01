package org.cosmic.backend.domains.mail;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.cosmic.backend.domain.mail.dto.EmailAddress;
import org.cosmic.backend.domains.DisableCSRFTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;

public class EmailControllerTest extends DisableCSRFTestBase {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void mailTest() throws MessagingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmailAddress> emailRequest = new HttpEntity<>(EmailAddress.builder().email("tester@spring.com").build(), headers);

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/mail/verify", emailRequest, Void.class);

        // Assert
        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];

        Assertions.assertEquals(1, receivedMessage.getAllRecipients().length);
        Assertions.assertEquals("tester@spring.com", receivedMessage.getAllRecipients()[0].toString());
        Assertions.assertEquals("test.sender@hotmail.com", receivedMessage.getFrom()[0].toString());
        Assertions.assertEquals("Message from Java Mail Sender", receivedMessage.getSubject());
        Assertions.assertEquals("123456", GreenMailUtil.getBody(receivedMessage));

    }
}
