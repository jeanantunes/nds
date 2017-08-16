#TODO:Identar tabelas,colunas UPPERCASE
create table if not exists acumulo_divida
(
  ID bigint auto_increment
    primary key,
  DIVIDA_ID bigint not null,
  MOV_PENDENTE_ID bigint not null,
  MOV_JUROS_ID bigint null,
  MOV_MULTA_ID bigint null,
  NUMERO_ACUMULO int default '1' null,
  DATA_CRIACAO date not null,
  STATUS varchar(255) default 'ATIVA' not null,
  USUARIO_ID bigint not null,
  COTA_ID bigint not null
)
  #TODO:Inserir essas linhas apos cada create table!!
#ENGINE = InnoDB
#DEFAULT CHARACTER SET = utf8;
;

create index COTA_ID
  on acumulo_divida (COTA_ID)
;

create index DIVIDA_ID
  on acumulo_divida (DIVIDA_ID)
;

create index MOV_JUROS_ID
  on acumulo_divida (MOV_JUROS_ID)
;

create index MOV_MULTA_ID
  on acumulo_divida (MOV_MULTA_ID)
;

create index MOV_PENDENTE_ID
  on acumulo_divida (MOV_PENDENTE_ID)
;

create index USUARIO_ID
  on acumulo_divida (USUARIO_ID)
;

create table if not exists ajuste_reparte
(
  ID bigint auto_increment
    primary key,
  AJUSTE_APLICADO decimal(18,4) null,
  DATA_ALTERACAO datetime null,
  DATA_FIM datetime null,
  DATA_INICIO datetime null,
  FORMA_AJUSTE varchar(255) null,
  MOTIVO varchar(255) null,
  COTA_ID bigint null,
  USUARIO_ID bigint not null,
  TIPO_SEGMENTO_AJUSTE_ID bigint null
)
;

create index FK367020587FFF790E
  on ajuste_reparte (USUARIO_ID)
;

create index FK36702058BA1ECD8
  on ajuste_reparte (TIPO_SEGMENTO_AJUSTE_ID)
;

create index FK36702058C8181F74
  on ajuste_reparte (COTA_ID)
;

create table if not exists algoritmo
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(255) null
)
;

create table if not exists area_influencia_pdv
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  DESCRICAO varchar(255) not null
)
;

create table if not exists assoc_veiculo_motorista_rota
(
  ID bigint auto_increment
    primary key,
  MOTORISTA bigint null,
  ROTA bigint null,
  TRANSPORTADOR_ID bigint null,
  VEICULO bigint null
)
;

create index FK45A0E4B78F516B2E
  on assoc_veiculo_motorista_rota (ROTA)
;

create index FK45A0E4B797A7E184
  on assoc_veiculo_motorista_rota (VEICULO)
;

create index FK45A0E4B7B2639DFE
  on assoc_veiculo_motorista_rota (MOTORISTA)
;

create index FK45A0E4B7D90B1440
  on assoc_veiculo_motorista_rota (TRANSPORTADOR_ID)
;

create table if not exists atualizacao_estoque_gfs
(
  id bigint auto_increment
    primary key,
  DATA_ATUALIZACAO date not null,
  DIFERENCA_ID bigint not null,
  MOVIMENTO_ESTOQUE_ID bigint not null,
  constraint DIFERENCA_ID
  unique (DIFERENCA_ID),
  constraint MOVIMENTO_ESTOQUE_ID
  unique (MOVIMENTO_ESTOQUE_ID)
)
;

create index FK66A4BF1C232FFB12
  on atualizacao_estoque_gfs (MOVIMENTO_ESTOQUE_ID)
;

create index FK66A4BF1C5A0FFAA9
  on atualizacao_estoque_gfs (DIFERENCA_ID)
;

create table if not exists baixa_cobranca
(
  TIPO_BAIXA varchar(31) not null,
  ID bigint auto_increment
    primary key,
  DATA_BAIXA date not null,
  DATA_PAGAMENTO date not null,
  STATUS varchar(255) null,
  VALOR_PAGO decimal(18,4) null,
  NOME_ARQUIVO varchar(255) null,
  NOSSO_NUMERO varchar(255) null,
  NUM_REGISTRO_ARQUIVO int null,
  OBSERVACAO varchar(255) null,
  STATUS_APROVACAO varchar(255) null,
  VALOR_DESCONTO decimal(18,4) null,
  VALOR_JUROS decimal(18,4) null,
  VALOR_MULTA decimal(18,4) null,
  BANCO_ID bigint null,
  COBRANCA_ID bigint null,
  USUARIO_ID bigint null
)
;

create index baixa_cobrancadata_idx
  on baixa_cobranca (DATA_BAIXA, DATA_PAGAMENTO)
;

create index FK2F0047737FFF790E
  on baixa_cobranca (USUARIO_ID)
;

create index FK2F004773D89C75C1
  on baixa_cobranca (COBRANCA_ID)
;

create index FK2F004773E44516C0
  on baixa_cobranca (BANCO_ID)
;

create table if not exists banco
(
  ID bigint auto_increment
    primary key,
  AGENCIA bigint not null,
  APELIDO varchar(255) not null,
  ATIVO tinyint(1) not null,
  CARTEIRA int null,
  PESSOA_JURIDICA_CEDENTE_ID bigint default '5' not null,
  CODIGO_CEDENTE varchar(255) not null,
  DIGITO_CODIGO_CEDENTE varchar(3) null,
  CONTA bigint not null,
  DV_AGENCIA varchar(255) null,
  DV_CONTA varchar(255) null,
  INSTRUCOES_1 varchar(255) null,
  INSTRUCOES_2 varchar(255) null,
  INSTRUCOES_3 varchar(255) null,
  INSTRUCOES_4 varchar(255) null,
  JUROS decimal(18,4) null,
  MULTA decimal(18,4) null,
  NOME varchar(255) not null,
  NUMERO_BANCO varchar(255) not null,
  VR_MULTA decimal(18,4) null,
  EXIBIR_VALOR_MONETARIO tinyint(1) default '0' null,
  CODIGO_EMPRESA varchar(30) null,
  SEQUENCIAL_ARQUIVO_COBRANCA int null
)
;

create index NDX_NOME
  on banco (NOME)
;

alter table baixa_cobranca
  add constraint FK2F004773E44516C0
foreign key (BANCO_ID) references banco (ID)
;

create table if not exists base_referencia_cota
(
  ID bigint auto_increment
    primary key,
  FINAL_PERIODO date null,
  INICIO_PERIODO date null,
  COTA_ID bigint not null,
  constraint COTA_ID
  unique (COTA_ID)
)
;

create index FK81B8E398C8181F74
  on base_referencia_cota (COTA_ID)
;

create table if not exists boleto_antecipado
(
  ID bigint auto_increment
    primary key,
  DATA date not null,
  DATA_VENCIMENTO date not null,
  DATA_PAGAMENTO date null,
  DATA_RECOLHIMENTO_CE_DE date not null,
  DATA_RECOLHIMENTO_CE_ATE date not null,
  NOSSO_NUMERO varchar(255) not null,
  DIGITO_NOSSO_NUMERO varchar(255) null,
  VALOR_DEBITO decimal(18,4) not null,
  VALOR_CREDITO decimal(18,4) not null,
  VALOR_REPARTE decimal(18,4) not null,
  VALOR_ENCALHE decimal(18,4) not null,
  VALOR_CE decimal(18,4) not null,
  VALOR decimal(18,4) not null,
  VALOR_PAGO decimal(18,4) null,
  USUARIO_ID bigint not null,
  CHAMADA_ENCALHE_COTA_ID bigint not null,
  FORNECEDOR_ID bigint null,
  BANCO_ID bigint null,
  VIAS int null,
  STATUS varchar(255) not null,
  TIPO_COBRANCA varchar(255) not null,
  MOVIMENTO_FINANCEIRO_COTA_ID bigint null,
  BOLETO_ANTECIPADO_ID bigint null,
  VALOR_DESCONTO decimal(18,4) null,
  TIPO_BAIXA varchar(31) null,
  VALOR_JUROS decimal(18,4) null,
  VALOR_MULTA decimal(18,4) null
)
;

create table if not exists boleto_distribuidor
(
  ID bigint auto_increment
    primary key,
  DATA_EMISSAO datetime not null,
  DATA_VENCIMENTO datetime not null,
  DIGITO_NOSSO_NUMERO_DISTRIBUIDOR varchar(255) null,
  NOSSO_NUMERO_DISTRIBUIDOR varchar(255) not null,
  STATUS varchar(255) null,
  TIPO_COBRANCA varchar(255) not null,
  VALOR decimal(18,4) null,
  VIAS int null,
  BANCO_ID bigint null,
  CHAMADA_ENCALHE_FORNECEDOR_ID bigint not null,
  FORNECEDOR_ID bigint null,
  constraint CHAMADA_ENCALHE_FORNECEDOR_ID
  unique (CHAMADA_ENCALHE_FORNECEDOR_ID),
  constraint FK4BA253ACE44516C0
  foreign key (BANCO_ID) references banco (ID)
)
;

create index FK4BA253AC9808F874
  on boleto_distribuidor (FORNECEDOR_ID)
;

create index FK4BA253ACE26473F4
  on boleto_distribuidor (CHAMADA_ENCALHE_FORNECEDOR_ID)
;

create index FK4BA253ACE44516C0
  on boleto_distribuidor (BANCO_ID)
;

create table if not exists boleto_email
(
  ID bigint auto_increment
    primary key,
  COBRANCA_ID bigint not null,
  constraint COBRANCA_ID
  unique (COBRANCA_ID)
)
;

create table if not exists box
(
  ID bigint auto_increment
    primary key,
  CODIGO int not null,
  NOME varchar(255) null,
  TIPO_BOX varchar(255) not null,
  constraint CODIGO
  unique (CODIGO),
  constraint NOME_UNIQUE
  unique (NOME)
)
;

create table if not exists box_usuario
(
  BOX_ID bigint not null,
  usuarios_ID bigint not null,
  constraint usuarios_ID
  unique (usuarios_ID),
  constraint FK6C964C7ABA6EBE40
  foreign key (BOX_ID) references box (ID)
)
;

create index FK6C964C7A232855D7
  on box_usuario (usuarios_ID)
;

create index FK6C964C7ABA6EBE40
  on box_usuario (BOX_ID)
;

create table if not exists brinde
(
  ID bigint auto_increment
    primary key,
  CODIGO int null,
  DESCRICAO_BRINDE varchar(255) null,
  VENDE_BRINDE_SEPARADO tinyint(1) null
)
;

create table if not exists caucao_liquida
(
  ID bigint auto_increment
    primary key,
  DATA_ATUALIZACAO datetime null,
  VALOR double null,
  COTA_GARANTIA_CAUCAO_LIQUIDA_ID bigint null
)
;

create index FKBC0347E0A3B7AA06
  on caucao_liquida (COTA_GARANTIA_CAUCAO_LIQUIDA_ID)
;

create table if not exists certificado
(
  id bigint(11) auto_increment
    primary key,
  DISTRIBUIDOR_ID bigint(11) not null,
  SENHA varchar(60) not null,
  NOME_ARQUIVO varchar(100) not null,
  EXTENSAO varchar(4) not null,
  ALIAS varchar(100) not null,
  DATA_CRIACAO timestamp default CURRENT_TIMESTAMP not null,
  DATA_INICIO date not null,
  DATA_FIM date not null,
  USUARIO_ID bigint not null,
  INSERT_TIME timestamp default '0000-00-00 00:00:00' not null,
  LAST_UPDATE_TIME timestamp default '0000-00-00 00:00:00' not null
)
;

create index FK_certificado
  on certificado (DISTRIBUIDOR_ID)
;

create trigger certificado_BINS
before INSERT on certificado
for each row
  BEGIN
    SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP;
  END;

create trigger certificado_BUPD
before UPDATE on certificado
for each row
  BEGIN
    SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP;
  END;

create table if not exists cfop
(
  ID bigint auto_increment
    primary key,
  CODIGO varchar(255) not null,
  DESCRICAO varchar(255) not null,
  constraint CODIGO
  unique (CODIGO)
)
;

create table if not exists chamada_encalhe
(
  ID bigint auto_increment
    primary key,
  DATA_RECOLHIMENTO date not null,
  TIPO_CHAMADA_ENCALHE varchar(255) not null,
  PRODUTO_EDICAO_ID bigint not null,
  SEQUENCIA int null
)
;

create index FK98FAFF70A53173D3
  on chamada_encalhe (PRODUTO_EDICAO_ID, DATA_RECOLHIMENTO)
;

create index NDX_CHAMADA_ENCALHE
  on chamada_encalhe (DATA_RECOLHIMENTO, PRODUTO_EDICAO_ID)
;

create table if not exists chamada_encalhe_aux
(
  id bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  DATA_RECOLHIMENTO date not null
)
;

create index data_recolhimento
  on chamada_encalhe_aux (DATA_RECOLHIMENTO)
;

create index produto_edicao_id
  on chamada_encalhe_aux (PRODUTO_EDICAO_ID)
;

create table if not exists chamada_encalhe_cota
(
  ID bigint auto_increment
    primary key,
  FECHADO tinyint(1) not null,
  POSTERGADO tinyint(1) not null,
  QTDE_PREVISTA decimal(18,4) null,
  PROCESSO_UTILIZA_NFE tinyint(1) default '0' not null,
  CHAMADA_ENCALHE_ID bigint not null,
  COTA_ID bigint not null,
  CHAMADA_ENCALHE_COTA_POSTERGADA_ID bigint null,
  USUARIO_ID bigint null,
  constraint FKDB858748E720C6B4
  foreign key (CHAMADA_ENCALHE_ID) references chamada_encalhe (ID)
)
;

create index cota_chamada_encalhe
  on chamada_encalhe_cota (COTA_ID, CHAMADA_ENCALHE_ID)
;

create index FKDB858748C8181F74
  on chamada_encalhe_cota (COTA_ID)
;

create index FKDB858748E720C6B4
  on chamada_encalhe_cota (CHAMADA_ENCALHE_ID, COTA_ID)
;

create index NDX_POSTERGADO
  on chamada_encalhe_cota (POSTERGADO)
;

create index NDX_QTDE_PREVISTA
  on chamada_encalhe_cota (QTDE_PREVISTA)
;

create index USUARIO_ID
  on chamada_encalhe_cota (USUARIO_ID)
;

create trigger before_insert_cec
before INSERT on chamada_encalhe_cota
for each row
  BEGIN
    DECLARE qtde_enviada INT;
    DECLARE numero_cota VARCHAR(10);
    DECLARE codigo varchar(10);
    declare numero_edicao varchar (10);

    select sum(qtde),c.numero_cota,p.codigo,pe.numero_edicao into qtde_enviada , numero_cota,codigo,numero_edicao
    from movimento_estoque_cota mec1
      inner join tipo_movimento tp on tp.ID = mec1.tipo_movimento_id
      inner join chamada_encalhe ce on mec1.produto_edicao_id = ce.produto_edicao_id
      inner join produto_edicao pe on pe.id = ce.produto_edicao_id
      inner join produto p on p.id = pe.produto_id
      inner join cota c on c.id = mec1.cota_id
    where tp.operacao_estoque = 'ENTRADA' and mec1.cota_id=NEW.cota_id
          and ce.id = NEW.chamada_encalhe_id;

    IF( NEW.qtde_prevista > qtde_enviada) THEN
      set @message_text = concat('ERRO. INSERINDO QUANTIDADE PREVISTA ACIMA DO ENVIADO PARA  COTA=',numero_cota,' PRODUTO=',codigo,' ED=',numero_edicao,' QTDE ENVIADO=',qtde_enviada,' QTDE PREVISTA=',NEW.qtde_prevista,' CONSULTE O SAD ');
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = @message_text;
    END IF;
  END;

create trigger before_update_cec
before UPDATE on chamada_encalhe_cota
for each row
  BEGIN
    DECLARE qtde_enviada INT;
    DECLARE numero_cota VARCHAR(10);
    DECLARE codigo varchar(10);
    declare numero_edicao varchar (10);

    select sum(qtde),c.numero_cota,p.codigo,pe.numero_edicao into qtde_enviada , numero_cota,codigo,numero_edicao
    from movimento_estoque_cota mec1
      inner join tipo_movimento tp on tp.ID = mec1.tipo_movimento_id
      inner join chamada_encalhe ce on mec1.produto_edicao_id = ce.produto_edicao_id
      inner join produto_edicao pe on pe.id = ce.produto_edicao_id
      inner join produto p on p.id = pe.produto_id
      inner join cota c on c.id = mec1.cota_id
    where tp.operacao_estoque = 'ENTRADA' and mec1.cota_id=NEW.cota_id
          and ce.id = NEW.chamada_encalhe_id;

    IF( NEW.qtde_prevista > qtde_enviada) THEN
      set @message_text = concat('ERRO. ATUALIZANDO QUANTIDADE PREVISTA ACIMA DO ENVIADO PARA  COTA=',numero_cota,' PRODUTO=',codigo,' ED=',numero_edicao,' QTDE ENVIADO=',qtde_enviada,' QTDE PREVISTA=',NEW.qtde_prevista,' CONSULTE O SAD ');
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = @message_text;
    END IF;
  END;

create table if not exists chamada_encalhe_cota_aux
(
  id bigint not null,
  chamada_encalhe_id bigint not null,
  COTA_ID int not null
)
;

create index chamada_encalhe_id
  on chamada_encalhe_cota_aux (chamada_encalhe_id)
;

create index cota_id
  on chamada_encalhe_cota_aux (COTA_ID)
;

create table if not exists chamada_encalhe_fornecedor
(
  ID bigint auto_increment
    primary key,
  ANO_REFERENCIA int null,
  CODIGO_DISTRIBUIDOR bigint null,
  CODIGO_PREENCHIMENTO varchar(255) not null,
  CONTROLE bigint null,
  DATA_EMISSAO date not null,
  DATA_LIMITE_RECOLHIMENTO date null,
  NOTA_VALORES_DIVERSOS decimal(18,4) null,
  NUM_CHAMADA_ENCALHE bigint not null,
  NUMERO_SEMANA int not null,
  STATUS varchar(255) not null,
  TIPO_CHAMADA_ENCALHE int not null,
  TOTAL_CREDITO_APURADO decimal(18,4) null,
  TOTAL_CREDITO_INFORMADO decimal(18,4) null,
  TOTAL_MARGEM_APURADO decimal(18,4) null,
  TOTAL_MARGEM_INFORMADO decimal(18,4) null,
  TOTAL_VENDA_APURADA decimal(18,4) null,
  TOTAL_VENDA_INFORMADA decimal(18,4) null,
  CFOP_ID bigint null,
  FORNECEDOR_ID bigint null,
  DATA_VENCIMENTO date not null,
  STATUS_CE_NDS varchar(255) null,
  STATUS_INTEGRACAO varchar(255) default 'NAO_INTEGRADO' null,
  DATA_FECHAMENTO_NDS date null,
  STATUS_INTEGRACAO_NFE varchar(255) default 'NAO_APROVADO' null,
  constraint NUM_CHAMADA_ENCALHE
  unique (NUM_CHAMADA_ENCALHE),
  constraint FKE41EDD947AB6324F
  foreign key (CFOP_ID) references cfop (ID)
)
;

create index FKE41EDD947AB6324F
  on chamada_encalhe_fornecedor (CFOP_ID)
;

create index FKE41EDD949808F874
  on chamada_encalhe_fornecedor (FORNECEDOR_ID)
;

alter table boleto_distribuidor
  add constraint FK4BA253ACE26473F4
foreign key (CHAMADA_ENCALHE_FORNECEDOR_ID) references chamada_encalhe_fornecedor (ID)
;

create table if not exists chamada_encalhe_lancamento
(
  CHAMADA_ENCALHE_ID bigint not null,
  LANCAMENTO_ID bigint not null,
  primary key (CHAMADA_ENCALHE_ID, LANCAMENTO_ID),
  constraint FK9669E1A9E720C6B4
  foreign key (CHAMADA_ENCALHE_ID) references chamada_encalhe (ID)
)
;

create index FK9669E1A945C07ACF
  on chamada_encalhe_lancamento (LANCAMENTO_ID)
;

create index FK9669E1A9E720C6B4
  on chamada_encalhe_lancamento (CHAMADA_ENCALHE_ID)
;

create table if not exists cheque
(
  ID bigint auto_increment
    primary key,
  AGENCIA bigint not null,
  CONTA bigint not null,
  CORRENTISTA varchar(255) not null,
  DV_AGENCIA varchar(255) not null,
  DV_CONTA varchar(255) not null,
  DATA_EMISSAO datetime not null,
  NOME_BANCO varchar(255) not null,
  NUMERO_BANCO varchar(255) not null,
  NUMERO_CHEQUE varchar(255) not null,
  DATA_VALIDADE datetime not null,
  VALOR decimal(18,4) null
)
;

create table if not exists cheque_imagem
(
  ID bigint not null
    primary key,
  IMAGE longblob not null
)
;

create table if not exists classificacao_nao_recebida
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  COTA_ID bigint not null,
  TIPO_CLASSIFICACAO_PRODUTO_ID bigint not null,
  USUARIO_ID bigint not null
)
;

create index FK7F7429DC10C84C95
  on classificacao_nao_recebida (TIPO_CLASSIFICACAO_PRODUTO_ID)
;

create index FK7F7429DC7FFF790E
  on classificacao_nao_recebida (USUARIO_ID)
;

create index FK7F7429DCC8181F74
  on classificacao_nao_recebida (COTA_ID)
;

create table if not exists cobranca
(
  TIPO_DOCUMENTO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  CONTEMPLACAO tinyint(1) null,
  DT_EMISSAO date not null,
  DT_PAGAMENTO date null,
  DT_VENCIMENTO date not null,
  ENCARGOS decimal(18,4) null,
  NOSSO_NUMERO varchar(255) not null,
  STATUS_COBRANCA varchar(255) not null,
  TIPO_BAIXA varchar(255) null,
  TIPO_COBRANCA varchar(255) not null,
  VALOR decimal(18,4) null,
  VIAS int null,
  DIGITO_NOSSO_NUMERO varchar(255) null,
  NOSSO_NUMERO_COMPLETO varchar(255) null,
  BANCO_ID bigint null,
  COTA_ID bigint null,
  DIVIDA_ID bigint not null,
  FORNECEDOR_ID bigint null,
  ENVIAR_POR_EMAIL tinyint null,
  COBRANCA_CENTRALIZACAO_ID bigint null,
  ORIUNDA_NEGOCIACAO_AVULSA tinyint(1) default '0' null,
  NOSSO_NUMERO_CONSOLIDADO varchar(255) null,
  OBSERVACAO varchar(45) null,
  COBRANCA_NFE tinyint(1) default '0' null,
  COBRANCA_REGISTRADA_GERADA tinyint(1) default '0' null,
  constraint NOSSO_NUMERO
  unique (NOSSO_NUMERO),
  constraint NOSSO_NUMERO_COMPLETO
  unique (NOSSO_NUMERO_COMPLETO),
  constraint DIVIDA_ID
  unique (DIVIDA_ID),
  constraint FKF83D2107E44516C0
  foreign key (BANCO_ID) references banco (ID)
)
;

create index DATA_PAGTO_STATUS_NDX
  on cobranca (DT_PAGAMENTO, STATUS_COBRANCA)
;

create index DATA_VENCIMENTO_IDX
  on cobranca (DT_VENCIMENTO)
;

create index EMISSAO_COTA
  on cobranca (DT_EMISSAO, COTA_ID)
;

create index FKF83D21077C42C4C1
  on cobranca (DIVIDA_ID)
;

create index FKF83D21079808F874
  on cobranca (FORNECEDOR_ID)
;

create index FKF83D2107C8181F74
  on cobranca (COTA_ID)
;

create index FKF83D2107E44516C0
  on cobranca (BANCO_ID)
;

alter table baixa_cobranca
  add constraint FK2F004773D89C75C1
foreign key (COBRANCA_ID) references cobranca (ID)
;

alter table boleto_email
  add constraint FKF83D21077C42C4C2
foreign key (COBRANCA_ID) references cobranca (ID)
;

create table if not exists cobranca_centralizacao
(
  ID bigint auto_increment
    primary key,
  NOSSO_NUMERO varchar(255) not null,
  DIGITO_NOSSO_NUMERO varchar(255) null,
  NOSSO_NUMERO_COMPLETO varchar(255) null,
  DT_EMISSAO date not null,
  DT_VENCIMENTO date not null,
  DT_PAGAMENTO date null,
  ENCARGOS decimal(18,4) null,
  VALOR decimal(18,4) null,
  TIPO_BAIXA varchar(255) null,
  TIPO_COBRANCA varchar(255) not null,
  STATUS_COBRANCA varchar(255) not null,
  VIAS int null,
  CONTEMPLACAO tinyint(1) null,
  ENVIAR_POR_EMAIL tinyint null,
  BANCO_ID bigint null,
  COTA_ID bigint null,
  FORNECEDOR_ID bigint null,
  constraint NOSSO_NUMERO
  unique (NOSSO_NUMERO),
  constraint NOSSO_NUMERO_COMPLETO
  unique (NOSSO_NUMERO_COMPLETO),
  constraint FKF83D2107E44516C7
  foreign key (BANCO_ID) references banco (ID)
)
;

create index FKF83D21079808F877
  on cobranca_centralizacao (FORNECEDOR_ID)
;

create index FKF83D2107C8181F77
  on cobranca_centralizacao (COTA_ID)
;

create index FKF83D2107E44516C7
  on cobranca_centralizacao (BANCO_ID)
;

create table if not exists cobranca_controle_conferencia_encalhe_cota
(
  ID bigint auto_increment
    primary key,
  COBRANCA_ID bigint null,
  CONTROLE_CONF_ENCALHE_COTA_ID bigint null,
  constraint FKA71C78B3D89C75C1
  foreign key (COBRANCA_ID) references cobranca (ID)
)
;

create index FKA71C78B326E8AFB2
  on cobranca_controle_conferencia_encalhe_cota (CONTROLE_CONF_ENCALHE_COTA_ID)
;

create index FKA71C78B3D89C75C1
  on cobranca_controle_conferencia_encalhe_cota (COBRANCA_ID)
;

create table if not exists comprador
(
  cp1 int null,
  cp2 int null,
  cp3 int null
)
;

create table if not exists concentracao_cobranca_caucao_liquida
(
  ID bigint auto_increment
    primary key,
  DIA_SEMANA int not null,
  FORMA_COBRANCA_CAUCAO_LIQUIDA_ID bigint null
)
;

create index FK20DE4A31EA41BD85
  on concentracao_cobranca_caucao_liquida (FORMA_COBRANCA_CAUCAO_LIQUIDA_ID)
;

create table if not exists concentracao_cobranca_cota
(
  ID bigint auto_increment
    primary key,
  DIA_SEMANA int not null,
  FORMA_COBRANCA_ID bigint null
)
;

create index FK53392B6AE34F875B
  on concentracao_cobranca_cota (FORMA_COBRANCA_ID)
;

create table if not exists conferencia_enc_parcial
(
  id bigint auto_increment
    primary key,
  DATA_APROVACAO datetime null,
  DATA_CONF_ENC_PARCIAL datetime not null,
  DATA_MOVIMENTO date not null,
  DIFERENCA_APURADA tinyint(1) null,
  NF_PARCIAL_GERADA tinyint(1) null,
  QTDE decimal(18,4) null,
  STATUS_APROVACAO varchar(255) not null,
  produtoEdicao_ID bigint null,
  responsavel_ID bigint null
)
;

create index FK35D69C6F5F18CCBC
  on conferencia_enc_parcial (responsavel_ID)
;

create index FK35D69C6FB7E346C0
  on conferencia_enc_parcial (produtoEdicao_ID)
;

create table if not exists conferencia_encalhe
(
  id bigint auto_increment
    primary key,
  DATA date not null,
  JURAMENTADA tinyint(1) null,
  OBSERVACAO varchar(255) null,
  PRECO_CAPA_INFORMADO decimal(18,4) null,
  QTDE decimal(18,4) null,
  QTDE_INFORMADA decimal(18,4) null,
  CHAMADA_ENCALHE_COTA_ID bigint null,
  CONTROLE_CONFERENCIA_ENCALHE_COTA_ID bigint not null,
  MOVIMENTO_ESTOQUE_ID bigint null,
  MOVIMENTO_ESTOQUE_COTA_ID bigint null,
  PRODUTO_EDICAO_ID bigint not null,
  DIA_RECOLHIMENTO int(10) null,
  PRECO_COM_DESCONTO decimal(18,4) null,
  constraint FK8E92BB04158AFF55
  foreign key (CHAMADA_ENCALHE_COTA_ID) references chamada_encalhe_cota (ID)
)
;

create index FK8E92BB04158AFF55
  on conferencia_encalhe (CHAMADA_ENCALHE_COTA_ID)
;

create index FK8E92BB04232FFB12
  on conferencia_encalhe (MOVIMENTO_ESTOQUE_ID)
;

create index FK8E92BB04A53173D3
  on conferencia_encalhe (PRODUTO_EDICAO_ID)
;

create index FK8E92BB04BBE20E9D
  on conferencia_encalhe (MOVIMENTO_ESTOQUE_COTA_ID)
;

create index FK8E92BB04D3D96F81
  on conferencia_encalhe (CONTROLE_CONFERENCIA_ENCALHE_COTA_ID, PRODUTO_EDICAO_ID)
;

create index NDX_DATA
  on conferencia_encalhe (DATA)
;

create table if not exists conferencia_encalhe_backup
(
  id bigint auto_increment
    primary key,
  ID_CONFERENCIA_ORIGINAL bigint null,
  DATA_OPERACAO date not null,
  CHAMADA_ENCALHE_COTA_ID bigint null,
  DATA_CRIACAO date not null,
  DIA_RECOLHIMENTO int(10) null,
  JURAMENTADA tinyint(1) null,
  OBSERVACAO varchar(255) null,
  COTA_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  QTDE decimal(18,4) null,
  QTDE_INFORMADA decimal(18,4) null,
  PRECO_CAPA_INFORMADO decimal(18,4) null,
  PRECO_CAPA decimal(18,4) null,
  PRECO_COM_DESCONTO decimal(18,4) null,
  PROCESSO_UTILIZA_NFE tinyint(1) default '0' not null,
  constraint FK_CONFBACKUP_CHAMADA_ENCALHE_COTA
  foreign key (CHAMADA_ENCALHE_COTA_ID) references chamada_encalhe_cota (ID)
)
;

create index FK_CONFBACKUP_CHAMADA_ENCALHE_COTA
  on conferencia_encalhe_backup (CHAMADA_ENCALHE_COTA_ID)
;

create index FK_CONFBACKUP_COTA_ID
  on conferencia_encalhe_backup (COTA_ID)
;

create index FK_CONFBACKUP_PRODUTO_EDICAO_ID
  on conferencia_encalhe_backup (PRODUTO_EDICAO_ID)
;

create table if not exists conferencia_encalhe_cota_usuario
(
  NUMERO_COTA int not null
    primary key,
  LOGIN varchar(45) not null,
  NOME_USUARIO varchar(45) null,
  SESSION_ID varchar(45) not null,
  constraint LOGIN_UNIQUE
  unique (LOGIN),
  constraint SESSION_ID_UNIQUE
  unique (SESSION_ID)
)
;

create table if not exists consignado
(
  cod_produto varchar(8) null,
  num_edicao int null,
  DATA_LANCAMENTO_STR varchar(10) null,
  PRECO float null,
  QTDE_REPARTE_HVCT float null,
  F2 float null,
  F3 float null
)
;

create table if not exists consignado_concat
(
  F1 varchar(255) null,
  F2 varchar(255) null
)
;

create index F1
  on consignado_concat (F1)
;

create table if not exists consolidado_financeiro_cota
(
  ID bigint auto_increment
    primary key,
  CONSIGNADO decimal(18,4) null,
  DT_CONSOLIDADO date not null,
  DEBITO_CREDITO decimal(18,4) null,
  ENCALHE decimal(18,4) null,
  ENCARGOS decimal(18,4) null,
  PENDENTE decimal(18,4) null,
  TOTAL decimal(18,4) null,
  VALOR_POSTERGADO decimal(18,4) null,
  VENDA_ENCALHE decimal(18,4) null,
  COTA_ID bigint not null
)
;

create index FK8468B330C8181F74
  on consolidado_financeiro_cota (COTA_ID)
;

create index NDX_DT_CONSOLIDADO
  on consolidado_financeiro_cota (DT_CONSOLIDADO, COTA_ID)
;

create table if not exists consolidado_mvto_financeiro_cota
(
  CONSOLIDADO_FINANCEIRO_ID bigint not null,
  MVTO_FINANCEIRO_COTA_ID bigint not null,
  constraint MVTO_FINANCEIRO_COTA_ID
  unique (MVTO_FINANCEIRO_COTA_ID),
  constraint FK6303D6CD68B7C893
  foreign key (CONSOLIDADO_FINANCEIRO_ID) references consolidado_financeiro_cota (ID)
)
;

create index FK6303D6CD68B7C893
  on consolidado_mvto_financeiro_cota (CONSOLIDADO_FINANCEIRO_ID)
;

create index FK6303D6CDE6E795C3
  on consolidado_mvto_financeiro_cota (MVTO_FINANCEIRO_COTA_ID)
;

create table if not exists contrato
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  AVISO_PREVIO_RESCISAO int null,
  DATA_INICIO datetime null,
  DATA_TERMINO datetime null,
  PRAZO int null,
  EXIGE_DOC_SUSPENSAO tinyint(1) not null,
  RECEBIDO tinyint(1) null,
  COTA_ID bigint not null,
  constraint COTA_ID
  unique (COTA_ID)
)
;

create index FKCDB031CC8181F74
  on contrato (COTA_ID)
;

create table if not exists controle_baixa_bancaria
(
  ID bigint auto_increment
    primary key,
  DATA_OPERACAO date not null,
  DATA_PAGAMENTO date not null,
  STATUS varchar(255) not null,
  BANCO_ID bigint not null,
  USUARIO_ID bigint not null,
  constraint DATA
  unique (DATA_PAGAMENTO, BANCO_ID),
  constraint FK5CE6C620E44516C0
  foreign key (BANCO_ID) references banco (ID)
)
;

create index FK5CE6C6207FFF790E
  on controle_baixa_bancaria (USUARIO_ID)
;

create index FK5CE6C620E44516C0
  on controle_baixa_bancaria (BANCO_ID)
;

create table if not exists controle_conferencia_encalhe
(
  ID bigint auto_increment
    primary key,
  DATA date not null,
  STATUS varchar(255) null
)
;

create index data_controle_conferencia_encalhe
  on controle_conferencia_encalhe (DATA)
;

create table if not exists controle_conferencia_encalhe_aux
(
  id bigint not null,
  DATA date not null
)
;

create index data
  on controle_conferencia_encalhe_aux (DATA)
;

create index id
  on controle_conferencia_encalhe_aux (id)
;

create table if not exists controle_conferencia_encalhe_cota
(
  ID bigint auto_increment
    primary key,
  DATA_FIM datetime not null,
  DATA_INICIO datetime not null,
  DATA_OPERACAO date not null,
  NOSSO_NUMERO varchar(255) null,
  NUMERO_SLIP bigint null,
  STATUS varchar(255) null,
  BOX_ID bigint not null,
  CTRL_CONF_ENCALHE_ID bigint not null,
  COTA_ID bigint not null,
  USUARIO_ID bigint not null,
  PROCESSO_UTILIZA_NFE tinyint(1) default '0' not null,
  NFE_DIGITADA tinyint(1) default '0' not null,
  constraint FK8EB091EBBA6EBE40
  foreign key (BOX_ID) references box (ID),
  constraint FK8EB091EB9BF01A38
  foreign key (CTRL_CONF_ENCALHE_ID) references controle_conferencia_encalhe (ID)
)
;

create index FK8EB091EB7FFF790E
  on controle_conferencia_encalhe_cota (USUARIO_ID)
;

create index FK8EB091EB9BF01A38
  on controle_conferencia_encalhe_cota (CTRL_CONF_ENCALHE_ID)
;

create index FK8EB091EBBA6EBE40
  on controle_conferencia_encalhe_cota (BOX_ID)
;

create index FK8EB091EBC8181F74
  on controle_conferencia_encalhe_cota (COTA_ID)
;

create index NDX_CCEC_DATA_OPERACAO
  on controle_conferencia_encalhe_cota (DATA_OPERACAO, COTA_ID)
;

create trigger before_insert_id
before INSERT on controle_conferencia_encalhe_cota
for each row
  BEGIN
    DECLARE C INT;
    select count(*) into c
    From controle_conferencia_encalhe_cota  A
    where (NEW.cota_id = A.cota_id and NEW.data_operacao = A.data_operacao);

    IF(C > 0) THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Ja existe conferencia em andamento ' ;
    END IF;

  END;

alter table cobranca_controle_conferencia_encalhe_cota
  add constraint FKA71C78B326E8AFB2
foreign key (CONTROLE_CONF_ENCALHE_COTA_ID) references controle_conferencia_encalhe_cota (ID)
;

alter table conferencia_encalhe
  add constraint FK8E92BB04D3D96F81
foreign key (CONTROLE_CONFERENCIA_ENCALHE_COTA_ID) references controle_conferencia_encalhe_cota (ID)
;

create table if not exists controle_conferencia_encalhe_cota_aux
(
  id bigint not null,
  ctrl_conf_encalhe_id bigint not null,
  data_operacao date not null,
  COTA_ID int not null
)
;

create index cota_id3
  on controle_conferencia_encalhe_cota_aux (COTA_ID)
;

create index ctrl_conf_encalhe_id
  on controle_conferencia_encalhe_cota_aux (ctrl_conf_encalhe_id)
;

create index data_operacao
  on controle_conferencia_encalhe_cota_aux (data_operacao)
;

create table if not exists controle_contagem_devolucao
(
  ID bigint auto_increment
    primary key,
  DATA date not null,
  STATUS varchar(255) null,
  PRODUTO_EDICAO_ID bigint not null,
  constraint DATA
  unique (DATA, PRODUTO_EDICAO_ID)
)
;

create index FK90E56DC6A53173D3
  on controle_contagem_devolucao (PRODUTO_EDICAO_ID)
;

create table if not exists controle_cota
(
  ID bigint auto_increment
    primary key,
  NUMERO_COTA_MASTER int not null,
  NUMERO_COTA int not null,
  SITUACAO varchar(1) null,
  constraint NUMERO_COTA
  unique (NUMERO_COTA)
)
;

create table if not exists controle_fechamento_encalhe
(
  DATA_ENCALHE date not null
    primary key,
  USUARIO_ID bigint null
)
;

create index fk_controle_fechamento_encalhe_usuario_id
  on controle_fechamento_encalhe (USUARIO_ID)
;

create table if not exists controle_geracao_divida
(
  id bigint auto_increment
    primary key,
  DATA date null,
  STATUS varchar(255) not null,
  USUARIO_ID bigint null
)
;

create index FK56402A877FFF790E
  on controle_geracao_divida (USUARIO_ID)
;

create table if not exists controle_numeracao_nf
(
  ID bigint auto_increment
    primary key,
  PROXIMO_NUMERO_NF bigint not null,
  SERIE_NF varchar(255) not null,
  constraint SERIE_NF
  unique (SERIE_NF)
)
;

create table if not exists controle_numeracao_slip
(
  ID bigint auto_increment
    primary key,
  PROXIMO_NUMERO_SLIP bigint not null,
  TIPO_SLIP varchar(255) not null,
  constraint TIPO_SLIP
  unique (TIPO_SLIP)
)
;

create table if not exists cota
(
  ID bigint auto_increment
    primary key,
  CLASSIFICACAO_ESPECTATIVA_FATURAMENTO varchar(255) null,
  INICIO_ATIVIDADE date not null,
  INICIO_TITULARIDADE date not null,
  NUMERO_COTA int not null,
  VALOR_MINIMO_COBRANCA decimal(18,4) null,
  ASSISTENTE_COMERCIAL varchar(255) null,
  BOLETO_EMAIL tinyint(1) null,
  BOLETO_IMPRESSO tinyint(1) null,
  BOLETO_SLIP_EMAIL tinyint(1) null,
  BOLETO_SLIP_IMPRESSO tinyint(1) null,
  CHAMADA_ENCALHE_EMAIL tinyint(1) null,
  CHAMADA_ENCALHE_IMPRESSO tinyint(1) null,
  DESCRICAO_TIPO_ENTREGA varchar(255) null,
  FIM_PERIODO_CARENCIA date null,
  GERENTE_COMERCIAL varchar(255) null,
  INICIO_PERIODO_CARENCIA date null,
  NOTA_ENVIO_EMAIL tinyint(1) null,
  NOTA_ENVIO_IMPRESSO tinyint(1) null,
  OBSERVACAO varchar(255) null,
  PROCURACAO_RECEBIDA tinyint(1) null,
  QTDE_PDV int null,
  RECEBE_RECOLHE_PARCIAIS tinyint(1) null,
  RECEBE_COMPLEMENTAR tinyint(1) default '0' null,
  RECIBO_EMAIL tinyint(1) null,
  RECIBO_IMPRESSO tinyint(1) null,
  REPARTE_POR_PONTO_VENDA tinyint(1) null,
  SLIP_EMAIL tinyint(1) null,
  SLIP_IMPRESSO tinyint(1) null,
  SOLICITA_NUM_ATRASADOS tinyint(1) null,
  TERMO_ADESAO_RECEBIDO tinyint(1) null,
  UTILIZA_PROCURACAO tinyint(1) null,
  UTILIZA_TERMO_ADESAO tinyint(1) null,
  EMAIL_NF_E varchar(255) null,
  EXIGE_NF_E tinyint(1) default '0' null,
  CONTRIBUINTE_ICMS tinyint(1) default '0' null,
  POSSUI_CONTRATO tinyint(1) not null,
  SITUACAO_CADASTRO varchar(255) not null,
  SUGERE_SUSPENSAO tinyint(1) not null,
  UTILIZA_IPV tinyint(1) default '0' not null,
  SUGERE_SUSPENSAO_DISTRIBUIDOR tinyint(1) not null,
  BOX_ID bigint null,
  ID_FIADOR bigint null,
  PESSOA_ID bigint not null,
  TIPO_DISTRIBUICAO_COTA varchar(255) null,
  TIPO_COTA varchar(255) not null,
  COTA_UNIFICACAO_ID bigint null,
  VALOR_SUSPENSAO decimal(18,4) null,
  NUM_ACUMULO_DIVIDA int null,
  DEVOLVE_ENCALHE tinyint(1) null,
  CONTRATO_ID bigint null,
  PARAMETRO_COBRANCA_ID bigint null,
  PARAMETRO_COBRANCA_DISTRIBUICAO_COTA_ID bigint null,
  BASE_REFERENCIA_COTA_ID bigint null,
  COTA_GARANTIA_ID bigint null,
  NUMERO_JORNALEIRO_IPV bigint null,
  UTILIZA_DOCS_PARAMETRO_DISTRIB tinyint(1) default '1' not null,
  ENDERECO_LED varchar(45) null,
  GERAR_BOLETO_NFE tinyint(1) default '0' null,
  constraint FK1FA7D9BA6EBE40
  foreign key (BOX_ID) references box (ID),
  constraint FKcota_contrato
  foreign key (CONTRATO_ID) references contrato (ID),
  constraint FKBASE_REFERENCIA_COTA
  foreign key (BASE_REFERENCIA_COTA_ID) references base_referencia_cota (ID)
)
;

create index FK1FA7D99B8CB634
  on cota (PESSOA_ID)
;

create index FK1FA7D9BA6EBE40
  on cota (BOX_ID)
;

create index FK1FA7D9DC6BE690
  on cota (ID_FIADOR)
;

create index FKBASE_REFERENCIA_COTA_idx
  on cota (BASE_REFERENCIA_COTA_ID)
;

create index FKcota_contrato_idx
  on cota (CONTRATO_ID)
;

create index FKCOTA_GARANTIA_idx
  on cota (COTA_GARANTIA_ID)
;

create index FKPARAMETRO_COBRANCA_COTA_idx
  on cota (PARAMETRO_COBRANCA_ID)
;

create index FKPARAMETRO_COBRANCA_DISTRIBUICAO_COTA_idx
  on cota (PARAMETRO_COBRANCA_DISTRIBUICAO_COTA_ID)
;

create index NDX_NUMERO_COTA
  on cota (NUMERO_COTA)
;

alter table acumulo_divida
  add constraint acumulo_divida_ibfk_6
foreign key (COTA_ID) references cota (ID)
;

alter table ajuste_reparte
  add constraint FK36702058C8181F74
foreign key (COTA_ID) references cota (ID)
;

alter table base_referencia_cota
  add constraint FK81B8E398C8181F74
foreign key (COTA_ID) references cota (ID)
;

alter table chamada_encalhe_cota
  add constraint FKDB858748C8181F74
foreign key (COTA_ID) references cota (ID)
;

alter table classificacao_nao_recebida
  add constraint FK7F7429DCC8181F74
foreign key (COTA_ID) references cota (ID)
;

alter table cobranca
  add constraint FKF83D2107C8181F74
foreign key (COTA_ID) references cota (ID)
;

alter table cobranca_centralizacao
  add constraint FKF83D2107C8181F77
foreign key (COTA_ID) references cota (ID)
;

alter table conferencia_encalhe_backup
  add constraint FK_CONFBACKUP_COTA_ID
foreign key (COTA_ID) references cota (ID)
;

alter table consolidado_financeiro_cota
  add constraint FK8468B330C8181F74
foreign key (COTA_ID) references cota (ID)
;

alter table contrato
  add constraint FKCDB031CC8181F74
foreign key (COTA_ID) references cota (ID)
;

alter table controle_conferencia_encalhe_cota
  add constraint FK8EB091EBC8181F74
foreign key (COTA_ID) references cota (ID)
;

create table if not exists cota_ausente
(
  ID bigint auto_increment
    primary key,
  DATA date not null,
  COTA_ID bigint not null,
  destino varchar(100) null,
  constraint FK544D8993C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK544D8993C8181F74
  on cota_ausente (COTA_ID)
;

create table if not exists cota_ausente_movimento_estoque_cota
(
  COTA_AUSENTE_ID bigint not null,
  MOVIMENTO_ESTOQUE_COTA_ID bigint not null,
  constraint MOVIMENTO_ESTOQUE_COTA_ID
  unique (MOVIMENTO_ESTOQUE_COTA_ID),
  constraint FK7D6984EF70E707D7
  foreign key (COTA_AUSENTE_ID) references cota_ausente (ID)
)
;

create index FK7D6984EF70E707D7
  on cota_ausente_movimento_estoque_cota (COTA_AUSENTE_ID)
;

create index FK7D6984EFBBE20E9D
  on cota_ausente_movimento_estoque_cota (MOVIMENTO_ESTOQUE_COTA_ID)
;

create table if not exists cota_base
(
  ID bigint auto_increment
    primary key,
  DATA_FIM date null,
  DATA_INICIO date null,
  INDICE_AJUSTE decimal(18,4) null,
  COTA_ID bigint null,
  constraint FKAC360F77C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FKAC360F77C8181F74
  on cota_base (COTA_ID)
;

create table if not exists cota_base_cota
(
  ID bigint auto_increment
    primary key,
  ATIVO tinyint(1) not null,
  DT_FIM_VIGENCIA date null,
  DT_INICIO_VIGENCIA date null,
  TIPO_ALTERACAO varchar(255) not null,
  COTA_ID bigint null,
  COTA_BASE_ID bigint null,
  constraint FKEEA663A1C8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FKEEA663A132855C87
  foreign key (COTA_BASE_ID) references cota_base (ID)
)
;

create index FKEEA663A132855C87
  on cota_base_cota (COTA_BASE_ID)
;

create index FKEEA663A1C8181F74
  on cota_base_cota (COTA_ID)
;

create table if not exists cota_box
(
  cota int null,
  box int null
)
;

create table if not exists cota_fornecedor
(
  COTA_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  primary key (FORNECEDOR_ID, COTA_ID),
  constraint FK1ADE098BC8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK1ADE098B9808F874
  on cota_fornecedor (FORNECEDOR_ID)
;

create index FK1ADE098BC8181F74
  on cota_fornecedor (COTA_ID)
;

create table if not exists cota_garantia
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  DATA datetime null,
  AGENCIA_BANCO bigint null,
  CONTA_BANCO bigint null,
  DV_AGENCIA_BANCO varchar(255) null,
  DV_CONTA_BANCO varchar(255) null,
  NOME_BANCO varchar(255) null,
  CORRENTISTA varchar(255) null,
  NUMERO_BANCO varchar(255) null,
  TIPO_COBRANCA varchar(255) null,
  COTA_ID bigint not null,
  PAGAMENTO_CAUCAO_LIQUIDA_ID bigint null,
  CHEQUE_CAUCAO_ID bigint null,
  FIADOR_ID bigint null,
  NOTA_PROMISSORIA_ID bigint null,
  TIPO_GARANTIA varchar(255) null,
  constraint COTA_ID
  unique (COTA_ID),
  constraint FK80C0DF0DC8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK80C0DF0DB6D1BA3D
  foreign key (CHEQUE_CAUCAO_ID) references cheque (ID)
)
;

create index FK80C0DF0D8DC372F4
  on cota_garantia (FIADOR_ID)
;

create index FK80C0DF0DB6D1BA3D
  on cota_garantia (CHEQUE_CAUCAO_ID)
;

create index FK80C0DF0DC8181F74
  on cota_garantia (COTA_ID)
;

create index FK80C0DF0DEA490E43
  on cota_garantia (NOTA_PROMISSORIA_ID)
;

create index FK80C0DF0DFE263B9B
  on cota_garantia (PAGAMENTO_CAUCAO_LIQUIDA_ID)
;

alter table caucao_liquida
  add constraint FKBC0347E0A3B7AA06
foreign key (COTA_GARANTIA_CAUCAO_LIQUIDA_ID) references cota_garantia (ID)
;

alter table cota
  add constraint FKCOTA_GARANTIA
foreign key (COTA_GARANTIA_ID) references cota_garantia (ID)
;

create table if not exists cota_grupo
(
  GRUPO_COTA_ID bigint not null,
  COTA_ID bigint not null,
  primary key (GRUPO_COTA_ID, COTA_ID),
  constraint FKDADA1863C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FKDADA18631E8AD533
  on cota_grupo (GRUPO_COTA_ID)
;

create index FKDADA1863C8181F74
  on cota_grupo (COTA_ID)
;

create table if not exists cota_unificacao
(
  ID bigint auto_increment
    primary key,
  COTA_ID bigint not null,
  DATA_UNIFICACAO date not null,
  POLITICA_COBRANCA_ID bigint null,
  constraint COTA_ID
  unique (COTA_ID),
  constraint FKF83D21077C42C4C3
  foreign key (COTA_ID) references cota (ID)
)
;

create table if not exists cotaunificacao_cotaunificada
(
  COTA_UNIFICACAO_ID bigint null,
  COTA_UNIFICADA_ID bigint null
)
;

create table if not exists cst
(
  id bigint auto_increment
    primary key,
  tributo_id bigint not null,
  codigo varchar(3) not null,
  tipo_operacao varchar(20) not null,
  descricao varchar(255) not null,
  constraint ndx_tributo_cst_unq
  unique (tributo_id, codigo)
)
;

create table if not exists cst_a
(
  id bigint auto_increment
    primary key,
  codigo int not null,
  descricao varchar(255) not null
)
;

create table if not exists debito_credito_cota
(
  ID bigint auto_increment
    primary key,
  COMPOSICAO_COBRANCA bit null,
  DATA_LANCAMENTO date null,
  TIPO_VENCIMENTO date null,
  TIPO_DESCRICAO varchar(255) null,
  OBSERVACOES varchar(255) null,
  TIPO_LANCAMENTO varchar(255) null,
  TIPO_MOVIMENTO varchar(255) null,
  VALOR decimal(18,4) null,
  SLIP_ID bigint not null
)
;

create index FKE2A9E3BEADA6DD0A
  on debito_credito_cota (SLIP_ID)
;

create table if not exists depara
(
  ID bigint auto_increment
    primary key,
  fc varchar(5) null,
  dinap varchar(5) null
)
;

create index dinapfc
  on depara (dinap, fc)
;

create index fc
  on depara (fc)
;

create table if not exists desconto
(
  ID bigint auto_increment
    primary key,
  DATA_OPERACAO datetime null,
  DATA_ALTERACAO timestamp default CURRENT_TIMESTAMP not null,
  TIPO_DESCONTO varchar(255) null,
  USADO tinyint(1) not null,
  valor decimal(18,4) null,
  PREDOMINANTE tinyint(1) not null,
  USUARIO_ID bigint not null
)
;

create index FK6B555EAB7FFF790E
  on desconto (USUARIO_ID)
;

create table if not exists desconto_cota
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  DESCONTO decimal(18,4) null,
  COTA_ID bigint not null,
  DISTRIBUIDOR_ID bigint not null,
  USUARIO_ID bigint not null,
  constraint FKF27C74EDC8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FKF27C74ED56501954
  on desconto_cota (DISTRIBUIDOR_ID)
;

create index FKF27C74ED7FFF790E
  on desconto_cota (USUARIO_ID)
;

create index FKF27C74EDC8181F74
  on desconto_cota (COTA_ID)
;

create table if not exists desconto_cota_fornecedor
(
  DESCONTO_COTA_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  primary key (DESCONTO_COTA_ID, FORNECEDOR_ID),
  constraint FK325007F72E6ACC5A
  foreign key (DESCONTO_COTA_ID) references desconto_cota (ID)
)
;

create index FK325007F72E6ACC5A
  on desconto_cota_fornecedor (DESCONTO_COTA_ID)
;

create index FK325007F79808F874
  on desconto_cota_fornecedor (FORNECEDOR_ID)
;

create table if not exists desconto_cota_produto_excessoes
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  DESCONTO_PREDOMINANTE tinyint(1) not null,
  TIPO_DESCONTO varchar(255) not null,
  COTA_ID bigint not null,
  EDITOR_ID bigint null,
  DESCONTO_ID bigint null,
  DISTRIBUIDOR_ID bigint not null,
  FORNECEDOR_ID bigint null,
  PRODUTO_ID bigint null,
  PRODUTO_EDICAO_ID bigint null,
  USUARIO_ID bigint not null,
  constraint FORNECEDOR_ID
  unique (FORNECEDOR_ID, COTA_ID, PRODUTO_ID, PRODUTO_EDICAO_ID),
  constraint FKD845FEF0C8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FKD845FEF029E8DFE3
  foreign key (DESCONTO_ID) references desconto (ID)
)
;

create index FKD845FEF029E8DFE3
  on desconto_cota_produto_excessoes (DESCONTO_ID)
;

create index FKD845FEF056501954
  on desconto_cota_produto_excessoes (DISTRIBUIDOR_ID)
;

create index FKD845FEF07FFF790E
  on desconto_cota_produto_excessoes (USUARIO_ID)
;

create index FKD845FEF09808F874
  on desconto_cota_produto_excessoes (FORNECEDOR_ID)
;

create index FKD845FEF0A53173D3
  on desconto_cota_produto_excessoes (PRODUTO_EDICAO_ID)
;

create index FKD845FEF0C5C16480
  on desconto_cota_produto_excessoes (PRODUTO_ID)
;

create index FKD845FEF0C8181F74
  on desconto_cota_produto_excessoes (COTA_ID)
;

create table if not exists desconto_distribuidor
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  DESCONTO decimal(18,4) null,
  DISTRIBUIDOR_ID bigint not null,
  USUARIO_ID bigint not null
)
;

create index FK647A9CC256501954
  on desconto_distribuidor (DISTRIBUIDOR_ID)
;

create index FK647A9CC27FFF790E
  on desconto_distribuidor (USUARIO_ID)
;

create table if not exists desconto_distribuidor_fornecedor
(
  DESCONTO_DISTRIBUIDOR_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  primary key (DESCONTO_DISTRIBUIDOR_ID, FORNECEDOR_ID),
  constraint FKAC9219027AB2803A
  foreign key (DESCONTO_DISTRIBUIDOR_ID) references desconto_distribuidor (ID)
)
;

create index FKAC9219027AB2803A
  on desconto_distribuidor_fornecedor (DESCONTO_DISTRIBUIDOR_ID)
;

create index FKAC9219029808F874
  on desconto_distribuidor_fornecedor (FORNECEDOR_ID)
;

create table if not exists desconto_lancamento_cota
(
  DESCONTO_LANCAMENTO_ID bigint not null,
  COTA_ID bigint not null,
  primary key (DESCONTO_LANCAMENTO_ID, COTA_ID),
  constraint FK9A3AA9EAC8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK9A3AA9EA738F4640
  on desconto_lancamento_cota (DESCONTO_LANCAMENTO_ID)
;

create index FK9A3AA9EAC8181F74
  on desconto_lancamento_cota (COTA_ID)
;

create table if not exists desconto_logistica
(
  ID bigint auto_increment
    primary key,
  DATA_INICIO_VIGENCIA date not null,
  DESCRICAO varchar(255) null,
  PERCENTUAL_DESCONTO decimal(18,4) null,
  PERCENTUAL_PRESTACAO_SERVICO float not null,
  TIPO_DESCONTO int not null,
  FORNECEDOR_ID bigint not null
)
;

create index FORNECEDOR_ID
  on desconto_logistica (FORNECEDOR_ID)
;

create table if not exists desconto_produto
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  DESCONTO decimal(18,4) null,
  DISTRIBUIDOR_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  USUARIO_ID bigint not null
)
;

create index FK15B6854556501954
  on desconto_produto (DISTRIBUIDOR_ID)
;

create index FK15B685457FFF790E
  on desconto_produto (USUARIO_ID)
;

create index FK15B68545A53173D3
  on desconto_produto (PRODUTO_EDICAO_ID)
;

create table if not exists desconto_produto_cota
(
  DESCONTO_PRODUTO_ID bigint not null,
  COTA_ID bigint not null,
  primary key (DESCONTO_PRODUTO_ID, COTA_ID),
  constraint FK7BAD5E9334C7C5DA
  foreign key (DESCONTO_PRODUTO_ID) references desconto_produto (ID),
  constraint FK7BAD5E93C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK7BAD5E9334C7C5DA
  on desconto_produto_cota (DESCONTO_PRODUTO_ID)
;

create index FK7BAD5E93C8181F74
  on desconto_produto_cota (COTA_ID)
;

create table if not exists desconto_produto_edicao
(
  ID bigint auto_increment
    primary key,
  DESCONTO decimal(18,4) null,
  TIPO_DESCONTO varchar(255) not null,
  COTA_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  constraint FORNECEDOR_ID
  unique (FORNECEDOR_ID, PRODUTO_EDICAO_ID, COTA_ID),
  constraint FK4899C421C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK4899C4219808F874
  on desconto_produto_edicao (FORNECEDOR_ID)
;

create index FK4899C421A53173D3
  on desconto_produto_edicao (PRODUTO_EDICAO_ID)
;

create index FK4899C421C8181F74
  on desconto_produto_edicao (COTA_ID)
;

create table if not exists desconto_proximos_lancamentos
(
  ID bigint auto_increment
    primary key,
  DATA_INICIO_DESCONTO datetime null,
  QUANTIDADE_PROXIMOS_LANCAMENTOS int null,
  VALOR_DESCONTO decimal(18,4) null,
  DISTRIBUIDOR_ID bigint not null,
  PRODUTO_ID bigint null,
  USUARIO_ID bigint not null,
  APLICADO_A_TODAS_AS_COTAS tinyint(1) not null,
  DESCONTO_ID bigint null,
  QUANTIDADE_PROXIMOS_LANCAMENTOS_ORIGINAL int null,
  constraint FK7E8614E129E8DFE3
  foreign key (DESCONTO_ID) references desconto (ID)
)
;

create index FK7E8614E129E8DFE3
  on desconto_proximos_lancamentos (DESCONTO_ID)
;

create index FK7E8614E156501954
  on desconto_proximos_lancamentos (DISTRIBUIDOR_ID)
;

create index FK7E8614E17FFF790E
  on desconto_proximos_lancamentos (USUARIO_ID)
;

create index FK7E8614E1C5C16480
  on desconto_proximos_lancamentos (PRODUTO_ID)
;

alter table desconto_lancamento_cota
  add constraint FK9A3AA9EA738F4640
foreign key (DESCONTO_LANCAMENTO_ID) references desconto_proximos_lancamentos (ID)
;

create table if not exists desenglobacao
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  PORCENTAGEM_COTA_ENGLOBADA float null,
  COTA_ID_DESENGLOBADA bigint null,
  COTA_ID_ENGLOBADA bigint null,
  USUARIO_ID bigint not null,
  TIPO_PDV_ID bigint not null,
  constraint FKC6D71263E4FA7FFC
  foreign key (COTA_ID_DESENGLOBADA) references cota (ID),
  constraint FKC6D712637F864012
  foreign key (COTA_ID_ENGLOBADA) references cota (ID)
)
;

create index FKC6D71263511FDA95
  on desenglobacao (TIPO_PDV_ID)
;

create index FKC6D712637F864012
  on desenglobacao (COTA_ID_ENGLOBADA)
;

create index FKC6D712637FFF790E
  on desenglobacao (USUARIO_ID)
;

create index FKC6D71263E4FA7FFC
  on desenglobacao (COTA_ID_DESENGLOBADA)
;

create table if not exists destino_encalhe
(
  ID bigint auto_increment
    primary key,
  codigo_distribuidor varchar(8) not null,
  semana_recolhimento varchar(6) not null,
  situacao_atendimento_dde varchar(10) not null,
  produto_edicao_id bigint not null,
  codigo_produto varchar(8) not null,
  numero_edicao bigint not null,
  nome_comercial varchar(44) not null,
  nome_destino_dde varchar(19) not null,
  numero_prioridade_atendimento_dde bigint not null,
  tipo_atendimento_dde varchar(19) null,
  nome_editor varchar(60) not null
)
;

create index fk_produto_edicao_idx
  on destino_encalhe (produto_edicao_id)
;

create table if not exists dia_recolhimento_grupo_cota
(
  GRUPO_ID bigint not null,
  DIA_ID varchar(255) null
)
;

create index FK90BC5188E6F63479
  on dia_recolhimento_grupo_cota (GRUPO_ID)
;

create table if not exists diferenca
(
  id bigint auto_increment
    primary key,
  AUTOMATICA tinyint(1) null,
  DATA_MOVIMENTACAO date null,
  QTDE decimal(18,4) null,
  STATUS_CONFIRMACAO varchar(255) not null,
  TIPO_DIFERENCA varchar(255) not null,
  TIPO_DIRECIONAMENTO varchar(255) null,
  TIPO_ESTOQUE varchar(255) not null,
  itemRecebimentoFisico_ID bigint null,
  LANCAMENTO_DIFERENCA_ID bigint null,
  responsavel_ID bigint null,
  PRODUTO_EDICAO_ID bigint not null
)
;

create index FKF2B9F0955F18CCBC
  on diferenca (responsavel_ID)
;

create index FKF2B9F0957204FEE9
  on diferenca (itemRecebimentoFisico_ID)
;

create index FKF2B9F095A53173D3
  on diferenca (PRODUTO_EDICAO_ID)
;

create index FKF2B9F095C4673234
  on diferenca (LANCAMENTO_DIFERENCA_ID)
;

alter table atualizacao_estoque_gfs
  add constraint FK66A4BF1C5A0FFAA9
foreign key (DIFERENCA_ID) references diferenca (id)
;

create table if not exists diretorio
(
  id bigint auto_increment
    primary key,
  nome_diretorio varchar(255) null,
  endereco_diretorio varchar(255) null
)
;

create table if not exists distribuicao_distribuidor
(
  ID bigint auto_increment
    primary key,
  DIA_SEMANA int not null,
  OPERACAO_DISTRIBUIDOR varchar(255) null,
  DISTRIBUIDOR_ID bigint not null
)
;

create index FKC8044A5556501954
  on distribuicao_distribuidor (DISTRIBUIDOR_ID)
;

create table if not exists distribuicao_fornecedor
(
  ID bigint auto_increment
    primary key,
  DIA_SEMANA int not null,
  OPERACAO_DISTRIBUIDOR varchar(255) null,
  DISTRIBUIDOR_ID bigint not null,
  FORNECEDOR_ID bigint not null
)
;

create index FK1F0231CC56501954
  on distribuicao_fornecedor (DISTRIBUIDOR_ID)
;

create index FK1F0231CC9808F874
  on distribuicao_fornecedor (FORNECEDOR_ID)
;

create table if not exists distribuidor
(
  ID bigint auto_increment
    primary key,
  ACEITA_JURAMENTADO tinyint(1) null,
  CAPACIDADE_DISTRIBUICAO decimal(18,4) null,
  CAPACIDADE_RECOLHIMENTO decimal(18,4) null,
  CODIGO int not null,
  COD_DISTRIBUIDOR_DINAP varchar(255) null,
  COD_DISTRIBUIDOR_FC varchar(255) null,
  CONTROLE_ARQUIVO_COBRANCA bigint null,
  DATA_OPERACAO date not null,
  DESCONTO_COTA_PARA_NEGOCIACAO decimal(18,4) null,
  EXECUTA_RECOLHIMENTO_PARCIAL tinyint(1) not null,
  FATOR_DESCONTO decimal(18,4) null,
  FATOR_RELANCAMENTO_PARCIAL int not null,
  FECHAMENTO_DIARIO_EM_ANDAMENTO tinyint(1) null,
  INFORMACOES_COMPLEMENTARES_PROCURACAO longtext null,
  INICIO_SEMANA_LANCAMENTO varchar(255) not null,
  INICIO_SEMANA_RECOLHIMENTO varchar(255) not null,
  NEGOCIACAO_ATE_PARCELAS int null,
  NUM_REPROG_LANCAMENTO int not null,
  COBRANCA_DATA_PROGRAMADA int null,
  DIAS_NEGOCIACAO int null,
  COMPLEMENTO_ADESAO_ENTREGA_BANCA longtext null,
  UTILIZA_ADESAO_ENTREGA_BANCA tinyint(1) null,
  AJUSTE_ESTOQUE tinyint(1) null,
  DEBITO_CREDITO tinyint(1) null,
  DEVOLUCAO_FORNECEDOR tinyint(1) null,
  FALTAS_SOBRAS tinyint(1) null,
  NEGOCIACAO tinyint(1) null,
  POSTERGACAO_COBRANCA tinyint(1) null,
  CONFERENCIA_CEGA_ENCALHE tinyint(1) null,
  CONFERENCIA_CEGA_RECEBIMENTO tinyint(1) null,
  DIA_RECOLHIMENTO_PRIMEIRO tinyint(1) null,
  DIA_RECOLHIMENTO_QUARTO tinyint(1) null,
  DIA_RECOLHIMENTO_QUINTO tinyint(1) null,
  DIA_RECOLHIMENTO_SEGUNDO tinyint(1) null,
  DIA_RECOLHIMENTO_TERCEIRO tinyint(1) null,
  PERMITE_RECOLHER_DIAS_POSTERIORES tinyint(1) null,
  PARCELAMENTO_DIVIDAS tinyint(1) null,
  DIAS_SUSPENSO int null,
  VALOR_CONSIGNADO decimal(18,4) null,
  NUM_ACUMULO_DIVIDA int null,
  VALOR_SUSPENSAO decimal(18,4) null,
  PRAZO_AVISO_PREVIO_VALIDADE_GARANTIA int null,
  PRAZO_FOLLOW_UP int null,
  AUTO_PREENCHE_QTDE_PDV tinyint(1) not null,
  QNT_DIAS_REUTILIZACAO_CODIGO_COTA bigint null,
  QNT_DIAS_VENCIMENTO_VENDA_ENCALHE int null,
  QTD_DIAS_ENCALHE_ATRASADO_ACEITAVEL int not null,
  QTD_DIAS_LIMITE_PARA_REPROG_LANCAMENTO int not null,
  QTD_DIAS_SUSPENSAO_COTAS int null,
  REQUER_AUTORIZACAO_ENCALHE_SUPERA_REPARTE tinyint(1) not null,
  SUPERVISIONA_VENDA_NEGATIVA tinyint(1) null,
  ACEITA_RECOLHIMENTO_PARCIAL_ATRASO tinyint(1) not null,
  CNAE varchar(10) null,
  TIPO_ATIVIDADE varchar(255) null,
  TIPO_CONT_CE varchar(255) null,
  TIPO_IMPRESSAO_CE varchar(255) null,
  TIPO_IMPRESSAO_INTERFACE_LED varchar(255) null,
  ARQUIVO_INTERFACE_LED_PICKING_1 varchar(100) null,
  ARQUIVO_INTERFACE_LED_PICKING_2 varchar(100) null,
  ARQUIVO_INTERFACE_LED_PICKING_3 varchar(100) null,
  ARQUIVO_INTERFACE_LED_PICKING_4 varchar(100) default 'PICKING4.TXT' null,
  TIPO_IMPRESSAO_NE_NECA_DANFE varchar(255) null,
  UTILIZA_CONTROLE_APROVACAO tinyint(1) null,
  UTILIZA_GARANTIA_PDV tinyint(1) not null,
  UTILIZA_PROCURACAO_ENTREGADORES tinyint(1) null,
  UTILIZA_SUGESTAO_INCREMENTO tinyint(1) null,
  VALOR_CONSIGNADO_SUSPENSAO_COTAS decimal(18,4) null,
  PJ_ID bigint not null,
  PARAMETRO_CONTRATO_COTA_ID bigint null,
  ACEITA_BAIXA_PGTO_MAIOR tinyint(1) null,
  ACEITA_BAIXA_PGTO_MENOR tinyint(1) null,
  ACEITA_BAIXA_PGTO_VENCIDO tinyint(1) null,
  ASSUNTO_EMAIL_COBRANCA varchar(255) null,
  MENSAGEM_EMAIL_COBRANCA varchar(255) null,
  NUM_DIAS_NOVA_COBRANCA int null,
  PRACA_VERANEIO tinyint(1) default '0' null,
  INTERFACES_MATRIZ_EM_EXECUCAO tinyint(1) default '0' not null,
  DATA_INICIO_INTERFACES_MATRIZ_EM_EXECUCAO datetime null,
  LEIAUTE_PICKING varchar(20) null,
  CONSOLIDADO_COTA tinyint(1) null,
  PARAR_ACUM_DIVIDAS tinyint(1) default '0' null,
  NUMERO_DISPOSITIVO_LEGAL varchar(60) null,
  DATA_LIMITE_VIGENCIA_REGIME_ESPECIAL varchar(45) null,
  POSSUI_REGIME_ESPECIAL_DISPENSA_INTERNA tinyint default '0' not null,
  REGIME_TRIBUTARIO_ID bigint default '1' not null,
  SUGERE_SUSPENSAO tinyint(1) null,
  DESCRICAO_TAXA_EXTRA varchar(100) null,
  PERCENTUAL_TAXA_EXTRA decimal(18,2) null,
  NF_INFORMACOES_ADICIONAIS varchar(600) null,
  CODIGO_ESTABELECIMENTO_EMISSOR varchar(100) null,
  CNPJ_ESTABELECIMENTO_EMISSOR varchar(100) null,
  CODIGO_LOCAL varchar(100) null,
  CODIGO_CENTRO_EMISSOR varchar(100) null,
  LIBERAR_TRANSFERENCIA_PARCIAL tinyint(1) default '0' null,
  VENDA_TOTAL_ENCALHE tinyint(1) default '0' null,
  constraint PJ_ID
  unique (PJ_ID)
)
;

create index FK2BE223AE479FD586
  on distribuidor (PJ_ID)
;

create index FK2BE223AEC54E738
  on distribuidor (PARAMETRO_CONTRATO_COTA_ID)
;

create index fk_reg_trib_distribuidor_1_idx
  on distribuidor (REGIME_TRIBUTARIO_ID)
;

alter table certificado
  add constraint FK_certificado
foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
;

alter table desconto_cota
  add constraint FKF27C74ED56501954
foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
;

alter table desconto_cota_produto_excessoes
  add constraint FKD845FEF056501954
foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
;

alter table desconto_distribuidor
  add constraint FK647A9CC256501954
foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
;

alter table desconto_produto
  add constraint FK15B6854556501954
foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
;

alter table desconto_proximos_lancamentos
  add constraint FK7E8614E156501954
foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
;

alter table distribuicao_distribuidor
  add constraint FKC8044A5556501954
foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
;

alter table distribuicao_fornecedor
  add constraint FK1F0231CC56501954
foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
;

create table if not exists distribuidor_classificacao_cota
(
  ID bigint auto_increment
    primary key,
  COD_CLASSIFICACAO varchar(255) null,
  VALOR_ATE decimal(10,2) null,
  VALOR_DE decimal(8,2) null,
  DISTRIBUIDOR_ID bigint null,
  constraint FK71650FAA56501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
)
;

create index FK71650FAA56501954
  on distribuidor_classificacao_cota (DISTRIBUIDOR_ID)
;

create table if not exists distribuidor_grid_distribuicao
(
  ID bigint auto_increment
    primary key,
  COMPLEMENTAR_AUTOMATICO tinyint(1) null,
  GERACAO_AUTOMATICA_ESTUDO tinyint(1) null,
  PERCENTUAL_MAXIMO_FIXACAO int null,
  PRACA_VERANEIO tinyint(1) null,
  VENDA_MEDIA_MAIS int not null,
  DISTRIBUIDOR_ID bigint null,
  EXIBIR_INFORMACOES_REPARTE_COMPLEMENTAR tinyint(1) default '0' null,
  constraint FKAB82C44056501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
)
;

create index FKAB82C44056501954
  on distribuidor_grid_distribuicao (DISTRIBUIDOR_ID)
;

create table if not exists distribuidor_nota_fiscal_tipo_emissao
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(60) not null,
  SEQUENCIA bigint not null,
  TIPO_EMISSAO_ENUM varchar(60) not null
)
;

create table if not exists distribuidor_nota_fiscal_tipos
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(60) not null,
  NOME_CAMPO_TELA varchar(45) not null,
  GRUPO_NOTA_FISCAL varchar(45) not null,
  NOTA_FISCAL_TIPO_EMISSAO_ID bigint null,
  DISTRIBUIDOR_ID bigint not null,
  constraint fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_1
  foreign key (NOTA_FISCAL_TIPO_EMISSAO_ID) references distribuidor_nota_fiscal_tipo_emissao (ID),
  constraint fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_2
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
)
;

create index fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_1_idx
  on distribuidor_nota_fiscal_tipos (NOTA_FISCAL_TIPO_EMISSAO_ID)
;

create index fk_distribuidor_nota_fiscal_tipos_2_idx
  on distribuidor_nota_fiscal_tipos (DISTRIBUIDOR_ID)
;

create table if not exists distribuidor_nota_fiscal_tipos_tipo_emissao
(
  NOTA_FISCAL_TIPO_ID bigint not null,
  NOTA_FISCAL_TIPO_EMISSAO_ID bigint not null,
  primary key (NOTA_FISCAL_TIPO_EMISSAO_ID, NOTA_FISCAL_TIPO_ID),
  constraint CHAVE_UNIQUE
  unique (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID),
  constraint fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_TIPO_EMISSAO_1
  foreign key (NOTA_FISCAL_TIPO_ID) references distribuidor_nota_fiscal_tipos (ID),
  constraint fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_TIPO_EMISSAO_2
  foreign key (NOTA_FISCAL_TIPO_EMISSAO_ID) references distribuidor_nota_fiscal_tipo_emissao (ID)
)
;

create table if not exists distribuidor_percentual_excedente
(
  ID bigint auto_increment
    primary key,
  EFICIENCIA varchar(255) null,
  PDV int not null,
  VENDA int not null,
  DISTRIBUIDOR_ID bigint null,
  constraint FK6A41D9656501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
)
;

create index FK6A41D9656501954
  on distribuidor_percentual_excedente (DISTRIBUIDOR_ID)
;

create table if not exists distribuidor_tipo_nota_natureza_operacao
(
  TIPO_NOTA_ID bigint not null,
  NATUREZA_OPERACAO_ID bigint not null,
  primary key (TIPO_NOTA_ID, NATUREZA_OPERACAO_ID)
)
;

create index FK_tipo_movimento_id
  on distribuidor_tipo_nota_natureza_operacao (NATUREZA_OPERACAO_ID)
;

create table if not exists distribuidor_tipos_emissoes_nota_fiscal
(
  DISTRIBUIDOR_ID bigint not null,
  NOTA_FISCAL_TIPO_EMISSAO_ID bigint not null,
  SEQUENCIA bigint not null,
  primary key (DISTRIBUIDOR_ID, NOTA_FISCAL_TIPO_EMISSAO_ID),
  constraint CHAVE_UNIQUE
  unique (DISTRIBUIDOR_ID, NOTA_FISCAL_TIPO_EMISSAO_ID),
  constraint fk_DISTRIBUIDOR_TIPOS_EMISSOES_1
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID),
  constraint fk_DISTRIBUIDOR_TIPOS_EMISSOES_2
  foreign key (NOTA_FISCAL_TIPO_EMISSAO_ID) references distribuidor_nota_fiscal_tipo_emissao (ID)
)
;

create index fk_DISTRIBUIDOR_TIPOS_EMISSOES_2
  on distribuidor_tipos_emissoes_nota_fiscal (NOTA_FISCAL_TIPO_EMISSAO_ID)
;

create table if not exists divida
(
  ID bigint auto_increment
    primary key,
  ACUMULADA tinyint(1) null,
  DATA date not null,
  STATUS varchar(255) not null,
  VALOR decimal(18,4) null,
  COTA_ID bigint not null,
  DIVIDA_RAIZ_ID bigint null,
  USUARIO_ID bigint not null,
  ORIGEM_NEGOCIACAO tinyint(1) null,
  constraint FK78367075C8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK78367075E87BFFEC
  foreign key (DIVIDA_RAIZ_ID) references divida (ID)
)
;

create index FK783670757FFF790E
  on divida (USUARIO_ID)
;

create index FK78367075C8181F74
  on divida (COTA_ID)
;

create index FK78367075E87BFFEC
  on divida (DIVIDA_RAIZ_ID)
;

alter table acumulo_divida
  add constraint acumulo_divida_ibfk_1
foreign key (DIVIDA_ID) references divida (ID)
;

alter table cobranca
  add constraint FKF83D21077C42C4C1
foreign key (DIVIDA_ID) references divida (ID)
;

create table if not exists divida_consolidado
(
  DIVIDA_ID bigint null,
  CONSOLIDADO_ID bigint null
)
;

create index ndx_consolidado
  on divida_consolidado (CONSOLIDADO_ID)
;

create index ndx_divida
  on divida_consolidado (DIVIDA_ID, CONSOLIDADO_ID)
;

create table if not exists duplis
(
  cota int null,
  codigo int null,
  edicao int null,
  tipo int null,
  qtd int null,
  mov_id int null
)
;

create table if not exists editor
(
  ID bigint auto_increment
    primary key,
  ATIVO tinyint(1) not null,
  CODIGO bigint not null,
  NOME_CONTATO varchar(255) null,
  ORIGEM_INTERFACE tinyint(1) null,
  JURIDICA_ID bigint not null,
  DESCONTO_ID bigint null
)
;

create index FK799F156D9218183B
  on editor (JURIDICA_ID)
;

create index NDX_EDITOR_COD_EDITOR
  on editor (CODIGO)
;

create table if not exists editor_fornecedor
(
  EDITOR_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  primary key (EDITOR_ID, FORNECEDOR_ID),
  constraint FK221C1777B2A11874
  foreign key (EDITOR_ID) references editor (ID)
)
;

create index FK221C17779808F874
  on editor_fornecedor (FORNECEDOR_ID)
;

create index FK221C1777B2A11874
  on editor_fornecedor (EDITOR_ID)
;

create table if not exists endereco
(
  ID bigint auto_increment
    primary key,
  BAIRRO varchar(60) null,
  CEP varchar(9) null,
  CIDADE varchar(60) null,
  CODIGO_BAIRRO int null,
  CODIGO_CIDADE_IBGE int null,
  CODIGO_UF int null,
  COMPLEMENTO varchar(60) null,
  LOGRADOURO varchar(60) null,
  NUMERO varchar(60) null,
  TIPO_LOGRADOURO varchar(255) null,
  UF varchar(2) null,
  PESSOA_ID bigint null,
  LINHA bigint null
)
;

create index FK95D357C99B8CB634
  on endereco (PESSOA_ID)
;

create table if not exists endereco_cota
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_ENDERECO varchar(255) not null,
  ENDERECO_ID bigint not null,
  COTA_ID bigint not null,
  constraint FKE8037E8FAE761C74
  foreign key (ENDERECO_ID) references endereco (ID),
  constraint FKE8037E8FC8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FKE8037E8FAE761C74
  on endereco_cota (ENDERECO_ID)
;

create index FKE8037E8FC8181F74
  on endereco_cota (COTA_ID)
;

create table if not exists endereco_distribuidor
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_ENDERECO varchar(255) not null,
  ENDERECO_ID bigint not null,
  DISTRIBUIDOR_ID bigint not null,
  constraint DISTRIBUIDOR_ID
  unique (DISTRIBUIDOR_ID),
  constraint FK1E36E464AE761C74
  foreign key (ENDERECO_ID) references endereco (ID),
  constraint FK1E36E46456501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
)
;

create index FK1E36E46456501954
  on endereco_distribuidor (DISTRIBUIDOR_ID)
;

create index FK1E36E464AE761C74
  on endereco_distribuidor (ENDERECO_ID)
;

create table if not exists endereco_editor
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_ENDERECO varchar(255) not null,
  ENDERECO_ID bigint not null,
  EDITOR_ID bigint not null,
  constraint FKF7E816A3AE761C74
  foreign key (ENDERECO_ID) references endereco (ID),
  constraint FKF7E816A3B2A11874
  foreign key (EDITOR_ID) references editor (ID)
)
;

create index FKF7E816A3AE761C74
  on endereco_editor (ENDERECO_ID)
;

create index FKF7E816A3B2A11874
  on endereco_editor (EDITOR_ID)
;

create table if not exists endereco_entregador
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_ENDERECO varchar(255) not null,
  ENDERECO_ID bigint not null,
  ENTREGADOR_ID bigint not null,
  constraint FKC8E5F6C5AE761C74
  foreign key (ENDERECO_ID) references endereco (ID)
)
;

create index FKC8E5F6C583B33034
  on endereco_entregador (ENTREGADOR_ID)
;

create index FKC8E5F6C5AE761C74
  on endereco_entregador (ENDERECO_ID)
;

create table if not exists endereco_fiador
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_ENDERECO varchar(255) not null,
  ENDERECO_ID bigint not null,
  FIADOR_ID bigint not null,
  constraint FKF9DF85BFAE761C74
  foreign key (ENDERECO_ID) references endereco (ID)
)
;

create index FKF9DF85BF8DC372F4
  on endereco_fiador (FIADOR_ID)
;

create index FKF9DF85BFAE761C74
  on endereco_fiador (ENDERECO_ID)
;

create table if not exists endereco_fornecedor
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_ENDERECO varchar(255) not null,
  ENDERECO_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  constraint FKADE2039BAE761C74
  foreign key (ENDERECO_ID) references endereco (ID)
)
;

create index FKADE2039B9808F874
  on endereco_fornecedor (FORNECEDOR_ID)
;

create index FKADE2039BAE761C74
  on endereco_fornecedor (ENDERECO_ID)
;

create table if not exists endereco_pdv
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_ENDERECO varchar(255) not null,
  ENDERECO_ID bigint not null,
  PDV_ID bigint not null,
  constraint FK498CAF6CAE761C74
  foreign key (ENDERECO_ID) references endereco (ID)
)
;

create index FK498CAF6CA65B70F4
  on endereco_pdv (PDV_ID)
;

create index FK498CAF6CAE761C74
  on endereco_pdv (ENDERECO_ID)
;

create table if not exists endereco_transportador
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_ENDERECO varchar(255) not null,
  ENDERECO_ID bigint not null,
  TRANSPORTADOR_ID bigint not null,
  constraint FKBAE045D9AE761C74
  foreign key (ENDERECO_ID) references endereco (ID)
)
;

create index FKBAE045D9AE761C74
  on endereco_transportador (ENDERECO_ID)
;

create index FKBAE045D9D90B1440
  on endereco_transportador (TRANSPORTADOR_ID)
;

create table if not exists entregador
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  COMISSIONADO tinyint(1) not null,
  INICIO_ATIVIDADE date not null,
  PERCENTUAL_COMISSAO decimal(18,4) null,
  PERCENTUAL_FATURAMENTO decimal(18,4) null,
  PROCURACAO tinyint(1) not null,
  TAXA_FIXA decimal(18,4) null,
  PESSOA_ID bigint not null,
  ROTA_ID bigint null,
  constraint CODIGO
  unique (CODIGO)
)
;

create index FK860C808F9B8CB634
  on entregador (PESSOA_ID)
;

create index FK860C808FE19C69D4
  on entregador (ROTA_ID)
;

alter table endereco_entregador
  add constraint FKC8E5F6C583B33034
foreign key (ENTREGADOR_ID) references entregador (ID)
;

create table if not exists estoque_cota_id_duplicados
(
  id bigint null
)
;

create table if not exists estoque_produto
(
  ID bigint auto_increment
    primary key,
  QTDE decimal(18,4) null,
  QTDE_DANIFICADO decimal(18,4) null,
  QTDE_DEVOLUCAO_ENCALHE decimal(18,4) null,
  QTDE_DEVOLUCAO_FORNECEDOR decimal(18,4) null,
  QTDE_SUPLEMENTAR decimal(18,4) null,
  QTDE_PERDA decimal(18,4) null,
  QTDE_GANHO decimal(18,4) null,
  VERSAO bigint default '0' not null,
  PRODUTO_EDICAO_ID bigint not null,
  qtde_juramentado decimal(18,4) null,
  constraint PRODUTO_EDICAO_ID
  unique (PRODUTO_EDICAO_ID)
)
;

create index FKE9F637F2A53173D3
  on estoque_produto (PRODUTO_EDICAO_ID)
;

create table if not exists estoque_produto_cota
(
  ID bigint auto_increment
    primary key,
  QTDE_DEVOLVIDA decimal(18,4) null,
  QTDE_RECEBIDA decimal(18,4) null,
  COTA_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  constraint FK5CA35006C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK5CA35006A53173D3
  on estoque_produto_cota (PRODUTO_EDICAO_ID)
;

create index FK5CA35006C8181F74
  on estoque_produto_cota (COTA_ID)
;

create index NDX_COTA_PRODUTO
  on estoque_produto_cota (COTA_ID, PRODUTO_EDICAO_ID)
;

create index NDX_PRODUTO_COTA
  on estoque_produto_cota (PRODUTO_EDICAO_ID, COTA_ID)
;

create table if not exists estoque_produto_cota_memoria
(
  ID_AUX bigint auto_increment
    primary key,
  ID bigint null,
  QTDE_DEVOLVIDA decimal(18,4) null,
  QTDE_RECEBIDA decimal(18,4) null,
  COTA_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  constraint COTA_ID
  unique (COTA_ID, PRODUTO_EDICAO_ID)
)
;

create table if not exists estoque_produto_fila
(
  ID bigint auto_increment
    primary key,
  TIPO_ESTOQUE varchar(255) default '0' not null,
  COTA_ID bigint default '0' null,
  PRODUTO_EDICAO_ID bigint default '0' not null,
  QTDE decimal(18,4) default '0.0000' not null,
  OPERACAO_ESTOQUE varchar(255) default '0' not null,
  SERVIDOR varchar(45) null,
  constraint FK_ESTOQUE_PRODUTO_FILA_cota
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK_ESTOQUE_PRODUTO_FILA_cota
  on estoque_produto_fila (COTA_ID)
;

create index FK_ESTOQUE_PRODUTO_FILA_produto_edicao
  on estoque_produto_fila (PRODUTO_EDICAO_ID)
;

create table if not exists estoque_produto_memoria
(
  ID_AUX bigint auto_increment
    primary key,
  ID bigint null,
  QTDE decimal(18,4) null,
  QTDE_DANIFICADO decimal(18,4) null,
  QTDE_DEVOLUCAO_ENCALHE decimal(18,4) null,
  QTDE_DEVOLUCAO_FORNECEDOR decimal(18,4) null,
  QTDE_SUPLEMENTAR decimal(18,4) null,
  QTDE_PERDA decimal(18,4) null,
  QTDE_GANHO decimal(18,4) null,
  VERSAO bigint default '0' not null,
  PRODUTO_EDICAO_ID bigint not null,
  qtde_juramentado decimal(18,4) null,
  constraint PRODUTO_EDICAO_ID
  unique (PRODUTO_EDICAO_ID)
)
;

create table if not exists estqbox
(
  linha_vazia varchar(1) null,
  tipo int null,
  box int null,
  nome_box varchar(10) null,
  produto varchar(8) null,
  edicao int null,
  nome_produto varchar(45) null,
  quantidade int null,
  produto_edicao_id bigint null
)
;

create table if not exists estqmov
(
  linha_vazia varchar(1) null,
  tipo int null,
  produto varchar(8) null,
  edicao int null,
  DATA date null,
  NRO_DOCTO int null,
  TIPO_MOVTO int null,
  ORIGEM int null,
  DESTINO int null,
  QUANTIDADE int null,
  PRECO_CAPA decimal(18,2) null,
  PERC_DESCTO decimal(18,4) null,
  DOCTO_ORIGEM int null,
  FLAG_ESTORNO varchar(1) null,
  produto_edicao_id bigint null
)
;

create table if not exists estqsisfil
(
  cod_sisfil int null,
  produto varchar(8) null,
  edicao int null,
  nome_produto varchar(45) null,
  entrada decimal(18,2) null,
  saida decimal(18,2) null,
  preco decimal(10,2) null,
  saldo decimal(18,2) null,
  data_str varchar(10) null,
  data date null,
  produto_edicao_id int null
)
;

create table if not exists estrategia
(
  ID bigint auto_increment
    primary key,
  ABRANGENCIA decimal(19,2) null,
  OPORTUNIDADE_VENDA varchar(255) null,
  PERIODO int null,
  REPARTE_MINIMO decimal(19,2) null,
  PRODUTO_EDICAO_ID bigint not null
)
;

create index FK65485699A53173D3
  on estrategia (PRODUTO_EDICAO_ID)
;

create table if not exists estrategia_base_distribuicao
(
  ID bigint auto_increment
    primary key,
  PERIODO_EDICAO int null,
  PESO int not null,
  ESTRATEGIA_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  constraint FK805710C02ADC726F
  foreign key (ESTRATEGIA_ID) references estrategia (ID)
)
;

create index FK805710C02ADC726F
  on estrategia_base_distribuicao (ESTRATEGIA_ID)
;

create index FK805710C0A53173D3
  on estrategia_base_distribuicao (PRODUTO_EDICAO_ID)
;

create table if not exists estudo
(
  ID bigint not null
    primary key,
  data_alteracao datetime null,
  data_cadastro datetime not null,
  DATA_LANCAMENTO date null,
  DISTRIBUICAO_POR_MULTIPLOS int null,
  ESTUDO_ORIGEM_COPIA bigint null,
  LANCAMENTO_ID bigint null,
  PACOTE_PADRAO decimal(19,2) null,
  QTDE_REPARTE decimal(19,2) not null,
  REPARTE_DISTRIBUIR decimal(19,2) null,
  SOBRA decimal(19,2) null,
  STATUS varchar(255) not null,
  PRODUTO_EDICAO_ID bigint null,
  USUARIO_ID bigint null,
  DADOS_VENDA_MEDIA text null,
  tipo_geracao_estudo varchar(255) null,
  ABRANGENCIA double null
)
;

create index FK7A77787A7FFF790E
  on estudo (USUARIO_ID)
;

create index FK7A77787AA53173D3
  on estudo (PRODUTO_EDICAO_ID)
;

create table if not exists estudo_bonificacoes
(
  ID bigint auto_increment
    primary key,
  BONIFICACAO int null,
  COMPONENTE varchar(255) null,
  ELEMENTO varchar(255) null,
  REPARTE_MINIMO decimal(19,2) null,
  TODAS_AS_COTAS tinyint(1) null,
  ESTUDO_ID bigint not null,
  constraint ESTUDO_ID
  unique (ESTUDO_ID)
)
;

create index FK33426C685E727B07
  on estudo_bonificacoes (ESTUDO_ID)
;

create table if not exists estudo_cota
(
  ID bigint auto_increment
    primary key,
  CLASSIFICACAO varchar(255) null,
  COTA_NOVA tinyint(1) null,
  INDICE_CORRECAO_TENDENCIA decimal(19,2) null,
  INDICE_VENDA_CRESCENTE decimal(19,2) null,
  MIX int null,
  PERCENTUAL_ENCALHE_MAXIMO decimal(19,2) null,
  QTDE_EFETIVA decimal(19,2) null,
  QTDE_PREVISTA decimal(19,2) null,
  QUANTIDADE_PDVS int null,
  REPARTE decimal(19,2) null,
  REPARTE_JURAMENTADO_A_FATURAR decimal(19,2) null,
  REPARTE_MAXIMO decimal(19,2) null,
  REPARTE_MINIMO decimal(19,2) null,
  TIPO_ESTUDO varchar(20) default 'NORMAL' null,
  VENDA_MEDIA decimal(19,2) null,
  VENDA_MEDIA_MAIS_N int null,
  VENDA_MEDIA_NOMINAL decimal(19,2) null,
  COTA_ID bigint not null,
  ESTUDO_ID bigint not null,
  constraint FK12FD247EC8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK12FD247ED3010D4F
  foreign key (ESTUDO_ID) references estudo (ID)
)
;

create index FK12FD247EC8181F74
  on estudo_cota (COTA_ID)
;

create index FK12FD247ED3010D4F
  on estudo_cota (ESTUDO_ID)
;

create index ndx_estudo_cota
  on estudo_cota (ESTUDO_ID, COTA_ID)
;

create table if not exists estudo_cota_gerado
(
  ID bigint auto_increment
    primary key,
  CLASSIFICACAO varchar(255) null,
  COTA_NOVA tinyint(1) null,
  INDICE_CORRECAO_TENDENCIA decimal(19,2) null,
  INDICE_VENDA_CRESCENTE decimal(19,2) null,
  MIX int null,
  PERCENTUAL_ENCALHE_MAXIMO decimal(19,2) null,
  QTDE_EFETIVA decimal(19,2) null,
  QTDE_PREVISTA decimal(19,2) null,
  QUANTIDADE_PDVS int null,
  REPARTE decimal(19,2) null,
  REPARTE_JURAMENTADO_A_FATURAR decimal(19,2) null,
  REPARTE_MAXIMO decimal(19,2) null,
  REPARTE_MINIMO decimal(19,2) null,
  TIPO_ESTUDO varchar(20) default 'NORMAL' null,
  VENDA_MEDIA decimal(19,2) null,
  VENDA_MEDIA_MAIS_N int null,
  VENDA_MEDIA_NOMINAL decimal(19,2) null,
  COTA_ID bigint not null,
  ESTUDO_ID bigint not null,
  REPARTE_INICIAL decimal(19) null,
  constraint FKF0111BB9C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FKF0111BB95E727B07
  on estudo_cota_gerado (ESTUDO_ID)
;

create index FKF0111BB9C8181F74
  on estudo_cota_gerado (COTA_ID)
;

create index ndx_estudo_cota
  on estudo_cota_gerado (ESTUDO_ID, COTA_ID)
;

create table if not exists estudo_gerado
(
  ID bigint auto_increment
    primary key,
  data_alteracao datetime null,
  data_cadastro datetime not null,
  DATA_LANCAMENTO date null,
  DISTRIBUICAO_POR_MULTIPLOS int null,
  ESTUDO_ORIGEM_COPIA bigint null,
  LANCAMENTO_ID bigint null,
  LIBERADO tinyint(1) null,
  PACOTE_PADRAO decimal(19,2) null,
  QTDE_REPARTE decimal(19,2) not null,
  REPARTE_DISTRIBUIR decimal(19,2) null,
  SOBRA decimal(19,2) null,
  STATUS varchar(255) not null,
  PRODUTO_EDICAO_ID bigint null,
  USUARIO_ID bigint null,
  DADOS_VENDA_MEDIA text null,
  REPARTE_MINIMO bigint null,
  tipo_geracao_estudo varchar(255) null,
  ABRANGENCIA double null,
  REPARTE_TOTAL bigint null,
  USED_MIN_MAX_MIX tinyint(1) default '0' null
)
;

create index FK4E8B943D7FFF790E
  on estudo_gerado (USUARIO_ID)
;

create index FK4E8B943DA53173D3
  on estudo_gerado (PRODUTO_EDICAO_ID)
;

create index idx_data_lancamento
  on estudo_gerado (DATA_LANCAMENTO)
;

alter table estudo_bonificacoes
  add constraint FK33426C685E727B07
foreign key (ESTUDO_ID) references estudo_gerado (ID)
;

alter table estudo_cota_gerado
  add constraint FKF0111BB95E727B07
foreign key (ESTUDO_ID) references estudo_gerado (ID)
;

create table if not exists estudo_pdv
(
  ID bigint auto_increment
    primary key,
  REPARTE decimal(19,2) null,
  COTA_ID bigint not null,
  ESTUDO_ID bigint not null,
  PDV_ID bigint not null,
  constraint FK8DF0F9DC8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK8DF0F9D5E727B07
  foreign key (ESTUDO_ID) references estudo_gerado (ID)
)
;

create index FK8DF0F9D5E727B07
  on estudo_pdv (ESTUDO_ID)
;

create index FK8DF0F9DA65B70F4
  on estudo_pdv (PDV_ID)
;

create index FK8DF0F9DC8181F74
  on estudo_pdv (COTA_ID)
;

create table if not exists estudo_produto_edicao
(
  ID bigint auto_increment
    primary key,
  INDICE_CORRECAO decimal(19,2) null,
  REPARTE decimal(19,2) not null,
  VENDA decimal(19,2) not null,
  VENDA_CORRIGIDA decimal(19,2) null,
  COTA_ID bigint null,
  ESTUDO_ID bigint not null,
  PRODUTO_EDICAO_ID bigint null,
  constraint FK70621A52C8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK70621A525E727B07
  foreign key (ESTUDO_ID) references estudo_gerado (ID)
)
;

create index FK70621A525E727B07
  on estudo_produto_edicao (ESTUDO_ID)
;

create index FK70621A52A53173D3
  on estudo_produto_edicao (PRODUTO_EDICAO_ID)
;

create index FK70621A52C8181F74
  on estudo_produto_edicao (COTA_ID)
;

create table if not exists estudo_produto_edicao_base
(
  ID bigint auto_increment
    primary key,
  COLECAO bigint not null,
  EDICAO_ABERTA bigint not null,
  PARCIAL bigint not null,
  PERIODO_PARCIAL bigint null,
  PESO bigint not null,
  VENDA_CORRIGIDA decimal(19,2) null,
  ESTUDO_ID bigint not null,
  PRODUTO_EDICAO_ID bigint null,
  isConsolidado tinyint(1) default '0' null,
  constraint FK6C34489E5E727B07
  foreign key (ESTUDO_ID) references estudo_gerado (ID)
)
;

create index FK6C34489E5E727B07
  on estudo_produto_edicao_base (ESTUDO_ID)
;

create index FK6C34489EA53173D3
  on estudo_produto_edicao_base (PRODUTO_EDICAO_ID)
;

create table if not exists evento_execucao
(
  ID bigint not null
    primary key,
  DESCRICAO varchar(100) not null,
  NOME varchar(30) not null
)
;

create table if not exists excecao_produto_cota
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  TIPO_EXCECAO varchar(255) not null,
  COTA_ID bigint not null,
  PRODUTO_ID bigint null,
  USUARIO_ID bigint not null,
  TIPO_CLASSIFICACAO_PRODUTO_ID bigint null,
  CODIGO_ICD bigint null,
  constraint FKA42FF9A2C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FKA42FF9A27FFF790E
  on excecao_produto_cota (USUARIO_ID)
;

create index FKA42FF9A2C5C16480
  on excecao_produto_cota (PRODUTO_ID)
;

create index FKA42FF9A2C8181F74
  on excecao_produto_cota (COTA_ID)
;

create table if not exists expedicao
(
  ID bigint auto_increment
    primary key,
  DATA_EXPEDICAO datetime not null,
  USUARIO bigint not null
)
;

create index FKAD6AB024EA1BE470
  on expedicao (USUARIO)
;

create index ndx_data_expedicao
  on expedicao (DATA_EXPEDICAO)
;

create table if not exists fechamento_diario
(
  ID bigint auto_increment
    primary key,
  DATA_FECHAMENTO date null,
  USUARIO tinyblob null,
  USUARIO_ID bigint not null,
  DATA_CRIACAO datetime null,
  constraint DATA_FECHAMENTO
  unique (DATA_FECHAMENTO)
)
;

create index FK23D758477FFF790E
  on fechamento_diario (USUARIO_ID)
;

create table if not exists fechamento_diario_consolidado_cota
(
  ID bigint auto_increment
    primary key,
  QNT_ATIVOS int null,
  QNT_AUSENTE_ENCALHE int null,
  QNT_AUSENTE_REPARTE int null,
  QNT_INATIVAS int null,
  QNT_NOVOS int null,
  QNT_TOTAL int null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FK40875A0D8CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK40875A0D8CD5CBB3
  on fechamento_diario_consolidado_cota (FECHAMENTO_DIARIO_ID)
;

create table if not exists fechamento_diario_consolidado_divida
(
  ID bigint auto_increment
    primary key,
  TIPO_DIVIDA varchar(255) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FK3D7A65A98CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK3D7A65A98CD5CBB3
  on fechamento_diario_consolidado_divida (FECHAMENTO_DIARIO_ID)
;

create table if not exists fechamento_diario_consolidado_encalhe
(
  ID bigint auto_increment
    primary key,
  SALDO decimal(18,4) null,
  VALOR_FALTA_EM decimal(18,4) null,
  VALOR_FISICO decimal(18,4) null,
  VALOR_JURAMENTADO decimal(18,4) null,
  VALOR_LOGICO decimal(18,4) null,
  VALOR_SOBRA_EM decimal(18,4) null,
  VALOR_VENDA decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FECHAMENTO_DIARIO_ID
  unique (FECHAMENTO_DIARIO_ID),
  constraint FKAE31836E8CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FKAE31836E8CD5CBB3
  on fechamento_diario_consolidado_encalhe (FECHAMENTO_DIARIO_ID)
;

create table if not exists fechamento_diario_consolidado_reparte
(
  ID bigint auto_increment
    primary key,
  VALOR_DIFERENCA decimal(18,4) null,
  VALOR_DISTRIBUIDO decimal(18,4) null,
  VALOR_FALTAS decimal(18,4) null,
  VALOR_REPARTE decimal(18,4) null,
  SOBRA_DISTRIBUIDA decimal(18,4) null,
  VALOR_SOBRAS decimal(18,4) null,
  VALOR_TRANSFERIDO decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  VALOR_A_DISTRIBUIR decimal(18,4) null,
  constraint FECHAMENTO_DIARIO_ID
  unique (FECHAMENTO_DIARIO_ID),
  constraint FK4F3E31AB8CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK4F3E31AB8CD5CBB3
  on fechamento_diario_consolidado_reparte (FECHAMENTO_DIARIO_ID)
;

create table if not exists fechamento_diario_consolidado_suplementar
(
  ID bigint auto_increment
    primary key,
  VALOR_ESTOQUE_LOGICO decimal(18,4) null,
  VALOR_SALDO decimal(18,4) null,
  VALOR_TRANSFERENCIA decimal(18,4) null,
  VALOR_VENDAS decimal(18,4) null,
  VALOR_ALTERACAO_PRECO decimal(18,4) default '0.0000' null,
  FECHAMENTO_DIARIO_ID bigint not null,
  valor_inventario decimal(18,4) null,
  constraint FECHAMENTO_DIARIO_ID
  unique (FECHAMENTO_DIARIO_ID),
  constraint FKD0D1E0428CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FKD0D1E0428CD5CBB3
  on fechamento_diario_consolidado_suplementar (FECHAMENTO_DIARIO_ID)
;

create table if not exists fechamento_diario_cota
(
  ID bigint auto_increment
    primary key,
  NOME_COTA varchar(255) null,
  NUMERO_COTA int null,
  TIPO_DETALHE_COTA varchar(255) null,
  FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID bigint not null,
  constraint FK53821CD11F45E509
  foreign key (FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID) references fechamento_diario_consolidado_cota (ID)
)
;

create index FK53821CD11F45E509
  on fechamento_diario_cota (FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID)
;

create table if not exists fechamento_diario_diferenca
(
  ID bigint auto_increment
    primary key,
  CODIGO_PRODUTO varchar(255) not null,
  DATA_LANCAMENTO date not null,
  ID_PRODUTO_EDICAO bigint not null,
  MOTIVO_APROVACAO varchar(255) null,
  NOME_PRODUTO varchar(255) not null,
  NUMERO_EDICAO bigint not null,
  QTDE_EXEMPLARES decimal(18,4) null,
  STATUS_APROVACAO varchar(255) null,
  TIPO_DIFERENCA varchar(255) not null,
  TOTAL decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FK5F1D69D8CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK5F1D69D8CD5CBB3
  on fechamento_diario_diferenca (FECHAMENTO_DIARIO_ID)
;

create table if not exists fechamento_diario_divida
(
  ID bigint auto_increment
    primary key,
  BANCO varchar(255) null,
  DATA_EMISSAO date null,
  DATA_VENCIMENTO date null,
  ID_DIVIDA bigint null,
  NOME_COTA varchar(255) null,
  NOSSO_NUMERO varchar(255) null,
  NUMERO_CONTA varchar(20) null,
  NUMERO_COTA bigint null,
  FORMA_PAGAMENTO varchar(255) null,
  VALOR decimal(18,4) null,
  FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID bigint not null,
  constraint FK7CCF876D3758FC9
  foreign key (FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID) references fechamento_diario_consolidado_divida (ID)
)
;

create index FK7CCF876D3758FC9
  on fechamento_diario_divida (FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID)
;

create table if not exists fechamento_diario_lancamento_encalhe
(
  ID bigint auto_increment
    primary key,
  QUANTIDADE int null,
  QNT_DIFERENCA int null,
  QNT_VENDA_ENCALHE int null,
  FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID bigint not null,
  ID_PRODUTO_EDICAO bigint not null,
  QNT_LOGICO_JURAMENTADO bigint null,
  QNT_FISICO bigint null,
  constraint FK15EC5355325F0A2B
  foreign key (FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID) references fechamento_diario_consolidado_encalhe (ID)
)
;

create index FK15EC5355325F0A2B
  on fechamento_diario_lancamento_encalhe (FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID)
;

create index FK15EC5355D2FE34B7
  on fechamento_diario_lancamento_encalhe (ID_PRODUTO_EDICAO)
;

create table if not exists fechamento_diario_lancamento_reparte
(
  ID bigint auto_increment
    primary key,
  QNT_A_DISTRIBUIR int null,
  QNT_DIFERENCA int null,
  QNT_DISTRIBUIDO int null,
  QNT_FALTA_EM int null,
  QNT_REPARTE int null,
  QNT_SOBRA_DISTRIBUIDO int null,
  QNT_SOBRA_EM int null,
  QNT_TRANSFERENCIA int null,
  FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID bigint not null,
  ID_PRODUTO_EDICAO bigint not null,
  constraint FKB6F901925614050B
  foreign key (FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID) references fechamento_diario_consolidado_reparte (ID)
)
;

create index FKB6F901925614050B
  on fechamento_diario_lancamento_reparte (FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID)
;

create index FKB6F90192D2FE34B7
  on fechamento_diario_lancamento_reparte (ID_PRODUTO_EDICAO)
;

create table if not exists fechamento_diario_lancamento_suplementar
(
  ID bigint auto_increment
    primary key,
  QNT_CONTABILIZADA decimal(18,4) null,
  QNT_LOGICO bigint null,
  QNT_VENDA bigint null,
  QNT_TRANSFERENCIA_ENTRADA bigint null,
  QNT_TRANSFERENCIA_SAIDA bigint null,
  SALDO bigint null,
  FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID bigint not null,
  ID_PRODUTO_EDICAO bigint not null,
  constraint FK3D4464A9EE73E92B
  foreign key (FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID) references fechamento_diario_consolidado_suplementar (ID)
)
;

create index FK3D4464A9D2FE34B7
  on fechamento_diario_lancamento_suplementar (ID_PRODUTO_EDICAO)
;

create index FK3D4464A9EE73E92B
  on fechamento_diario_lancamento_suplementar (FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID)
;

create table if not exists fechamento_diario_movimento_vendas_encalhe
(
  ID bigint not null
    primary key,
  DATA_VENCIMENTO date null,
  QUANTIDADE decimal(18,4) null,
  VALOR decimal(18,4) null,
  ID_PRODUTO_EDICAO bigint not null,
  FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID bigint null,
  constraint FKB6674115325F0A2B
  foreign key (FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID) references fechamento_diario_consolidado_encalhe (ID)
)
;

create index FK35E76390D2FE34B7b6674115
  on fechamento_diario_movimento_vendas_encalhe (ID_PRODUTO_EDICAO)
;

create index FKB6674115325F0A2B
  on fechamento_diario_movimento_vendas_encalhe (FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID)
;

create table if not exists fechamento_diario_movimento_vendas_suplementar
(
  ID bigint not null
    primary key,
  DATA_VENCIMENTO date null,
  QUANTIDADE decimal(18,4) null,
  VALOR decimal(18,4) null,
  ID_PRODUTO_EDICAO bigint not null,
  FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID bigint null,
  constraint FK27127269EE73E92B
  foreign key (FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID) references fechamento_diario_consolidado_suplementar (ID)
)
;

create index FK27127269EE73E92B
  on fechamento_diario_movimento_vendas_suplementar (FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID)
;

create index FK35E76390D2FE34B727127269
  on fechamento_diario_movimento_vendas_suplementar (ID_PRODUTO_EDICAO)
;

create table if not exists fechamento_diario_resumo_consignado
(
  TIPO_RESUMO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  SALDO_ANTERIOR decimal(18,4) null,
  SALDO_ATUAL decimal(18,4) null,
  VALOR_ENTRADA decimal(18,4) null,
  VALOR_SAIDA decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  VALOR_CE decimal(18,4) null,
  VALOR_OUTRAS_MOVIMENTACOES_ENTRADA decimal(18,4) null,
  VALOR_EXPEDICAO decimal(18,4) null,
  VALOR_OUTRAS_MOVIMENTACOES_SAIDA decimal(18,4) null,
  VALOR_OUTRAS_MOVIMENTACOES_A_VISTA decimal(18,4) null,
  VALOR_ALTERACAO_PRECO decimal(18,4) default '0.0000' null,
  constraint FK29E6791D8CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK29E6791D8CD5CBB3
  on fechamento_diario_resumo_consignado (FECHAMENTO_DIARIO_ID)
;

create table if not exists fechamento_diario_resumo_consolidado_divida
(
  ID bigint auto_increment
    primary key,
  FORMA_PAGAMENTO varchar(255) null,
  VALOR_INADIMPLENTE decimal(18,4) null,
  VALOR_PAGO decimal(18,4) null,
  VALOR_TOTAL decimal(18,4) null,
  FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID bigint not null,
  constraint FK806BF1C13758FC9
  foreign key (FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID) references fechamento_diario_consolidado_divida (ID)
)
;

create index FK806BF1C13758FC9
  on fechamento_diario_resumo_consolidado_divida (FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID)
;

create table if not exists fechamento_diario_resumo_estoque
(
  ID bigint auto_increment
    primary key,
  QNT_EXEMPLARES int null,
  QNT_PRODUTO int null,
  TIPO_ESTOQUE varchar(255) null,
  VALOR_TOTAL decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FKC8BE9488CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FKC8BE9488CD5CBB3
  on fechamento_diario_resumo_estoque (FECHAMENTO_DIARIO_ID)
;

create table if not exists fechamento_encalhe
(
  DATA_ENCALHE date not null,
  QUANTIDADE bigint null,
  PRODUTO_EDICAO_ID bigint not null,
  primary key (DATA_ENCALHE, PRODUTO_EDICAO_ID)
)
;

create index FK9496A657A53173D3
  on fechamento_encalhe (PRODUTO_EDICAO_ID)
;

create table if not exists fechamento_encalhe_box
(
  QUANTIDADE bigint null,
  DATA_ENCALHE date not null,
  PRODUTO_EDICAO_ID bigint not null,
  BOX_ID bigint not null,
  primary key (BOX_ID, DATA_ENCALHE, PRODUTO_EDICAO_ID),
  constraint FK81C9D3C317B54D97
  foreign key (DATA_ENCALHE, PRODUTO_EDICAO_ID) references fechamento_encalhe (DATA_ENCALHE, PRODUTO_EDICAO_ID),
  constraint FK81C9D3C3BA6EBE40
  foreign key (BOX_ID) references box (ID)
)
;

create index FK81C9D3C317B54D97
  on fechamento_encalhe_box (DATA_ENCALHE, PRODUTO_EDICAO_ID)
;

create index FK81C9D3C3BA6EBE40
  on fechamento_encalhe_box (BOX_ID)
;

create table if not exists fechamentodiariomovimentovenda
(
  ID bigint not null
    primary key,
  DATA_VENCIMENTO date null,
  QUANTIDADE decimal(18,4) null,
  VALOR decimal(18,4) null,
  ID_PRODUTO_EDICAO bigint not null
)
;

create index FK35E76390D2FE34B7
  on fechamentodiariomovimentovenda (ID_PRODUTO_EDICAO)
;

create table if not exists feriado
(
  ID bigint auto_increment
    primary key,
  DATA date not null,
  DESCRICAO varchar(255) not null,
  IND_EFETUA_COBRANCA tinyint(1) null,
  IND_OPERA tinyint(1) null,
  IND_REPETE_ANUALMENTE tinyint(1) null,
  ORIGEM varchar(255) null,
  TIPO_FERIADO varchar(255) null,
  LOCALIDADE_ID bigint null,
  UFE_SG varchar(4) null,
  LOCALIDADE varchar(255) null,
  constraint DATA
  unique (DATA, LOCALIDADE_ID, UFE_SG, TIPO_FERIADO)
)
;

create index FKF15849969BB6EC08
  on feriado (LOCALIDADE_ID)
;

create index FKF1584996B31F2A81
  on feriado (UFE_SG)
;

create table if not exists fiador
(
  ID bigint auto_increment
    primary key,
  INICIO_ATIVIDADE date null,
  PESSOA_ID bigint not null,
  constraint PESSOA_ID
  unique (PESSOA_ID)
)
;

create index FK7B9684899B8CB634
  on fiador (PESSOA_ID)
;

alter table cota
  add constraint FK1FA7D9DC6BE690
foreign key (ID_FIADOR) references fiador (ID)
;

alter table cota_garantia
  add constraint FK80C0DF0D8DC372F4
foreign key (FIADOR_ID) references fiador (ID)
;

alter table endereco_fiador
  add constraint FKF9DF85BF8DC372F4
foreign key (FIADOR_ID) references fiador (ID)
;

create table if not exists fiador_socio
(
  FIADOR_ID bigint not null,
  SOCIO_ID bigint not null,
  constraint FK9FAC29978DC372F4
  foreign key (FIADOR_ID) references fiador (ID)
)
;

create index FK9FAC29977CB7F32E
  on fiador_socio (SOCIO_ID)
;

create index FK9FAC29978DC372F4
  on fiador_socio (FIADOR_ID)
;

create table if not exists fixacao_reparte
(
  ID bigint auto_increment
    primary key,
  DATA_HORA datetime null,
  ED_FINAL int null,
  ED_INICIAL int null,
  ED_ATENDIDAS int null,
  QTDE_EDICOES int null,
  QTDE_EXEMPLARES int null,
  ID_COTA bigint null,
  ID_PRODUTO bigint null,
  ID_USUARIO bigint null,
  CODIGO_ICD varchar(255) null,
  MANTER_FIXA tinyint(1) null,
  ID_CLASSIFICACAO_EDICAO bigint null,
  DATA_FIXA_CADASTRO_FIXACAO date null,
  CODIGO_PRODUTO varchar(255) null,
  USAR_ICD_ESTUDO tinyint(1) default '1' not null,
  constraint FKA394AF65F1916CB0
  foreign key (ID_COTA) references cota (ID)
)
;

create index FKA394AF65917D3254
  on fixacao_reparte (ID_CLASSIFICACAO_EDICAO)
;

create index FKA394AF6593864D4C
  on fixacao_reparte (ID_USUARIO)
;

create index FKA394AF65F1916CB0
  on fixacao_reparte (ID_COTA)
;

create index FKA394AF65F34F8834
  on fixacao_reparte (ID_PRODUTO)
;

create table if not exists fixacao_reparte_pdv
(
  ID bigint auto_increment
    primary key,
  REPARTE int null,
  ID_FIXACAO_REPARTE bigint not null,
  ID_PDV bigint not null,
  constraint FK6160D9083AE26156
  foreign key (ID_FIXACAO_REPARTE) references fixacao_reparte (ID)
)
;

create index FK6160D9083AE26156
  on fixacao_reparte_pdv (ID_FIXACAO_REPARTE)
;

create index FK6160D9089A5F4F5A
  on fixacao_reparte_pdv (ID_PDV)
;

create table if not exists flag_pendente_ativacao
(
  id bigint auto_increment
    primary key,
  flag varchar(45) not null,
  descricao varchar(45) not null,
  valor tinyint(1) not null,
  tipo varchar(45) not null,
  id_alterado bigint null,
  data_alteracao timestamp default CURRENT_TIMESTAMP not null,
  constraint NOME_TIPO_ID_ITEM_UQ
  unique (flag, tipo, id_alterado)
)
;

create table if not exists forma_cobranca
(
  id bigint auto_increment
    primary key,
  ATIVA tinyint(1) not null,
  AGENCIA_BANCO bigint null,
  CONTA_BANCO bigint null,
  DV_AGENCIA_BANCO varchar(255) null,
  DV_CONTA_BANCO varchar(255) null,
  NOME_BANCO varchar(255) null,
  CORRENTISTA varchar(255) null,
  NUMERO_BANCO varchar(255) null,
  FORMA_COBRANCA_BOLETO int null,
  INSTRUCOES varchar(255) null,
  PRINCIPAL tinyint(1) not null,
  RECEBE_COBRANCA_EMAIL tinyint(1) null,
  TAXA_JUROS_MENSAL decimal(18,4) null,
  TAXA_MULTA decimal(18,4) null,
  TIPO_COBRANCA varchar(255) not null,
  TIPO_FORMA_COBRANCA varchar(255) not null,
  VENCIMENTO_DIA_UTIL tinyint(1) not null,
  BANCO_ID bigint null,
  PARAMETRO_COBRANCA_COTA_ID bigint null,
  VALOR_MULTA decimal(18,4) null,
  QTD_DIAS_PROTESTO bigint null,
  PROTESTAR_BOLETO_REGISTRADO tinyint(1) default '0' null,
  constraint FKA10CEB09E44516C0
  foreign key (BANCO_ID) references banco (ID)
)
;

create index FKA10CEB0961171A8E
  on forma_cobranca (PARAMETRO_COBRANCA_COTA_ID)
;

create index FKA10CEB09E44516C0
  on forma_cobranca (BANCO_ID)
;

alter table concentracao_cobranca_cota
  add constraint FK53392B6AE34F875B
foreign key (FORMA_COBRANCA_ID) references forma_cobranca (id)
;

create table if not exists forma_cobranca_caucao_liquida
(
  id bigint auto_increment
    primary key,
  TIPO_FORMA_COBRANCA varchar(255) not null
)
;

alter table concentracao_cobranca_caucao_liquida
  add constraint FK20DE4A31EA41BD85
foreign key (FORMA_COBRANCA_CAUCAO_LIQUIDA_ID) references forma_cobranca_caucao_liquida (id)
;

create table if not exists forma_cobranca_fornecedor
(
  FORMA_COBRANCA_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  primary key (FORMA_COBRANCA_ID, FORNECEDOR_ID),
  constraint FK8367E85BE34F875B
  foreign key (FORMA_COBRANCA_ID) references forma_cobranca (id)
)
;

create index FK8367E85B9808F874
  on forma_cobranca_fornecedor (FORNECEDOR_ID)
;

create index FK8367E85BE34F875B
  on forma_cobranca_fornecedor (FORMA_COBRANCA_ID)
;

create table if not exists formacobranca_diasdomes
(
  FormaCobranca_id bigint not null,
  diasDoMes int null,
  constraint FK6F669BE6ACA9040
  foreign key (FormaCobranca_id) references forma_cobranca (id)
)
;

create index FK6F669BE6ACA9040
  on formacobranca_diasdomes (FormaCobranca_id)
;

create table if not exists formacobrancacaucaoliquida_diasdomes
(
  FormaCobrancaCaucaoLiquida_id bigint not null,
  diasDoMes int null,
  constraint FK365C0CC1B530ACD4
  foreign key (FormaCobrancaCaucaoLiquida_id) references forma_cobranca_caucao_liquida (id)
)
;

create index FK365C0CC1B530ACD4
  on formacobrancacaucaoliquida_diasdomes (FormaCobrancaCaucaoLiquida_id)
;

create table if not exists fornecedor
(
  ID bigint auto_increment
    primary key,
  COD_INTERFACE int null,
  EMAIL_NFE varchar(255) null,
  INICIO_ATIVIDADE date not null,
  MARGEM_DISTRIBUIDOR decimal(18,4) default '0.0000' not null,
  ORIGEM varchar(255) not null,
  POSSUI_CONTRATO tinyint(1) null,
  RESPONSAVEL varchar(255) null,
  SITUACAO_CADASTRO varchar(255) not null,
  TIPO_CONTRATO varchar(255) null,
  VALIDADE_CONTRATO datetime null,
  JURIDICA_ID bigint not null,
  TIPO_FORNECEDOR_ID bigint null,
  BANCO_ID bigint null,
  DESCONTO_ID bigint null,
  CANAL_DISTRIBUICAO varchar(30) null,
  FORNECEDOR_UNIFICADOR_ID bigint null,
  INTEGRA_GFS tinyint(1) default '0' null,
  DESTINACAO_ENCALHE tinyint(1) default '0' null,
  constraint JURIDICA_ID
  unique (JURIDICA_ID),
  constraint FK6B088D65E44516C0
  foreign key (BANCO_ID) references banco (ID),
  constraint FK6B088D6529E8DFE3
  foreign key (DESCONTO_ID) references desconto (ID),
  constraint FKFORNECEDORUNIFICADOR
  foreign key (FORNECEDOR_UNIFICADOR_ID) references fornecedor (ID)
)
;

create index FK6B088D6529E8DFE3
  on fornecedor (DESCONTO_ID)
;

create index FK6B088D65440433FD
  on fornecedor (TIPO_FORNECEDOR_ID)
;

create index FK6B088D659218183B
  on fornecedor (JURIDICA_ID)
;

create index FK6B088D65E44516C0
  on fornecedor (BANCO_ID)
;

create index FKFORNECEDORUNIFICADOR_idx
  on fornecedor (FORNECEDOR_UNIFICADOR_ID)
;

alter table boleto_distribuidor
  add constraint FK4BA253AC9808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table chamada_encalhe_fornecedor
  add constraint FKE41EDD949808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table cobranca
  add constraint FKF83D21079808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table cobranca_centralizacao
  add constraint FKF83D21079808F877
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table cota_fornecedor
  add constraint FK1ADE098B9808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table desconto_cota_fornecedor
  add constraint FK325007F79808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table desconto_cota_produto_excessoes
  add constraint FKD845FEF09808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table desconto_distribuidor_fornecedor
  add constraint FKAC9219029808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table desconto_logistica
  add constraint desconto_logistica_ibfk_1
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table desconto_produto_edicao
  add constraint FK4899C4219808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table distribuicao_fornecedor
  add constraint FK1F0231CC9808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table editor_fornecedor
  add constraint FK221C17779808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table endereco_fornecedor
  add constraint FKADE2039B9808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

alter table forma_cobranca_fornecedor
  add constraint FK8367E85B9808F874
foreign key (FORNECEDOR_ID) references fornecedor (ID)
;

create table if not exists furo_produto
(
  ID bigint auto_increment
    primary key,
  DATA datetime not null,
  LANCAMENTO_ID bigint null,
  PRODUTO_EDICAO_ID bigint not null,
  USUARIO_ID bigint not null,
  DATA_LCTO_DISTRIBUIDOR date null
)
;

create index FK301010E645C07ACF
  on furo_produto (LANCAMENTO_ID)
;

create index FK301010E67FFF790E
  on furo_produto (USUARIO_ID)
;

create index FK301010E6A53173D3
  on furo_produto (PRODUTO_EDICAO_ID)
;

create index NDX_DTA_LANCTO
  on furo_produto (DATA_LCTO_DISTRIBUIDOR)
;

create table if not exists garantia
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(255) null,
  VALOR decimal(18,4) null,
  ID_FIADOR bigint not null,
  constraint FKCD8E9167DC6BE690
  foreign key (ID_FIADOR) references fiador (ID)
)
;

create index FKCD8E9167DC6BE690
  on garantia (ID_FIADOR)
;

create table if not exists garantia_cota_outros
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(255) null,
  DATA_VALIDADE datetime null,
  VALOR decimal(18,4) null,
  GARANTIA_ID bigint not null,
  constraint FK7F1AA0168FCB49C8
  foreign key (GARANTIA_ID) references cota_garantia (ID)
)
;

create index FK7F1AA0168FCB49C8
  on garantia_cota_outros (GARANTIA_ID)
;

create table if not exists gerador_fluxo_pdv
(
  ID bigint auto_increment
    primary key,
  PDV_ID bigint not null,
  TIPO_GERADOR_FLUXO_ID bigint null,
  constraint PDV_ID
  unique (PDV_ID),
  constraint PDV_ID_2
  unique (PDV_ID)
)
;

create index FKECD90D241656FEE0
  on gerador_fluxo_pdv (TIPO_GERADOR_FLUXO_ID)
;

create index FKECD90D24A65B70F4
  on gerador_fluxo_pdv (PDV_ID)
;

create table if not exists gerador_fluxo_pdv_tipo_gerador_fluxo_pdv
(
  GERADOR_FLUXO_PDV_ID bigint not null,
  TIPO_GERADOR_FLUXO_PDV_ID bigint not null,
  primary key (GERADOR_FLUXO_PDV_ID, TIPO_GERADOR_FLUXO_PDV_ID),
  constraint FK308AA654A4EA0566
  foreign key (GERADOR_FLUXO_PDV_ID) references gerador_fluxo_pdv (ID)
)
;

create index FK308AA654A4EA0566
  on gerador_fluxo_pdv_tipo_gerador_fluxo_pdv (GERADOR_FLUXO_PDV_ID)
;

create index FK308AA654FA255EBD
  on gerador_fluxo_pdv_tipo_gerador_fluxo_pdv (TIPO_GERADOR_FLUXO_PDV_ID)
;

create table if not exists grupo_cota
(
  ID bigint auto_increment
    primary key,
  NOME varchar(255) null,
  TIPO_COTA varchar(255) null,
  TIPO_GRUPO varchar(255) null,
  DATA_VIGENCIA_INICIO date not null,
  DATA_VIGENCIA_FIM date null
)
;

alter table cota_grupo
  add constraint FKDADA18631E8AD533
foreign key (GRUPO_COTA_ID) references grupo_cota (ID)
;

alter table dia_recolhimento_grupo_cota
  add constraint FK90BC5188E6F63479
foreign key (GRUPO_ID) references grupo_cota (ID)
;

create table if not exists grupo_municipio
(
  GRUPO_COTA_ID bigint not null,
  LOCALIDADE varchar(255) not null,
  constraint FK2CA1AF571E8AD533
  foreign key (GRUPO_COTA_ID) references grupo_cota (ID)
)
;

create index FK2CA1AF571E8AD533
  on grupo_municipio (GRUPO_COTA_ID)
;

create table if not exists grupo_permissao
(
  ID bigint auto_increment
    primary key,
  NOME varchar(255) not null
)
;

create table if not exists grupo_permissao_permissao
(
  PERMISSAO_GRUPO_ID bigint not null,
  PERMISSAO_ID varchar(255) null,
  constraint FK618A7B99415856F3
  foreign key (PERMISSAO_GRUPO_ID) references grupo_permissao (ID)
)
;

create index FK618A7B99415856F3
  on grupo_permissao_permissao (PERMISSAO_GRUPO_ID)
;

create table if not exists grupo_tributo
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(60) null
)
;

create table if not exists grupo_tributo_aliquota
(
  TRIBUTO_ID bigint not null,
  ALIQUOTA_ID bigint not null,
  primary key (TRIBUTO_ID, ALIQUOTA_ID)
)
;

create index FK_grupo_tributo_aliquota_1
  on grupo_tributo_aliquota (ALIQUOTA_ID)
;

create table if not exists hibernate_sequences
(
  sequence_name varchar(255) null,
  sequence_next_hi_value int null
)
;

create table if not exists historico_acumulo_divida
(
  ID bigint auto_increment
    primary key,
  DATA_INCLUSAO datetime not null,
  STATUS varchar(255) not null,
  DIVIDA_ID bigint not null,
  USUARIO_ID bigint not null,
  constraint FKA75B40057C42C4C1
  foreign key (DIVIDA_ID) references divida (ID)
)
;

create index FKA75B40057C42C4C1
  on historico_acumulo_divida (DIVIDA_ID)
;

create index FKA75B40057FFF790E
  on historico_acumulo_divida (USUARIO_ID)
;

create table if not exists historico_alteracao_preco_venda
(
  id bigint auto_increment
    primary key,
  DATA_CRIACAO timestamp default CURRENT_TIMESTAMP not null,
  DATA_OPERACAO date not null,
  PRODUTO_EDICAO_ID bigint not null,
  VALOR_ANTIGO decimal(18,4) not null,
  VALOR_ATUAL decimal(18,4) not null,
  USUARIO_ID bigint not null,
  TIPO_ALTERACAO varchar(20) null,
  COTA_ID bigint null
)
;

create index fk_HISTORICO_ALTERACAO_PRECO_VENDA_PRODUTO_EDICAO_idx
  on historico_alteracao_preco_venda (PRODUTO_EDICAO_ID)
;

create index fk_HISTORICO_ALTERACAO_PRECO_VENDA_USUARIO_idx
  on historico_alteracao_preco_venda (USUARIO_ID)
;

create table if not exists historico_conf_chamada_encalhe_cota
(
  ID bigint auto_increment
    primary key,
  DATA_INICIO datetime not null,
  DATA_FIM datetime not null,
  USUARIO_ID bigint not null,
  COTA_ID bigint not null,
  DATA_OPERACAO date not null,
  BOX_ID bigint not null,
  VALOR_ENCALHE decimal(18,4) not null
)
;

create table if not exists historico_desconto_cota_produto_excessoes
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  VALOR decimal(18,4) null,
  COTA_ID bigint not null,
  DESCONTO_ID bigint not null,
  EDITOR_ID bigint null,
  DISTRIBUIDOR_ID bigint not null,
  FORNECEDOR_ID bigint null,
  PRODUTO_ID bigint null,
  PRODUTO_EDICAO_ID bigint null,
  USUARIO_ID bigint not null,
  constraint FK56EB6281C8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK56EB628129E8DFE3
  foreign key (DESCONTO_ID) references desconto (ID),
  constraint FK56EB628156501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID),
  constraint FK56EB62819808F874
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index FK56EB628129E8DFE3
  on historico_desconto_cota_produto_excessoes (DESCONTO_ID)
;

create index FK56EB628156501954
  on historico_desconto_cota_produto_excessoes (DISTRIBUIDOR_ID)
;

create index FK56EB62817FFF790E
  on historico_desconto_cota_produto_excessoes (USUARIO_ID)
;

create index FK56EB62819808F874
  on historico_desconto_cota_produto_excessoes (FORNECEDOR_ID)
;

create index FK56EB6281A53173D3
  on historico_desconto_cota_produto_excessoes (PRODUTO_EDICAO_ID)
;

create index FK56EB6281C5C16480
  on historico_desconto_cota_produto_excessoes (PRODUTO_ID)
;

create index FK56EB6281C8181F74
  on historico_desconto_cota_produto_excessoes (COTA_ID)
;

create table if not exists historico_desconto_produto_edicoes
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  VALOR decimal(18,4) null,
  DESCONTO_ID bigint not null,
  DISTRIBUIDOR_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  PRODUTO_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  USUARIO_ID bigint not null,
  constraint FK9EC9013929E8DFE3
  foreign key (DESCONTO_ID) references desconto (ID),
  constraint FK9EC9013956501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID),
  constraint FK9EC901399808F874
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index FK9EC9013929E8DFE3
  on historico_desconto_produto_edicoes (DESCONTO_ID)
;

create index FK9EC9013956501954
  on historico_desconto_produto_edicoes (DISTRIBUIDOR_ID)
;

create index FK9EC901397FFF790E
  on historico_desconto_produto_edicoes (USUARIO_ID)
;

create index FK9EC901399808F874
  on historico_desconto_produto_edicoes (FORNECEDOR_ID)
;

create index FK9EC90139A53173D3
  on historico_desconto_produto_edicoes (PRODUTO_EDICAO_ID)
;

create index FK9EC90139C5C16480
  on historico_desconto_produto_edicoes (PRODUTO_ID)
;

create table if not exists historico_desconto_produtos
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  VALOR decimal(18,4) null,
  DESCONTO_ID bigint not null,
  DISTRIBUIDOR_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  PRODUTO_ID bigint not null,
  USUARIO_ID bigint not null,
  constraint FK6FDFA8FF29E8DFE3
  foreign key (DESCONTO_ID) references desconto (ID),
  constraint FK6FDFA8FF56501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID),
  constraint FK6FDFA8FF9808F874
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index FK6FDFA8FF29E8DFE3
  on historico_desconto_produtos (DESCONTO_ID)
;

create index FK6FDFA8FF56501954
  on historico_desconto_produtos (DISTRIBUIDOR_ID)
;

create index FK6FDFA8FF7FFF790E
  on historico_desconto_produtos (USUARIO_ID)
;

create index FK6FDFA8FF9808F874
  on historico_desconto_produtos (FORNECEDOR_ID)
;

create index FK6FDFA8FFC5C16480
  on historico_desconto_produtos (PRODUTO_ID)
;

create table if not exists historico_descontos_editores
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  DESCONTO_ID bigint not null,
  DISTRIBUIDOR_ID bigint not null,
  EDITOR_ID bigint not null,
  USUARIO_ID bigint not null,
  constraint fk_historico_descontos_editores_1
  foreign key (DESCONTO_ID) references desconto (ID),
  constraint fk_historico_descontos_editores_4
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID),
  constraint fk_historico_descontos_editores_2
  foreign key (EDITOR_ID) references editor (ID)
)
;

create index fk_historico_descontos_editores_1_idx
  on historico_descontos_editores (DESCONTO_ID)
;

create index fk_historico_descontos_editores_2_idx
  on historico_descontos_editores (EDITOR_ID)
;

create index fk_historico_descontos_editores_3_idx
  on historico_descontos_editores (USUARIO_ID)
;

create index fk_historico_descontos_editores_4_idx
  on historico_descontos_editores (DISTRIBUIDOR_ID)
;

create table if not exists historico_descontos_fornecedores
(
  ID bigint auto_increment
    primary key,
  DATA_ALTERACAO datetime null,
  DESCONTO_ID bigint not null,
  DISTRIBUIDOR_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  USUARIO_ID bigint not null,
  constraint FKB4E995F929E8DFE3
  foreign key (DESCONTO_ID) references desconto (ID),
  constraint FKB4E995F956501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID),
  constraint FKB4E995F99808F874
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index FKB4E995F929E8DFE3
  on historico_descontos_fornecedores (DESCONTO_ID)
;

create index FKB4E995F956501954
  on historico_descontos_fornecedores (DISTRIBUIDOR_ID)
;

create index FKB4E995F97FFF790E
  on historico_descontos_fornecedores (USUARIO_ID)
;

create index FKB4E995F99808F874
  on historico_descontos_fornecedores (FORNECEDOR_ID)
;

create table if not exists historico_estoque_produto
(
  ID bigint auto_increment
    primary key,
  DATA date not null,
  QTDE decimal(18,4) null,
  QTDE_DANIFICADO decimal(18,4) null,
  QTDE_DEVOLUCAO_ENCALHE decimal(18,4) null,
  QTDE_DEVOLUCAO_FORNECEDOR decimal(18,4) null,
  QTDE_JURAMENTADO decimal(18,4) null,
  QTDE_SUPLEMENTAR decimal(18,4) null,
  VERSAO bigint null,
  PRODUTO_EDICAO_ID bigint not null,
  constraint DATA
  unique (DATA, PRODUTO_EDICAO_ID)
)
;

create index FKEC4EDD83A53173D3
  on historico_estoque_produto (PRODUTO_EDICAO_ID)
;

create table if not exists historico_fechamento_diario_consolidado_cota
(
  ID bigint auto_increment
    primary key,
  QNT_ATIVOS decimal(18,4) null,
  QNT_AUSENTE_ENCALHE decimal(18,4) null,
  QNT_AUSENTE_REPARTE decimal(18,4) null,
  QNT_INATIVAS decimal(18,4) null,
  QNT_NOVOS decimal(18,4) null,
  QNT_TOTAL decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FK310500DC8CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK310500DC8CD5CBB3
  on historico_fechamento_diario_consolidado_cota (FECHAMENTO_DIARIO_ID)
;

create table if not exists historico_fechamento_diario_consolidado_divida
(
  ID bigint auto_increment
    primary key,
  TIPO_DIVIDA varchar(255) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FK52994B88CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK52994B88CD5CBB3
  on historico_fechamento_diario_consolidado_divida (FECHAMENTO_DIARIO_ID)
;

create table if not exists historico_fechamento_diario_consolidado_encalhe
(
  ID bigint auto_increment
    primary key,
  SALDO decimal(18,4) null,
  VALOR_FALTA_EM decimal(18,4) null,
  VALOR_FISICO decimal(18,4) null,
  VALOR_JURAMENTADO decimal(18,4) null,
  VALOR_LOGICO decimal(18,4) null,
  VALOR_SOBRA_EM decimal(18,4) null,
  VALOR_VENDA decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FECHAMENTO_DIARIO_ID
  unique (FECHAMENTO_DIARIO_ID),
  constraint FKDC68363F8CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FKDC68363F8CD5CBB3
  on historico_fechamento_diario_consolidado_encalhe (FECHAMENTO_DIARIO_ID)
;

create table if not exists historico_fechamento_diario_consolidado_reparte
(
  ID bigint auto_increment
    primary key,
  VALOR_DIFERENCA decimal(18,4) null,
  VALOR_DISTRIBUIDO decimal(18,4) null,
  VALOR_FALTAS decimal(18,4) null,
  VALOR_REPARTE decimal(18,4) null,
  SOBRA_DISTRIBUIDA decimal(18,4) null,
  VALOR_SOBRAS decimal(18,4) null,
  VALOR_TRANSFERIDO decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FECHAMENTO_DIARIO_ID
  unique (FECHAMENTO_DIARIO_ID),
  constraint FK7D74E47C8CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK7D74E47C8CD5CBB3
  on historico_fechamento_diario_consolidado_reparte (FECHAMENTO_DIARIO_ID)
;

create table if not exists historico_fechamento_diario_consolidado_suplementar
(
  ID bigint auto_increment
    primary key,
  VALOR_ESTOQUE_LOGICO decimal(18,4) null,
  VALOR_SALDO decimal(18,4) null,
  VALOR_TRANSFERENCIA decimal(18,4) null,
  VALOR_VENDAS decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  valor_inventario decimal(18,4) null,
  constraint FECHAMENTO_DIARIO_ID
  unique (FECHAMENTO_DIARIO_ID),
  constraint FKCBE0C2938CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FKCBE0C2938CD5CBB3
  on historico_fechamento_diario_consolidado_suplementar (FECHAMENTO_DIARIO_ID)
;

create table if not exists historico_fechamento_diario_cota
(
  ID bigint auto_increment
    primary key,
  NOME_COTA varchar(255) null,
  NUMERO_COTA int null,
  TIPO_DETALHE_COTA varchar(255) null,
  HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID bigint not null,
  constraint FK76A04220C9F103CA
  foreign key (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID) references historico_fechamento_diario_consolidado_cota (ID)
)
;

create index FK76A04220C9F103CA
  on historico_fechamento_diario_cota (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID)
;

create table if not exists historico_fechamento_diario_divida
(
  ID bigint auto_increment
    primary key,
  BANCO varchar(255) null,
  DATA_VENCIMENTO date null,
  ID_DIVIDA bigint null,
  NOME_COTA varchar(255) null,
  NOSSO_NUMERO varchar(255) null,
  NUMERO_CONTA int null,
  NUMERO_COTA bigint null,
  FORMA_PAGAMENTO varchar(255) null,
  VALOR decimal(18,4) null,
  HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID bigint not null,
  constraint FK50F994FCAFD4024A
  foreign key (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID) references historico_fechamento_diario_consolidado_divida (ID)
)
;

create index FK50F994FCAFD4024A
  on historico_fechamento_diario_divida (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID)
;

create table if not exists historico_fechamento_diario_lancamento_encalhe
(
  ID bigint auto_increment
    primary key,
  QUANTIDADE decimal(18,4) null,
  QNT_DIFERENCA decimal(18,4) null,
  QNT_VENDA_ENCALHE decimal(18,4) null,
  HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID bigint not null,
  ID_PRODUTO_EDICAO bigint not null,
  constraint FKDD9B826411CEE7CA
  foreign key (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID) references historico_fechamento_diario_consolidado_encalhe (ID)
)
;

create index FKDD9B826411CEE7CA
  on historico_fechamento_diario_lancamento_encalhe (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID)
;

create index FKDD9B8264D2FE34B7
  on historico_fechamento_diario_lancamento_encalhe (ID_PRODUTO_EDICAO)
;

create table if not exists historico_fechamento_diario_lancamento_reparte
(
  ID bigint auto_increment
    primary key,
  QNT_A_DISTRIBUIR decimal(18,4) null,
  QNT_DIFERENCA decimal(18,4) null,
  QNT_DISTRIBUIDO decimal(18,4) null,
  QNT_FALTA_EM decimal(18,4) null,
  QNT_REPARTE decimal(18,4) null,
  QNT_SOBRA_DISTRIBUIDO decimal(18,4) null,
  QNT_SOBRA_EM decimal(18,4) null,
  QNT_TRANSFERENCIA decimal(18,4) null,
  HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID bigint not null,
  ID_PRODUTO_EDICAO bigint not null,
  constraint FK7EA830A13583E2AA
  foreign key (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID) references historico_fechamento_diario_consolidado_reparte (ID)
)
;

create index FK7EA830A13583E2AA
  on historico_fechamento_diario_lancamento_reparte (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID)
;

create index FK7EA830A1D2FE34B7
  on historico_fechamento_diario_lancamento_reparte (ID_PRODUTO_EDICAO)
;

create table if not exists historico_fechamento_diario_lancamento_suplementar
(
  ID bigint auto_increment
    primary key,
  QNT_CONTABILIZADA decimal(18,4) null,
  QNT_DIFERENCA decimal(18,4) null,
  QNT_FISICO decimal(18,4) null,
  HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID bigint not null,
  ID_PRODUTO_EDICAO bigint not null,
  constraint FK2C977438B16DDF4A
  foreign key (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID) references historico_fechamento_diario_consolidado_suplementar (ID)
)
;

create index FK2C977438B16DDF4A
  on historico_fechamento_diario_lancamento_suplementar (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID)
;

create index FK2C977438D2FE34B7
  on historico_fechamento_diario_lancamento_suplementar (ID_PRODUTO_EDICAO)
;

create table if not exists historico_fechamento_diario_movimento_vendas_encalhe
(
  ID bigint not null
    primary key,
  DATA_VENCIMENTO date null,
  QUANTIDADE decimal(18,4) null,
  VALOR decimal(18,4) null,
  ID_PRODUTO_EDICAO bigint not null,
  HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID bigint null,
  constraint FK1D34A8E411CEE7CA
  foreign key (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID) references historico_fechamento_diario_consolidado_encalhe (ID)
)
;

create index FK1D34A8E411CEE7CA
  on historico_fechamento_diario_movimento_vendas_encalhe (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID)
;

create index FK534CD760D2FE34B71d34a8e4
  on historico_fechamento_diario_movimento_vendas_encalhe (ID_PRODUTO_EDICAO)
;

create table if not exists historico_fechamento_diario_movimento_vendas_suplementar
(
  ID bigint not null
    primary key,
  DATA_VENCIMENTO date null,
  QUANTIDADE decimal(18,4) null,
  VALOR decimal(18,4) null,
  ID_PRODUTO_EDICAO bigint not null,
  HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID bigint null,
  constraint FK16395AB8B16DDF4A
  foreign key (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID) references historico_fechamento_diario_consolidado_suplementar (ID)
)
;

create index FK16395AB8B16DDF4A
  on historico_fechamento_diario_movimento_vendas_suplementar (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID)
;

create index FK534CD760D2FE34B716395ab8
  on historico_fechamento_diario_movimento_vendas_suplementar (ID_PRODUTO_EDICAO)
;

create table if not exists historico_fechamento_diario_resumo_consignado
(
  ID bigint auto_increment
    primary key,
  TIPO_VALOR varchar(255) null,
  VALOR_AVISTA decimal(18,4) null,
  VALOR_CONSIGNADO decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FK491DAC2E8CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK491DAC2E8CD5CBB3
  on historico_fechamento_diario_resumo_consignado (FECHAMENTO_DIARIO_ID)
;

create table if not exists historico_fechamento_diario_resumo_consolidado_divida
(
  ID bigint auto_increment
    primary key,
  FORMA_PAGAMENTO varchar(255) null,
  VALOR_INADIMPLENTE decimal(18,4) null,
  VALOR_PAGO decimal(18,4) null,
  VALOR_TOTAL decimal(18,4) null,
  HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID bigint not null,
  constraint FKF34B83D2AFD4024A
  foreign key (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID) references historico_fechamento_diario_consolidado_divida (ID)
)
;

create index FKF34B83D2AFD4024A
  on historico_fechamento_diario_resumo_consolidado_divida (HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID)
;

create table if not exists historico_fechamento_diario_resumo_estoque
(
  ID bigint auto_increment
    primary key,
  QNT_EXEMPLARES decimal(18,4) null,
  QNT_PRODUTO decimal(18,4) null,
  TIPO_ESTOQUE varchar(255) null,
  VALOR_TOTAL decimal(18,4) null,
  FECHAMENTO_DIARIO_ID bigint not null,
  constraint FK6292F7D78CD5CBB3
  foreign key (FECHAMENTO_DIARIO_ID) references fechamento_diario (ID)
)
;

create index FK6292F7D78CD5CBB3
  on historico_fechamento_diario_resumo_estoque (FECHAMENTO_DIARIO_ID)
;

create table if not exists historico_lancamento
(
  ID bigint auto_increment
    primary key,
  DATA_EDICAO datetime not null,
  TIPO_EDICAO varchar(255) not null,
  STATUS_NOVO varchar(255) not null,
  USUARIO_ID bigint null,
  LANCAMENTO_ID bigint not null,
  STATUS_ANTIGO varchar(255) null
)
;

create index FKF4FBF54945C07ACF
  on historico_lancamento (LANCAMENTO_ID)
;

create index FKF4FBF5497FFF790E
  on historico_lancamento (USUARIO_ID)
;

create table if not exists historico_movto_financeiro_cota
(
  ID bigint auto_increment
    primary key,
  DATA_EDICAO datetime not null,
  TIPO_EDICAO varchar(255) not null,
  DATA date not null,
  VALOR decimal(18,4) null,
  USUARIO_ID bigint not null,
  COTA_ID bigint not null,
  MOVTO_FINANCEIRO_COTA_ID bigint not null,
  TIPO_MOVTO_ID bigint not null,
  constraint FKAB46D6EDC8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FKAB46D6ED3BDC9EE3
  on historico_movto_financeiro_cota (TIPO_MOVTO_ID)
;

create index FKAB46D6ED7FFF790E
  on historico_movto_financeiro_cota (USUARIO_ID)
;

create index FKAB46D6EDC7F49D38
  on historico_movto_financeiro_cota (MOVTO_FINANCEIRO_COTA_ID)
;

create index FKAB46D6EDC8181F74
  on historico_movto_financeiro_cota (COTA_ID)
;

create table if not exists historico_situacao_cota
(
  ID bigint auto_increment
    primary key,
  DATA_EDICAO datetime not null,
  TIPO_EDICAO varchar(255) not null,
  RESTAURADO tinyint(1) null,
  PROCESSADO tinyint(1) null,
  DATA_FIM_VALIDADE date null,
  DATA_INICIO_VALIDADE date null,
  DESCRICAO varchar(255) null,
  MOTIVO varchar(255) null,
  NOVA_SITUACAO varchar(255) not null,
  SITUACAO_ANTERIOR varchar(255) null,
  USUARIO_ID bigint not null,
  COTA_ID bigint not null,
  constraint FKA2348342C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FKA23483427FFF790E
  on historico_situacao_cota (USUARIO_ID)
;

create index FKA2348342C8181F74
  on historico_situacao_cota (COTA_ID)
;

create table if not exists historico_titularidade_cota
(
  ID bigint auto_increment
    primary key,
  BOX varchar(255) null,
  CLASSIFICACAO_EXPECTATIVA_FATURAMENTO varchar(255) null,
  DATA_INCLUSAO date null,
  DISTRIBUICAO_ASSISTENTE_COMERCIAL varchar(255) null,
  DISTRIBUICAO_BOLETO_EMAIL tinyint(1) null,
  DISTRIBUICAO_BOLETO_IMPRESSO tinyint(1) null,
  DISTRIBUICAO_BOLETO_SLIP_EMAIL tinyint(1) null,
  DISTRIBUICAO_BOLETO_SLIP_IMPRESSO tinyint(1) null,
  DISTRIBUICAO_CHAMADA_ENCALHE_EMAIL tinyint(1) null,
  DISTRIBUICAO_CHAMADA_ENCALHE_IMPRESSO tinyint(1) null,
  DISTRIBUICAO_ENTREGA_REPARTE_VENDA tinyint(1) null,
  DISTRIBUICAO_FIM_PERIODO_CARENCIA date null,
  DISTRIBUICAO_GERENTE_COMERCIAL varchar(255) null,
  DISTRIBUICAO_INICIO_PERIODO_CARENCIA date null,
  DISTRIBUICAO_NOTA_ENVIO_EMAIL tinyint(1) null,
  DISTRIBUICAO_NOTA_ENVIO_IMPRESSO tinyint(1) null,
  DISTRIBUICAO_OBSERVACAO varchar(255) null,
  DISTRIBUICAO_PERCENTUAL_FATURAMENTO_ENTREGA decimal(18,4) null,
  DISTRIBUICAO_POSSUI_PROCURACAO tinyint(1) null,
  DISTRIBUICAO_POSSUI_TERMO_ADESAO tinyint(1) null,
  DISTRIBUICAO_PROCURACAO_ASSINADA tinyint(1) null,
  DISTRIBUICAO_QTDE_PDV int null,
  DISTRIBUICAO_RECEBE_RECOLHE_PARCIAIS tinyint(1) null,
  DISTRIBUICAO_RECIBO_EMAIL tinyint(1) null,
  DISTRIBUICAO_RECIBO_IMPRESSO tinyint(1) null,
  DISTRIBUICAO_SLIP_EMAIL tinyint(1) null,
  DISTRIBUICAO_SLIP_IMPRESSO tinyint(1) null,
  DISTRIBUICAO_SOLICITA_NUM_ATRASADOS tinyint(1) null,
  DISTRIBUICAO_TAXA_FIXA_ENTREGA decimal(18,4) null,
  DISTRIBUICAO_TERMO_ADESAO_ASSINADO tinyint(1) null,
  DISTRIBUICAO_TIPO_ENTREGA varchar(255) null,
  EMAIL varchar(255) null,
  EMAIL_NFE varchar(255) null,
  EXIGE_NF_E tinyint(1) null,
  CONTRIBUINTE_ICMS tinyint(1) null,
  FIM_TITULARIDADE date not null,
  FIM_PERIODO_COTA_BASE date null,
  INICIO_TITULARIDADE date not null,
  INICIO_PERIODO_COTA_BASE date null,
  NUMERO_COTA int not null,
  APELIDO varchar(25) null,
  CPF varchar(255) null,
  DATA_NASCIMENTO date null,
  ESTADO_CIVIL varchar(255) null,
  NACIONALIDADE varchar(255) null,
  NATURALIDADE varchar(255) null,
  NOME varchar(255) null,
  ORGAO_EMISSOR varchar(255) null,
  RG varchar(255) null,
  SEXO varchar(255) null,
  UF_ORGAO_EMISSOR varchar(255) null,
  CNPJ varchar(255) null,
  INSC_ESTADUAL varchar(20) null,
  INSC_MUNICIPAL varchar(15) null,
  NOME_FANTASIA varchar(60) null,
  RAZAO_SOCIAL varchar(255) null,
  SITUACAO_CADASTRO varchar(255) not null,
  COTA_ID bigint not null,
  HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID bigint null,
  constraint FK7B8B4229C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK7B8B422997041E9A
  on historico_titularidade_cota (HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID)
;

create index FK7B8B4229C8181F74
  on historico_titularidade_cota (COTA_ID)
;

create table if not exists historico_titularidade_cota_atualizacao_caucao_liquida
(
  HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID bigint not null,
  DATA_ATUALIZACAO datetime null,
  VALOR decimal(18,4) null
)
;

create index FK876AEF279610C138
  on historico_titularidade_cota_atualizacao_caucao_liquida (HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID)
;

create table if not exists historico_titularidade_cota_concentracao_cobranca
(
  ID bigint auto_increment
    primary key,
  TIPO_FORMA_COBRANCA varchar(255) not null
)
;

create table if not exists historico_titularidade_cota_concentracao_cobranca_dia_mes
(
  HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID bigint not null,
  DIA_MES int null,
  constraint FK86D6DE91290F8C31
  foreign key (HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID) references historico_titularidade_cota_concentracao_cobranca (ID)
)
;

create index FK86D6DE91290F8C31
  on historico_titularidade_cota_concentracao_cobranca_dia_mes (HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID)
;

create table if not exists historico_titularidade_cota_concentracao_cobranca_dia_semana
(
  HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID bigint not null,
  DIA_SEMANA int null,
  constraint FK70D1A303290F8C31
  foreign key (HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID) references historico_titularidade_cota_concentracao_cobranca (ID)
)
;

create index FK70D1A303290F8C31
  on historico_titularidade_cota_concentracao_cobranca_dia_semana (HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID)
;

create table if not exists historico_titularidade_cota_desconto
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  ATUALIZACAO datetime not null,
  DESCONTO decimal(18,4) null,
  NOME_FORNECEDOR varchar(255) null,
  TIPO_DESCONTO varchar(255) null,
  PRODUTO_CODIGO varchar(255) null,
  PRODUTO_NOME varchar(255) null,
  PRODUTO_NUMERO_EDICAO bigint null,
  HISTORICO_TITULARIDADE_COTA_ID bigint null,
  constraint FKD07CAC01508A9E07
  foreign key (HISTORICO_TITULARIDADE_COTA_ID) references historico_titularidade_cota (ID)
)
;

create index FKD07CAC01508A9E07
  on historico_titularidade_cota_desconto (HISTORICO_TITULARIDADE_COTA_ID)
;

create table if not exists historico_titularidade_cota_endereco
(
  HISTORICO_TITULARIDADE_COTA_ID bigint not null,
  ENDERECO_BAIRRO varchar(60) null,
  ENDERECO_CEP varchar(9) null,
  ENDERECO_CIDADE varchar(60) null,
  ENDERECO_CODIGO_BAIRRO int null,
  ENDERECO_CODIGO_CIDADE_IBGE int null,
  ENDERECO_CODIGO_UF int null,
  ENDERECO_COMPLEMENTO varchar(60) null,
  ENDERECO_LOGRADOURO varchar(60) null,
  ENDERECO_NUMERO varchar(60) null,
  ENDERECO_PRINCIPAL tinyint(1) null,
  ENDERECO_TIPO_ENDERECO varchar(255) null,
  ENDERECO_TIPO_LOGRADOURO varchar(255) null,
  ENDERECO_UF varchar(2) null,
  constraint FKFAFAA51F508A9E07
  foreign key (HISTORICO_TITULARIDADE_COTA_ID) references historico_titularidade_cota (ID)
)
;

create index FKFAFAA51F508A9E07
  on historico_titularidade_cota_endereco (HISTORICO_TITULARIDADE_COTA_ID)
;

create table if not exists historico_titularidade_cota_fiador_garantia
(
  HISTORICO_TITULARIDADE_COTA_FIADOR_ID bigint not null,
  GARANTIA_DESCRICAO varchar(255) null,
  GARANTIA_VALOR decimal(18,4) null
)
;

create index FKADC6E47458C497A
  on historico_titularidade_cota_fiador_garantia (HISTORICO_TITULARIDADE_COTA_FIADOR_ID)
;

create table if not exists historico_titularidade_cota_financeiro
(
  ID bigint auto_increment
    primary key,
  CONTRATO_RECEBIDO tinyint(1) null,
  DATA_INICIO_CONTRATO date null,
  DATA_TERMINO_CONTRATO date null,
  FATOR_VENCIMENTO int null,
  NUM_ACUMULO_DIVIDA int null,
  VALOR_SUSPENSAO decimal(18,4) null,
  POSSUI_CONTRATO tinyint(1) null,
  TIPO_COTA varchar(255) null,
  VALOR_MINIMO_COBRANCA decimal(18,4) null
)
;

alter table historico_titularidade_cota
  add constraint FK7B8B422997041E9A
foreign key (HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID) references historico_titularidade_cota_financeiro (ID)
;

create table if not exists historico_titularidade_cota_forma_pagamento
(
  ID bigint auto_increment
    primary key,
  BANCO_AGENCIA bigint null,
  BANCO_CONTA bigint null,
  BANCO_DV_AGENCIA varchar(255) null,
  BANCO_DV_CONTA varchar(255) null,
  BANCO_NOME varchar(255) null,
  BANCO_NUMERO varchar(255) null,
  TIPO_COBRANCA varchar(255) not null,
  HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID bigint null,
  HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID bigint null,
  constraint FKB6F8BF2EBC56D0FB
  foreign key (HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID) references historico_titularidade_cota_concentracao_cobranca (ID),
  constraint FKB6F8BF2E97041E9A
  foreign key (HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID) references historico_titularidade_cota_financeiro (ID)
)
;

create index FKB6F8BF2E97041E9A
  on historico_titularidade_cota_forma_pagamento (HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID)
;

create index FKB6F8BF2EBC56D0FB
  on historico_titularidade_cota_forma_pagamento (HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID)
;

create table if not exists historico_titularidade_cota_forma_pagamento_fornecedor
(
  HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID bigint not null,
  HISTORICO_TITULARIDADE_COTA_FORNECEDOR_ID bigint not null,
  constraint FK563C7B16B38F2485
  foreign key (HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID) references historico_titularidade_cota_forma_pagamento (ID)
)
;

create index FK563C7B16B38F2485
  on historico_titularidade_cota_forma_pagamento_fornecedor (HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID)
;

create index FK563C7B16FAD71BFA
  on historico_titularidade_cota_forma_pagamento_fornecedor (HISTORICO_TITULARIDADE_COTA_FORNECEDOR_ID)
;

create table if not exists historico_titularidade_cota_fornecedor
(
  ID bigint auto_increment
    primary key,
  ID_ORIGEM bigint null,
  CNPJ varchar(255) null,
  INSC_ESTADUAL varchar(18) null,
  INSC_MUNICIPAL varchar(15) null,
  NOME_FANTASIA varchar(60) null,
  RAZAO_SOCIAL varchar(255) null,
  HISTORICO_TITULARIDADE_COTA_ID bigint null,
  constraint FK2391DD3B508A9E07
  foreign key (HISTORICO_TITULARIDADE_COTA_ID) references historico_titularidade_cota (ID)
)
;

create index FK2391DD3B508A9E07
  on historico_titularidade_cota_fornecedor (HISTORICO_TITULARIDADE_COTA_ID)
;

alter table historico_titularidade_cota_forma_pagamento_fornecedor
  add constraint FK563C7B16FAD71BFA
foreign key (HISTORICO_TITULARIDADE_COTA_FORNECEDOR_ID) references historico_titularidade_cota_fornecedor (ID)
;

create table if not exists historico_titularidade_cota_funcionamento_pdv
(
  HISTORICO_TITULARIDADE_COTA_PDV_ID bigint not null,
  HORARIO_FIM time null,
  HORARIO_INICIO time null,
  FUNCIONAMENTO_PDV varchar(255) null
)
;

create index FK21FF2C411043021A
  on historico_titularidade_cota_funcionamento_pdv (HISTORICO_TITULARIDADE_COTA_PDV_ID)
;

create table if not exists historico_titularidade_cota_garantia
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  TIPO_GARANTIA varchar(255) null,
  AGENCIA_BANCO bigint null,
  CONTA_BANCO bigint null,
  DV_AGENCIA_BANCO varchar(255) null,
  DV_CONTA_BANCO varchar(255) null,
  NOME_BANCO varchar(255) null,
  CORRENTISTA varchar(255) null,
  NUMERO_BANCO varchar(255) null,
  VALOR_CAUCAO_LIQUIDA decimal(18,4) null,
  CHEQUE_CAUCAO_AGENCIA bigint null,
  CHEQUE_CAUCAO_CONTA bigint null,
  CHEQUE_CAUCAO_CORRENTISTA varchar(255) null,
  CHEQUE_CAUCAO_DV_AGENCIA varchar(255) null,
  CHEQUE_CAUCAO_DV_CONTA varchar(255) null,
  CHEQUE_CAUCAO_DATA_EMISSAO date null,
  CHEQUE_CAUCAO_IMAGEM longblob null,
  CHEQUE_CAUCAO_NOME_BANCO varchar(255) null,
  CHEQUE_CAUCAO_NUMERO_BANCO varchar(255) null,
  CHEQUE_CAUCAO_NUMERO_CHEQUE varchar(255) null,
  CHEQUE_CAUCAO_DATA_VALIDADE date null,
  CHEQUE_CAUCAO_VALOR decimal(18,4) null,
  FIADOR_CPF_CNPJ varchar(255) null,
  ENDERECO_BAIRRO varchar(60) null,
  ENDERECO_CEP varchar(9) null,
  ENDERECO_CIDADE varchar(60) null,
  ENDERECO_CODIGO_BAIRRO int null,
  ENDERECO_CODIGO_CIDADE_IBGE int null,
  ENDERECO_CODIGO_UF int null,
  ENDERECO_COMPLEMENTO varchar(60) null,
  ENDERECO_LOGRADOURO varchar(60) null,
  ENDERECO_NUMERO varchar(60) null,
  ENDERECO_PRINCIPAL tinyint(1) null,
  ENDERECO_TIPO_ENDERECO varchar(255) null,
  ENDERECO_TIPO_LOGRADOURO varchar(255) null,
  ENDERECO_UF varchar(2) null,
  DDD varchar(255) null,
  TELEFONE_NUMERO varchar(255) null,
  TELEFONE_PRINCIPAL tinyint(1) null,
  TELEFONE_RAMAL varchar(255) null,
  TELEFONE_TIPO_TELEFONE varchar(255) null,
  FIADOR_NOME varchar(255) null,
  IMOVEL_ENDERECO varchar(255) null,
  IMOVEL_NUMERO_REGISTRO varchar(255) null,
  IMOVEL_OBSERVACAO varchar(255) null,
  IMOVEL_PROPRIETARIO varchar(255) null,
  IMOVEL_VALOR decimal(18,4) null,
  NOTA_PROMISSORIA_VALOR decimal(18,4) null,
  NOTA_PROMISSORIA_VALOR_EXTENSO varchar(255) null,
  NOTA_PROMISSORIA_VENCIMENTO date null,
  OUTROS_DESCRICAO varchar(255) null,
  OUTROS_DATA_VALIDADE datetime null,
  OUTROS_VALOR decimal(18,4) null,
  HISTORICO_TITULARIDADE_COTA_ID bigint null,
  HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID bigint null,
  constraint FK32B5DEBD508A9E07
  foreign key (HISTORICO_TITULARIDADE_COTA_ID) references historico_titularidade_cota (ID)
)
;

create index FK32B5DEBD508A9E07
  on historico_titularidade_cota_garantia (HISTORICO_TITULARIDADE_COTA_ID)
;

create index FK32B5DEBDF244821E
  on historico_titularidade_cota_garantia (HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID)
;

alter table historico_titularidade_cota_atualizacao_caucao_liquida
  add constraint FK876AEF279610C138
foreign key (HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID) references historico_titularidade_cota_garantia (ID)
;

alter table historico_titularidade_cota_fiador_garantia
  add constraint FKADC6E47458C497A
foreign key (HISTORICO_TITULARIDADE_COTA_FIADOR_ID) references historico_titularidade_cota_garantia (ID)
;

create table if not exists historico_titularidade_cota_gerador_fluxo_secundario
(
  HISTORICO_TITULARIDADE_COTA_PDV_ID bigint not null,
  CODIGO_GERADOR_FLUXO bigint null,
  DESCRICAO_GERADOR_FLUXO varchar(255) null
)
;

create index FKE4C9A5E51043021A
  on historico_titularidade_cota_gerador_fluxo_secundario (HISTORICO_TITULARIDADE_COTA_PDV_ID)
;

create table if not exists historico_titularidade_cota_material_promocional_pdv
(
  HISTORICO_TITULARIDADE_COTA_PDV_ID bigint not null,
  CODIGO_MATERIAL bigint null,
  DESCRICAO_MATERIAL varchar(255) null
)
;

create index FK1DE198401043021A
  on historico_titularidade_cota_material_promocional_pdv (HISTORICO_TITULARIDADE_COTA_PDV_ID)
;

create table if not exists historico_titularidade_cota_pdv
(
  ID bigint auto_increment
    primary key,
  CODIGO_AREA_INFLUENCIA bigint null,
  DESCRICAO_AREA_INFLUENCIA varchar(255) null,
  ARRENDATARIO tinyint(1) null,
  BALCAO_CENTRAL tinyint(1) null,
  PONTO_PRINCIPAL tinyint(1) null,
  POSSUI_CARTAO_CREDITO tinyint(1) null,
  POSSUI_COMPUTADOR tinyint(1) null,
  POSSUI_LUMINOSO tinyint(1) null,
  TEXTO_LUMINOSO varchar(255) null,
  CONTATO varchar(255) null,
  DATA_INCLUSAO date null,
  DENTRO_OUTRO_ESTABELECIMENTO tinyint(1) null,
  EMAIL varchar(255) null,
  EXPOSITOR tinyint(1) null,
  CODIGO_GERADOR_FLUXO_PRINCIPAL bigint null,
  DESCRICAO_GERADOR_FLUXO_PRINCIPAL varchar(255) null,
  IMAGEM longblob null,
  NOME varchar(255) not null,
  NOME_LICENCA_LICENCA_MUNICIPAL varchar(255) null,
  NUMERO_LICENCA_MUNICIPAL varchar(255) null,
  PONTO_REFERENCIA varchar(255) null,
  PORCENTAGEM_FATURAMENTO decimal(18,4) null,
  POSSUI_SISTEMA_IPV tinyint(1) null,
  QTDE_FUNCIONARIOS int null,
  SITE varchar(255) null,
  STATUS_PDV varchar(255) null,
  TAMANHO_PDV varchar(255) null,
  TIPO_CARACTERISTICA_PDV varchar(255) null,
  CODIGO_TIPO_ESTABELECIMENTO_PDV bigint null,
  DESCRICAO_TIPO_ESTABELECIMENTO_PDV varchar(255) null,
  TIPO_EXPOSITOR varchar(255) null,
  CODIGO_TIPO_LICENCA_MUNICIPAL bigint null,
  DESCRICAO_TIPO_LICENCA_MUNICIPAL varchar(255) null,
  CODIGO_TIPO_PONTO bigint null,
  DESCRICAO_TIPO_PONTO varchar(255) null,
  HISTORICO_TITULARIDADE_COTA_ID bigint null,
  constraint FK628869CC508A9E07
  foreign key (HISTORICO_TITULARIDADE_COTA_ID) references historico_titularidade_cota (ID)
)
;

create index FK628869CC508A9E07
  on historico_titularidade_cota_pdv (HISTORICO_TITULARIDADE_COTA_ID)
;

alter table historico_titularidade_cota_funcionamento_pdv
  add constraint FK21FF2C411043021A
foreign key (HISTORICO_TITULARIDADE_COTA_PDV_ID) references historico_titularidade_cota_pdv (ID)
;

alter table historico_titularidade_cota_gerador_fluxo_secundario
  add constraint FKE4C9A5E51043021A
foreign key (HISTORICO_TITULARIDADE_COTA_PDV_ID) references historico_titularidade_cota_pdv (ID)
;

alter table historico_titularidade_cota_material_promocional_pdv
  add constraint FK1DE198401043021A
foreign key (HISTORICO_TITULARIDADE_COTA_PDV_ID) references historico_titularidade_cota_pdv (ID)
;

create table if not exists historico_titularidade_cota_pdv_endereco
(
  HISTORICO_TITULARIDADE_COTA_PDV_ID bigint not null,
  ENDERECO_BAIRRO varchar(60) null,
  ENDERECO_CEP varchar(9) null,
  ENDERECO_CIDADE varchar(60) null,
  ENDERECO_CODIGO_BAIRRO int null,
  ENDERECO_CODIGO_CIDADE_IBGE int null,
  ENDERECO_CODIGO_UF int null,
  ENDERECO_COMPLEMENTO varchar(60) null,
  ENDERECO_LOGRADOURO varchar(60) null,
  ENDERECO_NUMERO varchar(60) null,
  ENDERECO_PRINCIPAL tinyint(1) null,
  ENDERECO_TIPO_ENDERECO varchar(255) null,
  ENDERECO_TIPO_LOGRADOURO varchar(255) null,
  ENDERECO_UF varchar(2) null,
  constraint FK5B9C64DC1043021A
  foreign key (HISTORICO_TITULARIDADE_COTA_PDV_ID) references historico_titularidade_cota_pdv (ID)
)
;

create index FK5B9C64DC1043021A
  on historico_titularidade_cota_pdv_endereco (HISTORICO_TITULARIDADE_COTA_PDV_ID)
;

create table if not exists historico_titularidade_cota_pdv_telefone
(
  HISTORICO_TITULARIDADE_COTA_PDV_ID bigint not null,
  DDD varchar(255) null,
  TELEFONE_NUMERO varchar(255) null,
  TELEFONE_PRINCIPAL tinyint(1) null,
  TELEFONE_RAMAL varchar(255) null,
  TELEFONE_TIPO_TELEFONE varchar(255) null,
  constraint FKA35767FD1043021A
  foreign key (HISTORICO_TITULARIDADE_COTA_PDV_ID) references historico_titularidade_cota_pdv (ID)
)
;

create index FKA35767FD1043021A
  on historico_titularidade_cota_pdv_telefone (HISTORICO_TITULARIDADE_COTA_PDV_ID)
;

create table if not exists historico_titularidade_cota_pgto_caucao_liquida
(
  ID bigint auto_increment
    primary key,
  DESCONTO_NORMAL_COTA decimal(18,4) null,
  DESCONTO_REDUZIDO_COTA decimal(18,4) null,
  PERIODICIDADE_BOLETO varchar(255) null,
  PERCENTUAL_UTILIZADO_DESCONTO decimal(18,4) null,
  QTDE_PARCELAS_BOLETO int null,
  TIPO_COBRANCA varchar(255) not null,
  VALOR_PARCELAS_BOLETO decimal(18,4) null
)
;

alter table historico_titularidade_cota_garantia
  add constraint FK32B5DEBDF244821E
foreign key (HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID) references historico_titularidade_cota_pgto_caucao_liquida (ID)
;

create table if not exists historico_titularidade_cota_pgto_caucao_liquida_dia_mes
(
  HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID bigint not null,
  DIA_MES int null,
  constraint FK945FFD10F244821E
  foreign key (HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID) references historico_titularidade_cota_pgto_caucao_liquida (ID)
)
;

create index FK945FFD10F244821E
  on historico_titularidade_cota_pgto_caucao_liquida_dia_mes (HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID)
;

create table if not exists historico_titularidade_cota_pgto_caucao_liquida_dia_semana
(
  HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID bigint not null,
  DIA_SEMANA int null,
  constraint FK98858024F244821E
  foreign key (HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID) references historico_titularidade_cota_pgto_caucao_liquida (ID)
)
;

create index FK98858024F244821E
  on historico_titularidade_cota_pgto_caucao_liquida_dia_semana (HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID)
;

create table if not exists historico_titularidade_cota_referencia_cota
(
  HISTORICO_TITULARIDADE_COTA_ID bigint not null,
  NUMERO_COTA int null,
  PERCENTUAL decimal(18,4) null,
  constraint FKD4A376F0508A9E07
  foreign key (HISTORICO_TITULARIDADE_COTA_ID) references historico_titularidade_cota (ID)
)
;

create index FKD4A376F0508A9E07
  on historico_titularidade_cota_referencia_cota (HISTORICO_TITULARIDADE_COTA_ID)
;

create table if not exists historico_titularidade_cota_socio
(
  ID bigint auto_increment
    primary key,
  CARGO varchar(255) null,
  ENDERECO_BAIRRO varchar(60) null,
  ENDERECO_CEP varchar(9) null,
  ENDERECO_CIDADE varchar(60) null,
  ENDERECO_CODIGO_BAIRRO int null,
  ENDERECO_CODIGO_CIDADE_IBGE int null,
  ENDERECO_CODIGO_UF int null,
  ENDERECO_COMPLEMENTO varchar(60) null,
  ENDERECO_LOGRADOURO varchar(60) null,
  ENDERECO_NUMERO varchar(60) null,
  ENDERECO_PRINCIPAL tinyint(1) null,
  ENDERECO_TIPO_ENDERECO varchar(255) null,
  ENDERECO_TIPO_LOGRADOURO varchar(255) null,
  ENDERECO_UF varchar(2) null,
  NOME varchar(255) not null,
  PRINCIPAL_SOCIO tinyint(1) null,
  DDD varchar(255) null,
  TELEFONE_NUMERO varchar(255) null,
  TELEFONE_PRINCIPAL tinyint(1) null,
  TELEFONE_RAMAL varchar(255) null,
  TELEFONE_TIPO_TELEFONE varchar(255) null,
  HISTORICO_TITULARIDADE_COTA_ID bigint null,
  constraint FKE2442F37508A9E07
  foreign key (HISTORICO_TITULARIDADE_COTA_ID) references historico_titularidade_cota (ID)
)
;

create index FKE2442F37508A9E07
  on historico_titularidade_cota_socio (HISTORICO_TITULARIDADE_COTA_ID)
;

create table if not exists historico_titularidade_cota_telefone
(
  HISTORICO_TITULARIDADE_COTA_ID bigint not null,
  DDD varchar(255) null,
  TELEFONE_NUMERO varchar(255) null,
  TELEFONE_PRINCIPAL tinyint(1) null,
  TELEFONE_RAMAL varchar(255) null,
  TELEFONE_TIPO_TELEFONE varchar(255) null,
  constraint FK42B5A840508A9E07
  foreign key (HISTORICO_TITULARIDADE_COTA_ID) references historico_titularidade_cota (ID)
)
;

create index FK42B5A840508A9E07
  on historico_titularidade_cota_telefone (HISTORICO_TITULARIDADE_COTA_ID)
;

create table if not exists historicofechamentodiariomovimentovenda
(
  ID bigint not null
    primary key,
  DATA_VENCIMENTO date null,
  QUANTIDADE decimal(18,4) null,
  VALOR decimal(18,4) null,
  ID_PRODUTO_EDICAO bigint not null
)
;

create index FK534CD760D2FE34B7
  on historicofechamentodiariomovimentovenda (ID_PRODUTO_EDICAO)
;

create table if not exists historiconumerocota
(
  DATA_ALTERACAO datetime not null,
  ID_COTA bigint not null,
  NUMERO_COTA int null,
  primary key (DATA_ALTERACAO, ID_COTA),
  constraint FKE5CAF605F1916CB0
  foreign key (ID_COTA) references cota (ID)
)
;

create index FKE5CAF605F1916CB0
  on historiconumerocota (ID_COTA)
;

create table if not exists hvnd
(
  COD_COTA_HVCT int null,
  cod_produto varchar(10) null,
  num_edicao int null,
  PRECO float null,
  QTDE_REPARTE_HVCT int null,
  QTDE_ENCALHE_HVCT int null,
  STATUS char null,
  CONSIGNADO tinyint(1) null,
  DEVOLVE_ENCALHE tinyint(1) null,
  COD_COTA_DP_HVCT int null,
  COD_COTA_FC_HVCT int null,
  NUMERO_PERIODO int null,
  PARCIAL_NORMAL char null,
  produto_edicao_id bigint null,
  DATA_LANCAMENTO date null,
  DATA_RECOLHIMENTO date null,
  cota_id bigint null
)
;

create table if not exists imovel
(
  ID bigint auto_increment
    primary key,
  ENDERECO varchar(255) not null,
  NUMERO_REGISTRO varchar(255) not null,
  OBSERVACAO varchar(255) not null,
  PROPRIETARIO varchar(255) not null,
  VALOR double not null,
  GARANTIA_ID bigint not null,
  constraint FK80F40BF2851B41F2
  foreign key (GARANTIA_ID) references cota_garantia (ID)
)
;

create index FK80F40BF2851B41F2
  on imovel (GARANTIA_ID)
;

create table if not exists informacoes_reparte_complementar_estudo
(
  ID bigint auto_increment
    primary key,
  ID_ESTUDO bigint not null,
  TOTAL_REPARTE_A_DISTRIBUIR bigint null,
  TOTAL_REPARTE_DISTRIBUIDO bigint null,
  TOTAL_REPARTE_COMPLEMENTAR bigint null
)
;

create table if not exists interface_execucao
(
  ID bigint not null
    primary key,
  NOME varchar(7) not null,
  MASCARA_ARQUIVO varchar(50) null,
  descricao varchar(255) not null,
  extensao_arquivo varchar(10) null,
  TIPO_INTERFACE_EXECUCAO varchar(30) null,
  constraint NOME
  unique (NOME)
)
;

create table if not exists item_chamada_encalhe_fornecedor
(
  ID bigint auto_increment
    primary key,
  CONTROLE int not null,
  DATA_RECOLHIMENTO date null,
  FORMA_DEVOLUCAO varchar(255) null,
  NUMERO_DOCUMENTO bigint not null,
  NUEMRO_ITEM int not null,
  NUMERO_NOTA_ENVIO bigint not null,
  PRECO_UNITARIO decimal(18,4) null,
  QTDE_DEVOLUCAO_APURADA bigint null,
  QTDE_DEVOLUCAO_INFORMADA bigint null,
  QTDE_DEVOLUCAO_PARCIAL bigint null,
  QTDE_ENVIADA bigint not null,
  QTDE_VENDA_APURADA bigint null,
  QTDE_VENDA_INFORMADA bigint null,
  REGIME_RECOLHIMENTO varchar(255) null,
  STATUS varchar(255) not null,
  TIPO_PRODUTO varchar(255) not null,
  VALOR_MARGEM_APURADO decimal(18,4) null,
  VALOR_MARGEM_INFORMADO decimal(18,4) null,
  VALOR_VENDA_APURADO decimal(18,4) null,
  VALOR_VENDA_INFORMADO decimal(18,4) null,
  CHAMADA_ENCALHE_FORNECEDOR_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  CODIGO_NOTA_ENVIO_MULTIPLA varchar(255) null,
  DIFERENCA_ID bigint null,
  constraint FKE9A54E40E26473F4
  foreign key (CHAMADA_ENCALHE_FORNECEDOR_ID) references chamada_encalhe_fornecedor (ID)
)
;

create index FKE9A54E40A53173D3
  on item_chamada_encalhe_fornecedor (PRODUTO_EDICAO_ID)
;

create index FKE9A54E40E26473F4
  on item_chamada_encalhe_fornecedor (CHAMADA_ENCALHE_FORNECEDOR_ID)
;

create table if not exists item_nota_fiscal_entrada
(
  ID bigint auto_increment
    primary key,
  CFOP_PRODUTO varchar(255) null,
  CSOSN_PRODUTO varchar(255) null,
  CST_PRODUTO varchar(255) null,
  NCM_PRODUTO varchar(255) null,
  ALIQUOTA_ICMS_PRODUTO decimal(18,4) null,
  ALIQUOTA_IPI_PRODUTO decimal(18,4) null,
  BASE_CALCULO_PRODUTO decimal(18,4) null,
  DATA_LANCAMENTO date not null,
  DATA_RECOLHIMENTO date not null,
  DESCONTO decimal(18,4) null,
  ORIGEM varchar(255) null,
  PRECO decimal(18,4) null,
  QTDE decimal(18,4) null,
  TIPO_LANCAMENTO int null,
  UNIDADE_PRODUTO bigint null,
  VALOR_ICMS_PRODUTO decimal(18,4) null,
  VALOR_IPI_PRODUTO decimal(18,4) null,
  NOTA_FISCAL_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  USUARIO_ID bigint null
)
;

create index FK3EDE8DFB7FFF790E
  on item_nota_fiscal_entrada (USUARIO_ID)
;

create index FK3EDE8DFBA53173D3
  on item_nota_fiscal_entrada (PRODUTO_EDICAO_ID)
;

create index FK3EDE8DFBC74D9881
  on item_nota_fiscal_entrada (NOTA_FISCAL_ID)
;

create table if not exists item_nota_fiscal_saida
(
  ID bigint auto_increment
    primary key,
  CFOP_PRODUTO varchar(255) null,
  CSOSN_PRODUTO varchar(255) null,
  CST_PRODUTO varchar(255) null,
  NCM_PRODUTO varchar(255) null,
  ALIQUOTA_ICMS_PRODUTO decimal(18,4) null,
  ALIQUOTA_IPI_PRODUTO decimal(18,4) null,
  BASE_CALCULO_PRODUTO decimal(18,4) null,
  QTDE decimal(18,4) null,
  UNIDADE_PRODUTO bigint null,
  VALOR_ICMS_PRODUTO decimal(18,4) null,
  VALOR_IPI_PRODUTO decimal(18,4) null,
  NOTA_FISCAL_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null
)
;

create index FK8FDD023CA05BE382
  on item_nota_fiscal_saida (NOTA_FISCAL_ID)
;

create index FK8FDD023CA53173D3
  on item_nota_fiscal_saida (PRODUTO_EDICAO_ID)
;

create table if not exists item_receb_fisico
(
  ID bigint auto_increment
    primary key,
  QTDE_FISICO decimal(18,4) null,
  DIFERENCA_ID bigint null,
  ITEM_NF_ENTRADA_ID bigint not null,
  RECEBIMENTO_FISICO_ID bigint not null,
  constraint ITEM_NF_ENTRADA_ID
  unique (ITEM_NF_ENTRADA_ID),
  constraint FK1217CDA35A0FFAA9
  foreign key (DIFERENCA_ID) references diferenca (id),
  constraint FK1217CDA3749B54C1
  foreign key (ITEM_NF_ENTRADA_ID) references item_nota_fiscal_entrada (ID)
)
;

create index FK1217CDA35A0FFAA9
  on item_receb_fisico (DIFERENCA_ID)
;

create index FK1217CDA3749B54C1
  on item_receb_fisico (ITEM_NF_ENTRADA_ID)
;

create index FK1217CDA3B7A04D34
  on item_receb_fisico (RECEBIMENTO_FISICO_ID)
;

alter table diferenca
  add constraint FKF2B9F0957204FEE9
foreign key (itemRecebimentoFisico_ID) references item_receb_fisico (ID)
;

create table if not exists lancamento
(
  ID bigint auto_increment
    primary key,
  ALTERADO_INTERFACE tinyint(1) not null,
  DATA_CRIACAO date not null,
  DATA_LCTO_DISTRIBUIDOR date not null,
  DATA_LCTO_PREVISTA date not null,
  DATA_REC_DISTRIB date not null,
  DATA_REC_PREVISTA date not null,
  DATA_FIN_MAT_DISTRIB date null,
  DATA_STATUS datetime not null,
  REPARTE decimal(18,4) null,
  REPARTE_PROMOCIONAL decimal(18,4) null,
  SEQUENCIA_MATRIZ int null,
  STATUS varchar(255) not null,
  TIPO_LANCAMENTO varchar(255) not null,
  PRODUTO_EDICAO_ID bigint null,
  EXPEDICAO_ID bigint null,
  USUARIO_ID bigint null,
  NUMERO_LANCAMENTO int null,
  PERIODO_LANCAMENTO_PARCIAL_ID bigint null,
  NUMERO_REPROGRAMACOES int null,
  ESTUDO_ID bigint null,
  juramentado tinyint(1) null,
  DATA_LED varchar(255) null,
  HORA_LED varchar(255) null,
  constraint FK1D53917A73249C49
  foreign key (EXPEDICAO_ID) references expedicao (ID),
  constraint lancamento_ibfk_1
  foreign key (ESTUDO_ID) references estudo (ID)
)
;

create index estudo_lancamento_id
  on lancamento (ESTUDO_ID)
;

create index FK1D53917A73249C49
  on lancamento (EXPEDICAO_ID)
;

create index FK1D53917A7FFF790E
  on lancamento (USUARIO_ID)
;

create index FK1D53917AA53173D3
  on lancamento (PRODUTO_EDICAO_ID)
;

create index INDEX_DATA_LCTO_DISTRIBUIDOR
  on lancamento (DATA_LCTO_DISTRIBUIDOR, DATA_REC_DISTRIB)
;

create index ndx_data_lcto_distribuidor
  on lancamento (DATA_LCTO_DISTRIBUIDOR)
;

create index NDX_DATA_RCTO_DIST
  on lancamento (DATA_REC_DISTRIB)
;

create index ndx_periodo_lancamento_parcial
  on lancamento (PERIODO_LANCAMENTO_PARCIAL_ID)
;

create index ndx_status
  on lancamento (PRODUTO_EDICAO_ID, ESTUDO_ID, STATUS)
;

create index NDX_STATUS_IDX
  on lancamento (STATUS)
;

create trigger trigger_historico_lancamento_insert
after INSERT on lancamento
for each row
  BEGIN
    INSERT INTO historico_lancamento
    SET STATUS_NOVO = NEW.STATUS,
      LANCAMENTO_ID = NEW.ID,
      DATA_EDICAO = SYSDATE(),
      TIPO_EDICAO = 'INCLUSAO',
      USUARIO_ID = NEW.USUARIO_ID;
  END;

create trigger trigger_historico_lancamento_update
after UPDATE on lancamento
for each row
  BEGIN
    INSERT INTO historico_lancamento
    SET STATUS_ANTIGO = OLD.STATUS,
      STATUS_NOVO = NEW.STATUS,
      LANCAMENTO_ID = NEW.ID,
      DATA_EDICAO = SYSDATE(),
      TIPO_EDICAO = 'ALTERACAO',
      USUARIO_ID = NEW.USUARIO_ID;
  END;

alter table chamada_encalhe_lancamento
  add constraint FK9669E1A945C07ACF
foreign key (LANCAMENTO_ID) references lancamento (ID)
;

alter table furo_produto
  add constraint FK301010E645C07ACF
foreign key (LANCAMENTO_ID) references lancamento (ID)
;

alter table historico_lancamento
  add constraint FKF4FBF54945C07ACF
foreign key (LANCAMENTO_ID) references lancamento (ID)
;

create table if not exists lancamento_diferenca
(
  ID bigint auto_increment
    primary key,
  DATA_PROCESSAMENTO datetime null,
  STATUS varchar(255) null,
  MOVIMENTO_ESTOQUE_ID bigint null,
  MOTIVO varchar(255) null
)
;

create index FKE9CA6C10232FFB12
  on lancamento_diferenca (MOVIMENTO_ESTOQUE_ID)
;

alter table diferenca
  add constraint FKF2B9F095C4673234
foreign key (LANCAMENTO_DIFERENCA_ID) references lancamento_diferenca (ID)
;

create table if not exists lancamento_diferenca_movimento_estoque_cota
(
  LANCAMENTO_DIFERENCA_ID bigint not null,
  MOVIMENTO_ESTOQUE_COTA_ID bigint not null,
  constraint MOVIMENTO_ESTOQUE_COTA_ID
  unique (MOVIMENTO_ESTOQUE_COTA_ID),
  constraint FKE95125D2C4673234
  foreign key (LANCAMENTO_DIFERENCA_ID) references lancamento_diferenca (ID)
)
;

create index FKE95125D2BBE20E9D
  on lancamento_diferenca_movimento_estoque_cota (MOVIMENTO_ESTOQUE_COTA_ID)
;

create index FKE95125D2C4673234
  on lancamento_diferenca_movimento_estoque_cota (LANCAMENTO_DIFERENCA_ID)
;

create table if not exists lancamento_item_receb_fisico
(
  LANCAMENTO_ID bigint not null,
  recebimentos_ID bigint not null,
  primary key (LANCAMENTO_ID, recebimentos_ID),
  constraint recebimentos_ID
  unique (recebimentos_ID),
  constraint FK175E9E1E45C07ACF
  foreign key (LANCAMENTO_ID) references lancamento (ID),
  constraint FK175E9E1ECA768D72
  foreign key (recebimentos_ID) references item_receb_fisico (ID)
)
;

create index FK175E9E1E45C07ACF
  on lancamento_item_receb_fisico (LANCAMENTO_ID)
;

create index FK175E9E1ECA768D72
  on lancamento_item_receb_fisico (recebimentos_ID)
;

create table if not exists lancamento_parcial
(
  ID bigint auto_increment
    primary key,
  LANCAMENTO_INICIAL date not null,
  RECOLHIMENTO_FINAL date not null,
  STATUS varchar(255) not null,
  PRODUTO_EDICAO_ID bigint not null,
  constraint PRODUTO_EDICAO_ID
  unique (PRODUTO_EDICAO_ID)
)
;

create index FKDDFB94DA53173D3
  on lancamento_parcial (PRODUTO_EDICAO_ID)
;

create table if not exists log_bairro
(
  BAI_NU bigint not null
    primary key,
  BAI_NO_ABREV varchar(72) null,
  BAI_NO varchar(144) null,
  UFE_SG varchar(4) null,
  LOC_NU bigint not null,
  constraint BAI_NU
  unique (BAI_NU)
)
;

create index FK2C25836044246A2B
  on log_bairro (LOC_NU)
;

create index FK2C258360C35A8410
  on log_bairro (LOC_NU)
;

create table if not exists log_execucao
(
  ID bigint auto_increment
    primary key,
  DATA_FIM datetime null,
  DATA_INICIO datetime not null,
  NOME_LOGIN_USUARIO varchar(20) not null,
  COD_DISTRIBUIDOR varchar(20) null,
  STATUS varchar(1) null,
  INTERFACE_EXECUCAO_ID bigint not null,
  constraint FKA97F7A088D8C2EB
  foreign key (INTERFACE_EXECUCAO_ID) references interface_execucao (ID)
)
;

create index FKA97F7A088D8C2EB
  on log_execucao (INTERFACE_EXECUCAO_ID)
;

create table if not exists log_execucao_arquivo
(
  ID bigint auto_increment
    primary key,
  CAMINHO_ARQUIVO varchar(350) null,
  DISTRIBUIDOR_ID int not null,
  MENSAGEM varchar(500) null,
  STATUS varchar(1) null,
  LOG_EXECUCAO_ID bigint not null,
  constraint FK85183756502B1201
  foreign key (LOG_EXECUCAO_ID) references log_execucao (ID)
)
;

create index FK85183756502B1201
  on log_execucao_arquivo (LOG_EXECUCAO_ID)
;

create table if not exists log_execucao_mensagem
(
  ID bigint auto_increment
    primary key,
  MENSAGEM varchar(500) null,
  MENSAGEM_INFO varchar(500) null,
  NOME_ARQUIVO varchar(50) not null,
  NUMERO_LINHA int null,
  EVENTO_EXECUCAO_ID bigint not null,
  LOG_EXECUCAO_ID bigint null,
  constraint FK478D3682189EC167
  foreign key (EVENTO_EXECUCAO_ID) references evento_execucao (ID),
  constraint FK478D3682502B1201
  foreign key (LOG_EXECUCAO_ID) references log_execucao (ID)
)
;

create index FK478D3682189EC167
  on log_execucao_mensagem (EVENTO_EXECUCAO_ID)
;

create index FK478D3682502B1201
  on log_execucao_mensagem (LOG_EXECUCAO_ID)
;

create table if not exists log_faixa_uf
(
  UFE_SG varchar(4) not null
    primary key,
  UFE_CEP_FIM varchar(16) null,
  UFE_CEP_INI varchar(16) null,
  constraint UFE_SG
  unique (UFE_SG)
)
;

alter table feriado
  add constraint FKF1584996B31F2A81
foreign key (UFE_SG) references log_faixa_uf (UFE_SG)
;

create table if not exists log_localidade
(
  LOC_NU bigint not null
    primary key,
  LOC_NO_ABREV varchar(72) null,
  CEP varchar(16) null,
  LOC_NU_SUB bigint null,
  MUN_NU bigint null,
  LOC_NO varchar(144) null,
  LOC_IN_SIT varchar(2) null,
  LOC_IN_TIPO_LOC varchar(2) null,
  UFE_SG varchar(255) null,
  constraint LOC_NU
  unique (LOC_NU),
  constraint FK768E9917B31F2A81
  foreign key (UFE_SG) references log_faixa_uf (UFE_SG)
)
;

create index FK768E9917B31F2A81
  on log_localidade (UFE_SG)
;

alter table feriado
  add constraint FKF15849969BB6EC08
foreign key (LOCALIDADE_ID) references log_localidade (LOC_NU)
;

alter table log_bairro
  add constraint FK2C25836044246A2B
foreign key (LOC_NU) references log_localidade (LOC_NU)
;

alter table log_bairro
  add constraint FK2C258360C35A8410
foreign key (LOC_NU) references log_localidade (LOC_NU)
;

create table if not exists log_logradouro
(
  LOG_NU bigint not null
    primary key,
  LOG_NO_ABREV varchar(72) null,
  CEP varchar(16) null,
  BAI_NU_FIM bigint null,
  BAI_NU_INI bigint null,
  LOG_COMPLEMENTO varchar(200) null,
  LOG_NO varchar(144) null,
  LOG_STA_TLO varchar(2) null,
  TLO_TX varchar(72) null,
  UFE_SG varchar(4) null,
  LOC_NU bigint not null,
  constraint LOG_NU
  unique (LOG_NU),
  constraint FK8656C46FC35A8410
  foreign key (LOC_NU) references log_localidade (LOC_NU)
)
;

create index FK8656C46FC35A8410
  on log_logradouro (LOC_NU)
;

create table if not exists material_promocional
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  DESCRICAO varchar(255) not null
)
;

create table if not exists mix_cota_produto
(
  ID bigint auto_increment
    primary key,
  DATAHORA datetime null,
  REPARTE_MAX bigint null,
  REPARTE_MED bigint null,
  REPARTE_MIN bigint null,
  ULTIMO_REPARTE bigint null,
  VENDA_MED bigint null,
  ID_COTA bigint null,
  ID_PRODUTO bigint null,
  ID_USUARIO bigint null,
  CODIGO_ICD varchar(255) null,
  TIPO_CLASSIFICACAO_PRODUTO_ID bigint null,
  CODIGO_PRODUTO varchar(255) null,
  USAR_ICD_ESTUDO tinyint(1) default '1' not null,
  constraint FK7C18196F1916CB0
  foreign key (ID_COTA) references cota (ID)
)
;

create index FK7C1819610C84C95
  on mix_cota_produto (TIPO_CLASSIFICACAO_PRODUTO_ID)
;

create index FK7C1819693864D4C
  on mix_cota_produto (ID_USUARIO)
;

create index FK7C18196F1916CB0
  on mix_cota_produto (ID_COTA)
;

create index FK7C18196F34F8834
  on mix_cota_produto (ID_PRODUTO)
;

create index ndx_cota_icd_tipo
  on mix_cota_produto (ID_COTA, CODIGO_ICD, TIPO_CLASSIFICACAO_PRODUTO_ID)
;

create table if not exists motorista
(
  ID bigint auto_increment
    primary key,
  CNH varchar(255) null,
  NOME varchar(255) null,
  TRANSPORTADOR_ID bigint null
)
;

create index FK8E127EECD90B1440
  on motorista (TRANSPORTADOR_ID)
;

alter table assoc_veiculo_motorista_rota
  add constraint FK45A0E4B7B2639DFE
foreign key (MOTORISTA) references motorista (ID)
;

create table if not exists mov_cred
(
  data varchar(10) null,
  numero_cota int null,
  tipo_credito int null,
  desc_credito varchar(255) null,
  valor decimal(18,2) null,
  cota_id bigint null,
  motivo varchar(255) null,
  tipo char null,
  valor_decimal decimal(11,4) null,
  data_real date null
)
;

create table if not exists mov_deb
(
  data varchar(10) null,
  numero_cota int null,
  tipo_debito int null,
  desc_debito varchar(255) null,
  valor decimal(18,2) null,
  cota_id bigint null,
  motivo varchar(255) null,
  tipo char null,
  valor_decimal decimal(11,4) null,
  data_real date null
)
;

create table if not exists movimento_estoque
(
  id bigint auto_increment
    primary key,
  APROVADO_AUTOMATICAMENTE tinyint(1) null,
  DATA_APROVACAO date null,
  MOTIVO varchar(255) null,
  STATUS varchar(255) null,
  DATA date not null,
  DATA_CRIACAO date not null,
  NOTA_FISCAL_EMITIDA tinyint(1) default '0' not null,
  APROVADOR_ID bigint null,
  TIPO_MOVIMENTO_ID bigint not null,
  USUARIO_ID bigint not null,
  QTDE decimal(18,4) null,
  PRODUTO_EDICAO_ID bigint not null,
  ESTOQUE_PRODUTO_ID bigint null,
  ITEM_REC_FISICO_ID bigint null,
  DATA_INTEGRACAO date null,
  STATUS_INTEGRACAO varchar(255) null,
  COD_ORIGEM_MOTIVO varchar(255) null,
  DAT_EMISSAO_DOC_ACERTO datetime null,
  NUM_DOC_ACERTO bigint null,
  ORIGEM varchar(80) default 'MANUAL' null,
  FURO_PRODUTO_ID bigint null,
  INTEGRADO_COM_CE tinyint(1) default '1' not null,
  constraint FKEF44811545A3E9FA
  foreign key (ESTOQUE_PRODUTO_ID) references estoque_produto (ID),
  constraint FKEF44811573BDE210
  foreign key (ITEM_REC_FISICO_ID) references item_receb_fisico (ID)
    on delete set null,
  constraint FK_movimento_estoque_furo_produto
  foreign key (FURO_PRODUTO_ID) references furo_produto (ID)
)
;

create index data_movimento_estoque
  on movimento_estoque (DATA)
;

create index FKBEB397FC4E3C0541ef448115
  on movimento_estoque (TIPO_MOVIMENTO_ID)
;

create index FKBEB397FC7FFF790Eef448115
  on movimento_estoque (USUARIO_ID)
;

create index FKBEB397FCB0C2856Cef448115
  on movimento_estoque (APROVADOR_ID)
;

create index FKEF44811545A3E9FA
  on movimento_estoque (ESTOQUE_PRODUTO_ID)
;

create index FKEF44811573BDE210
  on movimento_estoque (ITEM_REC_FISICO_ID)
;

create index FKEF448115A53173D3
  on movimento_estoque (PRODUTO_EDICAO_ID)
;

create index FK_movimento_estoque_furo_produto
  on movimento_estoque (FURO_PRODUTO_ID)
;

create index NDX_STATUS
  on movimento_estoque (STATUS)
;

alter table atualizacao_estoque_gfs
  add constraint FK66A4BF1C232FFB12
foreign key (MOVIMENTO_ESTOQUE_ID) references movimento_estoque (ID)
;

alter table conferencia_encalhe
  add constraint FK8E92BB04232FFB12
foreign key (MOVIMENTO_ESTOQUE_ID) references movimento_estoque (ID)
;

alter table lancamento_diferenca
  add constraint FKE9CA6C10232FFB12
foreign key (MOVIMENTO_ESTOQUE_ID) references movimento_estoque (ID)
;

create table if not exists movimento_estoque_cota
(
  id bigint auto_increment
    primary key,
  APROVADO_AUTOMATICAMENTE tinyint(1) null,
  DATA_APROVACAO date null,
  MOTIVO varchar(255) null,
  STATUS varchar(255) null,
  DATA date not null,
  DATA_CRIACAO date not null,
  APROVADOR_ID bigint null,
  TIPO_MOVIMENTO_ID bigint not null,
  FORMA_COMERCIALIZACAO varchar(20) null,
  USUARIO_ID bigint not null,
  QTDE decimal(18,4) null,
  STATUS_ESTOQUE_FINANCEIRO varchar(255) null,
  PRODUTO_EDICAO_ID bigint not null,
  COTA_ID bigint not null,
  ESTOQUE_PROD_COTA_ID bigint null,
  ESTUDO_COTA_ID bigint null,
  LANCAMENTO_ID bigint null,
  DATA_INTEGRACAO date null,
  STATUS_INTEGRACAO varchar(255) null,
  MOVIMENTO_ESTOQUE_COTA_FURO_ID bigint null,
  MOVIMENTO_ESTOQUE_COTA_ESTORNO_ID bigint null,
  NOTA_FISCAL_EMITIDA tinyint(1) default '0' not null,
  GERAR_COTA_CONTRIBUINTE_ICMS tinyint(1) default '0' not null,
  GERAR_COTA_EXIGE_NFE tinyint(1) default '0' not null,
  COTA_CONTRIBUINTE_EXIGE_NF tinyint(1) default '0' not null,
  ORIGEM varchar(50) default 'MANUAL' null,
  LANCAMENTO_DIFERENCA_ID bigint null,
  DATA_LANCAMENTO_ORIGINAL date null,
  MOVIMENTO_FINANCEIRO_COTA_ID bigint null,
  PRECO_COM_DESCONTO decimal(18,4) null,
  PRECO_VENDA decimal(18,4) null,
  VALOR_DESCONTO decimal(18,4) null,
  NOTA_ENVIO_ITEM_NOTA_ENVIO_ID bigint null,
  NOTA_ENVIO_ITEM_SEQUENCIA int null,
  MOVIMENTO_ESTOQUE_COTA_JURAMENTADO_ID bigint null,
  constraint FK459444C3C8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK459444C362506D6B
  foreign key (ESTOQUE_PROD_COTA_ID) references estoque_produto_cota (ID),
  constraint FK459444C3714FA744
  foreign key (ESTUDO_COTA_ID) references estudo_cota (ID),
  constraint FK459444C345C07ACF
  foreign key (LANCAMENTO_ID) references lancamento (ID),
  constraint FK459444C370D7DF78
  foreign key (MOVIMENTO_ESTOQUE_COTA_FURO_ID) references movimento_estoque_cota (id),
  constraint FK459444C3C4673234
  foreign key (LANCAMENTO_DIFERENCA_ID) references lancamento_diferenca (ID),
  constraint movimento_estoque_cota_ibfk_1
  foreign key (MOVIMENTO_ESTOQUE_COTA_JURAMENTADO_ID) references movimento_estoque_cota (id)
)
;

create index FK459444C322F9188B
  on movimento_estoque_cota (MOVIMENTO_FINANCEIRO_COTA_ID)
;

create index FK459444C345C07ACF
  on movimento_estoque_cota (LANCAMENTO_ID, ESTUDO_COTA_ID, NOTA_ENVIO_ITEM_SEQUENCIA)
;

create index FK459444C35644B37C
  on movimento_estoque_cota (PRODUTO_EDICAO_ID)
;

create index FK459444C362506D6B
  on movimento_estoque_cota (ESTOQUE_PROD_COTA_ID)
;

create index FK459444C370D7DF78
  on movimento_estoque_cota (MOVIMENTO_ESTOQUE_COTA_FURO_ID)
;

create index FK459444C3714FA744
  on movimento_estoque_cota (ESTUDO_COTA_ID)
;

create index FK459444C3A53173D3
  on movimento_estoque_cota (PRODUTO_EDICAO_ID)
;

create index FK459444C3C4673234
  on movimento_estoque_cota (LANCAMENTO_DIFERENCA_ID)
;

create index FK459444C3C8181F74
  on movimento_estoque_cota (COTA_ID)
;

create index FKBEB397FC4E3C0541459444c3
  on movimento_estoque_cota (TIPO_MOVIMENTO_ID)
;

create index FKBEB397FC7FFF790E459444c3
  on movimento_estoque_cota (USUARIO_ID)
;

create index FKBEB397FCB0C2856C459444c3
  on movimento_estoque_cota (APROVADOR_ID)
;

create index FKFDSA89F7CD8979AF
  on movimento_estoque_cota (NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, NOTA_ENVIO_ITEM_SEQUENCIA)
;

create index MOVIMENTO_ESTOQUE_COTA_JURAMENTADO_ID
  on movimento_estoque_cota (MOVIMENTO_ESTOQUE_COTA_JURAMENTADO_ID)
;

create index NDX_COTA_DATA_IDX
  on movimento_estoque_cota (COTA_ID, DATA)
;

create index NDX_COTA_LANCAMENTO
  on movimento_estoque_cota (COTA_ID, LANCAMENTO_ID)
;

create index NDX_DATA
  on movimento_estoque_cota (DATA)
;

create index NDX_MEC_COTA_FURO_STATUSFINANC_1
  on movimento_estoque_cota (STATUS_ESTOQUE_FINANCEIRO, COTA_ID, MOVIMENTO_ESTOQUE_COTA_FURO_ID)
;

create index NDX_MEC_NOTA_ENVIO
  on movimento_estoque_cota (COTA_ID, ESTUDO_COTA_ID, NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, NOTA_ENVIO_ITEM_SEQUENCIA)
;

create index NDX_PRODUTO_EDICAO
  on movimento_estoque_cota (PRODUTO_EDICAO_ID, COTA_ID, DATA)
;

create index NDX_STATUS_ESTOQUE_FINANCEIRO
  on movimento_estoque_cota (STATUS_ESTOQUE_FINANCEIRO, STATUS, TIPO_MOVIMENTO_ID)
;

create trigger ID_POPULATOR
before INSERT on movimento_estoque_cota
for each row
  BEGIN
    IF (NEW.id = -1) THEN
      SET @new_id = (SELECT sequence_next_hi_value FROM SEQ_GENERATOR WHERE sequence_name = 'Movimento' FOR UPDATE);
      SET NEW.id = @new_id;
      UPDATE SEQ_GENERATOR SET sequence_next_hi_value = (@new_id + 1) WHERE sequence_next_hi_value = @new_id AND sequence_name = 'Movimento';
    END IF;
  END;

alter table conferencia_encalhe
  add constraint FK8E92BB04BBE20E9D
foreign key (MOVIMENTO_ESTOQUE_COTA_ID) references movimento_estoque_cota (ID)
;

alter table cota_ausente_movimento_estoque_cota
  add constraint FK7D6984EFBBE20E9D
foreign key (MOVIMENTO_ESTOQUE_COTA_ID) references movimento_estoque_cota (ID)
;

alter table lancamento_diferenca_movimento_estoque_cota
  add constraint FKE95125D2BBE20E9D
foreign key (MOVIMENTO_ESTOQUE_COTA_ID) references movimento_estoque_cota (ID)
;

create table if not exists movimento_fechamento_fiscal_cota
(
  ID bigint auto_increment
    primary key,
  TIPO_MOVIMENTO_ID bigint not null,
  DATA date not null,
  DATA_CRIACAO timestamp default CURRENT_TIMESTAMP not null,
  NOTA_FISCAL_DEVOLUCAO_SIMBOLICA_EMITIDA tinyint(1) default '0' not null,
  DESOBRIGA_NOTA_FISCAL_DEVOLUCAO_SIMBOLICA tinyint(1) default '0' not null,
  NOTA_FISCAL_VENDA_EMITIDA tinyint(1) default '0' not null,
  DESOBRIGA_NOTA_FISCAL_VENDA tinyint(1) default '0' not null,
  NOTA_FISCAL_LIBERADA_EMISSAO varchar(45) default '0' not null,
  QTDE decimal(18,4) not null,
  TIPO_DESTINATARIO varchar(45) not null,
  COTA_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  CHAMADA_ENCALHE_COTA_ID bigint null,
  PRECO_COM_DESCONTO decimal(18,4) null,
  PRECO_VENDA decimal(18,4) null,
  VALOR_DESCONTO decimal(18,4) null,
  QTDE_CHAMADA_ENC_ANTERIOR varchar(45) default '0' null,
  constraint fk_MOVIMENTO_FECHAMENTO_FISCAL_COTA_1
  foreign key (COTA_ID) references cota (ID),
  constraint fk_movimento_fechamento_fiscal_cota_2
  foreign key (CHAMADA_ENCALHE_COTA_ID) references chamada_encalhe_cota (ID)
)
;

create index fk_MOVIMENTO_FECHAMENTO_FISCAL_COTA_1_idx
  on movimento_fechamento_fiscal_cota (COTA_ID)
;

create index fk_movimento_fechamento_fiscal_cota_2_idx
  on movimento_fechamento_fiscal_cota (CHAMADA_ENCALHE_COTA_ID)
;

create index fk_movimento_fechamento_fiscal_tipo_movimento_1
  on movimento_fechamento_fiscal_cota (TIPO_MOVIMENTO_ID)
;

create table if not exists movimento_fechamento_fiscal_fornecedor
(
  ID bigint not null
    primary key,
  DATA date not null,
  DATA_CRIACAO timestamp default CURRENT_TIMESTAMP not null,
  DESOBRIGA_NOTA_FISCAL_DEVOLUCAO_SIMBOLICA tinyint(1) default '0' not null,
  NOTA_FISCAL_DEVOLUCAO_SIMBOLICA_EMITIDA tinyint(1) default '0' not null,
  NOTA_FISCAL_LIBERADA_EMISSAO tinyint(1) default '0' not null,
  QTDE decimal(18,4) not null,
  TIPO_DESTINATARIO varchar(45) not null,
  TIPO_MOVIMENTO_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  PRECO_COM_DESCONTO decimal(18,4) null,
  PRECO_VENDA decimal(18,4) null,
  VALOR_DESCONTO decimal(18,4) null,
  QTDE_CHAMADA_ENC_ANTERIOR varchar(45) null,
  constraint fk_MOV_FECH_FISCAL_FORN_FORNECEDOR_1
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index fk_MOVIMENTO_FECHAMENTO_FISCAL_FORNECEDOR_1_idx
  on movimento_fechamento_fiscal_fornecedor (PRODUTO_EDICAO_ID)
;

create index fk_MOV_FECH_FISCAL_FORN_FORNECEDOR_1_idx
  on movimento_fechamento_fiscal_fornecedor (FORNECEDOR_ID)
;

create index fk_MOV_FECH_FISCAL_FORN_TIPO_MOVIMENTO_1_idx
  on movimento_fechamento_fiscal_fornecedor (TIPO_MOVIMENTO_ID)
;

create table if not exists movimento_fechamento_fiscal_origem_item
(
  ID bigint auto_increment
    primary key,
  TIPO varchar(45) not null,
  ORIGEM varchar(45) not null,
  MOVIMENTO_FECHAMENTO_FISCAL_ID bigint not null,
  MOVIMENTO_ID bigint default '-1' not null,
  constraint movimento_fechamento_estoque_unique
  unique (MOVIMENTO_FECHAMENTO_FISCAL_ID, MOVIMENTO_ID, TIPO)
)
;

create table if not exists movimento_financeiro_cota
(
  id bigint auto_increment
    primary key,
  APROVADO_AUTOMATICAMENTE tinyint(1) null,
  DATA_APROVACAO date null,
  MOTIVO varchar(255) null,
  STATUS varchar(255) null,
  DATA date not null,
  DATA_CRIACAO date not null,
  APROVADOR_ID bigint null,
  TIPO_MOVIMENTO_ID bigint not null,
  USUARIO_ID bigint not null,
  PARCELAS int null,
  PRAZO int null,
  VALOR decimal(18,4) null,
  LANCAMENTO_MANUAL tinyint(1) not null,
  OBSERVACAO varchar(255) null,
  BAIXA_COBRANCA_ID bigint null,
  COTA_ID bigint not null,
  DATA_INTEGRACAO date null,
  STATUS_INTEGRACAO varchar(255) null,
  FORNECEDOR_ID bigint not null,
  constraint FKC67EE7A9E7EBBF1A
  foreign key (BAIXA_COBRANCA_ID) references baixa_cobranca (ID),
  constraint FKC67EE7A9C8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK_MOVIMENTO_FINANCEIRO_COTA_FORNECEDOR_ID
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index FKBEB397FC4E3C0541c67ee7a9
  on movimento_financeiro_cota (TIPO_MOVIMENTO_ID)
;

create index FKBEB397FC7FFF790Ec67ee7a9
  on movimento_financeiro_cota (USUARIO_ID)
;

create index FKBEB397FCB0C2856Cc67ee7a9
  on movimento_financeiro_cota (APROVADOR_ID)
;

create index FKC67EE7A9C8181F74
  on movimento_financeiro_cota (COTA_ID)
;

create index FKC67EE7A9E7EBBF1A
  on movimento_financeiro_cota (BAIXA_COBRANCA_ID)
;

create index FK_MOVIMENTO_FINANCEIRO_COTA_FORNECEDOR_ID
  on movimento_financeiro_cota (FORNECEDOR_ID, DATA)
;

create index idx_DATA_COTA
  on movimento_financeiro_cota (DATA, COTA_ID, TIPO_MOVIMENTO_ID)
;

alter table acumulo_divida
  add constraint acumulo_divida_ibfk_2
foreign key (MOV_PENDENTE_ID) references movimento_financeiro_cota (ID)
;

alter table acumulo_divida
  add constraint acumulo_divida_ibfk_3
foreign key (MOV_JUROS_ID) references movimento_financeiro_cota (ID)
;

alter table acumulo_divida
  add constraint acumulo_divida_ibfk_4
foreign key (MOV_MULTA_ID) references movimento_financeiro_cota (ID)
;

alter table consolidado_mvto_financeiro_cota
  add constraint FK6303D6CDE6E795C3
foreign key (MVTO_FINANCEIRO_COTA_ID) references movimento_financeiro_cota (ID)
;

alter table historico_movto_financeiro_cota
  add constraint FKAB46D6EDC7F49D38
foreign key (MOVTO_FINANCEIRO_COTA_ID) references movimento_financeiro_cota (ID)
;

alter table movimento_estoque_cota
  add constraint FK459444C322F9188B
foreign key (MOVIMENTO_FINANCEIRO_COTA_ID) references movimento_financeiro_cota (ID)
;

create table if not exists natureza_operacao
(
  ID bigint auto_increment
    primary key,
  NOTA_FISCAL_VENDA_CONSIGNADO tinyint(1) not null,
  NOTA_FISCAL_DEVOLUCAO_SIMBOLICA tinyint(1) not null,
  GERAR_COTA_CONTRIBUINTE_ICMS tinyint(1) not null,
  GERAR_COTA_EXIGE_NFE tinyint(1) not null,
  GERAR_COTA_NAO_EXIGE_NFE tinyint(1) not null,
  GERAR_NOTAS_REFERENCIADAS tinyint(1) not null,
  DESCRICAO varchar(255) not null,
  CFOP_ESTADO bigint not null,
  CFOP_OUTROS_ESTADOS bigint not null,
  CFOP_EXTERIOR varchar(45) null,
  TIPO_ATIVIDADE varchar(255) not null,
  TIPO_EMITENTE varchar(45) not null,
  TIPO_DESTINATARIO varchar(45) not null,
  TIPO_OPERACAO varchar(255) not null,
  FORMA_COMERCIALIZACAO varchar(20) not null,
  NOTA_FISCAL_SERIE bigint not null,
  NOTA_FISCAL_NUMERO_NF bigint default '0' not null,
  DATA_CRIACAO datetime not null,
  LAST_UPDATE_TIME datetime not null,
  USUARIO_LOGADO varchar(45) not null
)
;

create index FK922056A44B86991A
  on natureza_operacao (CFOP_ESTADO)
;

create trigger natureza_operacao_BINS
before INSERT on natureza_operacao
for each row
  BEGIN
    SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP, NEW.DATA_CRIACAO = CURRENT_TIMESTAMP, NEW.USUARIO_LOGADO=USER();
  END;

create trigger natureza_operacao_BUPD
before UPDATE on natureza_operacao
for each row
  BEGIN
    SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP, NEW.USUARIO_LOGADO=USER();
  END;

create table if not exists natureza_operacao_nota_envio
(
  distribuidor_id bigint not null,
  natureza_operacao_id bigint not null,
  constraint dist_id_nop_id_UNIQUE
  unique (distribuidor_id, natureza_operacao_id)
)
;

create table if not exists natureza_operacao_tipo_movimento
(
  NATUREZA_OPERACAO_ID bigint not null,
  TIPO_MOVIMENTO_ID bigint not null,
  primary key (NATUREZA_OPERACAO_ID, TIPO_MOVIMENTO_ID),
  constraint FK_natureza_operacao_tipo_movimento
  foreign key (NATUREZA_OPERACAO_ID) references natureza_operacao (ID)
)
;

create index FK_tipo_movimento_id
  on natureza_operacao_tipo_movimento (TIPO_MOVIMENTO_ID)
;

create table if not exists natureza_operacao_tributo
(
  NATUREZA_OPERACAO_ID bigint not null,
  TRIBUTO_ID bigint not null,
  primary key (NATUREZA_OPERACAO_ID, TRIBUTO_ID),
  constraint FK_natureza_operacao_tributo
  foreign key (NATUREZA_OPERACAO_ID) references natureza_operacao (ID)
)
;

create index FK_tributo_id
  on natureza_operacao_tributo (TRIBUTO_ID)
;

create table if not exists ncm
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  DESCRICAO varchar(255) not null,
  UNIDADE_MEDIDA varchar(255) not null,
  GRUPO bigint null,
  IMPOSTO tinyint(1) default '0' null,
  constraint CODIGO
  unique (CODIGO)
)
;

create table if not exists negociacao
(
  ID bigint auto_increment
    primary key,
  ATIVAR_PAGAMENTO_APOS_PARCELA int null,
  COMISSAO_PARA_SALDO_DIVIDA decimal(18,4) null,
  ISENTA_ENCARGOS tinyint(1) null,
  NEGOCIACAO_AVULSA tinyint(1) null,
  VALOR_DIVIDA_PAGA_COMISSAO decimal(18,4) null,
  FORMA_COBRANCA_ID bigint null,
  DATA_CRIACAO datetime null,
  VALOR_ORIGINAL decimal(18,4) null,
  COMISSAO_ORIGINAL_COTA decimal(18,4) null,
  TIPO_NEGOCIACAO varchar(255) null,
  constraint FKFDCA12D5E34F875B
  foreign key (FORMA_COBRANCA_ID) references forma_cobranca (id)
)
;

create index FKFDCA12D5E34F875B
  on negociacao (FORMA_COBRANCA_ID)
;

create table if not exists negociacao_cobranca_originaria
(
  NEGOCIACAO_ID bigint not null,
  COBRANCA_ID bigint not null,
  constraint COBRANCA_ID
  unique (COBRANCA_ID),
  constraint FKDFAF66FDFCD23D41
  foreign key (NEGOCIACAO_ID) references negociacao (ID),
  constraint FKDFAF66FDD89C75C1
  foreign key (COBRANCA_ID) references cobranca (ID)
)
;

create index FKDFAF66FDD89C75C1
  on negociacao_cobranca_originaria (COBRANCA_ID)
;

create index FKDFAF66FDFCD23D41
  on negociacao_cobranca_originaria (NEGOCIACAO_ID)
;

create table if not exists negociacao_mov_finan
(
  NEGOCIACAO_ID bigint null,
  MOV_FINAN_ID bigint null,
  constraint fk_negociacao_id
  foreign key (NEGOCIACAO_ID) references negociacao (ID),
  constraint fk_mvto_finan_id
  foreign key (MOV_FINAN_ID) references movimento_financeiro_cota (id)
)
;

create index fk_mvto_finan_id
  on negociacao_mov_finan (MOV_FINAN_ID)
;

create index fk_negociacao_id
  on negociacao_mov_finan (NEGOCIACAO_ID)
;

create table if not exists nomes
(
  id int null,
  nome char(50) null,
  razao char(50) null
)
;

create table if not exists nota_envio
(
  numero bigint auto_increment
    primary key,
  CHAVE_ACESSO varchar(44) null,
  CODIGO_NATUREZA_OPERACAO int null,
  dataEmissao date null,
  DESCRICAO_NATUREZA_OPERACAO varchar(255) null,
  CODIGO_BOX int null,
  CODIGO_ROTA varchar(255) null,
  DESCRICAO_ROTA varchar(255) null,
  DOCUMENTO_DESTINATARIO varchar(50) not null,
  IE_DESTINATARIO varchar(20) null,
  NOME_DESTINATARIO varchar(60) not null,
  NOME_BOX varchar(255) null,
  NUMERO_COTA int not null,
  DOCUMENTO_EMITENTE varchar(14) not null,
  IE_EMITENTE varchar(14) not null,
  NOME_EMITENTE varchar(60) not null,
  ENDERECO_ID_DESTINATARIO bigint not null,
  PESSOA_DESTINATARIO_ID_REFERENCIA bigint null,
  TELEFONE_ID_DESTINATARIO bigint null,
  ENDERECO_ID_EMITENTE bigint not null,
  PESSOA_EMITENTE_ID_REFERENCIADA bigint null,
  TELEFONE_ID_EMITENTE bigint null,
  NOTA_IMPRESSA tinyint(1) not null,
  NOTA_FISCAL_ID bigint null
)
;

create index FK59E3F7224C92964
  on nota_envio (TELEFONE_ID_DESTINATARIO)
;

create index FK59E3F722742B297E
  on nota_envio (ENDERECO_ID_EMITENTE)
;

create index FK59E3F72278441BC1
  on nota_envio (PESSOA_DESTINATARIO_ID_REFERENCIA)
;

create index FK59E3F7228B188640
  on nota_envio (TELEFONE_ID_EMITENTE)
;

create index FK59E3F722B281499A
  on nota_envio (PESSOA_EMITENTE_ID_REFERENCIADA)
;

create index FK59E3F722E7D38522
  on nota_envio (ENDERECO_ID_DESTINATARIO)
;

create table if not exists nota_envio_endereco
(
  ID bigint auto_increment
    primary key,
  TIPO_LOGRADOURO varchar(60) null,
  LOGRADOURO varchar(60) null,
  NUMERO varchar(60) null,
  UF varchar(2) null,
  CIDADE varchar(60) null,
  COMPLEMENTO varchar(60) null,
  BAIRRO varchar(60) null,
  CEP varchar(60) null,
  CODIGO_PAIS varchar(20) null,
  PAIS varchar(60) null,
  CODIGO_CIDADE_IBGE bigint null,
  CODIGO_UF bigint null
)
;

alter table nota_envio
  add constraint fk_ne_nota_envio_endereco
foreign key (ENDERECO_ID_DESTINATARIO) references nota_envio_endereco (ID)
;

create table if not exists nota_envio_item
(
  SEQUENCIA int not null,
  CODIGO_PRODUTO varchar(60) not null,
  DESCONTO decimal(18,4) null,
  NUMERO_EDICAO bigint not null,
  PRECO_CAPA decimal(18,4) null,
  PUBLICACAO varchar(120) not null,
  REPARTE decimal(18,4) null,
  NOTA_ENVIO_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  ESTUDO_COTA_ID bigint null,
  SEQ_MATRIZ_LANCAMENTO int null,
  FURO_PRODUTO_ID bigint null,
  primary key (NOTA_ENVIO_ID, SEQUENCIA),
  constraint FK18605A10401E39D3
  foreign key (NOTA_ENVIO_ID) references nota_envio (numero),
  constraint FK18605A10714FA744
  foreign key (ESTUDO_COTA_ID) references estudo_cota (ID),
  constraint FK_nota_envio_item_estudo_cota
  foreign key (ESTUDO_COTA_ID) references estudo_cota (ID)
)
;

create index FK18605A10401E39D3
  on nota_envio_item (NOTA_ENVIO_ID)
;

create index FK18605A10714FA744
  on nota_envio_item (ESTUDO_COTA_ID)
;

create index FK18605A10A53173D3
  on nota_envio_item (PRODUTO_EDICAO_ID)
;

create index NEI_EC_ID_PE_ID
  on nota_envio_item (PRODUTO_EDICAO_ID, ESTUDO_COTA_ID)
;

create index NEI_FURO_NDX
  on nota_envio_item (FURO_PRODUTO_ID, ESTUDO_COTA_ID)
;

alter table movimento_estoque_cota
  add constraint FKFDSA89F7CD8979AF
foreign key (NOTA_ENVIO_ITEM_NOTA_ENVIO_ID, NOTA_ENVIO_ITEM_SEQUENCIA) references nota_envio_item (NOTA_ENVIO_ID, SEQUENCIA)
;

create table if not exists nota_fiscal_encargo_financeiro_produto
(
  ID bigint not null
    primary key,
  CST_COFINS int null,
  PER_ALIQ_COFINS decimal(18,4) null,
  QTD_VENDIDA_COFINS decimal(18,4) null,
  VLR_COFINS decimal(18,4) null,
  VLR_ALIQ_COFINS decimal(18,4) null,
  VLR_BASE_CALC_COFINS decimal(18,4) null,
  CST_COFINS_ST int null,
  PER_ALIQ_COFINS_ST decimal(18,4) null,
  QTD_VENDIDA_COFINS_ST decimal(18,4) null,
  VLR_COFINS_ST decimal(18,4) null,
  VLR_ALIQ_COFINS_ST decimal(18,4) null,
  VLR_BASE_CALC_COFINS_ST decimal(18,4) null,
  CST_PIS int null,
  PER_ALIQ_PIS decimal(18,4) null,
  QTD_VENDIDA_PIS decimal(18,4) null,
  VLR_PIS decimal(18,4) null,
  VLR_ALIQ_PIS decimal(18,4) null,
  VLR_BASE_CALC_PIS decimal(18,4) null,
  CST_PIS_ST int null,
  PER_ALIQ_PIS_ST decimal(18,4) null,
  QTD_VENDIDA_PIS_ST decimal(18,4) null,
  VLR_PIS_ST decimal(18,4) null,
  VLR_ALIQ_PIS_ST decimal(18,4) null,
  VLR_BASE_CALC_PIS_ST decimal(18,4) null,
  NOTA_FISCAL_ID bigint null,
  PRODUTO_SERVICO_SEQUENCIA int null,
  PERCENTUAL_ADCIONADO_ST decimal(18,4) null,
  MODELIDADE_ST int null,
  ORIGEM_ST int null,
  PERCENTUAL_REDUCAO_ST decimal(18,4) null,
  ALIQUOTA_ICMS_ST decimal(18,4) null,
  CST_ICMS_ST varchar(2) null,
  VLR_ICMS_ST decimal(18,4) null,
  VLR_BASE_CALC_ICMS_ST decimal(18,4) null,
  MOTIVO_DESONERACAO int null,
  MODELIDADE int null,
  ORIGEM int not null,
  PERCENTUAL_REDUCAO decimal(18,4) null,
  ALIQUOTA_ICMS decimal(18,4) null,
  CST_ICMS varchar(2) not null,
  VLR_ICMS decimal(18,4) null,
  VLR_BASE_CALC_ICMS decimal(18,4) null,
  CLASSE_ENQUADRAMENTO_IPI varchar(5) null,
  CNPJ_PRODUTOR_IPI varchar(14) null,
  CODIGO_ENQUADRAMENTO_IPI varchar(3) null,
  CODIGO_SELO_IPI varchar(60) null,
  QUANTIDADE_SELO_IPI bigint null,
  QUANTIDADE_UNIDADES double null,
  VALOR_UNIDADE_TRIBUTAVEL_IPI double null,
  ALIQUOTA_IPI decimal(18,4) null,
  CST_IPI varchar(2) null,
  VLR_IPI decimal(18,4) null,
  VLR_BASE_CALC_IPI decimal(18,4) null
)
;

create index FKE8D1773F35367812c7153222
  on nota_fiscal_encargo_financeiro_produto (NOTA_FISCAL_ID, PRODUTO_SERVICO_SEQUENCIA)
;

create table if not exists nota_fiscal_encargo_financeiro_servico
(
  ID bigint not null
    primary key,
  CST_COFINS int null,
  PER_ALIQ_COFINS decimal(18,4) null,
  QTD_VENDIDA_COFINS decimal(18,4) null,
  VLR_COFINS decimal(18,4) null,
  VLR_ALIQ_COFINS decimal(18,4) null,
  VLR_BASE_CALC_COFINS decimal(18,4) null,
  CST_COFINS_ST int null,
  PER_ALIQ_COFINS_ST decimal(18,4) null,
  QTD_VENDIDA_COFINS_ST decimal(18,4) null,
  VLR_COFINS_ST decimal(18,4) null,
  VLR_ALIQ_COFINS_ST decimal(18,4) null,
  VLR_BASE_CALC_COFINS_ST decimal(18,4) null,
  CST_PIS int null,
  PER_ALIQ_PIS decimal(18,4) null,
  QTD_VENDIDA_PIS decimal(18,4) null,
  VLR_PIS decimal(18,4) null,
  VLR_ALIQ_PIS decimal(18,4) null,
  VLR_BASE_CALC_PIS decimal(18,4) null,
  CST_PIS_ST int null,
  PER_ALIQ_PIS_ST decimal(18,4) null,
  QTD_VENDIDA_PIS_ST decimal(18,4) null,
  VLR_PIS_ST decimal(18,4) null,
  VLR_ALIQ_PIS_ST decimal(18,4) null,
  VLR_BASE_CALC_PIS_ST decimal(18,4) null,
  NOTA_FISCAL_ID bigint null,
  PRODUTO_SERVICO_SEQUENCIA int null,
  COD_MUNICIPIO int not null,
  ITEM_LISTA_SERVICO int not null,
  ALIQUOTA_ISSQN decimal(18,4) null,
  CST_ISSQN varchar(1) not null,
  VLR_ISSQN decimal(18,4) null,
  VLR_BASE_CALC_ISSQN decimal(18,4) null
)
;

create index FKE8D1773F353678124fcb29e8
  on nota_fiscal_encargo_financeiro_servico (NOTA_FISCAL_ID, PRODUTO_SERVICO_SEQUENCIA)
;

create table if not exists nota_fiscal_endereco
(
  ID bigint auto_increment
    primary key,
  TIPO_LOGRADOURO varchar(60) null,
  LOGRADOURO varchar(60) null,
  NUMERO varchar(60) null,
  UF varchar(2) null,
  CIDADE varchar(60) null,
  COMPLEMENTO varchar(60) null,
  BAIRRO varchar(60) null,
  CEP varchar(60) null,
  CODIGO_PAIS varchar(20) not null,
  PAIS varchar(60) null,
  CODIGO_CIDADE_IBGE bigint not null,
  CODIGO_UF bigint not null
)
;

create table if not exists nota_fiscal_entrada
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  ISSQN_BASE decimal(18,4) null,
  ISSQN_TOTAL decimal(18,4) null,
  ISSQN_VALOR decimal(18,4) null,
  AMBIENTE varchar(255) null,
  CHAVE_ACESSO varchar(255) null,
  DATA_EMISSAO datetime not null,
  DATA_EXPEDICAO datetime not null,
  EMISSOR_INSCRICAO_ESTADUAL_SUBSTITUTO varchar(255) null,
  EMISSOR_INSCRICAO_MUNICIPAL varchar(255) null,
  EMITIDA tinyint(1) not null,
  FORMA_PAGAMENTO varchar(255) null,
  FRETE int null,
  HORA_SAIDA varchar(255) null,
  INFORMACOES_COMPLEMENTARES varchar(255) null,
  MOVIMENTO_INTEGRACAO varchar(255) null,
  DESCRICAO_NATUREZA_OPERACAO varchar(255) null,
  NUMERO bigint null,
  NUMERO_FATURA varchar(255) null,
  PROTOCOLO varchar(255) null,
  SERIE bigint null,
  STATUS_EMISSAO varchar(255) null,
  STATUS_RECEBIMENTO varchar(255) null,
  STATUS_EMISSAO_NFE varchar(255) null,
  TIPO_EMISSAO_NFE varchar(255) null,
  TRANSPORTADORA_ANTT varchar(255) null,
  TRANSPORTADORA_CNPJ varchar(255) null,
  TRANSPORTADORA_ENDERECO varchar(255) null,
  TRANSPORTADORA_ESPECIE varchar(255) null,
  TRANSPORTADORA_INSCRICAO_ESTADUAL varchar(255) null,
  TRANSPORTADORA_MARCA varchar(255) null,
  TRANSPORTADORA_MUNICIPIO varchar(255) null,
  TRANSPORTADORA_NOME varchar(255) null,
  TRANSPORTADORA_NUMERACAO varchar(255) null,
  TRANSPORTADORA_PESO_BRUTO decimal(18,4) null,
  TRANSPORTADORA_PESO_LIQUIDO decimal(18,4) null,
  TRANSPORTADORA_PLACA_VEICULO varchar(255) null,
  TRANSPORTADORA_PLACA_VEICULO_UF varchar(255) null,
  TRANSPORTADORA_QUANTIDADE varchar(255) null,
  TRANSPORTADORA_UF varchar(255) null,
  VALOR_BASE_ICMS decimal(18,4) null,
  VALOR_BASE_ICMS_SUBSTITUTO decimal(18,4) null,
  VALOR_BRUTO decimal(18,4) null,
  VALOR_DESCONTO decimal(18,4) null,
  VALOR_FATURA decimal(18,4) null,
  VALOR_FRETE decimal(18,4) null,
  VALOR_ICMS decimal(18,4) null,
  VALOR_ICMS_SUBSTITUTO decimal(18,4) null,
  VALOR_IPI decimal(18,4) null,
  VALOR_LIQUIDO decimal(18,4) null,
  VALOR_NF decimal(18,4) null,
  VALOR_OUTRO decimal(18,4) null,
  VALOR_PRODUTOS decimal(18,4) null,
  VALOR_SEGURO decimal(18,4) null,
  VERSAO varchar(255) null,
  ORIGEM varchar(255) not null,
  STATUS_NOTA_FISCAL varchar(255) not null,
  NATUREZA_OPERACAO_ID bigint not null,
  CFOP_ID bigint null,
  USUARIO_ID bigint null,
  CONTROLE_CONFERENCIA_ENCALHE_COTA_ID bigint null,
  COTA_ID bigint null,
  FORNECEDOR_ID bigint null,
  pj_id bigint null,
  DATA_RECEBIMENTO datetime null,
  VALOR_INFORMADO decimal(18,4) null,
  NUMERO_NOTA_ENVIO bigint null,
  constraint FK_nf_entrada_nat_operacao
  foreign key (NATUREZA_OPERACAO_ID) references natureza_operacao (ID),
  constraint FK90AD5277AB6324F
  foreign key (CFOP_ID) references cfop (ID),
  constraint FK90AD527D3D96F81
  foreign key (CONTROLE_CONFERENCIA_ENCALHE_COTA_ID) references controle_conferencia_encalhe_cota (ID),
  constraint FK90AD527C8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK90AD5279808F874
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index FK90AD527479FD586
  on nota_fiscal_entrada (pj_id)
;

create index FK90AD5277AB6324F
  on nota_fiscal_entrada (CFOP_ID)
;

create index FK90AD5277FEEEECC
  on nota_fiscal_entrada (NATUREZA_OPERACAO_ID)
;

create index FK90AD5277FFF790E
  on nota_fiscal_entrada (USUARIO_ID)
;

create index FK90AD5279808F874
  on nota_fiscal_entrada (FORNECEDOR_ID)
;

create index FK90AD527C8181F74
  on nota_fiscal_entrada (COTA_ID)
;

create index FK90AD527D3D96F81
  on nota_fiscal_entrada (CONTROLE_CONFERENCIA_ENCALHE_COTA_ID)
;

alter table item_nota_fiscal_entrada
  add constraint FK3EDE8DFBC74D9881
foreign key (NOTA_FISCAL_ID) references nota_fiscal_entrada (ID)
;

create table if not exists nota_fiscal_fatura
(
  ID bigint not null
    primary key,
  NUMERO varchar(60) not null,
  VALOR decimal(18,4) not null,
  VENCIMENTO datetime not null,
  NOTA_FISCAL_NOVO_ID bigint not null
)
;

create index fk_NOTA_FISCAL_FATURA_1_idx
  on nota_fiscal_fatura (NOTA_FISCAL_NOVO_ID)
;

create table if not exists nota_fiscal_item_nota_fiscal_origem_item
(
  PRODUTO_SERVICO_SEQUENCIA bigint not null,
  ORIGEM_ITEM_NOTA_FISCAL_ID bigint not null,
  NOTA_FISCAL_ID bigint not null,
  PRODUTO_EDICAO_ID bigint null,
  primary key (PRODUTO_SERVICO_SEQUENCIA, ORIGEM_ITEM_NOTA_FISCAL_ID, NOTA_FISCAL_ID)
)
;

create table if not exists nota_fiscal_nota_fiscal_item
(
  NOTA_FISCAL_ID bigint not null,
  notaFiscalItens_ID bigint not null,
  primary key (NOTA_FISCAL_ID, notaFiscalItens_ID)
)
;

create table if not exists nota_fiscal_novo
(
  id bigint auto_increment
    primary key,
  CONDICAO varchar(255) null,
  DATA_EMISSAO date not null,
  DATA_ENTRADA_CONTIGENCIA datetime null,
  DATA_SAIDA_ENTRADA date null,
  DESCRICAO_NATUREZA_OPERACAO varchar(60) not null,
  FINALIDADE_EMISSAO tinyint(1) not null,
  INDICADOR_FORMA_PAGAMENTO int not null,
  HORA_SAIDA_ENTRADA datetime null,
  JUSTIFICATIVA_ENTRADA_CONTIGENCIA varchar(256) null,
  NUMERO_DOCUMENTO_FISCAL bigint not null,
  SERIE int not null,
  TIPO_EMISSAO int null,
  TIPO_OPERACAO int not null,
  DOCUMENTO_DESTINATARIO varchar(14) not null,
  EMAIL_DESTINATARIO varchar(60) null,
  IE_DESTINATARIO varchar(25) null,
  ISUF_DESTINATARIO varchar(9) null,
  NOME_DESTINATARIO varchar(60) not null,
  NOME_FANTASIA_DESTINATARIO varchar(60) null,
  CNAE_EMITENTE varchar(20) null,
  DOCUMENTO_EMITENTE varchar(14) not null,
  IE_EMITENTE varchar(25) not null,
  IE_SUBSTITUTO_TRIBUTARIO_EMITENTE varchar(14) null,
  IM_EMITENTE varchar(15) null,
  NOME_EMITENTE varchar(60) not null,
  NOME_FANTASIA_EMITENTE varchar(60) null,
  CRT_EMITENTE int null,
  INF_CPL varchar(5000) null,
  CHAVE_ACESSO varchar(44) null,
  DATA_RECEBIMENTO datetime null,
  MOTIVO varchar(255) null,
  PROTOCOLO bigint null,
  STATUS_RETORNADO varchar(255) null,
  DOCUMENTO_TRANS varchar(14) null,
  IE_TRANS varchar(14) null,
  MODALIDADE_FRETE int not null,
  MUN_TRANS varchar(60) null,
  NOME_TRANS varchar(60) null,
  UF_TRANS varchar(2) null,
  PLACA_VEICULO_TRANS varchar(7) null,
  RNTC_VEICULO_TRANS varchar(20) null,
  UF_VEICULO_TRANS varchar(2) null,
  VL_BC_ICMS decimal(18,4) null,
  VL_BC_ICMS_ST decimal(18,4) null,
  VL_TOTAL_COFINS decimal(18,4) null,
  VL_TOTAL_DESCONTO decimal(18,4) null,
  VL_TOTAL_FRETE decimal(18,4) null,
  VL_TOTAL_ICMS decimal(18,4) null,
  VL_TOTAL_ICMS_ST decimal(18,4) null,
  VL_TOTAL_IPI decimal(18,4) null,
  VL_TOTAL_NF decimal(18,4) null,
  VL_TOTAL_OUTRO decimal(18,4) null,
  VL_TOTAL_PIS decimal(18,4) null,
  VL_TOTAL_PRODUTOS decimal(18,4) null,
  VL_TOTAL_SEGURO decimal(18,4) null,
  STATUS_PROCESSAMENTO varchar(255) null,
  NATUREZA_OPERACAO_ID bigint not null,
  ENDERECO_ID_DESTINATARIO bigint not null,
  TIPO_DESTINATARIO varchar(45) not null,
  PESSOA_DESTINATARIO_ID_REFERENCIA bigint null,
  COTA_ID bigint null,
  TELEFONE_ID_DESTINATARIO bigint null,
  ENDERECO_ID_EMITENTE bigint not null,
  INDICADOR_DESTINATARIO bigint(11) null,
  PESSOA_EMITENTE_ID_REFERENCIADA bigint null,
  TELEFONE_ID_EMITENTE bigint null,
  ENDERECO_ID_TRANS bigint null,
  NOTA_IMPRESSA tinyint(1) not null,
  TIPO_AMBIENTE tinyint(1) null,
  NOTA_FISCAL_CODIGO_NF varchar(45) null,
  NOTA_FISCAL_DV_CHAVE_ACESSO tinyint(1) null,
  MODELO_DOCUMENTO_FISCAL varchar(2) not null,
  PROCESSO_EMISSAO tinyint(1) not null,
  NOTA_FISCAL_FORMATO_IMPRESSAO tinyint(1) not null,
  LOCAL_DESTINO_OPERACAO tinyint(1) not null,
  OPERACAO_CONSUMIDOR_FINAL tinyint(1) not null,
  PRESENCA_CONSUMIDOR tinyint(1) not null,
  VERSAO_SISTEMA_EMISSAO varchar(20) not null,
  NOTA_FISCAL_CODIGO_MUNICIPIO varchar(45) not null,
  NOTA_FISCAL_CODIGO_UF varchar(45) not null,
  NOTA_FISCAL_VALOR_CALCULADO_ID bigint null,
  INFORMACOES_ADICIONAIS varchar(4000) null,
  USUARIO_ID bigint not null,
  INSERT_TIME timestamp default CURRENT_TIMESTAMP not null,
  LAST_UPDATE_TIME timestamp default '0000-00-00 00:00:00' not null,
  QTD_VOLUME_PALLET bigint null,
  PESO_BRUTO_LIQUIDO decimal(18,4) null,
  INTERFACE_DEVOLUCAO_FORNECEDOR tinyint(1) null,
  BOLETO_NFE_GERADO tinyint(1) default '0' null,
  constraint FK933C2B4A5CF4E32B
  foreign key (NATUREZA_OPERACAO_ID) references natureza_operacao (ID),
  constraint FK933C2B4AE7D38522
  foreign key (ENDERECO_ID_DESTINATARIO) references nota_fiscal_endereco (ID),
  constraint FK933C2B4A742B297E
  foreign key (ENDERECO_ID_EMITENTE) references nota_fiscal_endereco (ID),
  constraint FK933C2B4A7A98715D
  foreign key (ENDERECO_ID_TRANS) references nota_fiscal_endereco (ID)
)
;

create index FK933C2B4A4C92964
  on nota_fiscal_novo (TELEFONE_ID_DESTINATARIO)
;

create index FK933C2B4A5CF4E32B
  on nota_fiscal_novo (NATUREZA_OPERACAO_ID)
;

create index FK933C2B4A742B297E
  on nota_fiscal_novo (ENDERECO_ID_EMITENTE)
;

create index FK933C2B4A78441BC1
  on nota_fiscal_novo (PESSOA_DESTINATARIO_ID_REFERENCIA)
;

create index FK933C2B4A7A98715D
  on nota_fiscal_novo (ENDERECO_ID_TRANS)
;

create index FK933C2B4A8B188640
  on nota_fiscal_novo (TELEFONE_ID_EMITENTE)
;

create index FK933C2B4AB281499A
  on nota_fiscal_novo (PESSOA_EMITENTE_ID_REFERENCIADA)
;

create index FK933C2B4AE7D38522
  on nota_fiscal_novo (ENDERECO_ID_DESTINATARIO)
;

create index nds_numero_doc_serie
  on nota_fiscal_novo (NUMERO_DOCUMENTO_FISCAL, SERIE)
;

create index ndx_chave_acesso
  on nota_fiscal_novo (CHAVE_ACESSO)
;

create index ndx_cota_idx
  on nota_fiscal_novo (COTA_ID)
;

create index ndx_dta_emissao_idx
  on nota_fiscal_novo (DATA_EMISSAO)
;

create trigger nota_fiscal_novo_BINS
before INSERT on nota_fiscal_novo
for each row
  BEGIN
    SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP;
  END;

create trigger nota_fiscal_novo_BUPD
before UPDATE on nota_fiscal_novo
for each row
  BEGIN
    SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP;
  END;

alter table nota_fiscal_fatura
  add constraint fk_NOTA_FISCAL_FATURA_1
foreign key (NOTA_FISCAL_NOVO_ID) references nota_fiscal_novo (id)
;

create table if not exists nota_fiscal_origem_item
(
  id bigint auto_increment
    primary key,
  ORIGEM_ID bigint not null,
  TIPO varchar(45) not null,
  PRODUTO_EDICAO_ID bigint null
)
;

create index fk_nota_fiscal_origem_item_produto_edicao_idx
  on nota_fiscal_origem_item (PRODUTO_EDICAO_ID)
;

create table if not exists nota_fiscal_pessoa
(
  ID bigint auto_increment
    primary key,
  NOME varchar(60) null,
  EMAIL varchar(60) null,
  CPF varchar(60) null,
  RG varchar(60) null,
  RAZAO_SOCIAL varchar(60) null,
  NOME_FANTASIA varchar(60) null,
  CNPJ varchar(18) null,
  INSCRICAO_ESTADUAL varchar(60) null,
  NOTA_FICAL_ENDERECO_ID bigint null,
  TIPO_PESSOA varchar(45) null,
  ID_PESSOA_ORIGINAL bigint not null
)
;

alter table nota_fiscal_novo
  add constraint FK933C2B4A78441BC1
foreign key (PESSOA_DESTINATARIO_ID_REFERENCIA) references nota_fiscal_pessoa (ID)
;

alter table nota_fiscal_novo
  add constraint FK933C2B4AB281499A
foreign key (PESSOA_EMITENTE_ID_REFERENCIADA) references nota_fiscal_pessoa (ID)
;

create table if not exists nota_fiscal_processo
(
  NOTA_FISCAL_ID bigint not null,
  PROCESSO varchar(255) null,
  constraint FK593C1AF077E1AF62
  foreign key (NOTA_FISCAL_ID) references nota_fiscal_novo (id)
)
;

create index FK593C1AF077E1AF62
  on nota_fiscal_processo (NOTA_FISCAL_ID)
;

create table if not exists nota_fiscal_produto_servico
(
  SEQUENCIA int not null,
  CFOP int not null,
  CST varchar(5) not null,
  CODIGO_BARRAS varchar(50) null,
  CODIGO_PRODUTO varchar(60) not null,
  DESCRICAO_PRODUTO varchar(120) not null,
  EXTIPI bigint null,
  NCM bigint not null,
  QUANTIDADE_COMERCIAL decimal(18,4) null,
  UNIDADE_COMERCIAL varchar(6) not null,
  VALOR_DESCONTO decimal(18,4) null,
  VALOR_FRETE decimal(18,4) null,
  VALOR_OUTROS decimal(18,4) null,
  VALOR_SERGURO decimal(18,4) null,
  VALOR_TOTAL_BRUTO decimal(18,4) null,
  VALOR_UNITARIO_COMERCIAL decimal(18,4) null,
  NOTA_FISCAL_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  VALOR_ALIQUOTA_ICMS decimal(13,2) not null,
  VALOR_ALIQUOTA_IPI decimal(13,2) not null,
  VALOR_COMPOE_VALOR_NF tinyint default '1' not null,
  VALOR_FRETE_COMPOE_VALOR_NF tinyint default '0' not null,
  primary key (SEQUENCIA, NOTA_FISCAL_ID),
  constraint FK65435E2977E1AF62
  foreign key (NOTA_FISCAL_ID) references nota_fiscal_novo (id)
)
;

create index FK65435E2977E1AF62
  on nota_fiscal_produto_servico (NOTA_FISCAL_ID)
;

create index FK65435E29A53173D3
  on nota_fiscal_produto_servico (PRODUTO_EDICAO_ID)
;

alter table nota_fiscal_encargo_financeiro_produto
  add constraint FKE8D1773F35367812c7153222
foreign key (NOTA_FISCAL_ID, PRODUTO_SERVICO_SEQUENCIA) references nota_fiscal_produto_servico (NOTA_FISCAL_ID, SEQUENCIA)
;

alter table nota_fiscal_encargo_financeiro_servico
  add constraint FKE8D1773F353678124fcb29e8
foreign key (NOTA_FISCAL_ID, PRODUTO_SERVICO_SEQUENCIA) references nota_fiscal_produto_servico (NOTA_FISCAL_ID, SEQUENCIA)
;

create table if not exists nota_fiscal_produto_servico_movimento_estoque_cota
(
  NOTA_FISCAL_ID bigint not null,
  PRODUTO_SERVICO_SEQUENCIA int not null,
  MOVIMENTO_ESTOQUE_COTA_ID bigint not null,
  constraint FK1C0EE79935367812
  foreign key (NOTA_FISCAL_ID, PRODUTO_SERVICO_SEQUENCIA) references nota_fiscal_produto_servico (NOTA_FISCAL_ID, SEQUENCIA),
  constraint FK1C0EE799BBE20E9D
  foreign key (MOVIMENTO_ESTOQUE_COTA_ID) references movimento_estoque_cota (id)
)
;

create index FK1C0EE79935367812
  on nota_fiscal_produto_servico_movimento_estoque_cota (NOTA_FISCAL_ID, PRODUTO_SERVICO_SEQUENCIA)
;

create index FK1C0EE799BBE20E9D
  on nota_fiscal_produto_servico_movimento_estoque_cota (MOVIMENTO_ESTOQUE_COTA_ID)
;

create table if not exists nota_fiscal_referenciada
(
  CHAVE_ACESSO varchar(44) not null,
  CHAVE_ACESSO_CTE varchar(44) not null,
  CNPJ varchar(14) not null,
  CODIGO_UF bigint(11) not null,
  DATA_EMISSAO date not null,
  MODELO_DOCUMENTO_FISCAL varchar(11) not null,
  NUMERO_DOCUMENTO_FISCAL varchar(20) not null,
  SERIE varchar(11) not null,
  NOTA_FISCAL_ID bigint not null,
  primary key (CHAVE_ACESSO, NOTA_FISCAL_ID),
  constraint FK41581DFF77E1AF62
  foreign key (NOTA_FISCAL_ID) references nota_fiscal_novo (id)
)
;

create index FK41581DFF77E1AF62
  on nota_fiscal_referenciada (NOTA_FISCAL_ID)
;

create table if not exists nota_fiscal_retencao_icms_transporte
(
  id bigint not null
    primary key,
  CFOP int not null,
  CODIGO_MUNICIPIO bigint not null,
  PER_ALIQUOTA decimal(18,4) null,
  VL_BC_RET_ICMS decimal(18,4) null,
  VL_ICMS decimal(18,4) null,
  VL_SERVICO decimal(18,4) null,
  constraint FK4B41D11350D82C52
  foreign key (id) references nota_fiscal_novo (id)
)
;

create index FK4B41D11350D82C52
  on nota_fiscal_retencao_icms_transporte (id)
;

create table if not exists nota_fiscal_saida
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  ISSQN_BASE decimal(18,4) null,
  ISSQN_TOTAL decimal(18,4) null,
  ISSQN_VALOR decimal(18,4) null,
  AMBIENTE varchar(255) null,
  CHAVE_ACESSO varchar(255) null,
  DATA_EMISSAO datetime not null,
  DATA_EXPEDICAO datetime not null,
  EMISSOR_INSCRICAO_ESTADUAL_SUBSTITUTO varchar(255) null,
  EMISSOR_INSCRICAO_MUNICIPAL varchar(255) null,
  EMITIDA tinyint(1) not null,
  FORMA_PAGAMENTO varchar(255) null,
  FRETE int null,
  HORA_SAIDA varchar(255) null,
  INFORMACOES_COMPLEMENTARES varchar(255) null,
  MOVIMENTO_INTEGRACAO varchar(255) null,
  NATUREZA_OPERACAO varchar(255) null,
  NUMERO bigint not null,
  NUMERO_FATURA varchar(255) null,
  PROTOCOLO varchar(255) null,
  SERIE varchar(255) not null,
  STATUS_EMISSAO varchar(255) null,
  STATUS_EMISSAO_NFE varchar(255) null,
  TIPO_EMISSAO_NFE varchar(255) null,
  TRANSPORTADORA_ANTT varchar(255) null,
  TRANSPORTADORA_CNPJ varchar(255) null,
  TRANSPORTADORA_ENDERECO varchar(255) null,
  TRANSPORTADORA_ESPECIE varchar(255) null,
  TRANSPORTADORA_INSCRICAO_ESTADUAL varchar(255) null,
  TRANSPORTADORA_MARCA varchar(255) null,
  TRANSPORTADORA_MUNICIPIO varchar(255) null,
  TRANSPORTADORA_NOME varchar(255) null,
  TRANSPORTADORA_NUMERACAO varchar(255) null,
  TRANSPORTADORA_PESO_BRUTO decimal(18,4) null,
  TRANSPORTADORA_PESO_LIQUIDO decimal(18,4) null,
  TRANSPORTADORA_PLACA_VEICULO varchar(255) null,
  TRANSPORTADORA_PLACA_VEICULO_UF varchar(255) null,
  TRANSPORTADORA_QUANTIDADE varchar(255) null,
  TRANSPORTADORA_UF varchar(255) null,
  VALOR_BASE_ICMS decimal(18,4) null,
  VALOR_BASE_ICMS_SUBSTITUTO decimal(18,4) null,
  VALOR_BRUTO decimal(18,4) null,
  VALOR_DESCONTO decimal(18,4) null,
  VALOR_FATURA decimal(18,4) null,
  VALOR_FRETE decimal(18,4) null,
  VALOR_ICMS decimal(18,4) null,
  VALOR_ICMS_SUBSTITUTO decimal(18,4) null,
  VALOR_IPI decimal(18,4) null,
  VALOR_LIQUIDO decimal(18,4) null,
  VALOR_NF decimal(18,4) null,
  VALOR_OUTRO decimal(18,4) null,
  VALOR_PRODUTOS decimal(18,4) null,
  VALOR_SEGURO decimal(18,4) null,
  VERSAO varchar(255) null,
  TIPO_NF_ID bigint not null,
  CFOP_ID bigint not null,
  FORNECEDOR_ID bigint null,
  PJ_ID bigint not null,
  VALOR_INFORMADO decimal(18,4) null,
  constraint FKD48924687FEEEECC
  foreign key (TIPO_NF_ID) references natureza_operacao (ID),
  constraint FKD48924687AB6324F
  foreign key (CFOP_ID) references cfop (ID),
  constraint FKD48924689808F874
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index FKD4892468479FD586
  on nota_fiscal_saida (PJ_ID)
;

create index FKD48924687AB6324F
  on nota_fiscal_saida (CFOP_ID)
;

create index FKD48924687FEEEECC
  on nota_fiscal_saida (TIPO_NF_ID)
;

create index FKD48924689808F874
  on nota_fiscal_saida (FORNECEDOR_ID)
;

alter table item_nota_fiscal_saida
  add constraint FK8FDD023CA05BE382
foreign key (NOTA_FISCAL_ID) references nota_fiscal_saida (ID)
;

create table if not exists nota_fiscal_serie
(
  id int not null
    primary key,
  CURRENT_VALUE bigint null
)
;

create table if not exists nota_fiscal_telefone
(
  ID bigint auto_increment
    primary key,
  TIPO_TELEFONE varchar(10) null,
  DDI bigint(2) null,
  DDD bigint(2) null,
  NUMERO varchar(10) null,
  RAMAL varchar(5) null
)
;

alter table nota_fiscal_novo
  add constraint FK933C2B4A4C92964
foreign key (TELEFONE_ID_DESTINATARIO) references nota_fiscal_telefone (ID)
;

alter table nota_fiscal_novo
  add constraint FK933C2B4A8B188640
foreign key (TELEFONE_ID_EMITENTE) references nota_fiscal_telefone (ID)
;

create table if not exists nota_fiscal_tributacao
(
  id bigint auto_increment
    primary key,
  ALIQUOTA_COFINS decimal(18,4) null,
  ALIQUOTA_ICMS decimal(18,4) null,
  ALIQUOTA_IPI decimal(18,4) null,
  ALIQUOTA_PIS decimal(18,4) null,
  COD_EMPRESA varchar(9) null,
  COD_NBM varchar(10) null,
  COD_NOP varchar(20) null,
  CST_COFINS varchar(2) null,
  CST_ICMS varchar(3) null,
  CST_IPI varchar(3) null,
  CST_PIS varchar(3) null,
  DAT_VIGENCIA date null,
  FLG_BSC_CRE_COFINS varchar(1) null,
  FLG_BSC_CRE_PIS varchar(1) null,
  FLG_BSC_DEB_COFINS varchar(1) null,
  FLG_BSC_DEB_PIS varchar(1) null,
  FLG_BSC_ICMS varchar(1) null,
  FLG_BSC_IPI varchar(1) null,
  FLG_VLR_CRE_COF varchar(1) null,
  FLG_VLR_CRE_PIS varchar(1) null,
  FLG_VLR_DEB_COF varchar(1) null,
  FLG_VLR_DEB_PIS varchar(1) null,
  FLG_VLR_ICMS varchar(1) null,
  FLG_VLR_IPI varchar(1) null,
  FLG_VLR_ISE_ICMS varchar(1) null,
  FLG_VLR_ISE_IPI varchar(1) null,
  FLG_VLR_OUT_ICMS varchar(1) null,
  FLG_VLR_OUT_IPI varchar(1) null,
  NAT_OPERACAO int null,
  TIP_BSC_ICMS varchar(1) null,
  TIP_OPERACAO varchar(1) null,
  TRIBUTACAO_COFINS varchar(1) null,
  TRIBUTACAO_ICMS varchar(1) null,
  TRIBUTACAO_IPI varchar(1) null,
  TRIBUTACAO_PIS varchar(1) null,
  UF_DESTINO varchar(2) null,
  UF_ORIGEM varchar(2) null
)
;

create table if not exists nota_fiscal_valor_calculado
(
  ID bigint auto_increment
    primary key,
  VALOR_BASE_ICMS decimal(18,4) default '0.0000' null,
  VALOR_ICMS decimal(18,4) default '0.0000' null,
  VALOR_ICMS_DESON decimal(18,4) null,
  VALOR_BASE_ICMS_SUBSTITUTO decimal(18,4) default '0.0000' null,
  VALOR_ICMS_SUBSTITUTO decimal(18,4) default '0.0000' null,
  VALOR_PRODUTOS decimal(18,4) default '0.0000' null,
  VALOR_FRETE decimal(18,4) default '0.0000' null,
  VALOR_SEGURO decimal(18,4) default '0.0000' null,
  VALOR_DESCONTO decimal(18,4) default '0.0000' null,
  VALOR_OUTRO decimal(18,4) default '0.0000' null,
  VALOR_IMPOSTO_IMPORTACAO decimal(18,4) default '0.0000' null,
  VALOR_IPI decimal(18,4) default '0.0000' null,
  VALOR_NF decimal(18,4) default '0.0000' null,
  VALOR_PIS decimal(18,4) default '0.0000' null,
  VALOR_COFINS decimal(18,4) default '0.0000' null,
  ISSQN_TOTAL decimal(18,4) default '0.0000' null,
  ISSQN_BASE decimal(18,4) default '0.0000' null,
  ISSQN_VALOR decimal(18,4) default '0.0000' null
)
;

create table if not exists nota_fiscal_valores_retencoes_tributos
(
  id bigint not null
    primary key,
  VL_BC_IRRF decimal(18,4) null,
  VL_BC_PREV decimal(18,4) null,
  VL_RET_COFINS decimal(18,4) null,
  VL_RET_CSLL decimal(18,4) null,
  VL_RET_IRRF decimal(18,4) null,
  VL_RET_PIS decimal(18,4) null,
  VL_RET_PREV decimal(18,4) null,
  constraint FK5A341D5250D82C52
  foreign key (id) references nota_fiscal_novo (id)
)
;

create index FK5A341D5250D82C52
  on nota_fiscal_valores_retencoes_tributos (id)
;

create table if not exists nota_fiscal_valores_totais_issqn
(
  id bigint not null
    primary key,
  VL_TOTAL_BC_ISS decimal(18,4) null,
  VL_TOTAL_COFINS decimal(18,4) null,
  VL_TOTAL_ISS decimal(18,4) null,
  VL_TOTAL_PIS decimal(18,4) null,
  VL_TOTAL_SERVICOS decimal(18,4) null,
  constraint FKAD8928F650D82C52
  foreign key (id) references nota_fiscal_novo (id)
)
;

create index FKAD8928F650D82C52
  on nota_fiscal_valores_totais_issqn (id)
;

create table if not exists nota_promissoria
(
  ID bigint auto_increment
    primary key,
  VALOR double not null,
  VALOR_EXTENSO varchar(255) not null,
  VENCIMENTO datetime not null
)
;

alter table cota_garantia
  add constraint FK80C0DF0DEA490E43
foreign key (NOTA_PROMISSORIA_ID) references nota_promissoria (ID)
;

create table if not exists pagamento_caucao_liquida
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  VALOR decimal(18,4) null,
  QNT_PARCELAS int null,
  VALOR_PARCELA decimal(18,4) null,
  DESCONTO_ATUAL decimal(18,4) null,
  DESCONTO_COTA decimal(18,4) null,
  PORCENTAGEM_UTILIZADA decimal(18,4) null,
  FORMA_COBRANCA_CAUCAO_LIQUIDA_ID bigint null,
  BANCO_ID bigint null,
  constraint FK403E6639EA41BD85
  foreign key (FORMA_COBRANCA_CAUCAO_LIQUIDA_ID) references forma_cobranca_caucao_liquida (id),
  constraint FK403E6639E44516C0
  foreign key (BANCO_ID) references banco (ID)
)
;

create index FK403E6639E44516C0
  on pagamento_caucao_liquida (BANCO_ID)
;

create index FK403E6639EA41BD85
  on pagamento_caucao_liquida (FORMA_COBRANCA_CAUCAO_LIQUIDA_ID)
;

alter table cota_garantia
  add constraint FK80C0DF0DFE263B9B
foreign key (PAGAMENTO_CAUCAO_LIQUIDA_ID) references pagamento_caucao_liquida (ID)
;

create table if not exists parametro_cobranca_cota
(
  ID bigint auto_increment
    primary key,
  FATOR_VENCIMENTO int null,
  COTA_ID bigint null,
  FORNECEDOR_ID bigint null,
  UNIFICA_COBRANCA bigint default '0' null,
  constraint COTA_ID
  unique (COTA_ID),
  constraint FK53B1036BC8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK_PARAMETRO_COBRANCA_COTA_FORNECEDOR_ID
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index FK53B1036BC8181F74
  on parametro_cobranca_cota (COTA_ID)
;

create index FK_PARAMETRO_COBRANCA_COTA_FORNECEDOR_ID
  on parametro_cobranca_cota (FORNECEDOR_ID)
;

alter table cota
  add constraint FKPARAMETRO_COBRANCA_COTA
foreign key (PARAMETRO_COBRANCA_ID) references parametro_cobranca_cota (ID)
;

alter table forma_cobranca
  add constraint FKA10CEB0961171A8E
foreign key (PARAMETRO_COBRANCA_COTA_ID) references parametro_cobranca_cota (ID)
;

create table if not exists parametro_cobranca_distribuicao_cota
(
  ID bigint auto_increment
    primary key,
  DIA_COBRANCA int null,
  MODELIDADE_COBRANCA varchar(255) null,
  PERIODICIDADE_COBRANCA varchar(255) null,
  POR_ENTREGA tinyint(1) null,
  BASE_CALCULO int null,
  PERCENTUAL_FATURAMENTO decimal(18,4) null,
  TAXA_FIXA decimal(18,4) null,
  COTA_ID bigint null,
  DIA_SEMANA varchar(50) null
)
;

alter table cota
  add constraint FKPARAMETRO_COBRANCA_DISTRIBUICAO_COTA
foreign key (PARAMETRO_COBRANCA_DISTRIBUICAO_COTA_ID) references parametro_cobranca_distribuicao_cota (ID)
;

create table if not exists parametro_contrato_cota
(
  ID bigint auto_increment
    primary key,
  COMPLEMENTO_CONTRATO longtext null,
  CONDICOES_CONTRATACAO longtext null,
  DIAS_AVISO_RESCISAO int not null,
  DURACAO_CONTRATO_COTA int not null
)
;

alter table distribuidor
  add constraint FK2BE223AEC54E738
foreign key (PARAMETRO_CONTRATO_COTA_ID) references parametro_contrato_cota (ID)
;

create table if not exists parametro_distribuidor
(
  ID bigint auto_increment
    primary key,
  CNPJ varchar(255) not null,
  CODIGO_DINAP bigint not null,
  CODIGO_FC bigint not null,
  NOME_FANTASIA varchar(255) not null,
  RAZAO_SOCIAL varchar(255) not null,
  TIPO_DISTRIBUIDOR varchar(255) not null
)
;

create table if not exists parametro_emissao_nota_fiscal
(
  ID bigint auto_increment
    primary key,
  GRUPO_NOTA_FISCAL varchar(255) not null,
  SERIE_NF varchar(255) not null,
  CFOP_DENTRO_UF bigint not null,
  CFOP_FORA_UF bigint null,
  constraint GRUPO_NOTA_FISCAL
  unique (GRUPO_NOTA_FISCAL),
  constraint FK5EE045794189EDCA
  foreign key (CFOP_DENTRO_UF) references cfop (ID),
  constraint FK5EE04579CC126856
  foreign key (CFOP_FORA_UF) references cfop (ID)
)
;

create index FK5EE045794189EDCA
  on parametro_emissao_nota_fiscal (CFOP_DENTRO_UF)
;

create index FK5EE04579CC126856
  on parametro_emissao_nota_fiscal (CFOP_FORA_UF)
;

create table if not exists parametro_sistema
(
  ID bigint auto_increment
    primary key,
  TIPO_PARAMETRO_SISTEMA varchar(255) not null,
  VALOR varchar(255) null
)
;

create table if not exists parametro_usuario_box
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) null,
  BOX_ID bigint not null,
  USUARIO_ID bigint not null,
  constraint FK94C9BED4BA6EBE40
  foreign key (BOX_ID) references box (ID)
)
;

create index FK94C9BED47FFF790E
  on parametro_usuario_box (USUARIO_ID)
;

create index FK94C9BED4BA6EBE40
  on parametro_usuario_box (BOX_ID)
;

create table if not exists parametros_distribuidor_emissao_documentos
(
  TIPO_PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS varchar(255) not null
    primary key,
  UTILIZA_EMAIL tinyint(1) not null,
  UTILIZA_IMPRESSAO tinyint(1) not null,
  DISTRIBUIDOR_ID bigint not null,
  constraint FKF2CA661B56501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
)
;

create index FKF2CA661B56501954
  on parametros_distribuidor_emissao_documentos (DISTRIBUIDOR_ID)
;

create table if not exists parametros_distribuidor_faltas_sobras
(
  ID bigint auto_increment
    primary key,
  FALTA_DE tinyint(1) not null,
  FALTA_EM tinyint(1) not null,
  SOBRA_DE tinyint(1) not null,
  SOBRA_EM tinyint(1) not null,
  TIPO_PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS varchar(255) null,
  DISTRIBUIDOR_ID bigint not null,
  constraint FK8547839C56501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
)
;

create index FK8547839C56501954
  on parametros_distribuidor_faltas_sobras (DISTRIBUIDOR_ID)
;

create table if not exists parametros_distribuidor_nota_fiscal
(
  ID bigint auto_increment
    primary key,
  CHAVE varchar(45) not null,
  VALOR varchar(4000) not null,
  DESCRICAO varchar(255) not null,
  constraint CHAVE_UNIQUE
  unique (CHAVE)
)
;

create table if not exists parametros_ftf_geracao
(
  ID bigint auto_increment
    primary key,
  NOME varchar(50) not null,
  CODIGO_CENTRO_EMISSOR varchar(50) not null,
  CODIGO_ESTABELECIMENTO_EMISSOR_GFF varchar(50) not null,
  CNPJ_EMISSOR varchar(50) not null,
  CNPJ_DESTINATARIO varchar(50) not null,
  ESTABELECIMENTO varchar(50) not null,
  TIPO_PEDIDO varchar(50) not null,
  CODIGO_SOLICITANTE varchar(50) not null,
  CODIGO_UNIDADE_OPERACIONAL varchar(50) not null,
  MENSAGEM varchar(200) not null,
  CODIGO_NATUREZA_OPERACAO_FTF varchar(45) not null,
  NATUREZA_OPERACAO_ID bigint not null,
  CFOP_ID bigint not null,
  constraint fk_parametros_ftf_geracao_nat_op_1
  foreign key (NATUREZA_OPERACAO_ID) references natureza_operacao (ID),
  constraint fk_parametros_ftf_geracao_cfop_1
  foreign key (CFOP_ID) references cfop (ID)
)
;

create index fk_parametros_ftf_geracao_cfop_1
  on parametros_ftf_geracao (CFOP_ID)
;

create index fk_parametros_ftf_geracao_nat_op_1
  on parametros_ftf_geracao (NATUREZA_OPERACAO_ID)
;

create table if not exists parcela_negociacao
(
  ID bigint auto_increment
    primary key,
  DATA_VENCIMENTO datetime null,
  ENCARGOS decimal(18,4) null,
  NUMERO_CHEQUE varchar(255) null,
  MOVIMENTO_FINANCEIRO_ID bigint null,
  NEGOCIACAO_ID bigint null,
  constraint FK904E941CEEC3D845
  foreign key (MOVIMENTO_FINANCEIRO_ID) references movimento_financeiro_cota (id),
  constraint FK904E941CFCD23D41
  foreign key (NEGOCIACAO_ID) references negociacao (ID)
)
;

create index FK904E941CEEC3D845
  on parcela_negociacao (MOVIMENTO_FINANCEIRO_ID)
;

create index FK904E941CFCD23D41
  on parcela_negociacao (NEGOCIACAO_ID)
;

create table if not exists pdv
(
  ID bigint auto_increment
    primary key,
  ARRENDATARIO tinyint(1) null,
  BALCAO_CENTRAL tinyint(1) null,
  PONTO_PRINCIPAL tinyint(1) null,
  POSSUI_CARTAO_CREDITO tinyint(1) null,
  POSSUI_COMPUTADOR tinyint(1) null,
  POSSUI_LUMINOSO tinyint(1) null,
  TEXTO_LUMINOSO varchar(255) null,
  CONTATO varchar(255) null,
  dataInclusao date null,
  DENTRO_OUTRO_ESTABELECIMENTO tinyint(1) null,
  EMAIL varchar(255) null,
  EXPOSITOR tinyint(1) null,
  NOME_LICENCA varchar(255) null,
  NUMERO_LICENCA varchar(255) null,
  NOME varchar(255) not null,
  PONTO_REFERENCIA varchar(255) null,
  PORCENTAGEM_FATURAMENTO decimal(18,4) null,
  POSSUI_SISTEMA_IPV tinyint(1) null,
  QTDE_FUNCIONARIOS int null,
  TIPO_CARACTERISTICA_PDV varchar(255) null,
  SITE varchar(255) null,
  STATUS_PDV varchar(255) null,
  TAMANHO_PDV varchar(255) null,
  TIPO_EXPOSITOR varchar(255) null,
  COTA_ID bigint not null,
  TIPO_LICENCA_MUNICIPAL_ID bigint null,
  AREA_INFLUENCIA_PDV_ID bigint null,
  TIPO_CLUSTER_PDV_ID bigint null,
  TIPO_PONTO_PDV_ID bigint null,
  TIPO_ESTABELECIMENTO_PDV_ID bigint null,
  LINHA bigint null,
  NUMERO_PDV int null,
  constraint FK134E2C8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK134E23D683EA4
  foreign key (AREA_INFLUENCIA_PDV_ID) references area_influencia_pdv (ID)
)
;

create index FK134E23007BE32
  on pdv (TIPO_ESTABELECIMENTO_PDV_ID)
;

create index FK134E23D683EA4
  on pdv (AREA_INFLUENCIA_PDV_ID)
;

create index FK134E24E24300A
  on pdv (TIPO_CLUSTER_PDV_ID)
;

create index FK134E2B076932A
  on pdv (TIPO_PONTO_PDV_ID)
;

create index FK134E2C8181F74
  on pdv (COTA_ID)
;

create index FK134E2DCF1938
  on pdv (TIPO_LICENCA_MUNICIPAL_ID)
;

alter table endereco_pdv
  add constraint FK498CAF6CA65B70F4
foreign key (PDV_ID) references pdv (ID)
;

alter table estudo_pdv
  add constraint FK8DF0F9DA65B70F4
foreign key (PDV_ID) references pdv (ID)
;

alter table fixacao_reparte_pdv
  add constraint FK6160D9089A5F4F5A
foreign key (ID_PDV) references pdv (ID)
;

alter table gerador_fluxo_pdv
  add constraint FKECD90D24A65B70F4
foreign key (PDV_ID) references pdv (ID)
;

create table if not exists pdv_material_promocional
(
  PDV_ID bigint not null,
  MATERIAL_PROMOCIONAL_ID bigint not null,
  primary key (PDV_ID, MATERIAL_PROMOCIONAL_ID),
  constraint FKE8BEF584A65B70F4
  foreign key (PDV_ID) references pdv (ID),
  constraint FKE8BEF5842B57B031
  foreign key (MATERIAL_PROMOCIONAL_ID) references material_promocional (ID)
)
;

create index FKE8BEF5842B57B031
  on pdv_material_promocional (MATERIAL_PROMOCIONAL_ID)
;

create index FKE8BEF584A65B70F4
  on pdv_material_promocional (PDV_ID)
;

create table if not exists perfil_usuario
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(255) not null
)
;

create table if not exists periodo_funcionamento_pdv
(
  ID bigint auto_increment
    primary key,
  HORARIO_FIM time null,
  HORARIO_INICIO time null,
  FUNCIONAMENTO_PDV varchar(255) null,
  PDV_ID bigint null,
  constraint FKB3048186A65B70F4
  foreign key (PDV_ID) references pdv (ID)
)
;

create index FKB3048186A65B70F4
  on periodo_funcionamento_pdv (PDV_ID)
;

create table if not exists periodo_lancamento_parcial
(
  ID bigint auto_increment
    primary key,
  STATUS varchar(255) not null,
  TIPO varchar(255) not null,
  LANCAMENTO_PARCIAL_ID bigint not null,
  NUMERO_PERIODO int not null,
  DATA_CRIACAO date null,
  constraint FKCC84267E4927B490
  foreign key (LANCAMENTO_PARCIAL_ID) references lancamento_parcial (ID)
)
;

create index FKCC84267E4927B490
  on periodo_lancamento_parcial (LANCAMENTO_PARCIAL_ID)
;

create table if not exists periodo_lancamento_parcial_aux
(
  ID bigint auto_increment
    primary key,
  STATUS varchar(255) null,
  TIPO varchar(255) null,
  LANCAMENTO_PARCIAL_ID bigint null,
  NUMERO_PERIODO int null,
  DATA_CRIACAO date null,
  DATA_LANCAMENTO date null,
  DATA_RECOLHIMENTO date null,
  PRODUTO_EDICAO_ID bigint null
)
;

create table if not exists pessoa
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  EMAIL varchar(255) null,
  APELIDO varchar(25) null,
  CPF varchar(255) null,
  DATA_NASCIMENTO date null,
  ESTADO_CIVIL varchar(255) null,
  NACIONALIDADE varchar(255) null,
  NATURALIDADE varchar(255) null,
  NOME varchar(255) null,
  ORGAO_EMISSOR varchar(255) null,
  RG varchar(255) null,
  SEXO varchar(255) null,
  SOCIO_PRINCIPAL tinyint(1) null,
  UF_ORGAO_EMISSOR varchar(255) null,
  CNPJ varchar(255) null,
  INSC_ESTADUAL varchar(20) null,
  INSC_MUNICIPAL varchar(15) null,
  NOME_FANTASIA varchar(60) null,
  RAZAO_SOCIAL varchar(255) null,
  PESSOA_ID_CONJUGE bigint null,
  constraint CPF
  unique (CPF),
  constraint CNPJ
  unique (CNPJ),
  constraint FK8C7703A7D236B5B7
  foreign key (PESSOA_ID_CONJUGE) references pessoa (ID)
)
;

create index FK8C7703A7D236B5B7
  on pessoa (PESSOA_ID_CONJUGE)
;

create index NDX_NOME
  on pessoa (NOME)
;

alter table cota
  add constraint FK1FA7D99B8CB634
foreign key (PESSOA_ID) references pessoa (ID)
;

alter table distribuidor
  add constraint FK2BE223AE479FD586
foreign key (PJ_ID) references pessoa (ID)
;

alter table editor
  add constraint FK799F156D9218183B
foreign key (JURIDICA_ID) references pessoa (ID)
;

alter table endereco
  add constraint FK95D357C99B8CB634
foreign key (PESSOA_ID) references pessoa (ID)
;

alter table entregador
  add constraint FK860C808F9B8CB634
foreign key (PESSOA_ID) references pessoa (ID)
;

alter table fiador
  add constraint FK7B9684899B8CB634
foreign key (PESSOA_ID) references pessoa (ID)
;

alter table fiador_socio
  add constraint FK9FAC29977CB7F32E
foreign key (SOCIO_ID) references pessoa (ID)
;

alter table fornecedor
  add constraint FK6B088D659218183B
foreign key (JURIDICA_ID) references pessoa (ID)
;

alter table nota_envio
  add constraint FK59E3F72278441BC1
foreign key (PESSOA_DESTINATARIO_ID_REFERENCIA) references pessoa (ID)
;

alter table nota_envio
  add constraint FK59E3F722B281499A
foreign key (PESSOA_EMITENTE_ID_REFERENCIADA) references pessoa (ID)
;

alter table nota_fiscal_entrada
  add constraint FK90AD527479FD586
foreign key (pj_id) references pessoa (ID)
;

alter table nota_fiscal_saida
  add constraint FKD4892468479FD586
foreign key (PJ_ID) references pessoa (ID)
;

create table if not exists politica_cobranca
(
  ID bigint auto_increment
    primary key,
  ACUMULA_DIVIDA tinyint(1) not null,
  ATIVO tinyint(1) not null,
  FORMA_EMISSAO varchar(255) null,
  NUM_DIAS_POSTERGADO int null,
  PRINCIPAL tinyint(1) not null,
  UNIFICA_COBRANCA tinyint(1) not null,
  DISTRIBUIDOR_ID bigint null,
  FORMA_COBRANCA_ID bigint null,
  COBRANCA_BO tinyint(1) null,
  FORNECEDOR_PADRAO_ID bigint null,
  FATOR_VENCIMENTO int null,
  UNIFICA_COBRANCA_POR_COTA tinyint(1) null,
  constraint FK5743FFF756501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID),
  constraint FK5743FFF7E34F875B
  foreign key (FORMA_COBRANCA_ID) references forma_cobranca (id),
  constraint FK5743FFF76051BBD2
  foreign key (FORNECEDOR_PADRAO_ID) references fornecedor (ID)
)
;

create index FK5743FFF756501954
  on politica_cobranca (DISTRIBUIDOR_ID)
;

create index FK5743FFF76051BBD2
  on politica_cobranca (FORNECEDOR_PADRAO_ID)
;

create index FK5743FFF7E34F875B
  on politica_cobranca (FORMA_COBRANCA_ID)
;

create table if not exists processo
(
  ID bigint auto_increment
    primary key,
  NOME varchar(100) not null,
  DESCRICAO varchar(255) null
)
;

create table if not exists processo_nfe
(
  PROCESSO_NFE_ID bigint not null,
  PROCESSO int null,
  constraint FK1D60CAEC0B404C1
  foreign key (PROCESSO_NFE_ID) references natureza_operacao (ID)
)
;

create index FK1D60CAEC0B404C1
  on processo_nfe (PROCESSO_NFE_ID)
;

create table if not exists procuracao_entregador
(
  ID bigint auto_increment
    primary key,
  ENDERECO varchar(255) null,
  ESTADO_CIVIL varchar(255) not null,
  NACIONALIDADE varchar(255) null,
  NUMERO_PERMISSAO varchar(255) not null,
  PROCURADOR varchar(255) not null,
  PROFISSAO varchar(255) null,
  RG varchar(255) null,
  COTA_ID bigint null,
  ENTREGADOR_ID bigint not null,
  constraint ENTREGADOR_ID
  unique (ENTREGADOR_ID),
  constraint FKB848C78BC8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FKB848C78B83B33034
  foreign key (ENTREGADOR_ID) references entregador (ID)
)
;

create index FKB848C78B83B33034
  on procuracao_entregador (ENTREGADOR_ID)
;

create index FKB848C78BC8181F74
  on procuracao_entregador (COTA_ID)
;

create table if not exists produto
(
  ID bigint auto_increment
    primary key,
  DATA_CRIACAO timestamp default CURRENT_TIMESTAMP null,
  ATIVO tinyint(1) null,
  CODIGO varchar(30) null,
  COD_CONTEXTO int null,
  DATA_DESATIVACAO date null,
  desconto decimal(18,4) null,
  COMPRIMENTO float null,
  ESPESSURA float null,
  LARGURA float null,
  fase varchar(255) null,
  FORMA_COMERCIALIZACAO varchar(255) null,
  LANCAMENTO_IMEDIATO tinyint(1) null,
  NOME varchar(60) not null,
  NOME_COMERCIAL varchar(255) null,
  numeroLancamento bigint null,
  ORIGEM varchar(255) not null,
  PACOTE_PADRAO int not null,
  PEB int not null,
  PERCENTUAl_ABRANGENCIA double null,
  PERC_LIMITE_COTA_FIXACAO double null,
  PERC_LIMITE_REPARTE_FIXACAO double null,
  PERIODICIDADE varchar(255) not null,
  PESO bigint not null,
  CLASSE_SOCIAL varchar(255) null,
  FAIXA_ETARIA varchar(255) null,
  FORMATO_PRODUTO varchar(255) null,
  SEXO varchar(255) null,
  TEMA_PRINCIPAL varchar(255) null,
  TEMA_SECUNDARIO varchar(255) null,
  SITUACAO_TRIBUTARIA varchar(255) null,
  SLOGAN varchar(50) null,
  TRIBUTACAO_FISCAL varchar(255) null,
  ALGORITMO_ID bigint null,
  DESCONTO_LOGISTICA_ID bigint null,
  EDITOR_ID bigint null,
  TIPO_PRODUTO_ID bigint not null,
  DESCRICAO_DESCONTO varchar(255) null,
  TIPO_SEGMENTO_PRODUTO_ID bigint null,
  DESCONTO_ID bigint null,
  GRUPO_TRIBUTO_ID bigint null,
  codigo_icd varchar(255) null,
  GRUPO_EDITORIAL varchar(25) null,
  geracao_automatica tinyint default '0' null,
  SEM_CE_INTEGRACAO tinyint(1) default '1' null,
  FORMA_FISICA varchar(255) null,
  SUB_GRUPO_EDITORIAL varchar(25) null,
  CENTRO_LUCRO_CORPORATIVO varchar(10) null,
  NOTA_FISCAL tinyint(1) default '0' null,
  constraint CODIGO
  unique (CODIGO),
  constraint FK18595AD9FEBD8620
  foreign key (ALGORITMO_ID) references algoritmo (ID),
  constraint FK18595AD946FC8AA9
  foreign key (DESCONTO_LOGISTICA_ID) references desconto_logistica (ID),
  constraint FK18595AD9B2A11874
  foreign key (EDITOR_ID) references editor (ID),
  constraint FK18595AD929E8DFE3
  foreign key (DESCONTO_ID) references desconto (ID),
  constraint FK_GRUPO_TRIBUTO_ID_1
  foreign key (GRUPO_TRIBUTO_ID) references grupo_tributo (ID)
)
;

create index FK18595AD929E8DFE3
  on produto (DESCONTO_ID)
;

create index FK18595AD946FC8AA9
  on produto (DESCONTO_LOGISTICA_ID)
;

create index FK18595AD999DDFD97
  on produto (TIPO_PRODUTO_ID)
;

create index FK18595AD9B2A11874
  on produto (EDITOR_ID)
;

create index FK18595AD9FEBD8620
  on produto (ALGORITMO_ID)
;

create index FK_GRUPO_TRIBUTO_ID_1
  on produto (GRUPO_TRIBUTO_ID)
;

create index FK_TIPO_SEGMENTO_PRODUTO
  on produto (TIPO_SEGMENTO_PRODUTO_ID)
;

create index NDX_CODIGO_ICD
  on produto (codigo_icd)
;

create index NDX_NOME
  on produto (NOME)
;

alter table desconto_cota_produto_excessoes
  add constraint FKD845FEF0C5C16480
foreign key (PRODUTO_ID) references produto (ID)
;

alter table desconto_proximos_lancamentos
  add constraint FK7E8614E1C5C16480
foreign key (PRODUTO_ID) references produto (ID)
;

alter table excecao_produto_cota
  add constraint FKA42FF9A2C5C16480
foreign key (PRODUTO_ID) references produto (ID)
;

alter table fixacao_reparte
  add constraint FKA394AF65F34F8834
foreign key (ID_PRODUTO) references produto (ID)
;

alter table historico_desconto_cota_produto_excessoes
  add constraint FK56EB6281C5C16480
foreign key (PRODUTO_ID) references produto (ID)
;

alter table historico_desconto_produto_edicoes
  add constraint FK9EC90139C5C16480
foreign key (PRODUTO_ID) references produto (ID)
;

alter table historico_desconto_produtos
  add constraint FK6FDFA8FFC5C16480
foreign key (PRODUTO_ID) references produto (ID)
;

alter table mix_cota_produto
  add constraint FK7C18196F34F8834
foreign key (ID_PRODUTO) references produto (ID)
;

create table if not exists produto_edicao
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  ATIVO tinyint(1) not null,
  BOLETIM_INFORMATIVO varchar(2048) null,
  DESCRICAO_BRINDE varchar(255) null,
  VENDE_BRINDE_SEPARADO tinyint(1) null,
  CHAMADA_CAPA varchar(255) null,
  CODIGO_DE_BARRAS_CORPORATIVO varchar(25) null,
  CODIGO_DE_BARRAS varchar(18) null,
  DATA_DESATIVACAO date null,
  COMPRIMENTO float null,
  ESPESSURA float null,
  LARGURA float null,
  EXPECTATIVA_VENDA decimal(18,4) null,
  NOME_COMERCIAL varchar(60) null,
  NUMERO_EDICAO bigint not null,
  ORIGEM varchar(255) not null,
  PACOTE_PADRAO int not null,
  PARCIAL tinyint(1) not null,
  PEB int not null,
  PERMITE_VALE_DESCONTO tinyint(1) not null,
  PESO bigint not null,
  POSSUI_BRINDE tinyint(1) not null,
  PRECO_CUSTO decimal(18,4) null,
  PRECO_PREVISTO decimal(18,4) null,
  PRECO_VENDA decimal(18,4) null,
  REPARTE_DISTRIBUIDO decimal(18,4) null,
  DESCONTO_LOGISTICA_ID bigint null,
  PRODUTO_ID bigint not null,
  PRODUTO_EDICAO_ID bigint null,
  BRINDE_ID bigint null,
  CLASSE_SOCIAL varchar(255) null,
  FAIXA_ETARIA varchar(255) null,
  FORMATO_PRODUTO varchar(255) null,
  SEXO varchar(255) null,
  TEMA_PRINCIPAL varchar(255) null,
  TEMA_SECUNDARIO varchar(255) null,
  TIPO_LANCAMENTO varchar(255) null,
  CARACTERISTICA_PRODUTO varchar(255) null,
  DESCONTO decimal(18,4) null,
  DESCRICAO_DESCONTO varchar(255) null,
  GRUPO_PRODUTO varchar(255) null,
  DESCONTO_ID bigint null,
  FORMA_FISICA varchar(255) null,
  TIPO_CLASSIFICACAO_PRODUTO_ID bigint null,
  HISTORICO varchar(255) null,
  VINCULAR_RECOLHIMENTO tinyint(1) null,
  CODIGO_NBM varchar(10) null,
  CODIGO_BARRAS_ATUALIZADO tinyint(1) default '0' null,
  constraint FKD43B400D46FC8AA9
  foreign key (DESCONTO_LOGISTICA_ID) references desconto_logistica (ID),
  constraint FKD43B400DC5C16480
  foreign key (PRODUTO_ID) references produto (ID),
  constraint FKD43B400DA53173D3
  foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID),
  constraint FKD43B400D94ECE7D4
  foreign key (BRINDE_ID) references brinde (ID),
  constraint FKD43B400D29E8DFE3
  foreign key (DESCONTO_ID) references desconto (ID)
)
;

create index FKD43B400D10C84C95
  on produto_edicao (TIPO_CLASSIFICACAO_PRODUTO_ID)
;

create index FKD43B400D29E8DFE3
  on produto_edicao (DESCONTO_ID)
;

create index FKD43B400D46FC8AA9
  on produto_edicao (DESCONTO_LOGISTICA_ID)
;

create index FKD43B400D94ECE7D4
  on produto_edicao (BRINDE_ID)
;

create index FKD43B400DA53173D3
  on produto_edicao (PRODUTO_EDICAO_ID)
;

create index FKD43B400DC5C16480
  on produto_edicao (PRODUTO_ID)
;

alter table chamada_encalhe
  add constraint FK98FAFF70A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table conferencia_enc_parcial
  add constraint FK35D69C6FB7E346C0
foreign key (produtoEdicao_ID) references produto_edicao (ID)
;

alter table conferencia_encalhe
  add constraint FK8E92BB04A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table conferencia_encalhe_backup
  add constraint FK_CONFBACKUP_PRODUTO_EDICAO_ID
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table controle_contagem_devolucao
  add constraint FK90E56DC6A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table desconto_cota_produto_excessoes
  add constraint FKD845FEF0A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table desconto_produto
  add constraint FK15B68545A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table desconto_produto_edicao
  add constraint FK4899C421A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table destino_encalhe
  add constraint fk_produto_edicao
foreign key (produto_edicao_id) references produto_edicao (ID)
;

alter table diferenca
  add constraint FKF2B9F095A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table estoque_produto
  add constraint FKE9F637F2A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table estoque_produto_cota
  add constraint FK5CA35006A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table estoque_produto_fila
  add constraint FK_ESTOQUE_PRODUTO_FILA_produto_edicao
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table estrategia
  add constraint FK65485699A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table estrategia_base_distribuicao
  add constraint FK805710C0A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table estudo
  add constraint FK7A77787AA53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table estudo_gerado
  add constraint FK4E8B943DA53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table estudo_produto_edicao
  add constraint FK70621A52A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table estudo_produto_edicao_base
  add constraint FK6C34489EA53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table fechamento_diario_lancamento_encalhe
  add constraint FK15EC5355D2FE34B7
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table fechamento_diario_lancamento_reparte
  add constraint FKB6F90192D2FE34B7
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table fechamento_diario_lancamento_suplementar
  add constraint FK3D4464A9D2FE34B7
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table fechamento_diario_movimento_vendas_encalhe
  add constraint FK35E76390D2FE34B7b6674115
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table fechamento_diario_movimento_vendas_suplementar
  add constraint FK35E76390D2FE34B727127269
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table fechamento_encalhe
  add constraint FK9496A657A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table fechamentodiariomovimentovenda
  add constraint FK35E76390D2FE34B7
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table furo_produto
  add constraint FK301010E6A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table historico_alteracao_preco_venda
  add constraint fk_HISTORICO_ALTERACAO_PRECO_VENDA_PRODUTO_EDICAO
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table historico_desconto_cota_produto_excessoes
  add constraint FK56EB6281A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table historico_desconto_produto_edicoes
  add constraint FK9EC90139A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table historico_estoque_produto
  add constraint FKEC4EDD83A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table historico_fechamento_diario_lancamento_encalhe
  add constraint FKDD9B8264D2FE34B7
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table historico_fechamento_diario_lancamento_reparte
  add constraint FK7EA830A1D2FE34B7
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table historico_fechamento_diario_lancamento_suplementar
  add constraint FK2C977438D2FE34B7
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table historico_fechamento_diario_movimento_vendas_encalhe
  add constraint FK534CD760D2FE34B71d34a8e4
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table historico_fechamento_diario_movimento_vendas_suplementar
  add constraint FK534CD760D2FE34B716395ab8
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table historicofechamentodiariomovimentovenda
  add constraint FK534CD760D2FE34B7
foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID)
;

alter table item_chamada_encalhe_fornecedor
  add constraint FKE9A54E40A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table item_nota_fiscal_entrada
  add constraint FK3EDE8DFBA53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table item_nota_fiscal_saida
  add constraint FK8FDD023CA53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table lancamento
  add constraint FK1D53917AA53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table lancamento_parcial
  add constraint FKDDFB94DA53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table movimento_estoque
  add constraint FKEF448115A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table movimento_estoque_cota
  add constraint FK459444C3A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table movimento_fechamento_fiscal_fornecedor
  add constraint fk_MOV_FECH_FISCAL_FORN_PROD_EDICAO_1
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table nota_envio_item
  add constraint FK18605A10A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table nota_fiscal_origem_item
  add constraint fk_nota_fiscal_origem_item_produto_edicao
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

alter table nota_fiscal_produto_servico
  add constraint FK65435E29A53173D3
foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
;

create table if not exists produto_edicao_slip
(
  ID bigint auto_increment
    primary key,
  DATA_OPERACAO date null,
  DATA_RECOLHIMENTO date null,
  DATA_RECOLHIMENTO_DISTRIBUIDOR date null,
  DIA int null,
  ENCALHE decimal(19,2) null,
  ID_CHAMADA_ENCALHE bigint null,
  ID_PRODUTO_EDICAO bigint null,
  NOME_PRODUTO varchar(255) null,
  NUMERO_EDICAO bigint null,
  ORDINAL_DIA_CONFERENCIA_ENCALHE varchar(255) null,
  PRECO_VENDA decimal(18,4) null,
  QTDE_TOTAL_PRODUTOS varchar(255) null,
  QUANTIDADE_EFETIVA decimal(19,2) null,
  REPARTE decimal(19,2) null,
  VALOR_TOTAL decimal(18,4) null,
  VALOR_TOTAL_ENCALHE varchar(255) null,
  SLIP_ID bigint not null,
  CODIGO_PRODUTO varchar(30) null
)
;

create index FKEA5D5A32ADA6DD0A
  on produto_edicao_slip (SLIP_ID)
;

create table if not exists produto_fornecedor
(
  PRODUTO_ID bigint not null,
  fornecedores_ID bigint not null,
  primary key (PRODUTO_ID, fornecedores_ID),
  constraint FKBA6768BC5C16480
  foreign key (PRODUTO_ID) references produto (ID),
  constraint FKBA6768BFC845246
  foreign key (fornecedores_ID) references fornecedor (ID)
)
;

create index FKBA6768BC5C16480
  on produto_fornecedor (PRODUTO_ID)
;

create index FKBA6768BFC845246
  on produto_fornecedor (fornecedores_ID)
;

create table if not exists qrtz_blob_triggers
(
  SCHED_NAME varchar(120) not null,
  TRIGGER_NAME varchar(200) not null,
  TRIGGER_GROUP varchar(200) not null,
  BLOB_DATA blob null,
  primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
;

create table if not exists qrtz_calendars
(
  SCHED_NAME varchar(120) not null,
  CALENDAR_NAME varchar(200) not null,
  CALENDAR blob not null,
  primary key (SCHED_NAME, CALENDAR_NAME)
)
;

create table if not exists qrtz_cron_triggers
(
  SCHED_NAME varchar(120) not null,
  TRIGGER_NAME varchar(200) not null,
  TRIGGER_GROUP varchar(200) not null,
  CRON_EXPRESSION varchar(200) not null,
  TIME_ZONE_ID varchar(80) null,
  primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
;

create table if not exists qrtz_fired_triggers
(
  SCHED_NAME varchar(120) not null,
  ENTRY_ID varchar(95) not null,
  TRIGGER_NAME varchar(200) not null,
  TRIGGER_GROUP varchar(200) not null,
  INSTANCE_NAME varchar(200) not null,
  FIRED_TIME bigint(13) not null,
  PRIORITY int not null,
  STATE varchar(16) not null,
  JOB_NAME varchar(200) null,
  JOB_GROUP varchar(200) null,
  IS_NONCONCURRENT varchar(1) null,
  REQUESTS_RECOVERY varchar(1) null,
  primary key (SCHED_NAME, ENTRY_ID)
)
;

create table if not exists qrtz_job_details
(
  SCHED_NAME varchar(120) not null,
  JOB_NAME varchar(200) not null,
  JOB_GROUP varchar(200) not null,
  DESCRIPTION varchar(250) null,
  JOB_CLASS_NAME varchar(250) not null,
  IS_DURABLE varchar(1) not null,
  IS_NONCONCURRENT varchar(1) not null,
  IS_UPDATE_DATA varchar(1) not null,
  REQUESTS_RECOVERY varchar(1) not null,
  JOB_DATA blob null,
  primary key (SCHED_NAME, JOB_NAME, JOB_GROUP)
)
;

create table if not exists qrtz_locks
(
  SCHED_NAME varchar(120) not null,
  LOCK_NAME varchar(40) not null,
  primary key (SCHED_NAME, LOCK_NAME)
)
;

create table if not exists qrtz_paused_trigger_grps
(
  SCHED_NAME varchar(120) not null,
  TRIGGER_GROUP varchar(200) not null,
  primary key (SCHED_NAME, TRIGGER_GROUP)
)
;

create table if not exists qrtz_scheduler_state
(
  SCHED_NAME varchar(120) not null,
  INSTANCE_NAME varchar(200) not null,
  LAST_CHECKIN_TIME bigint(13) not null,
  CHECKIN_INTERVAL bigint(13) not null,
  primary key (SCHED_NAME, INSTANCE_NAME)
)
;

create table if not exists qrtz_simple_triggers
(
  SCHED_NAME varchar(120) not null,
  TRIGGER_NAME varchar(200) not null,
  TRIGGER_GROUP varchar(200) not null,
  REPEAT_COUNT bigint(7) not null,
  REPEAT_INTERVAL bigint(12) not null,
  TIMES_TRIGGERED bigint(10) not null,
  primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
;

create table if not exists qrtz_simprop_triggers
(
  SCHED_NAME varchar(120) not null,
  TRIGGER_NAME varchar(200) not null,
  TRIGGER_GROUP varchar(200) not null,
  STR_PROP_1 varchar(512) null,
  STR_PROP_2 varchar(512) null,
  STR_PROP_3 varchar(512) null,
  INT_PROP_1 int null,
  INT_PROP_2 int null,
  LONG_PROP_1 bigint null,
  LONG_PROP_2 bigint null,
  DEC_PROP_1 decimal(13,4) null,
  DEC_PROP_2 decimal(13,4) null,
  BOOL_PROP_1 varchar(1) null,
  BOOL_PROP_2 varchar(1) null,
  primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
;

create table if not exists qrtz_triggers
(
  SCHED_NAME varchar(120) not null,
  TRIGGER_NAME varchar(200) not null,
  TRIGGER_GROUP varchar(200) not null,
  JOB_NAME varchar(200) not null,
  JOB_GROUP varchar(200) not null,
  DESCRIPTION varchar(250) null,
  NEXT_FIRE_TIME bigint(13) null,
  PREV_FIRE_TIME bigint(13) null,
  PRIORITY int null,
  TRIGGER_STATE varchar(16) not null,
  TRIGGER_TYPE varchar(8) not null,
  START_TIME bigint(13) not null,
  END_TIME bigint(13) null,
  CALENDAR_NAME varchar(200) null,
  MISFIRE_INSTR smallint(2) null,
  JOB_DATA blob null,
  primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  constraint qrtz_triggers_ibfk_1
  foreign key (SCHED_NAME, JOB_NAME, JOB_GROUP) references qrtz_job_details (SCHED_NAME, JOB_NAME, JOB_GROUP)
)
;

create index SCHED_NAME
  on qrtz_triggers (SCHED_NAME, JOB_NAME, JOB_GROUP)
;

alter table qrtz_blob_triggers
  add constraint qrtz_blob_triggers_ibfk_1
foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
;

alter table qrtz_cron_triggers
  add constraint qrtz_cron_triggers_ibfk_1
foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
;

alter table qrtz_simple_triggers
  add constraint qrtz_simple_triggers_ibfk_1
foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
;

alter table qrtz_simprop_triggers
  add constraint qrtz_simprop_triggers_ibfk_1
foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
;

create table if not exists ranking_faturamento
(
  ID bigint auto_increment
    primary key,
  COTA_ID bigint not null,
  FATURAMENTO decimal(18,4) null,
  DATA_GERACAO_RANK datetime null,
  constraint FK2BF1CB3DC8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK2BF1CB3DC8181F74
  on ranking_faturamento (COTA_ID)
;

create table if not exists ranking_faturamento_gerados
(
  ID bigint auto_increment
    primary key,
  DATA_GERACAO_RANK timestamp default CURRENT_TIMESTAMP not null
)
;

create table if not exists ranking_segmento
(
  ID bigint auto_increment
    primary key,
  COTA_ID bigint not null,
  TIPO_SEGMENTO_PRODUTO_ID bigint not null,
  QTDE decimal(18,4) null,
  DATA_GERACAO_RANK datetime null,
  constraint FK8614C9A5C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK8614C9A54C709C67
  on ranking_segmento (TIPO_SEGMENTO_PRODUTO_ID)
;

create index FK8614C9A5C8181F74
  on ranking_segmento (COTA_ID)
;

create index NDX_COTA_DATA_TIPO
  on ranking_segmento (COTA_ID, DATA_GERACAO_RANK, TIPO_SEGMENTO_PRODUTO_ID)
;

create table if not exists ranking_segmento_gerados
(
  ID bigint auto_increment
    primary key,
  DATA_GERACAO_RANK timestamp default CURRENT_TIMESTAMP not null
)
;

create table if not exists rateio_cota_ausente
(
  ID bigint auto_increment
    primary key,
  QTDE decimal(18,4) null,
  COTA_ID bigint not null,
  COTA_AUSENTE_ID bigint null,
  PRODUTO_EDICAO_ID bigint not null,
  constraint FK379AD72CC8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK379AD72C70E707D7
  foreign key (COTA_AUSENTE_ID) references cota_ausente (ID),
  constraint FK379AD72CA53173D3
  foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
)
;

create index FK379AD72C70E707D7
  on rateio_cota_ausente (COTA_AUSENTE_ID)
;

create index FK379AD72CA53173D3
  on rateio_cota_ausente (PRODUTO_EDICAO_ID)
;

create index FK379AD72CC8181F74
  on rateio_cota_ausente (COTA_ID)
;

create table if not exists rateio_diferenca
(
  ID bigint auto_increment
    primary key,
  DATA_NOTA_ENVIO date null,
  QTDE decimal(18,4) null,
  COTA_ID bigint not null,
  DIFERENCA_ID bigint not null,
  ESTUDO_COTA_ID bigint null,
  DATA_MOVIMENTO date not null,
  constraint FK35BC319CC8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FK35BC319C5A0FFAA9
  foreign key (DIFERENCA_ID) references diferenca (id),
  constraint FK35BC319C714FA744
  foreign key (ESTUDO_COTA_ID) references estudo_cota (ID)
)
;

create index FK35BC319C5A0FFAA9
  on rateio_diferenca (DIFERENCA_ID)
;

create index FK35BC319C714FA744
  on rateio_diferenca (ESTUDO_COTA_ID)
;

create index FK35BC319CC8181F74
  on rateio_diferenca (COTA_ID)
;

create table if not exists recebimento_fisico
(
  ID bigint auto_increment
    primary key,
  DATA_CONFIRMACAO datetime null,
  DATA_RECEBIMENTO date not null,
  STATUS_CONFIRMACAO varchar(255) not null,
  conferente_ID bigint null,
  NOTA_FISCAL_ID bigint not null,
  recebedor_ID bigint not null,
  constraint NOTA_FISCAL_ID
  unique (NOTA_FISCAL_ID),
  constraint FKE6D238AFC74D9881
  foreign key (NOTA_FISCAL_ID) references nota_fiscal_entrada (ID)
)
;

create index FKE6D238AF36A347F1
  on recebimento_fisico (conferente_ID)
;

create index FKE6D238AF68C2472D
  on recebimento_fisico (recebedor_ID)
;

create index FKE6D238AFC74D9881
  on recebimento_fisico (NOTA_FISCAL_ID)
;

alter table item_receb_fisico
  add constraint FK1217CDA3B7A04D34
foreign key (RECEBIMENTO_FISICO_ID) references recebimento_fisico (ID)
;

create table if not exists referencia_cota
(
  ID bigint auto_increment
    primary key,
  PERCENTUAL decimal(18,4) null,
  BASE_REFERENCIA_COTA_ID bigint not null,
  COTA_ID bigint not null,
  constraint BASE_REFERENCIA_COTA_ID
  unique (BASE_REFERENCIA_COTA_ID, COTA_ID),
  constraint FK6386510654DAA518
  foreign key (BASE_REFERENCIA_COTA_ID) references base_referencia_cota (ID),
  constraint FK63865106C8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK6386510654DAA518
  on referencia_cota (BASE_REFERENCIA_COTA_ID)
;

create index FK63865106C8181F74
  on referencia_cota (COTA_ID)
;

create table if not exists regiao
(
  ID bigint auto_increment
    primary key,
  DATA_REGIAO datetime not null,
  NOME_REGIAO varchar(255) not null,
  REGIAO_IS_FIXA tinyint(1) not null,
  USUARIO_ID bigint not null
)
;

create index FK8FDB19437FFF790E
  on regiao (USUARIO_ID)
;

create table if not exists regime_tributario
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  DESCRICAO varchar(60) not null,
  ATIVO tinyint(1) not null
)
;

alter table distribuidor
  add constraint fk_reg_trib_distribuidor_1
foreign key (REGIME_TRIBUTARIO_ID) references regime_tributario (ID)
;

create table if not exists regime_tributario_tributo_aliquota
(
  regime_tributario_id bigint not null,
  tributo_aliquota_id bigint not null,
  primary key (regime_tributario_id, tributo_aliquota_id),
  constraint fk_regime_tributario_tributo_aliquota_2
  foreign key (regime_tributario_id) references regime_tributario (ID)
)
;

create index fk_regime_tributario_tributo_aliquota_1
  on regime_tributario_tributo_aliquota (tributo_aliquota_id)
;

create table if not exists registro_cota_regiao
(
  ID bigint auto_increment
    primary key,
  USUARIO_ID bigint not null,
  REGIAO_ID bigint null,
  COTA_ID bigint null,
  DATA_ALTERACAO datetime null,
  constraint FK24C9625DAE907F55
  foreign key (REGIAO_ID) references regiao (ID),
  constraint FK24C9625DC8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK24C9625D7FFF790E
  on registro_cota_regiao (USUARIO_ID)
;

create index FK24C9625DAE907F55
  on registro_cota_regiao (REGIAO_ID)
;

create index FK24C9625DC8181F74
  on registro_cota_regiao (COTA_ID)
;

create table if not exists reparte_pdv
(
  ID bigint auto_increment
    primary key,
  REPARTE int null,
  MIX_COTA_PRODUTO_ID bigint null,
  PDV_ID bigint not null,
  PRODUTO_ID bigint not null,
  constraint FKC3AA9D0238A5EAC3
  foreign key (MIX_COTA_PRODUTO_ID) references mix_cota_produto (ID),
  constraint FKC3AA9D02A65B70F4
  foreign key (PDV_ID) references pdv (ID),
  constraint FKC3AA9D02C5C16480
  foreign key (PRODUTO_ID) references produto (ID)
)
;

create index FKC3AA9D0238A5EAC3
  on reparte_pdv (MIX_COTA_PRODUTO_ID)
;

create index FKC3AA9D02A65B70F4
  on reparte_pdv (PDV_ID)
;

create index FKC3AA9D02C5C16480
  on reparte_pdv (PRODUTO_ID)
;

create table if not exists rota
(
  ID bigint auto_increment
    primary key,
  DESCRICAO_ROTA varchar(255) null,
  ORDEM int not null,
  ROTEIRO_ID bigint not null
)
;

create index FK26796A7E316820
  on rota (ROTEIRO_ID)
;

alter table assoc_veiculo_motorista_rota
  add constraint FK45A0E4B78F516B2E
foreign key (ROTA) references rota (ID)
;

alter table entregador
  add constraint FK860C808FE19C69D4
foreign key (ROTA_ID) references rota (ID)
;

create table if not exists rota_pdv
(
  ID bigint auto_increment
    primary key,
  ORDEM int null,
  PDV_ID bigint null,
  ROTA_ID bigint null,
  constraint ROTA_PDV_UNIQUE_IDX
  unique (PDV_ID, ROTA_ID),
  constraint FK2C44188DA65B70F4
  foreign key (PDV_ID) references pdv (ID),
  constraint FK2C44188DE19C69D4
  foreign key (ROTA_ID) references rota (ID)
)
;

create index FK2C44188DA65B70F4
  on rota_pdv (PDV_ID)
;

create index FK2C44188DE19C69D4
  on rota_pdv (ROTA_ID)
;

create table if not exists roteirizacao
(
  ID bigint auto_increment
    primary key,
  BOX_ID bigint null,
  constraint BOX_ID
  unique (BOX_ID),
  constraint FK9A9E8A58BA6EBE40
  foreign key (BOX_ID) references box (ID)
)
;

create index FK9A9E8A58BA6EBE40
  on roteirizacao (BOX_ID)
;

create table if not exists roteiro
(
  ID bigint auto_increment
    primary key,
  DESCRICAO_ROTEIRO varchar(255) null,
  ORDEM int not null,
  TIPO_ROTEIRO varchar(255) not null,
  ROTEIRIZACAO_ID bigint null,
  constraint FK7D4E04183B7BA114
  foreign key (ROTEIRIZACAO_ID) references roteirizacao (ID)
)
;

create index FK7D4E04183B7BA114
  on roteiro (ROTEIRIZACAO_ID)
;

create table if not exists segmento_nao_recebido
(
  ID bigint auto_increment
    primary key,
  COTA_ID bigint not null,
  TIPO_SEGMENTO_PRODUTO_ID bigint not null,
  DATA_ALTERACAO datetime null,
  USUARIO_ID bigint not null,
  constraint FK6551CA0DC8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK6551CA0D4C709C67
  on segmento_nao_recebido (TIPO_SEGMENTO_PRODUTO_ID)
;

create index FK6551CA0D7FFF790E
  on segmento_nao_recebido (USUARIO_ID)
;

create index FK6551CA0DC8181F74
  on segmento_nao_recebido (COTA_ID)
;

create table if not exists semaforo
(
  NUMERO_COTA int not null
    primary key,
  STATUS_PROCESSO_ENCALHE varchar(255) null,
  DATA_OPERACAO date null,
  DATA_INICIO datetime null,
  DATA_FIM datetime null,
  ERROR_LOG varchar(255) null,
  USUARIO_ID bigint null
)
;

create index FK1_USUARIO_SEMAFORO
  on semaforo (USUARIO_ID)
;

create index FK_STATUS_DATA
  on semaforo (DATA_OPERACAO, STATUS_PROCESSO_ENCALHE)
;

create table if not exists seq_generator
(
  sequence_name varchar(255) not null,
  sequence_next_hi_value int null,
  constraint NDX_SEQ_NAME_UNIQUE
  unique (sequence_name)
)
;

create table if not exists slip
(
  ID bigint auto_increment
    primary key,
  CE_JORNALEIRO bigint null,
  CODIGO_BOX varchar(255) null,
  DATA_CONFERENCIA timestamp default CURRENT_TIMESTAMP not null,
  DESCRICAO_ROTA varchar(255) null,
  DESCRICAO_ROTEIRO varchar(255) null,
  NOME_COTA varchar(255) null,
  NUM_SLIP bigint null,
  NUMERO_COTA int not null,
  PAGAMENTO_PENDENTE decimal(18,4) null,
  TOTAL_PRODUTO_DIA decimal(19,2) null,
  TOTAL_PRODUTOS decimal(19,2) null,
  VALOR_CREDITO_DIF decimal(18,4) null,
  VALOR_DEVIDO decimal(18,4) null,
  VALOR_ENCALHE_DIA decimal(18,4) null,
  VALOR_LIQUIDO_DEVIDO decimal(18,4) null,
  VALOR_SLIP decimal(18,4) null,
  VALOR_TOTAL_DESCONTO decimal(18,4) null,
  VALOR_TOTAL_ENCALHE decimal(18,4) null,
  VALOR_TOTAL_PAGAR decimal(18,4) null,
  VALOR_TOTAL_REPARTE decimal(18,4) null,
  VALOR_TOTAL_SEM_DESCONTO decimal(18,4) null
)
;

create index nxd_cota_data
  on slip (NUMERO_COTA, DATA_CONFERENCIA)
;

alter table debito_credito_cota
  add constraint FKE2A9E3BEADA6DD0A
foreign key (SLIP_ID) references slip (ID)
;

alter table produto_edicao_slip
  add constraint FKEA5D5A32ADA6DD0A
foreign key (SLIP_ID) references slip (ID)
;

create table if not exists socio_cota
(
  ID bigint auto_increment
    primary key,
  CARGO varchar(255) null,
  NOME varchar(255) null,
  PRINCIPAL tinyint(1) null,
  COTA_ID bigint not null,
  ENDERECO_ID bigint not null,
  TELEFONE_ID bigint not null,
  constraint FKC6ACCC0BC8181F74
  foreign key (COTA_ID) references cota (ID),
  constraint FKC6ACCC0BAE761C74
  foreign key (ENDERECO_ID) references endereco (ID)
)
;

create index FKC6ACCC0B52023CD4
  on socio_cota (TELEFONE_ID)
;

create index FKC6ACCC0BAE761C74
  on socio_cota (ENDERECO_ID)
;

create index FKC6ACCC0BC8181F74
  on socio_cota (COTA_ID)
;

create table if not exists telefone
(
  ID bigint auto_increment
    primary key,
  DDD varchar(255) null,
  NUMERO varchar(255) not null,
  RAMAL varchar(255) null,
  PESSOA_ID bigint null,
  constraint FKDD8E5AEA9B8CB634
  foreign key (PESSOA_ID) references pessoa (ID)
)
;

create index FKDD8E5AEA9B8CB634
  on telefone (PESSOA_ID)
;

alter table nota_envio
  add constraint FK59E3F7224C92964
foreign key (TELEFONE_ID_DESTINATARIO) references telefone (ID)
;

alter table nota_envio
  add constraint FK59E3F7228B188640
foreign key (TELEFONE_ID_EMITENTE) references telefone (ID)
;

alter table socio_cota
  add constraint FKC6ACCC0B52023CD4
foreign key (TELEFONE_ID) references telefone (ID)
;

create table if not exists telefone_cota
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_TELEFONE varchar(255) not null,
  TELEFONE_ID bigint not null,
  COTA_ID bigint not null,
  constraint FK9402480E52023CD4
  foreign key (TELEFONE_ID) references telefone (ID),
  constraint FK9402480EC8181F74
  foreign key (COTA_ID) references cota (ID)
)
;

create index FK9402480E52023CD4
  on telefone_cota (TELEFONE_ID)
;

create index FK9402480EC8181F74
  on telefone_cota (COTA_ID)
;

create table if not exists telefone_distribuidor
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_TELEFONE varchar(255) not null,
  TELEFONE_ID bigint not null,
  DISTRIBUIDOR_ID bigint not null,
  constraint FK354FBEE352023CD4
  foreign key (TELEFONE_ID) references telefone (ID),
  constraint FK354FBEE356501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
)
;

create index FK354FBEE352023CD4
  on telefone_distribuidor (TELEFONE_ID)
;

create index FK354FBEE356501954
  on telefone_distribuidor (DISTRIBUIDOR_ID)
;

create table if not exists telefone_editor
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_TELEFONE varchar(255) not null,
  TELEFONE_ID bigint not null,
  EDITOR_ID bigint not null,
  constraint FK9F5A7C6252023CD4
  foreign key (TELEFONE_ID) references telefone (ID),
  constraint FK9F5A7C62B2A11874
  foreign key (EDITOR_ID) references editor (ID)
)
;

create index FK9F5A7C6252023CD4
  on telefone_editor (TELEFONE_ID)
;

create index FK9F5A7C62B2A11874
  on telefone_editor (EDITOR_ID)
;

create table if not exists telefone_entregador
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_TELEFONE varchar(255) not null,
  TELEFONE_ID bigint not null,
  ENTREGADOR_ID bigint not null,
  constraint FK121650452023CD4
  foreign key (TELEFONE_ID) references telefone (ID),
  constraint FK121650483B33034
  foreign key (ENTREGADOR_ID) references entregador (ID)
)
;

create index FK121650452023CD4
  on telefone_entregador (TELEFONE_ID)
;

create index FK121650483B33034
  on telefone_entregador (ENTREGADOR_ID)
;

create table if not exists telefone_fiador
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_TELEFONE varchar(255) not null,
  TELEFONE_ID bigint not null,
  FIADOR_ID bigint not null,
  constraint FKA151EB7E52023CD4
  foreign key (TELEFONE_ID) references telefone (ID),
  constraint FKA151EB7E8DC372F4
  foreign key (FIADOR_ID) references fiador (ID)
)
;

create index FKA151EB7E52023CD4
  on telefone_fiador (TELEFONE_ID)
;

create index FKA151EB7E8DC372F4
  on telefone_fiador (FIADOR_ID)
;

create table if not exists telefone_fornecedor
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_TELEFONE varchar(255) not null,
  TELEFONE_ID bigint not null,
  FORNECEDOR_ID bigint not null,
  constraint FKE61D71DA52023CD4
  foreign key (TELEFONE_ID) references telefone (ID),
  constraint FKE61D71DA9808F874
  foreign key (FORNECEDOR_ID) references fornecedor (ID)
)
;

create index FKE61D71DA52023CD4
  on telefone_fornecedor (TELEFONE_ID)
;

create index FKE61D71DA9808F874
  on telefone_fornecedor (FORNECEDOR_ID)
;

create table if not exists telefone_pdv
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_TELEFONE varchar(255) not null,
  TELEFONE_ID bigint not null,
  PDV_ID bigint not null,
  constraint FK67DF3A0D52023CD4
  foreign key (TELEFONE_ID) references telefone (ID),
  constraint FK67DF3A0DA65B70F4
  foreign key (PDV_ID) references pdv (ID)
)
;

create index FK67DF3A0D52023CD4
  on telefone_pdv (TELEFONE_ID)
;

create index FK67DF3A0DA65B70F4
  on telefone_pdv (PDV_ID)
;

create table if not exists telefone_transportador
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_TELEFONE varchar(255) not null,
  TELEFONE_ID bigint not null,
  TRANSPORTADOR_ID bigint not null,
  constraint FK86E2BB3A52023CD4
  foreign key (TELEFONE_ID) references telefone (ID)
)
;

create index FK86E2BB3A52023CD4
  on telefone_transportador (TELEFONE_ID)
;

create index FK86E2BB3AD90B1440
  on telefone_transportador (TRANSPORTADOR_ID)
;

create table if not exists tipo_classificacao_produto
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(255) not null
)
;

alter table classificacao_nao_recebida
  add constraint FK7F7429DC10C84C95
foreign key (TIPO_CLASSIFICACAO_PRODUTO_ID) references tipo_classificacao_produto (ID)
;

alter table fixacao_reparte
  add constraint FKA394AF65917D3254
foreign key (ID_CLASSIFICACAO_EDICAO) references tipo_classificacao_produto (ID)
;

alter table mix_cota_produto
  add constraint FK7C1819610C84C95
foreign key (TIPO_CLASSIFICACAO_PRODUTO_ID) references tipo_classificacao_produto (ID)
;

create table if not exists tipo_cluster_pdv
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  DESCRICAO varchar(255) not null
)
;

alter table pdv
  add constraint FK134E24E24300A
foreign key (TIPO_CLUSTER_PDV_ID) references tipo_cluster_pdv (ID)
;

create table if not exists tipo_entrega
(
  ID bigint auto_increment
    primary key,
  BASE_CALCULO varchar(255) null,
  DESCRICAO_TIPO_ENTREGA varchar(255) not null,
  DIA_MES int null,
  DIA_SEMANA varchar(255) null,
  PERCENTUAL_FATURAMENTO float null,
  PERIODICIDADE varchar(255) not null,
  TAXA_FIXA decimal(18,4) null
)
;

create table if not exists tipo_estabelecimento_associacao_pdv
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  DESCRICAO varchar(255) not null
)
;

alter table pdv
  add constraint FK134E23007BE32
foreign key (TIPO_ESTABELECIMENTO_PDV_ID) references tipo_estabelecimento_associacao_pdv (ID)
;

create table if not exists tipo_fornecedor
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(255) not null,
  GRUPO_FORNECEDOR varchar(255) not null
)
;

alter table fornecedor
  add constraint FK6B088D65440433FD
foreign key (TIPO_FORNECEDOR_ID) references tipo_fornecedor (ID)
;

create table if not exists tipo_garantia_aceita
(
  id bigint auto_increment
    primary key,
  tipoGarantia varchar(255) null,
  VALOR int null,
  DISTRIBUIDOR_ID bigint not null,
  constraint FK8EB4B40056501954
  foreign key (DISTRIBUIDOR_ID) references distribuidor (ID)
)
;

create index FK8EB4B40056501954
  on tipo_garantia_aceita (DISTRIBUIDOR_ID)
;

create table if not exists tipo_gerador_fluxo_pdv
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  DESCRICAO varchar(255) not null
)
;

alter table gerador_fluxo_pdv
  add constraint FKECD90D241656FEE0
foreign key (TIPO_GERADOR_FLUXO_ID) references tipo_gerador_fluxo_pdv (ID)
;

alter table gerador_fluxo_pdv_tipo_gerador_fluxo_pdv
  add constraint FK308AA654FA255EBD
foreign key (TIPO_GERADOR_FLUXO_PDV_ID) references tipo_gerador_fluxo_pdv (ID)
;

create table if not exists tipo_licenca_municipal
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  DESCRICAO varchar(255) not null
)
;

alter table pdv
  add constraint FK134E2DCF1938
foreign key (TIPO_LICENCA_MUNICIPAL_ID) references tipo_licenca_municipal (ID)
;

create table if not exists tipo_movimento
(
  TIPO varchar(31) not null,
  ID bigint auto_increment
    primary key,
  APROVACAO_AUTOMATICA tinyint(1) not null,
  DESCRICAO varchar(255) not null,
  GRUPO_MOVIMENTO_ESTOQUE varchar(255) null,
  INCIDE_DIVIDA tinyint(1) null,
  INCIDE_JURAMENTADO tinyint(1) null,
  OPERACAO_ESTOQUE varchar(255) null,
  GRUPO_MOVIMENTO_FINANCEIRO varchar(255) null,
  OPERACAO_FINANCEIRA varchar(255) null
)
;

alter table historico_movto_financeiro_cota
  add constraint FKAB46D6ED3BDC9EE3
foreign key (TIPO_MOVTO_ID) references tipo_movimento (ID)
;

alter table movimento_estoque
  add constraint FKBEB397FC4E3C0541ef448115
foreign key (TIPO_MOVIMENTO_ID) references tipo_movimento (ID)
;

alter table movimento_estoque_cota
  add constraint FKBEB397FC4E3C0541459444c3
foreign key (TIPO_MOVIMENTO_ID) references tipo_movimento (ID)
;

alter table movimento_fechamento_fiscal_cota
  add constraint fk_movimento_fechamento_fiscal_tipo_movimento_1
foreign key (TIPO_MOVIMENTO_ID) references tipo_movimento (ID)
;

alter table movimento_fechamento_fiscal_fornecedor
  add constraint fk_MOV_FECH_FISCAL_FORN_TIPO_MOVIMENTO_1
foreign key (TIPO_MOVIMENTO_ID) references tipo_movimento (ID)
;

alter table movimento_financeiro_cota
  add constraint FKBEB397FC4E3C0541c67ee7a9
foreign key (TIPO_MOVIMENTO_ID) references tipo_movimento (ID)
;

alter table natureza_operacao_tipo_movimento
  add constraint FK_tipo_movimento_id_2
foreign key (TIPO_MOVIMENTO_ID) references tipo_movimento (ID)
;

create table if not exists tipo_ponto_pdv
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  DESCRICAO varchar(255) not null
)
;

alter table desenglobacao
  add constraint FKC6D71263511FDA95
foreign key (TIPO_PDV_ID) references tipo_ponto_pdv (ID)
;

alter table pdv
  add constraint FK134E2B076932A
foreign key (TIPO_PONTO_PDV_ID) references tipo_ponto_pdv (ID)
;

create table if not exists tipo_produto
(
  ID bigint auto_increment
    primary key,
  CODIGO bigint not null,
  CODIGO_NBM varchar(255) null,
  DESCRICAO varchar(25) not null,
  GRUPO_PRODUTO varchar(255) not null,
  NCM_ID bigint not null,
  constraint CODIGO
  unique (CODIGO),
  constraint DESCRICAO
  unique (DESCRICAO),
  constraint FKB87BD8CE3FD9B8E5
  foreign key (NCM_ID) references ncm (ID)
)
;

create index FKB87BD8CE3FD9B8E5
  on tipo_produto (NCM_ID)
;

alter table produto
  add constraint FK18595AD999DDFD97
foreign key (TIPO_PRODUTO_ID) references tipo_produto (ID)
;

create table if not exists tipo_produto_editor
(
  ID bigint auto_increment
    primary key,
  PRINCIPAL tinyint(1) not null,
  TIPO_PRODUTO_ID bigint not null,
  EDITOR_ID bigint not null,
  constraint FK874255FE99DDFD97
  foreign key (TIPO_PRODUTO_ID) references tipo_produto (ID),
  constraint FK874255FEB2A11874
  foreign key (EDITOR_ID) references editor (ID)
)
;

create index FK874255FE99DDFD97
  on tipo_produto_editor (TIPO_PRODUTO_ID)
;

create index FK874255FEB2A11874
  on tipo_produto_editor (EDITOR_ID)
;

create table if not exists tipo_produto_tributacao
(
  tributacao_id bigint not null,
  tipo_produto_id bigint not null,
  primary key (tributacao_id, tipo_produto_id),
  constraint fk_tipo_produto_tributacao_2
  foreign key (tipo_produto_id) references tipo_produto (ID)
)
;

create index fk_tipo_produto_tributacao_2_idx
  on tipo_produto_tributacao (tipo_produto_id)
;

create table if not exists tipo_segmento_produto
(
  ID bigint auto_increment
    primary key,
  DESCRICAO varchar(255) not null
)
;

alter table ajuste_reparte
  add constraint FK36702058BA1ECD8
foreign key (TIPO_SEGMENTO_AJUSTE_ID) references tipo_segmento_produto (ID)
;

alter table produto
  add constraint FK_TIPO_SEGMENTO_PRODUTO
foreign key (TIPO_SEGMENTO_PRODUTO_ID) references tipo_segmento_produto (ID)
;

alter table ranking_segmento
  add constraint FK8614C9A54C709C67
foreign key (TIPO_SEGMENTO_PRODUTO_ID) references tipo_segmento_produto (ID)
;

alter table segmento_nao_recebido
  add constraint FK6551CA0D4C709C67
foreign key (TIPO_SEGMENTO_PRODUTO_ID) references tipo_segmento_produto (ID)
;

create table if not exists transportador
(
  ID bigint auto_increment
    primary key,
  RESPONSAVEL varchar(255) null,
  PESSOA_JURIDICA_ID bigint not null,
  constraint PESSOA_JURIDICA_ID
  unique (PESSOA_JURIDICA_ID),
  constraint FK629CEFCF40C343A3
  foreign key (PESSOA_JURIDICA_ID) references pessoa (ID)
)
;

create index FK629CEFCF40C343A3
  on transportador (PESSOA_JURIDICA_ID)
;

alter table assoc_veiculo_motorista_rota
  add constraint FK45A0E4B7D90B1440
foreign key (TRANSPORTADOR_ID) references transportador (ID)
;

alter table endereco_transportador
  add constraint FKBAE045D9D90B1440
foreign key (TRANSPORTADOR_ID) references transportador (ID)
;

alter table motorista
  add constraint FK8E127EECD90B1440
foreign key (TRANSPORTADOR_ID) references transportador (ID)
;

alter table telefone_transportador
  add constraint FK86E2BB3AD90B1440
foreign key (TRANSPORTADOR_ID) references transportador (ID)
;

create table if not exists tributacao
(
  id bigint auto_increment
    primary key,
  tributo varchar(45) not null,
  cst_a varchar(2) not null,
  cst varchar(2) not null,
  tipo_operacao varchar(45) not null,
  base_calculo decimal(13,4) not null,
  valor_aliquota decimal(13,4) not null,
  isento_nao_tributado tinyint(1) default '0' not null
)
;

alter table tipo_produto_tributacao
  add constraint fk_tipo_produto_tributacao_1
foreign key (tributacao_id) references tributacao (id)
;

create table if not exists tributos
(
  ID bigint auto_increment
    primary key,
  nome varchar(45) not null,
  descricao varchar(255) not null
)
;

alter table cst
  add constraint fk_cst_tributo_1
foreign key (tributo_id) references tributos (ID)
;

alter table grupo_tributo_aliquota
  add constraint FK_grupo_tributo_aliquota_2
foreign key (TRIBUTO_ID) references tributos (ID)
;

alter table natureza_operacao_tributo
  add constraint FK_tributo_id_2
foreign key (TRIBUTO_ID) references tributos (ID)
;

create table if not exists tributos_aliquotas
(
  ID bigint auto_increment
    primary key,
  TRIBUTO_ID bigint not null,
  tipo_aliquota varchar(45) not null,
  VALOR decimal(18,4) not null,
  constraint fk_aliquota_tributo_1
  foreign key (TRIBUTO_ID) references tributos (ID)
)
;

create index fk_aliquota_tributo_1
  on tributos_aliquotas (TRIBUTO_ID)
;

alter table grupo_tributo
  add constraint FK_grupo_tributo_aliquota_id
foreign key (ID) references tributos_aliquotas (ID)
;

alter table grupo_tributo_aliquota
  add constraint FK_grupo_tributo_aliquota_1
foreign key (ALIQUOTA_ID) references tributos_aliquotas (ID)
;

alter table regime_tributario_tributo_aliquota
  add constraint fk_regime_tributario_tributo_aliquota_1
foreign key (tributo_aliquota_id) references tributos_aliquotas (ID)
;

create table if not exists usuario
(
  ID bigint auto_increment
    primary key,
  CEP varchar(255) null,
  CIDADE varchar(255) null,
  CONTA_ATIVA tinyint(1) null,
  DDD varchar(255) null,
  EMAIL varchar(255) not null,
  ENDERECO varchar(255) null,
  LEMBRETE_SENHA varchar(255) null,
  LOGIN varchar(255) not null,
  NOME varchar(255) not null,
  PAIS varchar(255) null,
  SENHA varchar(255) not null,
  SOBRENOME varchar(255) null,
  TELEFONE varchar(255) null,
  BOX_ID bigint null,
  SYS tinyint(1) default '0' null,
  SUPERVISOR tinyint(1) null,
  constraint FKS5444TGTG4HJ5456Y
  foreign key (BOX_ID) references box (ID)
)
;

create index FKSHYE54DS545R5R4
  on usuario (BOX_ID)
;

create index NDX_LOGIN
  on usuario (LOGIN)
;

alter table acumulo_divida
  add constraint acumulo_divida_ibfk_5
foreign key (USUARIO_ID) references usuario (ID)
;

alter table ajuste_reparte
  add constraint FK367020587FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table baixa_cobranca
  add constraint FK2F0047737FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table box_usuario
  add constraint FK6C964C7A232855D7
foreign key (usuarios_ID) references usuario (ID)
;

alter table chamada_encalhe_cota
  add constraint chamada_encalhe_cota_ibfk_1
foreign key (USUARIO_ID) references usuario (ID)
;

alter table chamada_encalhe_cota
  add constraint chamada_encalhe_cota_ibfk_2
foreign key (USUARIO_ID) references usuario (ID)
;

alter table classificacao_nao_recebida
  add constraint FK7F7429DC7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table conferencia_enc_parcial
  add constraint FK35D69C6F5F18CCBC
foreign key (responsavel_ID) references usuario (ID)
;

alter table controle_baixa_bancaria
  add constraint FK5CE6C6207FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table controle_conferencia_encalhe_cota
  add constraint FK8EB091EB7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table controle_fechamento_encalhe
  add constraint fk_controle_fechamento_encalhe_usuario_id
foreign key (USUARIO_ID) references usuario (ID)
;

alter table controle_geracao_divida
  add constraint FK56402A877FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table desconto
  add constraint FK6B555EAB7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table desconto_cota
  add constraint FKF27C74ED7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table desconto_cota_produto_excessoes
  add constraint FKD845FEF07FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table desconto_distribuidor
  add constraint FK647A9CC27FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table desconto_produto
  add constraint FK15B685457FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table desconto_proximos_lancamentos
  add constraint FK7E8614E17FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table desenglobacao
  add constraint FKC6D712637FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table diferenca
  add constraint FKF2B9F0955F18CCBC
foreign key (responsavel_ID) references usuario (ID)
;

alter table divida
  add constraint FK783670757FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table estudo
  add constraint FK7A77787A7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table estudo_gerado
  add constraint FK4E8B943D7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table excecao_produto_cota
  add constraint FKA42FF9A27FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table expedicao
  add constraint FKAD6AB024EA1BE470
foreign key (USUARIO) references usuario (ID)
;

alter table fechamento_diario
  add constraint FK23D758477FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table fixacao_reparte
  add constraint FKA394AF6593864D4C
foreign key (ID_USUARIO) references usuario (ID)
;

alter table furo_produto
  add constraint FK301010E67FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_acumulo_divida
  add constraint FKA75B40057FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_alteracao_preco_venda
  add constraint fk_HISTORICO_ALTERACAO_PRECO_VENDA_USUARIO
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_desconto_cota_produto_excessoes
  add constraint FK56EB62817FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_desconto_produto_edicoes
  add constraint FK9EC901397FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_desconto_produtos
  add constraint FK6FDFA8FF7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_descontos_editores
  add constraint fk_historico_descontos_editores_3
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_descontos_fornecedores
  add constraint FKB4E995F97FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_lancamento
  add constraint FKF4FBF5497FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_movto_financeiro_cota
  add constraint FKAB46D6ED7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table historico_situacao_cota
  add constraint FKA23483427FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table item_nota_fiscal_entrada
  add constraint FK3EDE8DFB7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table lancamento
  add constraint FK1D53917A7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table mix_cota_produto
  add constraint FK7C1819693864D4C
foreign key (ID_USUARIO) references usuario (ID)
;

alter table movimento_estoque
  add constraint FKBEB397FCB0C2856Cef448115
foreign key (APROVADOR_ID) references usuario (ID)
;

alter table movimento_estoque
  add constraint FKBEB397FC7FFF790Eef448115
foreign key (USUARIO_ID) references usuario (ID)
;

alter table movimento_estoque_cota
  add constraint FKBEB397FCB0C2856C459444c3
foreign key (APROVADOR_ID) references usuario (ID)
;

alter table movimento_estoque_cota
  add constraint FKBEB397FC7FFF790E459444c3
foreign key (USUARIO_ID) references usuario (ID)
;

alter table movimento_financeiro_cota
  add constraint FKBEB397FCB0C2856Cc67ee7a9
foreign key (APROVADOR_ID) references usuario (ID)
;

alter table movimento_financeiro_cota
  add constraint FKBEB397FC7FFF790Ec67ee7a9
foreign key (USUARIO_ID) references usuario (ID)
;

alter table nota_fiscal_entrada
  add constraint FK90AD5277FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table parametro_usuario_box
  add constraint FK94C9BED47FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table recebimento_fisico
  add constraint FKE6D238AF36A347F1
foreign key (conferente_ID) references usuario (ID)
;

alter table recebimento_fisico
  add constraint FKE6D238AF68C2472D
foreign key (recebedor_ID) references usuario (ID)
;

alter table registro_cota_regiao
  add constraint FK24C9625D7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table segmento_nao_recebido
  add constraint FK6551CA0D7FFF790E
foreign key (USUARIO_ID) references usuario (ID)
;

alter table semaforo
  add constraint FK1_USUARIO_SEMAFORO
foreign key (USUARIO_ID) references usuario (ID)
;

create table if not exists usuario_grupo_permissao
(
  USUARIO_ID bigint not null,
  gruposPermissoes_ID bigint not null,
  primary key (USUARIO_ID, gruposPermissoes_ID),
  constraint FKC8F99C07FFF790E
  foreign key (USUARIO_ID) references usuario (ID),
  constraint FKC8F99C0EE8B8DB6
  foreign key (gruposPermissoes_ID) references grupo_permissao (ID)
)
;

create index FKC8F99C07FFF790E
  on usuario_grupo_permissao (USUARIO_ID)
;

create index FKC8F99C0EE8B8DB6
  on usuario_grupo_permissao (gruposPermissoes_ID)
;

create table if not exists usuario_perfil_usuario
(
  USUARIO_ID bigint not null,
  perfilUsuario_ID bigint not null,
  constraint perfilUsuario_ID
  unique (perfilUsuario_ID),
  constraint FK355A28EC7FFF790E
  foreign key (USUARIO_ID) references usuario (ID),
  constraint FK355A28ECDC85056E
  foreign key (perfilUsuario_ID) references perfil_usuario (ID)
)
;

create index FK355A28EC7FFF790E
  on usuario_perfil_usuario (USUARIO_ID)
;

create index FK355A28ECDC85056E
  on usuario_perfil_usuario (perfilUsuario_ID)
;

create table if not exists usuario_permissao
(
  USUARIO_ID bigint not null,
  PERMISSAO_ID varchar(255) null,
  constraint FK19B419767FFF790E
  foreign key (USUARIO_ID) references usuario (ID)
)
;

create index FK19B419767FFF790E
  on usuario_permissao (USUARIO_ID)
;

create table if not exists vale_desconto_produto
(
  VALE_DESCONTO_ID bigint not null,
  PRODUTO_EDICAO_ID bigint not null,
  primary key (VALE_DESCONTO_ID, PRODUTO_EDICAO_ID),
  constraint PRODUTO_EDICAO_ID
  unique (PRODUTO_EDICAO_ID),
  constraint FK1A20C6C041EC3F5D
  foreign key (VALE_DESCONTO_ID) references produto_edicao (ID),
  constraint FK1A20C6C0A53173D3
  foreign key (PRODUTO_EDICAO_ID) references produto_edicao (ID)
)
;

create index FK1A20C6C041EC3F5D
  on vale_desconto_produto (VALE_DESCONTO_ID)
;

create index FK1A20C6C0A53173D3
  on vale_desconto_produto (PRODUTO_EDICAO_ID)
;

create table if not exists veiculo
(
  ID bigint auto_increment
    primary key,
  PLACA varchar(255) null,
  TIPO_VEICULO varchar(255) null,
  TRANSPORTADOR_ID bigint null,
  constraint FK3F3ABBEFD90B1440
  foreign key (TRANSPORTADOR_ID) references transportador (ID)
)
;

create index FK3F3ABBEFD90B1440
  on veiculo (TRANSPORTADOR_ID)
;

alter table assoc_veiculo_motorista_rota
  add constraint FK45A0E4B797A7E184
foreign key (VEICULO) references veiculo (ID)
;

create table if not exists venda_produto
(
  id bigint auto_increment
    primary key,
  DATA_VENCIMENTO_DEBITO date null,
  DATA_VENDA date null,
  HORARIO_VENDA time null,
  QNT_PRODUTO decimal(18,4) null,
  TIPO_COMERCIALIZACAO_VENDA varchar(255) null,
  TIPO_VENDA_ENCALHE varchar(255) not null,
  VALOR_TOTAL_VENDA decimal(18,4) null,
  ID_COTA bigint null,
  ID_PRODUTO_EDICAO bigint null,
  USUARIO_ID bigint not null,
  PRECO_VENDA decimal(18,4) null,
  PRECO_COM_DESCONTO decimal(18,4) null,
  VALOR_DESCONTO decimal(18,4) null,
  DATA_OPERACAO date null,
  constraint FKBE5F75D6F1916CB0
  foreign key (ID_COTA) references cota (ID),
  constraint FKBE5F75D6D2FE34B7
  foreign key (ID_PRODUTO_EDICAO) references produto_edicao (ID),
  constraint FKBE5F75D67FFF790E
  foreign key (USUARIO_ID) references usuario (ID)
)
;

create index FKBE5F75D67FFF790E
  on venda_produto (USUARIO_ID)
;

create index FKBE5F75D6D2FE34B7
  on venda_produto (ID_PRODUTO_EDICAO)
;

create index FKBE5F75D6F1916CB0
  on venda_produto (ID_COTA)
;

create index ndx_data_status
  on venda_produto (DATA_OPERACAO, TIPO_VENDA_ENCALHE)
;

create table if not exists venda_produto_movimento_estoque
(
  ID_VENDA_PRODUTO bigint not null,
  ID_MOVIMENTO_ESTOQUE bigint not null,
  primary key (ID_VENDA_PRODUTO, ID_MOVIMENTO_ESTOQUE),
  constraint ID_MOVIMENTO_ESTOQUE
  unique (ID_MOVIMENTO_ESTOQUE),
  constraint FK312156EC1EDD2580
  foreign key (ID_VENDA_PRODUTO) references venda_produto (id),
  constraint FK312156ECEC4995FE
  foreign key (ID_MOVIMENTO_ESTOQUE) references movimento_estoque (id)
)
;

create index FK312156EC1EDD2580
  on venda_produto_movimento_estoque (ID_VENDA_PRODUTO)
;

create index FK312156ECEC4995FE
  on venda_produto_movimento_estoque (ID_MOVIMENTO_ESTOQUE)
;

create table if not exists venda_produto_movimento_financeiro
(
  ID_VENDA_PRODUTO bigint not null,
  ID_MOVIMENTO_FINANCEIRO bigint not null,
  primary key (ID_VENDA_PRODUTO, ID_MOVIMENTO_FINANCEIRO),
  constraint ID_MOVIMENTO_FINANCEIRO
  unique (ID_MOVIMENTO_FINANCEIRO),
  constraint FK2665ED381EDD2580
  foreign key (ID_VENDA_PRODUTO) references venda_produto (id),
  constraint FK2665ED3829F6062D
  foreign key (ID_MOVIMENTO_FINANCEIRO) references movimento_financeiro_cota (id)
)
;

create index FK2665ED381EDD2580
  on venda_produto_movimento_financeiro (ID_VENDA_PRODUTO)
;

create index FK2665ED3829F6062D
  on venda_produto_movimento_financeiro (ID_MOVIMENTO_FINANCEIRO)
;

create table if not exists verificalcto
(
  PROD_ID int null,
  QDT int null
)
;

create table if not exists verificar
(
  prod_id int null
)
;

create table if not exists view_consolidado_movimento_estoque_cota
(
  COTA_ID tinyint not null,
  PRODUTO_ID tinyint not null,
  EDITOR_ID tinyint not null,
  PRODUTO_EDICAO_ID tinyint not null,
  NUMERO_EDICAO tinyint not null,
  DATA_MOVIMENTO tinyint not null,
  PRECO_VENDA tinyint not null,
  QNT_ENTRADA_PRODUTO tinyint not null,
  QNT_SAIDA_PRODUTO tinyint not null,
  VALOR_TOTAL_VENDA tinyint not null,
  DESCONTO_PRODUTO tinyint not null,
  VALOR_TOTAL_VENDA_COM_DESCONTO tinyint not null
)
;

create table if not exists xitao
(
  id int null
)
;

create view lan as
  SELECT
    `p`.`CODIGO`                        AS `codigo`,
    `pe`.`NUMERO_EDICAO`                AS `numero_edicao`,
    `l`.`ID`                            AS `ID`,
    `l`.`ALTERADO_INTERFACE`            AS `ALTERADO_INTERFACE`,
    `l`.`DATA_CRIACAO`                  AS `DATA_CRIACAO`,
    `l`.`DATA_LCTO_DISTRIBUIDOR`        AS `DATA_LCTO_DISTRIBUIDOR`,
    `l`.`DATA_LCTO_PREVISTA`            AS `DATA_LCTO_PREVISTA`,
    `l`.`DATA_REC_DISTRIB`              AS `DATA_REC_DISTRIB`,
    `l`.`DATA_REC_PREVISTA`             AS `DATA_REC_PREVISTA`,
    `l`.`DATA_FIN_MAT_DISTRIB`          AS `DATA_FIN_MAT_DISTRIB`,
    `l`.`DATA_STATUS`                   AS `DATA_STATUS`,
    `l`.`REPARTE`                       AS `REPARTE`,
    `l`.`REPARTE_PROMOCIONAL`           AS `REPARTE_PROMOCIONAL`,
    `l`.`SEQUENCIA_MATRIZ`              AS `SEQUENCIA_MATRIZ`,
    `l`.`STATUS`                        AS `STATUS`,
    `l`.`TIPO_LANCAMENTO`               AS `TIPO_LANCAMENTO`,
    `l`.`PRODUTO_EDICAO_ID`             AS `PRODUTO_EDICAO_ID`,
    `l`.`EXPEDICAO_ID`                  AS `EXPEDICAO_ID`,
    `l`.`USUARIO_ID`                    AS `USUARIO_ID`,
    `l`.`NUMERO_LANCAMENTO`             AS `NUMERO_LANCAMENTO`,
    `l`.`PERIODO_LANCAMENTO_PARCIAL_ID` AS `PERIODO_LANCAMENTO_PARCIAL_ID`,
    `l`.`NUMERO_REPROGRAMACOES`         AS `NUMERO_REPROGRAMACOES`,
    `l`.`ESTUDO_ID`                     AS `ESTUDO_ID`,
    `l`.`juramentado`                   AS `juramentado`
  FROM ((`db_00757374`.`lancamento` `l`
    JOIN `db_00757374`.`produto_edicao` `pe` ON ((`pe`.`ID` = `l`.`PRODUTO_EDICAO_ID`))) JOIN
    `db_00757374`.`produto` `p` ON ((`p`.`ID` = `pe`.`PRODUTO_ID`)));

create view pe as
  SELECT
    `p`.`CODIGO`                         AS `codigo`,
    `pe`.`TIPO`                          AS `TIPO`,
    `pe`.`ID`                            AS `ID`,
    `pe`.`ATIVO`                         AS `ATIVO`,
    `pe`.`BOLETIM_INFORMATIVO`           AS `BOLETIM_INFORMATIVO`,
    `pe`.`DESCRICAO_BRINDE`              AS `DESCRICAO_BRINDE`,
    `pe`.`VENDE_BRINDE_SEPARADO`         AS `VENDE_BRINDE_SEPARADO`,
    `pe`.`CHAMADA_CAPA`                  AS `CHAMADA_CAPA`,
    `pe`.`CODIGO_DE_BARRAS_CORPORATIVO`  AS `CODIGO_DE_BARRAS_CORPORATIVO`,
    `pe`.`CODIGO_DE_BARRAS`              AS `CODIGO_DE_BARRAS`,
    `pe`.`DATA_DESATIVACAO`              AS `DATA_DESATIVACAO`,
    `pe`.`COMPRIMENTO`                   AS `COMPRIMENTO`,
    `pe`.`ESPESSURA`                     AS `ESPESSURA`,
    `pe`.`LARGURA`                       AS `LARGURA`,
    `pe`.`EXPECTATIVA_VENDA`             AS `EXPECTATIVA_VENDA`,
    `pe`.`NOME_COMERCIAL`                AS `NOME_COMERCIAL`,
    `pe`.`NUMERO_EDICAO`                 AS `NUMERO_EDICAO`,
    `pe`.`ORIGEM`                        AS `ORIGEM`,
    `pe`.`PACOTE_PADRAO`                 AS `PACOTE_PADRAO`,
    `pe`.`PARCIAL`                       AS `PARCIAL`,
    `pe`.`PEB`                           AS `PEB`,
    `pe`.`PERMITE_VALE_DESCONTO`         AS `PERMITE_VALE_DESCONTO`,
    `pe`.`PESO`                          AS `PESO`,
    `pe`.`POSSUI_BRINDE`                 AS `POSSUI_BRINDE`,
    `pe`.`PRECO_CUSTO`                   AS `PRECO_CUSTO`,
    `pe`.`PRECO_PREVISTO`                AS `PRECO_PREVISTO`,
    `pe`.`PRECO_VENDA`                   AS `PRECO_VENDA`,
    `pe`.`REPARTE_DISTRIBUIDO`           AS `REPARTE_DISTRIBUIDO`,
    `pe`.`DESCONTO_LOGISTICA_ID`         AS `DESCONTO_LOGISTICA_ID`,
    `pe`.`PRODUTO_ID`                    AS `PRODUTO_ID`,
    `pe`.`PRODUTO_EDICAO_ID`             AS `PRODUTO_EDICAO_ID`,
    `pe`.`BRINDE_ID`                     AS `BRINDE_ID`,
    `pe`.`CLASSE_SOCIAL`                 AS `CLASSE_SOCIAL`,
    `pe`.`FAIXA_ETARIA`                  AS `FAIXA_ETARIA`,
    `pe`.`FORMATO_PRODUTO`               AS `FORMATO_PRODUTO`,
    `pe`.`SEXO`                          AS `SEXO`,
    `pe`.`TEMA_PRINCIPAL`                AS `TEMA_PRINCIPAL`,
    `pe`.`TEMA_SECUNDARIO`               AS `TEMA_SECUNDARIO`,
    `pe`.`TIPO_LANCAMENTO`               AS `TIPO_LANCAMENTO`,
    `pe`.`CARACTERISTICA_PRODUTO`        AS `CARACTERISTICA_PRODUTO`,
    `pe`.`DESCONTO`                      AS `DESCONTO`,
    `pe`.`DESCRICAO_DESCONTO`            AS `DESCRICAO_DESCONTO`,
    `pe`.`GRUPO_PRODUTO`                 AS `GRUPO_PRODUTO`,
    `pe`.`DESCONTO_ID`                   AS `DESCONTO_ID`,
    `pe`.`FORMA_FISICA`                  AS `FORMA_FISICA`,
    `pe`.`TIPO_CLASSIFICACAO_PRODUTO_ID` AS `TIPO_CLASSIFICACAO_PRODUTO_ID`,
    `pe`.`HISTORICO`                     AS `HISTORICO`,
    `pe`.`VINCULAR_RECOLHIMENTO`         AS `VINCULAR_RECOLHIMENTO`,
    `pe`.`CODIGO_NBM`                    AS `CODIGO_NBM`
  FROM (`db_00757374`.`produto_edicao` `pe`
    JOIN `db_00757374`.`produto` `p` ON ((`p`.`ID` = `pe`.`PRODUTO_ID`)));

create view view_desconto_cota_fornecedor_produtos_edicoes as
  SELECT
    `d`.`ID`                       AS `DESCONTO_ID`,
    `c`.`ID`                       AS `COTA_ID`,
    `c`.`NUMERO_COTA`              AS `NUMERO_COTA`,
    (CASE WHEN (`p`.`TIPO` = 'F')
      THEN `p`.`NOME`
     ELSE `p`.`RAZAO_SOCIAL` END)  AS `NOME_COTA`,
    `d`.`valor`                    AS `VALOR`,
    `d`.`PREDOMINANTE`             AS `PREDOMINANTE`,
    `d`.`DATA_ALTERACAO`           AS `DATA_ALTERACAO`,
    `u`.`NOME`                     AS `NOME_USUARIO`,
    NULL                           AS `PRODUTO_ID`,
    NULL                           AS `CODIGO_PRODUTO`,
    NULL                           AS `NOME_PRODUTO`,
    NULL                           AS `PRODUTO_EDICAO_ID`,
    NULL                           AS `NUMERO_EDICAO`,
    `f`.`ID`                       AS `FORNECEDOR_ID`,
    NULL                           AS `EDITOR_ID`,
    (CASE WHEN (`p2`.`TIPO` = 'F')
      THEN `p2`.`NOME`
     ELSE `p2`.`RAZAO_SOCIAL` END) AS `NOME_FORNECEDOR`
  FROM ((((((`db_00757374`.`desconto_cota_produto_excessoes` `hdcpe`
    JOIN `db_00757374`.`cota` `c` ON ((`hdcpe`.`COTA_ID` = `c`.`ID`))) JOIN `db_00757374`.`pessoa` `p`
      ON ((`c`.`PESSOA_ID` = `p`.`ID`))) JOIN `db_00757374`.`fornecedor` `f`
      ON ((`hdcpe`.`FORNECEDOR_ID` = `f`.`ID`))) JOIN `db_00757374`.`pessoa` `p2`
      ON ((`f`.`JURIDICA_ID` = `p2`.`ID`))) JOIN `db_00757374`.`desconto` `d`
      ON ((`hdcpe`.`DESCONTO_ID` = `d`.`ID`))) JOIN `db_00757374`.`usuario` `u` ON ((`d`.`USUARIO_ID` = `u`.`ID`)))
  WHERE (isnull(`hdcpe`.`PRODUTO_ID`) AND isnull(`hdcpe`.`PRODUTO_EDICAO_ID`))
  UNION SELECT
          `d`.`ID`                      AS `DESCONTO_ID`,
          `c`.`ID`                      AS `COTA_ID`,
          `c`.`NUMERO_COTA`             AS `NUMERO_COTA`,
          (CASE WHEN (`p`.`TIPO` = 'F')
            THEN `p`.`NOME`
           ELSE `p`.`RAZAO_SOCIAL` END) AS `NOME_COTA`,
          `d`.`valor`                   AS `VALOR`,
          `d`.`PREDOMINANTE`            AS `PREDOMINANTE`,
          `d`.`DATA_ALTERACAO`          AS `DATA_ALTERACAO`,
          `u`.`NOME`                    AS `NOME_USUARIO`,
          NULL                          AS `PRODUTO_ID`,
          NULL                          AS `CODIGO_PRODUTO`,
          NULL                          AS `NOME_PRODUTO`,
          NULL                          AS `PRODUTO_EDICAO_ID`,
          NULL                          AS `NUMERO_EDICAO`,
          NULL                          AS `FORNECEDOR_ID`,
          `e`.`ID`                      AS `EDITOR_ID`,
          NULL                          AS `NOME_FORNECEDOR`
        FROM ((((((`db_00757374`.`desconto_cota_produto_excessoes` `hdcpe`
          JOIN `db_00757374`.`cota` `c` ON ((`hdcpe`.`COTA_ID` = `c`.`ID`))) JOIN `db_00757374`.`pessoa` `p`
            ON ((`c`.`PESSOA_ID` = `p`.`ID`))) JOIN `db_00757374`.`editor` `e`
            ON ((`hdcpe`.`EDITOR_ID` = `e`.`ID`))) JOIN `db_00757374`.`pessoa` `p2`
            ON ((`e`.`JURIDICA_ID` = `p2`.`ID`))) JOIN `db_00757374`.`desconto` `d`
            ON ((`hdcpe`.`DESCONTO_ID` = `d`.`ID`))) JOIN `db_00757374`.`usuario` `u`
            ON ((`d`.`USUARIO_ID` = `u`.`ID`)))
        WHERE (isnull(`hdcpe`.`PRODUTO_ID`) AND isnull(`hdcpe`.`PRODUTO_EDICAO_ID`))
  UNION SELECT
          `d`.`ID`                       AS `DESCONTO_ID`,
          `c`.`ID`                       AS `COTA_ID`,
          `c`.`NUMERO_COTA`              AS `NUMERO_COTA`,
          (CASE WHEN (`p`.`TIPO` = 'F')
            THEN `p`.`NOME`
           ELSE `p`.`RAZAO_SOCIAL` END)  AS `NOME_COTA`,
          `d`.`valor`                    AS `VALOR`,
          `d`.`PREDOMINANTE`             AS `PREDOMINANTE`,
          `d`.`DATA_ALTERACAO`           AS `DATA_ALTERACAO`,
          `u`.`NOME`                     AS `NOME_USUARIO`,
          `pr`.`ID`                      AS `PRODUTO_ID`,
          `pr`.`CODIGO`                  AS `CODIGO_PRODUTO`,
          `pr`.`NOME`                    AS `NOME_PRODUTO`,
          NULL                           AS `PRODUTO_EDICAO_ID`,
          NULL                           AS `NUMERO_EDICAO`,
          `f`.`ID`                       AS `FORNECEDOR_ID`,
          NULL                           AS `EDITOR_ID`,
          (CASE WHEN (`p2`.`TIPO` = 'F')
            THEN `p2`.`NOME`
           ELSE `p2`.`RAZAO_SOCIAL` END) AS `NOME_FORNECEDOR`
        FROM (((((((`db_00757374`.`desconto_cota_produto_excessoes` `hdcpe`
          JOIN `db_00757374`.`cota` `c` ON ((`hdcpe`.`COTA_ID` = `c`.`ID`))) JOIN `db_00757374`.`produto` `pr`
            ON ((`hdcpe`.`PRODUTO_ID` = `pr`.`ID`))) JOIN `db_00757374`.`pessoa` `p`
            ON ((`c`.`PESSOA_ID` = `p`.`ID`))) JOIN `db_00757374`.`fornecedor` `f`
            ON ((`hdcpe`.`FORNECEDOR_ID` = `f`.`ID`))) JOIN `db_00757374`.`pessoa` `p2`
            ON ((`f`.`JURIDICA_ID` = `p2`.`ID`))) JOIN `db_00757374`.`desconto` `d`
            ON ((`hdcpe`.`DESCONTO_ID` = `d`.`ID`))) JOIN `db_00757374`.`usuario` `u`
            ON ((`d`.`USUARIO_ID` = `u`.`ID`)))
        WHERE isnull(`hdcpe`.`PRODUTO_EDICAO_ID`)
  UNION SELECT
          `d`.`ID`                       AS `DESCONTO_ID`,
          `c`.`ID`                       AS `COTA_ID`,
          `c`.`NUMERO_COTA`              AS `NUMERO_COTA`,
          (CASE WHEN (`p`.`TIPO` = 'F')
            THEN `p`.`NOME`
           ELSE `p`.`RAZAO_SOCIAL` END)  AS `NOME_COTA`,
          `d`.`valor`                    AS `VALOR`,
          `d`.`PREDOMINANTE`             AS `PREDOMINANTE`,
          `d`.`DATA_ALTERACAO`           AS `DATA_ALTERACAO`,
          `u`.`NOME`                     AS `NOME_USUARIO`,
          `pr`.`ID`                      AS `PRODUTO_ID`,
          `pr`.`CODIGO`                  AS `CODIGO_PRODUTO`,
          `pr`.`NOME`                    AS `NOME_PRODUTO`,
          `pe`.`ID`                      AS `PRODUTO_EDICAO_ID`,
          `pe`.`NUMERO_EDICAO`           AS `NUMERO_EDICAO`,
          `f`.`ID`                       AS `FORNECEDOR_ID`,
          NULL                           AS `EDITOR_ID`,
          (CASE WHEN (`p2`.`TIPO` = 'F')
            THEN `p2`.`NOME`
           ELSE `p2`.`RAZAO_SOCIAL` END) AS `NOME_FORNECEDOR`
        FROM (((((((((`db_00757374`.`desconto_cota_produto_excessoes` `hdcpe`
          JOIN `db_00757374`.`cota` `c` ON ((`hdcpe`.`COTA_ID` = `c`.`ID`))) JOIN `db_00757374`.`produto` `pr`
            ON ((`hdcpe`.`PRODUTO_ID` = `pr`.`ID`))) JOIN `db_00757374`.`produto_edicao` `pe`
            ON ((`hdcpe`.`PRODUTO_EDICAO_ID` = `pe`.`ID`))) JOIN `db_00757374`.`pessoa` `p`
            ON ((`c`.`PESSOA_ID` = `p`.`ID`))) JOIN `db_00757374`.`fornecedor` `f`
            ON ((`hdcpe`.`FORNECEDOR_ID` = `f`.`ID`))) JOIN `db_00757374`.`editor` `e`
            ON ((`hdcpe`.`EDITOR_ID` = `e`.`ID`))) JOIN `db_00757374`.`pessoa` `p2`
            ON ((`f`.`JURIDICA_ID` = `p2`.`ID`))) JOIN `db_00757374`.`desconto` `d`
            ON ((`hdcpe`.`DESCONTO_ID` = `d`.`ID`))) JOIN `db_00757374`.`usuario` `u`
            ON ((`d`.`USUARIO_ID` = `u`.`ID`)));

create view view_desconto_produtos_edicoes as
  SELECT
    `d`.`ID`             AS `DESCONTO_ID`,
    `p`.`ID`             AS `PRODUTO_ID`,
    `p`.`CODIGO`         AS `CODIGO_PRODUTO`,
    `p`.`NOME`           AS `NOME_PRODUTO`,
    `pe`.`ID`            AS `PRODUTO_EDICAO_ID`,
    `pe`.`NUMERO_EDICAO` AS `NUMERO_EDICAO`,
    `d`.`valor`          AS `VALOR`,
    `d`.`PREDOMINANTE`   AS `PREDOMINANTE`,
    `d`.`DATA_ALTERACAO` AS `DATA_ALTERACAO`,
    `u`.`NOME`           AS `NOME_USUARIO`
  FROM ((((`db_00757374`.`historico_desconto_produto_edicoes` `hdpe`
    JOIN `db_00757374`.`produto_edicao` `pe` ON ((`hdpe`.`PRODUTO_EDICAO_ID` = `pe`.`ID`))) JOIN
    `db_00757374`.`produto` `p` ON ((`pe`.`PRODUTO_ID` = `p`.`ID`))) JOIN `db_00757374`.`desconto` `d`
      ON ((`hdpe`.`DESCONTO_ID` = `d`.`ID`))) JOIN `db_00757374`.`usuario` `u` ON ((`d`.`USUARIO_ID` = `u`.`ID`)))
  UNION SELECT
          `d`.`ID`             AS `DESCONTO_ID`,
          `p`.`ID`             AS `PRODUTO_ID`,
          `p`.`CODIGO`         AS `CODIGO_PRODUTO`,
          `p`.`NOME`           AS `NOME_PRODUTO`,
          NULL                 AS `PRODUTO_EDICAO_ID`,
          NULL                 AS `NUMERO_EDICAO`,
          `d`.`valor`          AS `VALOR`,
          `d`.`PREDOMINANTE`   AS `PREDOMINANTE`,
          `d`.`DATA_ALTERACAO` AS `DATA_ALTERACAO`,
          `u`.`NOME`           AS `NOME_USUARIO`
        FROM (((`db_00757374`.`historico_desconto_produtos` `hdp`
          JOIN `db_00757374`.`produto` `p` ON ((`hdp`.`PRODUTO_ID` = `p`.`ID`))) JOIN `db_00757374`.`desconto` `d`
            ON ((`hdp`.`DESCONTO_ID` = `d`.`ID`))) JOIN `db_00757374`.`usuario` `u` ON ((`d`.`USUARIO_ID` = `u`.`ID`)));

