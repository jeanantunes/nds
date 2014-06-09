-- Query OK, 19056 rows affected (1.13 sec)
-- Insere ENTRADAS de recebimento fisico no movimento estoque do distribuidor 
-- baseado na ENTRADA de REPARTE do movimento estoque da cota
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
min(h.data_lancamento),
date(sysdate()),
null,
20,
1,
sum(mec.qtde),
mec.PRODUTO_EDICAO_ID,
null,
null,
null,
null,
null,
null,
null,
'CARGA_INICIAL'

from movimento_estoque_cota mec,
hvnd h
where 	  mec.produto_edicao_id = h.produto_edicao_id
and h.cota_id = mec.cota_id
and mec.tipo_movimento_id = 21
and mec.QTDE > 0
and h.produto_edicao_id is not null
group by 1,2,3,4,6,7,8,9,11,12,13,14,15,16,17,18,19);

-- Atualizando estoque_produto_id no movimento_estoque para manter as referências do banco
update movimento_estoque 
set ESTOQUE_PRODUTO_ID=(select ep.id from estoque_produto ep 
where ep.produto_edicao_id=movimento_estoque.produto_edicao_id) 
where tipo_movimento_id=20;


-- Atualizando os movimentos de recebimento físico com as quantidades inseridas pelo arquivo de ESTQ_BOX,
-- ou seja, Deixando a quantidade recebida igual a quantidade de envio aos jornaleiro ao estoque do distribuidor
-- Robson Martins - Comentado -- + coalesce(ep.qtde_suplementar,0) suplementar já sensibilizado
update movimento_estoque 
set QTDE=COALESCE(QTDE,0)+(select coalesce(ep.qtde,0) -- + coalesce(ep.qtde_suplementar,0) 
from estoque_produto ep where ep.produto_edicao_id=movimento_estoque.produto_edicao_id) 
where tipo_movimento_id=20;



-- Robson Martins - Limpeza de tabela caso movimento precise ser reprocessado
/*
delete from movimento_estoque 
where TIPO_MOVIMENTO_ID = 20;
*/

-- ====================######## ABAIXO Scripts Tests ###############============================

/*
-- Não pode haver registro com data 0000-00-00
select count(1) from movimento_estoque where 
tipo_movimento_id = 20 and data = '0000-00-00';

-- Caso haja deve rodar esse update
update movimento_estoque set
data = (select min(l.data_lcto_distribuidor)
from
lancamento l
where
l.produto_edicao_id = movimento_estoque.produto_edicao_id
group by
l.produto_edicao_id
)
where
tipo_movimento_id = 20
and data = '0000-00-00'
;


update movimento_estoque set data = '2013-06-15' where 
tipo_movimento_id = 20 and data = '0000-00-00';

delete from movimento_estoque
where TIPO_MOVIMENTO_ID = 20;
*/



select count(1) from movimento_estoque where 
tipo_movimento_id = 20 and data = '0000-00-00';