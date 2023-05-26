create table public.file_data
(
    id               uuid primary key not null,
    author_id        uuid,
    name             character varying(255),
    upload_date_time timestamp without time zone
);

