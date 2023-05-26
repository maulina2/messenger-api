alter table public._user
add column email        varchar(255),
add column city         varchar(255),
add column phone_number varchar(255),
add column avatar       uuid;