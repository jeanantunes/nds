-- Insere a saida domaterial promocional que esteja no estoque do distribuidor ( estqbox tipo 92)
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
 181,
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


-- Robson Martins- Limpeza de tabela caso movimento precise ser reprocessado
/*
delete from movimento_estoque 
where TIPO_MOVIMENTO_ID = 181;
*/


-- ====================######## ABAIXO Scripts Tests ###############============================


select * from tipo_movimento where id = 191;