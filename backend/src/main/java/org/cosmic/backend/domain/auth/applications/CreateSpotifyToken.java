package org.cosmic.backend.domain.auth.applications;

import static java.lang.System.getenv;

import java.io.IOException;
import java.util.Map;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

@Component
public class CreateSpotifyToken {

  Map<String, String> env = getenv();
  private String clientId = "09175c6ea56247fd9529c96cfb2b7104";
  private String clientSecret = "9a0ed0f031ba43058aad372a30d00897";

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
