package org.cosmic.backend.domain.auth.applications;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static java.lang.System.getenv;

@Component
public class CreateSpotifyToken {
    Map<String,String> env=getenv();
    private String clientId=env.get("CLIENT_ID");
    private String clientSecret=env.get("CLIENT_SECRET");

    private SpotifyApi spotifyApi;

    // SpotifyApi 객체 초기화
    private void initSpotifyApi() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

    public String accesstoken() {
        if (spotifyApi == null) {
            initSpotifyApi();  // 프로퍼티가 주입된 후 SpotifyApi 초기화
        }

        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return spotifyApi.getAccessToken();

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
        }
    }
}
