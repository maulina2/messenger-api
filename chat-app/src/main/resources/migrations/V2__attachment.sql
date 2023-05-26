alter table public.attachment

  add  CONSTRAINT attachment_message_fk
    FOREIGN KEY (message_id)  REFERENCES message (id)
