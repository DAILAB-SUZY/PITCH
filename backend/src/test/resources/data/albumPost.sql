-- Email 테이블에 데이터 삽입
INSERT INTO emails (email, verified, verification_code, create_time)
VALUES ('test1@example.com', true, '12345678', CURRENT_TIMESTAMP);

-- User 테이블에 데이터 삽입
INSERT INTO users (email, username, password, profile_picture, create_time)
VALUES (
           'test1@example.com',  -- 외래키로 참조되는 email 값
           'testboy',
           '12345678',
           'base',
           CURRENT_TIMESTAMP
       );

INSERT INTO Artist(artist_name)VALUES('bibi');

INSERT INTO Album(title,album_cover,artist_id,created_date)
VALUES(
          'bam',
          'base',
          (SELECT Artist.artist_id FROM Artist WHERE artist_name='bibi'),
          CURRENT_TIMESTAMP
      );

INSERT INTO Track(title,album_id,artist_id)
VALUES(
          'bam',
          (SELECT Album.album_id FROM Album WHERE title='bam'),
          (SELECT Artist.artist_id FROM Artist WHERE Artist.artist_name='bibi')
      );

INSERT INTO post(content, user_id, album_id)
VALUES (
        '밤양갱 노래좋다',
        (SELECT users.user_id FROM users where email = 'test1@example.com'),
        (SELECT album.album_id FROM album where title = 'bam')
       );