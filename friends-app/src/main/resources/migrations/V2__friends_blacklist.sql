alter table public.black_list
  drop column name,
  drop column surname,
  drop column patronymic,
    add column full_name varchar(255);

alter table public.friend
    drop column name,
    drop column surname,
    drop column patronymic,
    add column full_name varchar(255);