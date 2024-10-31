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

-- Email 테이블에 데이터 삽입
INSERT INTO emails (email, verified, verification_code, create_time)
VALUES ('test2@example.com', true, '12345678', CURRENT_TIMESTAMP);

-- User 테이블에 데이터 삽입
INSERT INTO users (email, username, password, profile_picture, create_time)
VALUES (
           'test2@example.com',  -- 외래키로 참조되는 email 값
           'testgirl',
           '12345678',
           'base',
           CURRENT_TIMESTAMP
       );

-- Artist 테이블에 데이터 삽입
INSERT INTO Artist(artist_name,artist_cover,spotify_artist_id) VALUES('bibi','base','base');

-- Album 테이블에 데이터 삽입
INSERT INTO Album(title,album_cover, artist_id, created_date,spotify_album_id)
VALUES (
           'bam',
            'base',
           (SELECT Artist.artist_id FROM Artist WHERE artist_name='bibi'),
           CURRENT_TIMESTAMP,
            'base'
       );

-- AlbumChatComment 데이터 삽입
INSERT INTO album_chat_comment(content, album_id, user_id)
VALUES (
           'HI',
           (SELECT album.album_id FROM album WHERE album.title = 'bam'),
           (SELECT users.user_id FROM users WHERE users.email = 'test1@example.com')
       );

INSERT INTO album_chat_comment(content, album_id, user_id)
VALUES (
           'Heloo',
           (SELECT album.album_id FROM album WHERE album.title = 'bam'),
           (SELECT users.user_id FROM users WHERE users.email = 'test2@example.com')
       );
