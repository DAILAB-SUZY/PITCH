package org.cosmic.backend.testcontainer;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class PostgreSQLTestContainerTests extends RepositoryBaseTest{
    @Test
    public void testConnect(){
        assertEquals(postgres.getContainerInfo().getState().getStatus(), "running");
    }
}
