-- ==========================================================================

ALTER TABLE distribuidor 
DROP COLUMN REGIME_ESPECIAL,
DROP COLUMN OBRIGACAO_FISCAL;

ALTER TABLE distribuidor 
ADD COLUMN NF_INFORMACOES_ADICIONAIS VARCHAR(600) NULL,
ADD COLUMN POSSUI_REGIME_ESPECIAL_DISPENSA_INTERNA TINYINT NOT NULL DEFAULT 0 AFTER PARAR_ACUM_DIVIDAS,
ADD COLUMN NUMERO_DISPOSITIVO_LEGAL VARCHAR(60) NULL AFTER PARAR_ACUM_DIVIDAS,
ADD COLUMN DATA_LIMITE_VIGENCIA_REGIME_ESPECIAL VARCHAR(45) NULL AFTER NUMERO_DISPOSITIVO_LEGAL
;

SET FOREIGN_KEY_CHECKS=0;
ALTER TABLE distribuidor 
ADD COLUMN REGIME_TRIBUTARIO_ID BIGINT(20) NOT NULL DEFAULT 1 AFTER POSSUI_REGIME_ESPECIAL_DISPENSA_INTERNA,
ADD INDEX fk_reg_trib_distribuidor_1_idx (REGIME_TRIBUTARIO_ID ASC);
ALTER TABLE distribuidor 
ADD CONSTRAINT fk_reg_trib_distribuidor_1
  FOREIGN KEY (REGIME_TRIBUTARIO_ID)
  REFERENCES regime_tributario (ID)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
SET FOREIGN_KEY_CHECKS=1;

UPDATE distribuidor SET REGIME_TRIBUTARIO_ID='1' WHERE ID='1';
UPDATE distribuidor SET TIPO_ATIVIDADE='MERCANTIL' WHERE ID='1';

-- ==========================================================================

DELETE FROM parametro_sistema WHERE TIPO_PARAMETRO_SISTEMA='NFE_DPEC';

INSERT INTO parametro_sistema (TIPO_PARAMETRO_SISTEMA, VALOR) VALUES ('NFE_INFORMACOES_VERSAO_EMISSOR', '2.2.21');
INSERT INTO parametro_sistema (TIPO_PARAMETRO_SISTEMA, VALOR) VALUES ('NFE_INFORMACOES_AMBIENTE', 'HOMOLOGACAO');
INSERT INTO parametro_sistema (TIPO_PARAMETRO_SISTEMA, VALOR) VALUES ('NFE_INFORMACOES_TIPO_EMISSOR', 'EMISSAO_NFE_APLICATIVO_FORNECIDO_PELO_FISCO');
INSERT INTO parametro_sistema (TIPO_PARAMETRO_SISTEMA, VALOR) VALUES ('NFE_INFORMACOES_FORMATO_IMPRESSAO', 'PAISAGEM');
INSERT INTO parametro_sistema (TIPO_PARAMETRO_SISTEMA, VALOR) VALUES ('NFE_INFORMACOES_MODELO_DOCUMENTO', '55');

-- ==========================================================================

