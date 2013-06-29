
148191
//148191
select * from estudo where id =101737
select 35109002 1
select * from produto_edicao pe, produto p 
where p.codigo='35109002' and pe.numero_edicao = 1 and pe.produto_id = p.id

select * from lancamento where produto_edicao_id = 148191
select * from estudo where produto_edicao_id = 148191


//148061
select * from produto_edicao pe, produto p 
where p.codigo='30031001' and pe.numero_edicao = 13 and pe.produto_id = p.id

select * from lancamento where produto_edicao_id = 148061
select * from estudo where produto_edicao_id = 148061

create table estudoTmp (estudo_id bigint);

insert into estudoTmp
select id from estudo where id in
(select e.id from lancamento l, estudo e
where l.produto_edicao_id = e.produto_edicao_id
and l.data_lcto_prevista != e.data_lancamento
and (l.status = 'CONFIRMADO' or l.status = 'BALANCEADO')
and e.data_cadastro > '2013-03-27');

delete from estudo_cota where estudo_id in (select estudo_id from estudoTmp)
and l.produto_edicao_id = 148061;


//148411
select * from produto_edicao pe, produto p 
where p.codigo='30027001' and pe.numero_edicao = 13 and pe.produto_id = p.id;

select * from lancamento where produto_edicao_id = 148411;
select * from estudo where produto_edicao_id = 148411;


create table estudoTmp (estudo_id bigint);

insert into estudoTmp
(select e.id from lancamento l, estudo e
where l.produto_edicao_id = e.produto_edicao_id
and l.data_lcto_prevista = e.data_lancamento
and l.tipo_lancamento = 'RELANCAMENTO');

delete from estudo_cota where estudo_id in
(select * from estudoTmp);

delete from estudo where id in
(select * from estudoTmp);

delete from lancamento where tipo_lancamento = 'RELANCAMENTO';

drop table estudoTmp;