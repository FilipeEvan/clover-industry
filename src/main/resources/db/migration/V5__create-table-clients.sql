create table clients(

    id uuid default uuid_generate_v4() primary key,
    name varchar(255) not null,
    email varchar(100) not null,
    phone varchar(20) not null,
    created_at timestamp not null

);