CREATE TABLE distribuidor_nota_fiscal_tipo_emissao (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  DESCRICAO varchar(60) NOT NULL,
  SEQUENCIA BIGINT(20) NOT NULL,
  TIPO_EMISSAO_ENUM varchar(60) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO distribuidor_nota_fiscal_tipo_emissao (DESCRICAO, SEQUENCIA, TIPO_EMISSAO_ENUM) VALUES ('Desobriga Emissão', 1, 'DESOBRIGA_EMISSAO');
INSERT INTO distribuidor_nota_fiscal_tipo_emissao (DESCRICAO, SEQUENCIA, TIPO_EMISSAO_ENUM) VALUES ('Consolida emissão a Jornaleiros Diversos', 2, 'CONSOLIDA_EMISSAO_A_JORNALEIROS_DIVERSOS');
INSERT INTO distribuidor_nota_fiscal_tipo_emissao (DESCRICAO, SEQUENCIA, TIPO_EMISSAO_ENUM) VALUES ('Consolida emissão por Destinatário', 3, 'CONSOLIDA_EMISSAO_POR_DESTINATARIO');

-- ==========================================================================

CREATE TABLE distribuidor_nota_fiscal_tipos (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  DESCRICAO varchar(60) NOT NULL,
  NOME_CAMPO_TELA varchar(45) NOT NULL,
  GRUPO_NOTA_FISCAL VARCHAR(45) NOT NULL,
  NOTA_FISCAL_TIPO_EMISSAO_ID bigint(20) NOT NULL,
  DISTRIBUIDOR_ID bigint(20) NOT NULL,
  PRIMARY KEY (ID),
  KEY fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_1_idx (NOTA_FISCAL_TIPO_EMISSAO_ID),
  KEY fk_distribuidor_nota_fiscal_tipos_2_idx (DISTRIBUIDOR_ID),
  CONSTRAINT fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_2 FOREIGN KEY (DISTRIBUIDOR_ID) REFERENCES distribuidor (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_1 FOREIGN KEY (NOTA_FISCAL_TIPO_EMISSAO_ID) REFERENCES distribuidor_nota_fiscal_tipo_emissao (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE distribuidor_nota_fiscal_tipos 
DROP FOREIGN KEY fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_1;
ALTER TABLE distribuidor_nota_fiscal_tipos 
CHANGE COLUMN NOTA_FISCAL_TIPO_EMISSAO_ID NOTA_FISCAL_TIPO_EMISSAO_ID BIGINT(20) NULL ;
ALTER TABLE distribuidor_nota_fiscal_tipos 
ADD CONSTRAINT fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_1
  FOREIGN KEY (NOTA_FISCAL_TIPO_EMISSAO_ID)
  REFERENCES distribuidor_nota_fiscal_tipo_emissao (ID)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
-- ==========================================================================

CREATE TABLE distribuidor_nota_fiscal_tipos_tipo_emissao (
  NOTA_FISCAL_TIPO_ID bigint(20) NOT NULL,
  NOTA_FISCAL_TIPO_EMISSAO_ID bigint(20) NOT NULL,
  PRIMARY KEY (NOTA_FISCAL_TIPO_EMISSAO_ID,NOTA_FISCAL_TIPO_ID),
  UNIQUE KEY CHAVE_UNIQUE (NOTA_FISCAL_TIPO_ID,NOTA_FISCAL_TIPO_EMISSAO_ID),
  CONSTRAINT fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_TIPO_EMISSAO_1 FOREIGN KEY (NOTA_FISCAL_TIPO_ID) REFERENCES distribuidor_nota_fiscal_tipos (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_DISTRIBUIDOR_NOTA_FISCAL_TIPOS_TIPO_EMISSAO_2 FOREIGN KEY (NOTA_FISCAL_TIPO_EMISSAO_ID) REFERENCES distribuidor_nota_fiscal_tipo_emissao (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ==========================================================================

CREATE TABLE distribuidor_tipos_emissoes_nota_fiscal (
  DISTRIBUIDOR_ID bigint(20) NOT NULL,
  NOTA_FISCAL_TIPO_EMISSAO_ID bigint(20) NOT NULL,
  SEQUENCIA bigint(20) NOT NULL,
  PRIMARY KEY (DISTRIBUIDOR_ID, NOTA_FISCAL_TIPO_EMISSAO_ID),
  UNIQUE KEY CHAVE_UNIQUE (DISTRIBUIDOR_ID, NOTA_FISCAL_TIPO_EMISSAO_ID),
  CONSTRAINT fk_DISTRIBUIDOR_TIPOS_EMISSOES_1 FOREIGN KEY (DISTRIBUIDOR_ID) REFERENCES distribuidor (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_DISTRIBUIDOR_TIPOS_EMISSOES_2 FOREIGN KEY (NOTA_FISCAL_TIPO_EMISSAO_ID) REFERENCES distribuidor_nota_fiscal_tipo_emissao (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ==========================================================================

INSERT INTO distribuidor_nota_fiscal_tipos (DESCRICAO, NOME_CAMPO_TELA, GRUPO_NOTA_FISCAL, DISTRIBUIDOR_ID) VALUES ('Nota Fiscal de Remessa para a Cota', 'notaFiscalEnvioCota', 'NOTA_FISCAL_ENVIO_PARA_COTA', 1);
INSERT INTO distribuidor_nota_fiscal_tipos (DESCRICAO, NOME_CAMPO_TELA, GRUPO_NOTA_FISCAL, DISTRIBUIDOR_ID) VALUES ('Nota Fiscal de Devolução pela Cota', 'notaFiscalDevolucaoCota', 'NOTA_FISCAL_DEVOLUCAO_PELA_COTA', 1);
INSERT INTO distribuidor_nota_fiscal_tipos (DESCRICAO, NOME_CAMPO_TELA, GRUPO_NOTA_FISCAL, DISTRIBUIDOR_ID) VALUES ('Nota Fiscal de Venda', 'notaFiscalVenda', 'NOTA_FISCAL_VENDA', 1);
INSERT INTO distribuidor_nota_fiscal_tipos (DESCRICAO, NOME_CAMPO_TELA, GRUPO_NOTA_FISCAL, DISTRIBUIDOR_ID) VALUES ('Nota Fiscal de Devolução ao Fornecedor', 'notaFiscalDevolucaoFornecedor', 'NOTA_FISCAL_DEVOLUCAO_AO_FORNECEDOR', 1);
INSERT INTO distribuidor_nota_fiscal_tipos (DESCRICAO, NOME_CAMPO_TELA, GRUPO_NOTA_FISCAL, DISTRIBUIDOR_ID) VALUES ('Nota Fiscal Simbólica de Venda ao Fornecedor', 'notaFiscalSimbolicaVendaFornecedor', 'NOTA_FISCAL_SIMBOLICA_VENDA_FORNECEDOR', 1);

-- ==========================================================================

INSERT INTO distribuidor_tipos_emissoes_nota_fiscal (DISTRIBUIDOR_ID, NOTA_FISCAL_TIPO_EMISSAO_ID, SEQUENCIA) VALUES ('1', '1', 1);
INSERT INTO distribuidor_tipos_emissoes_nota_fiscal (DISTRIBUIDOR_ID, NOTA_FISCAL_TIPO_EMISSAO_ID, SEQUENCIA) VALUES ('1', '2', 2);
INSERT INTO distribuidor_tipos_emissoes_nota_fiscal (DISTRIBUIDOR_ID, NOTA_FISCAL_TIPO_EMISSAO_ID, SEQUENCIA) VALUES ('1', '3', 3);

-- ==========================================================================

INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('1', '1');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('1', '2');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('1', '3');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('2', '1');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('2', '2');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('2', '3');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('3', '1');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('3', '2');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('3', '3');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('4', '1');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('4', '3');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('5', '1');
INSERT INTO distribuidor_nota_fiscal_tipos_tipo_emissao (NOTA_FISCAL_TIPO_ID, NOTA_FISCAL_TIPO_EMISSAO_ID) VALUES ('5', '3');

-- ==========================================================================

ALTER TABLE movimento_estoque_cota 
ADD COLUMN MOVIMENTO_ESTOQUE_COTA_ESTORNO_ID BIGINT(20) NULL DEFAULT NULL AFTER MOVIMENTO_ESTOQUE_COTA_FURO_ID,
ADD COLUMN NOTA_FISCAL_EMITIDA TINYINT(1) NOT NULL DEFAULT 0 AFTER MOVIMENTO_ESTOQUE_COTA_ESTORNO_ID,
ADD COLUMN COTA_CONTRIBUINTE_EXIGE_NF TINYINT(1) NOT NULL DEFAULT 0 AFTER NOTA_FISCAL_EMITIDA,
ADD COLUMN GERAR_COTA_CONTRIBUINTE_ICMS TINYINT(1) NOT NULL DEFAULT 0 AFTER NOTA_FISCAL_EMITIDA,
ADD COLUMN GERAR_COTA_EXIGE_NFE TINYINT(1) NOT NULL DEFAULT 0 AFTER GERAR_COTA_CONTRIBUINTE_ICMS,
ADD COLUMN FORMA_COMERCIALIZACAO varchar(20) NULL after TIPO_MOVIMENTO_ID;

ALTER TABLE movimento_estoque
ADD COLUMN NOTA_FISCAL_EMITIDA TINYINT(1) NOT NULL DEFAULT 0 AFTER DATA_CRIACAO;

-- ==========================================================================

ALTER TABLE tipo_nota_fiscal 
RENAME TO  natureza_operacao;

ALTER TABLE natureza_operacao 
DROP COLUMN EMITENTE,
DROP COLUMN DESTINATARIO;

ALTER TABLE natureza_operacao
ADD COLUMN DISTRIBUIDOR_NOTA_FISCAL_TIPO_ID BIGINT(20) NOT NULL AFTER CFOP_OUTROS_ESTADOS;

ALTER TABLE natureza_operacao 
DROP COLUMN SERIE_NF,
DROP COLUMN NOP_DESCRICAO,
DROP COLUMN NOP_CODIGO,
CHANGE COLUMN CFOP_ESTADO CFOP_ESTADO BIGINT(20) NULL DEFAULT NULL AFTER DESCRICAO,
CHANGE COLUMN CFOP_OUTROS_ESTADOS CFOP_OUTROS_ESTADOS BIGINT(20) NULL DEFAULT NULL AFTER CFOP_ESTADO;

ALTER TABLE natureza_operacao 
DROP FOREIGN KEY FK922056A4A662870A,
DROP FOREIGN KEY FK922056A44B86991A;
ALTER TABLE natureza_operacao 
CHANGE COLUMN CFOP_ESTADO CFOP_ESTADO BIGINT(20) NOT NULL ,
CHANGE COLUMN CFOP_OUTROS_ESTADOS CFOP_OUTROS_ESTADOS BIGINT(20) NOT NULL ,
DROP INDEX FK922056A4A662870A ;

ALTER TABLE natureza_operacao 
DROP COLUMN GRUPO_NOTA_FISCAL;

ALTER TABLE natureza_operacao 
ADD COLUMN CFOP_EXTERIOR VARCHAR(45) NULL AFTER CFOP_OUTROS_ESTADOS;

ALTER TABLE natureza_operacao 
ADD COLUMN TIPO_EMITENTE VARCHAR(45) NOT NULL AFTER TIPO_ATIVIDADE;

ALTER TABLE natureza_operacao
ADD COLUMN TIPO_DESTINATARIO VARCHAR(45) NOT NULL AFTER TIPO_EMITENTE;

ALTER TABLE natureza_operacao ADD COLUMN NOTA_FISCAL_NUMERO_NF BIGINT(20) NOT NULL DEFAULT 0;
ALTER TABLE natureza_operacao ADD column NOTA_FISCAL_SERIE BIGINT(20) NOT NULL after DISTRIBUIDOR_NOTA_FISCAL_TIPO_ID;

ALTER TABLE natureza_operacao 
ADD COLUMN GERAR_NOTAS_REFERENCIADAS TINYINT(1) NOT NULL AFTER GERAR_COTA_NAO_EXIGE_NFE;

ALTER TABLE natureza_operacao 
ADD COLUMN DATA_CRIACAO DATETIME NOT NULL AFTER NOTA_FISCAL_NUMERO_NF,
ADD COLUMN LAST_UPDATE_TIME DATETIME NOT NULL AFTER DATA_CRIACAO,
ADD COLUMN USUARIO_LOGADO VARCHAR(45) NOT NULL AFTER LAST_UPDATE_TIME;

ALTER TABLE natureza_operacao 
CHANGE COLUMN DISTRIBUIDOR_NOTA_FISCAL_TIPO_ID NOTA_FISCAL_DEVOLUCAO_SIMBOLICA TINYINT(1) NOT NULL AFTER NOTA_FISCAL_VENDA_CONSIGNADO,
CHANGE COLUMN CONTRIBUINTE NOTA_FISCAL_VENDA_CONSIGNADO TINYINT(1) NOT NULL;

ALTER TABLE natureza_operacao 
ADD COLUMN GERAR_COTA_CONTRIBUINTE_ICMS TINYINT(1) NOT NULL AFTER NOTA_FISCAL_DEVOLUCAO_SIMBOLICA,
ADD COLUMN GERAR_COTA_EXIGE_NFE TINYINT(1) NOT NULL AFTER GERAR_COTA_CONTRIBUINTE_ICMS,
ADD COLUMN GERAR_COTA_NAO_EXIGE_NFE TINYINT(1) NOT NULL AFTER GERAR_COTA_EXIGE_NFE;

alter table natureza_operacao add column FORMA_COMERCIALIZACAO varchar(20) NOT NULL after TIPO_OPERACAO;

-- ==========================================================================

SET FOREIGN_KEY_CHECKS=0;

DELIMITER $$
DROP TRIGGER IF EXISTS natureza_operacao_BUPD$$
CREATE TRIGGER natureza_operacao_BUPD BEFORE UPDATE ON natureza_operacao FOR EACH ROW
BEGIN  
	SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP, NEW.USUARIO_LOGADO=USER();   
END$$
DELIMITER ;

DELIMITER $$
DROP TRIGGER IF EXISTS natureza_operacao_BINS$$

CREATE TRIGGER natureza_operacao_BINS BEFORE INSERT ON natureza_operacao FOR EACH ROW
BEGIN  
	SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP, NEW.DATA_CRIACAO = CURRENT_TIMESTAMP, NEW.USUARIO_LOGADO=USER();   
END$$
DELIMITER ;

truncate table natureza_operacao;
INSERT INTO natureza_operacao VALUES 
('1', '0', '0', '0', '0', '0', '0', 'Entrada em Devolução de Remessa em Consignação ', '1918', '2918', NULL, 'MERCANTIL', 'COTA', 'DISTRIBUIDOR', 'ENTRADA', 'CONSIGNADO', '1', '0', '0000-00-00 00:00:00', '2014-08-20 16:00:58', 'awsuser@10.37.8.145'						),
('2', '0', '0', '0', '0', '0', '0', 'Entrada de Devolução Simbólica de Mercadoria Vendida Remetida anteriormente em consignação', '1919', '2919', NULL, 'MERCANTIL', 'COTA', 'DISTRIBUIDOR', 'ENTRADA', 'CONSIGNADO', '2', '0', '0000-00-00 00:00:00', '0000-00-00 00:00:00', ''),
('3', '0', '0', '0', '0', '0', '0', 'Venda de Mercadoria remetida anteriormente em Consignação', '5114', '6114', NULL, 'MERCANTIL', 'DISTRIBUIDOR', 'COTA', 'SAIDA', 'CONSIGNADO', '3', '0', '0000-00-00 00:00:00', '0000-00-00 00:00:00', ''                                   ),
('5', '0', '0', '1', '0', '1', '0', 'Remessa de mercadoria em consignação mercantil ou industrial', '5917', '6917', NULL, 'MERCANTIL', 'DISTRIBUIDOR', 'COTA', 'SAIDA', 'CONSIGNADO', '5', '0', '0000-00-00 00:00:00', '0000-00-00 00:00:00', ''                                ),
('7', '0', '0', '0', '0', '0', '0', 'Devolução de Mercadoria Recebida em Consignação', '5918', '6918', NULL, 'MERCANTIL', 'DISTRIBUIDOR', 'FORNECEDOR', 'SAIDA', 'CONSIGNADO', '6', '0', '0000-00-00 00:00:00', '0000-00-00 00:00:00', ''                                       ),
('9', '0', '0', '0', '0', '0', '0', 'Devolução Simbólica de Mercadoria Vendida recebida anteriormente em Consignação', '5919', '6919', NULL, 'MERCANTIL', 'DISTRIBUIDOR', 'FORNECEDOR', 'ENTRADA', 'CONSIGNADO', '8', '0', '0000-00-00 00:00:00', '0000-00-00 00:00:00', ''     ),
('11', '0', '0', '0', '0', '0', '0', 'Venda de Mercadoria adquirida ou recebida de terceiros', '5115', '6115', '', 'PRESTADOR_SERVICO', 'DISTRIBUIDOR', 'COTA', 'SAIDA', 'CONTA_FIRME', '10', '0', '0000-00-00 00:00:00', '0000-00-00 00:00:00', ''                             ),
('12', '0', '0', '0', '0', '0', '0', 'Entrada de Retorno de Remessa para Distribuição', '1949', '2949', '', 'PRESTADOR_SERVICO', 'COTA', 'DISTRIBUIDOR', 'ENTRADA', 'CONSIGNADO', '11', '0', '0000-00-00 00:00:00', '2014-08-20 16:00:58', 'awsuser@10.37.8.145'                ),
('13', '0', '0', '0', '0', '0', '0', 'Devolução de Remessa para Distribuição', '5949', '6949', NULL, 'PRESTADOR_SERVICO', 'DISTRIBUIDOR', 'FORNECEDOR', 'SAIDA', 'CONSIGNADO', '12', '0', '0000-00-00 00:00:00', '2014-08-20 16:00:58', 'awsuser@10.37.8.145'                   ),
('14', '0', '0', '0', '0', '0', '0', 'Devolução de Remessa para Distribuição', '5949', '6949', NULL, 'PRESTADOR_SERVICO', 'COTA', 'DISTRIBUIDOR', 'SAIDA', 'CONSIGNADO', '13', '0', '0000-00-00 00:00:00', '2014-08-20 16:00:58', 'awsuser@10.37.8.145'                         ),
('16', '0', '0', '0', '0', '0', '0', 'Remessa para Distribuição', '5949', '6949', NULL, 'PRESTADOR_SERVICO', 'DISTRIBUIDOR', 'COTA', 'SAIDA', 'CONSIGNADO', '15', '0', '0000-00-00 00:00:00', '2014-08-20 16:00:58', 'awsuser@10.37.8.145'                                      ),
('22', '0', '0', '1', '0', '1', '0', 'Remessa para Distribuição (NECA / Danfe)', '5949', '6949', NULL, 'PRESTADOR_FILIAL', 'DISTRIBUIDOR', 'COTA', 'SAIDA', 'CONSIGNADO', '21', '18', '0000-00-00 00:00:00', '2014-09-22 18:16:12', 'awsuser@10.37.8.116'                       ),
('26', '1', '0', '0', '1', '1', '1', 'Entrada de Retorno de Remessa para Distribuição', '1949', '2949', NULL, 'PRESTADOR_FILIAL', 'COTA', 'DISTRIBUIDOR', 'SAIDA', 'CONSIGNADO', '25', '9', '0000-00-00 00:00:00', '2014-08-27 18:24:08', 'awsuser@10.37.8.212'                 ),
('28', '1', '0', '1', '0', '1', '0', 'Venda de Mercadoria recebida anteriormente em Consignação', '5115', '6115', NULL, 'PRESTADOR_FILIAL', 'DISTRIBUIDOR', 'COTA', 'SAIDA', 'CONSIGNADO', '27', '9', '0000-00-00 00:00:00', '2014-08-26 16:18:15', 'awsuser@10.129.28.111'     ),
('30', '0', '0', '0', '0', '0', '0', 'Remessa para Distribuição', '5949', '6949', NULL, 'PRESTADOR_FILIAL', 'FORNECEDOR', 'DISTRIBUIDOR', 'ENTRADA', 'CONSIGNADO', '99', '0', '0000-00-00 00:00:00', '0000-00-00 00:00:00', ''                                                  ),
('31', '0', '0', '0', '0', '0', '0', 'Devolução de Remessa para Distribuição', '5949', '6949', NULL, 'PRESTADOR_FILIAL', 'COTA', 'DISTRIBUIDOR', 'ENTRADA', 'CONSIGNADO', '29', '0', '2014-07-04 11:03:12', '2014-07-04 11:03:12', 'root@localhost'                             );
('32', '0', '0', '0', '0', '0', '0', 'Remessa de mercadoria em consignação mercantil ou industrial', '5917', '6917', NULL, 'MERCANTIL', 'FORNECEDOR', 'DISTRIBUIDOR', 'ENTRADA', 'CONSIGNADO', '98', '0', '2014-07-04 11:03:12', '2014-07-04 11:03:12', 'root@localhost'        );
SET FOREIGN_KEY_CHECKS=1;

-- ==========================================================================

DELETE FROM processo_nfe WHERE processo_nfe_id in (4, 15, 17, 18, 19, 20, 21, 23, 24, 27, 29);

DELETE FROM natureza_operacao WHERE ID='4';
DELETE FROM natureza_operacao WHERE ID='15';
DELETE FROM natureza_operacao WHERE ID='17';
DELETE FROM natureza_operacao WHERE ID='18';
DELETE FROM natureza_operacao WHERE ID='19';
DELETE FROM natureza_operacao WHERE ID='20';
DELETE FROM natureza_operacao WHERE ID='21';
DELETE FROM natureza_operacao WHERE ID='23';
DELETE FROM natureza_operacao WHERE ID='24';
DELETE FROM natureza_operacao WHERE ID='27';
DELETE FROM natureza_operacao WHERE ID='29';

-- ==========================================================================

CREATE TABLE natureza_operacao_tipo_movimento (
  NATUREZA_OPERACAO_ID bigint(20) NOT NULL,
  TIPO_MOVIMENTO_ID bigint(20) NOT NULL,
  PRIMARY KEY (NATUREZA_OPERACAO_ID,TIPO_MOVIMENTO_ID),
  KEY FK_tipo_movimento_id (TIPO_MOVIMENTO_ID),
  CONSTRAINT FK_tipo_movimento_id_2 FOREIGN KEY (TIPO_MOVIMENTO_ID) REFERENCES tipo_movimento (ID),
  CONSTRAINT FK_natureza_operacao_tipo_movimento FOREIGN KEY (NATUREZA_OPERACAO_ID) REFERENCES NATUREZA_OPERACAO (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ==========================================================================

INSERT INTO tipo_movimento (TIPO, APROVACAO_AUTOMATICA, DESCRICAO, GRUPO_MOVIMENTO_ESTOQUE, INCIDE_DIVIDA, INCIDE_JURAMENTADO, OPERACAO_ESTOQUE) VALUES ('FISCAL', '1', 'Devolução de Encalhe - Cota', 'CONTRAPARTIDA_DEVOLUCAO_ENCALHE_COTA', '0', '0', 'ENTRADA');
INSERT INTO tipo_movimento (TIPO, APROVACAO_AUTOMATICA, DESCRICAO, INCIDE_DIVIDA, INCIDE_JURAMENTADO, OPERACAO_ESTOQUE) VALUES ('FISCAL', '1', 'Devolução de Encalhe - Distribuidor', '0', '0', 'SAIDA');

INSERT INTO natureza_operacao_tipo_movimento(NATUREZA_OPERACAO_ID, TIPO_MOVIMENTO_ID)
VALUES (3,1), (3,4), (5,6), (5,9)
	, (5,18), (5,21), (1,26), (22,6)
	, (22,9), (22,18), (22,21), (9,(select (id + 1) from tipo_movimento tm where tm.GRUPO_MOVIMENTO_ESTOQUE = 'CONTRAPARTIDA_DEVOLUCAO_ENCALHE_COTA'))
	, (3,(select id from tipo_movimento tm where tm.GRUPO_MOVIMENTO_ESTOQUE = 'CONTRAPARTIDA_DEVOLUCAO_ENCALHE_COTA')), (28,(select id from tipo_movimento tm where tm.GRUPO_MOVIMENTO_ESTOQUE = 'CONTRAPARTIDA_DEVOLUCAO_ENCALHE_COTA')), (7,66), (13,66)
	, (30,66), (26,26), (31, 66);

-- ==========================================================================

CREATE TABLE distribuidor_tipo_nota_natureza_operacao (
  TIPO_NOTA_ID bigint(20) NOT NULL,
  NATUREZA_OPERACAO_ID bigint(20) NOT NULL,
  PRIMARY KEY (TIPO_NOTA_ID,NATUREZA_OPERACAO_ID),
  KEY FK_tipo_movimento_id (NATUREZA_OPERACAO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO DISTRIBUIDOR_TIPO_NOTA_NATUREZA_OPERACAO (TIPO_NOTA_ID, NATUREZA_OPERACAO_ID) 
VALUES ('1', '5'), ('1', '22'), ('2', '26'), ('4', '30'), ('3', '28');

-- ==========================================================================

update fornecedor f set FORNECEDOR_UNIFICADOR_ID = 16 where f.id in (1, 2);

-- ==========================================================================

insert into pessoa (TIPO, ID, EMAIL, APELIDO, CPF, DATA_NASCIMENTO, ESTADO_CIVIL, NACIONALIDADE, NATURALIDADE, NOME, ORGAO_EMISSOR, RG, SEXO, SOCIO_PRINCIPAL, UF_ORGAO_EMISSOR, CNPJ, INSC_ESTADUAL, INSC_MUNICIPAL, NOME_FANTASIA, RAZAO_SOCIAL, PESSOA_ID_CONJUGE) values('J','3882','jornaleiro@jornaleiro.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'10.438.248/0001-23','000000000000','99.999-9','Jornaleiro Diversos','Jornaleiro Diversos',NULL);

-- ==========================================================================

-- ########### Gerar a Tabela NOTA_FISCAL_PESSOA ###########
create table NOTA_FISCAL_PESSOA( 
ID bigint(20) NOT NULL AUTO_INCREMENT , 
NOME varchar(60), 
EMAIL varchar(60),
CPF varchar(60), 
RG varchar(60),
RAZAO_SOCIAL varchar(60) , 
NOME_FANTASIA varchar(60) , 
CNPJ varchar(18) , 
INSCRICAO_ESTADUAL varchar(60) , 
NOTA_FICAL_ENDERECO_ID bigint(20) , 
PRIMARY KEY (ID));

ALTER TABLE nota_fiscal_pessoa 
ADD COLUMN TIPO_PESSOA VARCHAR(45) NULL AFTER NOTA_FICAL_ENDERECO_ID;

-- ==========================================================================

CREATE TABLE nota_fiscal_origem_item (
  id  bigint(20) NOT NULL AUTO_INCREMENT,
  ORIGEM_ID BIGINT(20) NOT NULL,
  TIPO VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));

-- ==========================================================================

CREATE TABLE nota_fiscal_item_nota_fiscal_origem_item (
  NOTA_FISCAL_ITEM_ID BIGINT(20) NOT NULL,
  ORIGEM_ITEM_NOTA_FISCAL_ID BIGINT(20) NOT NULL,
  PRIMARY KEY (NOTA_FISCAL_ITEM_ID, ORIGEM_ITEM_NOTA_FISCAL_ID));

-- ==========================================================================

SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE produto
ADD COLUMN GRUPO_TRIBUTO_ID BIGINT(20) NULL AFTER DESCONTO_ID;

alter table produto add constraint FK_GRUPO_TRIBUTO_ID_1 FOREIGN KEY (GRUPO_TRIBUTO_ID) REFERENCES grupo_tributo (ID);

DROP TABLE IF EXISTS aliquota;

CREATE TABLE aliquota (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  VALOR decimal(18,4) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS tributo;

CREATE TABLE tributo (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  DESCRICAO varchar(60) DEFAULT NULL,
  ALIQUOTA_ID bigint(20) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS grupo_tributo_aliquota;

CREATE TABLE grupo_tributo_aliquota (
  TRIBUTO_ID bigint(20) NOT NULL,
  ALIQUOTA_ID bigint(20) NOT NULL,
  PRIMARY KEY (TRIBUTO_ID,ALIQUOTA_ID),
  KEY FK_grupo_tributo_aliquota_1 (ALIQUOTA_ID),
  CONSTRAINT FK_grupo_tributo_aliquota_2 FOREIGN KEY (TRIBUTO_ID) REFERENCES tributo (ID),
  CONSTRAINT FK_grupo_tributo_aliquota_1 FOREIGN KEY (ALIQUOTA_ID) REFERENCES aliquota (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS grupo_tributo;

CREATE TABLE grupo_tributo (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  DESCRICAO varchar(60) DEFAULT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_grupo_tributo_aliquota_id FOREIGN KEY (ID) REFERENCES aliquota (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS=1;

-- ==========================================================================

ALTER TABLE nota_fiscal_novo 
DROP INDEX ENDERECO_ID_EMITENTE ,
DROP INDEX ENDERECO_ID_DESTINATARIO;

alter table nota_fiscal_pessoa add column ID_PESSOA_ORIGINAL bigint(20) NOT NULL after TIPO_PESSOA;

alter table nota_fiscal_novo change MODALIDADE_FRENTE MODALIDADE_FRETE int(11) NOT NULL;

ALTER TABLE nota_fiscal_novo
ADD COLUMN TIPO_AMBIENTE TINYINT(1) NULL AFTER NOTA_IMPRESSA;

ALTER TABLE nota_fiscal_novo 
CHANGE COLUMN FINALIDADE_EMISSAO FINALIDADE_EMISSAO TINYINT(1) NOT NULL;

ALTER TABLE nota_fiscal_novo 
ADD COLUMN NOTA_FISCAL_CODIGO_NF VARCHAR(45) NULL AFTER TIPO_AMBIENTE;

ALTER TABLE nota_fiscal_novo 
ADD COLUMN NOTA_FISCAL_DV_CHAVE_ACESSO TINYINT(1) NULL AFTER NOTA_FISCAL_CODIGO_NF;

ALTER TABLE nota_fiscal_novo 
ADD COLUMN MODELO_DOCUMENTO_FISCAL VARCHAR(2) NOT NULL AFTER NOTA_FISCAL_DV_CHAVE_ACESSO;

ALTER TABLE nota_fiscal_novo 
ADD COLUMN PROCESSO_EMISSAO VARCHAR(45) NOT NULL AFTER MODELO_DOCUMENTO_FISCAL;

ALTER TABLE nota_fiscal_novo 
ADD COLUMN NOTA_FISCAL_FORMATO_IMPRESSAO VARCHAR(45) NOT NULL AFTER PROCESSO_EMISSAO;

ALTER TABLE nota_fiscal_novo 
CHANGE COLUMN CNAE_EMITENTE CNAE_EMITENTE VARCHAR(20) NULL DEFAULT NULL;

alter table nota_fiscal_novo change IE_DESTINATARIO IE_DESTINATARIO varchar(25) NOT NULL;
alter table nota_fiscal_novo change IE_EMITENTE IE_EMITENTE varchar(25) NOT NULL;

ALTER TABLE nota_fiscal_novo CHANGE COLUMN HORA_SAIDA_ENTRADA HORA_SAIDA_ENTRADA DATETIME NULL DEFAULT NULL;

ALTER TABLE nota_fiscal_novo 
ADD COLUMN LOCAL_DESTINO_OPERACAO TINYINT(1) NOT NULL AFTER NOTA_FISCAL_FORMATO_IMPRESSAO,
ADD COLUMN OPERACAO_CONSUMIDOR_FINAL TINYINT(1) NOT NULL AFTER LOCAL_DESTINO_OPERACAO,
ADD COLUMN PRESENCA_CONSUMIDOR TINYINT(1) NOT NULL AFTER OPERACAO_CONSUMIDOR_FINAL,
ADD COLUMN VERSAO_SISTEMA_EMISSAO VARCHAR(20) NOT NULL AFTER PRESENCA_CONSUMIDOR;

ALTER TABLE nota_fiscal_novo 
CHANGE COLUMN PROCESSO_EMISSAO PROCESSO_EMISSAO TINYINT(1) NOT NULL ,
CHANGE COLUMN NOTA_FISCAL_FORMATO_IMPRESSAO NOTA_FISCAL_FORMATO_IMPRESSAO TINYINT(1) NOT NULL ,
CHANGE COLUMN LOCAL_DESTINO_OPERACAO LOCAL_DESTINO_OPERACAO TINYINT(1) NOT NULL ,
CHANGE COLUMN OPERACAO_CONSUMIDOR_FINAL OPERACAO_CONSUMIDOR_FINAL TINYINT(1) NOT NULL ,
CHANGE COLUMN PRESENCA_CONSUMIDOR PRESENCA_CONSUMIDOR TINYINT(1) NOT NULL ,
CHANGE COLUMN VERSAO_SISTEMA_EMISSAO VERSAO_SISTEMA_EMISSAO VARCHAR(20) NOT NULL;

ALTER TABLE nota_fiscal_novo 
ADD COLUMN NOTA_FISCAL_CODIGO_MUNICIPIO VARCHAR(45) NOT NULL AFTER VERSAO_SISTEMA_EMISSAO;

ALTER TABLE nota_fiscal_novo 
ADD COLUMN NOTA_FISCAL_CODIGO_UF VARCHAR(45) NOT NULL AFTER NOTA_FISCAL_CODIGO_MUNICIPIO;

alter table nota_fiscal_novo add column INFORMACOES_ADICIONAIS varchar(4000) NULL after NOTA_FISCAL_CODIGO_UF;
alter table nota_fiscal_novo change TELEFONE_ID_DESTINATARIO TELEFONE_ID_DESTINATARIO bigint(20) NULL;

alter table nota_fiscal_novo add column NOTA_FISCAL_VALOR_CALCULADO_ID bigint(20) NULL after NOTA_FISCAL_CODIGO_UF;

ALTER TABLE nota_fiscal_novo 
ADD COLUMN USUARIO_ID BIGINT(20) NOT NULL AFTER INFORMACOES_ADICIONAIS,
ADD COLUMN INSERT_TIME TIMESTAMP NOT NULL DEFAULT now() AFTER USUARIO_ID,
ADD COLUMN LAST_UPDATE_TIME TIMESTAMP NOT NULL AFTER INSERT_TIME,
ADD COLUMN COTA_ID BIGINT(20) NULL AFTER PESSOA_DESTINATARIO_ID_REFERENCIA;

DELIMITER $$
DROP TRIGGER IF EXISTS nota_fiscal_novo_BUPD$$
CREATE TRIGGER nota_fiscal_novo_BUPD BEFORE UPDATE ON nota_fiscal_novo FOR EACH ROW
BEGIN  
	SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP;   
END$$
DELIMITER ;

DELIMITER $$
DROP TRIGGER IF EXISTS nota_fiscal_novo_BINS$$
CREATE TRIGGER nota_fiscal_novo_BINS BEFORE INSERT ON nota_fiscal_novo FOR EACH ROW
BEGIN  
	SET NEW.LAST_UPDATE_TIME = CURRENT_TIMESTAMP;   
END$$
DELIMITER ;

alter table nota_fiscal_novo change SERIE SERIE INT NOT NULL;

ALTER TABLE nota_fiscal_novo ADD COLUMN TIPO_DESTINATARIO VARCHAR(45) NOT NULL AFTER ENDERECO_ID_DESTINATARIO;

alter table nota_fiscal_novo change STATUS_PROCESSAMENTO_INTERNO STATUS_PROCESSAMENTO varchar(255) character set utf8 collate utf8_general_ci NULL;

alter table nota_fiscal_novo change STATUS STATUS_RETORNADO varchar(255) character set utf8 collate utf8_general_ci NULL;

-- ==========================================================================

alter table nota_fiscal_entrada drop foreign key  FK90AD5277FEEEECC ;
alter table nota_fiscal_entrada change TIPO_NF_ID NATUREZA_OPERACAO_ID bigint(20) NOT NULL;
alter table nota_fiscal_entrada add constraint FK_nf_entrada_nat_operacao FOREIGN KEY (NATUREZA_OPERACAO_ID) REFERENCES natureza_operacao (ID);
alter table nota_fiscal_entrada change NATUREZA_OPERACAO DESCRICAO_NATUREZA_OPERACAO varchar(255);

-- ==========================================================================

alter table nota_envio add column NOTA_FISCAL_ID tinyint(20) NULL after NOTA_IMPRESSA;

-- ==========================================================================

CREATE TABLE natureza_operacao_nota_envio (
  distribuidor_id BIGINT(20) NOT NULL,
  natureza_operacao_id BIGINT(20) NOT NULL,
  UNIQUE INDEX dist_id_nop_id_UNIQUE (distribuidor_id ASC, natureza_operacao_id ASC)
);

INSERT INTO natureza_operacao_nota_envio (distribuidor_id, natureza_operacao_id) VALUES ('1', '5');
INSERT INTO natureza_operacao_nota_envio (distribuidor_id, natureza_operacao_id) VALUES ('1', '16');
INSERT INTO natureza_operacao_nota_envio (distribuidor_id, natureza_operacao_id) VALUES ('1', '22');

-- ==========================================================================

-- ########### Gerar a Tabela NOTA_FISCAL_FATURA ###########
CREATE TABLE nota_fiscal_fatura (
  ID BIGINT(20) NOT NULL,
  NUMERO VARCHAR(60) NOT NULL,
  VALOR DECIMAL(18,4) NOT NULL,
  VENCIMENTO DATETIME NOT NULL,
  NOTA_FISCAL_NOVO_ID BIGINT(20) NOT NULL,
  PRIMARY KEY (ID),
  INDEX fk_NOTA_FISCAL_FATURA_1_idx (NOTA_FISCAL_NOVO_ID ASC),
  CONSTRAINT fk_NOTA_FISCAL_FATURA_1
    FOREIGN KEY (NOTA_FISCAL_NOVO_ID)
    REFERENCES nota_fiscal_novo (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
-- ==========================================================================

-- ########### Gerar a Tabela NOTA_FISCAL_VALOR_CALCULADO ###########
create table NOTA_FISCAL_VALOR_CALCULADO(
ID bigint(20) NOT NULL AUTO_INCREMENT , 
VALOR_BASE_ICMS decimal(18,4) DEFAULT 0, 
VALOR_ICMS decimal(18,4) DEFAULT 0, 
VALOR_BASE_ICMS_SUBSTITUTO decimal(18,4) DEFAULT 0, 
VALOR_ICMS_SUBSTITUTO decimal(18,4) DEFAULT 0, 
VALOR_PRODUTOS decimal(18,4) DEFAULT 0, 
VALOR_FRETE decimal(18,4) DEFAULT 0, 
VALOR_SEGURO decimal(18,4) DEFAULT 0, 
VALOR_DESCONTO decimal(18,4) DEFAULT 0, 
VALOR_OUTRO decimal(18,4) DEFAULT 0, 
VALOR_IPI decimal(18,4) DEFAULT 0, 
VALOR_NF decimal(18,4) DEFAULT 0, 
ISSQN_TOTAL decimal(18,4) DEFAULT 0, 
ISSQN_BASE decimal(18,4) DEFAULT 0, 
ISSQN_VALOR decimal(18,4) DEFAULT 0, PRIMARY KEY (ID)
);

alter table nota_fiscal_valor_calculado add column VALOR_IMPOSTO_IMPORTACAO decimal(18,4) DEFAULT '0.0000' NULL after VALOR_OUTRO;

alter table nota_fiscal_valor_calculado add column VALOR_PIS decimal(18,4) DEFAULT '0.0000' NULL after VALOR_NF, 
add column VALOR_COFINS decimal(18,4) DEFAULT '0.0000' NULL after VALOR_PIS,
change VALOR_NF VALOR_NF decimal(18,4) default '0.0000' NULL;

-- ==========================================================================

-- ########### Gerar a Tabela REGIME_TRIBUTARIO ###########
create table REGIME_TRIBUTARIO(
ID bigint(20) NOT NULL AUTO_INCREMENT,
CODIGO bigint(20) NOT NULL, 
DESCRICAO varchar(60) NOT NULL, 
ATIVO TINYINT(1) NOT NULL,
PRIMARY KEY (ID)
);

INSERT INTO REGIME_TRIBUTARIO (CODIGO, DESCRICAO, ATIVO) VALUES ('1', 'Lucro Real', true);
INSERT INTO REGIME_TRIBUTARIO (CODIGO, DESCRICAO, ATIVO) VALUES ('2', 'Lucro Presumido', true);
INSERT INTO REGIME_TRIBUTARIO (CODIGO, DESCRICAO, ATIVO) VALUES ('3', 'Simples Nacional', true);

-- ==========================================================================

-- ########### Gerar a Tabela NOTA_FISCAL_TELEFONE ###########
create table NOTA_FISCAL_TELEFONE(
ID bigint(20) NOT NULL AUTO_INCREMENT , 
TIPO_TELEFONE varchar(10) DEFAULT NULL , 
DDI bigint(2) DEFAULT NULL , 
DDD bigint(2) DEFAULT NULL , 
NUMERO bigint(9) DEFAULT NULL ,
PRIMARY KEY (ID));

-- ==========================================================================

create table NOTA_FISCAL_ENDERECO( 
ID bigint(20) NOT NULL NOT NULL AUTO_INCREMENT, 
TIPO_LOGRADOURO varchar(60) , 
LOGRADOURO varchar(60) , 
NUMERO varchar(60) , 
UF varchar(2) , 
CIDADE varchar(60) , 
COMPLEMENTO varchar(60) , 
BAIRRO varchar(60) , 
CEP varchar(60) , 
CODIGO_PAIS varchar(20) , 
PAIS varchar(60) , 
PRIMARY KEY (ID))  ;

-- ==========================================================================

CREATE TABLE nota_fiscal_nota_fiscal_item (
NOTA_FISCAL_ID BIGINT(20) NOT NULL,
notaFiscalItens_ID BIGINT(20) NOT NULL,
PRIMARY KEY (NOTA_FISCAL_ID, notaFiscalItens_ID));

-- ==========================================================================

-- ########### alter table ###########

alter table nota_fiscal_endereco change ID ID bigint(20) NOT NULL AUTO_INCREMENT;

alter table nota_fiscal_endereco add column CODIGO_CIDADE_IBGE bigint(20) NOT NULL after PAIS, change CODIGO_PAIS CODIGO_PAIS varchar(20) character set latin1 collate latin1_swedish_ci NOT NULL;

alter table nota_fiscal_endereco add column CODIGO_UF bigint(20) NOT NULL after CODIGO_CIDADE_IBGE;

-- ==========================================================================

CREATE TABLE parametros_distribuidor_nota_fiscal (
  ID BIGINT(20) NOT NULL AUTO_INCREMENT,
  CHAVE VARCHAR(45) NOT NULL,
  VALOR VARCHAR(4000) NOT NULL,
  DESCRICAO VARCHAR(255) NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE INDEX CHAVE_UNIQUE (CHAVE ASC));

-- ==========================================================================

alter table nota_fiscal_telefone add column RAMAL varchar(10) NULL after NUMERO;
alter table nota_fiscal_telefone change RAMAL RAMAL varchar(5) character set latin1 collate latin1_swedish_ci NULL;
alter table nota_fiscal_telefone change NUMERO NUMERO varchar(10) NULL;

-- ==========================================================================

alter table nota_fiscal_item_nota_fiscal_origem_item add column NOTA_FISCAL_ID bigint(20) NOT NULL after ORIGEM_ITEM_NOTA_FISCAL_ID, drop primary key,  add primary key(NOTA_FISCAL_ITEM_ID, ORIGEM_ITEM_NOTA_FISCAL_ID, NOTA_FISCAL_ID);
alter table nota_fiscal_item_nota_fiscal_origem_item change NOTA_FISCAL_ITEM_ID PRODUTO_SERVICO_SEQUENCIA bigint(20) NOT NULL;

-- ==========================================================================

SET FOREIGN_KEY_CHECKS=0;

alter table nota_fiscal_novo drop foreign key FK933C2B4AB281499A;
alter table nota_fiscal_novo add constraint FK933C2B4AB281499A FOREIGN KEY (PESSOA_EMITENTE_ID_REFERENCIADA) REFERENCES nota_fiscal_pessoa (ID);

alter table nota_fiscal_novo drop foreign key FK933C2B4A8B188640;
alter table nota_fiscal_novo add constraint FK933C2B4A8B188640 FOREIGN KEY (TELEFONE_ID_EMITENTE) REFERENCES nota_fiscal_telefone (ID);

alter table nota_fiscal_novo drop foreign key FK933C2B4A7A98715D;
alter table nota_fiscal_novo add constraint FK933C2B4A7A98715D FOREIGN KEY (ENDERECO_ID_TRANS) REFERENCES nota_fiscal_endereco (ID);

alter table nota_fiscal_novo drop foreign key FK933C2B4A78441BC1;
alter table nota_fiscal_novo add constraint FK933C2B4A78441BC1 FOREIGN KEY (PESSOA_DESTINATARIO_ID_REFERENCIA) REFERENCES nota_fiscal_pessoa (ID);

alter table nota_fiscal_novo drop foreign key FK933C2B4A742B297E;
alter table nota_fiscal_novo add constraint FK933C2B4A742B297E FOREIGN KEY (ENDERECO_ID_EMITENTE) REFERENCES nota_fiscal_endereco (ID);

alter table nota_fiscal_novo drop foreign key FK933C2B4A4C92964;
alter table nota_fiscal_novo add constraint FK933C2B4A4C92964 FOREIGN KEY (TELEFONE_ID_DESTINATARIO) REFERENCES nota_fiscal_telefone (ID);

alter table nota_fiscal_novo drop FOREIGN KEY FK933C2B4AE7D38522;
alter table nota_fiscal_novo add CONSTRAINT FK933C2B4AE7D38522 FOREIGN KEY (ENDERECO_ID_DESTINATARIO) REFERENCES nota_fiscal_endereco (ID);

ALTER TABLE nota_fiscal_novo 
DROP FOREIGN KEY FK933C2B4A5CF4E32B;
ALTER TABLE nota_fiscal_novo 
CHANGE COLUMN TIPO_NOTA_FISCAL_ID NATUREZA_OPERACAO_ID BIGINT(20) NOT NULL ;
ALTER TABLE nota_fiscal_novo 
ADD CONSTRAINT FK933C2B4A5CF4E32B
  FOREIGN KEY (NATUREZA_OPERACAO_ID)
  REFERENCES natureza_operacao (ID);

SET FOREIGN_KEY_CHECKS=1;

ALTER TABLE nota_fiscal_produto_servico 
ADD COLUMN CST VARCHAR(5) NOT NULL AFTER CFOP,
ADD COLUMN VALOR_ALIQUOTA_ICMS DECIMAL(13,2) NOT NULL AFTER PRODUTO_EDICAO_ID,
ADD COLUMN VALOR_ALIQUOTA_IPI DECIMAL(13,2) NOT NULL AFTER VALOR_ALIQUOTA_ICMS,
ADD COLUMN VALOR_FRETE_COMPOE_VALOR_NF TINYINT NOT NULL DEFAULT 0 AFTER VALOR_ALIQUOTA_IPI,
DROP PRIMARY KEY,
ADD PRIMARY KEY (SEQUENCIA, NOTA_FISCAL_ID);

-- ==========================================================================

ALTER TABLE tributo 
RENAME TO tributos ;

ALTER TABLE tributos 
DROP COLUMN ALIQUOTA_ID,
CHANGE COLUMN DESCRICAO DESCRICAO VARCHAR(255) NOT NULL ,
ADD COLUMN TRIBUTO VARCHAR(45) NOT NULL AFTER ID;

ALTER TABLE tributos 
CHANGE COLUMN TRIBUTO nome VARCHAR(45) NOT NULL ,
CHANGE COLUMN DESCRICAO descricao VARCHAR(255) NOT NULL ;

INSERT INTO tributos (nome, DESCRICAO) VALUES ('PIS', 'PIS');
INSERT INTO tributos (nome, DESCRICAO) VALUES ('COFINS', 'COFINS');
INSERT INTO tributos (nome, DESCRICAO) VALUES ('ICMS', 'ICMS');
INSERT INTO tributos (nome, DESCRICAO) VALUES ('IPI', 'IPI');
INSERT INTO tributos (nome, DESCRICAO) VALUES ('SIMPLES', 'SIMPLES');

-- ==========================================================================

ALTER TABLE aliquota 
RENAME TO  tributos_aliquotas;

ALTER TABLE tributos_aliquotas 
CHANGE COLUMN ID ID BIGINT(20) NOT NULL AUTO_INCREMENT,
ADD COLUMN TRIBUTO_ID BIGINT(20) NOT NULL AFTER ID;

ALTER TABLE tributos_aliquotas 
ADD CONSTRAINT fk_aliquota_tributo_1
  FOREIGN KEY (TRIBUTO_ID)
  REFERENCES tributos (ID)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE tributos_aliquotas 
ADD COLUMN tipo_aliquota VARCHAR(45) NOT NULL AFTER TRIBUTO_ID;

CREATE TABLE regime_tributario_tributo_aliquota (
	regime_tributario_id BIGINT(20) NOT NULL,
  	tributo_aliquota_id BIGINT(20) NOT NULL,
	PRIMARY KEY (regime_tributario_id, tributo_aliquota_id)
);

ALTER TABLE regime_tributario_tributo_aliquota 
	ADD CONSTRAINT fk_regime_tributario_tributo_aliquota_1
	  FOREIGN KEY (tributo_aliquota_id)
	  REFERENCES tributos_aliquotas (ID)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT fk_regime_tributario_tributo_aliquota_2
	  FOREIGN KEY (regime_tributario_id)
	  REFERENCES regime_tributario (ID)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;

INSERT INTO tributos_aliquotas (TRIBUTO_ID, tipo_aliquota, VALOR) VALUES (1, 'PERCENTUAL', '1.65');
INSERT INTO tributos_aliquotas (TRIBUTO_ID, tipo_aliquota, VALOR) VALUES (2, 'PERCENTUAL', '7.60');
INSERT INTO tributos_aliquotas (TRIBUTO_ID, tipo_aliquota, VALOR) VALUES (1, 'PERCENTUAL', '0.65');
INSERT INTO tributos_aliquotas (TRIBUTO_ID, tipo_aliquota, VALOR) VALUES (2, 'PERCENTUAL', '3');
INSERT INTO tributos_aliquotas (TRIBUTO_ID, tipo_aliquota, VALOR) VALUES (5, 'PERCENTUAL', '6');
INSERT INTO tributos_aliquotas (TRIBUTO_ID, TIPO_ALIQUOTA, VALOR) VALUES (3, 'PERCENTUAL', '0');

INSERT INTO regime_tributario_tributo_aliquota (regime_tributario_id, tributo_aliquota_id) VALUES (1, 1);
INSERT INTO regime_tributario_tributo_aliquota (regime_tributario_id, tributo_aliquota_id) VALUES (1, 2);
INSERT INTO regime_tributario_tributo_aliquota (regime_tributario_id, tributo_aliquota_id) VALUES (2, 3);
INSERT INTO regime_tributario_tributo_aliquota (regime_tributario_id, tributo_aliquota_id) VALUES (2, 4);
INSERT INTO regime_tributario_tributo_aliquota (regime_tributario_id, tributo_aliquota_id) VALUES (3, 5);

-- ==========================================================================

CREATE TABLE cst (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  imposto VARCHAR(45) NOT NULL,
  codigo VARCHAR(3) NOT NULL,
  tipo_operacao VARCHAR(20) NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE cst_a (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  codigo INT NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  PRIMARY KEY (id));

SET FOREIGN_KEY_CHECKS=0;
ALTER TABLE cst 
CHANGE COLUMN id id BIGINT(20) NOT NULL AUTO_INCREMENT,
CHANGE COLUMN imposto tributo_id BIGINT(20) NOT NULL ;
ALTER TABLE cst 
ADD CONSTRAINT fk_cst_tributo_1
  FOREIGN KEY (tributo_id)
  REFERENCES tributos (ID)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
SET FOREIGN_KEY_CHECKS=1;

ALTER TABLE cst 
ADD UNIQUE INDEX ndx_tributo_cst_unq (tributo_id ASC, codigo ASC);

INSERT INTO cst_a (codigo, descricao) VALUES (0, 'Nacional');
INSERT INTO cst_a (codigo, descricao) VALUES (1, 'Estrangeira - Importação direta');
INSERT INTO cst_a (codigo, descricao) VALUES (2, 'Estrangeira - Adquirida no mercado interno');

INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '00', 'ENTRADA_SAIDA', 'Tributada integralmente');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '10', 'ENTRADA_SAIDA', 'Tributada e com cobrança do ICMS por substituição tributária');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '20', 'ENTRADA_SAIDA', 'Com redução de base de cálculo');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '30', 'ENTRADA_SAIDA', 'Isenta ou não tributada e com cobrança do ICMS por substituição tributária');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '40', 'ENTRADA_SAIDA', 'Isenta');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '41', 'ENTRADA_SAIDA', 'Não Tributada');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '50', 'ENTRADA_SAIDA', 'Suspensão');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '51', 'ENTRADA_SAIDA', 'Diferimento');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '60', 'ENTRADA_SAIDA', 'ICMS cobrado anteriormente por substituição tributária');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '70', 'ENTRADA_SAIDA', 'Com redução de base de cálculo e cobrança do ICMS por substituição tributária');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES (3, '90', 'ENTRADA_SAIDA', 'Outras');

INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '00', 'ENTRADA', 'Entrada com recuperação de crédito');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '01', 'ENTRADA', 'Entrada tributada com alíquota zero');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '02', 'ENTRADA', 'Entrada isenta');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '03', 'ENTRADA', 'Entrada não-tributada');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '04', 'ENTRADA', 'Entrada imune');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '05', 'ENTRADA', 'Entrada com suspensão');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '49', 'ENTRADA', 'Outras entradas');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '50', 'SAIDA', 'Saída tributada');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '51', 'SAIDA', 'Saída tributada com alíquota zero');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '52', 'SAIDA', 'Saída isenta');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '53', 'SAIDA', 'Saída não-tributada');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '54', 'SAIDA', 'Saída imune');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '55', 'SAIDA', 'Saída com suspensão');
INSERT INTO cst (tributo_id, codigo, tipo_operacao, descricao) VALUES ('4', '99', 'SAIDA', 'Outras saídas');

insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','01', 'Operação Tributável com Alíquota Básica');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','02', 'Operação Tributável com Alíquota Diferenciada');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','03', 'Operação Tributável com Alíquota por Unidade de Medida de Produto');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','04', 'Operação Tributável Monofásica – Revenda a Alíquota Zero');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','05', 'Operação Tributável por Substituição Tributária');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','06', 'Operação Tributável a Alíquota Zero');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','07', 'Operação Isenta da Contribuição');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','08', 'Operação sem Incidência da Contribuição');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','09', 'Operação com Suspensão da Contribuição');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'SAIDA','49', 'Outras Operações de Saída');

insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '50', 'Operação com Direito a Crédito – Vinculada Exclusivamente a Receita Tributada no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '51', 'Operação com Direito a Crédito – Vinculada Exclusivamente a Receita Não Tributada no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '52', 'Operação com Direito a Crédito – Vinculada Exclusivamente a Receita de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '53', 'Operação com Direito a Crédito – Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '54', 'Operação com Direito a Crédito – Vinculada a Receitas Tributadas no Mercado Interno e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '55', 'Operação com Direito a Crédito – Vinculada a Receitas Não-Tributadas no Mercado Interno e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '56', 'Operação com Direito a Crédito – Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno, e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '60', 'Crédito Presumido – Operação de Aquisição Vinculada Exclusivamente a Receita Tributada no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '61', 'Crédito Presumido – Operação de Aquisição Vinculada Exclusivamente a Receita Não-Tributada no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '62', 'Crédito Presumido – Operação de Aquisição Vinculada Exclusivamente a Receita de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '63', 'Crédito Presumido – Operação de Aquisição Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '64', 'Crédito Presumido – Operação de Aquisição Vinculada a Receitas Tributadas no Mercado Interno e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '65', 'Crédito Presumido – Operação de Aquisição Vinculada a Receitas Não-Tributadas no Mercado Interno e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '66', 'Crédito Presumido – Operação de Aquisição Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno, e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '67', 'Crédito Presumido – Outras Operações');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '70', 'Operação de Aquisição sem Direito a Crédito');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '71', 'Operação de Aquisição com Isenção');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '72', 'Operação de Aquisição com Suspensão');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '73', 'Operação de Aquisição a Alíquota Zero');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '74', 'Operação de Aquisição sem Incidência da Contribuição');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '75', 'Operação de Aquisição por Substituição Tributária');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '98', 'Outras Operações de Entrada');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('1', 'ENTRADA', '99', 'Outras Operações');

insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','01', 'Operação Tributável com Alíquota Básica');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','02', 'Operação Tributável com Alíquota Diferenciada');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','03', 'Operação Tributável com Alíquota por Unidade de Medida de Produto');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','04', 'Operação Tributável Monofásica – Revenda a Alíquota Zero');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','05', 'Operação Tributável por Substituição Tributária');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','06', 'Operação Tributável a Alíquota Zero');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','07', 'Operação Isenta da Contribuição');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','08', 'Operação sem Incidência da Contribuição');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','09', 'Operação com Suspensão da Contribuição');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'SAIDA','49', 'Outras Operações de Saída');

insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '50', 'Operação com Direito a Crédito – Vinculada Exclusivamente a Receita Tributada no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '51', 'Operação com Direito a Crédito – Vinculada Exclusivamente a Receita Não Tributada no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '52', 'Operação com Direito a Crédito – Vinculada Exclusivamente a Receita de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '53', 'Operação com Direito a Crédito – Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '54', 'Operação com Direito a Crédito – Vinculada a Receitas Tributadas no Mercado Interno e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '55', 'Operação com Direito a Crédito – Vinculada a Receitas Não-Tributadas no Mercado Interno e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '56', 'Operação com Direito a Crédito – Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno, e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '60', 'Crédito Presumido – Operação de Aquisição Vinculada Exclusivamente a Receita Tributada no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '61', 'Crédito Presumido – Operação de Aquisição Vinculada Exclusivamente a Receita Não-Tributada no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '62', 'Crédito Presumido – Operação de Aquisição Vinculada Exclusivamente a Receita de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '63', 'Crédito Presumido – Operação de Aquisição Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '64', 'Crédito Presumido – Operação de Aquisição Vinculada a Receitas Tributadas no Mercado Interno e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '65', 'Crédito Presumido – Operação de Aquisição Vinculada a Receitas Não-Tributadas no Mercado Interno e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '66', 'Crédito Presumido – Operação de Aquisição Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno, e de Exportação');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '67', 'Crédito Presumido – Outras Operações');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '70', 'Operação de Aquisição sem Direito a Crédito');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '71', 'Operação de Aquisição com Isenção');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '72', 'Operação de Aquisição com Suspensão');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '73', 'Operação de Aquisição a Alíquota Zero');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '74', 'Operação de Aquisição sem Incidência da Contribuição');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '75', 'Operação de Aquisição por Substituição Tributária');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '98', 'Outras Operações de Entrada');
insert into cst (tributo_id, tipo_operacao, codigo, descricao) values('2', 'ENTRADA', '99', 'Outras Operações');

