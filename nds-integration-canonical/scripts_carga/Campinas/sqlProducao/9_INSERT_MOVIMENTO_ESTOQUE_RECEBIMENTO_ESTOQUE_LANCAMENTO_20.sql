-- insere o smovimebnto de recebimento de mercadoria dos produtos que ainda estãono estoque do distribuidor, ou seja, ainda não foi lançado para as cota.
-- Atualizar datas para data do rollout
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
 '2013-06-15',
 'CARGA',
 'APROVADO',
min(h.data_lancamento),
 '2013-06-15',
 null,
 20,
 1,
 ep.QTDE,
 ep.PRODUTO_EDICAO_ID,
 ep.ID,
 null,
 null,
 null,
 null,
 null,
 null,
 'CARGA_INICIAL'
 from 
	 estoque_produto ep,
	 hvnd h
 where 
	h.produto_edicao_id = ep.produto_edicao_id
    and ep.QTDE > 0
group by 1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18,19
);

-- Movimento criado para justificar a entrada no estoque suplementar do distribuidor para o rollout
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
 '2013-06-15',
 'CARGA',
 'APROVADO',
 min(h.data_lancamento),
 '2013-06-15',
 null,
 191,
 1,
 ep.QTDE_suplementar,
 ep.PRODUTO_EDICAO_ID,
 ep.ID,
 null,
 null,
 null,
 null,
 null,
 null,
 'CARGA_INICIAL'
 from 
	 estoque_produto ep,
	 hvnd h
 where 
	h.produto_edicao_id = ep.produto_edicao_id
    and ep.QTDE_suplementar > 0
 group by 1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18,19
);
