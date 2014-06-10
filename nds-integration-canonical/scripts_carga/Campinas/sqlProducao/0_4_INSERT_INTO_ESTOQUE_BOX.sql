drop table estqbox;

create table estqbox (
    linha_vazia varchar(1),
    tipo int,
    box int,
    nome_box varchar(10),
    produto varchar(8),
    edicao int,
    nome_produto varchar(45),
    quantidade int,
    produto_edicao_id int);

-- deve ser executado pela console pois o arquivo nao esta local, está no servidor
LOAD DATA LOCAL INFILE 'ESTQBOX.NEW' INTO TABLE estqbox COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\n';

update estqbox set produto_edicao_id = (select pe.id from produto_edicao pe, produto p 
										where p.id = pe.produto_id 
										and p.codigo = produto 
										and pe.numero_edicao = edicao)
;

delete from estqbox where produto_edicao_id is null;

update estoque_produto
set qtde=null, qtde_devolucao_encalhe=null, qtde_devolucao_fornecedor=null, qtde_suplementar=null;


#delete from estqbox where  produto_edicao_id is null;

#Estoque lançamento
#insert into estoque_produto (QTDE, QTDE_DEVOLUCAO_ENCALHE, QTDE_SUPLEMENTAR, PRODUTO_EDICAO_ID)
#(select case when box=60 then quantidade else null end, 
#        case when box=70 then quantidade else null end,
#        case when box=80 then quantidade else case when box=85 then quantidade else null end else null end,
#   produto_edicao_id from estqbox
#    where box not in (85,92));

-- Insere estoque do distribuidor baseado no arquivo ESTQBOX.NEW
insert into estoque_produto (QTDE, QTDE_DEVOLUCAO_ENCALHE, QTDE_SUPLEMENTAR, PRODUTO_EDICAO_ID)
select 
    sum(case when box=60 then quantidade else 0 end) as qtde,
    sum(case when box=70 then quantidade else 0 end) as QTDE_DEVOLUCAO_ENCALHE,          
    sum(case when box=80 then quantidade else case when box=85 then quantidade else 0 end end) as QTDE_SUPLEMENTAR,
    produto_edicao_id 
from estqbox
where box not in (92)
-- and produto_edicao_id is not null
group by 4;


-- ====================######## ABAIXO Scripts Tests ###############============================

-- Verificar porque não atualizou qtde na estoque_produto
-- 38575001 edi 103
-- 85880001 edi 212
-- São registros que não foram populados na estqbox devido a não existência dos mesmos no arquivo MATRIZ.NEW do MDC 108. 
-- Como ele não encontra produto_edicao_id, os registros são deletados da estqbox porque ficam com null no produto_edicao_id
-- Existem 700 linhas na estqbox, porem foram inseridas apenas 681 na estoque_produto
select * from estoque_produto;

select * from produto_edicao pe, produto p
where p.id = pe.produto_id 
and p.codigo = '85880001'
and pe.numero_edicao = 212
;

select pe.id from produto_edicao pe, produto p 
										where p.id = pe.produto_id 
										and p.codigo = produto 
										and pe.numero_edicao = edicao
;


select * from estqbox where produto_edicao_id is null;
select * from estqbox where edicao is null;
select * from estqbox where produto is null;


select q.* from produto_edicao pe, produto p, estqbox q 
where p.id = pe.produto_id 
and p.codigo != q.produto 
and pe.numero_edicao != q.edicao;

select * from estqbox q 
where q.produto not in (select p.codigo from produto p)
and	q.edicao not in	(select pe.numero_edicao from produto_edicao pe)
;



#update estoque_produto
#set qtde = (select quantidade from estqbox
#                       where estqbox.produto_edicao_id = estoque_produto.produto_edicao_id
#                       and box=60);

#Estoque encalhe
#update estoque_produto
#set qtde_devolucao_encalhe = (select quantidade from estqbox
#                       where estqbox.produto_edicao_id = estoque_produto.produto_edicao_id
#                       and box=70);

#Estoque Suplementar
#update estoque_produto
#set qtde_suplementar = (select quantidade from estqbox
#                       where estqbox.produto_edicao_id = estoque_produto.produto_edicao_id
#                       and box=80);

