DROP TABLE IF EXISTS VOTE;
DROP TABLE IF EXISTS USER_ROLE;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS MENU_ITEM;
DROP TABLE IF EXISTS MENU;
DROP TABLE IF EXISTS RESTAURANT;

CREATE TABLE USERS
(
    id               INTEGER            AUTO_INCREMENT,
    email            VARCHAR            NOT NULL,
    first_name       VARCHAR            NOT NULL,
    last_name        VARCHAR            NOT NULL,
    enabled          BOOL               DEFAULT TRUE  NOT NULL,
    registered       TIMESTAMP          DEFAULT now() NOT NULL,
    password         VARCHAR            NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX user_unique_email_idx
    ON USERS (email);


CREATE TABLE USER_ROLE
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_role_unique UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE RESTAURANT
(
    id          INTEGER      AUTO_INCREMENT,
    title       VARCHAR      NOT NULL,
    address     VARCHAR      NOT NULL,
    phone       VARCHAR      NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX restaurant_unique_address_idx
    ON RESTAURANT (title, address);

CREATE TABLE MENU
(
    id                INTEGER                    AUTO_INCREMENT,
    actual_date       DATE         DEFAULT now() NOT NULL,
    restaurant_id     INTEGER                    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX unique_menu_of_the_day
    ON MENU (actual_date, restaurant_id);

CREATE TABLE MENU_ITEM
(
    id                INTEGER                    AUTO_INCREMENT,
    title             VARCHAR                    NOT NULL,
    price             DECIMAL                    NOT NULL,
    menu_id           INTEGER                    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (menu_id) REFERENCES MENU (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX menu_item_unique_title_idx
    ON MENU_ITEM (menu_id, title);

CREATE TABLE VOTE
(
    id                INTEGER       AUTO_INCREMENT,
    actual_date        DATE          NOT NULL DEFAULT CURRENT_DATE ON UPDATE CURRENT_DATE,
    actual_time        TIME          NOT NULL,
    user_id           INTEGER       NOT NULL,
    restaurant_id     INTEGER       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)       REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX user_unique_vote_daily_idx
    ON VOTE (user_id, actual_date);