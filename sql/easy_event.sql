DROP TABLE IF EXISTS event;
CREATE TABLE event (
    id SERIAL,
    title varchar(100) NOT NULL,
    description varchar(255) NOT NULL,
    price float not null,
    date timestamp not null,
    primary key (id)
);


