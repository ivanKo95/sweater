DELETE FROM sweater_test.public.message;

INSERT INTO sweater_test.public.message (id, text, tag, user_id)
VALUES (1, 'first', 'first-tag', 1),
       (2, 'second', 'second-tag', 1),
       (3, 'third', 'third-tag', 1),
       (4, 'fourth', 'second-tag', 2);

ALTER SEQUENCE hibernate_sequence RESTART WITH 10;