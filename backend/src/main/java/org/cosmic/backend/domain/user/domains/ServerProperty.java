package org.cosmic.backend.domain.user.domains;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class ServerProperty {

    private static String serverOrigin;

    @Value("${server.origin}")
    public void setServerOrigin(String origin) {
        serverOrigin = origin;
    }

    public static String getServerOrigin() {
        return serverOrigin;
    }
}