delete from nota_envio_item;
delete from nota_envio;
delete  from fechamento_encalhe_box;
delete  from fechamento_encalhe;
delete  from controle_fechamento_encalhe;
delete  from conferencia_encalhe;
delete  from cobranca_controle_conferencia_encalhe_cota;
delete  from controle_conferencia_encalhe_cota;
delete  from controle_conferencia_encalhe;
delete  from conferencia_enc_parcial;
delete  from conferencia_encalhe;
delete  from chamada_encalhe_cota;
delete  from chamada_encalhe_lancamento;
delete  from chamada_encalhe;
delete  from negociacao_cobranca_originaria;
delete from baixa_cobranca;
delete  from cobranca;
delete  from divida;
delete  from consolidado_mvto_financeiro_cota;
delete  from consolidado_financeiro_cota;
delete  from historico_movto_financeiro_cota;
delete  from venda_produto_movimento_financeiro;
delete  from venda_produto_movimento_estoque;
delete  from venda_produto;
delete  from nota_fiscal_produto_servico_movimento_estoque_cota;
delete  from lancamento_diferenca_movimento_estoque_cota;
delete  from parcela_negociacao;
delete  from cota_ausente_movimento_estoque_cota;
delete  from movimento_estoque_cota;
delete from movimento_financeiro_cota;
delete  from movimento_financeiro_cota;
delete  from rateio_cota_ausente;
delete  from cota_ausente;
delete  from estoque_produto_cota;

delete from lancamento_item_receb_fisico;
delete from rateio_diferenca;
update movimento_estoque set item_rec_fisico_id = null;
update item_receb_fisico set diferenca_id = null;
delete from diferenca;
delete from item_receb_fisico;
delete from lancamento_diferenca;
delete from movimento_estoque;
delete from estoque_produto;

update nota_fiscal_entrada  set nota_fiscal_entrada.STATUS_NOTA_FISCAL = 'NAO_RECEBIDA'  where nota_fiscal_entrada.ID IN (select recebimento_fisico.NOTA_FISCAL_ID from recebimento_fisico);

update lancamento  set  lancamento.`STATUS` = 'BALANCEADO'
where lancamento.PRODUTO_EDICAO_ID IN (
	select item_nota_fiscal_entrada.PRODUTO_EDICAO_ID from nota_fiscal_entrada 
	join recebimento_fisico on recebimento_fisico.NOTA_FISCAL_ID = nota_fiscal_entrada.ID 
	join item_nota_fiscal_entrada on item_nota_fiscal_entrada.NOTA_FISCAL_ID = nota_fiscal_entrada.ID
);

update diferenca set diferenca.itemRecebimentoFisico_ID = null, diferenca.LANCAMENTO_DIFERENCA_ID = null;
delete from lancamento_item_receb_fisico;
delete from lancamento_diferenca;
delete from movimento_estoque;
delete from item_receb_fisico;
delete from rateio_diferenca;
delete from diferenca;
delete from recebimento_fisico;
delete from movimento_estoque;

update lancamento set lancamento.EXPEDICAO_ID = null;
delete  from expedicao;

delete from estoque_produto;


create table CSVImport
 (COD_AGENTE_HVCT int, 
 COD_CONTEXTO_AGENTE_HVCT tinyint,
 COD_COTA_HVCT int,
 COD_PRODUTO_HVCT varchar(12),
 IND_DESVIO_PADRAO_ABS_HVCT float(11,4),
 QTDE_DANIFICADO_ACEITO_HVCT int,
 QTDE_DANIFICADO_REJEITADO_HVCT int,
 QTDE_ENCALHE_HVCT int,
 QTDE_NIVELAMENTO_HVCT int,
 QTDE_RECHACADO_HVCT int,
 QTDE_REPARTE_BASE_CALCULO_HVCT int,
 QTDE_REPARTE_HVCT int,
 QTDE_SUPLEMENTACAO_HVCT int,
 QTDE_VENDA_BASE_CALCULO_HVCT int,
 QTDE_VENDA_ESTIMADA_HVCT int,
 QTDE_VENDA_HVCT int,
 QTDE_VENDA_TRATADA_HVCT int,
 TIPO_ENCALHE_HVCT char(1),
 TIPO_HISTORICO_HVCT char(1),
 TIPO_ORIGEM_HISTORICO_HVCT char(1),
 TIPO_ORIGEM_REPARTE_HVCT char(3),
 TIPO_VENDA_HVCT char(1),
 VLR_VENDA_HVCT float(14,2),
 cod_produto varchar(8),
 num_edicao int,
 produto_edicao_id bigint
 );

