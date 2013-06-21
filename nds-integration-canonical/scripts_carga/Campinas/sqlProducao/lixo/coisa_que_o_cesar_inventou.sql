(select box, nome_box, count(*)   from estqbox group by 1,2);
select * from estqbox

update estoque_produto
set qtde = (select quantidade from estqbox
                       where estqbox.produto_edicao_id = estoque_produto.produto_edicao_id
                       and box=60);

select count(*) from estoque_produto where qtde > 0
select * from estqbox where produto_edicao_id is null and box = 60;

update estoque_produto
set qtde_devolucao_encalhe = (select quantidade from estqbox
                       where estqbox.produto_edicao_id = estoque_produto.produto_edicao_id
                       and box=70);

update estoque_produto
set qtde_suplementar = (select quantidade from estqbox
                       where estqbox.produto_edicao_id = estoque_produto.produto_edicao_id
                       and box=80);

select produto_edicao_id from estoque_produto
where qtde > 0
and produto_edicao_id in (select produto_edicao_id from movimento_estoque where tipo_movimento_id = 13);

insert into `nds-client-homolog`.produto
(select * from `nds-client-piloto2`.produto p1 where p1.id 
not in (select p2.id from `nds-client-homolog`.produto p2));

insert into `nds-client-homolog`.brinde
(select * from `nds-client-piloto2`.brinde p1 where p1.id 
not in (select p2.id from `nds-client-homolog`.brinde p2));


insert into `nds-client-homolog`.produto_edicao
(select * from `nds-client-piloto2`.produto_edicao p1 where p1.id 
not in (select p2.id from `nds-client-homolog`.produto_edicao p2));

insert into `nds-client-homolog`.lancamento
(id,
alterado_interface,
data_criacao,
data_lcto_distribuidor,
data_lcto_prevista,
data_rec_distrib,
data_rec_prevista,
data_status,
status,
tipo_lancamento,
produto_edicao_id)
(select id,
alterado_interface,
data_criacao,
data_lcto_distribuidor,
data_lcto_prevista,
data_rec_distrib,
data_rec_prevista,
data_status,
status,
tipo_lancamento,
produto_edicao_id
 from `nds-client-piloto2`.lancamento p1 where p1.id 
not in (select p2.id from `nds-client-homolog`.lancamento p2));

select QTDE_DEVOLUCAO_ENCALHE from estoque_produto where QTDE_DEVOLUCAO_ENCALHE > 0

select count(*), box from estqbox group by box

select count(*), box from estqbox group by box
begin;
insert into estoque_produto (QTDE, QTDE_DEVOLUCAO_ENCALHE, QTDE_SUPLEMENTAR, PRODUTO_EDICAO_ID)
(select case when box=60 then quantidade else null end, 
        case when box=70 then quantidade else null end,
        case when box=80 then quantidade else null end,
   produto_edicao_id from estqbox
    where box not in (85,92) and produto_edicao_id not in (select e.produto_edicao_id from estoque_produto e group by 1));
rollback;

update estoque_produto
set qtde_devolucao_encalhe = (select quantidade from estqbox
                       where estqbox.produto_edicao_id = estoque_produto.produto_edicao_id
                       and box=70);

select * from estqbox
where produto_edicao_id not in (select a.produto_edicao_id from estoque_produto a)
and b.box = 70
