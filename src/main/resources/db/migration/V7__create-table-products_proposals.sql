create table products_proposals(

    product_id uuid,
    proposal_id uuid,
    primary key(product_id, proposal_id),

    foreign key (product_id)
    references products (id)
    on delete set default
    on update set default,

    foreign key (proposal_id)
    references proposals (id)
    on delete set default
    on update set default

);