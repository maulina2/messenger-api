create table public.notification
(
    id                uuid not null
        primary key,
    notification_type varchar(255),
    read_date         date,
    receiving_date    timestamp,
    text              varchar(255),
    user_id           uuid
);

alter table public.notification
    owner to postgres;