LOAD DATA LOCAL INFILE 'HVCT.TXT' INTO TABLE CSVImport COLUMNS TERMINATED BY ';' LINES TERMINATED BY ';\n';

update CSVImport set cod_produto=substring(COD_PRODUTO_HVCT, -12, 8), num_edicao=substring(COD_PRODUTO_HVCT, -4);

update CSVImport set produto_edicao_id = 
(select pe.id from produto_edicao pe, produto p 
where p.id = pe.produto_id 
and p.codigo = CSVImport.cod_produto 
and pe.numero_edicao = CSVImport.num_edicao);

ALTER TABLE `csvimport` ADD INDEX `csvimport_cota_id` (`COD_COTA_HVCT` ASC, `cod_produto` ASC, `num_edicao` ASC, `produto_edicao_id` ASC) ;

create table RCPR
(COD_AGENTE_RCPR int,
COD_CONTEXTO_AGENTE_RCPR int,
COD_FORMA_INCLUSAO_RCPR varchar(1),
COD_PRODUTO_RCPR varchar(12),
COD_STATUS_SINALIZADORES_RCPR varchar(1),
DATA_LIMITE_RECOLTO_RCPR varchar(12),
DATA_PREVISTA_RECOLTO_RCPR varchar(12),
DATA_REAL_RECOLTO_RCPR varchar(12),
NUM_RECOLTO_RCPR int,
NUM_SEQUENCIA_RECOLTO_RCPR int,
TIPO_DESCONTO_TPDS int,
TIPO_REGIME_RECOLHIMENTO_RGRC varchar(1),
TIPO_STATUS_RECOLTO_RCPR varchar(3),
TXT_BOLETIM_INFORMATIVO_RCPR varchar(12),
UNIDADE_RECOLHIMENTO_RCPR varchar(12),
cod_produto varchar(8),
num_edicao int,
produto_edicao_id int
);

LOAD DATA LOCAL INFILE 'RCPR.TXT' INTO TABLE RCPR COLUMNS TERMINATED BY ';' LINES TERMINATED BY ';\n';

update rcpr set cod_produto=substring(COD_PRODUTO_RCPR, -12, 8), num_edicao=substring(COD_PRODUTO_RCPR, -4);

update RCPR set produto_edicao_id = (select pe.id from produto p, produto_edicao pe where p.id = pe.produto_id and p.codigo = RCPR.cod_produto and pe.numero_edicao = RCPR.num_edicao);

create table estqbox (
    linha_vazia varchar(1),
    tipo int,
    box int,
    nome_box varchar(10),
    produto varchar(8),
    edicao int,
    nome_produto varchar(45),
    quantidade int,
    produto_edicao_id int);

LOAD DATA LOCAL INFILE 'ESTQBOX.NEW' INTO TABLE estqbox COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\n';

update estqbox set produto_edicao_id = (select pe.id from produto_edicao pe, produto p where
p.id = pe.produto_id and p.codigo = produto and pe.numero_edicao = edicao);

update estoque_produto
set qtde=null, qtde_devolucao_encalhe=null, qtde_devolucao_fornecedor=null, qtde_suplementar=null;

