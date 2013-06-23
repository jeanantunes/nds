INSERT INTO CHAMADA_ENCALHE_COTA (fechado, postergado, qtde_prevista, chamada_encalhe_id, cota_id)

(select false, false, qtde, ce.id, m.cota_id
    from 
        movimento_estoque_cota m, 
        chamada_encalhe ce
    where 
        tipo_movimento_id = 21
    and ce.produto_edicao_id = m.produto_edicao_id);

create table tmpEstudo (estudo_id int);

insert into tmpEstudo 
(select estudo.id
from estudo, lancamento
where lancamento.PRODUTO_EDICAO_ID = estudo.PRODUTO_EDICAO_ID
and lancamento.DATA_LCTO_PREVISTA = estudo.DATA_LANCAMENTO
group by estudo.PRODUTO_EDICAO_ID, estudo.DATA_LANCAMENTO having count(*) > 1);

update nota_envio_item set estudo_cota_id = null
where estudo_cota_id in
(select id from estudo_cota where estudo_id in (select estudo_id from tmpEstudo));

delete from estudo_cota where estudo_id in (select estudo_id from tmpEstudo);

delete from estudo where id in (select estudo_id from tmpEstudo);

drop table tmpEstudo;

INSERT INTO FECHAMENTO_ENCALHE
(DATA_ENCALHE, QUANTIDADE, PRODUTO_EDICAO_ID)

(select data, 
	qtde,
	produto_edicao_id
 from movimento_estoque
 where tipo_movimento_id=31 );

INSERT INTO CONTROLE_FECHAMENTO_ENCALHE 
(select DATA_ENCALHE from fechamento_encalhe where data_encalhe <='2013-04-22' group by 1);

update MOVIMENTO_ESTOQUE_COTA SET preco_venda = (select pe.preco_venda from produto_edicao pe where pe.id = movimento_estoque_cota.produto_edicao_id);

update movimento_estoque_cota set preco_venda = 
(select (select pe2.preco_venda from produto_edicao pe2 where pe2.preco_venda is not null and preco_venda != 0 and pe2.produto_id = produto_edicao.produto_id order by pe2.numero_edicao desc limit 1)
from
produto_edicao
where
produto_edicao.id = movimento_estoque_cota.produto_edicao_id
group by produto_edicao.produto_id)
where preco_venda is null or preco_venda = 0;

update movimento_estoque_cota set preco_venda = 0 where preco_venda is null;
update movimento_estoque_cota set valor_Desconto = 23;
update movimento_estoque_cota set preco_com_Desconto = preco_venda - (preco_venda * valor_desconto/100);

update movimento_estoque_cota set valor_Desconto = 16, preco_com_desconto = preco_venda - (preco_venda * 16/100)
where cota_id in 
(select id from cota where numero_cota in (356,350,369,352,362,355,359,354,371,353,380,366,377,364,378,374,381,351,365,368,376,384,382));

insert into fechamento_encalhe_box
(select 
qtde,data,produto_edicao_id,105
from movimento_estoque
where tipo_movimento_id = 31);

create table mov_cred (linha_inicial varchar(1), 
                       data varchar(8),
                       numero_cota int,
                       tipo_credito int,
                       desc_credito varchar(255),
                       valor varchar(11),
                       valor_decimal decimal(11,2),
                       data_real date,
                       cota_id int);

create table mov_deb (linha_inicial varchar(1),
                      data varchar(8),
                      numero_cota int,
                      tipo_debito int,
                      desc_debito varchar(255),
                      valor varchar(11),
                      valor_decimal decimal(11,2),
                      data_real date,
                      cota_id int);

LOAD DATA LOCAL INFILE 'ARQCD08.NEW' INTO TABLE mov_cred COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\n';

LOAD DATA LOCAL INFILE 'ARQDB97.NEW' INTO TABLE mov_deb COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\n';

update mov_cred set valor_decimal = (cast(valor as decimal(12.2))/100), data_real = str_to_date(data, '%Y%m%d'), cota_id = (select c.id from cota c where c.numero_cota = mov_cred.numero_cota);
update mov_deb set valor_decimal = (cast(valor as decimal(12.2))/100), data_real = str_to_date(data, '%Y%m%d'), cota_id = (select c.id from cota c where c.numero_cota = mov_deb.numero_cota);

alter table movimento_financeiro_cota modify id bigint(20) AUTO_INCREMENT;

INSERT INTO movimento_financeiro_cota
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
 LANCAMENTO_MANUAL,
 COTA_ID,
 OBSERVACAO, 
 valor)

(select 
	true,
    data_real,
    'CARGA',
    'APROVADO',
    data_real,
    data_real,
    null,
    23,
    1,
    true,
    cota_id,
	concat('CARGA INICIAL - ', desc_debito),
    valor_decimal

    from mov_deb);

INSERT INTO movimento_financeiro_cota
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
 LANCAMENTO_MANUAL,
 COTA_ID,
 OBSERVACAO,
 VALOR)

(select 
	true,
    data_real,
    'CARGA',
    'APROVADO',
    data_real,
    data_real,
    null,
    22,
    1,
    true,
    cota_id,
	concat('CARGA INICIAL - ', desc_credito),
    valor_decimal

    from mov_cred);
