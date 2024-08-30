-- Email 테이블에 데이터 삽입
INSERT INTO emails (email, verified, verification_code, create_time)
VALUES ('test1@example.com', true, '12345678', CURRENT_TIMESTAMP);

-- User 테이블에 데이터 삽입
INSERT INTO users (email, username, password, profile_picture, signup_date)
VALUES (
           'test1@example.com',  -- 외래키로 참조되는 email 값
           'testboy',
           '12345678',
           'base',
           CURRENT_TIMESTAMP
       );

-- Email 테이블에 데이터 삽입
INSERT INTO emails (email, verified, verification_code, create_time)
VALUES ('test2@example.com', true, '12345678', CURRENT_TIMESTAMP);

-- User 테이블에 데이터 삽입
INSERT INTO users (email, username, password, profile_picture, signup_date)
VALUES (
           'test2@example.com',  -- 외래키로 참조되는 email 값
           'testgirl',
           '12345678',
           'base',
           CURRENT_TIMESTAMP
       );

INSERT INTO Artist(artist_name)VALUES('bibi');

INSERT INTO Album(title,cover,artist_id,genre,created_date)
VALUES(
          'bam',
          'base',
          (SELECT Artist.artist_id FROM Artist WHERE artist_name='bibi'),
          'balad',
          CURRENT_TIMESTAMP
      );

INSERT INTO album_chat(title,cover,genre,create_time,album_id,artist_name)
VALUES(
          'bam',
          'base',
          'balad',
          CURRENT_TIMESTAMP,
          (SELECT Album.album_id FROM Album WHERE title='bam'),
          'bibi'
      );