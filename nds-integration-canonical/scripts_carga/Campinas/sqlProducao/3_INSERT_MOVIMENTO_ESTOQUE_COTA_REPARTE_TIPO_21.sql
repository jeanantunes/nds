-- Query OK, 1288970 rows affected (2 min 44.77 sec)
-- Insere movimentos de estoque cota de reparte (tipo 21) baseado no estoque de produtos da cota (onde a quantidade recebida > 0)
INSERT INTO MOVIMENTO_ESTOQUE_COTA
(
 APROVADO_AUTOMATICAMENTE,
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
	min(h.data_lancamento),
	'2013-06-15',
	21,
	1,
	epc.QTDE_RECEBIDA,
 	pe.id,
	epc.cota_id,
	epc.id,
	'CARGA_INICIAL',
	1

    from estoque_produto_cota epc,
		hvnd h
   where 
		epc.produto_edicao_id = h.produto_edicao_id
    and epc.cota_id = h.cota_id
    and epc.qtde_recebida > 0
group by 1,2,3,5,6,7,8,9,10,11,12,13);
