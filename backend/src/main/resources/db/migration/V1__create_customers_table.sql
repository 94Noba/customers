create sequence customers_id_sequence;
create table customers(
    id bigint default nextval('customers_id_sequence') primary key,
    name varchar(100) not null ,
    email varchar(100) not null ,
    age int not null
);