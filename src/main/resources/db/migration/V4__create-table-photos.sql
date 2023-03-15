create table photos(

    id uuid default uuid_generate_v4() primary key,
    product_id uuid not null,
    filename varchar(255) not null,
    type varchar(255) not null,
    photo bytea not null,
    created_at timestamp not null,

    foreign key (product_id)
    references products (id)
    on delete set default
    on update set default

);