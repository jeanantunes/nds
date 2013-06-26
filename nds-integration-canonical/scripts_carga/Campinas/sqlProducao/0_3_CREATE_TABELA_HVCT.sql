drop table HVND;

create table HVND
 (COD_COTA_HVCT int,
 COD_PRODUTO_HVCT varchar(12),
 QTDE_REPARTE_HVCT int,
 QTDE_ENCALHE_HVCT int,
 DATA_LANCAMENTO_STR varchar(10),
 DATA_RECOLHIMENTO_STR varchar(10),
 STATUS char(1), 
 DATA_LANCAMENTO date,
 DATA_RECOLHIMENTO date,
 cod_produto varchar(8),
 num_edicao int,
 produto_edicao_id bigint,
 cota_id bigint
 );

 -- Popula a tabela temporário com todas as movimentações de estoques de cotas para os produtos
LOAD DATA LOCAL INFILE 'HVND.TXT' INTO TABLE HVND COLUMNS TERMINATED BY ';' LINES TERMINATED BY ';\n';

update HVND set 
	cod_produto=substring(COD_PRODUTO_HVCT, -12, 8), 
	num_edicao=substring(COD_PRODUTO_HVCT, -4),
	DATA_LANCAMENTO = STR_TO_DATE(DATA_LANCAMENTO_STR, '%d/%m/%Y'),
	DATA_RECOLHIMENTO = STR_TO_DATE(DATA_RECOLHIMENTO_STR, '%d/%m/%Y');

-- script de validação atualização dos campos cod_produto, num_edicao, DATA_LANCAMENTO, DATA_RECOLHIMENTO
select * from HVND where 
cod_produto <> substring(COD_PRODUTO_HVCT, -12, 8)
and num_edicao <> substring(COD_PRODUTO_HVCT, -4)
and DATA_LANCAMENTO <> STR_TO_DATE(DATA_LANCAMENTO_STR, '%d/%m/%Y')
and DATA_RECOLHIMENTO <> STR_TO_DATE(DATA_RECOLHIMENTO_STR, '%d/%m/%Y')
;

update HVND set produto_edicao_id = 
(select pe.id from produto_edicao pe, produto p 
where p.id = pe.produto_id 
and p.codigo = HVND.cod_produto 
and pe.numero_edicao = HVND.num_edicao);

-- Validação update acima 80.808 sec / 0.000 sec
select * from HVND
where produto_edicao_id <> (select pe.id from produto_edicao pe, produto p 
where p.id = pe.produto_id 
and p.codigo = HVND.cod_produto 
and pe.numero_edicao = HVND.num_edicao)
;

update HVND set cota_id = 
(select c.id from cota c
where c.numero_cota = HVND.COD_COTA_HVCT);

-- Validação update acima
select * from HVND h
where h.cota_id <>
(select c.id from cota c
where c.numero_cota = h.COD_COTA_HVCT)
;

desc HVND;

ALTER TABLE `HVND` ADD INDEX `hvnd_cota_id` (`COD_COTA_HVCT` ASC, `cod_produto` ASC, `num_edicao` ASC, `produto_edicao_id` ASC) ;
