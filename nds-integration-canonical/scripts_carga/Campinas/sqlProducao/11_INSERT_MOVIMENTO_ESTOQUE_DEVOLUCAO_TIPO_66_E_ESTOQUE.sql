-- Query OK, 18756 rows affected (0.97 sec)
-- Insere os moviemento de devolução para o fornecedor baseado no encalhe das cotas
-- Com exceção dos produto que ainda estão do distribuidor (Esses produto serão excluídos após rollaut)
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
	66,
	1,
	me.QTDE,
	me.PRODUTO_EDICAO_ID,
	ep.ID,
 	null,
 	null,
	null,
 	null,
 	null,
	null,
	'CARGA_INICIAL'

    from movimento_estoque me,
	 estoque_produto ep,
	 hvnd h
 where me.tipo_movimento_id = 31
	and ep.produto_edicao_id = me.produto_edicao_id
	and h.produto_edicao_id = ep.produto_edicao_id
    and me.QTDE > 0
	and h.produto_edicao_id is not null
    and me.produto_edicao_id not in 
(select distinct(mec.produto_edicao_id) from movimento_estoque_cota mec, estoque_produto p
where mec.tipo_movimento_id = 26 
and mec.produto_edicao_id = p.produto_edicao_id 
and coalesce(p.qtde_devolucao_encalhe) > 0) );

-- Atualiza o estoque do distribuidor com as quantidades enviadas p/ o fornecedor
update estoque_produto
set qtde_devolucao_fornecedor = coalesce(qtde_devolucao_fornecedor,0)+coalesce((select sum(me.qtde) 
from movimento_estoque me 
where me.tipo_movimento_id = 66 
and me.produto_edicao_id = estoque_produto.produto_edicao_id), 0);


-- Robson Martins- Limpeza de tabela caso movimento precise ser reprocessado
/*
delete from movimento_estoque 
where TIPO_MOVIMENTO_ID = 66;
*/