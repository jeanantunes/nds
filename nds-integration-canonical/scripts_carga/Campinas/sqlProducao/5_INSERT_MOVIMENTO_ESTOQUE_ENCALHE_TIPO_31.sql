alter table movimento_estoque modify id bigint(20) AUTO_INCREMENT;

-- Insere movimento de estoque de recebimento de encalhe do distribuidor baseado no movimento de estoque de envio de encalhe da cota 
-- Query OK, 17550 rows affected (8.37 sec)
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
        min(h.data_recolhimento),
        '2013-06-15',
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
group by
	1,2,3,4,6,7,8,9,11,12,13,14,15,16,17,18,19
having sum(mec.QTDE) > 0);
