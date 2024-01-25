/*
Создать в базе test UTF8 таблицу users с полями:
id: auto increment
email
pass: md5 от пароля
sum: деньги, целое число
updated: дата время изменения пользователя, должно проставляться автоматически при изменении данных
created: дата время создания пользователя, ставится руками при добавлении
*/

create table test.users (
   -- user_id int primary key auto_increment,
    id int primary key auto_increment,
    email varchar(256),
    pass_md5 char(32),
    balance int,
    updated_at timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    created_at timestamp
);

create trigger test.tr_users_ai before insert on test.users for each row
    SET new.updated_at := new.created_at;