delete from estqbox where  produto_edicao_id is null;

insert into estoque_produto (QTDE, QTDE_DEVOLUCAO_ENCALHE, QTDE_SUPLEMENTAR, PRODUTO_EDICAO_ID)
(select case when box=60 then quantidade else null end, 
        case when box=70 then quantidade else null end,
        case when box=80 then quantidade else null end,
   produto_edicao_id from estqbox
    where box not in (85,92));

create table LANP_RCPR
 (COD_AGENTE_LANP int,
  COD_PRODUTO_LANP varchar(8),
  COD_PRODIN varchar(8),
  NUM_EDICAO int,
  DATA_PREVISTA_LANCAMENTO_LANP varchar(20),
  DATA_REAL_LANCAMENTO_LANP varchar(20),
  TIPO_STATUS_LANCTO_LANP  varchar(3),
  TIPO_LANCAMENTO_LANP  varchar(3),
  VLR_PRECO_REAL_LANP  bigint,
  DATA_PREVISTA_RECOLTO_RCPR  varchar(20),
  DATA_REAL_RECOLTO_RCPR  varchar(20),
  TIPO_STATUS_RECOLTO_RCPR varchar(3),
  produto_edicao_id bigint
);

LOAD DATA LOCAL INFILE 'export_LANP_RCPR.csv' INTO TABLE LANP_RCPR COLUMNS TERMINATED BY ';' LINES TERMINATED BY '\n';

update LANP_RCPR set COD_PRODIN = lpad(COD_PRODIN, 8, 0);

update LANP_RCPR set produto_edicao_id = 
(select pe.id from produto_edicao pe, produto p 
where p.id = pe.produto_id 
and p.codigo = LANP_RCPR.COD_PRODIN
and pe.numero_edicao = LANP_RCPR.num_edicao);

ALTER TABLE `LANP_RCPR` ADD INDEX `lanp_rcpr_produto_edicao_id` (`produto_edicao_id` ASC);

INSERT INTO estoque_produto_cota
    (QTDE_DEVOLVIDA, QTDE_RECEBIDA, VERSAO, COTA_ID, PRODUTO_EDICAO_ID)

( select
    csv.QTDE_ENCALHE_HVCT,
    csv.QTDE_REPARTE_HVCT,
    null,
    c.id,
    pe.id
    from CSVImport csv,
         cota c,
         produto p,
         produto_edicao pe
where c.numero_cota = csv.COD_COTA_HVCT
    and p.codigo = csv.cod_produto
    and pe.numero_edicao = csv.num_edicao
    and p.id = pe.produto_id);

alter table movimento_estoque_cota modify id bigint(20) AUTO_INCREMENT;

alter table movimento_estoque modify id bigint(20) AUTO_INCREMENT;

INSERT INTO MOVIMENTO_ESTOQUE_COTA
(APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 COTA_ID,
 ESTOQUE_PROD_COTA_ID,
 ORIGEM,
 APROVADOR_ID
)
(select 
	true,
	'2013-06-15',
 	'APROVADO',
	'2013-06-15',
	'2013-06-15',
	26,
	1,
	epc.QTDE_DEVOLVIDA,
 	pe.id,
	c.id,
	epc.id,
	'CARGA_INICIAL',
	1

    from cota c,
         produto p,
         produto_edicao pe,
	 estoque_produto_cota epc
   where 
	p.id = pe.produto_id
    and epc.cota_id = c.id
    and epc.produto_edicao_id = pe.id
    and epc.QTDE_DEVOLVIDA > 0);

