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

INSERT INTO music_Dna (emotion) VALUES ('lazy');
INSERT INTO music_Dna (emotion) VALUES ('funny');
INSERT INTO music_Dna (emotion) VALUES ('quiet');
INSERT INTO music_Dna (emotion) VALUES ('beautiful');