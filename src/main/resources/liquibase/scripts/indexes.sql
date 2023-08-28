-- liquibase formatted sql

-- changeset golenko:1
create index stud_id on student (name);
create index fac_id on faculty (name, color);