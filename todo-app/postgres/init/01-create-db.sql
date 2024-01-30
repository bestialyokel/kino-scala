

create database todo_db;

create table tasks (
    task_id serial primary key,
    name varchar(64),
    deleted boolean,
    completed boolean
);