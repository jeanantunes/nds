drop table if exists HVND_MDC;


create table HVND_MDC (
COD_COTA_HVCT INT,
cod_produto VARCHAR(10),
num_edicao INT,
nome_produto VARCHAR(255),
PEB int,
DATA_LANCAMENTO_STR VARCHAR(10),
PRECO_STR varchar(255),
segmento VARCHAR(255),
QTDE_REPARTE_HVCT INT,
QTDE_VENDA_HVCT INT,
DATA_RECOLHIMENTO_STR VARCHAR(10),
QTDE_ENCALHE_HVCT INT,
PRECO FLOAT,
STATUS CHAR(1),
CONSIGNADO boolean,
DEVOLVE_ENCALHE boolean,
COD_COTA_DP_HVCT INT,
COD_COTA_FC_HVCT INT,
NUMERO_PERIODO INT,
PARCIAL_NORMAL CHAR,
produto_edicao_id bigint,
DATA_LANCAMENTO DATE,
DATA_RECOLHIMENTO DATE,
cota_id bigint
) -- ENGINE=MEMORY
;


LOAD DATA INFILE '/opt/rollout/load_files/092014.csv' INTO TABLE HVND_MDC COLUMNS TERMINATED BY ';' LINES TERMINATED BY '\r\n' IGNORE 1 LINES;


-- ENCALHE
update HVND_MDC
set QTDE_ENCALHE_HVCT = (QTDE_REPARTE_HVCT - QTDE_VENDA_HVCT);

-- PRECO
update HVND_MDC
set PRECO_STR = REPLACE(PRECO_STR ,',','.');

update HVND_MDC
set PRECO = CAST(PRECO_STR as DECIMAL (6,2));



update HVND_MDC set parcial_normal = 'N';

update HVND_MDC 
set cod_produto = lpad(cod_produto,8,'0');

update HVND_MDC set 
DATA_LANCAMENTO = STR_TO_DATE(DATA_LANCAMENTO_STR, '%d/%m/%Y'),
DATA_RECOLHIMENTO = STR_TO_DATE(DATA_RECOLHIMENTO_STR, '%d/%m/%Y');

ALTER TABLE HVND_MDC  
DROP DATA_LANCAMENTO_STR,
DROP DATA_RECOLHIMENTO_STR;


update HVND_MDC h,produto_edicao pe, produto p 
set h.produto_edicao_id = pe.id
where p.id = pe.produto_id 
and p.codigo = h.cod_produto 
and pe.numero_edicao = h.num_edicao; 

delete from HVND_MDC 
where produto_edicao_id is null;

update HVND_MDC set cota_id = (
select c.id from cota c where c.numero_cota = HVND_MDC.COD_COTA_HVCT);

-- Excluir os que ja tem 
delete from HVND_MDC where produto_edicao_id in (select produto_edicao_id from estoque_produto);
-- delete from HVND_MDC where produto_edicao_id in (select produto_edicao_id from estoque_produto_cota);
-- delete from HVND_MDC where produto_edicao_id in (select produto_edicao_id from movimento_estoque);
-- delete from HVND_MDC where produto_edicao_id in (select produto_edicao_id from movimento_estoque_cota);
-- delete from HVND_MDC where produto_edicao_id in (select produto_edicao_id from lancamento where status <> 'FECHADO');


DROP TABLE IF EXISTS estoque_produto_cota_memoria;


CREATE TABLE `estoque_produto_cota_memoria` (
  `ID_AUX` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` bigint(20) DEFAULT NULL,
  `QTDE_DEVOLVIDA` decimal(18,4) DEFAULT NULL,
  `QTDE_RECEBIDA` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID_AUX`),
  UNIQUE KEY `COTA_ID` (`COTA_ID`,`PRODUTO_EDICAO_ID`)
); -- ENGINE=MEMORY;


INSERT INTO estoque_produto_cota_memoria (QTDE_DEVOLVIDA, QTDE_RECEBIDA, COTA_ID, PRODUTO_EDICAO_ID)
(select 
sum(csv.QTDE_ENCALHE_HVCT) QTDE_DEVOLVIDA,
sum(csv.QTDE_REPARTE_HVCT) QTDE_RECEBIDA,
c.id COTA_ID,
csv.produto_edicao_id produto_edicao_id
from HVND_MDC csv,cota c 
where c.numero_cota = csv.COD_COTA_HVCT
and csv.produto_edicao_id is not null
group by c.id,csv.produto_edicao_id);


