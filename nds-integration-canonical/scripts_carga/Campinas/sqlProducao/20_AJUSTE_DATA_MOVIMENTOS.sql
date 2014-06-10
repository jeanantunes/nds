-- Query OK, 457668 rows affected (22.02 sec)
-- Não deve mais ocorrer Fazer com begin apenas para verificação, caso ocorra, 
-- verificar nos primeiros inserts dos movimento_estoque_cota e movimento_estoque o porquê.


-- update movimento_estoque_cota set data = date(sysdate()) where data = '0000-00-00';

-- Query OK, 11074 rows affected (0.19 sec)
-- update movimento_estoque set data = date(sysdate()) where data = '0000-00-00';
update movimento_estoque_cota set data = 
(select min(l.DATA_REC_DISTRIB) from lancamento l where l.produto_edicao_id = movimento_estoque_cota.produto_edicao_id)
where data = '0000-00-00'
and TIPO_MOVIMENTO_ID = 26
;

update movimento_estoque set data = 
(
select min(l.DATA_REC_DISTRIB) from lancamento l where l.produto_edicao_id = movimento_estoque.produto_edicao_id
)
where data = '0000-00-00'
and TIPO_MOVIMENTO_ID = 31
;


select * from movimento_estoque where data = '0000-00-00';
select * from movimento_estoque_cota where data = '0000-00-00' limit 100000000;