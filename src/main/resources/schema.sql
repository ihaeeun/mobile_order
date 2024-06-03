create table cart_item
(
    id         bigint  not null auto_increment,
    quantity   integer not null,
    product_id bigint unique,
    user_id    varchar(255),
    primary key (id)
);
create index idx_userId
    on cart_item (user_id);
create index ix_userId_productId
    on cart_item (user_id, product_id);

create table member
(
    id                   varchar(255) not null,
    user_id              varchar(255) unique,
    password             varchar(255) not null,
    user_name            varchar(255),
    phone                varchar(255) unique,
    birth                date,
    gender               tinyint check (gender between 0 and 1),
    withdrawal           boolean      not null,
    sign_up_date_time    timestamp(6) with time zone,
    withdrawal_date_time timestamp(6) with time zone,
    primary key (id)
);
create index ix_userName_phone
    on member (user_name, phone);

create table member_history
(
    id               bigint not null auto_increment,
    user_id          varchar(255),
    action_type      varchar(255),
    request          TEXT,
    result           TEXT,
    action_date_time timestamp(6) with time zone,
    primary key (id)
);

create table order_form
(
    id              varchar(255) not null,
    user_id         varchar(255),
    total_amount    bigint       not null,
    order_status    tinyint check (order_status between 0 and 2),
    order_date_time timestamp(6) with time zone,
    primary key (id)
);
create index ix_userId
    on order_form (user_id);
create index ix_id_orderStatus
    on order_form (id, order_status);

create table order_item
(
    id         bigint  not null auto_increment,
    order_id   varchar(255),
    product_id bigint unique,
    price      integer not null,
    quantity   integer not null,
    primary key (id)
);
create index ix_orderId
    on order_item (order_id);

create table payment
(
    id           bigint  not null auto_increment,
    order_id     varchar(255),
    cancellation boolean not null,
    primary key (id)
);

create table product
(
    id    bigint  not null auto_increment,
    name  varchar(255),
    price integer not null,
    stock integer not null,
    version bigint not null,
    primary key (id)
);

create table user_action
(
    id               bigint not null auto_increment,
    action_date_time timestamp(6) with time zone,
    action_type      varchar(255),
    api_path         varchar(255),
    request          TEXT,
    result           TEXT,
    user_id          varchar(255),
    primary key (id)
);