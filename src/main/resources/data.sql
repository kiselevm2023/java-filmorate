MERGE INTO GENRES (GENRE_ID , GENRE_NAME)
VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

MERGE INTO RATINGS (RATING_ID, RATING_NAME, RATING_DESC)
VALUES
(1, 'G', 'Film has no age restrictions'),
(2, 'PG', 'Children are advised to watch the film with their parents'),
(3, 'PG-13', 'Viewing is not recommended for children under 13 years of age'),
(4, 'R', 'Persons under 17 years of age can watch the film only in the presence of an adult'),
(5, 'NC-17', 'Viewing is prohibited for persons under 18 years of age');


