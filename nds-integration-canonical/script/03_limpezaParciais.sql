
-- deleta peridos lancamento parcial
DELETE FROM periodo_lancamento_parcial;

-- deleta lancamento parcial
DELETE FROM lancamento_parcial;

-- tabela temporaria que armazena os lançamentos que não podem ser excluidos
CREATE TABLE lancamento_backup (id int);

-- insere os lançamentos que não podem ser excluidos
INSERT INTO lancamento_backup 
SELECT l.ID from lancamento l
WHERE l.TIPO_LANCAMENTO = 'PARCIAL'
AND l.DATA_LCTO_DISTRIBUIDOR =
	(SELECT min(lan.DATA_LCTO_DISTRIBUIDOR) FROM lancamento lan WHERE lan.produto_edicao_id = l.PRODUTO_EDICAO_ID)
GROUP BY l.PRODUTO_EDICAO_ID;

-- deleta historico de lançamentos
DELETE h FROM historico_lancamento h JOIN lancamento l ON l.ID = h.LANCAMENTO_ID   
WHERE 
	l.TIPO_LANCAMENTO = 'PARCIAL' AND
h.LANCAMENTO_ID NOT IN (
	SELECT lancamento_backup.id FROM lancamento_backup 
);

-- deleta associação de lançamento com recebimento fisico
DELETE lrf FROM lancamento_item_receb_fisico lrf
JOIN lancamento l ON l.ID = lrf.LANCAMENTO_ID
WHERE l.TIPO_LANCAMENTO = 'PARCIAL' 
AND l.ID NOT IN (
	SELECT lancamento_backup.id FROM lancamento_backup 
);

-- deleta os lançamentos parciais	
DELETE fp FROM furo_produto fp 
INNER JOIN lancamento l ON fp.lancamento_id = l.id 
WHERE lancamento_id NOT IN (
	SELECT lancamento_backup.id FROM lancamento_backup 
)
AND TIPO_LANCAMENTO = 'PARCIAL';
DELETE FROM lancamento WHERE lancamento.ID NOT IN (
	SELECT lancamento_backup.id FROM lancamento_backup 
)
AND TIPO_LANCAMENTO = 'PARCIAL';

-- remove tabela temporaria
DROP TABLE lancamento_backup;
