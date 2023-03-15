create table cultures_products(

    product_id uuid,
    culture_id bigint,
    primary key(product_id, culture_id),

    foreign key (product_id)
    references products (id)
    on delete set default
    on update set default,

    foreign key (culture_id)
    references cultures (id)
    on delete set default
    on update set default

);