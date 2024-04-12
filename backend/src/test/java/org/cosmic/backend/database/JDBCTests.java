package org.cosmic.backend.database;

import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;

import java.sql.*;

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
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "USER", "PASSWORD")) {
            log.info(con);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
