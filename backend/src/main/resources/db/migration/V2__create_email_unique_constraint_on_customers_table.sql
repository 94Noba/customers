alter table customers
    add constraint UQ_CUSTOMERS_EMAIL unique (email);