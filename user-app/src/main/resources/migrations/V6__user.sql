ALTER TABLE "users-db".public._user add constraint constraint_email UNIQUE (email),
                                    add constraint constraint_login UNIQUE (login);