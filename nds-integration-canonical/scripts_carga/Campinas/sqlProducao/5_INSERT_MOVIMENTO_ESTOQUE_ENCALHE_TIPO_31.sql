alter table movimento_estoque modify id bigint(20) AUTO_INCREMENT;

-- Insere movimento de estoque de recebimento de encalhe do distribuidor baseado no 
-- movimento de estoque de envio de encalhe da cota 
-- Query OK, 17550 rows affected (23 min)
INSERT INTO movimento_estoque
(
APROVADO_AUTOMATICAMENTE,
DATA_APROVACAO,
MOTIVO,
STATUS,
DATA,
DATA_CRIACAO,
APROVADOR_ID,
TIPO_MOVIMENTO_ID,
USUARIO_ID,
QTDE,
PRODUTO_EDICAO_ID,
ESTOQUE_PRODUTO_ID,
ITEM_REC_FISICO_ID,
DATA_INTEGRACAO,
STATUS_INTEGRACAO,
COD_ORIGEM_MOTIVO,
DAT_EMISSAO_DOC_ACERTO,
NUM_DOC_ACERTO,
ORIGEM)
(select 
true,
date(sysdate()),
'CARGA',
'APROVADO',
min(h.data_recolhimento),
date(sysdate()),
null,
31,
1,
sum(mec.QTDE),
mec.PRODUTO_EDICAO_ID,
ep.ID,
null,
null,
null,
null,
null,
null,
'CARGA_INICIAL'
from movimento_estoque_cota mec,
estoque_produto ep,
hvnd h
where 
ep.produto_edicao_id = mec.produto_edicao_id
and ep.produto_edicao_id = h.produto_edicao_id
and mec.cota_id = h.cota_id
and mec.tipo_movimento_id = 26
and h.produto_edicao_id is not null
group by
1,2,3,4,6,7,8,9,11,12,13,14,15,16,17,18,19
having sum(mec.QTDE) > 0);


/*
-- Limpeza de tabela caso movimento precise ser reprocessado
delete from movimento_estoque 
where TIPO_MOVIMENTO_ID = 31;
*/

-- ====================######## ABAIXO Scripts Tests ###############============================

select count(1) from movimento_estoque where 
tipo_movimento_id = 31 and data = '0000-00-00';

select count(1) from hvnd where DATA_RECOLHIMENTO = '0000-00-00';

select count(1) from
movimento_estoque_cota mec,
	 estoque_produto ep,
	 hvnd h
where ep.produto_edicao_id = mec.produto_edicao_id
	and ep.produto_edicao_id = h.produto_edicao_id
	and mec.cota_id = h.cota_id
	and mec.tipo_movimento_id = 26
	and h.produto_edicao_id is not null;

