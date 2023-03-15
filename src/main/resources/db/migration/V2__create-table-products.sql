create extension if not exists "uuid-ossp";

create table products(

    id uuid default uuid_generate_v4() primary key,
    name varchar(255) not null,
    area_size integer not null,
    description text not null,
    created_at timestamp not null,
    status boolean not null

);