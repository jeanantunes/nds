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

LOAD DATA LOCAL INFILE 'ESTQBOX.NEW' INTO TABLE estqbox COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\n';

update estqbox set produto_edicao_id = (select pe.id from produto_edicao pe, produto p where
p.id = pe.produto_id and p.codigo = produto and pe.numero_edicao = edicao);

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
group by 4;

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