update estoque_produto_cota_memoria 
set ID = ID_AUX + (select max(id) from estoque_produto_cota);


DROP TABLE IF EXISTS estoque_produto_memoria;

CREATE TABLE `estoque_produto_memoria` (
  `ID_AUX` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` bigint(20) DEFAULT NULL,
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
  PRIMARY KEY (`ID_AUX`),
  UNIQUE KEY `PRODUTO_EDICAO_ID` (`PRODUTO_EDICAO_ID`)) 
DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS movimento_estoque_cota_memoria;


CREATE TABLE `movimento_estoque_cota_memoria` (
  `id_aux` bigint(20) NOT NULL AUTO_INCREMENT,
  `id` bigint(20) DEFAULT NULL,
  `DATA` date NOT NULL,
  `TIPO_MOVIMENTO_ID` smallint NOT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL, -- *
  `STATUS_ESTOQUE_FINANCEIRO` tinyint(1) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL, -- *
  `COTA_ID` int NOT NULL, -- *
  `ESTOQUE_PROD_COTA_ID` bigint(20) DEFAULT NULL, -- *
  `LANCAMENTO_ID` bigint(20) DEFAULT NULL, -- *
  `PRECO_COM_DESCONTO` decimal(18,4) DEFAULT NULL,
  `PRECO_VENDA` decimal(18,4) DEFAULT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`id_aux`))
;


INSERT INTO MOVIMENTO_ESTOQUE_COTA_MEMORIA
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,COTA_ID,ESTOQUE_PROD_COTA_ID,status_estoque_financeiro)
(select 
h.data_recolhimento,
26,
h.QTDE_ENCALHE_HVCT, 
epc.produto_edicao_id,
epc.cota_id,
epc.id,
1
from estoque_produto_cota_memoria epc, HVND_MDC h
where epc.cota_id = h.cota_id
and epc.PRODUTO_EDICAO_ID = h.produto_edicao_id 
and h.status = 'F'
group by 1,2,3,5,6,7);


INSERT INTO MOVIMENTO_ESTOQUE_COTA_MEMORIA
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,COTA_ID,ESTOQUE_PROD_COTA_ID,LANCAMENTO_ID,status_estoque_financeiro)
(select 
h.data_lancamento,
21,
h.QTDE_REPARTE_HVCT,
h.produto_edicao_id,
epc.cota_id,
epc.id,
l.ID,
case when h.status ='A' then 0 else 1 end
from estoque_produto_cota_memoria epc,HVND_MDC h,lancamento l
where epc.produto_edicao_id = h.produto_edicao_id
and epc.cota_id = h.cota_id
and l.produto_edicao_id = h.produto_edicao_id 
-- and l.DATA_LCTO_DISTRIBUIDOR = h.DATA_LANCAMENTO 
and epc.PRODUTO_EDICAO_ID = l.produto_edicao_id 
and epc.qtde_recebida > 0
-- and h.parcial_normal = 'N'
group by 1,2,3,4,5,6,7,8);


update MOVIMENTO_ESTOQUE_COTA_MEMORIA 
set ID = ID_AUX + (select max(id) from MOVIMENTO_ESTOQUE_COTA);


insert into estoque_produto_memoria (produto_edicao_id)
(select distinct produto_edicao_id 
from movimento_estoque_cota_memoria 
where produto_edicao_id not in (select distinct produto_edicao_id from estoque_produto) 
group by 1);


update estoque_produto_memoria 
set ID = ID_AUX + (select max(id) from estoque_produto);

DROP TABLE IF EXISTS movimento_estoque_memoria;


CREATE TABLE `movimento_estoque_memoria` (
  `id_aux` bigint(20) NOT NULL AUTO_INCREMENT,
  `id` bigint(20),
  `DATA` date NOT NULL,
  `TIPO_MOVIMENTO_ID` bigint(20) NOT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `ESTOQUE_PRODUTO_ID` bigint(20) DEFAULT 0,
  PRIMARY KEY (`id_aux`))  
;

-- Insere movimento de estoque de recebimento de encalhe do distribuidor baseado no movimento de estoque de envio de encalhe da cota 
INSERT INTO movimento_estoque_memoria
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
mec.data, -- min(mec.data), -- min(mec.data_recolhimento),
31,
sum(mec.QTDE),
mec.PRODUTO_EDICAO_ID,
ep.ID
from movimento_estoque_cota_memoria mec,estoque_produto_memoria ep
where ep.produto_edicao_id = mec.produto_edicao_id
and mec.tipo_movimento_id = 26
group by 1,2,4,5
having sum(mec.QTDE) > 0);


INSERT INTO movimento_estoque_memoria
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
mec.data, -- min(mec.data), -- min(mec.data_lancamento),
13,
sum(mec.QTDE),
mec.PRODUTO_EDICAO_ID,
ep.ID
from movimento_estoque_cota_memoria mec,estoque_produto_memoria ep
where ep.produto_edicao_id = mec.produto_edicao_id
and mec.tipo_movimento_id = 21
group by 1,2,4,5
having sum(mec.QTDE) > 0);


INSERT INTO movimento_estoque_memoria
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
mec.data, -- min(mec.data), -- min(mec.data_lancamento),
20,
sum(mec.qtde),
mec.PRODUTO_EDICAO_ID,
null
from movimento_estoque_cota_memoria mec
where mec.tipo_movimento_id = 21
and mec.QTDE > 0
group by 1,2,4,5);


update movimento_estoque_memoria 
set ESTOQUE_PRODUTO_ID=(select ep.id from estoque_produto_memoria ep 
where ep.produto_edicao_id=movimento_estoque_memoria.produto_edicao_id) 
where tipo_movimento_id=20;


update movimento_estoque_memoria 
set QTDE=COALESCE(QTDE,0)+(select coalesce(ep.qtde,0) -- + coalesce(ep.qtde_suplementar,0) 
from estoque_produto_memoria ep where ep.produto_edicao_id=movimento_estoque_memoria.produto_edicao_id) 
where tipo_movimento_id=20;


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
and me.produto_edicao_id not in (select distinct(mec.produto_edicao_id) from movimento_estoque_cota_memoria mec, estoque_produto_memoria p
where mec.tipo_movimento_id = 26 
and mec.produto_edicao_id = p.produto_edicao_id 
and p.qtde_devolucao_encalhe > 0 
) group by 1,2,3,4,5); -- agrupar tambem pela data


/*
INSERT INTO movimento_estoque_memoria 
(DATA,TIPO_MOVIMENTO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID)
(select 
e.data,
66,
sum(e.quantidade),
e.produto_edicao_id,
ep.id
from estqmov e,estoque_produto_memoria ep
where ep.produto_edicao_id = e.produto_edicao_id
and e.tipo_movto = 12
and e.produto_edicao_id not in(select produto_edicao_id from movimento_estoque_memoria where tipo_movimento_id = 66) 
group by 1,2,4,5);
*/

update movimento_estoque_memoria 
set ID = ID_AUX + (select max(id) from MOVIMENTO_ESTOQUE);


update movimento_estoque_memoria set qtde = 0 where qtde is null;


update estoque_produto_memoria set qtde_devolucao_fornecedor = coalesce(qtde_devolucao_fornecedor,0) + coalesce((select sum(me.qtde) 
from movimento_estoque_memoria me 
where me.tipo_movimento_id = 66 
and me.produto_edicao_id = estoque_produto_memoria.produto_edicao_id), 0)
where produto_edicao_id in (select produto_edicao_id from movimento_estoque_memoria 
where estoque_produto_memoria.produto_edicao_id = produto_edicao_id 
and tipo_movimento_id = 66);


insert into expedicao (data_expedicao,usuario) 
(select data,1 from movimento_estoque_memoria where tipo_movimento_id=13 group by 1);


update lancamento l,expedicao e
set l.expedicao_id = e.id
where l.data_lcto_distribuidor = e.data_expedicao
and l.produto_edicao_id in (select distinct me.produto_edicao_id  from movimento_estoque_memoria me where me.tipo_movimento_id=13)
and l.expedicao_id is null; 


update movimento_estoque_cota_memoria set data = 
(select min(l.DATA_REC_DISTRIB) from lancamento l where l.produto_edicao_id = movimento_estoque_cota_memoria.produto_edicao_id)
where data = '0000-00-00'
and TIPO_MOVIMENTO_ID = 26;


update movimento_estoque_memoria set data = 
(select min(l.DATA_REC_DISTRIB) from lancamento l where l.produto_edicao_id = movimento_estoque_memoria.produto_edicao_id)
where data = '0000-00-00'
and TIPO_MOVIMENTO_ID = 31;


update movimento_estoque_memoria set data = 
(select min(l.DATA_LCTO_DISTRIBUIDOR) from lancamento l where l.produto_edicao_id = movimento_estoque_memoria.produto_edicao_id)
where data = '0000-00-00'
and TIPO_MOVIMENTO_ID in (13,20);


insert into CONTROLE_CONFERENCIA_ENCALHE 
(data, status) 
(select 
data,
'CONCLUIDO'
from movimento_estoque_memoria
where tipo_movimento_id = 31
and data not in (select distinct data from CONTROLE_CONFERENCIA_ENCALHE)
group by 1, 2);


insert into CHAMADA_ENCALHE (data_recolhimento, tipo_chamada_encalhe, produto_edicao_id)
(select data, 'MATRIZ_RECOLHIMENTO', m.produto_edicao_id 
from movimento_estoque_cota_memoria m, cota c 
where  c.id = m.cota_id 
and tipo_movimento_id = 26 
group by m.produto_edicao_id);


update CHAMADA_ENCALHE
set Sequencia = id;


insert into chamada_encalhe_lancamento
(select ce.id, l.id from chamada_encalhe ce, lancamento l
where 1 = 1
and ce.produto_edicao_id = l.produto_edicao_id
and l.id not in (select lancamento_id from chamada_encalhe_lancamento));

INSERT INTO CHAMADA_ENCALHE_COTA (fechado, postergado, qtde_prevista, chamada_encalhe_id, cota_id)
(select false, false, qtde, ce.id, m.cota_id
from movimento_estoque_cota_memoria m, chamada_encalhe ce
where tipo_movimento_id = 21
and ce.produto_edicao_id = m.produto_edicao_id);


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


INSERT INTO FECHAMENTO_ENCALHE
(DATA_ENCALHE, QUANTIDADE, PRODUTO_EDICAO_ID)
(select data, qtde,produto_edicao_id
 from movimento_estoque_memoria
 where tipo_movimento_id=31 );


INSERT INTO CONTROLE_FECHAMENTO_ENCALHE (
select DATA_ENCALHE,1 from fechamento_encalhe where data_encalhe not in (select DATA_ENCALHE from CONTROLE_FECHAMENTO_ENCALHE) group by 1);


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


update movimento_estoque_cota_memoria  epc, CARGA_MDC_COTA_DIFERENCIADA car, COTA c
set epc.valor_Desconto = car.DESCONTO
where car.NUMERO_COTA = c.NUMERO_COTA
  and epc.cota_id = c.id;


update movimento_estoque_cota_memoria set valor_Desconto = 25 where valor_Desconto is null; -- 25 = Campinas,Santos / 30 = Rio


update movimento_estoque_cota_memoria set preco_com_Desconto = preco_venda - (preco_venda * valor_desconto/100);


insert into estoque_produto
(ID,QTDE,QTDE_DANIFICADO,QTDE_DEVOLUCAO_ENCALHE,QTDE_DEVOLUCAO_FORNECEDOR,QTDE_SUPLEMENTAR,QTDE_PERDA,
QTDE_GANHO,VERSAO,PRODUTO_EDICAO_ID,qtde_juramentado)
(select ID, 
QTDE,QTDE_DANIFICADO,QTDE_DEVOLUCAO_ENCALHE,QTDE_DEVOLUCAO_FORNECEDOR,QTDE_SUPLEMENTAR,QTDE_PERDA,
QTDE_GANHO,VERSAO,PRODUTO_EDICAO_ID,qtde_juramentado
from estoque_produto_memoria where PRODUTO_EDICAO_ID is not null 
and PRODUTO_EDICAO_ID not in (select produto_edicao_id from estoque_produto));

INSERT INTO estoque_produto_cota (ID,QTDE_DEVOLVIDA, QTDE_RECEBIDA, COTA_ID, PRODUTO_EDICAO_ID)
( select
ID,QTDE_DEVOLVIDA,QTDE_RECEBIDA,COTA_ID,PRODUTO_EDICAO_ID
from estoque_produto_cota_memoria);


insert into movimento_estoque_cota (
ID,APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,STATUS,DATA,DATA_CRIACAO,TIPO_MOVIMENTO_ID,USUARIO_ID,
QTDE,PRODUTO_EDICAO_ID,COTA_ID,ESTOQUE_PROD_COTA_ID,ORIGEM,APROVADOR_ID,LANCAMENTO_ID,status_estoque_financeiro,
PRECO_COM_DESCONTO,PRECO_VENDA,VALOR_DESCONTO,FORMA_COMERCIALIZACAO)
(select 
ID,true,date(sysdate()),'APROVADO',DATA,date(sysdate()),TIPO_MOVIMENTO_ID,1,
QTDE,PRODUTO_EDICAO_ID,COTA_ID,
ESTOQUE_PROD_COTA_ID,
'CARGA_INICIAL',1,LANCAMENTO_ID,
IF(STATUS_ESTOQUE_FINANCEIRO=1,'FINANCEIRO_PROCESSADO','FINANCEIRO_NAO_PROCESSADO'),
PRECO_COM_DESCONTO,PRECO_VENDA,VALOR_DESCONTO,'CONSIGNADO'
from movimento_estoque_cota_memoria );


insert into movimento_estoque
(ID,APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,
DATA_CRIACAO,TIPO_MOVIMENTO_ID,USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,ORIGEM)
(select 
ID,true,date(sysdate()),'CARGA','APROVADO',DATA,date(sysdate()),TIPO_MOVIMENTO_ID,1,QTDE,
PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,'CARGA_INICIAL'
from movimento_estoque_memoria 
where ESTOQUE_PRODUTO_ID in (select id from estoque_produto)
);



update SEQ_GENERATOR set sequence_next_hi_value = (
select max(id) from (
 select max(id) id from movimento_estoque
 union
 select max(id) id from movimento_estoque_cota
 union
 select max(id) id from movimento_financeiro_cota
) rs1) WHERE sequence_name = 'Movimento';



-- ###########################################################

-- MOVIMENTO_ESTOQUE_COTA

drop table if exists movimento_estoque_cota_memoria_aux;

CREATE TABLE movimento_estoque_cota_memoria_aux (
id bigint(20) NOT NULL,
PRODUTO_EDICAO_ID bigint(20) NOT NULL,
TIPO_MOVIMENTO_ID smallint NOT NULL,
QTDE decimal(18,4) NOT NULL,
COTA_ID int NOT NULL
) -- ENGINE=MEMORY
;


ALTER TABLE `movimento_estoque_cota_memoria_aux` ADD INDEX `produto_edicao_id` (`produto_edicao_id` ASC) ;

insert into movimento_estoque_cota_memoria_aux (id,produto_edicao_id,tipo_movimento_id,qtde,cota_id)
(select id,produto_edicao_id,tipo_movimento_id,qtde,cota_id
from movimento_estoque_cota_memoria where tipo_movimento_id = 26);

-- MOVIMENTO_ESTOQUE

drop table if exists movimento_estoque_memoria_aux;

CREATE TABLE movimento_estoque_memoria_aux (
id bigint(20) NOT NULL,
PRODUTO_EDICAO_ID bigint(20) NOT NULL,
TIPO_MOVIMENTO_ID smallint NOT NULL,
DATA date NOT NULL
) -- ENGINE=MEMORY
;

ALTER TABLE `movimento_estoque_memoria_aux` ADD INDEX `produto_edicao_id` (`produto_edicao_id` ASC) ;

insert into movimento_estoque_memoria_aux (id,produto_edicao_id,tipo_movimento_id,data)
(select id,produto_edicao_id,tipo_movimento_id,data
from movimento_estoque_memoria where tipo_movimento_id = 31);


-- CHAMADA_ENCALHE

drop table if exists chamada_encalhe_aux;

CREATE TABLE chamada_encalhe_aux (
id bigint(20) NOT NULL,
PRODUTO_EDICAO_ID bigint(20) NOT NULL,
DATA_RECOLHIMENTO date NOT NULL
) -- ENGINE=MEMORY
;

ALTER TABLE `chamada_encalhe_aux` ADD INDEX `produto_edicao_id` (`produto_edicao_id` ASC) ;


insert into chamada_encalhe_aux (id,produto_edicao_id,data_recolhimento)
(select id,produto_edicao_id,data_recolhimento
from chamada_encalhe);


-- CHAMADA_ENCALHE_COTA

drop table if exists chamada_encalhe_cota_aux;

CREATE TABLE chamada_encalhe_cota_aux (
id bigint(20) NOT NULL,
chamada_encalhe_id bigint(20) NOT NULL,
COTA_ID int NOT NULL
) -- ENGINE=MEMORY
;

ALTER TABLE `chamada_encalhe_cota_aux` ADD INDEX `chamada_encalhe_id` (`chamada_encalhe_id` ASC) ;

insert into chamada_encalhe_cota_aux (id,chamada_encalhe_id,cota_id)
(select id,chamada_encalhe_id,cota_id
from chamada_encalhe_cota);


-- CONTROLE_CONFERENCIA_ENCALHE

drop table if exists controle_conferencia_encalhe_aux;

CREATE TABLE controle_conferencia_encalhe_aux (
id bigint(20) NOT NULL,
DATA date NOT NULL
) -- ENGINE=MEMORY
;

ALTER TABLE `controle_conferencia_encalhe_aux` ADD INDEX `id` (`id` ASC) ;

insert into controle_conferencia_encalhe_aux (id,data)
(select id,data
from controle_conferencia_encalhe);


-- CONTROLE_CONFERENCIA_ENCALHE_COTA

drop table if exists controle_conferencia_encalhe_cota_aux;

CREATE TABLE controle_conferencia_encalhe_cota_aux (
id bigint(20) NOT NULL,
ctrl_conf_encalhe_id bigint(20) NOT NULL,
data_operacao date NOT NULL,
COTA_ID int NOT NULL
) -- ENGINE=MEMORY
;

ALTER TABLE `controle_conferencia_encalhe_cota_aux` ADD INDEX `ctrl_conf_encalhe_id` (`ctrl_conf_encalhe_id` ASC) ;

insert into controle_conferencia_encalhe_cota_aux (id,ctrl_conf_encalhe_id,data_operacao,cota_id)
(select id,ctrl_conf_encalhe_id,data_operacao,cota_id
from controle_conferencia_encalhe_cota);


insert into CONFERENCIA_ENCALHE
(data, preco_capa_informado, qtde, qtde_informada, 
chamada_encalhe_cota_id, controle_conferencia_encalhe_cota_id,
movimento_estoque_id, movimento_estoque_cota_id, produto_edicao_id,juramentada)
(select distinct data_recolhimento, pe.preco_previsto, mec.qtde, mec.qtde,
cec.id, max(ccec.id), me.id, mec.id, mec.produto_edicao_id,0
from 
movimento_estoque_cota_memoria_aux mec, 
movimento_estoque_memoria_aux me,
chamada_encalhe_aux ce,
chamada_encalhe_cota_aux cec,
controle_conferencia_encalhe_aux cce,
controle_conferencia_encalhe_cota_aux ccec,
produto_edicao pe
where 1=1
-- mec.tipo_movimento_id = 26
and mec.produto_edicao_id = pe.id
and me.produto_edicao_id = mec.produto_edicao_id
-- and me.tipo_movimento_id = 31    
and me.produto_edicao_id = pe.id
and ce.produto_edicao_id = mec.produto_edicao_id
and ce.produto_edicao_id = me.produto_edicao_id
and cec.chamada_encalhe_id = ce.id
and cec.cota_id = mec.cota_id
and cce.data = me.data
and ccec.ctrl_conf_encalhe_id = cce.id
and ccec.data_operacao = cce.data
and ccec.cota_id = mec.cota_id
and mec.id not in(select movimento_estoque_cota_id from CONFERENCIA_ENCALHE)
group by 1,2,3,4,5,7,8,9);

select 'FIM';
-- #####################################################################
-- ##################### Verificação ###################################
-- #####################################################################





