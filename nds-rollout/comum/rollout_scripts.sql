--
-- mysql -hrds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com -uawsuser -pdgbdistb01mgr
-- use db_09795816

--
--  `AJUSTA VARIAVEIS DO SIATEMA`
--

SET max_heap_table_size = 1024 * 1024 * 64;
SET tmp_table_size = 1024 * 1024 * 64;

update distribuidor set data_operacao = date(sysdate());
insert into SEQ_GENERATOR (sequence_next_hi_value,sequence_name) values (1,'Movimento');

--
--  
--

ALTER TABLE `carga_lancamento_mdc` ADD INDEX `produto_edicao_id` (`produto_edicao_id` ASC);

update CARGA_LANCAMENTO_MDC set produto_edicao_id = 
 (select pe.id 
  from produto_edicao pe, produto p 
  where p.id = pe.produto_id 
  and p.codigo = lpad(CARGA_LANCAMENTO_MDC.cod_prodin,8,'0') 
  and pe.numero_edicao = CARGA_LANCAMENTO_MDC.num_edicao);


update carga_lancamento_mdc cm,produto p, produto_edicao pe set cm.produto_edicao_id = pe.id
where p.id = pe.produto_id
and CAST(cm.cod_prodin AS UNSIGNED) = CAST(p.codigo AS UNSIGNED)
and CAST(cm.num_edicao AS UNSIGNED) = CAST(pe.numero_edicao AS UNSIGNED);

--
--
--

update estqbox set produto_edicao_id = (select pe.id from produto_edicao pe, produto p 
where p.id = pe.produto_id 
and p.codigo = produto 
and pe.numero_edicao = edicao);

delete from estqbox where produto_edicao_id is null;

--
--
--

