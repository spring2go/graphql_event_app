-- noinspection SqlNoDataSourceInspectionForFile

DROP TABLE IF EXISTS tb_booking;
DROP TABLE IF EXISTS tb_event;
DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user (
    id SERIAL,
    email varchar(100) NOT NULL,
    password varchar(100) NOT NULL,
    primary key (id)
);

CREATE TABLE tb_event (
    id SERIAL,
    title varchar(100) NOT NULL,
    description varchar(255) NOT NULL,
    price float not null,
    date timestamp not null,
    creator_id integer not null,
    primary key (id),
    constraint fk_created_id foreign key(creator_id) references tb_user(id)
);

CREATE TABLE tb_booking (
    id SERIAL,
    user_id integer not null,
    event_id integer not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    primary key (id),
    constraint fk_user_id foreign key(user_id) references tb_user(id),
    constraint fk_event_id foreign key(event_id) references tb_event(id)
)

CREATE INDEX idx_user_id ON tb_booking (user_id);