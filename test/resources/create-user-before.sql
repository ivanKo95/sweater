DELETE FROM sweater_test.public.user_role;
DELETE FROM sweater_test.public.usr;

INSERT INTO sweater_test.public.usr(id, username, password, active)
VALUES (1, 'admin', '$2a$08$5oLAL7kHYVrMtNzOCOvwH.VXkp4BCPDO0FwyJ5giJdY0ttqlapd/m', true),
       (2, 'user', '$2a$08$32fGBP5yov5r3w6G5eEwBuaA5E8WwGwgrx.kZSo/WGg2/bOOWYB.S', true);

INSERT INTO sweater_test.public.user_role(user_id, roles)
VALUES (1, 'ADMIN'), (1, 'USER'),
       (2, 'USER');