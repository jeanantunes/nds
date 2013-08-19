update movimento_estoque_cota set lancamento_id = 
(select l.id from lancamento l 
where l.produto_edicao_id = movimento_estoque_cota.produto_edicao_id 
limit 1);
