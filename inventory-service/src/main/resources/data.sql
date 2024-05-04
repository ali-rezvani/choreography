drop table if exists product;
drop table if exists order_inventory;

create table product(
    id int AUTO_INCREMENT primary key,
    description varchar(100),
    available_quantity int
);

create table order_inventory(
    inventory_id UUID default random_uuid() primary key ,
    order_id uuid,
    product_id int,
    status varchar(50),
    quantity int,
    foreign key (product_id) references product(id)
);

insert into product(description,available_quantity)
    values ('book',10),
           ('pen',10),
           ('rug',10);