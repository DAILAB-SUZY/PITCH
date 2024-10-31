package org.cosmic.backend;

import com.redis.testcontainers.RedisContainer;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy;
import org.testcontainers.utility.DockerImageName;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({Creator.class, TestConfig.class})
public abstract class AbstractContainerBaseTest {

  private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:latest");
  static final RedisContainer REDIS_CONTAINER = new RedisContainer(REDIS_IMAGE)
      .withStartupCheckStrategy(new IsRunningStartupCheckStrategy());
  private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:latest");
  static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>(
      POSTGRES_IMAGE)
      .withStartupCheckStrategy(new IsRunningStartupCheckStrategy());
  @Autowired
  protected Creator creator;

  @BeforeAll
  static void beforeAll() {
    REDIS_CONTAINER.start();
    POSTGRES_SQL_CONTAINER.start();
  }

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
    registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
    registry.add("spring.redis.port", () -> REDIS_CONTAINER.getFirstMappedPort().toString());
  }
}

@Log4j2
class FirstTest extends AbstractContainerBaseTest {

  @Test
  @DisplayName("Postgresql 연결 확인")
  public void postgresqlTest() {
    Assertions.assertTrue(POSTGRES_SQL_CONTAINER.isRunning());
    Assertions.assertTrue(REDIS_CONTAINER.isRunning());
    log.info("Postgresql URL: {}", POSTGRES_SQL_CONTAINER.getJdbcUrl());
    log.info("Redis URL: {}", REDIS_CONTAINER.getHost());
  }
}