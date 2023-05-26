create table public.black_list
(
    id                  uuid not null
        primary key,
    external_user       uuid,
    friend_added_date   timestamp,
    friend_removal_date timestamp,
    name                varchar(255),
    patronymic          varchar(255),
    surname             varchar(255),
    target_user         uuid
);

alter table public.black_list
    owner to postgres;

create table public.friend
(
    id                  uuid not null
        primary key,
    external_user       uuid,
    friend_added_date   timestamp,
    friend_removal_date timestamp,
    name                varchar(255),
    patronymic          varchar(255),
    surname             varchar(255),
    target_user         uuid
);

alter table public.friend
    owner to postgres;

