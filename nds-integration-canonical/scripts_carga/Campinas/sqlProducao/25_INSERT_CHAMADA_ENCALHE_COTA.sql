-- Query OK, 1288970 rows affected (33.72 sec)
INSERT INTO CHAMADA_ENCALHE_COTA (fechado, postergado, qtde_prevista, chamada_encalhe_id, cota_id)
(select false, false, qtde, ce.id, m.cota_id
    from 
        movimento_estoque_cota m, 
        chamada_encalhe ce
    where 
        tipo_movimento_id = 21
    and ce.produto_edicao_id = m.produto_edicao_id);


select false, false, qtde, ce.id, m.cota_id
from 
	movimento_estoque_cota m, 
	chamada_encalhe ce
where 
	tipo_movimento_id = 21
and ce.produto_edicao_id = m.produto_edicao_id
and produto_edicao_id = 387;