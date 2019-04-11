CREATE TABLE manager
(
    id           INTEGER      NOT NULL AUTO_INCREMENT,
    first_name   VARCHAR(128) NOT NULL,
    middle_name  VARCHAR(128) NOT NULL,
    last_name    VARCHAR(128) NOT NULL,
    phone        VARCHAR(20),
    alternate_id INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (alternate_id) REFERENCES manager (id)
);

CREATE TABLE client
(
    id          INTEGER      NOT NULL AUTO_INCREMENT,
    name        VARCHAR(128) NOT NULL,
    address     VARCHAR(255) NOT NULL,
    manager_id  INTEGER      NOT NULL,
    deleted     BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (manager_id) REFERENCES manager (id)
);