-- **** Caso o arquivo do cesar nao tiver arrumado, trocar o comentario da linha abaixo
update lancamento
set DATA_LCTO_DISTRIBUIDOR = (select  STR_TO_DATE(data_real_lancamento_lanp,'%d/%m/%Y') -- = (select  STR_TO_DATE(data_prevista_lancamento_lanp,'%d/%m/%Y')
         from CARGA_LANCAMENTO_MDC
         where CARGA_LANCAMENTO_MDC.produto_edicao_id = lancamento.produto_edicao_id
       and status in ('CONFIRMADO', 'FECHADO', 'PLANEJADO')
       order by STR_TO_DATE(data_real_lancamento_lanp,'%d/%m/%Y') asc
       limit 1), 
 DATA_LCTO_PREVISTA = (select STR_TO_DATE(data_prevista_lancamento_lanp,'%d/%m/%Y') 
      from CARGA_LANCAMENTO_MDC
      where CARGA_LANCAMENTO_MDC.produto_edicao_id = lancamento.produto_edicao_id
      and status in ('CONFIRMADO', 'FECHADO', 'PLANEJADO')
      order by STR_TO_DATE(data_real_lancamento_lanp,'%d/%m/%Y') asc
      limit 1),
 DATA_REC_DISTRIB = (select STR_TO_DATE(data_real_recolto_rcpr,'%d/%m/%Y') 
      from CARGA_LANCAMENTO_MDC
      where CARGA_LANCAMENTO_MDC.produto_edicao_id = lancamento.produto_edicao_id
      and status in ('CONFIRMADO', 'FECHADO', 'PLANEJADO')
      order by STR_TO_DATE(data_real_lancamento_lanp,'%d/%m/%Y') asc
      limit 1),
 DATA_REC_PREVISTA = (select STR_TO_DATE(data_prevista_recolto_rcpr,'%d/%m/%Y') 
      from CARGA_LANCAMENTO_MDC
      where CARGA_LANCAMENTO_MDC.produto_edicao_id = lancamento.produto_edicao_id
      and status in ('CONFIRMADO', 'FECHADO', 'PLANEJADO')
      order by STR_TO_DATE(data_real_lancamento_lanp,'%d/%m/%Y') asc
      limit 1)
where PRODUTO_EDICAO_ID in (select CARGA_LANCAMENTO_MDC.produto_edicao_id from CARGA_LANCAMENTO_MDC
       where CARGA_LANCAMENTO_MDC.produto_edicao_id = lancamento.produto_edicao_id)
and status in ('CONFIRMADO', 'FECHADO', 'PLANEJADO');

--
--
--

update HVND set produto_edicao_id = (
select pe.id from produto_edicao pe, produto p where p.id = pe.produto_id and p.codigo = HVND.cod_produto and pe.numero_edicao = HVND.num_edicao);


update HVND set DATA_RECOLHIMENTO = (select min(l.DATA_REC_DISTRIB) from lancamento l 
where l.produto_edicao_id = HVND.produto_edicao_id)
where DATA_RECOLHIMENTO = '0000-00-00' 
and STATUS = 'F';

--
-- Edicoes 0
--
DROP TABLE IF EXISTS `hvnd_produto_edicao_zero`;

CREATE TABLE `hvnd_produto_edicao_zero` (
  `COD_COTA_HVCT` int(11) DEFAULT NULL,
  `COD_PRODUTO_HVCT` varchar(12) DEFAULT NULL,
  `PRECO` float DEFAULT NULL,
  `QTDE_REPARTE_HVCT` int(11) DEFAULT NULL,
  `QTDE_ENCALHE_HVCT` int(11) DEFAULT NULL,
  `DATA_LANCAMENTO_STR` varchar(10) DEFAULT NULL,
  `DATA_RECOLHIMENTO_STR` varchar(10) DEFAULT NULL,
  `STATUS` char(1) DEFAULT NULL,
  `DATA_LANCAMENTO` date DEFAULT NULL,
  `DATA_RECOLHIMENTO` date DEFAULT NULL,
  `cod_produto` varchar(8) DEFAULT NULL,
  `num_edicao` int(11) DEFAULT NULL,
  `produto_edicao_id` bigint(20) DEFAULT NULL,
  `cota_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


insert into hvnd_produto_edicao_zero 
select * from hvnd where produto_edicao_id is null;

--
--
--

delete from hvnd where produto_edicao_id is null;

update HVND set cota_id = (select c.id from cota c where c.numero_cota = HVND.COD_COTA_HVCT);

ALTER TABLE `HVND` ADD INDEX `hvnd_cota_id` (`COD_COTA_HVCT` ASC, `cod_produto` ASC, `num_edicao` ASC, `produto_edicao_id` ASC) ;

ALTER TABLE `HVND` ADD INDEX `hvnd_cota_status` (`STATUS` ASC) ;

ALTER TABLE `HVND` ADD INDEX `cota_id` (`cota_id` ASC);


-- 
-- 04
--

-- Insere estoque do distribuidor baseado no arquivo ESTQBOX.NEW
insert into estoque_produto (QTDE, QTDE_DEVOLUCAO_ENCALHE, QTDE_SUPLEMENTAR, PRODUTO_EDICAO_ID)
select 
sum(case when box=150 then quantidade else 0 end) as QTDE, 
sum(case when box=181 then quantidade else 0 end) as QTDE_DEVOLUCAO_ENCALHE,
sum(case when box in(140,142) then quantidade else 0 end) as QTDE_SUPLEMENTAR, 
produto_edicao_id 
from estqbox 
where box not in (190,191)
group by 4;


-- 
-- FINANCEIRO 2
--



-- alter table movimento_financeiro_cota modify id bigint(20) AUTO_INCREMENT;

update mov_deb md,cota c set md.cota_id = c.id
where md.numero_cota=c.numero_cota;

INSERT INTO movimento_financeiro_cota
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,TIPO_MOVIMENTO_ID,USUARIO_ID,LANCAMENTO_MANUAL,
COTA_ID,OBSERVACAO, valor,fornecedor_id)
(select true,data_real,'CARGA','APROVADO',data_real,data_real,null,23,1,true,cota_id,concat('CARGA INICIAL - ', desc_debito),valor_decimal,1
from mov_deb);

-- 
-- FINANCEIRO 3
--

update mov_cred mc,cota c set mc.cota_id = c.id
where mc.numero_cota=c.numero_cota;

INSERT INTO movimento_financeiro_cota
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,TIPO_MOVIMENTO_ID,
USUARIO_ID,LANCAMENTO_MANUAL,COTA_ID,OBSERVACAO,VALOR,fornecedor_id)
(select true,data_real,'CARGA','APROVADO',data_real,
data_real,null,22,1,true,cota_id,concat('CARGA INICIAL - ', desc_credito),valor_decimal,1 -- Default DINAP 
from mov_cred);

-- 
-- 1
--

-- Query OK, 1288970 rows affected (4 min)
-- Insere os estoque de produtos das cotas baseados no arquivo HVND.TXT
INSERT INTO estoque_produto_cota (QTDE_DEVOLVIDA, QTDE_RECEBIDA, COTA_ID, PRODUTO_EDICAO_ID)
( select
csv.QTDE_ENCALHE_HVCT,
csv.QTDE_REPARTE_HVCT,
c.id,
csv.produto_edicao_id
from HVND csv,cota c
where c.numero_cota = csv.COD_COTA_HVCT
and csv.produto_edicao_id is not null);

--
-- 13
--
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
from hvnd 
where produto_edicao_id not in (select produto_edicao_id from lancamento) 
and data_recolhimento <> '0000-00-00' 
and data_recolhimento is not null
and produto_edicao_id is not null);


-- 
-- 2 ********** ATENCAO !!!!!
--

-- Necessário para não tomar erro de ID duplicado (pq não autoincrementa) ao 
-- inserir baseado em uma consulta
-- alter table movimento_estoque_cota modify id bigint(20) AUTO_INCREMENT;

-- Insere os movimentos de estoque da cota de encalhe (tipo_movimento_id = 26) onde houve devolução (estoque produto cota.QTDE_DEVOLVIDA > 0)
-- Alterar DATA_APROVACAO e DATA_CRIACAO para hoje
-- Query OK, 1288970 rows affected (5 min)
INSERT INTO MOVIMENTO_ESTOQUE_COTA
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,STATUS,DATA,DATA_CRIACAO,TIPO_MOVIMENTO_ID,USUARIO_ID,
QTDE,PRODUTO_EDICAO_ID,COTA_ID,ESTOQUE_PROD_COTA_ID,ORIGEM,APROVADOR_ID,LANCAMENTO_ID,status_estoque_financeiro)
(select true,date(sysdate()),'APROVADO',min(h.data_recolhimento),date(sysdate()),
26,1,epc.QTDE_DEVOLVIDA,epc.produto_edicao_id,epc.cota_id,epc.id,'CARGA_INICIAL',1,l.ID,'FINANCEIRO_PROCESSADO'
from estoque_produto_cota epc, hvnd h, lancamento l
where epc.cota_id = h.cota_id
and epc.PRODUTO_EDICAO_ID = h.produto_edicao_id 
and l.produto_edicao_id =h.produto_edicao_id 
and epc.PRODUTO_EDICAO_ID = l.produto_edicao_id 
and h.status = 'F'
and h.produto_edicao_id is not null
group by 1,2,3,5,6,7,8,9,10,11,12,13,14);

-- 
-- 3 ********** ATENCAO !!!!!
--

-- Query OK, 1288970 rows affected (6 min)
-- Insere movimentos de estoque cota de reparte (tipo 21) baseado no estoque de produtos da cota (onde a quantidade recebida > 0)
INSERT INTO MOVIMENTO_ESTOQUE_COTA
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,STATUS,DATA,DATA_CRIACAO,TIPO_MOVIMENTO_ID,
USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,COTA_ID,ESTOQUE_PROD_COTA_ID,ORIGEM,APROVADOR_ID,LANCAMENTO_ID,status_estoque_financeiro)
(select true,date(sysdate()),'APROVADO',min(h.data_lancamento),date(sysdate()),
21,1,epc.QTDE_RECEBIDA,h.produto_edicao_id,epc.cota_id,epc.id,'CARGA_INICIAL',1,l.ID,
case when h.status ='A' then 'FINANCEIRO_NAO_PROCESSADO' else 'FINANCEIRO_PROCESSADO' end 
from estoque_produto_cota epc,hvnd h,lancamento l
where epc.produto_edicao_id = h.produto_edicao_id
and epc.cota_id = h.cota_id
and l.produto_edicao_id =h.produto_edicao_id 
and epc.PRODUTO_EDICAO_ID = l.produto_edicao_id 
and epc.qtde_recebida > 0
and h.produto_edicao_id is not null
group by 1,2,3,5,6,7,8,9,10,11,12,13,14,15);

-- 
-- 4
--

-- Insere edições de produtos que não existem na estoque produto mas existem no estoque de produto da cota.
-- Essa anomalia pode acontecer pq o arquivo HVND que popula o estoque produto pode não conter 
-- movimentos que estão na ESTQBOX (que insere movimentações de estoque baseados no PDV das cotas)
-- (1 min)
insert into estoque_produto (produto_edicao_id)
(select produto_edicao_id from movimento_estoque_cota where produto_edicao_id not in (select produto_edicao_id from estoque_produto) 
group by 1);

-- 
-- 5
--

alter table movimento_estoque modify id bigint(20) AUTO_INCREMENT;

-- Insere movimento de estoque de recebimento de encalhe do distribuidor baseado no 
-- movimento de estoque de envio de encalhe da cota 
-- Query OK, 17550 rows affected (23 min)
INSERT INTO movimento_estoque
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,
DATA_CRIACAO,APROVADOR_ID,TIPO_MOVIMENTO_ID,USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,
ITEM_REC_FISICO_ID,DATA_INTEGRACAO,STATUS_INTEGRACAO,COD_ORIGEM_MOTIVO,DAT_EMISSAO_DOC_ACERTO,NUM_DOC_ACERTO,ORIGEM)
(select 
true,
date(sysdate()),
'CARGA',
'APROVADO',
min(h.data_recolhimento),
date(sysdate()),
null,
31,
1,
sum(mec.QTDE),
mec.PRODUTO_EDICAO_ID,
ep.ID,
null,
null,
null,
null,
null,
null,
'CARGA_INICIAL'
from movimento_estoque_cota mec,estoque_produto ep,hvnd h
where ep.produto_edicao_id = mec.produto_edicao_id
and ep.produto_edicao_id = h.produto_edicao_id
and mec.cota_id = h.cota_id
and mec.tipo_movimento_id = 26
and h.produto_edicao_id is not null
group by
1,2,3,4,6,7,8,9,11,12,13,14,15,16,17,18,19
having sum(mec.QTDE) > 0);

-- 
-- 6
--

-- Query OK, 19056 rows affected (1h e 14m)
-- Insere no movimento de envio de reparte do distribuidor baseado no movimento de estoque de recebimento de reparte da cota
INSERT INTO movimento_estoque
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,TIPO_MOVIMENTO_ID,
USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,ITEM_REC_FISICO_ID,DATA_INTEGRACAO,STATUS_INTEGRACAO,
COD_ORIGEM_MOTIVO,DAT_EMISSAO_DOC_ACERTO,NUM_DOC_ACERTO,ORIGEM)
(select 
true,
date(sysdate()),
'CARGA',
'APROVADO',
min(h.data_lancamento),
date(sysdate()),
null,
13,
1,
sum(mec.QTDE),
mec.PRODUTO_EDICAO_ID,
ep.ID,
null,
null,
null,
null,
null,
null,
'CARGA_INICIAL'
from movimento_estoque_cota mec,estoque_produto ep,hvnd h
where ep.produto_edicao_id = mec.produto_edicao_id
and ep.produto_edicao_id = h.produto_edicao_id
and h.cota_id = mec.cota_id
and mec.tipo_movimento_id = 21
and h.produto_edicao_id is not null
group by
1,2,3,4,6,7,8,9,11,12,13,14,15,16,17,18,19
having sum(mec.QTDE) > 0);


-- 
-- 7
--

-- Query OK, 19056 rows affected (1.13 sec)
-- Insere ENTRADAS de recebimento fisico no movimento estoque do distribuidor 
-- baseado na ENTRADA de REPARTE do movimento estoque da cota
INSERT INTO movimento_estoque
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,
TIPO_MOVIMENTO_ID,USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,ITEM_REC_FISICO_ID,
DATA_INTEGRACAO,STATUS_INTEGRACAO,COD_ORIGEM_MOTIVO,DAT_EMISSAO_DOC_ACERTO,NUM_DOC_ACERTO,ORIGEM)
(select 
true,
date(sysdate()),
'CARGA',
'APROVADO',
min(h.data_lancamento),
date(sysdate()),
null,
20,
1,
sum(mec.qtde),
mec.PRODUTO_EDICAO_ID,
null,
null,
null,
null,
null,
null,
null,
'CARGA_INICIAL'
from movimento_estoque_cota mec,hvnd h
where mec.produto_edicao_id = h.produto_edicao_id
and h.cota_id = mec.cota_id
and mec.tipo_movimento_id = 21
and mec.QTDE > 0
and h.produto_edicao_id is not null
group by 1,2,3,4,6,7,8,9,11,12,13,14,15,16,17,18,19);

-- Atualizando estoque_produto_id no movimento_estoque para manter as referências do banco
update movimento_estoque 
set ESTOQUE_PRODUTO_ID=(select ep.id from estoque_produto ep 
where ep.produto_edicao_id=movimento_estoque.produto_edicao_id) 
where tipo_movimento_id=20;


-- Atualizando os movimentos de recebimento físico com as quantidades inseridas pelo arquivo de ESTQ_BOX,
-- ou seja, Deixando a quantidade recebida igual a quantidade de envio aos jornaleiro ao estoque do distribuidor
-- Robson Martins - Comentado -- + coalesce(ep.qtde_suplementar,0) suplementar já sensibilizado
update movimento_estoque 
set QTDE=COALESCE(QTDE,0)+(select coalesce(ep.qtde,0) -- + coalesce(ep.qtde_suplementar,0) 
from estoque_produto ep where ep.produto_edicao_id=movimento_estoque.produto_edicao_id) 
where tipo_movimento_id=20;


--
-- 8
--

-- insere estoque de produto do fornecedor baseado
-- nos tipos A.R.PROM. do arquivo ESTQBOX
-- Atualizar para data do rollout
insert into estoque_produto (produto_edicao_id)
(select produto_edicao_id from estqbox where box = 191 -- 92 
and produto_edicao_id not in (select produto_edicao_id from estoque_produto));

-- Insere movimentos de estoque de recebimento fisico do distribuidor
-- baseado nos tipos A.R.PROM. do arquivo ESTQBOX
insert into movimento_estoque
(APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,TIPO_MOVIMENTO_ID,
USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,ITEM_REC_FISICO_ID,DATA_INTEGRACAO,STATUS_INTEGRACAO,COD_ORIGEM_MOTIVO,DAT_EMISSAO_DOC_ACERTO,
NUM_DOC_ACERTO,ORIGEM)
(select 
 true,
 date(sysdate()),
 'CARGA',
 'APROVADO',
 min(h.data_lancamento),
 date(sysdate()),
 null,
 20,
 1,
 eb.QUANTIDADE,
 eb.PRODUTO_EDICAO_ID,
 pe.id,
 null,
 null,
 null,
 null,
 null,
 null,
 'CARGA_INICIAL'
 from estqbox eb,estoque_produto pe, hvnd h
 where h.produto_edicao_id = pe.produto_edicao_id
 and eb.box = 191 -- 92 
 and pe.produto_edicao_id = eb.produto_edicao_id
 and h.produto_edicao_id is not null
group by 1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18,19);

--
-- 9
--
/*
-- insere os movimentos de recebimento de mercadoria dos produtos que ainda estão no estoque do distribuidor, ou seja, ainda não foram 
--	lançados para as cota.
insert into movimento_estoque (
APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,TIPO_MOVIMENTO_ID,USUARIO_ID,
QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,ITEM_REC_FISICO_ID,DATA_INTEGRACAO,STATUS_INTEGRACAO,COD_ORIGEM_MOTIVO,
DAT_EMISSAO_DOC_ACERTO,NUM_DOC_ACERTO,ORIGEM)
(select 
 true,
 date(sysdate()),
 'CARGA',
 'APROVADO',
 min(h.data_lancamento),
 date(sysdate()),
 null,
 20,
 1,
 ep.QTDE,
 ep.PRODUTO_EDICAO_ID,
 ep.ID,
 null,
 null,
 null,
 null,
 null,
 null,
 'CARGA_INICIAL'
 from 
 estoque_produto ep,hvnd h
 where h.produto_edicao_id = ep.produto_edicao_id
    -- nand ep.QTDE > 0
 and h.produto_edicao_id is not null
group by 1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18,19);

-- Movimento criado para justificar a entrada no estoque suplementar do distribuidor para o rollout
insert into movimento_estoque (APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,
TIPO_MOVIMENTO_ID,USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,ITEM_REC_FISICO_ID,DATA_INTEGRACAO,
STATUS_INTEGRACAO,COD_ORIGEM_MOTIVO,DAT_EMISSAO_DOC_ACERTO,NUM_DOC_ACERTO,ORIGEM)
(select 
 true,
 date(sysdate()),
 'CARGA',
 'APROVADO',
 min(h.data_lancamento),
 date(sysdate()),
 null,
 191,
 1,
 ep.QTDE_suplementar,
 ep.PRODUTO_EDICAO_ID,
 ep.ID,
 null,
 null,
 null,
 null,
 null,
 null,
 'CARGA_INICIAL'
 from estoque_produto ep,hvnd h
 where h.produto_edicao_id = ep.produto_edicao_id
-- and ep.QTDE_suplementar > 0
and h.produto_edicao_id is not null
group by 1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18,19);

*/

--
-- 10
--

-- Insere a saida domaterial promocional que esteja no estoque do distribuidor ( estqbox tipo 92)
insert into movimento_estoque (APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,
APROVADOR_ID,TIPO_MOVIMENTO_ID,USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,ITEM_REC_FISICO_ID,
DATA_INTEGRACAO,STATUS_INTEGRACAO,COD_ORIGEM_MOTIVO,DAT_EMISSAO_DOC_ACERTO,NUM_DOC_ACERTO,ORIGEM)
(select 
 true,
 date(sysdate()),
 'CARGA',
 'APROVADO',
 min(h.data_lancamento),
 date(sysdate()),
 null,
 181,
 1,
 eb.QUANTIDADE,
 eb.PRODUTO_EDICAO_ID,
 pe.id,
 null,
 null,
 null,
 null,
 null,
 null,
 'CARGA_INICIAL'
 from estqbox eb, estoque_produto pe, hvnd h
 where h.produto_edicao_id = pe.produto_edicao_id
 and eb.box = 191 -- Campinas 92
 and pe.produto_edicao_id = eb.produto_edicao_id
 and h.produto_edicao_id is not null
group by 1,2,3,4,6,7,8,9,10,11,12,13,14,15,16,17,18,19);

--
-- 11
--

-- Query OK, 18756 rows affected (0.97 sec)
-- Insere os moviemento de devolução para o fornecedor baseado no encalhe das cotas
-- Com exceção dos produtos que ainda estão do distribuidor (Esses produto serão excluídos após rollaut)
INSERT INTO movimento_estoque (APROVADO_AUTOMATICAMENTE,DATA_APROVACAO,MOTIVO,STATUS,DATA,DATA_CRIACAO,APROVADOR_ID,TIPO_MOVIMENTO_ID,
USUARIO_ID,QTDE,PRODUTO_EDICAO_ID,ESTOQUE_PRODUTO_ID,ITEM_REC_FISICO_ID,DATA_INTEGRACAO,STATUS_INTEGRACAO,COD_ORIGEM_MOTIVO,DAT_EMISSAO_DOC_ACERTO,
NUM_DOC_ACERTO,ORIGEM)
(select 
true,
date(sysdate()),
'CARGA',
'APROVADO',
me.data,
date(sysdate()),
null,
66,
1,
me.QTDE,
me.PRODUTO_EDICAO_ID,
ep.ID,
null,
null,
null,
null,
null,
null,
'CARGA_INICIAL'
from movimento_estoque me,estoque_produto ep, hvnd h
where me.tipo_movimento_id = 31
and ep.produto_edicao_id = me.produto_edicao_id
and h.produto_edicao_id = ep.produto_edicao_id
-- and me.QTDE > 0
and h.produto_edicao_id is not null
-- Robson Martins Retirado o not in - As devoluções de fornecedores devem ser contabilizadas no fornecedor
and me.produto_edicao_id in (
select distinct(mec.produto_edicao_id) from movimento_estoque_cota mec, estoque_produto p
where mec.tipo_movimento_id = 26 
and mec.produto_edicao_id = p.produto_edicao_id 
and p.qtde_devolucao_encalhe > 0 
) group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18);

-- Atualiza o estoque do distribuidor com as quantidades enviadas p/ o fornecedor
update estoque_produto set qtde_devolucao_fornecedor = coalesce(qtde_devolucao_fornecedor,0) + coalesce((select sum(me.qtde) 
from movimento_estoque me 
where me.tipo_movimento_id = 66 
and me.produto_edicao_id = estoque_produto.produto_edicao_id), 0)
where produto_edicao_id in (select produto_edicao_id from movimento_estoque 
where estoque_produto.produto_edicao_id = produto_edicao_id 
and tipo_movimento_id = 66);

/*
drop table temp_mvto_estq_sem_12;

create table temp_mvto_estq_sem_12 as (
select a.data, c.id as estoque_produto_id, f.nome_box,a.produto_edicao_id,d.nome,d.codigo,e.numero_edicao,
round(a.qtde) me,round(c.qtde_devolucao_encalhe) pe, round(sum(b.qtde)) mec
from movimento_estoque   a, movimento_estoque_cota b, estoque_produto   c,produto     d, 
produto_edicao   e, estqbox    f
where a.tipo_movimento_id = 31 and b.tipo_movimento_id = 26
and a.produto_edicao_id = b.produto_edicao_id
and a.produto_edicao_id = c.produto_edicao_id
and c.produto_edicao_id = b.produto_edicao_id
and a.produto_edicao_id = e.id
and b.produto_edicao_id = e.id
and c.produto_edicao_id = e.id
and e.produto_id        = d.id
and a.produto_edicao_id = f.produto_edicao_id
and b.produto_edicao_id = f.produto_edicao_id
and c.produto_edicao_id = f.produto_edicao_id
and c.produto_edicao_id = e.id
-- and f.nome_box = 'ENCALHE'
and a.qtde  <> c.qtde_devolucao_encalhe  ## somente para 31 X 26
-- and   f.produto_edicao_id   not in (select produto_edicao_id from estqmov where tipo_movto = 12)
group by 1,2,3,4
limit 1000000);

select * from temp_mvto_estq_sem_12;

-- inserir movimento em estoque tipo 18, atribuir o valor ao atributo qtde o resultado da subtração de pe - me
INSERT INTO movimento_estoque
(APROVADO_AUTOMATICAMENTE, DATA_APROVACAO, MOTIVO, STATUS, DATA, DATA_CRIACAO, APROVADOR_ID, TIPO_MOVIMENTO_ID,
USUARIO_ID, QTDE, PRODUTO_EDICAO_ID, ESTOQUE_PRODUTO_ID, ITEM_REC_FISICO_ID, DATA_INTEGRACAO, STATUS_INTEGRACAO,
COD_ORIGEM_MOTIVO, DAT_EMISSAO_DOC_ACERTO, NUM_DOC_ACERTO, ORIGEM)
(select true,	date(sysdate()),	'CARGA',	'APROVADO',	m.data,	date(sysdate()),	null,	18,	1,	(m.pe - m.me),
m.PRODUTO_EDICAO_ID,	m.estoque_produto_id, 	null, 	null,	null, 	null, 	null,	null,	'CARGA_INICIAL'
from temp_mvto_estq_sem_12 m
where pe > me);

-- inserir movimento em estoque tipo 15, atribuir o valor ao atributo qtde da tabela movimento_estoque o resultado 
-- da subtração de me - pe
INSERT INTO movimento_estoque
(APROVADO_AUTOMATICAMENTE, DATA_APROVACAO, MOTIVO, STATUS, DATA, DATA_CRIACAO, APROVADOR_ID, TIPO_MOVIMENTO_ID,
USUARIO_ID, QTDE, PRODUTO_EDICAO_ID, ESTOQUE_PRODUTO_ID, ITEM_REC_FISICO_ID, DATA_INTEGRACAO, STATUS_INTEGRACAO,
COD_ORIGEM_MOTIVO, DAT_EMISSAO_DOC_ACERTO, NUM_DOC_ACERTO, ORIGEM)
(select true,	date(sysdate()),	'CARGA',	'APROVADO',	m.data,	date(sysdate()),	null,	15,	1,	(m.me - m.pe),
m.PRODUTO_EDICAO_ID,	m.estoque_produto_id, 	null, 	null,	null, 	null, 	null,	null,	'CARGA_INICIAL'
from temp_mvto_estq_sem_12 m
where pe < me);


-- Excluir os movimentos tipo 66 da movimento_estoque dos 47 registros da temp.
delete from movimento_estoque 
where PRODUTO_EDICAO_ID in (select t.PRODUTO_EDICAO_ID from temp_mvto_estq_sem_12 t 
where t.PRODUTO_EDICAO_ID = movimento_estoque.PRODUTO_EDICAO_ID)
and tipo_movimento_id = 66;

drop table temp_mvto_estq_sem_12;
drop table temp_mvto_estq_qtde_igual;

create table temp_mvto_estq_qtde_igual(
select a.data, c.id as estoque_produto_id, f.nome_box,a.produto_edicao_id,d.nome,d.codigo,e.numero_edicao,
round(a.qtde) me,round(c.qtde_devolucao_encalhe) pe, round(sum(b.qtde)) mec
from movimento_estoque   a, 
     movimento_estoque_cota b, 
     estoque_produto   c,
  produto     d, 
  produto_edicao   e,
  estqbox    f
where a.tipo_movimento_id = 31 and b.tipo_movimento_id = 26
and a.produto_edicao_id = b.produto_edicao_id
and a.produto_edicao_id = c.produto_edicao_id
and c.produto_edicao_id = b.produto_edicao_id
and a.produto_edicao_id = e.id
and b.produto_edicao_id = e.id
and c.produto_edicao_id = e.id
and e.produto_id        = d.id
and a.produto_edicao_id = f.produto_edicao_id
and b.produto_edicao_id = f.produto_edicao_id
and c.produto_edicao_id = f.produto_edicao_id
and c.produto_edicao_id = e.id
-- and f.nome_box = 'ENCALHE'
and a.qtde  = c.qtde_devolucao_encalhe  ## somente para 31 X 26
and   f.produto_edicao_id   not in (select produto_edicao_id from estqmov where tipo_movto = 12)
group by 1,2,3,4
limit 1000000)
;

delete from movimento_estoque 
where PRODUTO_EDICAO_ID in (select t.PRODUTO_EDICAO_ID from temp_mvto_estq_qtde_igual t 
where t.PRODUTO_EDICAO_ID = movimento_estoque.PRODUTO_EDICAO_ID)
and tipo_movimento_id = 66
;

*/

--
-- 12
--
/*
-- Query OK, 7990 rows affected, 7990 warnings (15 min 55.77 sec)
-- 
drop table temp_script_12_target;

create table temp_script_12_target
as(select a.data_recolhimento, a.produto_edicao_id, a.status from hvnd a, lancamento b 
where  a.produto_edicao_id = b.produto_edicao_id
and    a.status = 'F' and    a.data_recolhimento <> b.data_rec_distrib group by a.data_recolhimento, a.produto_edicao_id, a.status);

ALTER TABLE `temp_script_12_target` ADD INDEX `ndx_produto_edicao_id` (`produto_edicao_id` ASC);
ALTER TABLE `temp_script_12_target` ADD INDEX `ndx_status` (`status` ASC);
optimize table temp_script_12_target;

-- Executar esse update pelo prompt de comando do mysql por ser lento, evita perda de conexões 
-- ou qqr outra interferência no client que faça parar o processo
UPDATE lancamento set status = 'FECHADO', DATA_REC_DISTRIB = (select t.data_recolhimento from temp_script_12_target t
where t.produto_edicao_id = lancamento.produto_edicao_id group by 1)
where produto_edicao_id in (select produto_edicao_id from temp_script_12_target group by 1);

-- ---- ##### -------

drop table temp_script_12_target_2;

create table temp_script_12_target_2
as(
select a.data_lancamento, a.produto_edicao_id from hvnd a, lancamento b 
where  a.produto_edicao_id = b.produto_edicao_id
and    a.data_lancamento <> b.data_lcto_distribuidor group by a.data_lancamento, a.produto_edicao_id, a.status);

ALTER TABLE `temp_script_12_target_2` ADD INDEX `ndx_produto_edicao_id` (`produto_edicao_id` ASC);
ALTER TABLE `temp_script_12_target_2` ADD INDEX `ndx_data_lancamento` (`data_lancamento` ASC);
optimize table temp_script_12_target_2;


-- Executar esse update pelo prompt de comando do mysql por ser lento, evita perda de conexões 
-- ou qqr outra interferência no client que faça parar o processo
UPDATE lancamento SET
DATA_LCTO_DISTRIBUIDOR = (select h.data_lancamento from temp_script_12_target_2 h
where h.produto_edicao_id = lancamento.produto_edicao_id order by 1 limit 1)
where produto_edicao_id in (select h.produto_edicao_id from temp_script_12_target_2 h group by 1);
*/


-- 
-- 15
--

insert into expedicao (data_expedicao, usuario)
(select data,1 from movimento_estoque where tipo_movimento_id=13 group by 1);

-- alter table `expedicao` add index `ndx_data_expedicao` (`data_expedicao` desc);
-- alter table `lancamento` add index `ndx_data_lcto_distribuidor` (`data_lcto_distribuidor` desc);
optimize table expedicao;
optimize table lancamento;

update lancamento 
set expedicao_id = 
(select e.id 
from expedicao e 
where e.data_expedicao = lancamento.data_lcto_distribuidor)
where 
produto_edicao_id 
in (select 
me.produto_edicao_id  
from movimento_estoque me
where me.tipo_movimento_id=13
group by me.produto_edicao_id);

-- 
-- 19
--
/*
update movimento_estoque_cota set lancamento_id = (
select l.id from lancamento l where l.produto_edicao_id = movimento_estoque_cota.produto_edicao_id limit 1);
*/

--
-- 20
--

-- Query OK, 457668 rows affected (22.02 sec)
-- Não deve mais ocorrer Fazer com begin apenas para verificação, caso ocorra, 
-- verificar nos primeiros inserts dos movimento_estoque_cota e movimento_estoque o porquê.


-- update movimento_estoque_cota set data = date(sysdate()) where data = '0000-00-00';

-- Query OK, 11074 rows affected (0.19 sec)
-- update movimento_estoque set data = date(sysdate()) where data = '0000-00-00';
update movimento_estoque_cota set data = 
(select min(l.DATA_REC_DISTRIB) from lancamento l where l.produto_edicao_id = movimento_estoque_cota.produto_edicao_id)
where data = '0000-00-00'
and TIPO_MOVIMENTO_ID = 26;

update movimento_estoque set data = 
(select min(l.DATA_REC_DISTRIB) from lancamento l where l.produto_edicao_id = movimento_estoque.produto_edicao_id)
where data = '0000-00-00'
and TIPO_MOVIMENTO_ID = 31;

--
-- 22
--

-- Query OK, 1007 rows affected (0.14 sec)
insert into CONTROLE_CONFERENCIA_ENCALHE 
(data, status) 
(select 
data,
'CONCLUIDO'
from movimento_estoque
where tipo_movimento_id = 31
group by 1, 2);

--
-- 23
--

-- Query OK, 19056 rows affected (6.18 sec)
insert into CHAMADA_ENCALHE (data_recolhimento, tipo_chamada_encalhe, produto_edicao_id)
(select data, 'MATRIZ_RECOLHIMENTO', m.produto_edicao_id 
from movimento_estoque_cota m, cota c 
where  c.id = m.cota_id 
and tipo_movimento_id = 26 group by m.produto_edicao_id);


update CHAMADA_ENCALHE
set Sequencia = id;

--
-- 24
--

insert into chamada_encalhe_lancamento
(select ce.id, l.id from chamada_encalhe ce, lancamento l
where ce.data_recolhimento = l.data_rec_distrib
and ce.produto_edicao_id = l.produto_edicao_id);

--
-- 25
--

-- Query OK, 1288970 rows affected (33.72 sec)
INSERT INTO CHAMADA_ENCALHE_COTA (fechado, postergado, qtde_prevista, chamada_encalhe_id, cota_id)
(select false, false, qtde, ce.id, m.cota_id
from movimento_estoque_cota m, chamada_encalhe ce
where tipo_movimento_id = 21
and ce.produto_edicao_id = m.produto_edicao_id);

--
-- 26
--

INSERT INTO CONTROLE_CONFERENCIA_ENCALHE_COTA
(data_fim, data_inicio, data_operacao, box_id, ctrl_conf_encalhe_id, 
cota_id, usuario_id)
(select data_recolhimento, data_recolhimento, data_recolhimento,
99, -- alterado de 105 para 99 
cce.id, m.cota_id, 1
from 
movimento_estoque_cota m, 
chamada_encalhe ce,
controle_conferencia_encalhe cce
where 
tipo_movimento_id = 26
and ce.produto_edicao_id = m.produto_edicao_id
and cce.data = m.data
group by 1,2,3,4,5,6,7);

--
-- 27
--

-- Query OK, 17550 rows affected (0.37 sec)
INSERT INTO FECHAMENTO_ENCALHE
(DATA_ENCALHE, QUANTIDADE, PRODUTO_EDICAO_ID)
(select data, qtde,produto_edicao_id
 from movimento_estoque
 where tipo_movimento_id=31 );

INSERT INTO CONTROLE_FECHAMENTO_ENCALHE (select DATA_ENCALHE,1 from fechamento_encalhe group by 1);

--
-- 28
--

-- Query OK, 2576308 rows affected (3 min 43.47 sec)
update MOVIMENTO_ESTOQUE_COTA 
SET preco_venda = (select pe.preco_venda from produto_edicao pe 
where pe.id = movimento_estoque_cota.produto_edicao_id 
and preco_venda is not null 
and preco_venda <>0);

--
-- 29
--

-- Query OK, 227702 rows affected (46.29 sec)
update movimento_estoque_cota set preco_venda = 
(select (select pe2.preco_venda 
from produto_edicao pe2 
where pe2.preco_venda is not null 
and preco_venda != 0 
and pe2.produto_id = produto_edicao.produto_id 
order by pe2.numero_edicao desc limit 1)
from  produto_edicao
where produto_edicao.id = movimento_estoque_cota.produto_edicao_id
group by produto_edicao.produto_id)
where preco_venda is null 
or preco_venda = 0;

--
-- 30
--

-- Query OK, 1288970 rows affected (22 min 17.84 sec)

-- ALTER TABLE `controle_conferencia_encalhe` ADD INDEX `data_controle_conferencia_encalhe` (`data` ASC);
-- ALTER TABLE `movimento_estoque` ADD INDEX `data_movimento_estoque` (`data` ASC);

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

--
-- 31
--

update movimento_estoque_cota set preco_venda = 0 where preco_venda is null;
update movimento_estoque_cota set valor_Desconto = 25; -- validar se desconto DF é 25
update movimento_estoque_cota set preco_com_Desconto = preco_venda - (preco_venda * valor_desconto/100);

-- Isso está sendo feito devido a interface 118 não atualizar preço_previsto e deixa o atributo com zero
update produto_edicao set preco_previsto = preco_venda
where (preco_previsto = 0 or preco_previsto is null)
and preco_venda is not null and preco_venda > 0;

--
-- 32
--

insert into fechamento_encalhe_box (
select qtde,data,produto_edicao_id,99
from movimento_estoque
where tipo_movimento_id = 31);

--
-- 33
--
-- FIXME Remover quando possivel
update lancamento set data_lcto_distribuidor = '0001-01-01' where data_lcto_distribuidor = '0000-00-00';
update movimento_estoque set qtde = 0 where qtde is null;

-- ##################################################################################################################
-- ###########################################  ATUALIZA STATUS LANCAMENTO ##########################################
-- ##################################################################################################################

DROP TABLE IF EXISTS `hvndaux`;

CREATE TABLE `hvndaux` (
`produto_edicao_id` int(11) NOT NULL,
PRIMARY KEY (`produto_edicao_id`)); 

-- A = ABERTO
insert into hvndaux (select distinct produto_edicao_id from hvnd where data_recolhimento <> '0000-00-00' and status = 'A');

update lancamento
set status = 'BALANCEADO_RECOLHIMENTO'
where PRODUTO_EDICAO_ID in (select distinct produto_edicao_id from hvndaux);

-- 0000-00-00 = EXPEDIDO

truncate hvndaux;

insert into hvndaux (select distinct produto_edicao_id from hvnd where data_recolhimento = '0000-00-00' and status = 'A');

update lancamento
set status = 'EXPEDIDO'
where PRODUTO_EDICAO_ID in (select distinct produto_edicao_id from hvndaux);

-- F = FECHADO

truncate hvndaux;

insert into hvndaux (select distinct produto_edicao_id from hvnd where status = 'F');

update lancamento
set status = 'FECHADO'
where PRODUTO_EDICAO_ID in (select distinct produto_edicao_id from hvndaux);

-- ##################################################################################################################
-- ############################################ NORMALIZA CARGA SEGMENTO ############################################
-- ##################################################################################################################

/*
DROP TABLE IF EXISTS `carga_segmento`;

CREATE TABLE `carga_segmento` (
  `codigo` varchar(8) NOT NULL,
  `segmento` int NOT NULL
);

-- deve ser executado pela console pois o arquivo nao esta local, está no servidor
LOAD DATA INFILE '07040001.prd' INTO TABLE carga_segmento
(@var1)
SET codigo=SUBSTR(@var1,34,8),
    segmento=SUBSTR(@var1,581,2);

LOAD DATA INFILE '07040002.prd' INTO TABLE carga_segmento
(@var1)
SET codigo=SUBSTR(@var1,34,8),
    segmento=SUBSTR(@var1,581,2);



-- Caso precise normalizar os segmento dos produtos
update (
select distinct  p.codigo,p.nome,ts.descricao,a.segmento
from carga_segmento a , produto p, tipo_segmento_produto ts
where a.codigo = p.codigo
and ts.id =a.segmento
and p.tipo_segmento_produto_id in (0, 9)
order by ts.descricao
) z,produto px
set px.tipo_segmento_produto_id = z.segmento
where px.codigo = z.codigo;


update  (
select distinct p.codigo as codigo_aux,p.nome as nome_aux,p.tipo_segmento_produto_id as tipo_segmento_produto_id_aux, aux.tipo_segmento_produto_id as tipo_segmento_produto_id_aux2,ts.descricao as descricao_aux from (
select * from (
select codigo_icd,codigo, nome, tipo_segmento_produto_id from produto where codigo_icd in (
select codigo_icd from produto 
where tipo_segmento_produto_id in (0,9)) 
) s where s.tipo_segmento_produto_id not in (0,9)
) aux, produto p,tipo_segmento_produto ts
where p.tipo_segmento_produto_id in (0,9)
and aux.codigo_icd = p.codigo_icd
and ts.id = aux.tipo_segmento_produto_id
)aa, produto pp
set pp.tipo_segmento_produto_id = aa.tipo_segmento_produto_id_aux2
where pp.codigo = aa.codigo_aux
and pp.tipo_segmento_produto_id in (0,9);

update produto set tipo_segmento_produto_id = 9 where tipo_segmento_produto_id is null;
update produto set tipo_segmento_produto_id = 9 where tipo_segmento_produto_id =0;

update ranking_segmento set tipo_segmento_produto_id = 9 where tipo_segmento_produto_id is null;
update ranking_segmento set tipo_segmento_produto_id = 9 where tipo_segmento_produto_id =0;

delete from tipo_segmento_produto where id = 0;
*/
-- ##################################################################################################################
-- ######################################### NORMALIZA CARGA CLASSIFICAÇÃO ##########################################
-- ##################################################################################################################
/*
DROP TABLE IF EXISTS `carga_classificacao`;

CREATE TABLE `carga_classificacao` (
  `codigo` varchar(8) NOT NULL,
  `edicao` int NOT NULL,
  `classificacao` varchar(28) NOT NULL
);

-- deve ser executado pela console pois o arquivo nao esta local, está no servidor
LOAD DATA INFILE '07040001.prd' INTO TABLE carga_classificacao
(@var1)
SET codigo=SUBSTR(@var1,34,8),
    edicao=SUBSTR(@var1,42,4),
    classificacao=SUBSTR(@var1,689,28);

LOAD DATA INFILE '07040002.prd' INTO TABLE carga_classificacao
(@var1)
SET codigo=SUBSTR(@var1,34,8),
    edicao=SUBSTR(@var1,42,4),
    classificacao=SUBSTR(@var1,689,28);

update (
select  p.codigo,p.nome,ts.descricao,a.classificacao,a.edicao
from carga_classificacao a , produto p,produto_edicao pe, tipo_classificacao_produto ts
where a.codigo = p.codigo
and p.id = pe.produto_id
and ts.descricao =a.classificacao
and pe.tipo_classificacao_produto_id in (0, 16)
and pe.numero_edicao = a.edicao
and ts.descricao =a.classificacao
order by ts.descricao limit 100000
) z,produto px,produto_edicao pex
set pex.tipo_classificacao_produto_id = z.classificacao
where px.codigo = z.codigo
and pex.numero_edicao = z.edicao
and px.id = pex.produto_id
and z.descricao<>z.classificacao;

update produto_edicao set tipo_classificacao_produto_id = 16 where tipo_classificacao_produto_id  is null;
update produto_edicao set tipo_classificacao_produto_id = 16 where tipo_classificacao_produto_id  =0;


delete from tipo_classificacao_produto where id =0;
*/

-- ##################################################################################################################
-- ############################################## NORMALIZA ENDEREÇOS ###############################################
-- ##################################################################################################################

-- Normalizacao de endereços (verificar os que faltam) e os tipos logradouro

update endereco set tipo_logradouro = 'RUA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('Rua','R. ','R..','Rua');

update endereco set tipo_logradouro = 'AVENIDA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('Av.','A. ','A..');

update endereco set tipo_logradouro = 'PRAÇA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('PC.','PCA ','P..');

update endereco set tipo_logradouro = 'ESTRADA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('ES.');

update endereco set tipo_logradouro = 'LARGO'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('LR.');

update endereco set tipo_logradouro = 'RODOVIA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('RO.');

update endereco set tipo_logradouro = 'ALAMENDA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('AL.');

update endereco set tipo_logradouro = 'TRAVESSA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('TR.','TV.');

-- select substr(logradouro,1,3) from endereco where logradouro is not null  limit 100000

update endereco set logradouro=substring( logradouro,5) 
where logradouro <> '   ' 
and substring( logradouro,1,3) in ('Rua','PC.','AV.','RUA','ES.','R..','LR.','RO.','AL.','TR.','TV.');

-- ##################################################################################################################
-- ####################################### ATUALIZA TIPO DISTRIBUIÇÃO DA COTA #######################################
-- ##################################################################################################################

update cota set tipo_distribuicao_cota = 'ALTERNATIVO'
where numero_cota in (238,357,507,508,540,607,608,609,610,704,709,814,
926,951,958,1154,1160,1190,1196,1198,4000,4401,4410,4414,4437,4442,4446,5058);

-- ##################################################################################################################
-- ##################################### ATUALIZA COTAS PARA INATIVAS  ##############################################
-- ##################################################################################################################

update cota set SITUACAO_CADASTRO = 'INATIVO'
where SITUACAO_CADASTRO = 'PENDENTE';

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


-- Atualizar Tipo Distribuição Cota

update cota
set tipo_distribuicao_cota = 'CONVENCIONAL'
where tipo_distribuicao_cota is null;

-- Atualizar Descontos Produtos

update produto_edicao
set desconto = 25.00
where origem = 'MANUAL';

update produto
set desconto = 25.00
where origem = 'MANUAL';

update estoque_produto set versao=0


 

