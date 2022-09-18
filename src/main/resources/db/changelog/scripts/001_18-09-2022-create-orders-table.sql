-- order is keyword into postgres
create table if not exists public.orders
(
    id    bigserial primary key,
    name  text      not null unique,
    price bigserial not null
);
