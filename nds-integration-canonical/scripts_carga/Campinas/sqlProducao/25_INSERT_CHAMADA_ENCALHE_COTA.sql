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

desc chamada_encalhe_cota;


select sum(m.qtde)
from 
movimento_estoque_cota m, 
movimento_estoque me,
chamada_encalhe ce,
chamada_encalhe_cota cec,
controle_conferencia_encalhe cce

where 
m.tipo_movimento_id = 21
and ce.produto_edicao_id = 168831
and me.TIPO_MOVIMENTO_ID = 31

and me.produto_edicao_id = m.produto_edicao_id
and ce.produto_edicao_id = m.produto_edicao_id
and cec.CHAMADA_ENCALHE_ID = ce.id
and m.COTA_ID = cec.COTA_ID

and cce.DATA = me.DATA

;

select * from tipo_movimento where id = 21;

select *
    from 
        movimento_estoque_cota m, 
        chamada_encalhe ce
    where 
        tipo_movimento_id = 26
    and ce.produto_edicao_id = m.produto_edicao_id
and ce.produto_edicao_id = 168831
and m.COTA_ID = 142
;

select * from hvnd
where
produto_edicao_id = 168831
and COTA_ID = 142


;