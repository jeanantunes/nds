--
-- VARIAVEIS DO SISTEMA
--

SET max_heap_table_size = 18446744073709551615;
SET tmp_table_size = 18446744073709551615;

-- SET max_heap_table_size = 7000000000;
-- SET tmp_table_size = 7000000000;	


-- SET GLOBAL general_log = 'ON';
-- SET GLOBAL slow_query_log = 'ON';


-- ################################################################################################
-- #########################################  CARGA DE TABELAS    #################################
-- ################################################################################################

select 'Inicio: ',sysdate(); -- Log 

-- 
-- HVND
--

DROP TABLE IF EXISTS HVND; 

create table HVND (
COD_COTA_HVCT INT,
COD_PRODUTO_HVCT VARCHAR(12),
PRECO FLOAT,
QTDE_REPARTE_HVCT INT,
QTDE_ENCALHE_HVCT INT,
DATA_LANCAMENTO_STR VARCHAR(10),
DATA_RECOLHIMENTO_STR VARCHAR(10),
STATUS CHAR(1),
CONSIGNADO boolean,
DEVOLVE_ENCALHE boolean
) ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/HVND.TXT' INTO TABLE HVND COLUMNS TERMINATED BY ';' LINES TERMINATED BY ';\r\n';

update HVND set CONSIGNADO =1;

LOAD DATA INFILE '/opt/rollout/load_files/HVND_AVISTA.TXT' INTO TABLE HVND COLUMNS TERMINATED BY ';' LINES TERMINATED BY ';\r\n';

update HVND set CONSIGNADO =0 where CONSIGNADO is null;
update HVND set DEVOLVE_ENCALHE =1;

-- FIXME FAZER UPDATE AQUI DAS COTAS QUE NAO DEVOLVEM ENCALHE
-- ESSAS COTAS DEVERAM SER CRIADAS NO ARQUIVO carga_inicial.sql do respectivo distribuidor

ALTER TABLE HVND
ADD COLUMN DATA_LANCAMENTO DATE,
ADD COLUMN DATA_RECOLHIMENTO DATE,
ADD COLUMN cod_produto VARCHAR(8),
ADD COLUMN num_edicao INT,
ADD COLUMN produto_edicao_id bigint,
ADD COLUMN cota_id bigint AFTER STATUS;

update HVND set 
cod_produto=substring(COD_PRODUTO_HVCT, -12, 8), 
num_edicao=substring(COD_PRODUTO_HVCT, -4),
DATA_LANCAMENTO = STR_TO_DATE(DATA_LANCAMENTO_STR, '%d/%m/%Y'),
DATA_RECOLHIMENTO = STR_TO_DATE(DATA_RECOLHIMENTO_STR, '%d/%m/%Y');

ALTER TABLE HVND 
DROP COD_PRODUTO_HVCT, 
-- DROP PRECO, 
DROP DATA_LANCAMENTO_STR,
DROP DATA_RECOLHIMENTO_STR;


-- ##################### FIXME    #######################
-- Remover issso
delete from hvnd where cod_produto = '28681001' and num_edicao = 1013;
delete from hvnd where cod_produto = '27801001' and num_edicao = 192;
-- #######################################################

update HVND h,produto_edicao pe, produto p 
set h.produto_edicao_id = pe.id
where p.id = pe.produto_id 
and p.codigo = h.cod_produto 
and pe.numero_edicao = h.num_edicao;

delete from HVND where produto_edicao_id is null;

update HVND set DATA_RECOLHIMENTO = (select min(l.DATA_REC_DISTRIB) from lancamento l 
where l.produto_edicao_id = HVND.produto_edicao_id)
where DATA_RECOLHIMENTO = '0000-00-00' 
and STATUS = 'F';

update HVND set cota_id = (select c.id from cota c where c.numero_cota = HVND.COD_COTA_HVCT);

select '*** HVND Cotas nulas:';
select GROUP_CONCAT(COD_COTA_HVCT) from HVND where cota_id is null;

ALTER TABLE `HVND` ADD INDEX `hvnd_cota_id` (`COD_COTA_HVCT` ASC, `cod_produto` ASC, `num_edicao` ASC, `produto_edicao_id` ASC) ;

ALTER TABLE `HVND` ADD INDEX `hvnd_cota_status` (`STATUS` ASC) ;

ALTER TABLE `HVND` ADD INDEX `cota_id` (`cota_id` ASC);

ALTER TABLE `HVND` ENGINE=MEMORY;

select '*** HVND Produtos nao encontrados: ';
select GROUP_CONCAT(a.cod_produto SEPARATOR ',') from (
select distinct cod_produto 
from HVND 
where status = 'A' 
and cod_produto not in(select codigo from produto)) a;

select '*** HVND Edicoes nao encontradas: ';
select distinct h.cod_produto,h.num_edicao
from (select distinct cod_produto,num_edicao from hvnd 
-- where status = 'A'
) h
left join (select p.codigo,pe.numero_edicao from produto p,produto_edicao pe where p.id = pe.produto_id) ppe
on (h.cod_produto = ppe.codigo and h.num_edicao = ppe.numero_edicao) 
where (ppe.codigo is null or ppe.numero_edicao is null);

select '*** HVND produto_edicao_id nulos: ';
-- select * from HVND where produto_edicao_id is null;

select 'CONSIGNADO: ',sum(preco*qtde_reparte_hvct) from HVND ; -- where status = 'A';
select 'BALANCEADO: ',sum(preco*qtde_reparte_hvct) from HVND where data_recolhimento <> '0000-00-00' and status = 'A';
select 'EXPEDIDO  : ',sum(preco*qtde_reparte_hvct) from HVND where data_recolhimento = '0000-00-00' and status = 'A';
select 'FECHADO   : ',sum(preco* (qtde_reparte_hvct-qtde_encalhe_hvct)) from HVND where status = 'F';

