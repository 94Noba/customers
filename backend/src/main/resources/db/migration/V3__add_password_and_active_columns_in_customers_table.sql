alter table customers
    add password varchar(255) not null default 'password',
    add active bool not null default true;
