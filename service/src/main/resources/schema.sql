CREATE TABLE IF NOT EXISTS users
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(255)                            NOT NULL,
    email        VARCHAR(512)                            NOT NULL,
    private_mode BOOLEAN                                 NOT NULL,
    version      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS user_friends
(
    user_id   BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    CONSTRAINT fk_users_friends_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categories
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255)                            NOT NULL,
    version TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_categoy PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    category_id        BIGINT,
    state              VARCHAR(10)                             NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator_id       BIGINT,
    lat                BIGINT,
    lon                BIGINT,
    participant_limit  BIGINT,
    paid               BOOLEAN                                 NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    version            TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    status       VARCHAR(10)                             NOT NULL,
    version      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title   VARCHAR(255)                            NOT NULL,
    pinned  BOOLEAN                                 NOT NULL,
    version TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_compilations_events_compilation_id
        FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT fk_compilations_events_event_id
        FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);