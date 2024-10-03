INSERT INTO public.emails (email, create_time, verification_code, verified)
VALUES
    ('john@example.com', '2023-09-01 09:00:00', 'abc123', TRUE),
    ('jane@example.com', '2023-09-01 09:10:00', 'def456', TRUE),
    ('abc@example.com', '2023-09-01 09:20:00', 'ghi789', TRUE),
    ('fan@example.com', '2023-09-01 09:30:00', 'jkl012', TRUE),
    ('master@example.com', '2023-09-01 09:40:00', 'mno345', TRUE);

INSERT INTO public.music_dna (dna_id, name)
VALUES
    (1, 'Rock'),
    (2, 'Pop'),
    (3, 'Jazz'),
    (4, 'Electronic'),
    (5, 'Hip-hop'),
    (6, 'Classical'),
    (7, 'Blues'),
    (8, 'Reggae');

INSERT INTO public.users (user_id,create_time, password, profile_picture, username, dna1_id, dna2_id, dna3_id, dna4_id, email)
VALUES
    (1,'2023-09-01 10:00:00', '12345678', 'profile1.jpg', 'john_doe', 1, 2, 3, 4, 'john@example.com'),
    (2,'2023-09-02 11:00:00', 'securepass456', 'profile2.jpg', 'jane_doe', 2, 3, 4, 5, 'jane@example.com'),
    (3,'2023-09-03 12:00:00', 'mypassword789', 'profile3.jpg', 'user_abc', 3, 4, 5, 6, 'abc@example.com'),
    (4,'2023-09-04 13:00:00', 'pass1234', 'profile4.jpg', 'music_fan', 4, 5, 6, 7, 'fan@example.com'),
    (5,'2023-09-05 14:00:00', 'secret123', 'profile5.jpg', 'track_master', 5, 6, 7, 8, 'master@example.com');

INSERT INTO public.artist (artist_id,artist_name,artist_cover,spotify_artist_id)
VALUES
    (1,'John Band','https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FNew%2520Jeans%2528%25EC%259D%258C%25EB%25B0%2598%2529&psig=AOvVaw2Ez7FDosUoTnCd1DPfqkvA&ust=1727440454080000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOi-j6jP4IgDFQAAAAAdAAAAABAJ','6UbmqUEgjLA6jAcXwbM1Z9'),
    (2,'Jane Singer','https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FNew%2520Jeans%2528%25EC%259D%258C%25EB%25B0%2598%2529&psig=AOvVaw2Ez7FDosUoTnCd1DPfqkvA&ust=1727440454080000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOi-j6jP4IgDFQAAAAAdAAAAABAJ','6UbmqUEgjLA6jAcXwbM1Z9'),
    (3,'ABC Collective','https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FNew%2520Jeans%2528%25EC%259D%258C%25EB%25B0%2598%2529&psig=AOvVaw2Ez7FDosUoTnCd1DPfqkvA&ust=1727440454080000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOi-j6jP4IgDFQAAAAAdAAAAABAJ','6UbmqUEgjLA6jAcXwbM1Z9'),
    (4,'Fan Beats','https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FNew%2520Jeans%2528%25EC%259D%258C%25EB%25B0%2598%2529&psig=AOvVaw2Ez7FDosUoTnCd1DPfqkvA&ust=1727440454080000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOi-j6jP4IgDFQAAAAAdAAAAABAJ','6UbmqUEgjLA6jAcXwbM1Z9'),
    (5,'Master DJ','https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FNew%2520Jeans%2528%25EC%259D%258C%25EB%25B0%2598%2529&psig=AOvVaw2Ez7FDosUoTnCd1DPfqkvA&ust=1727440454080000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOi-j6jP4IgDFQAAAAAdAAAAABAJ','6UbmqUEgjLA6jAcXwbM1Z9');

INSERT INTO public.album (album_id,album_cover, created_date, title, artist_id,spotify_album_id)
VALUES
    (1,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ', '2023-09-01 12:00:00', 'Greatest Hits', 1,'64M6ah0SkkRsnPGtGiRAbb'),
    (2,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ', '2023-09-02 12:30:00', 'Sing Along', 2,'64M6ah0SkkRsnPGtGiRAbb'),
    (3,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ', '2023-09-03 13:00:00', 'Chill Vibes', 3,'64M6ah0SkkRsnPGtGiRAbb'),
    (4,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ', '2023-09-04 13:30:00', 'Fan Favorites', 4,'64M6ah0SkkRsnPGtGiRAbb'),
    (5,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ', '2023-09-05 14:00:00', 'Master Mix', 5,'64M6ah0SkkRsnPGtGiRAbb');

INSERT INTO public.track (track_id,title, album_id, artist_id,track_cover,spotify_track_id)
VALUES
    (1,'Hit Song 1', 1, 1,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX'),
    (2,'Hit Song 2', 1, 1,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX'),
    (3,'Sing Along 1', 2, 2,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX'),
    (4,'Sing Along 2', 2, 2,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX'),
    (5,'Chill Track 1', 3, 3,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX'),
    (6,'Chill Track 2', 3, 3,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX'),
    (7,'Fan Favorite 1', 4, 4,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX'),
    (8,'Fan Favorite 2', 4, 4,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX'),
    (9,'Master Beat 1', 5, 5,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX'),
    (10,'Master Beat 2', 5, 5,'https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2FHow%2520Sweet&psig=AOvVaw35qICCVABCRHPQ3HTstgdM&ust=1727438923943000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOD_2s3J4IgDFQAAAAAdAAAAABAJ','2fdwPdFrxlIm0jVKHVnxbX');

INSERT INTO public.playlist (update_time, user_id)
VALUES
    ('2024-09-06 15:00:00', 1),
    ('2024-09-07 16:00:00', 2),
    ('2024-09-08 17:00:00', 3),
    ('2024-09-26 18:00:00', 4),
    ('2024-09-26 19:00:00', 5);

INSERT INTO Playlist_Track (id, track_order, playlist_id, track_id)
VALUES
    (1, 1, 1, 1),
    (2, 2, 1, 2),
    (3, 1, 2, 3),
    (4, 2, 2, 4),
    (5, 1, 3, 5),
    (6, 2, 3, 6),
    (7, 1, 4, 7),
    (8, 2, 4, 8),
    (9, 1, 5, 9),
    (10, 2, 5, 10);