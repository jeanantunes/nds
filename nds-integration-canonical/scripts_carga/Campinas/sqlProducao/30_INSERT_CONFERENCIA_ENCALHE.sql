-- Query OK, 1288970 rows affected (22 min 17.84 sec)
ALTER TABLE `controle_conferencia_encalhe` ADD INDEX `data_controle_conferencia_encalhe` (`data` ASC);
ALTER TABLE `movimento_estoque` ADD INDEX `data_movimento_estoque` (`data` ASC);

insert into CONFERENCIA_ENCALHE
(data, preco_capa_informado, qtde, qtde_informada, 
chamada_encalhe_cota_id, controle_conferencia_encalhe_cota_id,
movimento_estoque_id, movimento_estoque_cota_id, produto_edicao_id)
(select data_recolhimento, pe.preco_previsto, mec.qtde, mec.qtde,
cec.id, ccec.id, me.id, mec.id, mec.produto_edicao_id
from 
movimento_estoque_cota mec, 
movimento_estoque me,
chamada_encalhe ce,
chamada_encalhe_cota cec,
controle_conferencia_encalhe cce,
controle_conferencia_encalhe_cota ccec,
produto_edicao pe
where 
mec.tipo_movimento_id = 26
and mec.produto_edicao_id = pe.id
and me.produto_edicao_id = mec.produto_edicao_id
and me.tipo_movimento_id = 31    
and me.produto_edicao_id = pe.id
and ce.produto_edicao_id = mec.produto_edicao_id
and ce.produto_edicao_id = me.produto_edicao_id
and cec.chamada_encalhe_id = ce.id
and cec.cota_id = mec.cota_id
and cce.data = me.data
and ccec.ctrl_conf_encalhe_id = cce.id
and ccec.cota_id = mec.cota_id
);

-- Robson Martins
update CONFERENCIA_ENCALHE set qtde = QTDE_INFORMADA;

-- ====================######## ABAIXO Scripts Tests ###############============================



select sum(mec.qtde)
from 
movimento_estoque_cota mec, 
movimento_estoque me,
chamada_encalhe ce,
chamada_encalhe_cota cec,
controle_conferencia_encalhe cce,
controle_conferencia_encalhe_cota ccec,
produto_edicao pe
where 
mec.tipo_movimento_id = 26 --
and mec.produto_edicao_id = pe.id
and me.produto_edicao_id = mec.produto_edicao_id
and me.tipo_movimento_id = 31    --
and me.produto_edicao_id = pe.id
and ce.produto_edicao_id = mec.produto_edicao_id
and ce.produto_edicao_id = me.produto_edicao_id
and cec.chamada_encalhe_id = ce.id
and cec.cota_id = mec.cota_id
and cce.data = me.data --
and ccec.ctrl_conf_encalhe_id = cce.id
and ccec.cota_id = mec.cota_id-- 
and ce.produto_edicao_id = 168831

;

select me.produto_edicao_id, mec.qtde, mec.cota_id from movimento_estoque_cota mec, movimento_estoque me, chamada_encalhe ce,
chamada_encalhe_cota cec
where mec.produto_edicao_id = 168831
and mec.tipo_movimento_id = 26
and me.produto_edicao_id = mec.produto_edicao_id
and me.tipo_movimento_id = 31    --
and ce.produto_edicao_id = mec.produto_edicao_id
and cec.chamada_encalhe_id = ce.id
and cec.chamada_encalhe_id = ce.id
and cec.cota_id = mec.cota_id
;

-- 15
select * from cota where id = 142
;

select * from movimento_estoque_cota
where cota_id = 142
and tipo_movimento_id = 26
and produto_edicao_id = 168831
;