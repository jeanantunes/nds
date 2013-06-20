--Query OK, 19056 rows affected (5.07 sec)

--DEPRECATED!! DO NOT USE THIS!
INSERT INTO estoque_produto
    (QTDE, QTDE_DEVOLUCAO_ENCALHE, PRODUTO_EDICAO_ID)

( select
	sum(QTDE_RECEBIDA),
	sum(QTDE_DEVOLVIDA),
	PRODUTO_EDICAO_ID
 from
	estoque_produto_cota
	group by PRODUTO_EDICAO_ID
);
