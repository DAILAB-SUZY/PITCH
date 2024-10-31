package org.cosmic.backend.globals.configs;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class YoutubeConfig {

    private final String youtubeKey = System.getenv("YOUTUBE_KEY"); // 환경변수에서 API 키 가져오기

    @Bean
    public YouTube youtube() throws GeneralSecurityException, IOException {
        return new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                request -> {
                } // 인증이 필요하지 않은 경우 null 대신 빈 RequestInitializer 사용
        ).setApplicationName("youtube-playlist-service")
                .build();
    }
}