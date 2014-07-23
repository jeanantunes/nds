-- insere estoque de produto do fornecedor baseado
-- nos tipos A.R.PROM. do arquivo ESTQBOX
-- Atualizar para data do rollout
insert into estoque_produto 
(produto_edicao_id)
(select 
	produto_edicao_id 
from estqbox 
where box = 92 
and produto_edicao_id not in (select produto_edicao_id from estoque_produto));

-- Insere movimentos de estoque de recebimento fisico do distribuidor
-- baseado nos tipos A.R.PROM. do arquivo ESTQBOX
insert into movimento_estoque
(APROVADO_AUTOMATICAMENTE,
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
 eb.QUANTIDADE,
 eb.PRODUTO_EDICAO_ID,
 pe.id,
 null,
 null,
 null,
 null,
 null,
 null,
 'CARGA_INICIAL'
 from 
	 estqbox eb,
	 estoque_produto pe,
	 hvnd h
 where 
	h.produto_edicao_id = pe.produto_edicao_id
    and eb.box = 92 
	and pe.produto_edicao_id = eb.produto_edicao_id
	and h.produto_edicao_id is not null
group by 1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18,19
);

