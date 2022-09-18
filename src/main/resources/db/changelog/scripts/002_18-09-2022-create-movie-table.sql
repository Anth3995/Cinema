create table if not exists public.movie
(
    id          bigserial primary key,
    name        text not null unique,
    description text,
    director    text not null
);
