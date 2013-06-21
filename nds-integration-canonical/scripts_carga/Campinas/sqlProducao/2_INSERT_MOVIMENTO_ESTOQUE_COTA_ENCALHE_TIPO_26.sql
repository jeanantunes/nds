-- Necessário para não tomcar erro de ID duplicado (pq não autoincrementa) ao inserir baseado em uma consulta
alter table movimento_estoque_cota modify id bigint(20) AUTO_INCREMENT;

-- Insere os movimentos de estoque da cota de encalhe (tipo_movimento_id = 26) onde houve devolução (estoque produto cota.QTDE_DEVOLVIDA > 0)
-- Query OK, 1288970 rows affected (4 min 1.14 sec)
INSERT INTO MOVIMENTO_ESTOQUE_COTA
(APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 COTA_ID,
 ESTOQUE_PROD_COTA_ID,
 ORIGEM,
 APROVADOR_ID
)
(select 
	true,
	'2013-06-15',
 	'APROVADO',
	min(h.data_recolhimento),
	'2013-06-15',
	26,
	1,
	epc.QTDE_DEVOLVIDA,
 	epc.produto_edicao_id,
	epc.cota_id,
	epc.id,
	'CARGA_INICIAL',
	1

    from estoque_produto_cota epc,
		 hvdn h
   where 
		epc.cota_id = h.cota_id
	and pe.id = h.produto_edicao_id 
    and epc.QTDE_DEVOLVIDA > 0
group by 1,2,3,5,6,7,8,9,10,11,12,13);
