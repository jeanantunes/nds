-- Query OK, 1288970 rows affected (33.72 sec)
INSERT INTO CHAMADA_ENCALHE_COTA (fechado, postergado, qtde_prevista, chamada_encalhe_id, cota_id)
(select false, false, qtde, ce.id, m.cota_id
    from 
        movimento_estoque_cota m, 
        chamada_encalhe ce
    where 
        tipo_movimento_id = 21
    and ce.produto_edicao_id = m.produto_edicao_id)
;

-- ====================######## ABAIXO Scripts Tests ###############============================

select false, false, sum(qtde), ce.id, m.cota_id
from 
	movimento_estoque_cota m, 
	chamada_encalhe ce
where 
	tipo_movimento_id = 21
and ce.produto_edicao_id = m.produto_edicao_id
and ce.produto_edicao_id = 387

;

select sum(QTDE_PREVISTA) 
from chamada_encalhe c, CHAMADA_ENCALHE_COTA cc 
where c.id = cc.chamada_encalhe_id 
and  produto_edicao_id = 387;

desc movimento_estoque_cota;