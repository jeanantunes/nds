DROP TABLE IF EXISTS VIEW_DESCONTO;

CREATE OR REPLACE VIEW VIEW_DESCONTO AS

SELECT
	
	CASE 
		tipo_produto.GRUPO_PRODUTO 
	WHEN 
		'OUTROS' 
	THEN 
		produto.DESCONTO
	ELSE
		desconto_produto_edicao.DESCONTO 
	END					 		 AS DESCONTO,
		cota.ID			 		 AS COTA_ID,
		produto_edicao.ID		 AS PRODUTO_EDICAO_ID,
		fornecedor.ID	 		 AS FORNECEDOR_ID
FROM
	desconto_produto_edicao,
	cota,
	produto,
	produto_edicao,
	tipo_produto,
	fornecedor
WHERE
	desconto_produto_edicao.COTA_ID = cota.ID
	AND desconto_produto_edicao.PRODUTO_EDICAO_ID = produto_edicao.ID
	AND desconto_produto_edicao.FORNECEDOR_ID = fornecedor.ID
	AND produto_edicao.PRODUTO_ID = produto.ID
	AND produto.TIPO_PRODUTO_ID = tipo_produto.ID;