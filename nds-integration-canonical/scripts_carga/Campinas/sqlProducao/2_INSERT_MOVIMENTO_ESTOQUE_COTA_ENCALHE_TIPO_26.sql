-- Necessário para não tomar erro de ID duplicado (pq não autoincrementa) ao 
-- inserir baseado em uma consulta
alter table movimento_estoque_cota modify id bigint(20) AUTO_INCREMENT;

-- Insere os movimentos de estoque da cota de encalhe (tipo_movimento_id = 26) onde houve devolução (estoque produto cota.QTDE_DEVOLVIDA > 0)
-- Alterar DATA_APROVACAO e DATA_CRIACAO para hoje
-- Query OK, 1288970 rows affected (5 min)
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
	date(sysdate()),
 	'APROVADO',
	min(h.data_recolhimento),
	date(sysdate()),
	26,
	1,
	epc.QTDE_DEVOLVIDA,
 	epc.produto_edicao_id,
	epc.cota_id,
	epc.id,
	'CARGA_INICIAL',
	1

    from estoque_produto_cota epc,
		 hvnd h
   where 
		epc.cota_id = h.cota_id
	and epc.PRODUTO_EDICAO_ID = h.produto_edicao_id 
    and epc.QTDE_DEVOLVIDA > 0
	and h.produto_edicao_id is not null
group by 1,2,3,5,6,7,8,9,10,11,12,13);


-- ====================######## ABAIXO Scripts Tests ###############============================

select count(1) from MOVIMENTO_ESTOQUE_COTA;

select count(1), produto_edicao_id from hvnd where QTDE_ENCALHE_HVCT > 0 group by 2;

select date(sysdate());

