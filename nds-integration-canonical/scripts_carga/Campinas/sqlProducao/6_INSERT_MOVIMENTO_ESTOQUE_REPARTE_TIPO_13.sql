-- Query OK, 19056 rows affected (8.61 sec)
-- Insere no movimento de envio de reparte do distribuidor baseado no movimento de estoque de recebimento de reparte da cota
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
        '2013-06-15',
        'CARGA',
        'APROVADO',
        min(h.data_lancamento),
        '2013-06-15',
        null,
        13,
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
	and mec.tipo_movimento_id = 21
group by
	1,2,3,4,6,7,8,9,11,12,13,14,15,16,17,18,19
having sum(mec.QTDE) > 0);
