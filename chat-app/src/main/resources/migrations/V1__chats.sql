create table public.attachment
(
    id         uuid not null
        primary key,
    file_id    uuid,
    message_id uuid,
    name       varchar(255)
);

create table public.chat
(
    id                 uuid not null
        primary key,
    admin              uuid,
    avatar_id          uuid,
    chat_creation_date timestamp,
    chat_type          integer,
    name               varchar(255)
);

create table public.chat_user
(
    id      uuid not null
        primary key,
    chat_id uuid,
    user_id uuid
);

create table public.message
(
    id        uuid not null
        primary key,
    send_date timestamp,
    text      varchar(255),
    chat_id   uuid not null
        constraint chat_id
            references public.chat
);

