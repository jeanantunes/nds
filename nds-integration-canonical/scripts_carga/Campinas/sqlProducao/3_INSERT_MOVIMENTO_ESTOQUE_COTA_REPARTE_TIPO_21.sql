-- Query OK, 1288970 rows affected (6 min)
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
	date(sysdate()),
 	'APROVADO',
	min(h.data_lancamento),
	date(sysdate()),
	21,
	1,
	epc.QTDE_RECEBIDA,
 	h.produto_edicao_id,
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
	and h.produto_edicao_id is not null
group by 1,2,3,5,6,7,8,9,10,11,12,13);




select sum(PRECO_COM_DESCONTO), sum(PRECO_VENDA), cota_id from MOVIMENTO_ESTOQUE_COTA group by 3;