INSERT INTO MOVIMENTO_ESTOQUE_COTA
(
 APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 COTA_ID,
 ESTOQUE_PROD_COTA_ID,
 ORIGEM,
 APROVADOR_ID
)
(select 
	true,
	'2013-06-15',
 	'APROVADO',
	'2013-06-15',
	'2013-06-15',
	21,
	1,
	epc.QTDE_RECEBIDA,
 	pe.id,
	c.id,
	epc.id,
	'CARGA_INICIAL',
	1

    from cota c,
         produto p,
         produto_edicao pe,
	 estoque_produto_cota epc
   where 
	p.id = pe.produto_id
    and epc.cota_id = c.id
    and epc.produto_edicao_id = pe.id
    and epc.qtde_recebida > 0);

insert into estoque_produto 
(produto_edicao_id)
(select produto_edicao_id
from movimento_estoque_cota 
where produto_edicao_id not in (select produto_edicao_id from estoque_produto) 
group by 1);

INSERT INTO movimento_estoque
(
 APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 MOTIVO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 APROVADOR_ID,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 ESTOQUE_PRODUTO_ID,
 ITEM_REC_FISICO_ID,
 DATA_INTEGRACAO,
 STATUS_INTEGRACAO,
 COD_ORIGEM_MOTIVO,
 DAT_EMISSAO_DOC_ACERTO,
 NUM_DOC_ACERTO,
 ORIGEM)

(select 
	true,
        '2013-06-15',
        'CARGA',
        'APROVADO',
        '2013-06-15',
        '2013-06-15',
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

    from movimento_estoque_cota mec,
	 estoque_produto ep
where mec.tipo_movimento_id = 26
    and ep.produto_edicao_id = mec.produto_edicao_id
group by
	mec.PRODUTO_EDICAO_ID, ep.ID
having sum(mec.QTDE) > 0);

INSERT INTO movimento_estoque
(
 APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 MOTIVO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 APROVADOR_ID,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 ESTOQUE_PRODUTO_ID,
 ITEM_REC_FISICO_ID,
 DATA_INTEGRACAO,
 STATUS_INTEGRACAO,
 COD_ORIGEM_MOTIVO,
 DAT_EMISSAO_DOC_ACERTO,
 NUM_DOC_ACERTO,
 ORIGEM)

(select 
	true,
        '2013-06-15',
        'CARGA',
        'APROVADO',
        '2013-06-15',
        '2013-06-15',
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

    from movimento_estoque_cota mec,
	 estoque_produto ep
where mec.tipo_movimento_id = 21
    and ep.produto_edicao_id = mec.produto_edicao_id
group by
	mec.PRODUTO_EDICAO_ID, ep.ID
having sum(mec.QTDE) > 0);

INSERT INTO movimento_estoque
(
 APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 MOTIVO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 APROVADOR_ID,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 ESTOQUE_PRODUTO_ID,
 ITEM_REC_FISICO_ID,
 DATA_INTEGRACAO,
 STATUS_INTEGRACAO,
 COD_ORIGEM_MOTIVO,
 DAT_EMISSAO_DOC_ACERTO,
 NUM_DOC_ACERTO,
 ORIGEM)

(select 
	true,
        '2013-06-15',
        'CARGA',
        'APROVADO',
        '2013-06-15',
        '2013-06-15',
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

    from movimento_estoque_cota mec
where mec.tipo_movimento_id = 21
      and mec.QTDE > 0
group by 1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19);


update movimento_estoque set
data = (select min(l.data_lcto_distribuidor)
from 
lancamento l
where 
l.produto_edicao_id = movimento_estoque.produto_edicao_id
group by
l.produto_edicao_id
)
where 
tipo_movimento_id = 20;

update movimento_estoque set ESTOQUE_PRODUTO_ID=(select ep.id from estoque_produto ep where ep.produto_edicao_id=movimento_estoque.produto_edicao_id) where tipo_movimento_id=20;
update movimento_estoque set QTDE=COALESCE(QTDE,0)+(select coalesce(ep.qtde,0) + coalesce(ep.qtde_suplementar,0) from estoque_produto ep where ep.produto_edicao_id=movimento_estoque.produto_edicao_id) where tipo_movimento_id=20;

