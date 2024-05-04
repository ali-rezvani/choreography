drop table if exists customer;
drop table if exists customer_payment;

create table customer(
    id int auto_increment primary key ,
    name varchar(50) not null ,
    balance int
);

create table customer_payment(
    payment_id uuid default random_uuid() primary key ,
    order_id uuid,
    customer_id int,
    status varchar(50),
    amount int
);

insert into customer(name,balance)
    values ('Ali',200),
           ('Fariba',200),
           ('Helen',200);