create table public._user
(
    id            uuid not null
        primary key,
    birth_date    timestamp,
    creation_date timestamp,
    login         varchar(255),
    name          varchar(255),
    password      varchar(255),
    patronymic    varchar(255),
    sex           varchar(255),
    surname       varchar(255)
);

alter table public._user
    owner to postgres;

