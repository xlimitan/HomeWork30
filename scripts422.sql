--**Шаг 2**
--Описание структуры: у каждого человека есть машина. Причем несколько человек могут пользоваться одной машиной.
--У каждого человека есть имя, возраст и признак того, что у него есть права (или их нет).
--У каждой машины есть марка, модель и стоимость. Также не забудьте добавить таблицам первичные ключи и связать их.

create table person (
id serial primary key,
name varchar,
age int,
driver_license boolean,
car_id serial references car (id)
);

create table car (
id serial primary key,
brand varchar,
model varchar,
price int
);

insert into car values (1, 'bmw', 'x3', 3500000);
insert into car values (2, 'mazda', 'm5', 2000000);

insert into person values (1, 'Ivan', 25, true, 1);
insert into person values (2, 'Roman', 24, false, 1);

insert into person values (3, 'Igor', 27, true, 2);
insert into person values (4, 'Marina', 21, false, 2);

--**Шаг 3**
--
--Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах (достаточно получить только имя и возраст студента) школы Хогвартс вместе с названиями факультетов.
--
--Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.

select s.name as student_name, s.age as student_age, f.name as faculty_name
from faculty f
right join student s on s.faculty_id = f.id;

select s.name as student_name, s.age as student_age, a.id as avarat_id
from avatar a
join student s on a.student_id  = s.id;