-- ==========================================================================

CREATE TABLE tributacao (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  tributo VARCHAR(45) NOT NULL,
  cst_a BIGINT(20) NOT NULL,
  cst BIGINT(20) NOT NULL,
  tipo_operacao VARCHAR(45) NOT NULL,
  base_calculo DECIMAL(13,4) NOT NULL,
  valor_aliquota DECIMAL(13,4) NOT NULL,
  isento_nao_tributado TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE tipo_produto_tributacao (
  tributacao_id BIGINT(20) NOT NULL,
  tipo_produto_id BIGINT(20) NOT NULL,
  PRIMARY KEY (tributacao_id, tipo_produto_id),
  INDEX fk_tipo_produto_tributacao_2_idx (tipo_produto_id ASC),
  CONSTRAINT fk_tipo_produto_tributacao_1
    FOREIGN KEY (tributacao_id)
    REFERENCES tributacao (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_tipo_produto_tributacao_2
    FOREIGN KEY (tipo_produto_id)
    REFERENCES tipo_produto (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('ICMS', '0', '41', 'ENTRADA_SAIDA', '0', '0', '1');
INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('IPI', '0', '49', 'ENTRADA', '0', '0', '1');
INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('IPI', '0', '99', 'SAIDA', '0', '0', '1');
INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('PIS', '0', '70', 'ENTRADA', '0', '0', '0');
INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('PIS', '0', '01', 'SAIDA', '0', '0', '0');
INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('COFINS', '0', '70', 'ENTRADA', '0', '0', '0');
INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('COFINS', '0', '01', 'SAIDA', '0', '0', '0');

INSERT INTO tipo_produto_tributacao
SELECT t.id tributacao_id, tp.id AS tipo_produto_id
FROM tipo_produto tp, tributacao t;

INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('PIS', '0', '74', 'ENTRADA', '0', '0', '1');
INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('PIS', '0', '07', 'SAIDA', '0', '0', '1');
INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('COFINS', '0', '74', 'ENTRADA', '0', '0', '1');
INSERT INTO tributacao (tributo, cst_a, cst, tipo_operacao, base_calculo, valor_aliquota, isento_nao_tributado) VALUES ('COFINS', '0', '07', 'SAIDA', '0', '0', '1');

update tipo_produto_tributacao tpt
set tributacao_id = (select id from tributacao t where tributo = 'PIS' and t.isento_nao_tributado = true and tipo_operacao = 'ENTRADA')
where tributacao_id = (select id from tributacao t where tributo = 'PIS' and t.isento_nao_tributado = false and tipo_operacao = 'ENTRADA')
and tpt.tipo_produto_id = (select id from tipo_produto tp where grupo_produto = 'LIVRO');

update tipo_produto_tributacao tpt
set tributacao_id = (select id from tributacao t where tributo = 'PIS' and t.isento_nao_tributado = true and tipo_operacao = 'SAIDA')
where tributacao_id = (select id from tributacao t where tributo = 'PIS' and t.isento_nao_tributado = false and tipo_operacao = 'SAIDA')
and tpt.tipo_produto_id = (select id from tipo_produto tp where grupo_produto = 'LIVRO');

update tipo_produto_tributacao tpt
set tributacao_id = (select id from tributacao t where tributo = 'COFINS' and t.isento_nao_tributado = true and tipo_operacao = 'ENTRADA')
where tributacao_id = (select id from tributacao t where tributo = 'COFINS' and t.isento_nao_tributado = false and tipo_operacao = 'ENTRADA')
and tpt.tipo_produto_id = (select id from tipo_produto tp where grupo_produto = 'LIVRO');

update tipo_produto_tributacao tpt
set tributacao_id = (select id from tributacao t where tributo = 'COFINS' and t.isento_nao_tributado = true and tipo_operacao = 'SAIDA')
where tributacao_id = (select id from tributacao t where tributo = 'COFINS' and t.isento_nao_tributado = false and tipo_operacao = 'SAIDA')
and tpt.tipo_produto_id = (select id from tipo_produto tp where grupo_produto = 'LIVRO');

-- ==========================================================================

alter table historico_titularidade_cota add column CONTRIBUINTE_ICMS tinyint(1) NULL after EMITE_NFE;

alter table cota add column CONTRIBUINTE_ICMS tinyint(1) NULL after EMITE_NF_E;

alter table cota change EMITE_NF_E EXIGE_NF_E tinyint(1) default '0' NULL, change CONTRIBUINTE_ICMS CONTRIBUINTE_ICMS tinyint(1) default '0' NULL;

ALTER TABLE historico_titularidade_cota CHANGE COLUMN EMITE_NFE EXIGE_NF_E TINYINT(1) NULL DEFAULT NULL;

UPDATE cota 
SET CONTRIBUINTE_ICMS = CASE WHEN CONTRIBUINTE_ICMS IS NULL THEN 0 ELSE CONTRIBUINTE_ICMS END,
EXIGE_NF_E = CASE WHEN EXIGE_NF_E IS NULL THEN 0 ELSE EXIGE_NF_E END;

UPDATE historico_titularidade_cota set CONTRIBUINTE_ICMS = 0;

update cota c inner join pessoa p on p.id = c.PESSOA_ID set c.CONTRIBUINTE_ICMS = false where p.TIPO = 'F' and c.CONTRIBUINTE_ICMS = true;

-- ==========================================================================

ALTER TABLE distribuidor
ADD COLUMN CODIGO_ESTABELECIMENTO_EMISSOR varchar(100) NULL,
ADD COLUMN CNPJ_ESTABELECIMENTO_EMISSOR varchar(100) NULL,
ADD COLUMN CODIGO_LOCAL varchar(100) NULL,
ADD COLUMN CODIGO_CENTRO_EMISSOR varchar(100) NULL;

INSERT INTO parametro_sistema (TIPO_PARAMETRO_SISTEMA, VALOR) VALUES
('PATH_INTERFACE_NFE_EXPORTACAO_FTF', '/opt/parametros_nds/ftf/saida'),
('PATH_INTERFACE_NFE_IMPORTACAO_FTF', '/opt/parametros_nds/ftf/entrada'),
('FTF_INDEX_FILENAME', 1),
('FTF_CODIGO_ESTABELECIMENTO_EMISSOR', 1),
('FTF_CNPJ_ESTABELECIMENTO_EMISSOR', '09.516.147/0001-03'),
('FTF_CODIGO_LOCAL', 1),
('FTF_CODIGO_CENTRO_EMISSOR', 1),
('NFE_LIMITAR_QTDE_ITENS', 900)
;

UPDATE parametro_sistema SET VALOR = '/opt/parametros_nds/notas/importado' WHERE TIPO_PARAMETRO_SISTEMA = 'PATH_INTERFACE_NFE_IMPORTACAO';
UPDATE parametro_sistema SET VALOR = '/opt/parametros_nds/notas/exportado' WHERE TIPO_PARAMETRO_SISTEMA = 'PATH_INTERFACE_NFE_EXPORTACAO';

-- ==========================================================================

alter table chamada_encalhe_cota add column PROCESSO_UTILIZA_NFE tinyint(1) NOT NULL DEFAULT '0' after QTDE_PREVISTA; 

alter table controle_conferencia_encalhe_cota add column PROCESSO_UTILIZA_NFE tinyint(1) NOT NULL DEFAULT '0'; 
alter table controle_conferencia_encalhe_cota add column NFE_DIGITADA tinyint(1) NOT NULL DEFAULT '0'; 

alter table conferencia_encalhe add column PRECO_COM_DESCONTO decimal(18,4) NULL;

alter table conferencia_encalhe_backup add column `PROCESSO_UTILIZA_NFE` tinyint(1) DEFAULT '0' NOT NULL;

-- ==========================================================================

create table processo ( 
	ID bigint(20) NOT NULL AUTO_INCREMENT , NOME varchar(100) NOT NULL , DESCRICAO varchar(255) , PRIMARY KEY (ID)
);

insert into processo (NOME,DESCRICAO) values ('ENVIO', 'Envio de repartes');
insert into processo (NOME,DESCRICAO) values ('VENDA', 'Venda');
insert into processo (NOME,DESCRICAO) values ('ENTRADA_ENCALHE', 'Entrada do encalhe');
insert into processo (NOME,DESCRICAO) values ('FALTA_REPARTE', 'Faltas do reparte');
insert into processo (NOME,DESCRICAO) values ('FALTA_ENCALHE', 'Faltas do encalhe');
insert into processo (NOME,DESCRICAO) values ('SOBRA_ENCALHE', 'Sobras do encalhe');
insert into processo (NOME,DESCRICAO) values ('CANCELADA', 'Cancelada');
insert into processo (NOME,DESCRICAO) values ('GERACAO_NF_E', 'Geração NF-e');
insert into processo (NOME,DESCRICAO) values ('LANCAMENTO_FALTA_SOBRA','Lançamento faltas e sobra');
insert into processo (NOME,DESCRICAO) values ('VENDA_SUPLEMENTAR', 'Venda de Suplementar');
insert into processo (NOME,DESCRICAO) values ('FECHAMENTO_ENCALHE', 'Fechamento de Encalhe');
insert into processo (NOME,DESCRICAO) values ('DEVOLUCAO_AO_FORNECEDOR', 'Devolução ao Fornecedor');
insert into processo (NOME,DESCRICAO) values ('DEVOLUCAO_ENCALHE', 'Nota da Devolução do Encalhe');
insert into processo (NOME,DESCRICAO) values ('CONSIGNACAO_REPARTE_NORMAL', 'Consignação do Reparte Normal');
insert into processo (NOME,DESCRICAO) values ('NOTA_LANCAMENTO', 'Nota de Lançamento (NE/NECA)');

-- ==========================================================================

CREATE TABLE movimento_fechamento_fiscal_origem_item (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  TIPO VARCHAR(45) NOT NULL,
  ORIGEM VARCHAR(45) NOT NULL,
  MOVIMENTO_FECHAMENTO_FISCAL_ID BIGINT(20) NOT NULL,  
  MOVIMENTO_ID BIGINT(20) NOT NULL DEFAULT -1,
  PRIMARY KEY (ID),
  UNIQUE INDEX movimento_fechamento_estoque_unique (MOVIMENTO_FECHAMENTO_FISCAL_ID ASC, MOVIMENTO_ID ASC, TIPO ASC)
);

CREATE TABLE movimento_fechamento_fiscal_cota (
  ID BIGINT(20) NOT NULL AUTO_INCREMENT,
  TIPO_MOVIMENTO_ID BIGINT(20) NOT NULL,
  DATA DATE NOT NULL,
  DATA_CRIACAO TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  NOTA_FISCAL_DEVOLUCAO_SIMBOLICA_EMITIDA TINYINT(1) NOT NULL DEFAULT false,
  DESOBRIGA_NOTA_FISCAL_DEVOLUCAO_SIMBOLICA TINYINT(1) NOT NULL DEFAULT 0,
  NOTA_FISCAL_VENDA_EMITIDA TINYINT(1) NOT NULL DEFAULT false,
  DESOBRIGA_NOTA_FISCAL_VENDA TINYINT(1) NOT NULL DEFAULT 0,
  NOTA_FISCAL_LIBERADA_EMISSAO VARCHAR(45) NOT NULL DEFAULT '0',
  QTDE DECIMAL(18,4) NOT NULL,
  TIPO_DESTINATARIO VARCHAR(45) NOT NULL,
  COTA_ID BIGINT(20) NOT NULL,
  PRODUTO_EDICAO_ID BIGINT(20) NOT NULL,
  CHAMADA_ENCALHE_COTA_ID BIGINT(20) NULL,
  PRECO_COM_DESCONTO DECIMAL(18,4) NULL,
  PRECO_VENDA DECIMAL(18,4) NULL,
  VALOR_DESCONTO DECIMAL(18,4) NULL,
  PRIMARY KEY (ID),
  INDEX fk_MOVIMENTO_FECHAMENTO_FISCAL_COTA_1_idx (COTA_ID ASC),
  INDEX fk_movimento_fechamento_fiscal_cota_2_idx (CHAMADA_ENCALHE_COTA_ID ASC),
  CONSTRAINT fk_MOVIMENTO_FECHAMENTO_FISCAL_COTA_1
    FOREIGN KEY (COTA_ID)
    REFERENCES cota (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_movimento_fechamento_fiscal_cota_2
    FOREIGN KEY (CHAMADA_ENCALHE_COTA_ID)
    REFERENCES chamada_encalhe_cota (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_movimento_fechamento_fiscal_tipo_movimento_1
    FOREIGN KEY (TIPO_MOVIMENTO_ID)
    REFERENCES tipo_movimento (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE movimento_fechamento_fiscal_fornecedor (
  ID BIGINT(20) NOT NULL,
  DATA DATE NOT NULL,
  DATA_CRIACAO TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  DESOBRIGA_NOTA_FISCAL_DEVOLUCAO_SIMBOLICA TINYINT(1) NOT NULL DEFAULT 0,
  NOTA_FISCAL_DEVOLUCAO_SIMBOLICA_EMITIDA TINYINT(1) NOT NULL DEFAULT 0,
  NOTA_FISCAL_LIBERADA_EMISSAO TINYINT(1) NOT NULL DEFAULT 0,
  QTDE DECIMAL(18,4) NOT NULL,
  TIPO_DESTINATARIO VARCHAR(45) NOT NULL,
  TIPO_MOVIMENTO_ID BIGINT(20) NOT NULL,
  FORNECEDOR_ID BIGINT(20) NOT NULL,
  PRODUTO_EDICAO_ID BIGINT(20) NOT NULL,
  PRECO_COM_DESCONTO DECIMAL(18,4) NULL,
  PRECO_VENDA DECIMAL(18,4) NULL,
  VALOR_DESCONTO DECIMAL(18,4) NULL,
  PRIMARY KEY (ID),
  INDEX fk_MOVIMENTO_FECHAMENTO_FISCAL_FORNECEDOR_1_idx (PRODUTO_EDICAO_ID ASC),
  INDEX fk_MOV_FECH_FISCAL_FORN_FORNECEDOR_1_idx (FORNECEDOR_ID ASC),
  INDEX fk_MOV_FECH_FISCAL_FORN_TIPO_MOVIMENTO_1_idx (TIPO_MOVIMENTO_ID ASC),
  CONSTRAINT fk_MOV_FECH_FISCAL_FORN_PROD_EDICAO_1
    FOREIGN KEY (PRODUTO_EDICAO_ID)
    REFERENCES produto_edicao (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_MOV_FECH_FISCAL_FORN_FORNECEDOR_1
    FOREIGN KEY (FORNECEDOR_ID)
    REFERENCES fornecedor (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_MOV_FECH_FISCAL_FORN_TIPO_MOVIMENTO_1
    FOREIGN KEY (TIPO_MOVIMENTO_ID)
    REFERENCES tipo_movimento (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- ==========================================================================

CREATE TABLE PARAMETROS_FTF_GERACAO (
	  ID BIGINT(20) NOT NULL AUTO_INCREMENT
	, NOME VARCHAR(50) NOT NULL
	, CODIGO_CENTRO_EMISSOR VARCHAR(50) NOT NULL
	, CODIGO_ESTABELECIMENTO_EMISSOR_GFF VARCHAR(50) NOT NULL
	, CNPJ_EMISSOR VARCHAR(50) NOT NULL
	, CNPJ_DESTINATARIO VARCHAR(50) NOT NULL
	, ESTABELECIMENTO VARCHAR(50) NOT NULL
	, TIPO_PEDIDO VARCHAR(50) NOT NULL
	, CODIGO_SOLICITANTE VARCHAR(50) NOT NULL
	, CODIGO_UNIDADE_OPERACIONAL VARCHAR(50) NOT NULL
	, MENSAGEM VARCHAR(200) NOT NULL
	, CODIGO_NATUREZA_OPERACAO_FTF VARCHAR(45) NOT NULL
	, NATUREZA_OPERACAO_ID BIGINT(20) NOT NULL
	, CFOP_ID BIGINT(20) NOT NULL
	, PRIMARY KEY(ID)
	, CONSTRAINT fk_parametros_ftf_geracao_cfop_1
	  FOREIGN KEY (CFOP_ID)
	  REFERENCES cfop (ID)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION
	, CONSTRAINT fk_parametros_ftf_geracao_nat_op_1
	  FOREIGN KEY (NATUREZA_OPERACAO_ID)
	  REFERENCES natureza_operacao (ID)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION
);

-- ==========================================================================

insert into parametros_ftf_geracao 
	(NOME, CODIGO_CENTRO_EMISSOR, CODIGO_ESTABELECIMENTO_EMISSOR_GFF, CNPJ_EMISSOR, CNPJ_DESTINATARIO, ESTABELECIMENTO, TIPO_PEDIDO, CODIGO_SOLICITANTE, CODIGO_UNIDADE_OPERACIONAL, MENSAGEM, CODIGO_NATUREZA_OPERACAO_FTF, NATUREZA_OPERACAO_ID, CFOP_ID)
values
	('Remessa Treelog Santos x Jornaleiros', '008', '07', '61438248004625', 'FTFTREELOG0046 (CRP 5826042)', 'TREELOG_046', '2', 'SIST_NDI', '', 'Procedimento Autorizado por Regime Especial – Processo UA 51257-371143/2009', '957', 22, 9);

-- ==========================================================================

insert into parametros_ftf_geracao 
	(NOME, CODIGO_CENTRO_EMISSOR, CODIGO_ESTABELECIMENTO_EMISSOR_GFF, CNPJ_EMISSOR, CNPJ_DESTINATARIO, ESTABELECIMENTO, TIPO_PEDIDO, CODIGO_SOLICITANTE, CODIGO_UNIDADE_OPERACIONAL, MENSAGEM, CODIGO_NATUREZA_OPERACAO_FTF, NATUREZA_OPERACAO_ID, CFOP_ID)
values
	('Devolução globalizada Treelog Santos x Jornaleiros', '008', '07', '61438248004625', 'FTFTREELOG0046 (CRP 5826042)', 'TREELOG_046', '5', 'SIST_NDI', '', 'Procedimento Autorizado por Regime Especial – Processo UA 51257-371143/2009', '1355', 26, 11);

-- ==========================================================================

insert into parametros_ftf_geracao 
	(NOME, CODIGO_CENTRO_EMISSOR, CODIGO_ESTABELECIMENTO_EMISSOR_GFF, CNPJ_EMISSOR, CNPJ_DESTINATARIO, ESTABELECIMENTO, TIPO_PEDIDO, CODIGO_SOLICITANTE, CODIGO_UNIDADE_OPERACIONAL, MENSAGEM, CODIGO_NATUREZA_OPERACAO_FTF, NATUREZA_OPERACAO_ID, CFOP_ID)
values
	('Venda globalizada Treelog x Jornaleiros', '023', '07', '61438248000123', 'FTFTREELOG0001 (CRP 5826077)', 'DINAP09', '2', 'SIST_NDI', 'FISC_DINAP', '', '288', 28, 13);

-- ==========================================================================

insert into parametros_ftf_geracao 
	(NOME, CODIGO_CENTRO_EMISSOR, CODIGO_ESTABELECIMENTO_EMISSOR_GFF, CNPJ_EMISSOR, CNPJ_DESTINATARIO, ESTABELECIMENTO, TIPO_PEDIDO, CODIGO_SOLICITANTE, CODIGO_UNIDADE_OPERACIONAL, MENSAGEM, CODIGO_NATUREZA_OPERACAO_FTF, NATUREZA_OPERACAO_ID, CFOP_ID)
values
	('Remessa Treelog Santos x Jornaleiros', '008', '07', '61438248004625', '', 'TREELOG_046', '2', 'SIST_NDI', '', 'Procedimento Autorizado por Regime Especial – Processo UA 51257-371143/2009', '957', 22, 9);

-- ==========================================================================

insert into parametros_ftf_geracao 
	(NOME, CODIGO_CENTRO_EMISSOR, CODIGO_ESTABELECIMENTO_EMISSOR_GFF, CNPJ_EMISSOR, CNPJ_DESTINATARIO, ESTABELECIMENTO, TIPO_PEDIDO, CODIGO_SOLICITANTE, CODIGO_UNIDADE_OPERACIONAL, MENSAGEM, CODIGO_NATUREZA_OPERACAO_FTF, NATUREZA_OPERACAO_ID, CFOP_ID)
values
	('Devolução globalizada Treelog Santos x Jornaleiros', '008', '07', '61438248004625', '', 'TREELOG_046', '5', 'SIST_NDI', '', 'Procedimento Autorizado por Regime Especial – Processo UA 51257-371143/2009', '1355', 26, 11);

-- ==========================================================================

insert into parametros_ftf_geracao 
	(NOME, CODIGO_CENTRO_EMISSOR, CODIGO_ESTABELECIMENTO_EMISSOR_GFF, CNPJ_EMISSOR, CNPJ_DESTINATARIO, ESTABELECIMENTO, TIPO_PEDIDO, CODIGO_SOLICITANTE, CODIGO_UNIDADE_OPERACIONAL, MENSAGEM, CODIGO_NATUREZA_OPERACAO_FTF, NATUREZA_OPERACAO_ID, CFOP_ID)
values
	('Venda globalizada Treelog x Jornaleiros', '023', '07', '61438248000123', '', 'DINAP09', '2', 'SIST_NDI', 'FISC_DINAP', '', '288', 28, 13);
	
-- ==========================================================================	

CREATE TABLE flag_pendente_ativacao (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  flag VARCHAR(45) NOT NULL,
  descricao VARCHAR(45) NOT NULL,
  valor TINYINT(1) NOT NULL,
  tipo VARCHAR(45) NOT NULL,
  id_alterado BIGINT(20) NOT NULL,
  data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE INDEX NOME_TIPO_ID_ITEM_UQ (flag ASC, tipo ASC, id_alterado ASC),
  PRIMARY KEY (id));

-- ==========================================================================

alter table nota_fiscal_referenciada change CHAVE_ACESSO CHAVE_ACESSO varchar(44) NOT NULL, change CHAVE_ACESSO_CTE CHAVE_ACESSO_CTE varchar(44) NOT NULL;
alter table nota_fiscal_referenciada change CODIGO_UF CODIGO_UF bigint(11) NOT NULL, change MODELO_DOCUMENTO_FISCAL MODELO_DOCUMENTO_FISCAL varchar(11) NOT NULL, change NUMERO_DOCUMENTO_FISCAL NUMERO_DOCUMENTO_FISCAL varchar(20) NOT NULL, change SERIE SERIE varchar(11) NOT NULL;

update grupo_permissao_permissao set PERMISSAO_ID = 'ROLE_ADMINISTRACAO_NATUREZA_OPERACAO' where PERMISSAO_ID = 'ROLE_ADMINISTRACAO_TIPO_NOTA';
update grupo_permissao_permissao set PERMISSAO_ID = 'ROLE_ADMINISTRACAO_NATUREZA_OPERACAO_ALTERACAO' where PERMISSAO_ID = 'ROLE_ADMINISTRACAO_TIPO_NOTA_ALTERACAO';

-- ==========================================================================

alter table movimento_estoque_cota add index NDX_MEC_COTA_FURO_STATUSFINANC_1 (STATUS_ESTOQUE_FINANCEIRO, COTA_ID, MOVIMENTO_ESTOQUE_COTA_FURO_ID);

-- ==========================================================================

alter table nota_fiscal_produto_servico add column VALOR_COMPOE_VALOR_NF tinyint(4) DEFAULT '1' NOT NULL after VALOR_ALIQUOTA_IPI;


alter table distribuidor add column CNAE varchar(10) NULL after ACEITA_RECOLHIMENTO_PARCIAL_ATRASO;

update `natureza_operacao` set `ID`='5',`GERAR_COTA_CONTRIBUINTE_ICMS`='1' where `ID`='5';