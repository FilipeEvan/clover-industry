create table proposals(

    id uuid default uuid_generate_v4() primary key,
    client_id  uuid not null,

    foreign key (client_id)
    references clients (id)
    on delete set default
    on update set default

);