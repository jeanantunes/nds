-- Query OK, 227702 rows affected (46.29 sec)
update movimento_estoque_cota set preco_venda = 
(select (select pe2.preco_venda 
from produto_edicao pe2 
where pe2.preco_venda is not null 
and preco_venda != 0 
and pe2.produto_id = produto_edicao.produto_id 
order by pe2.numero_edicao desc 
limit 1)
from  produto_edicao
where produto_edicao.id = movimento_estoque_cota.produto_edicao_id
group by produto_edicao.produto_id)
where preco_venda is null 
or preco_venda = 0;
