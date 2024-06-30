INSERT INTO emails (create_time, vertification_code, verified, email) VALUES (NOW(), "123456", TRUE, "test@example.com");

INSERT INTO user (emails, password, profile_picture, signup_date, username) VALUES ("test@example.com", "123456", "none", NOW(), "testman");
