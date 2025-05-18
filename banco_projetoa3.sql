create database banco_projetoa3;
use banco_projetoa3;
create table usuario(
	id int auto_increment primary key,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    senha varchar(256) not null
);
select * from usuario;
alter table usuario add telefone varchar(13) not null;
create table bazar(
	id int auto_increment unique , 
    nome varchar(100) not null,
    cnpj varchar (14) not null unique,
    endereco varchar(100) not null,
    emailContato varchar(100) not null
);
alter table bazar drop column emailContato;
alter table bazar add email_contato varchar(100) not null unique;
select * from bazar;
alter table bazar add senha varchar(256) not null;