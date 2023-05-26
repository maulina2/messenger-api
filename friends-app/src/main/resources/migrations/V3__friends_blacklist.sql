alter table public.black_list
    rename column friend_added_date to blocked_user_added_date;

alter table public.black_list
    rename column
        friend_removal_date to blocked_user_removal_date;