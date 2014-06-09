-- insere os movimentos de recebimento de mercadoria dos produtos que ainda estão no estoque do distribuidor, ou seja, ainda não foram 
--	lançados para as cota.
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
	and h.produto_edicao_id is not null
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
 date(sysdate()),
 'CARGA',
 'APROVADO',
 min(h.data_lancamento),
 date(sysdate()),
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
	and h.produto_edicao_id is not null
 group by 1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18,19
);


-- ====================######## ABAIXO Scripts Tests ###############============================

-- Robson Martins- Limpeza de tabela caso movimento precise ser reprocessado
select count(1) from estoque_produto
where QTDE_suplementar > 0;

delete from movimento_estoque 
where TIPO_MOVIMENTO_ID = 191;
/*
*/