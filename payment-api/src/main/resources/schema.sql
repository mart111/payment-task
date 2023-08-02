set FOREIGN_KEY_CHECKS = 0;
drop table if exists __user;
drop table if exists transaction;
drop table if exists merchant_transaction;
set FOREIGN_KEY_CHECKS = 1;

create table __user
(
    user_type             varchar(31) not null,
    id                    bigint      not null auto_increment,
    description           varchar(255),
    email                 varchar(255),
    name                  varchar(255),
    password              varchar(255),
    role                  enum ('ADMIN','MERCHANT'),
    status                enum ('ACTIVE','INACTIVE'),
    total_transaction_sum decimal(38, 2),
    primary key (id)
) engine = InnoDB;

create table transaction
(
    transaction_type varchar(31)  not null,
    transaction_id   binary(16)   not null,
    amount           decimal(38, 2),
    creation_date    datetime(6)  not null,
    customer_email   varchar(255) not null,
    customer_phone   varchar(255),
    merchant_id      bigint,
    reference_id     varchar(255),
    status           enum ('APPROVED','AUTHORIZED','REFUNDED','REVERSED'),
    primary key (transaction_id)
) engine = InnoDB;

create table merchant_transaction
(
    user_id        bigint     not null,
    transaction_id binary(16) not null
) engine = InnoDB;

create index user_email_idx
    on __user (email);

alter table merchant_transaction
    add constraint FK_transaction_id
        foreign key (transaction_id)
            references transaction (transaction_id);

alter table merchant_transaction
    add constraint FK_user_id
        foreign key (user_id)
            references __user (id);