DELETE
FROM USER_ROLE;
DELETE
FROM VOTE;
DELETE
FROM USERS;
DELETE
FROM MENU_ITEM;
DELETE
FROM MENU;
DELETE
FROM RESTAURANT;

INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}password'),
       ('tori.plaksunova@gmail.com', 'Viktoria', 'Plaksunova', '{noop}admin'),
       ('user3@mail.ru', 'User_Third', 'User_Last', '{noop}password3');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);

INSERT INTO RESTAURANT (TITLE, ADDRESS, PHONE)
VALUES ('Eataly', 'Kiyevskaya, 2, Moscow, 121059', 84956680679),
       ('Ra`men', 'Tsvetnoy Blvd 21, b. 7, Moscow, 127051', 89855508152);

INSERT INTO MENU (ACTUAL_DATE, RESTAURANT_ID)
VALUES (CURRENT_DATE(), 1),
       ('2020-11-29', 1),
       (CURRENT_DATE(), 2),
       ('2020-11-29', 2);

INSERT INTO MENU_ITEM (TITLE, PRICE, MENU_ID)
VALUES ('Capricciosa', 890, 1),
       ('Martini Fiero', 490, 1),
       ('Espresso', 90, 1),
       ('Chinotto', 90, 1),
       ('Cinque formaggi', 790, 2),
       ('Americano', 190, 2),
       ('Negroni', 590, 2),
       ('San Pellegrino', 90, 2),
       ('Miso siru', 390, 3),
       ('Gyoza', 590, 3),
       ('Milk Oolong', 590, 3),
       ('Tayaki', 190, 3),
       ('Takoyaki', 390, 4),
       ('Tom yam', 590, 4),
       ('Sencha', 590, 4);

INSERT INTO VOTE (USER_ID, ACTUAL_DATE, ACTUAL_TIME, RESTAURANT_ID)
VALUES (1, CURRENT_DATE, '10:00', 1),
       (2, CURRENT_DATE, '10:00', 2);