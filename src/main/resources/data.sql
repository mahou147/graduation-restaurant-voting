DELETE
FROM USER_ROLE;
DELETE
FROM VOTE;
DELETE
FROM USERS;
DELETE
FROM DISH;
DELETE
FROM MENU;
DELETE
FROM RESTAURANT;

INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '{noop}password'),
       ('tori.plaksunova@gmail.com', 'Viktoria', 'Plaksunova', '{noop}admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (TITLE, ADDRESS, PHONE)
VALUES ('Eataly', 'Kiyevskaya, 2, Moscow, 121059', 84956680679),
       ('Ra`men', 'Tsvetnoy Blvd 21, b. 7, Moscow, 127051', 89855508152);

INSERT INTO MENU (DATE, RESTAURANT_ID)
VALUES (CURRENT_DATE(), 1),
       ('2020-11-29', 1),
       ('2020-11-30', 1),
       ('2020-12-01', 1),
       ('2020-12-02', 1),
       ('2020-12-03', 1),
       (CURRENT_DATE(), 2),
       ('2020-11-29', 2),
       ('2020-11-30', 2),
       ('2020-12-01', 2);

INSERT INTO DISH (TITLE, PRICE, MENU_ID)
VALUES ('Capricciosa', 890, 1),
       ('Martini Fiero', 490, 1),
       ('Espresso', 90, 1),
       ('Chinotto', 90, 1),
       ('Cinque formaggi', 790, 2),
       ('Americano', 190, 2),
       ('Negroni', 590, 2),
       ('San Pellegrino', 90, 2),
       ('Salad Caprese', 590, 3),
       ('Crema di zucca', 490, 3),
       ('Focaccia al rosmarino', 190, 3),
       ('Aperol Spritz', 490, 3),
       ('Tagliere di salumi', 990, 4),
       ('Spagetti Frutti di mare', 990, 4),
       ('Conole', 390, 4),
       ('Birra del borgo', 290, 4),
       ('Bruscette al salmone', 390, 5),
       ('Verdure alla griglia', 490, 5),
       ('Tiramisu', 290, 5),
       ('Cappuccino', 190, 5),
       ('Lamura Rose', 390, 5),
       ('T-Bone steak', 4990, 6),
       ('Nero D`avola', 890, 6),
       ('Tagliere di formaggi', 990, 6),
       ('Lurisia', 90, 6),
       ('Miso siru', 390, 7),
       ('Gyoza', 590, 7),
       ('Mealk Oolong', 590, 7),
       ('Tayaki', 190, 7),
       ('Takoyaki', 390, 8),
       ('Tom yam', 590, 8),
       ('Sencha', 590, 8),
       ('Okonomiyaki', 390, 9),
       ('Fo bo', 590, 9),
       ('Coke', 190, 9),
       ('Tataki salmon', 590, 10),
       ('Onigiri', 190, 10),
       ('Asahi', 490, 10),
       ('Sushi set', 990, 10);

INSERT INTO VOTE (USER_ID, DATE, TIME, RESTAURANT_ID)
VALUES (1, '2020-11-29', '10:00', 1),
       (2, '2020-11-29', '10:00', 1),
       (1, '2020-12-30', '10:00', 2),
       (2, '2020-12-30', '10:00', 2),
       (1, CURRENT_DATE, '10:00', 1),
       (2, CURRENT_DATE, '10:00', 2);