select 'HVND: ',sysdate(); -- Log 

-- delete from HVND where produto_edicao_id is null;
-- 
-- ESTQBOX
--

DROP TABLE IF EXISTS ESTQBOX;

create table ESTQBOX (
linha_vazia varchar(1),
tipo int,
box int,
nome_box varchar(10),
produto varchar(8),
edicao int,
nome_produto varchar(45),
quantidade int) 
 ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/ESTQBOX.NEW' INTO TABLE ESTQBOX COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n' IGNORE 1 LINES;

ALTER TABLE ESTQBOX
ADD COLUMN produto_edicao_id bigint AFTER quantidade;

update ESTQBOX stq,produto_edicao pe, produto p 
set stq.produto_edicao_id = pe.id
where p.id = pe.produto_id 
and p.codigo = produto 
and pe.numero_edicao = edicao;

select '*** ESTQBOX Cotas nulas:';
-- select GROUP_CONCAT(cota_id) from ESTQBOX where cota_id is null;

select 'ESTQBOX: ',sysdate(); -- Log 


/*
-- 
-- ESTQMOV
--

DROP TABLE IF EXISTS ESTQMOV;

create table ESTQMOV (
linha_vazia varchar(1),
tipo int,
produto varchar(8),
edicao int,
DATA date,
NRO_DOCTO int,
TIPO_MOVTO int,
ORIGEM int,
DESTINO int,
QUANTIDADE int,
PRECO_CAPA decimal(18,2),
PERC_DESCTO decimal(18,2),
DOCTO_ORIGEM int,
FLAG_ESTORNO varchar(1)) ENGINE=MEMORY;

ALTER TABLE ESTQMOV
ADD COLUMN produto_edicao_id bigint AFTER FLAG_ESTORNO;

LOAD DATA LOCAL INFILE '/opt/rollout/load_files/ESTQMOV.NEW' INTO TABLE estqmov COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n';

-- update estqmov set produto_edicao_id = (select pe.id from produto_edicao pe, produto p 
-- where p.id = pe.produto_id 
-- and p.codigo = produto 
-- and pe.numero_edicao = edicao);

select 'ESTQMOV: ',sysdate(); -- Log
*/

-- 
-- MOV_CRED
--

DROP TABLE IF EXISTS MOV_CRED;

create table MOV_CRED (
linha_inicial varchar(1), 
data varchar(8),
numero_cota int,
tipo_credito int,
desc_credito varchar(255),
valor varchar(11)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/ARQCD08.NEW' INTO TABLE MOV_CRED COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n';

ALTER TABLE MOV_CRED
ADD COLUMN valor_decimal decimal(11,2),
ADD COLUMN data_real date,
ADD COLUMN cota_id bigint AFTER valor;

update mov_cred set valor_decimal = (cast(valor as decimal(12.2))/100), data_real = str_to_date(data, '%Y%m%d'), cota_id = (
select c.id from cota c where c.numero_cota = mov_cred.numero_cota);

update MOV_CRED mc,cota c set mc.cota_id = c.id
where mc.numero_cota=c.numero_cota;

select '*** MOV_CRED Cotas nulas:  ',count(*) from MOV_CRED where cota_id is null;

select 'MOV_CRED: ',sysdate(); -- Log

-- 
-- MOV_DEB
--

DROP TABLE IF EXISTS MOV_DEB;

create table MOV_DEB (
linha_inicial varchar(1),
data varchar(8),
numero_cota int,
tipo_debito int,
desc_debito varchar(255),
valor varchar(11)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/ARQDB97.NEW' INTO TABLE MOV_DEB COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n';

ALTER TABLE MOV_DEB
ADD COLUMN valor_decimal decimal(11,2),
ADD COLUMN data_real date,
ADD COLUMN cota_id bigint AFTER valor;

update mov_deb set valor_decimal = (cast(valor as decimal(12.2))/100), data_real = str_to_date(data, '%Y%m%d'), cota_id = (
select c.id from cota c where c.numero_cota = mov_deb.numero_cota);

update MOV_DEB md,cota c set md.cota_id = c.id
where md.numero_cota=c.numero_cota;

select '*** MOV_DEB Cotas nulas:  ',count(*) from MOV_DEB where cota_id is null;

select 'MOV_DEB: ',sysdate(); -- Log

-- 
-- CARGA_LANCAMENTO_MDC
--
 
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
 ENGINE=MEMORY
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

select 'CARGA_LANCAMENTO_MDC: ',sysdate(); -- Log

-- ################################################################################################
-- #########################################  MOVIMENTACOES    ####################################
-- ################################################################################################

--
-- LANCAMENTO
--

select 'LANCAMENTO: ',count(*) from LANCAMENTO;

-- Garante que não tenha nenhum movimento para as cotas / distribuidor sem lançamento
insert into lancamento
(alterado_interface,
data_criacao,
data_lcto_distribuidor,
data_lcto_prevista,
data_rec_distrib,
data_rec_prevista,
data_status,
status,
tipo_lancamento,
produto_edicao_id)
(select 
true,
date(sysdate()),
data_lancamento,
data_lancamento,
data_recolhimento,
data_recolhimento,
date(sysdate()),
'EXPEDIDO',
'LANCAMENTO',
produto_edicao_id
from HVND 
where produto_edicao_id not in (select produto_edicao_id from lancamento)
group by produto_edicao_id);

select 'LANCAMENTO (HVND): ',count(*) from LANCAMENTO;

select 'LANCAMENTO: ',sysdate(); -- Log


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
IF (data_real_recolto_rcpr='',l.DATA_REC_PREVISTA,STR_TO_DATE(clm.data_real_recolto_rcpr,'%d/%m/%Y')),
STR_TO_DATE(clm.data_prevista_recolto_rcpr,'%d/%m/%Y')),
l.DATA_REC_DISTRIB =
IF (data_real_recolto_rcpr ='', 
IF (data_prevista_recolto_rcpr='',l.DATA_REC_DISTRIB,STR_TO_DATE(clm.data_prevista_recolto_rcpr,'%d/%m/%Y')),
STR_TO_DATE(clm.data_real_recolto_rcpr,'%d/%m/%Y'))
where clm.produto_edicao_id = l.produto_edicao_id
and l.status in ('CONFIRMADO', 'FECHADO', 'PLANEJADO');

-- ATUALIZA REC PELA PEB

update lancamento
set DATA_REC_PREVISTA = (select adddate(DATA_LCTO_DISTRIBUIDOR, peb)
from produto_edicao
where produto_edicao.id = lancamento.produto_edicao_id limit 1)
WHERE (DATA_REC_DISTRIB = '0000-00-00' or DATA_REC_DISTRIB = NULL)
or (DATA_REC_PREVISTA = '0000-00-00' or DATA_REC_PREVISTA = NULL);

update lancamento
set DATA_REC_DISTRIB = DATA_REC_PREVISTA
where DATA_REC_DISTRIB = '0000-00-00';

-- ATUALIZA PRECO

-- 
-- MOVIMENTO_FINANCEIRO_COTA
--


INSERT INTO movimento_financeiro_cota
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,TIPO_MOVIMENTO_ID,
USUARIO_ID,LANCAMENTO_MANUAL,COTA_ID,OBSERVACAO,VALOR,fornecedor_id)	
(select true,data_real,'CARGA','APROVADO',data_real,
data_real,null,22,1,true,cota_id,concat('CARGA INICIAL - ', desc_credito),valor_decimal,1 -- Default DINAP 
from MOV_CRED where cota_id is not null);

