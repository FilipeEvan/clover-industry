/* Cria um novo banco de dados chamado clover-industry-db */
CREATE DATABASE clover_industry_db;

/* Cria uma tabela chamada cultures */
CREATE TABLE cultures(
    id SERIAL PRIMARY KEY,
    culture VARCHAR(255) NOT NULL
);

/* Adiciona uma extensão uuid-ossp que gera um valor UUID único para cada registro */
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

/* Cria uma tabela chamada products */
CREATE TABLE products(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    area_size INTEGER NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    status BOOLEAN NOT NULL
);

/* Cria uma tabela de associação chamada cultures_products entre a tabela products e a tabela cultures */
CREATE TABLE cultures_products(
    product_id UUID,
    culture_id BIGINT,
    PRIMARY KEY(product_id, culture_id),

    FOREIGN KEY (product_id)
    	REFERENCES products (id)
		ON DELETE SET DEFAULT
		ON UPDATE SET DEFAULT,

    FOREIGN KEY (culture_id)
    	REFERENCES cultures (id)
		ON DELETE SET DEFAULT
		ON UPDATE SET DEFAULT
);

/* Cria uma tabela chamada photos com uma chave estrangeira referenciando a tabela products */
CREATE TABLE photos(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    product_id UUID NOT NULL,
    filename VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    photo BYTEA NOT NULL,
    created_at TIMESTAMP NOT NULL,

    FOREIGN KEY (product_id)
		REFERENCES products (id)
		ON DELETE SET DEFAULT
		ON UPDATE SET DEFAULT
);

/* Cria uma tabela chamada clients */
CREATE TABLE clients(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

/* Cria uma tabela chamada proposals com uma chave estrangeira referenciando a tabela clients */
CREATE TABLE proposals(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    client_id  UUID NOT NULL,

    FOREIGN KEY (client_id)
		REFERENCES clients (id)
		ON DELETE SET DEFAULT
		ON UPDATE SET DEFAULT

);

/* Cria uma tabela de associação chamada products_proposals entre a tabela products e a tabela proposals */
CREATE TABLE products_proposals(
    product_id UUID,
    proposal_id UUID,
    PRIMARY KEY(product_id, proposal_id),

    FOREIGN KEY (product_id)
    	REFERENCES products (id)
    	ON DELETE SET DEFAULT
		ON UPDATE SET DEFAULT,

    FOREIGN KEY (proposal_id)
    	REFERENCES proposals (id)
    	ON DELETE SET DEFAULT
		ON UPDATE SET DEFAULT
);

/* Cria um novo banco de dados chamado clover_industry_test */
CREATE DATABASE clover_industry_test;
