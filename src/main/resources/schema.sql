create table if not exists users
(
    user_id  bigserial
        primary key,
    email    varchar(255),
    password varchar(255),
    username varchar(255)
        constraint uk_r43af9ap4edm43mmtq01oddj6
            unique
);

alter table users
    owner to postgres;

create table if not exists notes
(
    note_id bigserial
        primary key,
    date    date    default now(),
    done    boolean default false,
    task    varchar(255),
    user_id bigint
        constraint fkechaouoa6kus6k1dpix1u91c
            references users
            on delete cascade
);

alter table notes
    owner to postgres;

create table if not exists authorities
(
    id        bigserial
        primary key,
    authority varchar(255),
    username  bigint
        constraint fkhjuy9y4fd8v5m3klig05ktofg
            references users
);

alter table authorities
    owner to postgres;
