CREATE TABLE if not exists item
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR NOT NULL,
    description     VARCHAR NOT NULL,
    created         TIMESTAMP NOT NULL,
    done            BOOLEAN NOT NULL
);
