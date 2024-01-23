-- Создать несколько пользователей, 
-- дата создания должна отличаться мин на 1 день + 1 запись на сегодня

insert into 
test.users(email, pass_md5, balance, created_at)
values
('user1@mail.ru', '#', 0, '2024-01-01'),
('user2@mail.ru', '#', 0, '2024-01-03'),
('user3@mail.ru', '#', 0, '2024-01-06'),
('user4@mail.ru', '#', 0, '2024-01-10');