INSERT INTO movimento_financeiro_cota
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,TIPO_MOVIMENTO_ID,USUARIO_ID,LANCAMENTO_MANUAL,
COTA_ID,OBSERVACAO, valor,fornecedor_id)
(select true,data_real,'CARGA','APROVADO',data_real,data_real,null,23,1,true,cota_id,concat('CARGA INICIAL - ', desc_debito),valor_decimal,1
from MOV_DEB where cota_id is not null);

select 'MOVIMENTO_FINANCEIRO_COTA: ',sysdate(); -- Log

-- 
-- ESTOQUE_PRODUTO_COTA
-- 


-- Insere os estoque de produtos das cotas baseados no arquivo HVND.TXT
INSERT INTO estoque_produto_cota (QTDE_DEVOLVIDA, QTDE_RECEBIDA, COTA_ID, PRODUTO_EDICAO_ID)
( select
csv.QTDE_ENCALHE_HVCT,
csv.QTDE_REPARTE_HVCT,
c.id,
csv.produto_edicao_id
from HVND csv,cota c
where c.numero_cota = csv.COD_COTA_HVCT
and csv.produto_edicao_id is not null
group by c.id,csv.produto_edicao_id);

select 'ESTOQUE_PRODUTO_COTA: ',sysdate(); -- Log

-- 
-- ESTOQUE_PRODUTO (MEMORIA) FIXME **** CONFIRMAR SEMPRE QUAL OS NUMEROS DE BOX DO DISTRIBUIDOR
-- 

DROP TABLE IF EXISTS estoque_produto_memoria;

