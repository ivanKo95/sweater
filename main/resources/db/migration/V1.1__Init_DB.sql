ALTER TABLE IF EXISTS message
    DROP CONSTRAINT IF EXISTS message_user_FK;

ALTER TABLE IF EXISTS user_role
    DROP CONSTRAINT IF EXISTS user_role_FK;

DROP TABLE IF EXISTS message CASCADE;
DROP TABLE IF EXISTS user_role CASCADE;
DROP TABLE IF EXISTS usr CASCADE;
DROP SEQUENCE IF EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

CREATE TABLE message
(
    id        int8          NOT NULL,
    file_name VARCHAR(255),
    tag       VARCHAR(255),
    text      VARCHAR(2048) NOT NULL,
    user_id   int8,
    PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id int8 NOT NULL,
    roles   VARCHAR(255)
);

CREATE TABLE usr
(
    id              int8         NOT NULL,
    activation_code VARCHAR(255),
    active          BOOLEAN      NOT NULL,
    email           VARCHAR(255),
    password        VARCHAR(255) NOT NULL,
    username        VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS message
    ADD CONSTRAINT message_user_FK
        FOREIGN KEY (user_id) REFERENCES usr;

ALTER TABLE IF EXISTS user_role
    ADD CONSTRAINT user_role_FK
        FOREIGN KEY (user_id) REFERENCES usr;
