alter table public.black_list
    alter column  blocked_user_added_date Type date,
    alter column  blocked_user_removal_date Type date;

alter table public.friend
    alter column  friend_added_date Type date,
    alter column  friend_removal_date Type date;