CREATE TABLE `estoque_produto_memoria` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `QTDE_DANIFICADO` decimal(18,4) DEFAULT NULL,
  `QTDE_DEVOLUCAO_ENCALHE` decimal(18,4) DEFAULT NULL,
  `QTDE_DEVOLUCAO_FORNECEDOR` decimal(18,4) DEFAULT NULL,
  `QTDE_SUPLEMENTAR` decimal(18,4) DEFAULT NULL,
  `QTDE_PERDA` decimal(18,4) DEFAULT NULL,
  `QTDE_GANHO` decimal(18,4) DEFAULT NULL,
  `VERSAO` bigint(20) NOT NULL DEFAULT '0',
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `qtde_juramentado` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PRODUTO_EDICAO_ID` (`PRODUTO_EDICAO_ID`)) 
 ENGINE=MEMORY 
DEFAULT CHARSET=utf8;

-- Insere estoque do distribuidor baseado no arquivo ESTQBOX.NEW
insert into estoque_produto_memoria (QTDE, QTDE_DEVOLUCAO_ENCALHE, QTDE_SUPLEMENTAR, PRODUTO_EDICAO_ID)
select 
sum(case when box=150 then quantidade else 0 end) as QTDE, 
sum(case when box=181 then quantidade else 0 end) as QTDE_DEVOLUCAO_ENCALHE,
sum(case when box in(140,142) then quantidade else 0 end) as QTDE_SUPLEMENTAR, 
produto_edicao_id 
from ESTQBOX 
where box not in (190,191) -- 191=Rio,Santos / 92=Campinas 
group by 4;

-- insere estoque de produto do fornecedor baseado nos tipos A.R.PROM. do arquivo ESTQBOX
insert into estoque_produto_memoria (produto_edicao_id)
(select distinct produto_edicao_id from ESTQBOX where box = 191 -- 92 FIXME Alterar para o box correspondente
and produto_edicao_id not in (select produto_edicao_id from estoque_produto_memoria));

select 'ESTOQUE_PRODUTO: ',sysdate(); -- Log

-- 
-- MOVIMENTO_ESTOQUE_COTA (MEMORIA)
-- 

DROP TABLE IF EXISTS movimento_estoque_cota_memoria;

CREATE TABLE `movimento_estoque_cota_memoria` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  -- `APROVADO_AUTOMATICAMENTE` tinyint(1) DEFAULT NULL,
  -- `DATA_APROVACAO` date DEFAULT NULL,
  -- `MOTIVO` varchar(255) DEFAULT NULL,
  -- `STATUS` varchar(255) DEFAULT NULL,
  `DATA` date NOT NULL,
  -- `DATA_CRIACAO` date NOT NULL,
  -- `APROVADOR_ID` bigint(20) DEFAULT NULL,
  `TIPO_MOVIMENTO_ID` smallint NOT NULL,
  -- `USUARIO_ID` bigint(20) NOT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL, -- *
  `STATUS_ESTOQUE_FINANCEIRO` tinyint(1) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL, -- *
  `COTA_ID` int NOT NULL, -- *
  `ESTOQUE_PROD_COTA_ID` bigint(20) DEFAULT NULL, -- *
  -- `ESTUDO_COTA_ID` bigint(20) DEFAULT NULL,
  `LANCAMENTO_ID` bigint(20) DEFAULT NULL, -- *
  -- `DATA_INTEGRACAO` date DEFAULT NULL,
  -- `STATUS_INTEGRACAO` varchar(255) DEFAULT NULL,
  -- `MOVIMENTO_ESTOQUE_COTA_FURO_ID` bigint(20) DEFAULT NULL,
  -- `ORIGEM` varchar(50) DEFAULT 'MANUAL',
  -- `LANCAMENTO_DIFERENCA_ID` bigint(20) DEFAULT NULL,
  -- `DATA_LANCAMENTO_ORIGINAL` date DEFAULT NULL,
  -- `MOVIMENTO_FINANCEIRO_COTA_ID` bigint(20) DEFAULT NULL,
  `PRECO_COM_DESCONTO` decimal(18,4) DEFAULT NULL,
  `PRECO_VENDA` decimal(18,4) DEFAULT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  -- `NOTA_ENVIO_ITEM_NOTA_ENVIO_ID` bigint(20) DEFAULT NULL,
  -- `NOTA_ENVIO_ITEM_SEQUENCIA` int(11) DEFAULT NULL,
  -- `MOVIMENTO_ESTOQUE_COTA_JURAMENTADO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)) ENGINE=MEMORY 
;


INSERT INTO MOVIMENTO_ESTOQUE_COTA_MEMORIA
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,COTA_ID,ESTOQUE_PROD_COTA_ID,LANCAMENTO_ID,status_estoque_financeiro)
(select 
min(h.data_recolhimento),
26,
epc.QTDE_DEVOLVIDA,
epc.produto_edicao_id,
epc.cota_id,
epc.id,
l.ID,
1
from estoque_produto_cota epc, HVND h, lancamento l
where epc.cota_id = h.cota_id
and epc.PRODUTO_EDICAO_ID = h.produto_edicao_id 
and l.produto_edicao_id =h.produto_edicao_id 
and epc.PRODUTO_EDICAO_ID = l.produto_edicao_id 
and h.status = 'F'
and h.produto_edicao_id is not null
group by 2,3,5,6,7);

-- Insere movimentos de estoque cota de reparte (tipo 21) baseado no estoque de produtos da cota (onde a quantidade recebida > 0)
INSERT INTO MOVIMENTO_ESTOQUE_COTA_MEMORIA
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,COTA_ID,ESTOQUE_PROD_COTA_ID,LANCAMENTO_ID,status_estoque_financeiro)
(select 
min(h.data_lancamento),
21,
epc.QTDE_RECEBIDA,
h.produto_edicao_id,
epc.cota_id,
epc.id,
l.ID,
case when h.status ='A' then 0 else 1 end
from estoque_produto_cota epc,HVND h,lancamento l
where epc.produto_edicao_id = h.produto_edicao_id
and epc.cota_id = h.cota_id
and l.produto_edicao_id =h.produto_edicao_id 
and epc.PRODUTO_EDICAO_ID = l.produto_edicao_id 
and epc.qtde_recebida > 0
and h.produto_edicao_id is not null
group by 2,3,5,6);


select 'MOVIMENTO_ESTOQUE_COTA: ',sysdate(); -- Log

-- *** FIXME AJUSTE
-- Insere edições de produtos que não existem na estoque produto mas existem no estoque de produto da cota.
-- Essa anomalia pode acontecer pq o arquivo HVND que popula o estoque produto pode não conter 
-- movimentos que estão na ESTQBOX (que insere movimentações de estoque baseados no PDV das cotas)
-- FIXME Trocar de lugar apos analize
insert into estoque_produto_memoria (produto_edicao_id)
(select distinct produto_edicao_id from movimento_estoque_cota_memoria where produto_edicao_id not in (select distinct produto_edicao_id from estoque_produto_memoria) 
group by 1);

-- 
-- MOVIMENTO_ESTOQUE (MEMORIA)
-- 

DROP TABLE IF EXISTS movimento_estoque_memoria;

