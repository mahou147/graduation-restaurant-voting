DROP TABLE IF EXISTS USER_ROLE;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    id               INTEGER                 AUTO_INCREMENT,
    email            VARCHAR(255)            NOT NULL,
    first_name       VARCHAR(255)            NOT NULL,
    last_name        VARCHAR(255)            NOT NULL,
    password         VARCHAR(255)            NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX user_unique_email_idx
    ON USERS (email);


CREATE TABLE USER_ROLE
(
    user_id INTEGER NOT NULL,
    role    VARCHAR(255),
    CONSTRAINT user_role_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);