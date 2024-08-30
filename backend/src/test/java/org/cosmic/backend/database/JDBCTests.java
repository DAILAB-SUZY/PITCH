package org.cosmic.backend.database;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
public class JDBCTests {
    static{

        try{
            Class.forName("org.postgresql.Driver");
        }
        catch(Exception e){
            e.fillInStackTrace();
        }
    }

    @Test
    public void testConnection() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:9000/pitch",
            "myuser", "secret")) {
            log.info(con);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