CREATE TABLE `movimento_estoque_memoria` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
--  `APROVADO_AUTOMATICAMENTE` tinyint(1) DEFAULT NULL,
--  `DATA_APROVACAO` date DEFAULT NULL,
--  `MOTIVO` varchar(255) DEFAULT NULL,
--  `STATUS` varchar(255) DEFAULT NULL,
  `DATA` date NOT NULL,
--  `DATA_CRIACAO` date NOT NULL,
--  `APROVADOR_ID` bigint(20) DEFAULT NULL,
  `TIPO_MOVIMENTO_ID` bigint(20) NOT NULL,
--  `USUARIO_ID` bigint(20) NOT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `ESTOQUE_PRODUTO_ID` bigint(20) DEFAULT 0,
--  `ITEM_REC_FISICO_ID` bigint(20) DEFAULT NULL,
--  `DATA_INTEGRACAO` date DEFAULT NULL,
--  `STATUS_INTEGRACAO` varchar(255) DEFAULT NULL,
--  `COD_ORIGEM_MOTIVO` varchar(255) DEFAULT NULL,
--  `DAT_EMISSAO_DOC_ACERTO` datetime DEFAULT NULL,
--  `NUM_DOC_ACERTO` bigint(20) DEFAULT NULL,
--  `ORIGEM` varchar(50) DEFAULT 'MANUAL',
--  `FURO_PRODUTO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)) 
ENGINE=MEMORY 
;

-- Insere movimento de estoque de recebimento de encalhe do distribuidor baseado no movimento de estoque de envio de encalhe da cota 
INSERT INTO movimento_estoque_memoria
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
min(mec.data), -- min(mec.data_recolhimento),
31,
sum(mec.QTDE),
mec.PRODUTO_EDICAO_ID,
ep.ID
from movimento_estoque_cota_memoria mec,estoque_produto_memoria ep
where ep.produto_edicao_id = mec.produto_edicao_id
and mec.tipo_movimento_id = 26
group by 2,4,5
having sum(mec.QTDE) > 0);

select 'MOVIMENTO_ESTOQUE 31: ',sysdate(); -- Log

-- Insere no movimento de envio de reparte do distribuidor baseado no movimento de estoque de recebimento de reparte da cota
INSERT INTO movimento_estoque_memoria
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
min(mec.data), -- min(mec.data_lancamento),
13,
sum(mec.QTDE),
mec.PRODUTO_EDICAO_ID,
ep.ID
from movimento_estoque_cota_memoria mec,estoque_produto_memoria ep
where ep.produto_edicao_id = mec.produto_edicao_id
and mec.tipo_movimento_id = 21
group by 2,4,5
having sum(mec.QTDE) > 0);

select 'MOVIMENTO_ESTOQUE 13: ',sysdate(); -- Log

-- Insere ENTRADAS de recebimento fisico no movimento estoque do distribuidor 
-- baseado na ENTRADA de REPARTE do movimento estoque da cota
INSERT INTO movimento_estoque_memoria
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
min(mec.data), -- min(mec.data_lancamento),
20,
sum(mec.qtde),
mec.PRODUTO_EDICAO_ID,
null
from movimento_estoque_cota_memoria mec
where mec.tipo_movimento_id = 21
and mec.QTDE > 0
group by 2,4,5);

select '1 MOVIMENTO_ESTOQUE 20: ',sysdate(); -- Log

-- Atualizando estoque_produto_id no movimento_estoque para manter as referências do banco
update movimento_estoque_memoria 
set ESTOQUE_PRODUTO_ID=(select ep.id from estoque_produto_memoria ep 
where ep.produto_edicao_id=movimento_estoque_memoria.produto_edicao_id) 
where tipo_movimento_id=20;

-- Atualizando os movimentos de recebimento físico com as quantidades inseridas pelo arquivo de ESTQ_BOX,
-- ou seja, Deixando a quantidade recebida igual a quantidade de envio aos jornaleiro ao estoque do distribuidor
-- Robson Martins - Comentado -- + coalesce(ep.qtde_suplementar,0) suplementar já sensibilizado
update movimento_estoque_memoria 
set QTDE=COALESCE(QTDE,0)+(select coalesce(ep.qtde,0) -- + coalesce(ep.qtde_suplementar,0) 
from estoque_produto_memoria ep where ep.produto_edicao_id=movimento_estoque_memoria.produto_edicao_id) 
where tipo_movimento_id=20;

select 'UPDATE MOVIMENTO_ESTOQUE 20: ',sysdate(); -- Log

-- Insere movimentos de estoque de recebimento fisico do distribuidor baseado nos tipos A.R.PROM. do arquivo ESTQBOX
insert into movimento_estoque_memoria
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
min(h.data_lancamento),
 20,
 eb.QUANTIDADE,
 eb.PRODUTO_EDICAO_ID,
 pe.id
 from ESTQBOX eb,estoque_produto_memoria pe, HVND h
 where h.produto_edicao_id = pe.produto_edicao_id
 and eb.box = 191 -- 191=Rio,Santos / 92=Campinas 
 and pe.produto_edicao_id = eb.produto_edicao_id
group by 2,3,4,5);

select '2 MOVIMENTO_ESTOQUE 20: ',sysdate(); -- Log


-- Insere a saida do material promocional que esteja no estoque do distribuidor ( ESTQBOX tipo 92)
insert into movimento_estoque_memoria 
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
 min(h.data_lancamento),
 181,
 eb.QUANTIDADE,
 eb.PRODUTO_EDICAO_ID,
 pe.id
 from ESTQBOX eb,estoque_produto_memoria pe, HVND h
 where h.produto_edicao_id = pe.produto_edicao_id
 and eb.box = 191 -- 191=Rio,Santos / 92=Campinas 
 and pe.produto_edicao_id = eb.produto_edicao_id
group by 2,3,4,5);

-- Insere os moviemento de devolução para o fornecedor baseado no encalhe das cotas Com exceção dos produtos que ainda estão do distribuidor (Esses produto serão excluídos após rollaut)
INSERT INTO movimento_estoque_memoria 
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
me.data,
66,
me.QTDE,
me.PRODUTO_EDICAO_ID,
ep.ID
from movimento_estoque_memoria me,estoque_produto_memoria ep
where me.tipo_movimento_id = 31
and ep.produto_edicao_id = me.produto_edicao_id
and me.produto_edicao_id in (
select distinct(mec.produto_edicao_id) from movimento_estoque_cota_memoria mec, estoque_produto_memoria p
where mec.tipo_movimento_id = 26 
and mec.produto_edicao_id = p.produto_edicao_id 
and p.qtde_devolucao_encalhe > 0 
) group by 2,3,4,5);

update movimento_estoque_memoria set qtde = 0 where qtde is null;

select 'MOVIMENTO_ESTOQUE: ',sysdate(); -- Log

-- ############################
-- ######## ATUALIZACOES ######
-- ############################

-- Atualiza o estoque do distribuidor com as quantidades enviadas p/ o fornecedor
update estoque_produto_memoria set qtde_devolucao_fornecedor = coalesce(qtde_devolucao_fornecedor,0) + coalesce((select sum(me.qtde) 
from movimento_estoque_memoria me 
where me.tipo_movimento_id = 66 
and me.produto_edicao_id = estoque_produto_memoria.produto_edicao_id), 0)
where produto_edicao_id in (select produto_edicao_id from movimento_estoque_memoria 
where estoque_produto_memoria.produto_edicao_id = produto_edicao_id 
and tipo_movimento_id = 66);

select 'ATUALIZACOES: ',sysdate(); -- Log 

-- 
-- EXPEDICAO
-- 

insert into expedicao (data_expedicao,usuario) 
(select data,1 from movimento_estoque_memoria where tipo_movimento_id=13 group by 1);

update lancamento l,expedicao e
set l.expedicao_id = e.id
where l.data_lcto_distribuidor = e.data_expedicao
and l.produto_edicao_id in (select distinct me.produto_edicao_id  from movimento_estoque_memoria me where me.tipo_movimento_id=13); 

select 'EXPEDICAO: ',sysdate(); -- Log 

-- Query OK, 11074 rows affected (0.19 sec)
-- update movimento_estoque set data = date(sysdate()) where data = '0000-00-00';
update movimento_estoque_cota_memoria set data = 
(select min(l.DATA_REC_DISTRIB) from lancamento l where l.produto_edicao_id = movimento_estoque_cota_memoria.produto_edicao_id)
where data = '0000-00-00'
and TIPO_MOVIMENTO_ID = 26;

update movimento_estoque_memoria set data = 
(select min(l.DATA_REC_DISTRIB) from lancamento l where l.produto_edicao_id = movimento_estoque_memoria.produto_edicao_id)
where data = '0000-00-00'
and TIPO_MOVIMENTO_ID = 31;

select 'ATUALIZACAO 2: ',sysdate(); -- Log 
-- 
-- CONTROLE_CONFERENCIA_ENCALHE
-- 

insert into CONTROLE_CONFERENCIA_ENCALHE 
(data, status) 
(select 
data,
'CONCLUIDO'
from movimento_estoque_memoria
where tipo_movimento_id = 31
group by 1, 2);

select 'CONTROLE_CONFERENCIA_ENCALHE: ',sysdate(); -- Log 

-- ######################################################################################################
-- ######################################## CHAMADA_ENCALHE #############################################
-- ######################################################################################################

insert into CHAMADA_ENCALHE (data_recolhimento, tipo_chamada_encalhe, produto_edicao_id)
(select data, 'MATRIZ_RECOLHIMENTO', m.produto_edicao_id 
from movimento_estoque_cota_memoria m, cota c 
where  c.id = m.cota_id 
and tipo_movimento_id = 26 group by m.produto_edicao_id);

update CHAMADA_ENCALHE
set Sequencia = id;

insert into chamada_encalhe_lancamento
(select ce.id, l.id from chamada_encalhe ce, lancamento l
where ce.data_recolhimento = l.data_rec_distrib
and ce.produto_edicao_id = l.produto_edicao_id);

INSERT INTO CHAMADA_ENCALHE_COTA (fechado, postergado, qtde_prevista, chamada_encalhe_id, cota_id)
(select false, false, qtde, ce.id, m.cota_id
from movimento_estoque_cota_memoria m, chamada_encalhe ce
where tipo_movimento_id = 21
and ce.produto_edicao_id = m.produto_edicao_id);

select 'CHAMADA_ENCALHE: ',sysdate(); -- Log 

-- ######################################################################################################
-- ############################## CONTROLE_CONFERENCIA_ENCALHE_COTA #####################################
-- ######################################################################################################

INSERT INTO CONTROLE_CONFERENCIA_ENCALHE_COTA
(data_fim, data_inicio, data_operacao, box_id, ctrl_conf_encalhe_id, 
cota_id, usuario_id)
(select data_recolhimento, data_recolhimento, data_recolhimento,
99, -- alterado de 105 para 99 
cce.id, m.cota_id, 1
from 
movimento_estoque_cota_memoria m, 
chamada_encalhe ce,
controle_conferencia_encalhe cce
where 
tipo_movimento_id = 26
and ce.produto_edicao_id = m.produto_edicao_id
and cce.data = m.data
group by 1,2,3,4,5,6,7);

select 'CONTROLE_CONFERENCIA_ENCALHE_COTA: ',sysdate(); -- Log 

-- ######################################################################################################
-- ###################################### FECHAMENTO_ENCALHE ############################################
-- ######################################################################################################

INSERT INTO FECHAMENTO_ENCALHE
(DATA_ENCALHE, QUANTIDADE, PRODUTO_EDICAO_ID)
(select data, qtde,produto_edicao_id
 from movimento_estoque_memoria
 where tipo_movimento_id=31 );

select 'FECHAMENTO_ENCALHE: ',sysdate(); -- Log

-- ######################################################################################################
-- ################################ CONTROLE_FECHAMENTO_ENCALHE #########################################
-- ######################################################################################################

INSERT INTO CONTROLE_FECHAMENTO_ENCALHE (select DATA_ENCALHE,1 from fechamento_encalhe group by 1);

select 'CONTROLE_FECHAMENTO_ENCALHE: ',sysdate(); -- Log

-- ######################################################################################################

update movimento_estoque_cota_memoria 
SET preco_venda = (select pe.preco_venda from produto_edicao pe 
where pe.id = movimento_estoque_cota_memoria.produto_edicao_id 
and preco_venda is not null 
and preco_venda <>0);

update movimento_estoque_cota_memoria set preco_venda = 
(select (select pe2.preco_venda 
from produto_edicao pe2 
where pe2.preco_venda is not null 
and preco_venda != 0 
and pe2.produto_id = produto_edicao.produto_id 
order by pe2.numero_edicao desc limit 1)
from  produto_edicao
where produto_edicao.id = movimento_estoque_cota_memoria.produto_edicao_id
group by produto_edicao.produto_id)
where preco_venda is null 
or preco_venda = 0;

update movimento_estoque_cota_memoria set preco_venda = 0 where preco_venda is null;

-- movimento_estoque_cota_memoria PRECO_VENDA,VALOR_DESCONTO,PRECO_COM_DESCONTO
update movimento_estoque_cota_memoria  epc, CARGA_MDC_COTA_DIFERENCIADA car, COTA c
set epc.valor_Desconto = car.DESCONTO
where car.NUMERO_COTA = c.NUMERO_COTA
  and epc.cota_id = c.id;

update movimento_estoque_cota_memoria set valor_Desconto = 30 where valor_Desconto is null; -- 25 = Campinas,Santos / 30 = Rio
update movimento_estoque_cota_memoria set preco_com_Desconto = preco_venda - (preco_venda * valor_desconto/100);

select 'MOVIMENTO_ESTOQUE_COTA (PRECOS): ',sysdate(); -- Log

--
-- MATERIALIZA as TABELAS
--
select 'MATERIALIZACAO INICIO: ',sysdate(); -- Log

insert into estoque_produto
(ID,QTDE,QTDE_DANIFICADO,QTDE_DEVOLUCAO_ENCALHE,QTDE_DEVOLUCAO_FORNECEDOR,QTDE_SUPLEMENTAR,QTDE_PERDA,
QTDE_GANHO,VERSAO,PRODUTO_EDICAO_ID,qtde_juramentado)
(select 
ID,QTDE,QTDE_DANIFICADO,QTDE_DEVOLUCAO_ENCALHE,QTDE_DEVOLUCAO_FORNECEDOR,QTDE_SUPLEMENTAR,QTDE_PERDA,
QTDE_GANHO,VERSAO,PRODUTO_EDICAO_ID,qtde_juramentado
from estoque_produto_memoria where PRODUTO_EDICAO_ID is not null and PRODUTO_EDICAO_ID in (select id from produto_edicao));

drop table estoque_produto_memoria;

select 'MATERIALIZADO - ESTOQUE_PRODUTO: ',count(*) from estoque_produto; -- Log

insert into movimento_estoque_cota (
ID,APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,STATUS,DATA,DATA_CRIACAO,TIPO_MOVIMENTO_ID,USUARIO_ID,
QTDE,PRODUTO_EDICAO_ID,COTA_ID,ESTOQUE_PROD_COTA_ID,ORIGEM,APROVADOR_ID,LANCAMENTO_ID,status_estoque_financeiro,
PRECO_COM_DESCONTO,PRECO_VENDA,VALOR_DESCONTO)
(select 
id,true,date(sysdate()),'APROVADO',DATA,date(sysdate()),TIPO_MOVIMENTO_ID,1,
QTDE,PRODUTO_EDICAO_ID,COTA_ID,ESTOQUE_PROD_COTA_ID,'CARGA_INICIAL',1,LANCAMENTO_ID,
IF(STATUS_ESTOQUE_FINANCEIRO=1,'FINANCEIRO_PROCESSADO','FINANCEIRO_NAO_PROCESSADO'),
PRECO_COM_DESCONTO,PRECO_VENDA,VALOR_DESCONTO
from movimento_estoque_cota_memoria);

drop table movimento_estoque_cota_memoria;

select 'MATERIALIZADO - MOVIMENTO_ESTOQUE_COTA: ',count(*) from movimento_estoque_cota; -- Log

insert into movimento_estoque
(ID,APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,
DATA_CRIACAO,TIPO_MOVIMENTO_ID,USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,ORIGEM)
(select 
ID,true,date(sysdate()),'CARGA','APROVADO',DATA,date(sysdate()),TIPO_MOVIMENTO_ID,1,QTDE,
PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,'CARGA_INICIAL'
from movimento_estoque_memoria);

drop table movimento_estoque_memoria;

select 'MATERIALIZADO - MOVIMENTO_ESTOQUE: ',count(*) from movimento_estoque; -- Log

ALTER TABLE HVND ENGINE=InnoDB;

select 'MATERIALIZADO - HVND: ',count(*) from hvnd; -- Log

ALTER TABLE ESTQBOX ENGINE=InnoDB;

select 'MATERIALIZADO - ESTQBOX: ',count(*) from ESTQBOX; -- Log

ALTER TABLE MOV_CRED ENGINE=InnoDB;

select 'MATERIALIZADO - MOV_CRED: ',count(*) from MOV_CRED; -- Log

ALTER TABLE MOV_DEB ENGINE=InnoDB;

select 'MATERIALIZADO - MOV_DEB: ',count(*) from MOV_DEB; -- Log

ALTER TABLE CARGA_LANCAMENTO_MDC ENGINE=InnoDB;

select 'MATERIALIZADO - CARGA_LANCAMENTO_MDC: ',count(*) from CARGA_LANCAMENTO_MDC; -- Log



select 'MATERIALIZACAO FIM: ',sysdate(); -- Log

-- 
-- CONFERENCIA_ENCALHE
--

insert into CONFERENCIA_ENCALHE
(data, preco_capa_informado, qtde, qtde_informada, 
chamada_encalhe_cota_id, controle_conferencia_encalhe_cota_id,
movimento_estoque_id, movimento_estoque_cota_id, produto_edicao_id,juramentada)
(select data_recolhimento, pe.preco_previsto, mec.qtde, mec.qtde,
cec.id, ccec.id, me.id, mec.id, mec.produto_edicao_id,0
from 
movimento_estoque_cota mec, 
movimento_estoque me,
chamada_encalhe ce,
chamada_encalhe_cota cec,
controle_conferencia_encalhe cce,
controle_conferencia_encalhe_cota ccec,
produto_edicao pe
where 
mec.tipo_movimento_id = 26
and mec.produto_edicao_id = pe.id
and me.produto_edicao_id = mec.produto_edicao_id
and me.tipo_movimento_id = 31    
and me.produto_edicao_id = pe.id
and ce.produto_edicao_id = mec.produto_edicao_id
and ce.produto_edicao_id = me.produto_edicao_id
and cec.chamada_encalhe_id = ce.id
and cec.cota_id = mec.cota_id
and cce.data = me.data
and ccec.ctrl_conf_encalhe_id = cce.id
and ccec.cota_id = mec.cota_id);

select 'CONFERENCIA_ENCALHE: ',sysdate(); -- Log

-- 
-- FECHAMENTO_ENCALHE_BOX
--

insert into fechamento_encalhe_box (
select qtde,data,produto_edicao_id,99
from movimento_estoque
where tipo_movimento_id = 31);

select 'FECHAMENTO_ENCALHE_BOX: ',sysdate(); -- Log
-- ##################################################################################################################
-- ################################################  LANCAMENTO #####################################################
-- ##################################################################################################################

-- FIXME Remover quando possivel
update lancamento set data_lcto_distribuidor = '0001-01-01' where data_lcto_distribuidor = '0000-00-00';

CREATE TABLE HVND_AUX (produto_edicao_id INT(6));

-- A = ABERTO
insert into HVND_AUX
select distinct produto_edicao_id from HVND where data_recolhimento <> '0000-00-00' and status = 'A';

update lancamento
set status = 'EM_BALANCEAMENTO_RECOLHIMENTO'
where PRODUTO_EDICAO_ID in (select produto_edicao_id from HVND_AUX);

truncate table HVND_AUX;

-- 0000-00-00 = EXPEDIDO
insert into HVND_AUX
select distinct produto_edicao_id from HVND where data_recolhimento = '0000-00-00' and status = 'A';

update lancamento
set status = 'EXPEDIDO'
where PRODUTO_EDICAO_ID in (select produto_edicao_id from HVND_AUX);

truncate table HVND_AUX;

-- F = FECHADO
insert into HVND_AUX
select distinct produto_edicao_id from HVND where status = 'F';

update lancamento
set status = 'FECHADO'
where PRODUTO_EDICAO_ID in (select produto_edicao_id from HVND_AUX);

update lancamento l
set status = 'EM_RECOLHIMENTO'
where l.data_rec_distrib between date_add(l.data_rec_distrib, interval -5 day) and date(now()) and status = 'FECHADO';

DROP TABLE HVND_AUX;

update lancamento
set DATA_REC_PREVISTA = (select adddate(DATA_LCTO_DISTRIBUIDOR, peb)
from produto_edicao
where produto_edicao.id = lancamento.produto_edicao_id
limit 1) WHERE   DATA_REC_DISTRIB = '0001-01-01'
or    DATA_REC_PREVISTA = '0001-01-01'
and status = 'CONFIRMADO';

update lancamento
set DATA_REC_DISTRIB = DATA_REC_PREVISTA
where DATA_REC_DISTRIB = '0001-01-01';

select 'ATUALIZACOES LANCAMENTOS: ',sysdate(); -- Log

-- ##################################################################################################################
-- ############################################### SEQ_GENERATOR  ###################################################
-- ##################################################################################################################

update SEQ_GENERATOR set sequence_next_hi_value = (
select max(id) from (
 select max(id) id from movimento_estoque
 union
 select max(id) id from movimento_estoque_cota
 union
 select max(id) id from movimento_financeiro_cota
) rs1) WHERE sequence_name = 'Movimento';

select 'SEQ_GENERATOR: ',sysdate(); -- Log
select 'FIM: ',sysdate(); -- Log


