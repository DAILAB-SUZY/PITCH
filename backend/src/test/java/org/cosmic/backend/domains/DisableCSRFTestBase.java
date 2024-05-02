package org.cosmic.backend.domains;

import org.cosmic.backend.configs.SecurityTestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SecurityTestConfig.class)
public abstract class DisableCSRFTestBase {

}
