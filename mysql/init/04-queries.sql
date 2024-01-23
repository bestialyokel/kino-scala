/*
Сделать выборки:
пользователь с email=XXXX
выбрать всех созданных сегодня пользователей
посчитать сколько было зарегистрировано пользователей за каждый день
посчитать сумму денег всех пользователей
*/

select * from test.users where email='XXXX';

select * from test.users where date(created_at) = date(now());

select
count(*) as registered_on_date_cnt,
date(created_at) as create_date
from test.users
group by date(created_at);

select sum(balance) FROM test.users;

/*
Изменить деньги пользователей:
по id
по email
всех которые были созданы в прошлом месяце
*/

update test.users set balance = balance + 1
where user_id = 1 OR email = 'user2@mail.ru';

update test.users set balance = balance + 1
where 
    extract(YEAR_MONTH FROM created_at) = extract(YEAR_MONTH FROM date_sub(now(), interval 1 month));


/*
Добавить уникальный ключ по полю email
Добавить нового пользователя, но если пользователь с таким email уже есть, то обновить его данные. on duplicate
Вывести все доступные базы данных
Вывести список таблиц
Вывести описание таблицы users
Получить команду создания таблицы users
Добавить в таблицу users поле sex после поля pass, которое может принимать три значения: 'unknown', 'M', 'F'. По умолчанию должно быть 'unknown'.
Изменить поле sum с целых на дробные
Переименовать поле sum в money
*/

alter table test.users
add constraint uc_users unique(email);

insert into 
test.users(email, pass_md5, balance, created_at)
values
('user1@mail.ru', '#', 0, '2024-01-01')
on duplicate key update email = concat("_", email);

show databases;

use test;

show tables;

describe users;

show create table test.users;

alter table test.users add 
sex enum('unknown', 'M', 'F') default 'unknown'
after pass_md5;

alter table test.users rename column balance to money;