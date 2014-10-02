--
-- VARIAVEIS DO SISTEMA
--

SET max_heap_table_size = 18446744073709551615;
SET tmp_table_size = 18446744073709551615;

-- ################################################################################################
-- ######################################   TABELAS DE CARGA    ###################################
-- ################################################################################################




--  CARGA_LANCAMENTO_MDC
DROP TABLE IF EXISTS CARGA_LANCAMENTO_MDC;

CREATE TABLE CARGA_LANCAMENTO_MDC (
id int(11) NOT NULL,
COD_AGENTE_LANP varchar(7),
COD_PRODUTO_LANP varchar(14),
COD_PRODIN varchar(8),
NUM_EDICAO int(11),
DATA_PREVISTA_LANCAMENTO_LANP varchar(10),
DATA_REAL_LANCAMENTO_LANP varchar(10),
TIPO_STATUS_LANCTO_LANP varchar(3),
TIPO_LANCAMENTO_LANP varchar(3),
VLR_PRECO_REAL_LANP varchar(10),
COD_PRODUTO_RCPR varchar(12),
NUM_RECOLTO_RCPR varchar(11),
DATA_PREVISTA_RECOLTO_RCPR varchar(10),
DATA_REAL_RECOLTO_RCPR varchar(10),
TIPO_STATUS_RECOLTO_RCPR varchar(45),
PRIMARY KEY (id))
-- ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/CARGA_LANCAMENTO_MDC.CAR' INTO TABLE CARGA_LANCAMENTO_MDC COLUMNS TERMINATED BY ';' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 LINES;

select 'CARGA_LANCAMENTO_MDC:',count(*) from CARGA_LANCAMENTO_MDC; -- Log

ALTER TABLE CARGA_LANCAMENTO_MDC
ADD COLUMN produto_edicao_id bigint AFTER TIPO_STATUS_RECOLTO_RCPR;

update CARGA_LANCAMENTO_MDC cm,produto p, produto_edicao pe 
set cm.produto_edicao_id = pe.id 
where p.id = pe.produto_id 
and lpad(cm.cod_prodin,8,'0') = p.codigo 
and cm.num_edicao = pe.numero_edicao;


-- #########################################################################################################################################
-- ###########################################  CARGA_LANCAMENTO_MDC  ######################################################################
-- #########################################################################################################################################


--
-- CARGA_LANCAMENTO_MDC
--

update lancamento l,CARGA_LANCAMENTO_MDC clm
set 
l.DATA_LCTO_PREVISTA =
IF (clm.data_prevista_lancamento_lanp ='', 
IF (data_real_lancamento_lanp='',l.DATA_LCTO_PREVISTA,STR_TO_DATE(clm.data_real_lancamento_lanp,'%d/%m/%Y')),
STR_TO_DATE(clm.data_prevista_lancamento_lanp,'%d/%m/%Y')),
l.DATA_LCTO_DISTRIBUIDOR =
IF (clm.data_real_lancamento_lanp ='', 
IF (data_prevista_lancamento_lanp='',l.DATA_LCTO_DISTRIBUIDOR,STR_TO_DATE(clm.data_prevista_lancamento_lanp,'%d/%m/%Y')),
STR_TO_DATE(clm.data_real_lancamento_lanp,'%d/%m/%Y')),
l.DATA_REC_PREVISTA =
IF (clm.data_prevista_recolto_rcpr ='', 
IF (data_real_lancamento_lanp='',l.DATA_REC_PREVISTA,STR_TO_DATE(clm.data_real_lancamento_lanp,'%d/%m/%Y')),
STR_TO_DATE(clm.data_prevista_recolto_rcpr,'%d/%m/%Y')),
l.DATA_REC_DISTRIB =
IF (data_real_recolto_rcpr ='', 
IF (data_prevista_recolto_rcpr='',l.DATA_REC_DISTRIB,STR_TO_DATE(clm.data_prevista_recolto_rcpr,'%d/%m/%Y')),
STR_TO_DATE(clm.data_real_lancamento_lanp,'%d/%m/%Y'))
where clm.produto_edicao_id = l.produto_edicao_id
and l.status in ('CONFIRMADO', 'FECHADO', 'PLANEJADO');









