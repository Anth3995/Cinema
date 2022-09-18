create table if not exists public.orders_movie
(
    id       bigserial primary key,
    order_id bigserial references public.orders (id),
    movie_id bigserial references public.movie (id)
);

create index if not exists order_movie_order_id_idx on public.orders_movie (order_id);
create index if not exists order_movie_movie_id_idx on public.orders_movie (movie_id);
