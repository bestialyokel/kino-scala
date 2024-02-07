

create database todo_db;

CREATE TYPE task_status AS ENUM ('completed', 'incompleted');

create table tasks (
    id serial primary key,
    name varchar(64),
    deleted timestamp,
    status task_status
);

CREATE CAST (character varying AS task_status) WITH INOUT AS ASSIGNMENT;
