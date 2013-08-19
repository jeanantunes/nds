-- Query OK, 1288970 rows affected (4 min)
-- Insere os estoque de produtos das cotas baseados no arquivo HVND.TXT
INSERT INTO estoque_produto_cota
    (QTDE_DEVOLVIDA, QTDE_RECEBIDA, VERSAO, COTA_ID, PRODUTO_EDICAO_ID)

( select
    csv.QTDE_ENCALHE_HVCT,
    csv.QTDE_REPARTE_HVCT,
    null,
    c.id,
    csv.produto_edicao_id
    from HVND csv,
         cota c
where c.numero_cota = csv.COD_COTA_HVCT
and csv.produto_edicao_id is not null);

