-- Email 테이블에 데이터 삽입
INSERT INTO emails (email, verified, verification_code, create_time)
VALUES ('kimjunho@naver.com', true, '1234', CURRENT_TIMESTAMP);

-- User 테이블에 데이터 삽입
INSERT INTO users (email, username, password, profile_picture, create_time)
VALUES (
           'kimjunho@naver.com',  -- 외래키로 참조되는 email 값
           'junho',
           '1234',
           'base',
           CURRENT_TIMESTAMP
       );