update movimento_estoque set data = '2013-06-15' where 
tipo_movimento_id = 20 and data = '0000-00-00';

insert into movimento_estoque
(APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 MOTIVO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 APROVADOR_ID,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 ESTOQUE_PRODUTO_ID,
 ITEM_REC_FISICO_ID,
 DATA_INTEGRACAO,
 STATUS_INTEGRACAO,
 COD_ORIGEM_MOTIVO,
 DAT_EMISSAO_DOC_ACERTO,
 NUM_DOC_ACERTO,
 ORIGEM)
(select 
 true,
 '2013-06-15',
 'CARGA',
 'APROVADO',
 '2013-06-15',
 '2013-06-15',
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
	 estoque_produto ep
 where 
    ep.QTDE > 0
);

insert into movimento_estoque
(APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 MOTIVO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 APROVADOR_ID,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 ESTOQUE_PRODUTO_ID,
 ITEM_REC_FISICO_ID,
 DATA_INTEGRACAO,
 STATUS_INTEGRACAO,
 COD_ORIGEM_MOTIVO,
 DAT_EMISSAO_DOC_ACERTO,
 NUM_DOC_ACERTO,
 ORIGEM)
(select 
 true,
 '2013-06-15',
 'CARGA',
 'APROVADO',
 '2013-06-15',
 '2013-06-15',
 null,
 38,
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
 from 
	 estoque_produto ep
 where 
    ep.QTDE_suplementar > 0
);

INSERT INTO movimento_estoque
(
 APROVADO_AUTOMATICAMENTE,
 DATA_APROVACAO,
 MOTIVO,
 STATUS,
 DATA,
 DATA_CRIACAO,
 APROVADOR_ID,
 TIPO_MOVIMENTO_ID,
 USUARIO_ID,
 QTDE,
 PRODUTO_EDICAO_ID,
 ESTOQUE_PRODUTO_ID,
 ITEM_REC_FISICO_ID,
 DATA_INTEGRACAO,
 STATUS_INTEGRACAO,
 COD_ORIGEM_MOTIVO,
 DAT_EMISSAO_DOC_ACERTO,
 NUM_DOC_ACERTO,
 ORIGEM)

(select 
	true,
        '2013-04-04',
        'CARGA',
        'APROVADO',
        me.data,
        '2013-04-04',
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

    from movimento_estoque me,
	 estoque_produto ep
where me.tipo_movimento_id = 31
    and ep.produto_edicao_id = me.produto_edicao_id
    and me.QTDE > 0
    and me.produto_edicao_id not in 
(select distinct(mec.produto_edicao_id) from movimento_estoque_cota mec, estoque_produto p
where mec.tipo_movimento_id = 26 
and mec.produto_edicao_id = p.produto_edicao_id 
and coalesce(p.qtde_devolucao_encalhe) > 0) );

update estoque_produto
set qtde_devolucao_fornecedor = 
coalesce(qtde_devolucao_fornecedor,0)+coalesce((select sum(me.qtde) 
from movimento_estoque me 
where me.tipo_movimento_id = 66 
and me.produto_edicao_id = estoque_produto.produto_edicao_id), 0);


UPDATE lancamento set status = 'FECHADO', 
    DATA_REC_DISTRIB = (select r.data_real_recolto_rcpr from LANP_RCPR r 
                        where r.TIPO_STATUS_LANCTO_LANP = 'FEC' 
                            and r.produto_edicao_id = lancamento.produto_edicao_id 
                            and r.produto_edicao_id is not null group by 1)
where produto_edicao_id in (select r.produto_edicao_id from LANP_RCPR r 
                            where r.TIPO_STATUS_LANCTO_LANP = 'FEC'
                            and r.produto_edicao_id is not null group by 1);

