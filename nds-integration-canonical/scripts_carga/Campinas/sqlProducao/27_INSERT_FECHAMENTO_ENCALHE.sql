-- Query OK, 17550 rows affected (0.37 sec)
INSERT INTO FECHAMENTO_ENCALHE
(DATA_ENCALHE, QUANTIDADE, PRODUTO_EDICAO_ID)
(select data, 
	qtde,
	produto_edicao_id
 from movimento_estoque
 where tipo_movimento_id=31 );

INSERT INTO CONTROLE_FECHAMENTO_ENCALHE 
(select DATA_ENCALHE from fechamento_encalhe group by 1);
