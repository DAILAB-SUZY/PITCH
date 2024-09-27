INSERT INTO "post_like" (post_id, user_id)
VALUES (
           (SELECT post_id FROM post WHERE content = '밤양갱 노래좋다'),
           (SELECT user_id FROM users WHERE email = 'test1@example.com')
       );

INSERT INTO POST_COMMENT (create_time, parent_comment_comment_id, post_id, update_time, user_id, content)
VALUES (
                 CURRENT_TIMESTAMP
       ,null
       ,(SELECT post_id FROM post WHERE content = '밤양갱 노래좋다')
       ,current_timestamp
       ,(SELECT user_id FROM users WHERE email = 'test1@example.com')
       ,'안녕'
       );

INSERT INTO POST_COMMENT (create_time, parent_comment_comment_id, post_id, update_time, user_id, content)
VALUES (
         CURRENT_TIMESTAMP
       ,1
       ,(SELECT post_id FROM post WHERE content = '밤양갱 노래좋다')
       ,current_timestamp
       ,(SELECT user_id FROM users WHERE email = 'test1@example.com')
       ,'배고프다'
       )