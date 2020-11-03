create table IF NOT EXISTS articles
(
    id              varchar(255) not null
        constraint articles_pkey
        primary key,
    description     text,
    news_keywords   text[],
    image           varchar(255),
    article_html    text,
    front_url       varchar(255),
    title           text,
    photo           json,
    project         varchar(255),
    category        varchar(255),
    opinion_authors varchar(255),
    anons           text,
    publish_date    timestamp,
    parsed_date     timestamp with time zone
);

alter table articles
    owner to postgres;

create table IF NOT EXISTS roles
(
    role_name varchar(255) not null,
    id        serial       not null
);

alter table roles
    owner to postgres;

create table IF NOT EXISTS users
(
    login    varchar,
    email    varchar,
    password varchar,
    role_id  integer default 2,
    id       serial not null
);

alter table users
    owner to postgres;