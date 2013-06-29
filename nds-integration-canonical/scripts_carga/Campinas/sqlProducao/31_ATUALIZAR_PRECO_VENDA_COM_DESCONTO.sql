update movimento_estoque_cota set preco_venda = 0 where preco_venda is null;
update movimento_estoque_cota set valor_Desconto = 23;
update movimento_estoque_cota set preco_com_Desconto = preco_venda - (preco_venda * valor_desconto/100);

update movimento_estoque_cota set valor_Desconto = 16, preco_com_desconto = preco_venda - (preco_venda * 16/100)
where cota_id in 
(select id from cota where numero_cota in (356,350,369,352,362,355,359,354,371,353,380,366,377,364,378,374,381,351,365,368,376,384,382));