UPDATE lancamento SET
    DATA_LCTO_DISTRIBUIDOR = (select r.data_real_lancamento_LANP from LANP_RCPR r 
                        where r.produto_edicao_id = lancamento.produto_edicao_id 
                            and r.produto_edicao_id is not null group by 1 order by 1 limit 1)
where produto_edicao_id in (select r.produto_edicao_id from LANP_RCPR r 
                            where r.produto_edicao_id is not null group by 1);


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
'2013-06-15',
STR_TO_DATE(data_real_lancamento_lanp,'%m/%d/%Y'),
STR_TO_DATE(data_prevista_lancamento_lanp,'%m/%d/%Y'),
STR_TO_DATE(data_real_recolto_rcpr,'%m/%d/%Y'),
STR_TO_DATE(data_prevista_recolto_rcpr,'%m/%d/%Y'),
'2013-06-15',
'EXPEDIDO',
'LANCAMENTO',
produto_edicao_id
from lanp_rcpr 
where produto_edicao_id not in (select produto_edicao_id from lancamento) 
and data_prevista_recolto_rcpr != '');

update movimento_estoque set
data = (select min(l.data_lcto_distribuidor)
from 
lancamento l
where 
l.produto_edicao_id = movimento_estoque.produto_edicao_id
group by
l.produto_edicao_id
)
where 
tipo_movimento_id = 13;

insert into expedicao (data_expedicao, usuario)
(select data,1 from movimento_estoque where tipo_movimento_id=13 group by 1);

update lancamento set expedicao_id = (select e.id from expedicao e where e.data_expedicao = lancamento.data_lcto_distribuidor);

update movimento_estoque_cota set
data = 
(select min(l.data_lcto_distribuidor)
from 
lancamento l
where 
l.produto_edicao_id = movimento_estoque_cota.produto_edicao_id
group by
l.produto_edicao_id
)
where 
tipo_movimento_id = 21;

update movimento_estoque set
data = (select min(l.data_rec_distrib)
from 
lancamento l
where 
l.produto_edicao_id = movimento_estoque.produto_edicao_id
group by
l.produto_edicao_id
)
where 
 tipo_movimento_id = 31;

update movimento_estoque_cota set
data = 
(select min(l.data_rec_distrib)
from 
lancamento l
where 
l.produto_edicao_id = movimento_estoque_cota.produto_edicao_id
group by
l.produto_edicao_id
)
where 
tipo_movimento_id = 26;

update movimento_estoque_cota set lancamento_id = 
(select l.id from lancamento l where l.produto_edicao_id = movimento_estoque_cota.produto_edicao_id limit 1);

update movimento_estoque_cota set data = '2013-06-15' where data = '0000-00-00';

update movimento_estoque set data = '2013-06-15' where data = '0000-00-00';

create table tmpLancamento (lancamento_id int);

insert into tmpLancamento (select l.id from lancamento l, movimento_estoque m
where l.produto_edicao_id = m.produto_edicao_id
and l.data_lcto_distribuidor = m.data group by l.produto_edicao_id);

update lancamento set status = 'EXPEDIDO'
where id in (select lancamento_id from tmpLancamento);

drop table tmpLancamento;

insert into CONTROLE_CONFERENCIA_ENCALHE 
(data, status) 
(select 
data,
'CONCLUIDO'
from movimento_estoque
where tipo_movimento_id = 31
group by 1, 2);

insert into CHAMADA_ENCALHE (
data_recolhimento, tipo_chamada_encalhe, produto_edicao_id, sequencia
)

(select data, 'MATRIZ_RECOLHIMENTO', m.produto_edicao_id, null
    from 
        movimento_estoque_cota m, 
        cota c
    where 
        c.id = m.cota_id
    and tipo_movimento_id = 26 group by m.produto_edicao_id);

insert into chamada_encalhe_lancamento
(select ce.id, l.id from chamada_encalhe ce, lancamento l
where
ce.data_recolhimento = l.data_rec_distrib
and ce.produto_edicao_id = l.produto_edicao_id);

