package org.cosmic.backend;

import com.redis.testcontainers.RedisContainer;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractContainerBaseTest.DataSourceInitializer.class)
public abstract class AbstractContainerBaseTest {

  private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:latest");
  @Container
  static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>(
      POSTGRES_IMAGE)
      .withStartupCheckStrategy(new IsRunningStartupCheckStrategy());
  private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:latest");
  @Container
  static final RedisContainer REDIS_CONTAINER = new RedisContainer(REDIS_IMAGE)
      .withStartupCheckStrategy(new IsRunningStartupCheckStrategy());

  static {
    POSTGRES_SQL_CONTAINER.start();
    REDIS_CONTAINER.start();
  }

  public static class DataSourceInitializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    /*
       `initialize` function allows us to set properties dynamically. Since the DataSource is initialized dynamically,
        we need to set url, username, and password that is provided/set by the testcontainers.
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
          applicationContext,
          "spring.test.database.replace=none",
          // Tells Spring Boot not to start in-memory db for tests.
          "spring.datasource.url=" + POSTGRES_SQL_CONTAINER.getJdbcUrl(),
          "spring.datasource.username=" + POSTGRES_SQL_CONTAINER.getUsername(),
          "spring.datasource.password=" + POSTGRES_SQL_CONTAINER.getPassword(),
          "spring.redis.host=" + REDIS_CONTAINER.getHost(),
          "spring.redis.port=" + REDIS_CONTAINER.getFirstMappedPort()
      );
    }
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