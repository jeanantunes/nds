-- Query OK, 2576308 rows affected (3 min 43.47 sec)
update MOVIMENTO_ESTOQUE_COTA 
SET preco_venda = (select pe.preco_venda from produto_edicao pe 
where pe.id = movimento_estoque_cota.produto_edicao_id 
and preco_venda is not null 
and preco_venda <>0);
