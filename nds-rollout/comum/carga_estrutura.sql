-- MySQL dump 10.13  Distrib 5.6.20, for Linux (x86_64)
--
-- Host: rds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com    Database: db_09795816
-- ------------------------------------------------------
-- Server version	5.5.27-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acesar`
--

DROP TABLE IF EXISTS `acesar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acesar` (
  `id` int(11) NOT NULL,
  `COD_AGENTE_LANP` varchar(7) DEFAULT NULL,
  `COD_PRODUTO_LANP` varchar(14) DEFAULT NULL,
  `COD_PRODIN` varchar(8) DEFAULT NULL,
  `NUM_EDICAO` int(11) DEFAULT NULL,
  `DATA_PREVISTA_LANCAMENTO_LANP` varchar(10) DEFAULT NULL,
  `DATA_REAL_LANCAMENTO_LANP` varchar(10) DEFAULT NULL,
  `TIPO_STATUS_LANCTO_LANP` varchar(3) DEFAULT NULL,
  `TIPO_LANCAMENTO_LANP` varchar(3) DEFAULT NULL,
  `VLR_PRECO_REAL_LANP` varchar(10) DEFAULT NULL,
  `COD_PRODUTO_RCPR` varchar(12) DEFAULT NULL,
  `NUM_RECOLTO_RCPR` int(11) DEFAULT NULL,
  `DATA_PREVISTA_RECOLTO_RCPR` varchar(10) DEFAULT NULL,
  `DATA_REAL_RECOLTO_RCPR` varchar(10) DEFAULT NULL,
  `TIPO_STATUS_RECOLTO_RCPR` varchar(45) DEFAULT NULL,
  `produto_edicao_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acesar2`
--

DROP TABLE IF EXISTS `acesar2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acesar2` (
  `id` int(11) NOT NULL,
  `COD_AGENTE_LANP` varchar(7) DEFAULT NULL,
  `COD_PRODUTO_LANP` varchar(14) DEFAULT NULL,
  `COD_PRODIN` varchar(8) DEFAULT NULL,
  `NUM_EDICAO` int(11) DEFAULT NULL,
  `DATA_PREVISTA_LANCAMENTO_LANP` varchar(10) DEFAULT NULL,
  `DATA_REAL_LANCAMENTO_LANP` varchar(10) DEFAULT NULL,
  `TIPO_STATUS_LANCTO_LANP` varchar(3) DEFAULT NULL,
  `TIPO_LANCAMENTO_LANP` varchar(3) DEFAULT NULL,
  `VLR_PRECO_REAL_LANP` varchar(10) DEFAULT NULL,
  `COD_PRODUTO_RCPR` varchar(12) DEFAULT NULL,
  `NUM_RECOLTO_RCPR` int(11) DEFAULT NULL,
  `DATA_PREVISTA_RECOLTO_RCPR` varchar(10) DEFAULT NULL,
  `DATA_REAL_RECOLTO_RCPR` varchar(10) DEFAULT NULL,
  `TIPO_STATUS_RECOLTO_RCPR` varchar(45) DEFAULT NULL,
  `produto_edicao_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aclassificacao`
--

DROP TABLE IF EXISTS `aclassificacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aclassificacao` (
  `codigo` varchar(8) NOT NULL,
  `edicao` int(11) NOT NULL,
  `classificacao` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acumulo_divida`
--

DROP TABLE IF EXISTS `acumulo_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acumulo_divida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DIVIDA_ID` bigint(20) NOT NULL,
  `MOV_PENDENTE_ID` bigint(20) NOT NULL,
  `MOV_JUROS_ID` bigint(20) DEFAULT NULL,
  `MOV_MULTA_ID` bigint(20) DEFAULT NULL,
  `NUMERO_ACUMULO` int(11) NOT NULL DEFAULT '1',
  `DATA_CRIACAO` date NOT NULL,
  `STATUS` varchar(255) NOT NULL DEFAULT 'ATIVA',
  `USUARIO_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `DIVIDA_ID` (`DIVIDA_ID`),
  KEY `MOV_PENDENTE_ID` (`MOV_PENDENTE_ID`),
  KEY `MOV_JUROS_ID` (`MOV_JUROS_ID`),
  KEY `MOV_MULTA_ID` (`MOV_MULTA_ID`),
  KEY `USUARIO_ID` (`USUARIO_ID`),
  KEY `COTA_ID` (`COTA_ID`),
  CONSTRAINT `acumulo_divida_ibfk_1` FOREIGN KEY (`DIVIDA_ID`) REFERENCES `divida` (`ID`),
  CONSTRAINT `acumulo_divida_ibfk_2` FOREIGN KEY (`MOV_PENDENTE_ID`) REFERENCES `movimento_financeiro_cota` (`ID`),
  CONSTRAINT `acumulo_divida_ibfk_3` FOREIGN KEY (`MOV_JUROS_ID`) REFERENCES `movimento_financeiro_cota` (`ID`),
  CONSTRAINT `acumulo_divida_ibfk_4` FOREIGN KEY (`MOV_MULTA_ID`) REFERENCES `movimento_financeiro_cota` (`ID`),
  CONSTRAINT `acumulo_divida_ibfk_5` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `acumulo_divida_ibfk_6` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ajuste_reparte`
--

DROP TABLE IF EXISTS `ajuste_reparte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ajuste_reparte` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AJUSTE_APLICADO` decimal(18,4) DEFAULT NULL,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `DATA_FIM` datetime DEFAULT NULL,
  `DATA_INICIO` datetime DEFAULT NULL,
  `FORMA_AJUSTE` varchar(255) DEFAULT NULL,
  `MOTIVO` varchar(255) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `TIPO_SEGMENTO_AJUSTE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK367020587FFF790E` (`USUARIO_ID`),
  KEY `FK36702058C8181F74` (`COTA_ID`),
  KEY `FK36702058BA1ECD8` (`TIPO_SEGMENTO_AJUSTE_ID`),
  CONSTRAINT `FK367020587FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK36702058BA1ECD8` FOREIGN KEY (`TIPO_SEGMENTO_AJUSTE_ID`) REFERENCES `tipo_segmento_produto` (`ID`),
  CONSTRAINT `FK36702058C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `algoritmo`
--

DROP TABLE IF EXISTS `algoritmo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `algoritmo` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRICAO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `area_influencia_pdv`
--

DROP TABLE IF EXISTS `area_influencia_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area_influencia_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asegmento`
--

DROP TABLE IF EXISTS `asegmento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asegmento` (
  `codigo` varchar(8) NOT NULL,
  `segmento` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `assoc_veiculo_motorista_rota`
--

DROP TABLE IF EXISTS `assoc_veiculo_motorista_rota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assoc_veiculo_motorista_rota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MOTORISTA` bigint(20) DEFAULT NULL,
  `ROTA` bigint(20) DEFAULT NULL,
  `TRANSPORTADOR_ID` bigint(20) DEFAULT NULL,
  `VEICULO` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK45A0E4B7D90B1440` (`TRANSPORTADOR_ID`),
  KEY `FK45A0E4B7B2639DFE` (`MOTORISTA`),
  KEY `FK45A0E4B78F516B2E` (`ROTA`),
  KEY `FK45A0E4B797A7E184` (`VEICULO`),
  CONSTRAINT `FK45A0E4B78F516B2E` FOREIGN KEY (`ROTA`) REFERENCES `rota` (`ID`),
  CONSTRAINT `FK45A0E4B797A7E184` FOREIGN KEY (`VEICULO`) REFERENCES `veiculo` (`ID`),
  CONSTRAINT `FK45A0E4B7B2639DFE` FOREIGN KEY (`MOTORISTA`) REFERENCES `motorista` (`ID`),
  CONSTRAINT `FK45A0E4B7D90B1440` FOREIGN KEY (`TRANSPORTADOR_ID`) REFERENCES `transportador` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atualizacao_estoque_gfs`
--

DROP TABLE IF EXISTS `atualizacao_estoque_gfs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atualizacao_estoque_gfs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ATUALIZACAO` date NOT NULL,
  `DIFERENCA_ID` bigint(20) NOT NULL,
  `MOVIMENTO_ESTOQUE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `MOVIMENTO_ESTOQUE_ID` (`MOVIMENTO_ESTOQUE_ID`),
  UNIQUE KEY `DIFERENCA_ID` (`DIFERENCA_ID`),
  KEY `FK66A4BF1C232FFB12` (`MOVIMENTO_ESTOQUE_ID`),
  KEY `FK66A4BF1C5A0FFAA9` (`DIFERENCA_ID`),
  CONSTRAINT `FK66A4BF1C232FFB12` FOREIGN KEY (`MOVIMENTO_ESTOQUE_ID`) REFERENCES `movimento_estoque` (`ID`),
  CONSTRAINT `FK66A4BF1C5A0FFAA9` FOREIGN KEY (`DIFERENCA_ID`) REFERENCES `diferenca` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `baixa_cobranca`
--

DROP TABLE IF EXISTS `baixa_cobranca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `baixa_cobranca` (
  `TIPO_BAIXA` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_BAIXA` date NOT NULL,
  `DATA_PAGAMENTO` date NOT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `VALOR_PAGO` decimal(18,4) DEFAULT NULL,
  `NOME_ARQUIVO` varchar(255) DEFAULT NULL,
  `NOSSO_NUMERO` varchar(255) DEFAULT NULL,
  `NUM_REGISTRO_ARQUIVO` int(11) DEFAULT NULL,
  `OBSERVACAO` varchar(255) DEFAULT NULL,
  `STATUS_APROVACAO` varchar(255) DEFAULT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  `VALOR_JUROS` decimal(18,4) DEFAULT NULL,
  `VALOR_MULTA` decimal(18,4) DEFAULT NULL,
  `BANCO_ID` bigint(20) DEFAULT NULL,
  `COBRANCA_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK2F0047737FFF790E` (`USUARIO_ID`),
  KEY `FK2F004773D89C75C1` (`COBRANCA_ID`),
  KEY `FK2F004773E44516C0` (`BANCO_ID`),
  CONSTRAINT `FK2F0047737FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK2F004773D89C75C1` FOREIGN KEY (`COBRANCA_ID`) REFERENCES `cobranca` (`ID`),
  CONSTRAINT `FK2F004773E44516C0` FOREIGN KEY (`BANCO_ID`) REFERENCES `banco` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `banco`
--

DROP TABLE IF EXISTS `banco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `banco` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AGENCIA` bigint(20) NOT NULL,
  `APELIDO` varchar(255) NOT NULL,
  `ATIVO` tinyint(1) NOT NULL,
  `CARTEIRA` int(11) DEFAULT NULL,
  `CODIGO_CEDENTE` varchar(255) NOT NULL,
  `CONTA` bigint(20) NOT NULL,
  `DV_AGENCIA` varchar(255) DEFAULT NULL,
  `DV_CONTA` varchar(255) DEFAULT NULL,
  `INSTRUCOES` varchar(255) DEFAULT NULL,
  `JUROS` decimal(18,4) DEFAULT NULL,
  `MULTA` decimal(18,4) DEFAULT NULL,
  `NOME` varchar(255) NOT NULL,
  `NUMERO_BANCO` varchar(255) NOT NULL,
  `VR_MULTA` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `NDX_NOME` (`NOME`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_referencia_cota`
--

DROP TABLE IF EXISTS `base_referencia_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_referencia_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FINAL_PERIODO` date DEFAULT NULL,
  `INICIO_PERIODO` date DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `COTA_ID` (`COTA_ID`),
  KEY `FK81B8E398C8181F74` (`COTA_ID`),
  CONSTRAINT `FK81B8E398C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `boleto_antecipado`
--

DROP TABLE IF EXISTS `boleto_antecipado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `boleto_antecipado` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` date NOT NULL,
  `DATA_VENCIMENTO` date NOT NULL,
  `DATA_PAGAMENTO` date DEFAULT NULL,
  `DATA_RECOLHIMENTO_CE_DE` date NOT NULL,
  `DATA_RECOLHIMENTO_CE_ATE` date NOT NULL,
  `NOSSO_NUMERO` varchar(255) NOT NULL,
  `DIGITO_NOSSO_NUMERO` varchar(255) DEFAULT NULL,
  `VALOR_DEBITO` decimal(18,4) NOT NULL,
  `VALOR_CREDITO` decimal(18,4) NOT NULL,
  `VALOR_REPARTE` decimal(18,4) NOT NULL,
  `VALOR_ENCALHE` decimal(18,4) NOT NULL,
  `VALOR_CE` decimal(18,4) NOT NULL,
  `VALOR` decimal(18,4) NOT NULL,
  `VALOR_PAGO` decimal(18,4) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `CHAMADA_ENCALHE_COTA_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  `BANCO_ID` bigint(20) DEFAULT NULL,
  `VIAS` int(11) DEFAULT NULL,
  `STATUS` varchar(255) NOT NULL,
  `TIPO_COBRANCA` varchar(255) NOT NULL,
  `MOVIMENTO_FINANCEIRO_COTA_ID` bigint(20) DEFAULT NULL,
  `BOLETO_ANTECIPADO_ID` bigint(20) DEFAULT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  `TIPO_BAIXA` varchar(31) DEFAULT NULL,
  `VALOR_JUROS` decimal(18,4) DEFAULT NULL,
  `VALOR_MULTA` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `boleto_distribuidor`
--

DROP TABLE IF EXISTS `boleto_distribuidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `boleto_distribuidor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_EMISSAO` datetime NOT NULL,
  `DATA_VENCIMENTO` datetime NOT NULL,
  `DIGITO_NOSSO_NUMERO_DISTRIBUIDOR` varchar(255) DEFAULT NULL,
  `NOSSO_NUMERO_DISTRIBUIDOR` varchar(255) NOT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `TIPO_COBRANCA` varchar(255) NOT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `VIAS` int(11) DEFAULT NULL,
  `BANCO_ID` bigint(20) DEFAULT NULL,
  `CHAMADA_ENCALHE_FORNECEDOR_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CHAMADA_ENCALHE_FORNECEDOR_ID` (`CHAMADA_ENCALHE_FORNECEDOR_ID`),
  KEY `FK4BA253ACE26473F4` (`CHAMADA_ENCALHE_FORNECEDOR_ID`),
  KEY `FK4BA253AC9808F874` (`FORNECEDOR_ID`),
  KEY `FK4BA253ACE44516C0` (`BANCO_ID`),
  CONSTRAINT `FK4BA253AC9808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK4BA253ACE26473F4` FOREIGN KEY (`CHAMADA_ENCALHE_FORNECEDOR_ID`) REFERENCES `chamada_encalhe_fornecedor` (`ID`),
  CONSTRAINT `FK4BA253ACE44516C0` FOREIGN KEY (`BANCO_ID`) REFERENCES `banco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `boleto_email`
--

DROP TABLE IF EXISTS `boleto_email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `boleto_email` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COBRANCA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `COBRANCA_ID` (`COBRANCA_ID`),
  CONSTRAINT `FKF83D21077C42C4C2` FOREIGN KEY (`COBRANCA_ID`) REFERENCES `cobranca` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `box`
--

DROP TABLE IF EXISTS `box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `box` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(11) NOT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `TIPO_BOX` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `box_usuario`
--

DROP TABLE IF EXISTS `box_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `box_usuario` (
  `BOX_ID` bigint(20) NOT NULL,
  `usuarios_ID` bigint(20) NOT NULL,
  UNIQUE KEY `usuarios_ID` (`usuarios_ID`),
  KEY `FK6C964C7A232855D7` (`usuarios_ID`),
  KEY `FK6C964C7ABA6EBE40` (`BOX_ID`),
  CONSTRAINT `FK6C964C7A232855D7` FOREIGN KEY (`usuarios_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK6C964C7ABA6EBE40` FOREIGN KEY (`BOX_ID`) REFERENCES `box` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `brinde`
--

DROP TABLE IF EXISTS `brinde`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `brinde` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(11) DEFAULT NULL,
  `DESCRICAO_BRINDE` varchar(255) DEFAULT NULL,
  `VENDE_BRINDE_SEPARADO` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carga_lancamento_aux`
--

DROP TABLE IF EXISTS `carga_lancamento_aux`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carga_lancamento_aux` (
  `codigodistribuidor` varchar(8) NOT NULL,
  `codigoFornecedorProduto` int(11) DEFAULT NULL,
  `codigoProduto` varchar(8) DEFAULT NULL,
  `edicaoProduto` int(11) DEFAULT NULL,
  `dataLancamento` date DEFAULT NULL,
  `repartePrevisto` float DEFAULT NULL,
  `precoPrevisto` float DEFAULT NULL,
  `repartePromocional` float DEFAULT NULL,
  `produto_edicao_id` varchar(60) DEFAULT NULL,
  KEY `produto_edicao_id` (`produto_edicao_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carga_lancamento_mdc`
--

DROP TABLE IF EXISTS `carga_lancamento_mdc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carga_lancamento_mdc` (
  `id` int(11) NOT NULL,
  `COD_AGENTE_LANP` varchar(7) DEFAULT NULL,
  `COD_PRODUTO_LANP` varchar(14) DEFAULT NULL,
  `COD_PRODIN` varchar(8) DEFAULT NULL,
  `NUM_EDICAO` int(11) DEFAULT NULL,
  `DATA_PREVISTA_LANCAMENTO_LANP` varchar(10) DEFAULT NULL,
  `DATA_REAL_LANCAMENTO_LANP` varchar(10) DEFAULT NULL,
  `TIPO_STATUS_LANCTO_LANP` varchar(3) DEFAULT NULL,
  `TIPO_LANCAMENTO_LANP` varchar(3) DEFAULT NULL,
  `VLR_PRECO_REAL_LANP` varchar(10) DEFAULT NULL,
  `COD_PRODUTO_RCPR` varchar(12) DEFAULT NULL,
  `NUM_RECOLTO_RCPR` int(11) DEFAULT NULL,
  `DATA_PREVISTA_RECOLTO_RCPR` varchar(10) DEFAULT NULL,
  `DATA_REAL_RECOLTO_RCPR` varchar(10) DEFAULT NULL,
  `TIPO_STATUS_RECOLTO_RCPR` varchar(45) DEFAULT NULL,
  `produto_edicao_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `produto_edicao_id` (`produto_edicao_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carga_lancamento_nds`
--

DROP TABLE IF EXISTS `carga_lancamento_nds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carga_lancamento_nds` (
  `codigodistribuidor` varchar(8) NOT NULL,
  `codigoFornecedorProduto` int(11) DEFAULT NULL,
  `codigoProduto` varchar(8) DEFAULT NULL,
  `edicaoProduto` int(11) DEFAULT NULL,
  `dataLancamento` date DEFAULT NULL,
  `repartePrevisto` float DEFAULT NULL,
  `precoPrevisto` float DEFAULT NULL,
  `repartePromocional` float DEFAULT NULL,
  `produto_edicao_id` varchar(60) DEFAULT NULL,
  KEY `produto_edicao_id` (`produto_edicao_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carga_segmento`
--

DROP TABLE IF EXISTS `carga_segmento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carga_segmento` (
  `codigo` varchar(8) NOT NULL,
  `segmento` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `caucao_liquida`
--

DROP TABLE IF EXISTS `caucao_liquida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `caucao_liquida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ATUALIZACAO` datetime DEFAULT NULL,
  `VALOR` double DEFAULT NULL,
  `COTA_GARANTIA_CAUCAO_LIQUIDA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKBC0347E0A3B7AA06` (`COTA_GARANTIA_CAUCAO_LIQUIDA_ID`),
  CONSTRAINT `FKBC0347E0A3B7AA06` FOREIGN KEY (`COTA_GARANTIA_CAUCAO_LIQUIDA_ID`) REFERENCES `cota_garantia` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cesar1`
--

DROP TABLE IF EXISTS `cesar1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cesar1` (
  `prid` int(11) DEFAULT NULL,
  `lanid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cfop`
--

DROP TABLE IF EXISTS `cfop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cfop` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` varchar(255) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chamada_encalhe`
--

DROP TABLE IF EXISTS `chamada_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chamada_encalhe` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_RECOLHIMENTO` date NOT NULL,
  `TIPO_CHAMADA_ENCALHE` varchar(255) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `SEQUENCIA` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK98FAFF70A53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `NDX_CHAMADA_ENCALHE` (`DATA_RECOLHIMENTO`),
  CONSTRAINT `FK98FAFF70A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chamada_encalhe_cota`
--

DROP TABLE IF EXISTS `chamada_encalhe_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chamada_encalhe_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FECHADO` tinyint(1) NOT NULL,
  `POSTERGADO` tinyint(1) NOT NULL,
  `QTDE_PREVISTA` decimal(18,4) DEFAULT NULL,
  `CHAMADA_ENCALHE_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKDB858748E720C6B4` (`CHAMADA_ENCALHE_ID`),
  KEY `FKDB858748C8181F74` (`COTA_ID`),
  KEY `NDX_POSTERGADO` (`POSTERGADO`),
  KEY `NDX_QTDE_PREVISTA` (`QTDE_PREVISTA`),
  KEY `USUARIO_ID` (`USUARIO_ID`),
  CONSTRAINT `chamada_encalhe_cota_ibfk_1` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `chamada_encalhe_cota_ibfk_2` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKDB858748C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FKDB858748E720C6B4` FOREIGN KEY (`CHAMADA_ENCALHE_ID`) REFERENCES `chamada_encalhe` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chamada_encalhe_fornecedor`
--

DROP TABLE IF EXISTS `chamada_encalhe_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chamada_encalhe_fornecedor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ANO_REFERENCIA` int(11) DEFAULT NULL,
  `CODIGO_DISTRIBUIDOR` bigint(20) DEFAULT NULL,
  `CODIGO_PREENCHIMENTO` varchar(255) NOT NULL,
  `CONTROLE` bigint(20) DEFAULT NULL,
  `DATA_EMISSAO` date NOT NULL,
  `DATA_LIMITE_RECOLHIMENTO` date DEFAULT NULL,
  `NOTA_VALORES_DIVERSOS` decimal(18,4) DEFAULT NULL,
  `NUM_CHAMADA_ENCALHE` bigint(20) NOT NULL,
  `NUMERO_SEMANA` int(11) NOT NULL,
  `STATUS` varchar(255) NOT NULL,
  `TIPO_CHAMADA_ENCALHE` int(11) NOT NULL,
  `TOTAL_CREDITO_APURADO` decimal(18,4) DEFAULT NULL,
  `TOTAL_CREDITO_INFORMADO` decimal(18,4) DEFAULT NULL,
  `TOTAL_MARGEM_APURADO` decimal(18,4) DEFAULT NULL,
  `TOTAL_MARGEM_INFORMADO` decimal(18,4) DEFAULT NULL,
  `TOTAL_VENDA_APURADA` decimal(18,4) DEFAULT NULL,
  `TOTAL_VENDA_INFORMADA` decimal(18,4) DEFAULT NULL,
  `CFOP_ID` bigint(20) DEFAULT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  `DATA_VENCIMENTO` date NOT NULL,
  `STATUS_CE_NDS` varchar(255) DEFAULT NULL,
  `STATUS_INTEGRACAO` varchar(255) DEFAULT 'NAO_INTEGRADO',
  `DATA_FECHAMENTO_NDS` date DEFAULT NULL,
  `STATUS_INTEGRACAO_NFE` varchar(255) DEFAULT 'NAO_APROVADO',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NUM_CHAMADA_ENCALHE` (`NUM_CHAMADA_ENCALHE`),
  KEY `FKE41EDD949808F874` (`FORNECEDOR_ID`),
  KEY `FKE41EDD947AB6324F` (`CFOP_ID`),
  CONSTRAINT `FKE41EDD947AB6324F` FOREIGN KEY (`CFOP_ID`) REFERENCES `cfop` (`ID`),
  CONSTRAINT `FKE41EDD949808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chamada_encalhe_lancamento`
--

DROP TABLE IF EXISTS `chamada_encalhe_lancamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chamada_encalhe_lancamento` (
  `CHAMADA_ENCALHE_ID` bigint(20) NOT NULL,
  `LANCAMENTO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`CHAMADA_ENCALHE_ID`,`LANCAMENTO_ID`),
  KEY `FK9669E1A9E720C6B4` (`CHAMADA_ENCALHE_ID`),
  KEY `FK9669E1A945C07ACF` (`LANCAMENTO_ID`),
  CONSTRAINT `FK9669E1A945C07ACF` FOREIGN KEY (`LANCAMENTO_ID`) REFERENCES `lancamento` (`ID`),
  CONSTRAINT `FK9669E1A9E720C6B4` FOREIGN KEY (`CHAMADA_ENCALHE_ID`) REFERENCES `chamada_encalhe` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cheque`
--

DROP TABLE IF EXISTS `cheque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cheque` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AGENCIA` bigint(20) NOT NULL,
  `CONTA` bigint(20) NOT NULL,
  `CORRENTISTA` varchar(255) NOT NULL,
  `DV_AGENCIA` varchar(255) NOT NULL,
  `DV_CONTA` varchar(255) NOT NULL,
  `DATA_EMISSAO` datetime NOT NULL,
  `NOME_BANCO` varchar(255) NOT NULL,
  `NUMERO_BANCO` varchar(255) NOT NULL,
  `NUMERO_CHEQUE` varchar(255) NOT NULL,
  `DATA_VALIDADE` datetime NOT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cheque_imagem`
--

DROP TABLE IF EXISTS `cheque_imagem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cheque_imagem` (
  `ID` bigint(20) NOT NULL,
  `IMAGE` longblob NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `classificacao_nao_recebida`
--

DROP TABLE IF EXISTS `classificacao_nao_recebida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classificacao_nao_recebida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `TIPO_CLASSIFICACAO_PRODUTO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7F7429DC7FFF790E` (`USUARIO_ID`),
  KEY `FK7F7429DCC8181F74` (`COTA_ID`),
  KEY `FK7F7429DC10C84C95` (`TIPO_CLASSIFICACAO_PRODUTO_ID`),
  CONSTRAINT `FK7F7429DC10C84C95` FOREIGN KEY (`TIPO_CLASSIFICACAO_PRODUTO_ID`) REFERENCES `tipo_classificacao_produto` (`ID`),
  CONSTRAINT `FK7F7429DC7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK7F7429DCC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cobranca`
--

DROP TABLE IF EXISTS `cobranca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cobranca` (
  `TIPO_DOCUMENTO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONTEMPLACAO` tinyint(1) DEFAULT NULL,
  `DT_EMISSAO` date NOT NULL,
  `DT_PAGAMENTO` date DEFAULT NULL,
  `DT_VENCIMENTO` date NOT NULL,
  `ENCARGOS` decimal(18,4) DEFAULT NULL,
  `NOSSO_NUMERO` varchar(255) NOT NULL,
  `STATUS_COBRANCA` varchar(255) NOT NULL,
  `TIPO_BAIXA` varchar(255) DEFAULT NULL,
  `TIPO_COBRANCA` varchar(255) NOT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `VIAS` int(11) DEFAULT NULL,
  `DIGITO_NOSSO_NUMERO` varchar(255) DEFAULT NULL,
  `NOSSO_NUMERO_COMPLETO` varchar(255) DEFAULT NULL,
  `BANCO_ID` bigint(20) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `DIVIDA_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  `ENVIAR_POR_EMAIL` tinyint(4) DEFAULT NULL,
  `COBRANCA_CENTRALIZACAO_ID` bigint(20) DEFAULT NULL,
  `ORIUNDA_NEGOCIACAO_AVULSA` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOSSO_NUMERO` (`NOSSO_NUMERO`),
  UNIQUE KEY `DIVIDA_ID` (`DIVIDA_ID`),
  UNIQUE KEY `NOSSO_NUMERO_COMPLETO` (`NOSSO_NUMERO_COMPLETO`),
  KEY `FKF83D2107C8181F74` (`COTA_ID`),
  KEY `FKF83D21077C42C4C1` (`DIVIDA_ID`),
  KEY `FKF83D21079808F874` (`FORNECEDOR_ID`),
  KEY `FKF83D2107E44516C0` (`BANCO_ID`),
  CONSTRAINT `FKF83D21077C42C4C1` FOREIGN KEY (`DIVIDA_ID`) REFERENCES `divida` (`ID`),
  CONSTRAINT `FKF83D21079808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FKF83D2107C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FKF83D2107E44516C0` FOREIGN KEY (`BANCO_ID`) REFERENCES `banco` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cobranca_centralizacao`
--

DROP TABLE IF EXISTS `cobranca_centralizacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cobranca_centralizacao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOSSO_NUMERO` varchar(255) NOT NULL,
  `DIGITO_NOSSO_NUMERO` varchar(255) DEFAULT NULL,
  `NOSSO_NUMERO_COMPLETO` varchar(255) DEFAULT NULL,
  `DT_EMISSAO` date NOT NULL,
  `DT_VENCIMENTO` date NOT NULL,
  `DT_PAGAMENTO` date DEFAULT NULL,
  `ENCARGOS` decimal(18,4) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `TIPO_BAIXA` varchar(255) DEFAULT NULL,
  `TIPO_COBRANCA` varchar(255) NOT NULL,
  `STATUS_COBRANCA` varchar(255) NOT NULL,
  `VIAS` int(11) DEFAULT NULL,
  `CONTEMPLACAO` tinyint(1) DEFAULT NULL,
  `ENVIAR_POR_EMAIL` tinyint(4) DEFAULT NULL,
  `BANCO_ID` bigint(20) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOSSO_NUMERO` (`NOSSO_NUMERO`),
  UNIQUE KEY `NOSSO_NUMERO_COMPLETO` (`NOSSO_NUMERO_COMPLETO`),
  KEY `FKF83D2107C8181F77` (`COTA_ID`),
  KEY `FKF83D21079808F877` (`FORNECEDOR_ID`),
  KEY `FKF83D2107E44516C7` (`BANCO_ID`),
  CONSTRAINT `FKF83D21079808F877` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FKF83D2107C8181F77` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FKF83D2107E44516C7` FOREIGN KEY (`BANCO_ID`) REFERENCES `banco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cobranca_controle_conferencia_encalhe_cota`
--

DROP TABLE IF EXISTS `cobranca_controle_conferencia_encalhe_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cobranca_controle_conferencia_encalhe_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COBRANCA_ID` bigint(20) DEFAULT NULL,
  `CONTROLE_CONF_ENCALHE_COTA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKA71C78B3D89C75C1` (`COBRANCA_ID`),
  KEY `FKA71C78B326E8AFB2` (`CONTROLE_CONF_ENCALHE_COTA_ID`),
  CONSTRAINT `FKA71C78B326E8AFB2` FOREIGN KEY (`CONTROLE_CONF_ENCALHE_COTA_ID`) REFERENCES `controle_conferencia_encalhe_cota` (`ID`),
  CONSTRAINT `FKA71C78B3D89C75C1` FOREIGN KEY (`COBRANCA_ID`) REFERENCES `cobranca` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `concentracao_cobranca_caucao_liquida`
--

DROP TABLE IF EXISTS `concentracao_cobranca_caucao_liquida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concentracao_cobranca_caucao_liquida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DIA_SEMANA` int(11) NOT NULL,
  `FORMA_COBRANCA_CAUCAO_LIQUIDA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK20DE4A31EA41BD85` (`FORMA_COBRANCA_CAUCAO_LIQUIDA_ID`),
  CONSTRAINT `FK20DE4A31EA41BD85` FOREIGN KEY (`FORMA_COBRANCA_CAUCAO_LIQUIDA_ID`) REFERENCES `forma_cobranca_caucao_liquida` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `concentracao_cobranca_cota`
--

DROP TABLE IF EXISTS `concentracao_cobranca_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concentracao_cobranca_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DIA_SEMANA` int(11) NOT NULL,
  `FORMA_COBRANCA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK53392B6AE34F875B` (`FORMA_COBRANCA_ID`),
  CONSTRAINT `FK53392B6AE34F875B` FOREIGN KEY (`FORMA_COBRANCA_ID`) REFERENCES `forma_cobranca` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `conferencia_enc_parcial`
--

DROP TABLE IF EXISTS `conferencia_enc_parcial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conferencia_enc_parcial` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_APROVACAO` datetime DEFAULT NULL,
  `DATA_CONF_ENC_PARCIAL` datetime NOT NULL,
  `DATA_MOVIMENTO` date NOT NULL,
  `DIFERENCA_APURADA` tinyint(1) DEFAULT NULL,
  `NF_PARCIAL_GERADA` tinyint(1) DEFAULT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `STATUS_APROVACAO` varchar(255) NOT NULL,
  `produtoEdicao_ID` bigint(20) DEFAULT NULL,
  `responsavel_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK35D69C6FB7E346C0` (`produtoEdicao_ID`),
  KEY `FK35D69C6F5F18CCBC` (`responsavel_ID`),
  CONSTRAINT `FK35D69C6F5F18CCBC` FOREIGN KEY (`responsavel_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK35D69C6FB7E346C0` FOREIGN KEY (`produtoEdicao_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `conferencia_encalhe`
--

DROP TABLE IF EXISTS `conferencia_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conferencia_encalhe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` date NOT NULL,
  `JURAMENTADA` tinyint(1) DEFAULT NULL,
  `OBSERVACAO` varchar(255) DEFAULT NULL,
  `PRECO_CAPA_INFORMADO` decimal(18,4) DEFAULT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `QTDE_INFORMADA` decimal(18,4) DEFAULT NULL,
  `CHAMADA_ENCALHE_COTA_ID` bigint(20) DEFAULT NULL,
  `CONTROLE_CONFERENCIA_ENCALHE_COTA_ID` bigint(20) NOT NULL,
  `MOVIMENTO_ESTOQUE_ID` bigint(20) DEFAULT NULL,
  `MOVIMENTO_ESTOQUE_COTA_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `DIA_RECOLHIMENTO` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8E92BB04232FFB12` (`MOVIMENTO_ESTOQUE_ID`),
  KEY `FK8E92BB04D3D96F81` (`CONTROLE_CONFERENCIA_ENCALHE_COTA_ID`),
  KEY `FK8E92BB04158AFF55` (`CHAMADA_ENCALHE_COTA_ID`),
  KEY `FK8E92BB04BBE20E9D` (`MOVIMENTO_ESTOQUE_COTA_ID`),
  KEY `FK8E92BB04A53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `NDX_DATA` (`DATA`),
  CONSTRAINT `FK8E92BB04158AFF55` FOREIGN KEY (`CHAMADA_ENCALHE_COTA_ID`) REFERENCES `chamada_encalhe_cota` (`ID`),
  CONSTRAINT `FK8E92BB04232FFB12` FOREIGN KEY (`MOVIMENTO_ESTOQUE_ID`) REFERENCES `movimento_estoque` (`ID`),
  CONSTRAINT `FK8E92BB04A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK8E92BB04BBE20E9D` FOREIGN KEY (`MOVIMENTO_ESTOQUE_COTA_ID`) REFERENCES `movimento_estoque_cota` (`ID`),
  CONSTRAINT `FK8E92BB04D3D96F81` FOREIGN KEY (`CONTROLE_CONFERENCIA_ENCALHE_COTA_ID`) REFERENCES `controle_conferencia_encalhe_cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `conferencia_encalhe_backup`
--

DROP TABLE IF EXISTS `conferencia_encalhe_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conferencia_encalhe_backup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID_CONFERENCIA_ORIGINAL` bigint(20) DEFAULT NULL,
  `DATA_OPERACAO` date NOT NULL,
  `CHAMADA_ENCALHE_COTA_ID` bigint(20) DEFAULT NULL,
  `DATA_CRIACAO` date NOT NULL,
  `DIA_RECOLHIMENTO` int(10) DEFAULT NULL,
  `JURAMENTADA` tinyint(1) DEFAULT NULL,
  `OBSERVACAO` varchar(255) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `QTDE_INFORMADA` decimal(18,4) DEFAULT NULL,
  `PRECO_CAPA_INFORMADO` decimal(18,4) DEFAULT NULL,
  `PRECO_CAPA` decimal(18,4) DEFAULT NULL,
  `PRECO_COM_DESCONTO` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CONFBACKUP_CHAMADA_ENCALHE_COTA` (`CHAMADA_ENCALHE_COTA_ID`),
  KEY `FK_CONFBACKUP_COTA_ID` (`COTA_ID`),
  KEY `FK_CONFBACKUP_PRODUTO_EDICAO_ID` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK_CONFBACKUP_CHAMADA_ENCALHE_COTA` FOREIGN KEY (`CHAMADA_ENCALHE_COTA_ID`) REFERENCES `chamada_encalhe_cota` (`ID`),
  CONSTRAINT `FK_CONFBACKUP_COTA_ID` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FK_CONFBACKUP_PRODUTO_EDICAO_ID` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consolidado_financeiro_cota`
--

DROP TABLE IF EXISTS `consolidado_financeiro_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consolidado_financeiro_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONSIGNADO` decimal(18,4) DEFAULT NULL,
  `DT_CONSOLIDADO` date NOT NULL,
  `DEBITO_CREDITO` decimal(18,4) DEFAULT NULL,
  `ENCALHE` decimal(18,4) DEFAULT NULL,
  `ENCARGOS` decimal(18,4) DEFAULT NULL,
  `PENDENTE` decimal(18,4) DEFAULT NULL,
  `TOTAL` decimal(18,4) DEFAULT NULL,
  `VALOR_POSTERGADO` decimal(18,4) DEFAULT NULL,
  `VENDA_ENCALHE` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8468B330C8181F74` (`COTA_ID`),
  KEY `NDX_DT_CONSOLIDADO` (`DT_CONSOLIDADO`),
  CONSTRAINT `FK8468B330C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consolidado_mvto_financeiro_cota`
--

DROP TABLE IF EXISTS `consolidado_mvto_financeiro_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consolidado_mvto_financeiro_cota` (
  `CONSOLIDADO_FINANCEIRO_ID` bigint(20) NOT NULL,
  `MVTO_FINANCEIRO_COTA_ID` bigint(20) NOT NULL,
  UNIQUE KEY `MVTO_FINANCEIRO_COTA_ID` (`MVTO_FINANCEIRO_COTA_ID`),
  KEY `FK6303D6CDE6E795C3` (`MVTO_FINANCEIRO_COTA_ID`),
  KEY `FK6303D6CD68B7C893` (`CONSOLIDADO_FINANCEIRO_ID`),
  CONSTRAINT `FK6303D6CD68B7C893` FOREIGN KEY (`CONSOLIDADO_FINANCEIRO_ID`) REFERENCES `consolidado_financeiro_cota` (`ID`),
  CONSTRAINT `FK6303D6CDE6E795C3` FOREIGN KEY (`MVTO_FINANCEIRO_COTA_ID`) REFERENCES `movimento_financeiro_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contrato`
--

DROP TABLE IF EXISTS `contrato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contrato` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AVISO_PREVIO_RESCISAO` int(11) DEFAULT NULL,
  `DATA_INICIO` datetime DEFAULT NULL,
  `DATA_TERMINO` datetime DEFAULT NULL,
  `PRAZO` int(11) DEFAULT NULL,
  `EXIGE_DOC_SUSPENSAO` tinyint(1) NOT NULL,
  `RECEBIDO` tinyint(1) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `COTA_ID` (`COTA_ID`),
  KEY `FKCDB031CC8181F74` (`COTA_ID`),
  CONSTRAINT `FKCDB031CC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controle_baixa_bancaria`
--

DROP TABLE IF EXISTS `controle_baixa_bancaria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `controle_baixa_bancaria` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_OPERACAO` date NOT NULL,
  `DATA_PAGAMENTO` date NOT NULL,
  `STATUS` varchar(255) NOT NULL,
  `BANCO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DATA` (`DATA_PAGAMENTO`,`BANCO_ID`),
  KEY `FK5CE6C6207FFF790E` (`USUARIO_ID`),
  KEY `FK5CE6C620E44516C0` (`BANCO_ID`),
  CONSTRAINT `FK5CE6C6207FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK5CE6C620E44516C0` FOREIGN KEY (`BANCO_ID`) REFERENCES `banco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controle_conferencia_encalhe`
--

DROP TABLE IF EXISTS `controle_conferencia_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `controle_conferencia_encalhe` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` date NOT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `data_controle_conferencia_encalhe` (`DATA`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controle_conferencia_encalhe_cota`
--

DROP TABLE IF EXISTS `controle_conferencia_encalhe_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `controle_conferencia_encalhe_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_FIM` datetime NOT NULL,
  `DATA_INICIO` datetime NOT NULL,
  `DATA_OPERACAO` date NOT NULL,
  `NOSSO_NUMERO` varchar(255) DEFAULT NULL,
  `NUMERO_SLIP` bigint(20) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `BOX_ID` bigint(20) NOT NULL,
  `CTRL_CONF_ENCALHE_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8EB091EB7FFF790E` (`USUARIO_ID`),
  KEY `FK8EB091EBC8181F74` (`COTA_ID`),
  KEY `FK8EB091EB9BF01A38` (`CTRL_CONF_ENCALHE_ID`),
  KEY `FK8EB091EBBA6EBE40` (`BOX_ID`),
  CONSTRAINT `FK8EB091EB7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK8EB091EB9BF01A38` FOREIGN KEY (`CTRL_CONF_ENCALHE_ID`) REFERENCES `controle_conferencia_encalhe` (`ID`),
  CONSTRAINT `FK8EB091EBBA6EBE40` FOREIGN KEY (`BOX_ID`) REFERENCES `box` (`ID`),
  CONSTRAINT `FK8EB091EBC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controle_contagem_devolucao`
--

DROP TABLE IF EXISTS `controle_contagem_devolucao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `controle_contagem_devolucao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` date NOT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DATA` (`DATA`,`PRODUTO_EDICAO_ID`),
  KEY `FK90E56DC6A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK90E56DC6A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controle_fechamento_encalhe`
--

DROP TABLE IF EXISTS `controle_fechamento_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `controle_fechamento_encalhe` (
  `DATA_ENCALHE` date NOT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`DATA_ENCALHE`),
  KEY `fk_controle_fechamento_encalhe_usuario_id` (`USUARIO_ID`),
  CONSTRAINT `fk_controle_fechamento_encalhe_usuario_id` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controle_geracao_divida`
--

DROP TABLE IF EXISTS `controle_geracao_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `controle_geracao_divida` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` date DEFAULT NULL,
  `STATUS` varchar(255) NOT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK56402A877FFF790E` (`USUARIO_ID`),
  CONSTRAINT `FK56402A877FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controle_numeracao_nf`
--

DROP TABLE IF EXISTS `controle_numeracao_nf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `controle_numeracao_nf` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PROXIMO_NUMERO_NF` bigint(20) NOT NULL,
  `SERIE_NF` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SERIE_NF` (`SERIE_NF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `controle_numeracao_slip`
--

DROP TABLE IF EXISTS `controle_numeracao_slip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `controle_numeracao_slip` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PROXIMO_NUMERO_SLIP` bigint(20) NOT NULL,
  `TIPO_SLIP` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TIPO_SLIP` (`TIPO_SLIP`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cota`
--

DROP TABLE IF EXISTS `cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CLASSIFICACAO_ESPECTATIVA_FATURAMENTO` varchar(255) DEFAULT NULL,
  `INICIO_ATIVIDADE` date NOT NULL,
  `INICIO_TITULARIDADE` date NOT NULL,
  `NUMERO_COTA` int(11) NOT NULL,
  `VALOR_MINIMO_COBRANCA` decimal(18,4) DEFAULT NULL,
  `ASSISTENTE_COMERCIAL` varchar(255) DEFAULT NULL,
  `BOLETO_EMAIL` tinyint(1) DEFAULT NULL,
  `BOLETO_IMPRESSO` tinyint(1) DEFAULT NULL,
  `BOLETO_SLIP_EMAIL` tinyint(1) DEFAULT NULL,
  `BOLETO_SLIP_IMPRESSO` tinyint(1) DEFAULT NULL,
  `CHAMADA_ENCALHE_EMAIL` tinyint(1) DEFAULT NULL,
  `CHAMADA_ENCALHE_IMPRESSO` tinyint(1) DEFAULT NULL,
  `DESCRICAO_TIPO_ENTREGA` varchar(255) DEFAULT NULL,
  `FIM_PERIODO_CARENCIA` date DEFAULT NULL,
  `GERENTE_COMERCIAL` varchar(255) DEFAULT NULL,
  `INICIO_PERIODO_CARENCIA` date DEFAULT NULL,
  `NOTA_ENVIO_EMAIL` tinyint(1) DEFAULT NULL,
  `NOTA_ENVIO_IMPRESSO` tinyint(1) DEFAULT NULL,
  `OBSERVACAO` varchar(255) DEFAULT NULL,
  `PROCURACAO_RECEBIDA` tinyint(1) DEFAULT NULL,
  `QTDE_PDV` int(11) DEFAULT NULL,
  `RECEBE_RECOLHE_PARCIAIS` tinyint(1) DEFAULT NULL,
  `RECEBE_COMPLEMENTAR` tinyint(1) DEFAULT '0',
  `RECIBO_EMAIL` tinyint(1) DEFAULT NULL,
  `RECIBO_IMPRESSO` tinyint(1) DEFAULT NULL,
  `REPARTE_POR_PONTO_VENDA` tinyint(1) DEFAULT NULL,
  `SLIP_EMAIL` tinyint(1) DEFAULT NULL,
  `SLIP_IMPRESSO` tinyint(1) DEFAULT NULL,
  `SOLICITA_NUM_ATRASADOS` tinyint(1) DEFAULT NULL,
  `TERMO_ADESAO_RECEBIDO` tinyint(1) DEFAULT NULL,
  `UTILIZA_PROCURACAO` tinyint(1) DEFAULT NULL,
  `UTILIZA_TERMO_ADESAO` tinyint(1) DEFAULT NULL,
  `EMAIL_NF_E` varchar(255) DEFAULT NULL,
  `EMITE_NF_E` tinyint(1) DEFAULT NULL,
  `POSSUI_CONTRATO` tinyint(1) NOT NULL,
  `SITUACAO_CADASTRO` varchar(255) NOT NULL,
  `SUGERE_SUSPENSAO` tinyint(1) NOT NULL,
  `SUGERE_SUSPENSAO_DISTRIBUIDOR` tinyint(1) NOT NULL,
  `BOX_ID` bigint(20) DEFAULT NULL,
  `ID_FIADOR` bigint(20) DEFAULT NULL,
  `PESSOA_ID` bigint(20) NOT NULL,
  `TIPO_DISTRIBUICAO_COTA` varchar(255) DEFAULT NULL,
  `TIPO_COTA` varchar(255) NOT NULL,
  `COTA_UNIFICACAO_ID` bigint(20) DEFAULT NULL,
  `VALOR_SUSPENSAO` decimal(18,4) DEFAULT NULL,
  `NUM_ACUMULO_DIVIDA` int(11) DEFAULT NULL,
  `DEVOLVE_ENCALHE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK1FA7D99B8CB634` (`PESSOA_ID`),
  KEY `FK1FA7D9DC6BE690` (`ID_FIADOR`),
  KEY `FK1FA7D9BA6EBE40` (`BOX_ID`),
  KEY `NDX_NUMERO_COTA` (`NUMERO_COTA`),
  CONSTRAINT `FK1FA7D99B8CB634` FOREIGN KEY (`PESSOA_ID`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FK1FA7D9BA6EBE40` FOREIGN KEY (`BOX_ID`) REFERENCES `box` (`ID`),
  CONSTRAINT `FK1FA7D9DC6BE690` FOREIGN KEY (`ID_FIADOR`) REFERENCES `fiador` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cota_ausente`
--

DROP TABLE IF EXISTS `cota_ausente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cota_ausente` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` date NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK544D8993C8181F74` (`COTA_ID`),
  CONSTRAINT `FK544D8993C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cota_ausente_movimento_estoque_cota`
--

DROP TABLE IF EXISTS `cota_ausente_movimento_estoque_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cota_ausente_movimento_estoque_cota` (
  `COTA_AUSENTE_ID` bigint(20) NOT NULL,
  `MOVIMENTO_ESTOQUE_COTA_ID` bigint(20) NOT NULL,
  UNIQUE KEY `MOVIMENTO_ESTOQUE_COTA_ID` (`MOVIMENTO_ESTOQUE_COTA_ID`),
  KEY `FK7D6984EF70E707D7` (`COTA_AUSENTE_ID`),
  KEY `FK7D6984EFBBE20E9D` (`MOVIMENTO_ESTOQUE_COTA_ID`),
  CONSTRAINT `FK7D6984EF70E707D7` FOREIGN KEY (`COTA_AUSENTE_ID`) REFERENCES `cota_ausente` (`ID`),
  CONSTRAINT `FK7D6984EFBBE20E9D` FOREIGN KEY (`MOVIMENTO_ESTOQUE_COTA_ID`) REFERENCES `movimento_estoque_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cota_base`
--

DROP TABLE IF EXISTS `cota_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cota_base` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_FIM` date DEFAULT NULL,
  `DATA_INICIO` date DEFAULT NULL,
  `INDICE_AJUSTE` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKAC360F77C8181F74` (`COTA_ID`),
  CONSTRAINT `FKAC360F77C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cota_base_cota`
--

DROP TABLE IF EXISTS `cota_base_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cota_base_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATIVO` tinyint(1) NOT NULL,
  `DT_FIM_VIGENCIA` date DEFAULT NULL,
  `DT_INICIO_VIGENCIA` date DEFAULT NULL,
  `TIPO_ALTERACAO` varchar(255) NOT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `COTA_BASE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKEEA663A132855C87` (`COTA_BASE_ID`),
  KEY `FKEEA663A1C8181F74` (`COTA_ID`),
  CONSTRAINT `FKEEA663A132855C87` FOREIGN KEY (`COTA_BASE_ID`) REFERENCES `cota_base` (`ID`),
  CONSTRAINT `FKEEA663A1C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cota_fornecedor`
--

DROP TABLE IF EXISTS `cota_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cota_fornecedor` (
  `COTA_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`FORNECEDOR_ID`,`COTA_ID`),
  KEY `FK1ADE098BC8181F74` (`COTA_ID`),
  KEY `FK1ADE098B9808F874` (`FORNECEDOR_ID`),
  CONSTRAINT `FK1ADE098B9808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK1ADE098BC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cota_garantia`
--

DROP TABLE IF EXISTS `cota_garantia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cota_garantia` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` datetime DEFAULT NULL,
  `AGENCIA_BANCO` bigint(20) DEFAULT NULL,
  `CONTA_BANCO` bigint(20) DEFAULT NULL,
  `DV_AGENCIA_BANCO` varchar(255) DEFAULT NULL,
  `DV_CONTA_BANCO` varchar(255) DEFAULT NULL,
  `NOME_BANCO` varchar(255) DEFAULT NULL,
  `CORRENTISTA` varchar(255) DEFAULT NULL,
  `NUMERO_BANCO` varchar(255) DEFAULT NULL,
  `TIPO_COBRANCA` varchar(255) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `PAGAMENTO_CAUCAO_LIQUIDA_ID` bigint(20) DEFAULT NULL,
  `CHEQUE_CAUCAO_ID` bigint(20) DEFAULT NULL,
  `FIADOR_ID` bigint(20) DEFAULT NULL,
  `NOTA_PROMISSORIA_ID` bigint(20) DEFAULT NULL,
  `TIPO_GARANTIA` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `COTA_ID` (`COTA_ID`),
  KEY `FK80C0DF0DFE263B9B` (`PAGAMENTO_CAUCAO_LIQUIDA_ID`),
  KEY `FK80C0DF0DC8181F74` (`COTA_ID`),
  KEY `FK80C0DF0DEA490E43` (`NOTA_PROMISSORIA_ID`),
  KEY `FK80C0DF0DB6D1BA3D` (`CHEQUE_CAUCAO_ID`),
  KEY `FK80C0DF0D8DC372F4` (`FIADOR_ID`),
  CONSTRAINT `FK80C0DF0D8DC372F4` FOREIGN KEY (`FIADOR_ID`) REFERENCES `fiador` (`ID`),
  CONSTRAINT `FK80C0DF0DB6D1BA3D` FOREIGN KEY (`CHEQUE_CAUCAO_ID`) REFERENCES `cheque` (`ID`),
  CONSTRAINT `FK80C0DF0DC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FK80C0DF0DEA490E43` FOREIGN KEY (`NOTA_PROMISSORIA_ID`) REFERENCES `nota_promissoria` (`ID`),
  CONSTRAINT `FK80C0DF0DFE263B9B` FOREIGN KEY (`PAGAMENTO_CAUCAO_LIQUIDA_ID`) REFERENCES `pagamento_caucao_liquida` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cota_grupo`
--

DROP TABLE IF EXISTS `cota_grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cota_grupo` (
  `GRUPO_COTA_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`GRUPO_COTA_ID`,`COTA_ID`),
  KEY `FKDADA1863C8181F74` (`COTA_ID`),
  KEY `FKDADA18631E8AD533` (`GRUPO_COTA_ID`),
  CONSTRAINT `FKDADA18631E8AD533` FOREIGN KEY (`GRUPO_COTA_ID`) REFERENCES `grupo_cota` (`ID`),
  CONSTRAINT `FKDADA1863C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cota_unificacao`
--

DROP TABLE IF EXISTS `cota_unificacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cota_unificacao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COTA_ID` bigint(20) NOT NULL,
  `DATA_UNIFICACAO` date NOT NULL,
  `POLITICA_COBRANCA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `COTA_ID` (`COTA_ID`),
  CONSTRAINT `FKF83D21077C42C4C3` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cotaunificacao_cotaunificada`
--

DROP TABLE IF EXISTS `cotaunificacao_cotaunificada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cotaunificacao_cotaunificada` (
  `COTA_UNIFICACAO_ID` bigint(20) DEFAULT NULL,
  `COTA_UNIFICADA_ID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `debito_credito_cota`
--

DROP TABLE IF EXISTS `debito_credito_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `debito_credito_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COMPOSICAO_COBRANCA` bit(1) DEFAULT NULL,
  `DATA_LANCAMENTO` date DEFAULT NULL,
  `TIPO_VENCIMENTO` date DEFAULT NULL,
  `TIPO_DESCRICAO` varchar(255) DEFAULT NULL,
  `OBSERVACOES` varchar(255) DEFAULT NULL,
  `TIPO_LANCAMENTO` varchar(255) DEFAULT NULL,
  `TIPO_MOVIMENTO` varchar(255) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `SLIP_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKE2A9E3BEADA6DD0A` (`SLIP_ID`),
  CONSTRAINT `FKE2A9E3BEADA6DD0A` FOREIGN KEY (`SLIP_ID`) REFERENCES `slip` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto`
--

DROP TABLE IF EXISTS `desconto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `TIPO_DESCONTO` varchar(255) DEFAULT NULL,
  `USADO` tinyint(1) NOT NULL,
  `valor` decimal(18,4) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6B555EAB7FFF790E` (`USUARIO_ID`),
  CONSTRAINT `FK6B555EAB7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_cota`
--

DROP TABLE IF EXISTS `desconto_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `DESCONTO` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKF27C74ED7FFF790E` (`USUARIO_ID`),
  KEY `FKF27C74ED56501954` (`DISTRIBUIDOR_ID`),
  KEY `FKF27C74EDC8181F74` (`COTA_ID`),
  CONSTRAINT `FKF27C74ED56501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FKF27C74ED7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKF27C74EDC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_cota_fornecedor`
--

DROP TABLE IF EXISTS `desconto_cota_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_cota_fornecedor` (
  `DESCONTO_COTA_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`DESCONTO_COTA_ID`,`FORNECEDOR_ID`),
  KEY `FK325007F79808F874` (`FORNECEDOR_ID`),
  KEY `FK325007F72E6ACC5A` (`DESCONTO_COTA_ID`),
  CONSTRAINT `FK325007F72E6ACC5A` FOREIGN KEY (`DESCONTO_COTA_ID`) REFERENCES `desconto_cota` (`ID`),
  CONSTRAINT `FK325007F79808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_cota_produto_excessoes`
--

DROP TABLE IF EXISTS `desconto_cota_produto_excessoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_cota_produto_excessoes` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `DESCONTO_PREDOMINANTE` tinyint(1) NOT NULL,
  `TIPO_DESCONTO` varchar(255) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `DESCONTO_ID` bigint(20) DEFAULT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FORNECEDOR_ID` (`FORNECEDOR_ID`,`COTA_ID`,`PRODUTO_ID`,`PRODUTO_EDICAO_ID`),
  KEY `FKD845FEF07FFF790E` (`USUARIO_ID`),
  KEY `FKD845FEF056501954` (`DISTRIBUIDOR_ID`),
  KEY `FKD845FEF0C8181F74` (`COTA_ID`),
  KEY `FKD845FEF029E8DFE3` (`DESCONTO_ID`),
  KEY `FKD845FEF0C5C16480` (`PRODUTO_ID`),
  KEY `FKD845FEF09808F874` (`FORNECEDOR_ID`),
  KEY `FKD845FEF0A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FKD845FEF029E8DFE3` FOREIGN KEY (`DESCONTO_ID`) REFERENCES `desconto` (`ID`),
  CONSTRAINT `FKD845FEF056501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FKD845FEF07FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKD845FEF09808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FKD845FEF0A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FKD845FEF0C5C16480` FOREIGN KEY (`PRODUTO_ID`) REFERENCES `produto` (`ID`),
  CONSTRAINT `FKD845FEF0C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_distribuidor`
--

DROP TABLE IF EXISTS `desconto_distribuidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_distribuidor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `DESCONTO` decimal(18,4) DEFAULT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK647A9CC27FFF790E` (`USUARIO_ID`),
  KEY `FK647A9CC256501954` (`DISTRIBUIDOR_ID`),
  CONSTRAINT `FK647A9CC256501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FK647A9CC27FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_distribuidor_fornecedor`
--

DROP TABLE IF EXISTS `desconto_distribuidor_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_distribuidor_fornecedor` (
  `DESCONTO_DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`DESCONTO_DISTRIBUIDOR_ID`,`FORNECEDOR_ID`),
  KEY `FKAC9219027AB2803A` (`DESCONTO_DISTRIBUIDOR_ID`),
  KEY `FKAC9219029808F874` (`FORNECEDOR_ID`),
  CONSTRAINT `FKAC9219027AB2803A` FOREIGN KEY (`DESCONTO_DISTRIBUIDOR_ID`) REFERENCES `desconto_distribuidor` (`ID`),
  CONSTRAINT `FKAC9219029808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_lancamento_cota`
--

DROP TABLE IF EXISTS `desconto_lancamento_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_lancamento_cota` (
  `DESCONTO_LANCAMENTO_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`DESCONTO_LANCAMENTO_ID`,`COTA_ID`),
  KEY `FK9A3AA9EAC8181F74` (`COTA_ID`),
  KEY `FK9A3AA9EA738F4640` (`DESCONTO_LANCAMENTO_ID`),
  CONSTRAINT `FK9A3AA9EA738F4640` FOREIGN KEY (`DESCONTO_LANCAMENTO_ID`) REFERENCES `desconto_proximos_lancamentos` (`ID`),
  CONSTRAINT `FK9A3AA9EAC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_logistica`
--

DROP TABLE IF EXISTS `desconto_logistica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_logistica` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_INICIO_VIGENCIA` date NOT NULL,
  `DESCRICAO` varchar(255) DEFAULT NULL,
  `PERCENTUAL_DESCONTO` decimal(18,4) DEFAULT NULL,
  `PERCENTUAL_PRESTACAO_SERVICO` float NOT NULL,
  `TIPO_DESCONTO` int(11) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FORNECEDOR_ID` (`FORNECEDOR_ID`),
  CONSTRAINT `desconto_logistica_ibfk_1` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_produto`
--

DROP TABLE IF EXISTS `desconto_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_produto` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `DESCONTO` decimal(18,4) DEFAULT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK15B685457FFF790E` (`USUARIO_ID`),
  KEY `FK15B6854556501954` (`DISTRIBUIDOR_ID`),
  KEY `FK15B68545A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK15B6854556501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FK15B685457FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK15B68545A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_produto_cota`
--

DROP TABLE IF EXISTS `desconto_produto_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_produto_cota` (
  `DESCONTO_PRODUTO_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`DESCONTO_PRODUTO_ID`,`COTA_ID`),
  KEY `FK7BAD5E9334C7C5DA` (`DESCONTO_PRODUTO_ID`),
  KEY `FK7BAD5E93C8181F74` (`COTA_ID`),
  CONSTRAINT `FK7BAD5E9334C7C5DA` FOREIGN KEY (`DESCONTO_PRODUTO_ID`) REFERENCES `desconto_produto` (`ID`),
  CONSTRAINT `FK7BAD5E93C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_produto_edicao`
--

DROP TABLE IF EXISTS `desconto_produto_edicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_produto_edicao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCONTO` decimal(18,4) DEFAULT NULL,
  `TIPO_DESCONTO` varchar(255) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FORNECEDOR_ID` (`FORNECEDOR_ID`,`PRODUTO_EDICAO_ID`,`COTA_ID`),
  KEY `FK4899C421C8181F74` (`COTA_ID`),
  KEY `FK4899C4219808F874` (`FORNECEDOR_ID`),
  KEY `FK4899C421A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK4899C4219808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK4899C421A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK4899C421C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desconto_proximos_lancamentos`
--

DROP TABLE IF EXISTS `desconto_proximos_lancamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desconto_proximos_lancamentos` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_INICIO_DESCONTO` datetime DEFAULT NULL,
  `QUANTIDADE_PROXIMOS_LANCAMENTOS` int(11) DEFAULT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `PRODUTO_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `APLICADO_A_TODAS_AS_COTAS` tinyint(1) NOT NULL,
  `DESCONTO_ID` bigint(20) DEFAULT NULL,
  `QUANTIDADE_PROXIMOS_LANCAMENTOS_ORIGINAL` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7E8614E17FFF790E` (`USUARIO_ID`),
  KEY `FK7E8614E156501954` (`DISTRIBUIDOR_ID`),
  KEY `FK7E8614E1C5C16480` (`PRODUTO_ID`),
  KEY `FK7E8614E129E8DFE3` (`DESCONTO_ID`),
  CONSTRAINT `FK7E8614E129E8DFE3` FOREIGN KEY (`DESCONTO_ID`) REFERENCES `desconto` (`ID`),
  CONSTRAINT `FK7E8614E156501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FK7E8614E17FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK7E8614E1C5C16480` FOREIGN KEY (`PRODUTO_ID`) REFERENCES `produto` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desenglobacao`
--

DROP TABLE IF EXISTS `desenglobacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desenglobacao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `PORCENTAGEM_COTA_ENGLOBADA` float DEFAULT NULL,
  `COTA_ID_DESENGLOBADA` bigint(20) DEFAULT NULL,
  `COTA_ID_ENGLOBADA` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `TIPO_PDV_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKC6D71263511FDA95` (`TIPO_PDV_ID`),
  KEY `FKC6D712637FFF790E` (`USUARIO_ID`),
  KEY `FKC6D71263E4FA7FFC` (`COTA_ID_DESENGLOBADA`),
  KEY `FKC6D712637F864012` (`COTA_ID_ENGLOBADA`),
  CONSTRAINT `FKC6D71263511FDA95` FOREIGN KEY (`TIPO_PDV_ID`) REFERENCES `tipo_ponto_pdv` (`ID`),
  CONSTRAINT `FKC6D712637F864012` FOREIGN KEY (`COTA_ID_ENGLOBADA`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FKC6D712637FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKC6D71263E4FA7FFC` FOREIGN KEY (`COTA_ID_DESENGLOBADA`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dia_recolhimento_grupo_cota`
--

DROP TABLE IF EXISTS `dia_recolhimento_grupo_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dia_recolhimento_grupo_cota` (
  `GRUPO_ID` bigint(20) NOT NULL,
  `DIA_ID` varchar(255) DEFAULT NULL,
  KEY `FK90BC5188E6F63479` (`GRUPO_ID`),
  CONSTRAINT `FK90BC5188E6F63479` FOREIGN KEY (`GRUPO_ID`) REFERENCES `grupo_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `diferenca`
--

DROP TABLE IF EXISTS `diferenca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diferenca` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AUTOMATICA` tinyint(1) DEFAULT NULL,
  `DATA_MOVIMENTACAO` date DEFAULT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `STATUS_CONFIRMACAO` varchar(255) NOT NULL,
  `TIPO_DIFERENCA` varchar(255) NOT NULL,
  `TIPO_DIRECIONAMENTO` varchar(255) DEFAULT NULL,
  `TIPO_ESTOQUE` varchar(255) NOT NULL,
  `itemRecebimentoFisico_ID` bigint(20) DEFAULT NULL,
  `LANCAMENTO_DIFERENCA_ID` bigint(20) DEFAULT NULL,
  `responsavel_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF2B9F0957204FEE9` (`itemRecebimentoFisico_ID`),
  KEY `FKF2B9F095C4673234` (`LANCAMENTO_DIFERENCA_ID`),
  KEY `FKF2B9F0955F18CCBC` (`responsavel_ID`),
  KEY `FKF2B9F095A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FKF2B9F0955F18CCBC` FOREIGN KEY (`responsavel_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKF2B9F0957204FEE9` FOREIGN KEY (`itemRecebimentoFisico_ID`) REFERENCES `item_receb_fisico` (`ID`),
  CONSTRAINT `FKF2B9F095A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FKF2B9F095C4673234` FOREIGN KEY (`LANCAMENTO_DIFERENCA_ID`) REFERENCES `lancamento_diferenca` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribuicao_distribuidor`
--

DROP TABLE IF EXISTS `distribuicao_distribuidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribuicao_distribuidor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DIA_SEMANA` int(11) NOT NULL,
  `OPERACAO_DISTRIBUIDOR` varchar(255) DEFAULT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKC8044A5556501954` (`DISTRIBUIDOR_ID`),
  CONSTRAINT `FKC8044A5556501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribuicao_fornecedor`
--

DROP TABLE IF EXISTS `distribuicao_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribuicao_fornecedor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DIA_SEMANA` int(11) NOT NULL,
  `OPERACAO_DISTRIBUIDOR` varchar(255) DEFAULT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK1F0231CC56501954` (`DISTRIBUIDOR_ID`),
  KEY `FK1F0231CC9808F874` (`FORNECEDOR_ID`),
  CONSTRAINT `FK1F0231CC56501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FK1F0231CC9808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribuidor`
--

DROP TABLE IF EXISTS `distribuidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribuidor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACEITA_JURAMENTADO` tinyint(1) DEFAULT NULL,
  `CAPACIDADE_DISTRIBUICAO` decimal(18,4) DEFAULT NULL,
  `CAPACIDADE_RECOLHIMENTO` decimal(18,4) DEFAULT NULL,
  `CODIGO` int(11) NOT NULL,
  `COD_DISTRIBUIDOR_DINAP` varchar(255) DEFAULT NULL,
  `COD_DISTRIBUIDOR_FC` varchar(255) DEFAULT NULL,
  `CONTROLE_ARQUIVO_COBRANCA` bigint(20) DEFAULT NULL,
  `DATA_OPERACAO` date NOT NULL,
  `DESCONTO_COTA_PARA_NEGOCIACAO` decimal(18,4) DEFAULT NULL,
  `EXECUTA_RECOLHIMENTO_PARCIAL` tinyint(1) NOT NULL,
  `FATOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  `FATOR_RELANCAMENTO_PARCIAL` int(11) NOT NULL,
  `FECHAMENTO_DIARIO_EM_ANDAMENTO` tinyint(1) DEFAULT NULL,
  `INFORMACOES_COMPLEMENTARES_PROCURACAO` longtext,
  `INICIO_SEMANA_LANCAMENTO` varchar(255) NOT NULL,
  `INICIO_SEMANA_RECOLHIMENTO` varchar(255) NOT NULL,
  `NEGOCIACAO_ATE_PARCELAS` int(11) DEFAULT NULL,
  `NUM_REPROG_LANCAMENTO` int(11) NOT NULL,
  `OBRIGACAO_FISCAL` varchar(255) DEFAULT NULL,
  `COBRANCA_DATA_PROGRAMADA` int(11) DEFAULT NULL,
  `DIAS_NEGOCIACAO` int(11) DEFAULT NULL,
  `COMPLEMENTO_ADESAO_ENTREGA_BANCA` longtext,
  `UTILIZA_ADESAO_ENTREGA_BANCA` tinyint(1) DEFAULT NULL,
  `AJUSTE_ESTOQUE` tinyint(1) DEFAULT NULL,
  `DEBITO_CREDITO` tinyint(1) DEFAULT NULL,
  `DEVOLUCAO_FORNECEDOR` tinyint(1) DEFAULT NULL,
  `FALTAS_SOBRAS` tinyint(1) DEFAULT NULL,
  `NEGOCIACAO` tinyint(1) DEFAULT NULL,
  `POSTERGACAO_COBRANCA` tinyint(1) DEFAULT NULL,
  `CONFERENCIA_CEGA_ENCALHE` tinyint(1) DEFAULT NULL,
  `CONFERENCIA_CEGA_RECEBIMENTO` tinyint(1) DEFAULT NULL,
  `DIA_RECOLHIMENTO_PRIMEIRO` tinyint(1) DEFAULT NULL,
  `DIA_RECOLHIMENTO_QUARTO` tinyint(1) DEFAULT NULL,
  `DIA_RECOLHIMENTO_QUINTO` tinyint(1) DEFAULT NULL,
  `DIA_RECOLHIMENTO_SEGUNDO` tinyint(1) DEFAULT NULL,
  `DIA_RECOLHIMENTO_TERCEIRO` tinyint(1) DEFAULT NULL,
  `PERMITE_RECOLHER_DIAS_POSTERIORES` tinyint(1) DEFAULT NULL,
  `PARCELAMENTO_DIVIDAS` tinyint(1) DEFAULT NULL,
  `DIAS_SUSPENSO` int(11) DEFAULT NULL,
  `VALOR_CONSIGNADO` decimal(18,4) DEFAULT NULL,
  `NUM_ACUMULO_DIVIDA` int(11) DEFAULT NULL,
  `VALOR_SUSPENSAO` decimal(18,4) DEFAULT NULL,
  `PRAZO_AVISO_PREVIO_VALIDADE_GARANTIA` int(11) DEFAULT NULL,
  `PRAZO_FOLLOW_UP` int(11) DEFAULT NULL,
  `AUTO_PREENCHE_QTDE_PDV` tinyint(1) NOT NULL,
  `QNT_DIAS_REUTILIZACAO_CODIGO_COTA` bigint(20) DEFAULT NULL,
  `QNT_DIAS_VENCIMENTO_VENDA_ENCALHE` int(11) DEFAULT NULL,
  `QTD_DIAS_ENCALHE_ATRASADO_ACEITAVEL` int(11) NOT NULL,
  `QTD_DIAS_LIMITE_PARA_REPROG_LANCAMENTO` int(11) NOT NULL,
  `QTD_DIAS_SUSPENSAO_COTAS` int(11) DEFAULT NULL,
  `REGIME_ESPECIAL` tinyint(1) NOT NULL,
  `REQUER_AUTORIZACAO_ENCALHE_SUPERA_REPARTE` tinyint(1) NOT NULL,
  `SUPERVISIONA_VENDA_NEGATIVA` tinyint(1) DEFAULT NULL,
  `ACEITA_RECOLHIMENTO_PARCIAL_ATRASO` tinyint(1) NOT NULL,
  `TIPO_ATIVIDADE` varchar(255) DEFAULT NULL,
  `TIPO_CONT_CE` varchar(255) DEFAULT NULL,
  `TIPO_IMPRESSAO_CE` varchar(255) DEFAULT NULL,
  `TIPO_IMPRESSAO_INTERFACE_LED` varchar(255) DEFAULT NULL,
  `ARQUIVO_INTERFACE_LED_PICKING_1` varchar(100) DEFAULT NULL,
  `ARQUIVO_INTERFACE_LED_PICKING_2` varchar(100) DEFAULT NULL,
  `ARQUIVO_INTERFACE_LED_PICKING_3` varchar(100) DEFAULT NULL,
  `TIPO_IMPRESSAO_NE_NECA_DANFE` varchar(255) DEFAULT NULL,
  `UTILIZA_CONTROLE_APROVACAO` tinyint(1) DEFAULT NULL,
  `UTILIZA_GARANTIA_PDV` tinyint(1) NOT NULL,
  `UTILIZA_PROCURACAO_ENTREGADORES` tinyint(1) DEFAULT NULL,
  `UTILIZA_SUGESTAO_INCREMENTO` tinyint(1) DEFAULT NULL,
  `VALOR_CONSIGNADO_SUSPENSAO_COTAS` decimal(18,4) DEFAULT NULL,
  `PJ_ID` bigint(20) NOT NULL,
  `PARAMETRO_CONTRATO_COTA_ID` bigint(20) DEFAULT NULL,
  `ACEITA_BAIXA_PGTO_MAIOR` tinyint(1) DEFAULT NULL,
  `ACEITA_BAIXA_PGTO_MENOR` tinyint(1) DEFAULT NULL,
  `ACEITA_BAIXA_PGTO_VENCIDO` tinyint(1) DEFAULT NULL,
  `ASSUNTO_EMAIL_COBRANCA` varchar(255) DEFAULT NULL,
  `MENSAGEM_EMAIL_COBRANCA` varchar(255) DEFAULT NULL,
  `NUM_DIAS_NOVA_COBRANCA` int(11) DEFAULT NULL,
  `PRACA_VERANEIO` tinyint(1) DEFAULT '0',
  `INTERFACES_MATRIZ_EM_EXECUCAO` tinyint(1) NOT NULL DEFAULT '0',
  `DATA_INICIO_INTERFACES_MATRIZ_EM_EXECUCAO` datetime DEFAULT NULL,
  `LEIAUTE_PICKING` varchar(20) DEFAULT NULL,
  `CONSOLIDADO_COTA` tinyint(1) DEFAULT NULL,
  `PARAR_ACUM_DIVIDAS` tinyint(1) DEFAULT '0',
  `SUGERE_SUSPENSAO` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PJ_ID` (`PJ_ID`),
  KEY `FK2BE223AE479FD586` (`PJ_ID`),
  KEY `FK2BE223AEC54E738` (`PARAMETRO_CONTRATO_COTA_ID`),
  CONSTRAINT `FK2BE223AE479FD586` FOREIGN KEY (`PJ_ID`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FK2BE223AEC54E738` FOREIGN KEY (`PARAMETRO_CONTRATO_COTA_ID`) REFERENCES `parametro_contrato_cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribuidor_classificacao_cota`
--

DROP TABLE IF EXISTS `distribuidor_classificacao_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribuidor_classificacao_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COD_CLASSIFICACAO` varchar(255) DEFAULT NULL,
  `VALOR_ATE` decimal(10,2) DEFAULT NULL,
  `VALOR_DE` decimal(8,2) DEFAULT NULL,
  `DISTRIBUIDOR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK71650FAA56501954` (`DISTRIBUIDOR_ID`),
  CONSTRAINT `FK71650FAA56501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribuidor_grid_distribuicao`
--

DROP TABLE IF EXISTS `distribuidor_grid_distribuicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribuidor_grid_distribuicao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COMPLEMENTAR_AUTOMATICO` tinyint(1) DEFAULT NULL,
  `GERACAO_AUTOMATICA_ESTUDO` tinyint(1) DEFAULT NULL,
  `PERCENTUAL_MAXIMO_FIXACAO` int(11) DEFAULT NULL,
  `PRACA_VERANEIO` tinyint(1) DEFAULT NULL,
  `VENDA_MEDIA_MAIS` int(11) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKAB82C44056501954` (`DISTRIBUIDOR_ID`),
  CONSTRAINT `FKAB82C44056501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distribuidor_percentual_excedente`
--

DROP TABLE IF EXISTS `distribuidor_percentual_excedente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distribuidor_percentual_excedente` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EFICIENCIA` varchar(255) DEFAULT NULL,
  `PDV` int(11) NOT NULL,
  `VENDA` int(11) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6A41D9656501954` (`DISTRIBUIDOR_ID`),
  CONSTRAINT `FK6A41D9656501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `divida`
--

DROP TABLE IF EXISTS `divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `divida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACUMULADA` tinyint(1) DEFAULT NULL,
  `DATA` date NOT NULL,
  `STATUS` varchar(255) NOT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `DIVIDA_RAIZ_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `ORIGEM_NEGOCIACAO` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK783670757FFF790E` (`USUARIO_ID`),
  KEY `FK78367075E87BFFEC` (`DIVIDA_RAIZ_ID`),
  KEY `FK78367075C8181F74` (`COTA_ID`),
  CONSTRAINT `FK783670757FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK78367075C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FK78367075E87BFFEC` FOREIGN KEY (`DIVIDA_RAIZ_ID`) REFERENCES `divida` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `divida_consolidado`
--

DROP TABLE IF EXISTS `divida_consolidado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `divida_consolidado` (
  `DIVIDA_ID` bigint(20) DEFAULT NULL,
  `CONSOLIDADO_ID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `editor`
--

DROP TABLE IF EXISTS `editor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATIVO` tinyint(1) NOT NULL,
  `CODIGO` bigint(20) NOT NULL,
  `NOME_CONTATO` varchar(255) DEFAULT NULL,
  `ORIGEM_INTERFACE` tinyint(1) DEFAULT NULL,
  `JURIDICA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK799F156D9218183B` (`JURIDICA_ID`),
  CONSTRAINT `FK799F156D9218183B` FOREIGN KEY (`JURIDICA_ID`) REFERENCES `pessoa` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `editor_fornecedor`
--

DROP TABLE IF EXISTS `editor_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor_fornecedor` (
  `EDITOR_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`EDITOR_ID`,`FORNECEDOR_ID`),
  KEY `FK221C17779808F874` (`FORNECEDOR_ID`),
  KEY `FK221C1777B2A11874` (`EDITOR_ID`),
  CONSTRAINT `FK221C17779808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK221C1777B2A11874` FOREIGN KEY (`EDITOR_ID`) REFERENCES `editor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endereco`
--

DROP TABLE IF EXISTS `endereco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endereco` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BAIRRO` varchar(60) DEFAULT NULL,
  `CEP` varchar(9) DEFAULT NULL,
  `CIDADE` varchar(60) DEFAULT NULL,
  `CODIGO_BAIRRO` int(11) DEFAULT NULL,
  `CODIGO_CIDADE_IBGE` int(11) DEFAULT NULL,
  `CODIGO_UF` int(11) DEFAULT NULL,
  `COMPLEMENTO` varchar(60) DEFAULT NULL,
  `LOGRADOURO` varchar(60) DEFAULT NULL,
  `NUMERO` varchar(60) DEFAULT NULL,
  `TIPO_LOGRADOURO` varchar(255) DEFAULT NULL,
  `UF` varchar(2) DEFAULT NULL,
  `PESSOA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK95D357C99B8CB634` (`PESSOA_ID`),
  CONSTRAINT `FK95D357C99B8CB634` FOREIGN KEY (`PESSOA_ID`) REFERENCES `pessoa` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endereco_cota`
--

DROP TABLE IF EXISTS `endereco_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endereco_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_ENDERECO` varchar(255) NOT NULL,
  `ENDERECO_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKE8037E8FAE761C74` (`ENDERECO_ID`),
  KEY `FKE8037E8FC8181F74` (`COTA_ID`),
  CONSTRAINT `FKE8037E8FAE761C74` FOREIGN KEY (`ENDERECO_ID`) REFERENCES `endereco` (`ID`),
  CONSTRAINT `FKE8037E8FC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endereco_distribuidor`
--

DROP TABLE IF EXISTS `endereco_distribuidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endereco_distribuidor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_ENDERECO` varchar(255) NOT NULL,
  `ENDERECO_ID` bigint(20) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DISTRIBUIDOR_ID` (`DISTRIBUIDOR_ID`),
  KEY `FK1E36E464AE761C74` (`ENDERECO_ID`),
  KEY `FK1E36E46456501954` (`DISTRIBUIDOR_ID`),
  CONSTRAINT `FK1E36E46456501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FK1E36E464AE761C74` FOREIGN KEY (`ENDERECO_ID`) REFERENCES `endereco` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endereco_editor`
--

DROP TABLE IF EXISTS `endereco_editor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endereco_editor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_ENDERECO` varchar(255) NOT NULL,
  `ENDERECO_ID` bigint(20) NOT NULL,
  `EDITOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKF7E816A3AE761C74` (`ENDERECO_ID`),
  KEY `FKF7E816A3B2A11874` (`EDITOR_ID`),
  CONSTRAINT `FKF7E816A3AE761C74` FOREIGN KEY (`ENDERECO_ID`) REFERENCES `endereco` (`ID`),
  CONSTRAINT `FKF7E816A3B2A11874` FOREIGN KEY (`EDITOR_ID`) REFERENCES `editor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endereco_entregador`
--

DROP TABLE IF EXISTS `endereco_entregador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endereco_entregador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_ENDERECO` varchar(255) NOT NULL,
  `ENDERECO_ID` bigint(20) NOT NULL,
  `ENTREGADOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKC8E5F6C5AE761C74` (`ENDERECO_ID`),
  KEY `FKC8E5F6C583B33034` (`ENTREGADOR_ID`),
  CONSTRAINT `FKC8E5F6C583B33034` FOREIGN KEY (`ENTREGADOR_ID`) REFERENCES `entregador` (`ID`),
  CONSTRAINT `FKC8E5F6C5AE761C74` FOREIGN KEY (`ENDERECO_ID`) REFERENCES `endereco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endereco_fiador`
--

DROP TABLE IF EXISTS `endereco_fiador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endereco_fiador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_ENDERECO` varchar(255) NOT NULL,
  `ENDERECO_ID` bigint(20) NOT NULL,
  `FIADOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKF9DF85BFAE761C74` (`ENDERECO_ID`),
  KEY `FKF9DF85BF8DC372F4` (`FIADOR_ID`),
  CONSTRAINT `FKF9DF85BF8DC372F4` FOREIGN KEY (`FIADOR_ID`) REFERENCES `fiador` (`ID`),
  CONSTRAINT `FKF9DF85BFAE761C74` FOREIGN KEY (`ENDERECO_ID`) REFERENCES `endereco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endereco_fornecedor`
--

DROP TABLE IF EXISTS `endereco_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endereco_fornecedor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_ENDERECO` varchar(255) NOT NULL,
  `ENDERECO_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKADE2039BAE761C74` (`ENDERECO_ID`),
  KEY `FKADE2039B9808F874` (`FORNECEDOR_ID`),
  CONSTRAINT `FKADE2039B9808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FKADE2039BAE761C74` FOREIGN KEY (`ENDERECO_ID`) REFERENCES `endereco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endereco_pdv`
--

DROP TABLE IF EXISTS `endereco_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endereco_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_ENDERECO` varchar(255) NOT NULL,
  `ENDERECO_ID` bigint(20) NOT NULL,
  `PDV_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK498CAF6CAE761C74` (`ENDERECO_ID`),
  KEY `FK498CAF6CA65B70F4` (`PDV_ID`),
  CONSTRAINT `FK498CAF6CA65B70F4` FOREIGN KEY (`PDV_ID`) REFERENCES `pdv` (`ID`),
  CONSTRAINT `FK498CAF6CAE761C74` FOREIGN KEY (`ENDERECO_ID`) REFERENCES `endereco` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `endereco_transportador`
--

DROP TABLE IF EXISTS `endereco_transportador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endereco_transportador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_ENDERECO` varchar(255) NOT NULL,
  `ENDERECO_ID` bigint(20) NOT NULL,
  `TRANSPORTADOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKBAE045D9D90B1440` (`TRANSPORTADOR_ID`),
  KEY `FKBAE045D9AE761C74` (`ENDERECO_ID`),
  CONSTRAINT `FKBAE045D9AE761C74` FOREIGN KEY (`ENDERECO_ID`) REFERENCES `endereco` (`ID`),
  CONSTRAINT `FKBAE045D9D90B1440` FOREIGN KEY (`TRANSPORTADOR_ID`) REFERENCES `transportador` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entregador`
--

DROP TABLE IF EXISTS `entregador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entregador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `COMISSIONADO` tinyint(1) NOT NULL,
  `INICIO_ATIVIDADE` date NOT NULL,
  `PERCENTUAL_COMISSAO` decimal(18,4) DEFAULT NULL,
  `PERCENTUAL_FATURAMENTO` decimal(18,4) DEFAULT NULL,
  `PROCURACAO` tinyint(1) NOT NULL,
  `TAXA_FIXA` decimal(18,4) DEFAULT NULL,
  `PESSOA_ID` bigint(20) NOT NULL,
  `ROTA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`),
  KEY `FK860C808F9B8CB634` (`PESSOA_ID`),
  KEY `FK860C808FE19C69D4` (`ROTA_ID`),
  CONSTRAINT `FK860C808F9B8CB634` FOREIGN KEY (`PESSOA_ID`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FK860C808FE19C69D4` FOREIGN KEY (`ROTA_ID`) REFERENCES `rota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estoque_produto`
--

DROP TABLE IF EXISTS `estoque_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estoque_produto` (
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
  UNIQUE KEY `PRODUTO_EDICAO_ID` (`PRODUTO_EDICAO_ID`),
  KEY `FKE9F637F2A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FKE9F637F2A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estoque_produto_cota`
--

DROP TABLE IF EXISTS `estoque_produto_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estoque_produto_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QTDE_DEVOLVIDA` decimal(18,4) DEFAULT NULL,
  `QTDE_RECEBIDA` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `COTA_ID` (`COTA_ID`,`PRODUTO_EDICAO_ID`),
  KEY `FK5CA35006C8181F74` (`COTA_ID`),
  KEY `FK5CA35006A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK5CA35006A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK5CA35006C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estoque_produto_fila`
--

DROP TABLE IF EXISTS `estoque_produto_fila`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estoque_produto_fila` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TIPO_ESTOQUE` varchar(255) NOT NULL DEFAULT '0',
  `COTA_ID` bigint(20) DEFAULT '0',
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL DEFAULT '0',
  `QTDE` decimal(18,4) NOT NULL DEFAULT '0.0000',
  `OPERACAO_ESTOQUE` varchar(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_ESTOQUE_PRODUTO_FILA_produto_edicao` (`PRODUTO_EDICAO_ID`),
  KEY `FK_ESTOQUE_PRODUTO_FILA_cota` (`COTA_ID`),
  CONSTRAINT `FK_ESTOQUE_PRODUTO_FILA_cota` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FK_ESTOQUE_PRODUTO_FILA_produto_edicao` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estqbox`
--

DROP TABLE IF EXISTS `estqbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estqbox` (
  `linha_vazia` varchar(1) DEFAULT NULL,
  `tipo` int(11) DEFAULT NULL,
  `box` int(11) DEFAULT NULL,
  `nome_box` varchar(10) DEFAULT NULL,
  `produto` varchar(8) DEFAULT NULL,
  `edicao` int(11) DEFAULT NULL,
  `nome_produto` varchar(45) DEFAULT NULL,
  `quantidade` int(11) DEFAULT NULL,
  `produto_edicao_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estrategia`
--

DROP TABLE IF EXISTS `estrategia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estrategia` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ABRANGENCIA` decimal(19,2) DEFAULT NULL,
  `OPORTUNIDADE_VENDA` varchar(255) DEFAULT NULL,
  `PERIODO` int(11) DEFAULT NULL,
  `REPARTE_MINIMO` decimal(19,2) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK65485699A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK65485699A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estrategia_base_distribuicao`
--

DROP TABLE IF EXISTS `estrategia_base_distribuicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estrategia_base_distribuicao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PERIODO_EDICAO` int(11) DEFAULT NULL,
  `PESO` int(11) NOT NULL,
  `ESTRATEGIA_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK805710C02ADC726F` (`ESTRATEGIA_ID`),
  KEY `FK805710C0A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK805710C02ADC726F` FOREIGN KEY (`ESTRATEGIA_ID`) REFERENCES `estrategia` (`ID`),
  CONSTRAINT `FK805710C0A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudo`
--

DROP TABLE IF EXISTS `estudo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudo` (
  `ID` bigint(20) NOT NULL,
  `data_alteracao` datetime DEFAULT NULL,
  `data_cadastro` datetime NOT NULL,
  `DATA_LANCAMENTO` date DEFAULT NULL,
  `DISTRIBUICAO_POR_MULTIPLOS` int(11) DEFAULT NULL,
  `ESTUDO_ORIGEM_COPIA` bigint(20) DEFAULT NULL,
  `LANCAMENTO_ID` bigint(20) DEFAULT NULL,
  `PACOTE_PADRAO` decimal(19,2) DEFAULT NULL,
  `QTDE_REPARTE` decimal(19,2) NOT NULL,
  `REPARTE_DISTRIBUIR` decimal(19,2) DEFAULT NULL,
  `SOBRA` decimal(19,2) DEFAULT NULL,
  `STATUS` varchar(255) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  `DADOS_VENDA_MEDIA` text,
  `tipo_geracao_estudo` varchar(255) DEFAULT NULL,
  `ABRANGENCIA` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7A77787A7FFF790E` (`USUARIO_ID`),
  KEY `FK7A77787AA53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK7A77787A7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK7A77787AA53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudo_bonificacoes`
--

DROP TABLE IF EXISTS `estudo_bonificacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudo_bonificacoes` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BONIFICACAO` int(11) DEFAULT NULL,
  `COMPONENTE` varchar(255) DEFAULT NULL,
  `ELEMENTO` varchar(255) DEFAULT NULL,
  `REPARTE_MINIMO` decimal(19,2) DEFAULT NULL,
  `TODAS_AS_COTAS` tinyint(1) DEFAULT NULL,
  `ESTUDO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ESTUDO_ID` (`ESTUDO_ID`),
  KEY `FK33426C685E727B07` (`ESTUDO_ID`),
  CONSTRAINT `FK33426C685E727B07` FOREIGN KEY (`ESTUDO_ID`) REFERENCES `estudo_gerado` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudo_cota`
--

DROP TABLE IF EXISTS `estudo_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudo_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CLASSIFICACAO` varchar(255) DEFAULT NULL,
  `COTA_NOVA` tinyint(1) DEFAULT NULL,
  `INDICE_CORRECAO_TENDENCIA` decimal(19,2) DEFAULT NULL,
  `INDICE_VENDA_CRESCENTE` decimal(19,2) DEFAULT NULL,
  `MIX` int(11) DEFAULT NULL,
  `PERCENTUAL_ENCALHE_MAXIMO` decimal(19,2) DEFAULT NULL,
  `QTDE_EFETIVA` decimal(19,2) DEFAULT NULL,
  `QTDE_PREVISTA` decimal(19,2) DEFAULT NULL,
  `QUANTIDADE_PDVS` int(11) DEFAULT NULL,
  `REPARTE` decimal(19,2) DEFAULT NULL,
  `REPARTE_JURAMENTADO_A_FATURAR` decimal(19,2) DEFAULT NULL,
  `REPARTE_MAXIMO` decimal(19,2) DEFAULT NULL,
  `REPARTE_MINIMO` decimal(19,2) DEFAULT NULL,
  `TIPO_ESTUDO` varchar(20) DEFAULT 'NORMAL',
  `VENDA_MEDIA` decimal(19,2) DEFAULT NULL,
  `VENDA_MEDIA_MAIS_N` int(11) DEFAULT NULL,
  `VENDA_MEDIA_NOMINAL` decimal(19,2) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `ESTUDO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK12FD247EC8181F74` (`COTA_ID`),
  KEY `FK12FD247ED3010D4F` (`ESTUDO_ID`),
  CONSTRAINT `FK12FD247EC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FK12FD247ED3010D4F` FOREIGN KEY (`ESTUDO_ID`) REFERENCES `estudo` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudo_cota_gerado`
--

DROP TABLE IF EXISTS `estudo_cota_gerado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudo_cota_gerado` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CLASSIFICACAO` varchar(255) DEFAULT NULL,
  `COTA_NOVA` tinyint(1) DEFAULT NULL,
  `INDICE_CORRECAO_TENDENCIA` decimal(19,2) DEFAULT NULL,
  `INDICE_VENDA_CRESCENTE` decimal(19,2) DEFAULT NULL,
  `MIX` int(11) DEFAULT NULL,
  `PERCENTUAL_ENCALHE_MAXIMO` decimal(19,2) DEFAULT NULL,
  `QTDE_EFETIVA` decimal(19,2) DEFAULT NULL,
  `QTDE_PREVISTA` decimal(19,2) DEFAULT NULL,
  `QUANTIDADE_PDVS` int(11) DEFAULT NULL,
  `REPARTE` decimal(19,2) DEFAULT NULL,
  `REPARTE_JURAMENTADO_A_FATURAR` decimal(19,2) DEFAULT NULL,
  `REPARTE_MAXIMO` decimal(19,2) DEFAULT NULL,
  `REPARTE_MINIMO` decimal(19,2) DEFAULT NULL,
  `TIPO_ESTUDO` varchar(20) DEFAULT 'NORMAL',
  `VENDA_MEDIA` decimal(19,2) DEFAULT NULL,
  `VENDA_MEDIA_MAIS_N` int(11) DEFAULT NULL,
  `VENDA_MEDIA_NOMINAL` decimal(19,2) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `ESTUDO_ID` bigint(20) NOT NULL,
  `REPARTE_INICIAL` decimal(19,0) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKF0111BB9C8181F74` (`COTA_ID`),
  KEY `FKF0111BB95E727B07` (`ESTUDO_ID`),
  CONSTRAINT `FKF0111BB95E727B07` FOREIGN KEY (`ESTUDO_ID`) REFERENCES `estudo_gerado` (`ID`),
  CONSTRAINT `FKF0111BB9C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudo_gerado`
--

DROP TABLE IF EXISTS `estudo_gerado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudo_gerado` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_alteracao` datetime DEFAULT NULL,
  `data_cadastro` datetime NOT NULL,
  `DATA_LANCAMENTO` date DEFAULT NULL,
  `DISTRIBUICAO_POR_MULTIPLOS` int(11) DEFAULT NULL,
  `ESTUDO_ORIGEM_COPIA` bigint(20) DEFAULT NULL,
  `LANCAMENTO_ID` bigint(20) DEFAULT NULL,
  `LIBERADO` tinyint(1) DEFAULT NULL,
  `PACOTE_PADRAO` decimal(19,2) DEFAULT NULL,
  `QTDE_REPARTE` decimal(19,2) NOT NULL,
  `REPARTE_DISTRIBUIR` decimal(19,2) DEFAULT NULL,
  `SOBRA` decimal(19,2) DEFAULT NULL,
  `STATUS` varchar(255) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  `DADOS_VENDA_MEDIA` text,
  `REPARTE_MINIMO` bigint(20) DEFAULT NULL,
  `tipo_geracao_estudo` varchar(255) DEFAULT NULL,
  `ABRANGENCIA` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK4E8B943D7FFF790E` (`USUARIO_ID`),
  KEY `FK4E8B943DA53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK4E8B943D7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK4E8B943DA53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudo_pdv`
--

DROP TABLE IF EXISTS `estudo_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudo_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `REPARTE` decimal(19,2) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `ESTUDO_ID` bigint(20) NOT NULL,
  `PDV_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8DF0F9DC8181F74` (`COTA_ID`),
  KEY `FK8DF0F9D5E727B07` (`ESTUDO_ID`),
  KEY `FK8DF0F9DA65B70F4` (`PDV_ID`),
  CONSTRAINT `FK8DF0F9D5E727B07` FOREIGN KEY (`ESTUDO_ID`) REFERENCES `estudo_gerado` (`ID`),
  CONSTRAINT `FK8DF0F9DA65B70F4` FOREIGN KEY (`PDV_ID`) REFERENCES `pdv` (`ID`),
  CONSTRAINT `FK8DF0F9DC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudo_produto_edicao`
--

DROP TABLE IF EXISTS `estudo_produto_edicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudo_produto_edicao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `INDICE_CORRECAO` decimal(19,2) DEFAULT NULL,
  `REPARTE` decimal(19,2) NOT NULL,
  `VENDA` decimal(19,2) NOT NULL,
  `VENDA_CORRIGIDA` decimal(19,2) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `ESTUDO_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK70621A52C8181F74` (`COTA_ID`),
  KEY `FK70621A525E727B07` (`ESTUDO_ID`),
  KEY `FK70621A52A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK70621A525E727B07` FOREIGN KEY (`ESTUDO_ID`) REFERENCES `estudo_gerado` (`ID`),
  CONSTRAINT `FK70621A52A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK70621A52C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estudo_produto_edicao_base`
--

DROP TABLE IF EXISTS `estudo_produto_edicao_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudo_produto_edicao_base` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COLECAO` bigint(20) NOT NULL,
  `EDICAO_ABERTA` bigint(20) NOT NULL,
  `PARCIAL` bigint(20) NOT NULL,
  `PERIODO_PARCIAL` bigint(20) DEFAULT NULL,
  `PESO` bigint(20) NOT NULL,
  `VENDA_CORRIGIDA` decimal(19,2) DEFAULT NULL,
  `ESTUDO_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6C34489E5E727B07` (`ESTUDO_ID`),
  KEY `FK6C34489EA53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK6C34489E5E727B07` FOREIGN KEY (`ESTUDO_ID`) REFERENCES `estudo_gerado` (`ID`),
  CONSTRAINT `FK6C34489EA53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evento_execucao`
--

DROP TABLE IF EXISTS `evento_execucao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento_execucao` (
  `ID` bigint(20) NOT NULL,
  `DESCRICAO` varchar(100) NOT NULL,
  `NOME` varchar(30) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `excecao_produto_cota`
--

DROP TABLE IF EXISTS `excecao_produto_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `excecao_produto_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `TIPO_EXCECAO` varchar(255) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `PRODUTO_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `TIPO_CLASSIFICACAO_PRODUTO_ID` bigint(20) DEFAULT NULL,
  `CODIGO_ICD` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKA42FF9A27FFF790E` (`USUARIO_ID`),
  KEY `FKA42FF9A2C8181F74` (`COTA_ID`),
  KEY `FKA42FF9A2C5C16480` (`PRODUTO_ID`),
  CONSTRAINT `FKA42FF9A27FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKA42FF9A2C5C16480` FOREIGN KEY (`PRODUTO_ID`) REFERENCES `produto` (`ID`),
  CONSTRAINT `FKA42FF9A2C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expedicao`
--

DROP TABLE IF EXISTS `expedicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expedicao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_EXPEDICAO` datetime NOT NULL,
  `USUARIO` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKAD6AB024EA1BE470` (`USUARIO`),
  KEY `ndx_data_expedicao` (`DATA_EXPEDICAO`),
  CONSTRAINT `FKAD6AB024EA1BE470` FOREIGN KEY (`USUARIO`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario`
--

DROP TABLE IF EXISTS `fechamento_diario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_FECHAMENTO` date DEFAULT NULL,
  `USUARIO` tinyblob,
  `USUARIO_ID` bigint(20) NOT NULL,
  `DATA_CRIACAO` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DATA_FECHAMENTO` (`DATA_FECHAMENTO`),
  KEY `FK23D758477FFF790E` (`USUARIO_ID`),
  CONSTRAINT `FK23D758477FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_consolidado_cota`
--

DROP TABLE IF EXISTS `fechamento_diario_consolidado_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_consolidado_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QNT_ATIVOS` int(11) DEFAULT NULL,
  `QNT_AUSENTE_ENCALHE` int(11) DEFAULT NULL,
  `QNT_AUSENTE_REPARTE` int(11) DEFAULT NULL,
  `QNT_INATIVAS` int(11) DEFAULT NULL,
  `QNT_NOVOS` int(11) DEFAULT NULL,
  `QNT_TOTAL` int(11) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK40875A0D8CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK40875A0D8CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_consolidado_divida`
--

DROP TABLE IF EXISTS `fechamento_diario_consolidado_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_consolidado_divida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TIPO_DIVIDA` varchar(255) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3D7A65A98CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK3D7A65A98CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_consolidado_encalhe`
--

DROP TABLE IF EXISTS `fechamento_diario_consolidado_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_consolidado_encalhe` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SALDO` decimal(18,4) DEFAULT NULL,
  `VALOR_FALTA_EM` decimal(18,4) DEFAULT NULL,
  `VALOR_FISICO` decimal(18,4) DEFAULT NULL,
  `VALOR_JURAMENTADO` decimal(18,4) DEFAULT NULL,
  `VALOR_LOGICO` decimal(18,4) DEFAULT NULL,
  `VALOR_SOBRA_EM` decimal(18,4) DEFAULT NULL,
  `VALOR_VENDA` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FECHAMENTO_DIARIO_ID` (`FECHAMENTO_DIARIO_ID`),
  KEY `FKAE31836E8CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FKAE31836E8CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_consolidado_reparte`
--

DROP TABLE IF EXISTS `fechamento_diario_consolidado_reparte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_consolidado_reparte` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VALOR_DIFERENCA` decimal(18,4) DEFAULT NULL,
  `VALOR_DISTRIBUIDO` decimal(18,4) DEFAULT NULL,
  `VALOR_FALTAS` decimal(18,4) DEFAULT NULL,
  `VALOR_REPARTE` decimal(18,4) DEFAULT NULL,
  `SOBRA_DISTRIBUIDA` decimal(18,4) DEFAULT NULL,
  `VALOR_SOBRAS` decimal(18,4) DEFAULT NULL,
  `VALOR_TRANSFERIDO` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  `VALOR_A_DISTRIBUIR` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FECHAMENTO_DIARIO_ID` (`FECHAMENTO_DIARIO_ID`),
  KEY `FK4F3E31AB8CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK4F3E31AB8CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_consolidado_suplementar`
--

DROP TABLE IF EXISTS `fechamento_diario_consolidado_suplementar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_consolidado_suplementar` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VALOR_ESTOQUE_LOGICO` decimal(18,4) DEFAULT NULL,
  `VALOR_SALDO` decimal(18,4) DEFAULT NULL,
  `VALOR_TRANSFERENCIA` decimal(18,4) DEFAULT NULL,
  `VALOR_VENDAS` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FECHAMENTO_DIARIO_ID` (`FECHAMENTO_DIARIO_ID`),
  KEY `FKD0D1E0428CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FKD0D1E0428CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_cota`
--

DROP TABLE IF EXISTS `fechamento_diario_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOME_COTA` varchar(255) DEFAULT NULL,
  `NUMERO_COTA` int(11) DEFAULT NULL,
  `TIPO_DETALHE_COTA` varchar(255) DEFAULT NULL,
  `FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK53821CD11F45E509` (`FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID`),
  CONSTRAINT `FK53821CD11F45E509` FOREIGN KEY (`FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID`) REFERENCES `fechamento_diario_consolidado_cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_diferenca`
--

DROP TABLE IF EXISTS `fechamento_diario_diferenca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_diferenca` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO_PRODUTO` varchar(255) NOT NULL,
  `DATA_LANCAMENTO` date NOT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  `MOTIVO_APROVACAO` varchar(255) DEFAULT NULL,
  `NOME_PRODUTO` varchar(255) NOT NULL,
  `NUMERO_EDICAO` bigint(20) NOT NULL,
  `QTDE_EXEMPLARES` decimal(18,4) DEFAULT NULL,
  `STATUS_APROVACAO` varchar(255) DEFAULT NULL,
  `TIPO_DIFERENCA` varchar(255) NOT NULL,
  `TOTAL` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK5F1D69D8CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK5F1D69D8CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_divida`
--

DROP TABLE IF EXISTS `fechamento_diario_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_divida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BANCO` varchar(255) DEFAULT NULL,
  `DATA_VENCIMENTO` date DEFAULT NULL,
  `ID_DIVIDA` bigint(20) DEFAULT NULL,
  `NOME_COTA` varchar(255) DEFAULT NULL,
  `NOSSO_NUMERO` varchar(255) DEFAULT NULL,
  `NUMERO_CONTA` varchar(20) DEFAULT NULL,
  `NUMERO_COTA` bigint(20) DEFAULT NULL,
  `FORMA_PAGAMENTO` varchar(255) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7CCF876D3758FC9` (`FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID`),
  CONSTRAINT `FK7CCF876D3758FC9` FOREIGN KEY (`FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID`) REFERENCES `fechamento_diario_consolidado_divida` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_lancamento_encalhe`
--

DROP TABLE IF EXISTS `fechamento_diario_lancamento_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_lancamento_encalhe` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QUANTIDADE` int(11) DEFAULT NULL,
  `QNT_DIFERENCA` int(11) DEFAULT NULL,
  `QNT_VENDA_ENCALHE` int(11) DEFAULT NULL,
  `FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID` bigint(20) NOT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  `QNT_LOGICO_JURAMENTADO` bigint(20) DEFAULT NULL,
  `QNT_FISICO` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK15EC5355D2FE34B7` (`ID_PRODUTO_EDICAO`),
  KEY `FK15EC5355325F0A2B` (`FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID`),
  CONSTRAINT `FK15EC5355325F0A2B` FOREIGN KEY (`FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID`) REFERENCES `fechamento_diario_consolidado_encalhe` (`ID`),
  CONSTRAINT `FK15EC5355D2FE34B7` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_lancamento_reparte`
--

DROP TABLE IF EXISTS `fechamento_diario_lancamento_reparte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_lancamento_reparte` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QNT_A_DISTRIBUIR` int(11) DEFAULT NULL,
  `QNT_DIFERENCA` int(11) DEFAULT NULL,
  `QNT_DISTRIBUIDO` int(11) DEFAULT NULL,
  `QNT_FALTA_EM` int(11) DEFAULT NULL,
  `QNT_REPARTE` int(11) DEFAULT NULL,
  `QNT_SOBRA_DISTRIBUIDO` int(11) DEFAULT NULL,
  `QNT_SOBRA_EM` int(11) DEFAULT NULL,
  `QNT_TRANSFERENCIA` int(11) DEFAULT NULL,
  `FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID` bigint(20) NOT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKB6F90192D2FE34B7` (`ID_PRODUTO_EDICAO`),
  KEY `FKB6F901925614050B` (`FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID`),
  CONSTRAINT `FKB6F901925614050B` FOREIGN KEY (`FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID`) REFERENCES `fechamento_diario_consolidado_reparte` (`ID`),
  CONSTRAINT `FKB6F90192D2FE34B7` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_lancamento_suplementar`
--

DROP TABLE IF EXISTS `fechamento_diario_lancamento_suplementar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_lancamento_suplementar` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QNT_CONTABILIZADA` decimal(18,4) DEFAULT NULL,
  `QNT_LOGICO` bigint(20) DEFAULT NULL,
  `QNT_VENDA` bigint(20) DEFAULT NULL,
  `QNT_TRANSFERENCIA_ENTRADA` bigint(20) DEFAULT NULL,
  `QNT_TRANSFERENCIA_SAIDA` bigint(20) DEFAULT NULL,
  `SALDO` bigint(20) DEFAULT NULL,
  `FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID` bigint(20) NOT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3D4464A9D2FE34B7` (`ID_PRODUTO_EDICAO`),
  KEY `FK3D4464A9EE73E92B` (`FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID`),
  CONSTRAINT `FK3D4464A9D2FE34B7` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK3D4464A9EE73E92B` FOREIGN KEY (`FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID`) REFERENCES `fechamento_diario_consolidado_suplementar` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_movimento_vendas_encalhe`
--

DROP TABLE IF EXISTS `fechamento_diario_movimento_vendas_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_movimento_vendas_encalhe` (
  `ID` bigint(20) NOT NULL,
  `DATA_VENCIMENTO` date DEFAULT NULL,
  `QUANTIDADE` decimal(18,4) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  `FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK35E76390D2FE34B7b6674115` (`ID_PRODUTO_EDICAO`),
  KEY `FKB6674115325F0A2B` (`FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID`),
  CONSTRAINT `FK35E76390D2FE34B7b6674115` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FKB6674115325F0A2B` FOREIGN KEY (`FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID`) REFERENCES `fechamento_diario_consolidado_encalhe` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_movimento_vendas_suplementar`
--

DROP TABLE IF EXISTS `fechamento_diario_movimento_vendas_suplementar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_movimento_vendas_suplementar` (
  `ID` bigint(20) NOT NULL,
  `DATA_VENCIMENTO` date DEFAULT NULL,
  `QUANTIDADE` decimal(18,4) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  `FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK35E76390D2FE34B727127269` (`ID_PRODUTO_EDICAO`),
  KEY `FK27127269EE73E92B` (`FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID`),
  CONSTRAINT `FK27127269EE73E92B` FOREIGN KEY (`FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID`) REFERENCES `fechamento_diario_consolidado_suplementar` (`ID`),
  CONSTRAINT `FK35E76390D2FE34B727127269` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_resumo_consignado`
--

DROP TABLE IF EXISTS `fechamento_diario_resumo_consignado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_resumo_consignado` (
  `TIPO_RESUMO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SALDO_ANTERIOR` decimal(18,4) DEFAULT NULL,
  `SALDO_ATUAL` decimal(18,4) DEFAULT NULL,
  `VALOR_ENTRADA` decimal(18,4) DEFAULT NULL,
  `VALOR_SAIDA` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  `VALOR_CE` decimal(18,4) DEFAULT NULL,
  `VALOR_OUTRAS_MOVIMENTACOES_ENTRADA` decimal(18,4) DEFAULT NULL,
  `VALOR_EXPEDICAO` decimal(18,4) DEFAULT NULL,
  `VALOR_OUTRAS_MOVIMENTACOES_SAIDA` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK29E6791D8CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK29E6791D8CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_resumo_consolidado_divida`
--

DROP TABLE IF EXISTS `fechamento_diario_resumo_consolidado_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_resumo_consolidado_divida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FORMA_PAGAMENTO` varchar(255) DEFAULT NULL,
  `VALOR_INADIMPLENTE` decimal(18,4) DEFAULT NULL,
  `VALOR_PAGO` decimal(18,4) DEFAULT NULL,
  `VALOR_TOTAL` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK806BF1C13758FC9` (`FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID`),
  CONSTRAINT `FK806BF1C13758FC9` FOREIGN KEY (`FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID`) REFERENCES `fechamento_diario_consolidado_divida` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_diario_resumo_estoque`
--

DROP TABLE IF EXISTS `fechamento_diario_resumo_estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_diario_resumo_estoque` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QNT_EXEMPLARES` int(11) DEFAULT NULL,
  `QNT_PRODUTO` int(11) DEFAULT NULL,
  `TIPO_ESTOQUE` varchar(255) DEFAULT NULL,
  `VALOR_TOTAL` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKC8BE9488CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FKC8BE9488CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_encalhe`
--

DROP TABLE IF EXISTS `fechamento_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_encalhe` (
  `DATA_ENCALHE` date NOT NULL,
  `QUANTIDADE` bigint(20) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`DATA_ENCALHE`,`PRODUTO_EDICAO_ID`),
  KEY `FK9496A657A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK9496A657A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamento_encalhe_box`
--

DROP TABLE IF EXISTS `fechamento_encalhe_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamento_encalhe_box` (
  `QUANTIDADE` bigint(20) DEFAULT NULL,
  `DATA_ENCALHE` date NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `BOX_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`BOX_ID`,`DATA_ENCALHE`,`PRODUTO_EDICAO_ID`),
  KEY `FK81C9D3C3BA6EBE40` (`BOX_ID`),
  KEY `FK81C9D3C317B54D97` (`DATA_ENCALHE`,`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK81C9D3C317B54D97` FOREIGN KEY (`DATA_ENCALHE`, `PRODUTO_EDICAO_ID`) REFERENCES `fechamento_encalhe` (`DATA_ENCALHE`, `PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK81C9D3C3BA6EBE40` FOREIGN KEY (`BOX_ID`) REFERENCES `box` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fechamentodiariomovimentovenda`
--

DROP TABLE IF EXISTS `fechamentodiariomovimentovenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fechamentodiariomovimentovenda` (
  `ID` bigint(20) NOT NULL,
  `DATA_VENCIMENTO` date DEFAULT NULL,
  `QUANTIDADE` decimal(18,4) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK35E76390D2FE34B7` (`ID_PRODUTO_EDICAO`),
  CONSTRAINT `FK35E76390D2FE34B7` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feriado`
--

DROP TABLE IF EXISTS `feriado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feriado` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` date NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  `IND_EFETUA_COBRANCA` tinyint(1) DEFAULT NULL,
  `IND_OPERA` tinyint(1) DEFAULT NULL,
  `IND_REPETE_ANUALMENTE` tinyint(1) DEFAULT NULL,
  `ORIGEM` varchar(255) DEFAULT NULL,
  `TIPO_FERIADO` varchar(255) DEFAULT NULL,
  `LOCALIDADE_ID` bigint(20) DEFAULT NULL,
  `UFE_SG` varchar(4) DEFAULT NULL,
  `LOCALIDADE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DATA` (`DATA`,`LOCALIDADE_ID`,`UFE_SG`,`TIPO_FERIADO`),
  KEY `FKF15849969BB6EC08` (`LOCALIDADE_ID`),
  KEY `FKF1584996B31F2A81` (`UFE_SG`),
  CONSTRAINT `FKF15849969BB6EC08` FOREIGN KEY (`LOCALIDADE_ID`) REFERENCES `log_localidade` (`LOC_NU`),
  CONSTRAINT `FKF1584996B31F2A81` FOREIGN KEY (`UFE_SG`) REFERENCES `log_faixa_uf` (`UFE_SG`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fiador`
--

DROP TABLE IF EXISTS `fiador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fiador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `INICIO_ATIVIDADE` date DEFAULT NULL,
  `PESSOA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PESSOA_ID` (`PESSOA_ID`),
  KEY `FK7B9684899B8CB634` (`PESSOA_ID`),
  CONSTRAINT `FK7B9684899B8CB634` FOREIGN KEY (`PESSOA_ID`) REFERENCES `pessoa` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fiador_socio`
--

DROP TABLE IF EXISTS `fiador_socio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fiador_socio` (
  `FIADOR_ID` bigint(20) NOT NULL,
  `SOCIO_ID` bigint(20) NOT NULL,
  KEY `FK9FAC29977CB7F32E` (`SOCIO_ID`),
  KEY `FK9FAC29978DC372F4` (`FIADOR_ID`),
  CONSTRAINT `FK9FAC29977CB7F32E` FOREIGN KEY (`SOCIO_ID`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FK9FAC29978DC372F4` FOREIGN KEY (`FIADOR_ID`) REFERENCES `fiador` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fixacao_reparte`
--

DROP TABLE IF EXISTS `fixacao_reparte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fixacao_reparte` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_HORA` datetime DEFAULT NULL,
  `ED_FINAL` int(11) DEFAULT NULL,
  `ED_INICIAL` int(11) DEFAULT NULL,
  `ED_ATENDIDAS` int(11) DEFAULT NULL,
  `QTDE_EDICOES` int(11) DEFAULT NULL,
  `QTDE_EXEMPLARES` int(11) DEFAULT NULL,
  `ID_COTA` bigint(20) DEFAULT NULL,
  `ID_PRODUTO` bigint(20) DEFAULT NULL,
  `ID_USUARIO` bigint(20) DEFAULT NULL,
  `CODIGO_ICD` varchar(255) DEFAULT NULL,
  `MANTER_FIXA` tinyint(1) DEFAULT NULL,
  `ID_CLASSIFICACAO_EDICAO` bigint(20) DEFAULT NULL,
  `DATA_FIXA_CADASTRO_FIXACAO` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKA394AF65F1916CB0` (`ID_COTA`),
  KEY `FKA394AF6593864D4C` (`ID_USUARIO`),
  KEY `FKA394AF65F34F8834` (`ID_PRODUTO`),
  KEY `FKA394AF65917D3254` (`ID_CLASSIFICACAO_EDICAO`),
  CONSTRAINT `FKA394AF65917D3254` FOREIGN KEY (`ID_CLASSIFICACAO_EDICAO`) REFERENCES `tipo_classificacao_produto` (`ID`),
  CONSTRAINT `FKA394AF6593864D4C` FOREIGN KEY (`ID_USUARIO`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKA394AF65F1916CB0` FOREIGN KEY (`ID_COTA`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FKA394AF65F34F8834` FOREIGN KEY (`ID_PRODUTO`) REFERENCES `produto` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fixacao_reparte_pdv`
--

DROP TABLE IF EXISTS `fixacao_reparte_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fixacao_reparte_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `REPARTE` int(11) DEFAULT NULL,
  `ID_FIXACAO_REPARTE` bigint(20) NOT NULL,
  `ID_PDV` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6160D9089A5F4F5A` (`ID_PDV`),
  KEY `FK6160D9083AE26156` (`ID_FIXACAO_REPARTE`),
  CONSTRAINT `FK6160D9083AE26156` FOREIGN KEY (`ID_FIXACAO_REPARTE`) REFERENCES `fixacao_reparte` (`ID`),
  CONSTRAINT `FK6160D9089A5F4F5A` FOREIGN KEY (`ID_PDV`) REFERENCES `pdv` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `forma_cobranca`
--

DROP TABLE IF EXISTS `forma_cobranca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forma_cobranca` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATIVA` tinyint(1) NOT NULL,
  `AGENCIA_BANCO` bigint(20) DEFAULT NULL,
  `CONTA_BANCO` bigint(20) DEFAULT NULL,
  `DV_AGENCIA_BANCO` varchar(255) DEFAULT NULL,
  `DV_CONTA_BANCO` varchar(255) DEFAULT NULL,
  `NOME_BANCO` varchar(255) DEFAULT NULL,
  `CORRENTISTA` varchar(255) DEFAULT NULL,
  `NUMERO_BANCO` varchar(255) DEFAULT NULL,
  `FORMA_COBRANCA_BOLETO` int(11) DEFAULT NULL,
  `INSTRUCOES` varchar(255) DEFAULT NULL,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `RECEBE_COBRANCA_EMAIL` tinyint(1) DEFAULT NULL,
  `TAXA_JUROS_MENSAL` decimal(18,4) DEFAULT NULL,
  `TAXA_MULTA` decimal(18,4) DEFAULT NULL,
  `TIPO_COBRANCA` varchar(255) NOT NULL,
  `TIPO_FORMA_COBRANCA` varchar(255) NOT NULL,
  `VENCIMENTO_DIA_UTIL` tinyint(1) NOT NULL,
  `BANCO_ID` bigint(20) DEFAULT NULL,
  `PARAMETRO_COBRANCA_COTA_ID` bigint(20) DEFAULT NULL,
  `VALOR_MULTA` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA10CEB0961171A8E` (`PARAMETRO_COBRANCA_COTA_ID`),
  KEY `FKA10CEB09E44516C0` (`BANCO_ID`),
  CONSTRAINT `FKA10CEB0961171A8E` FOREIGN KEY (`PARAMETRO_COBRANCA_COTA_ID`) REFERENCES `parametro_cobranca_cota` (`ID`),
  CONSTRAINT `FKA10CEB09E44516C0` FOREIGN KEY (`BANCO_ID`) REFERENCES `banco` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `forma_cobranca_caucao_liquida`
--

DROP TABLE IF EXISTS `forma_cobranca_caucao_liquida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forma_cobranca_caucao_liquida` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `TIPO_FORMA_COBRANCA` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `forma_cobranca_fornecedor`
--

DROP TABLE IF EXISTS `forma_cobranca_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forma_cobranca_fornecedor` (
  `FORMA_COBRANCA_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`FORMA_COBRANCA_ID`,`FORNECEDOR_ID`),
  KEY `FK8367E85BE34F875B` (`FORMA_COBRANCA_ID`),
  KEY `FK8367E85B9808F874` (`FORNECEDOR_ID`),
  CONSTRAINT `FK8367E85B9808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK8367E85BE34F875B` FOREIGN KEY (`FORMA_COBRANCA_ID`) REFERENCES `forma_cobranca` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `formacobranca_diasdomes`
--

DROP TABLE IF EXISTS `formacobranca_diasdomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formacobranca_diasdomes` (
  `FormaCobranca_id` bigint(20) NOT NULL,
  `diasDoMes` int(11) DEFAULT NULL,
  KEY `FK6F669BE6ACA9040` (`FormaCobranca_id`),
  CONSTRAINT `FK6F669BE6ACA9040` FOREIGN KEY (`FormaCobranca_id`) REFERENCES `forma_cobranca` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `formacobrancacaucaoliquida_diasdomes`
--

DROP TABLE IF EXISTS `formacobrancacaucaoliquida_diasdomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formacobrancacaucaoliquida_diasdomes` (
  `FormaCobrancaCaucaoLiquida_id` bigint(20) NOT NULL,
  `diasDoMes` int(11) DEFAULT NULL,
  KEY `FK365C0CC1B530ACD4` (`FormaCobrancaCaucaoLiquida_id`),
  CONSTRAINT `FK365C0CC1B530ACD4` FOREIGN KEY (`FormaCobrancaCaucaoLiquida_id`) REFERENCES `forma_cobranca_caucao_liquida` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fornecedor`
--

DROP TABLE IF EXISTS `fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fornecedor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COD_INTERFACE` int(11) DEFAULT NULL,
  `EMAIL_NFE` varchar(255) DEFAULT NULL,
  `INICIO_ATIVIDADE` date NOT NULL,
  `MARGEM_DISTRIBUIDOR` decimal(18,4) DEFAULT NULL,
  `ORIGEM` varchar(255) NOT NULL,
  `POSSUI_CONTRATO` tinyint(1) DEFAULT NULL,
  `RESPONSAVEL` varchar(255) DEFAULT NULL,
  `SITUACAO_CADASTRO` varchar(255) NOT NULL,
  `TIPO_CONTRATO` varchar(255) DEFAULT NULL,
  `VALIDADE_CONTRATO` datetime DEFAULT NULL,
  `JURIDICA_ID` bigint(20) NOT NULL,
  `TIPO_FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  `BANCO_ID` bigint(20) DEFAULT NULL,
  `DESCONTO_ID` bigint(20) DEFAULT NULL,
  `CANAL_DISTRIBUICAO` varchar(30) DEFAULT NULL,
  `FORNECEDOR_UNIFICADOR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `JURIDICA_ID` (`JURIDICA_ID`),
  KEY `FK6B088D65440433FD` (`TIPO_FORNECEDOR_ID`),
  KEY `FK6B088D659218183B` (`JURIDICA_ID`),
  KEY `FK6B088D65E44516C0` (`BANCO_ID`),
  KEY `FK6B088D6529E8DFE3` (`DESCONTO_ID`),
  KEY `FKFORNECEDORUNIFICADOR_idx` (`FORNECEDOR_UNIFICADOR_ID`),
  CONSTRAINT `FK6B088D6529E8DFE3` FOREIGN KEY (`DESCONTO_ID`) REFERENCES `desconto` (`ID`),
  CONSTRAINT `FK6B088D65440433FD` FOREIGN KEY (`TIPO_FORNECEDOR_ID`) REFERENCES `tipo_fornecedor` (`ID`),
  CONSTRAINT `FK6B088D659218183B` FOREIGN KEY (`JURIDICA_ID`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FK6B088D65E44516C0` FOREIGN KEY (`BANCO_ID`) REFERENCES `banco` (`ID`),
  CONSTRAINT `FKFORNECEDORUNIFICADOR` FOREIGN KEY (`FORNECEDOR_UNIFICADOR_ID`) REFERENCES `fornecedor` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `furo_produto`
--

DROP TABLE IF EXISTS `furo_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `furo_produto` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` datetime NOT NULL,
  `LANCAMENTO_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `DATA_LCTO_DISTRIBUIDOR` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK301010E67FFF790E` (`USUARIO_ID`),
  KEY `FK301010E645C07ACF` (`LANCAMENTO_ID`),
  KEY `FK301010E6A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK301010E645C07ACF` FOREIGN KEY (`LANCAMENTO_ID`) REFERENCES `lancamento` (`ID`),
  CONSTRAINT `FK301010E67FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK301010E6A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `garantia`
--

DROP TABLE IF EXISTS `garantia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `garantia` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRICAO` varchar(255) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `ID_FIADOR` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKCD8E9167DC6BE690` (`ID_FIADOR`),
  CONSTRAINT `FKCD8E9167DC6BE690` FOREIGN KEY (`ID_FIADOR`) REFERENCES `fiador` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `garantia_cota_outros`
--

DROP TABLE IF EXISTS `garantia_cota_outros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `garantia_cota_outros` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRICAO` varchar(255) DEFAULT NULL,
  `DATA_VALIDADE` datetime DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `GARANTIA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7F1AA0168FCB49C8` (`GARANTIA_ID`),
  CONSTRAINT `FK7F1AA0168FCB49C8` FOREIGN KEY (`GARANTIA_ID`) REFERENCES `cota_garantia` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gerador_fluxo_pdv`
--

DROP TABLE IF EXISTS `gerador_fluxo_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gerador_fluxo_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PDV_ID` bigint(20) NOT NULL,
  `TIPO_GERADOR_FLUXO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PDV_ID` (`PDV_ID`),
  UNIQUE KEY `PDV_ID_2` (`PDV_ID`),
  KEY `FKECD90D241656FEE0` (`TIPO_GERADOR_FLUXO_ID`),
  KEY `FKECD90D24A65B70F4` (`PDV_ID`),
  CONSTRAINT `FKECD90D241656FEE0` FOREIGN KEY (`TIPO_GERADOR_FLUXO_ID`) REFERENCES `tipo_gerador_fluxo_pdv` (`ID`),
  CONSTRAINT `FKECD90D24A65B70F4` FOREIGN KEY (`PDV_ID`) REFERENCES `pdv` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gerador_fluxo_pdv_tipo_gerador_fluxo_pdv`
--

DROP TABLE IF EXISTS `gerador_fluxo_pdv_tipo_gerador_fluxo_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gerador_fluxo_pdv_tipo_gerador_fluxo_pdv` (
  `GERADOR_FLUXO_PDV_ID` bigint(20) NOT NULL,
  `TIPO_GERADOR_FLUXO_PDV_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`GERADOR_FLUXO_PDV_ID`,`TIPO_GERADOR_FLUXO_PDV_ID`),
  KEY `FK308AA654FA255EBD` (`TIPO_GERADOR_FLUXO_PDV_ID`),
  KEY `FK308AA654A4EA0566` (`GERADOR_FLUXO_PDV_ID`),
  CONSTRAINT `FK308AA654A4EA0566` FOREIGN KEY (`GERADOR_FLUXO_PDV_ID`) REFERENCES `gerador_fluxo_pdv` (`ID`),
  CONSTRAINT `FK308AA654FA255EBD` FOREIGN KEY (`TIPO_GERADOR_FLUXO_PDV_ID`) REFERENCES `tipo_gerador_fluxo_pdv` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupo_cota`
--

DROP TABLE IF EXISTS `grupo_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(255) DEFAULT NULL,
  `TIPO_COTA` varchar(255) DEFAULT NULL,
  `TIPO_GRUPO` varchar(255) DEFAULT NULL,
  `DATA_VIGENCIA_INICIO` date NOT NULL,
  `DATA_VIGENCIA_FIM` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupo_municipio`
--

DROP TABLE IF EXISTS `grupo_municipio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo_municipio` (
  `GRUPO_COTA_ID` bigint(20) NOT NULL,
  `LOCALIDADE` varchar(255) NOT NULL,
  KEY `FK2CA1AF571E8AD533` (`GRUPO_COTA_ID`),
  CONSTRAINT `FK2CA1AF571E8AD533` FOREIGN KEY (`GRUPO_COTA_ID`) REFERENCES `grupo_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupo_permissao`
--

DROP TABLE IF EXISTS `grupo_permissao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo_permissao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupo_permissao_permissao`
--

DROP TABLE IF EXISTS `grupo_permissao_permissao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo_permissao_permissao` (
  `PERMISSAO_GRUPO_ID` bigint(20) NOT NULL,
  `PERMISSAO_ID` varchar(255) DEFAULT NULL,
  KEY `FK618A7B99415856F3` (`PERMISSAO_GRUPO_ID`),
  CONSTRAINT `FK618A7B99415856F3` FOREIGN KEY (`PERMISSAO_GRUPO_ID`) REFERENCES `grupo_permissao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_acumulo_divida`
--

DROP TABLE IF EXISTS `historico_acumulo_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_acumulo_divida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_INCLUSAO` datetime NOT NULL,
  `STATUS` varchar(255) NOT NULL,
  `DIVIDA_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKA75B40057FFF790E` (`USUARIO_ID`),
  KEY `FKA75B40057C42C4C1` (`DIVIDA_ID`),
  CONSTRAINT `FKA75B40057C42C4C1` FOREIGN KEY (`DIVIDA_ID`) REFERENCES `divida` (`ID`),
  CONSTRAINT `FKA75B40057FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_desconto_cota_produto_excessoes`
--

DROP TABLE IF EXISTS `historico_desconto_cota_produto_excessoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_desconto_cota_produto_excessoes` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `DESCONTO_ID` bigint(20) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK56EB62817FFF790E` (`USUARIO_ID`),
  KEY `FK56EB628156501954` (`DISTRIBUIDOR_ID`),
  KEY `FK56EB6281C8181F74` (`COTA_ID`),
  KEY `FK56EB628129E8DFE3` (`DESCONTO_ID`),
  KEY `FK56EB6281C5C16480` (`PRODUTO_ID`),
  KEY `FK56EB62819808F874` (`FORNECEDOR_ID`),
  KEY `FK56EB6281A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK56EB628129E8DFE3` FOREIGN KEY (`DESCONTO_ID`) REFERENCES `desconto` (`ID`),
  CONSTRAINT `FK56EB628156501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FK56EB62817FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK56EB62819808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK56EB6281A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK56EB6281C5C16480` FOREIGN KEY (`PRODUTO_ID`) REFERENCES `produto` (`ID`),
  CONSTRAINT `FK56EB6281C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_desconto_produto_edicoes`
--

DROP TABLE IF EXISTS `historico_desconto_produto_edicoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_desconto_produto_edicoes` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `DESCONTO_ID` bigint(20) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  `PRODUTO_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK9EC901397FFF790E` (`USUARIO_ID`),
  KEY `FK9EC9013956501954` (`DISTRIBUIDOR_ID`),
  KEY `FK9EC9013929E8DFE3` (`DESCONTO_ID`),
  KEY `FK9EC90139C5C16480` (`PRODUTO_ID`),
  KEY `FK9EC901399808F874` (`FORNECEDOR_ID`),
  KEY `FK9EC90139A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK9EC9013929E8DFE3` FOREIGN KEY (`DESCONTO_ID`) REFERENCES `desconto` (`ID`),
  CONSTRAINT `FK9EC9013956501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FK9EC901397FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK9EC901399808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK9EC90139A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK9EC90139C5C16480` FOREIGN KEY (`PRODUTO_ID`) REFERENCES `produto` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_desconto_produtos`
--

DROP TABLE IF EXISTS `historico_desconto_produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_desconto_produtos` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `DESCONTO_ID` bigint(20) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  `PRODUTO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6FDFA8FF7FFF790E` (`USUARIO_ID`),
  KEY `FK6FDFA8FF56501954` (`DISTRIBUIDOR_ID`),
  KEY `FK6FDFA8FF29E8DFE3` (`DESCONTO_ID`),
  KEY `FK6FDFA8FFC5C16480` (`PRODUTO_ID`),
  KEY `FK6FDFA8FF9808F874` (`FORNECEDOR_ID`),
  CONSTRAINT `FK6FDFA8FF29E8DFE3` FOREIGN KEY (`DESCONTO_ID`) REFERENCES `desconto` (`ID`),
  CONSTRAINT `FK6FDFA8FF56501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FK6FDFA8FF7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK6FDFA8FF9808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK6FDFA8FFC5C16480` FOREIGN KEY (`PRODUTO_ID`) REFERENCES `produto` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_descontos_fornecedores`
--

DROP TABLE IF EXISTS `historico_descontos_fornecedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_descontos_fornecedores` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `DESCONTO_ID` bigint(20) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKB4E995F97FFF790E` (`USUARIO_ID`),
  KEY `FKB4E995F956501954` (`DISTRIBUIDOR_ID`),
  KEY `FKB4E995F929E8DFE3` (`DESCONTO_ID`),
  KEY `FKB4E995F99808F874` (`FORNECEDOR_ID`),
  CONSTRAINT `FKB4E995F929E8DFE3` FOREIGN KEY (`DESCONTO_ID`) REFERENCES `desconto` (`ID`),
  CONSTRAINT `FKB4E995F956501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FKB4E995F97FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKB4E995F99808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_estoque_produto`
--

DROP TABLE IF EXISTS `historico_estoque_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_estoque_produto` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA` date NOT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `QTDE_DANIFICADO` decimal(18,4) DEFAULT NULL,
  `QTDE_DEVOLUCAO_ENCALHE` decimal(18,4) DEFAULT NULL,
  `QTDE_DEVOLUCAO_FORNECEDOR` decimal(18,4) DEFAULT NULL,
  `QTDE_JURAMENTADO` decimal(18,4) DEFAULT NULL,
  `QTDE_SUPLEMENTAR` decimal(18,4) DEFAULT NULL,
  `VERSAO` bigint(20) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DATA` (`DATA`,`PRODUTO_EDICAO_ID`),
  KEY `FKEC4EDD83A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FKEC4EDD83A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_consolidado_cota`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_consolidado_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_consolidado_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QNT_ATIVOS` decimal(18,4) DEFAULT NULL,
  `QNT_AUSENTE_ENCALHE` decimal(18,4) DEFAULT NULL,
  `QNT_AUSENTE_REPARTE` decimal(18,4) DEFAULT NULL,
  `QNT_INATIVAS` decimal(18,4) DEFAULT NULL,
  `QNT_NOVOS` decimal(18,4) DEFAULT NULL,
  `QNT_TOTAL` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK310500DC8CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK310500DC8CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_consolidado_divida`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_consolidado_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_consolidado_divida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TIPO_DIVIDA` varchar(255) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK52994B88CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK52994B88CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_consolidado_encalhe`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_consolidado_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_consolidado_encalhe` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SALDO` decimal(18,4) DEFAULT NULL,
  `VALOR_FALTA_EM` decimal(18,4) DEFAULT NULL,
  `VALOR_FISICO` decimal(18,4) DEFAULT NULL,
  `VALOR_JURAMENTADO` decimal(18,4) DEFAULT NULL,
  `VALOR_LOGICO` decimal(18,4) DEFAULT NULL,
  `VALOR_SOBRA_EM` decimal(18,4) DEFAULT NULL,
  `VALOR_VENDA` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FECHAMENTO_DIARIO_ID` (`FECHAMENTO_DIARIO_ID`),
  KEY `FKDC68363F8CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FKDC68363F8CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_consolidado_reparte`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_consolidado_reparte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_consolidado_reparte` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VALOR_DIFERENCA` decimal(18,4) DEFAULT NULL,
  `VALOR_DISTRIBUIDO` decimal(18,4) DEFAULT NULL,
  `VALOR_FALTAS` decimal(18,4) DEFAULT NULL,
  `VALOR_REPARTE` decimal(18,4) DEFAULT NULL,
  `SOBRA_DISTRIBUIDA` decimal(18,4) DEFAULT NULL,
  `VALOR_SOBRAS` decimal(18,4) DEFAULT NULL,
  `VALOR_TRANSFERIDO` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FECHAMENTO_DIARIO_ID` (`FECHAMENTO_DIARIO_ID`),
  KEY `FK7D74E47C8CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK7D74E47C8CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_consolidado_suplementar`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_consolidado_suplementar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_consolidado_suplementar` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VALOR_ESTOQUE_LOGICO` decimal(18,4) DEFAULT NULL,
  `VALOR_SALDO` decimal(18,4) DEFAULT NULL,
  `VALOR_TRANSFERENCIA` decimal(18,4) DEFAULT NULL,
  `VALOR_VENDAS` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FECHAMENTO_DIARIO_ID` (`FECHAMENTO_DIARIO_ID`),
  KEY `FKCBE0C2938CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FKCBE0C2938CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_cota`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOME_COTA` varchar(255) DEFAULT NULL,
  `NUMERO_COTA` int(11) DEFAULT NULL,
  `TIPO_DETALHE_COTA` varchar(255) DEFAULT NULL,
  `HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK76A04220C9F103CA` (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID`),
  CONSTRAINT `FK76A04220C9F103CA` FOREIGN KEY (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID`) REFERENCES `historico_fechamento_diario_consolidado_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_divida`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_divida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BANCO` varchar(255) DEFAULT NULL,
  `DATA_VENCIMENTO` date DEFAULT NULL,
  `ID_DIVIDA` bigint(20) DEFAULT NULL,
  `NOME_COTA` varchar(255) DEFAULT NULL,
  `NOSSO_NUMERO` varchar(255) DEFAULT NULL,
  `NUMERO_CONTA` int(11) DEFAULT NULL,
  `NUMERO_COTA` bigint(20) DEFAULT NULL,
  `FORMA_PAGAMENTO` varchar(255) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK50F994FCAFD4024A` (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID`),
  CONSTRAINT `FK50F994FCAFD4024A` FOREIGN KEY (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID`) REFERENCES `historico_fechamento_diario_consolidado_divida` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_lancamento_encalhe`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_lancamento_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_lancamento_encalhe` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QUANTIDADE` decimal(18,4) DEFAULT NULL,
  `QNT_DIFERENCA` decimal(18,4) DEFAULT NULL,
  `QNT_VENDA_ENCALHE` decimal(18,4) DEFAULT NULL,
  `HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID` bigint(20) NOT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKDD9B8264D2FE34B7` (`ID_PRODUTO_EDICAO`),
  KEY `FKDD9B826411CEE7CA` (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID`),
  CONSTRAINT `FKDD9B826411CEE7CA` FOREIGN KEY (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID`) REFERENCES `historico_fechamento_diario_consolidado_encalhe` (`ID`),
  CONSTRAINT `FKDD9B8264D2FE34B7` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_lancamento_reparte`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_lancamento_reparte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_lancamento_reparte` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QNT_A_DISTRIBUIR` decimal(18,4) DEFAULT NULL,
  `QNT_DIFERENCA` decimal(18,4) DEFAULT NULL,
  `QNT_DISTRIBUIDO` decimal(18,4) DEFAULT NULL,
  `QNT_FALTA_EM` decimal(18,4) DEFAULT NULL,
  `QNT_REPARTE` decimal(18,4) DEFAULT NULL,
  `QNT_SOBRA_DISTRIBUIDO` decimal(18,4) DEFAULT NULL,
  `QNT_SOBRA_EM` decimal(18,4) DEFAULT NULL,
  `QNT_TRANSFERENCIA` decimal(18,4) DEFAULT NULL,
  `HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID` bigint(20) NOT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7EA830A13583E2AA` (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID`),
  KEY `FK7EA830A1D2FE34B7` (`ID_PRODUTO_EDICAO`),
  CONSTRAINT `FK7EA830A13583E2AA` FOREIGN KEY (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_REPARTE_ID`) REFERENCES `historico_fechamento_diario_consolidado_reparte` (`ID`),
  CONSTRAINT `FK7EA830A1D2FE34B7` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_lancamento_suplementar`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_lancamento_suplementar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_lancamento_suplementar` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QNT_CONTABILIZADA` decimal(18,4) DEFAULT NULL,
  `QNT_DIFERENCA` decimal(18,4) DEFAULT NULL,
  `QNT_FISICO` decimal(18,4) DEFAULT NULL,
  `HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID` bigint(20) NOT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK2C977438D2FE34B7` (`ID_PRODUTO_EDICAO`),
  KEY `FK2C977438B16DDF4A` (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID`),
  CONSTRAINT `FK2C977438B16DDF4A` FOREIGN KEY (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID`) REFERENCES `historico_fechamento_diario_consolidado_suplementar` (`ID`),
  CONSTRAINT `FK2C977438D2FE34B7` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_movimento_vendas_encalhe`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_movimento_vendas_encalhe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_movimento_vendas_encalhe` (
  `ID` bigint(20) NOT NULL,
  `DATA_VENCIMENTO` date DEFAULT NULL,
  `QUANTIDADE` decimal(18,4) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  `HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK534CD760D2FE34B71d34a8e4` (`ID_PRODUTO_EDICAO`),
  KEY `FK1D34A8E411CEE7CA` (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID`),
  CONSTRAINT `FK1D34A8E411CEE7CA` FOREIGN KEY (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID`) REFERENCES `historico_fechamento_diario_consolidado_encalhe` (`ID`),
  CONSTRAINT `FK534CD760D2FE34B71d34a8e4` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_movimento_vendas_suplementar`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_movimento_vendas_suplementar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_movimento_vendas_suplementar` (
  `ID` bigint(20) NOT NULL,
  `DATA_VENCIMENTO` date DEFAULT NULL,
  `QUANTIDADE` decimal(18,4) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  `HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK534CD760D2FE34B716395ab8` (`ID_PRODUTO_EDICAO`),
  KEY `FK16395AB8B16DDF4A` (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID`),
  CONSTRAINT `FK16395AB8B16DDF4A` FOREIGN KEY (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID`) REFERENCES `historico_fechamento_diario_consolidado_suplementar` (`ID`),
  CONSTRAINT `FK534CD760D2FE34B716395ab8` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_resumo_consignado`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_resumo_consignado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_resumo_consignado` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TIPO_VALOR` varchar(255) DEFAULT NULL,
  `VALOR_AVISTA` decimal(18,4) DEFAULT NULL,
  `VALOR_CONSIGNADO` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK491DAC2E8CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK491DAC2E8CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_resumo_consolidado_divida`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_resumo_consolidado_divida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_resumo_consolidado_divida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FORMA_PAGAMENTO` varchar(255) DEFAULT NULL,
  `VALOR_INADIMPLENTE` decimal(18,4) DEFAULT NULL,
  `VALOR_PAGO` decimal(18,4) DEFAULT NULL,
  `VALOR_TOTAL` decimal(18,4) DEFAULT NULL,
  `HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKF34B83D2AFD4024A` (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID`),
  CONSTRAINT `FKF34B83D2AFD4024A` FOREIGN KEY (`HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID`) REFERENCES `historico_fechamento_diario_consolidado_divida` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_fechamento_diario_resumo_estoque`
--

DROP TABLE IF EXISTS `historico_fechamento_diario_resumo_estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_fechamento_diario_resumo_estoque` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QNT_EXEMPLARES` decimal(18,4) DEFAULT NULL,
  `QNT_PRODUTO` decimal(18,4) DEFAULT NULL,
  `TIPO_ESTOQUE` varchar(255) DEFAULT NULL,
  `VALOR_TOTAL` decimal(18,4) DEFAULT NULL,
  `FECHAMENTO_DIARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6292F7D78CD5CBB3` (`FECHAMENTO_DIARIO_ID`),
  CONSTRAINT `FK6292F7D78CD5CBB3` FOREIGN KEY (`FECHAMENTO_DIARIO_ID`) REFERENCES `fechamento_diario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_lancamento`
--

DROP TABLE IF EXISTS `historico_lancamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_lancamento` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_EDICAO` datetime NOT NULL,
  `TIPO_EDICAO` varchar(255) NOT NULL,
  `STATUS_NOVO` varchar(255) NOT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  `LANCAMENTO_ID` bigint(20) NOT NULL,
  `STATUS_ANTIGO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKF4FBF5497FFF790E` (`USUARIO_ID`),
  KEY `FKF4FBF54945C07ACF` (`LANCAMENTO_ID`),
  CONSTRAINT `FKF4FBF54945C07ACF` FOREIGN KEY (`LANCAMENTO_ID`) REFERENCES `lancamento` (`ID`),
  CONSTRAINT `FKF4FBF5497FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_movto_financeiro_cota`
--

DROP TABLE IF EXISTS `historico_movto_financeiro_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_movto_financeiro_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_EDICAO` datetime NOT NULL,
  `TIPO_EDICAO` varchar(255) NOT NULL,
  `DATA` date NOT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `MOVTO_FINANCEIRO_COTA_ID` bigint(20) NOT NULL,
  `TIPO_MOVTO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKAB46D6EDC7F49D38` (`MOVTO_FINANCEIRO_COTA_ID`),
  KEY `FKAB46D6ED7FFF790E` (`USUARIO_ID`),
  KEY `FKAB46D6EDC8181F74` (`COTA_ID`),
  KEY `FKAB46D6ED3BDC9EE3` (`TIPO_MOVTO_ID`),
  CONSTRAINT `FKAB46D6ED3BDC9EE3` FOREIGN KEY (`TIPO_MOVTO_ID`) REFERENCES `tipo_movimento` (`ID`),
  CONSTRAINT `FKAB46D6ED7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKAB46D6EDC7F49D38` FOREIGN KEY (`MOVTO_FINANCEIRO_COTA_ID`) REFERENCES `movimento_financeiro_cota` (`ID`),
  CONSTRAINT `FKAB46D6EDC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_situacao_cota`
--

DROP TABLE IF EXISTS `historico_situacao_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_situacao_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_EDICAO` datetime NOT NULL,
  `TIPO_EDICAO` varchar(255) NOT NULL,
  `RESTAURADO` tinyint(1) DEFAULT NULL,
  `PROCESSADO` tinyint(1) DEFAULT NULL,
  `DATA_FIM_VALIDADE` date DEFAULT NULL,
  `DATA_INICIO_VALIDADE` date DEFAULT NULL,
  `DESCRICAO` varchar(255) DEFAULT NULL,
  `MOTIVO` varchar(255) DEFAULT NULL,
  `NOVA_SITUACAO` varchar(255) NOT NULL,
  `SITUACAO_ANTERIOR` varchar(255) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKA23483427FFF790E` (`USUARIO_ID`),
  KEY `FKA2348342C8181F74` (`COTA_ID`),
  CONSTRAINT `FKA23483427FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKA2348342C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota`
--

DROP TABLE IF EXISTS `historico_titularidade_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BOX` varchar(255) DEFAULT NULL,
  `CLASSIFICACAO_EXPECTATIVA_FATURAMENTO` varchar(255) DEFAULT NULL,
  `DATA_INCLUSAO` date DEFAULT NULL,
  `DISTRIBUICAO_ASSISTENTE_COMERCIAL` varchar(255) DEFAULT NULL,
  `DISTRIBUICAO_BOLETO_EMAIL` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_BOLETO_IMPRESSO` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_BOLETO_SLIP_EMAIL` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_BOLETO_SLIP_IMPRESSO` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_CHAMADA_ENCALHE_EMAIL` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_CHAMADA_ENCALHE_IMPRESSO` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_ENTREGA_REPARTE_VENDA` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_FIM_PERIODO_CARENCIA` date DEFAULT NULL,
  `DISTRIBUICAO_GERENTE_COMERCIAL` varchar(255) DEFAULT NULL,
  `DISTRIBUICAO_INICIO_PERIODO_CARENCIA` date DEFAULT NULL,
  `DISTRIBUICAO_NOTA_ENVIO_EMAIL` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_NOTA_ENVIO_IMPRESSO` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_OBSERVACAO` varchar(255) DEFAULT NULL,
  `DISTRIBUICAO_PERCENTUAL_FATURAMENTO_ENTREGA` decimal(18,4) DEFAULT NULL,
  `DISTRIBUICAO_POSSUI_PROCURACAO` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_POSSUI_TERMO_ADESAO` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_PROCURACAO_ASSINADA` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_QTDE_PDV` int(11) DEFAULT NULL,
  `DISTRIBUICAO_RECEBE_RECOLHE_PARCIAIS` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_RECIBO_EMAIL` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_RECIBO_IMPRESSO` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_SLIP_EMAIL` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_SLIP_IMPRESSO` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_SOLICITA_NUM_ATRASADOS` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_TAXA_FIXA_ENTREGA` decimal(18,4) DEFAULT NULL,
  `DISTRIBUICAO_TERMO_ADESAO_ASSINADO` tinyint(1) DEFAULT NULL,
  `DISTRIBUICAO_TIPO_ENTREGA` varchar(255) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `EMAIL_NFE` varchar(255) DEFAULT NULL,
  `EMITE_NFE` tinyint(1) DEFAULT NULL,
  `FIM_TITULARIDADE` date NOT NULL,
  `FIM_PERIODO_COTA_BASE` date DEFAULT NULL,
  `INICIO_TITULARIDADE` date NOT NULL,
  `INICIO_PERIODO_COTA_BASE` date DEFAULT NULL,
  `NUMERO_COTA` int(11) NOT NULL,
  `APELIDO` varchar(25) DEFAULT NULL,
  `CPF` varchar(255) DEFAULT NULL,
  `DATA_NASCIMENTO` date DEFAULT NULL,
  `ESTADO_CIVIL` varchar(255) DEFAULT NULL,
  `NACIONALIDADE` varchar(255) DEFAULT NULL,
  `NATURALIDADE` varchar(255) DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `ORGAO_EMISSOR` varchar(255) DEFAULT NULL,
  `RG` varchar(255) DEFAULT NULL,
  `SEXO` varchar(255) DEFAULT NULL,
  `UF_ORGAO_EMISSOR` varchar(255) DEFAULT NULL,
  `CNPJ` varchar(255) DEFAULT NULL,
  `INSC_ESTADUAL` varchar(20) DEFAULT NULL,
  `INSC_MUNICIPAL` varchar(15) DEFAULT NULL,
  `NOME_FANTASIA` varchar(60) DEFAULT NULL,
  `RAZAO_SOCIAL` varchar(255) DEFAULT NULL,
  `SITUACAO_CADASTRO` varchar(255) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7B8B4229C8181F74` (`COTA_ID`),
  KEY `FK7B8B422997041E9A` (`HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID`),
  CONSTRAINT `FK7B8B422997041E9A` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID`) REFERENCES `historico_titularidade_cota_financeiro` (`ID`),
  CONSTRAINT `FK7B8B4229C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_atualizacao_caucao_liquida`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_atualizacao_caucao_liquida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_atualizacao_caucao_liquida` (
  `HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID` bigint(20) NOT NULL,
  `DATA_ATUALIZACAO` datetime DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  KEY `FK876AEF279610C138` (`HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID`),
  CONSTRAINT `FK876AEF279610C138` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID`) REFERENCES `historico_titularidade_cota_garantia` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_concentracao_cobranca`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_concentracao_cobranca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_concentracao_cobranca` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TIPO_FORMA_COBRANCA` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_concentracao_cobranca_dia_mes`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_concentracao_cobranca_dia_mes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_concentracao_cobranca_dia_mes` (
  `HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID` bigint(20) NOT NULL,
  `DIA_MES` int(11) DEFAULT NULL,
  KEY `FK86D6DE91290F8C31` (`HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID`),
  CONSTRAINT `FK86D6DE91290F8C31` FOREIGN KEY (`HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID`) REFERENCES `historico_titularidade_cota_concentracao_cobranca` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_concentracao_cobranca_dia_semana`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_concentracao_cobranca_dia_semana`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_concentracao_cobranca_dia_semana` (
  `HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID` bigint(20) NOT NULL,
  `DIA_SEMANA` int(11) DEFAULT NULL,
  KEY `FK70D1A303290F8C31` (`HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID`),
  CONSTRAINT `FK70D1A303290F8C31` FOREIGN KEY (`HISTORICO_TITULARIDADE_CONCENTRACAO_COBRANCA_ID`) REFERENCES `historico_titularidade_cota_concentracao_cobranca` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_desconto`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_desconto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_desconto` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATUALIZACAO` datetime NOT NULL,
  `DESCONTO` decimal(18,4) DEFAULT NULL,
  `NOME_FORNECEDOR` varchar(255) DEFAULT NULL,
  `TIPO_DESCONTO` varchar(255) DEFAULT NULL,
  `PRODUTO_CODIGO` varchar(255) DEFAULT NULL,
  `PRODUTO_NOME` varchar(255) DEFAULT NULL,
  `PRODUTO_NUMERO_EDICAO` bigint(20) DEFAULT NULL,
  `HISTORICO_TITULARIDADE_COTA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKD07CAC01508A9E07` (`HISTORICO_TITULARIDADE_COTA_ID`),
  CONSTRAINT `FKD07CAC01508A9E07` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_ID`) REFERENCES `historico_titularidade_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_endereco`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_endereco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_endereco` (
  `HISTORICO_TITULARIDADE_COTA_ID` bigint(20) NOT NULL,
  `ENDERECO_BAIRRO` varchar(60) DEFAULT NULL,
  `ENDERECO_CEP` varchar(9) DEFAULT NULL,
  `ENDERECO_CIDADE` varchar(60) DEFAULT NULL,
  `ENDERECO_CODIGO_BAIRRO` int(11) DEFAULT NULL,
  `ENDERECO_CODIGO_CIDADE_IBGE` int(11) DEFAULT NULL,
  `ENDERECO_CODIGO_UF` int(11) DEFAULT NULL,
  `ENDERECO_COMPLEMENTO` varchar(60) DEFAULT NULL,
  `ENDERECO_LOGRADOURO` varchar(60) DEFAULT NULL,
  `ENDERECO_NUMERO` varchar(60) DEFAULT NULL,
  `ENDERECO_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `ENDERECO_TIPO_ENDERECO` varchar(255) DEFAULT NULL,
  `ENDERECO_TIPO_LOGRADOURO` varchar(255) DEFAULT NULL,
  `ENDERECO_UF` varchar(2) DEFAULT NULL,
  KEY `FKFAFAA51F508A9E07` (`HISTORICO_TITULARIDADE_COTA_ID`),
  CONSTRAINT `FKFAFAA51F508A9E07` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_ID`) REFERENCES `historico_titularidade_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_fiador_garantia`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_fiador_garantia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_fiador_garantia` (
  `HISTORICO_TITULARIDADE_COTA_FIADOR_ID` bigint(20) NOT NULL,
  `GARANTIA_DESCRICAO` varchar(255) DEFAULT NULL,
  `GARANTIA_VALOR` decimal(18,4) DEFAULT NULL,
  KEY `FKADC6E47458C497A` (`HISTORICO_TITULARIDADE_COTA_FIADOR_ID`),
  CONSTRAINT `FKADC6E47458C497A` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_FIADOR_ID`) REFERENCES `historico_titularidade_cota_garantia` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_financeiro`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_financeiro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_financeiro` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONTRATO_RECEBIDO` tinyint(1) DEFAULT NULL,
  `DATA_INICIO_CONTRATO` date DEFAULT NULL,
  `DATA_TERMINO_CONTRATO` date DEFAULT NULL,
  `FATOR_VENCIMENTO` int(11) DEFAULT NULL,
  `NUM_ACUMULO_DIVIDA` int(11) DEFAULT NULL,
  `VALOR_SUSPENSAO` decimal(18,4) DEFAULT NULL,
  `POSSUI_CONTRATO` tinyint(1) DEFAULT NULL,
  `TIPO_COTA` varchar(255) DEFAULT NULL,
  `VALOR_MINIMO_COBRANCA` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_forma_pagamento`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_forma_pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_forma_pagamento` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BANCO_AGENCIA` bigint(20) DEFAULT NULL,
  `BANCO_CONTA` bigint(20) DEFAULT NULL,
  `BANCO_DV_AGENCIA` varchar(255) DEFAULT NULL,
  `BANCO_DV_CONTA` varchar(255) DEFAULT NULL,
  `BANCO_NOME` varchar(255) DEFAULT NULL,
  `BANCO_NUMERO` varchar(255) DEFAULT NULL,
  `TIPO_COBRANCA` varchar(255) NOT NULL,
  `HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID` bigint(20) DEFAULT NULL,
  `HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKB6F8BF2E97041E9A` (`HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID`),
  KEY `FKB6F8BF2EBC56D0FB` (`HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID`),
  CONSTRAINT `FKB6F8BF2E97041E9A` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID`) REFERENCES `historico_titularidade_cota_financeiro` (`ID`),
  CONSTRAINT `FKB6F8BF2EBC56D0FB` FOREIGN KEY (`HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID`) REFERENCES `historico_titularidade_cota_concentracao_cobranca` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_forma_pagamento_fornecedor`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_forma_pagamento_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_forma_pagamento_fornecedor` (
  `HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID` bigint(20) NOT NULL,
  `HISTORICO_TITULARIDADE_COTA_FORNECEDOR_ID` bigint(20) NOT NULL,
  KEY `FK563C7B16FAD71BFA` (`HISTORICO_TITULARIDADE_COTA_FORNECEDOR_ID`),
  KEY `FK563C7B16B38F2485` (`HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID`),
  CONSTRAINT `FK563C7B16B38F2485` FOREIGN KEY (`HISTORICO_TITULARIDADE_FORMA_PAGAMENTO_ID`) REFERENCES `historico_titularidade_cota_forma_pagamento` (`ID`),
  CONSTRAINT `FK563C7B16FAD71BFA` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_FORNECEDOR_ID`) REFERENCES `historico_titularidade_cota_fornecedor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_fornecedor`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_fornecedor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID_ORIGEM` bigint(20) DEFAULT NULL,
  `CNPJ` varchar(255) DEFAULT NULL,
  `INSC_ESTADUAL` varchar(18) DEFAULT NULL,
  `INSC_MUNICIPAL` varchar(15) DEFAULT NULL,
  `NOME_FANTASIA` varchar(60) DEFAULT NULL,
  `RAZAO_SOCIAL` varchar(255) DEFAULT NULL,
  `HISTORICO_TITULARIDADE_COTA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK2391DD3B508A9E07` (`HISTORICO_TITULARIDADE_COTA_ID`),
  CONSTRAINT `FK2391DD3B508A9E07` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_ID`) REFERENCES `historico_titularidade_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_funcionamento_pdv`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_funcionamento_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_funcionamento_pdv` (
  `HISTORICO_TITULARIDADE_COTA_PDV_ID` bigint(20) NOT NULL,
  `HORARIO_FIM` time DEFAULT NULL,
  `HORARIO_INICIO` time DEFAULT NULL,
  `FUNCIONAMENTO_PDV` varchar(255) DEFAULT NULL,
  KEY `FK21FF2C411043021A` (`HISTORICO_TITULARIDADE_COTA_PDV_ID`),
  CONSTRAINT `FK21FF2C411043021A` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_PDV_ID`) REFERENCES `historico_titularidade_cota_pdv` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_garantia`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_garantia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_garantia` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TIPO_GARANTIA` varchar(255) DEFAULT NULL,
  `AGENCIA_BANCO` bigint(20) DEFAULT NULL,
  `CONTA_BANCO` bigint(20) DEFAULT NULL,
  `DV_AGENCIA_BANCO` varchar(255) DEFAULT NULL,
  `DV_CONTA_BANCO` varchar(255) DEFAULT NULL,
  `NOME_BANCO` varchar(255) DEFAULT NULL,
  `CORRENTISTA` varchar(255) DEFAULT NULL,
  `NUMERO_BANCO` varchar(255) DEFAULT NULL,
  `VALOR_CAUCAO_LIQUIDA` decimal(18,4) DEFAULT NULL,
  `CHEQUE_CAUCAO_AGENCIA` bigint(20) DEFAULT NULL,
  `CHEQUE_CAUCAO_CONTA` bigint(20) DEFAULT NULL,
  `CHEQUE_CAUCAO_CORRENTISTA` varchar(255) DEFAULT NULL,
  `CHEQUE_CAUCAO_DV_AGENCIA` varchar(255) DEFAULT NULL,
  `CHEQUE_CAUCAO_DV_CONTA` varchar(255) DEFAULT NULL,
  `CHEQUE_CAUCAO_DATA_EMISSAO` date DEFAULT NULL,
  `CHEQUE_CAUCAO_IMAGEM` longblob,
  `CHEQUE_CAUCAO_NOME_BANCO` varchar(255) DEFAULT NULL,
  `CHEQUE_CAUCAO_NUMERO_BANCO` varchar(255) DEFAULT NULL,
  `CHEQUE_CAUCAO_NUMERO_CHEQUE` varchar(255) DEFAULT NULL,
  `CHEQUE_CAUCAO_DATA_VALIDADE` date DEFAULT NULL,
  `CHEQUE_CAUCAO_VALOR` decimal(18,4) DEFAULT NULL,
  `FIADOR_CPF_CNPJ` varchar(255) DEFAULT NULL,
  `ENDERECO_BAIRRO` varchar(60) DEFAULT NULL,
  `ENDERECO_CEP` varchar(9) DEFAULT NULL,
  `ENDERECO_CIDADE` varchar(60) DEFAULT NULL,
  `ENDERECO_CODIGO_BAIRRO` int(11) DEFAULT NULL,
  `ENDERECO_CODIGO_CIDADE_IBGE` int(11) DEFAULT NULL,
  `ENDERECO_CODIGO_UF` int(11) DEFAULT NULL,
  `ENDERECO_COMPLEMENTO` varchar(60) DEFAULT NULL,
  `ENDERECO_LOGRADOURO` varchar(60) DEFAULT NULL,
  `ENDERECO_NUMERO` varchar(60) DEFAULT NULL,
  `ENDERECO_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `ENDERECO_TIPO_ENDERECO` varchar(255) DEFAULT NULL,
  `ENDERECO_TIPO_LOGRADOURO` varchar(255) DEFAULT NULL,
  `ENDERECO_UF` varchar(2) DEFAULT NULL,
  `DDD` varchar(255) DEFAULT NULL,
  `TELEFONE_NUMERO` varchar(255) DEFAULT NULL,
  `TELEFONE_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `TELEFONE_RAMAL` varchar(255) DEFAULT NULL,
  `TELEFONE_TIPO_TELEFONE` varchar(255) DEFAULT NULL,
  `FIADOR_NOME` varchar(255) DEFAULT NULL,
  `IMOVEL_ENDERECO` varchar(255) DEFAULT NULL,
  `IMOVEL_NUMERO_REGISTRO` varchar(255) DEFAULT NULL,
  `IMOVEL_OBSERVACAO` varchar(255) DEFAULT NULL,
  `IMOVEL_PROPRIETARIO` varchar(255) DEFAULT NULL,
  `IMOVEL_VALOR` decimal(18,4) DEFAULT NULL,
  `NOTA_PROMISSORIA_VALOR` decimal(18,4) DEFAULT NULL,
  `NOTA_PROMISSORIA_VALOR_EXTENSO` varchar(255) DEFAULT NULL,
  `NOTA_PROMISSORIA_VENCIMENTO` date DEFAULT NULL,
  `OUTROS_DESCRICAO` varchar(255) DEFAULT NULL,
  `OUTROS_DATA_VALIDADE` datetime DEFAULT NULL,
  `OUTROS_VALOR` decimal(18,4) DEFAULT NULL,
  `HISTORICO_TITULARIDADE_COTA_ID` bigint(20) DEFAULT NULL,
  `HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK32B5DEBD508A9E07` (`HISTORICO_TITULARIDADE_COTA_ID`),
  KEY `FK32B5DEBDF244821E` (`HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID`),
  CONSTRAINT `FK32B5DEBD508A9E07` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_ID`) REFERENCES `historico_titularidade_cota` (`ID`),
  CONSTRAINT `FK32B5DEBDF244821E` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID`) REFERENCES `historico_titularidade_cota_pgto_caucao_liquida` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_gerador_fluxo_secundario`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_gerador_fluxo_secundario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_gerador_fluxo_secundario` (
  `HISTORICO_TITULARIDADE_COTA_PDV_ID` bigint(20) NOT NULL,
  `CODIGO_GERADOR_FLUXO` bigint(20) DEFAULT NULL,
  `DESCRICAO_GERADOR_FLUXO` varchar(255) DEFAULT NULL,
  KEY `FKE4C9A5E51043021A` (`HISTORICO_TITULARIDADE_COTA_PDV_ID`),
  CONSTRAINT `FKE4C9A5E51043021A` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_PDV_ID`) REFERENCES `historico_titularidade_cota_pdv` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_material_promocional_pdv`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_material_promocional_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_material_promocional_pdv` (
  `HISTORICO_TITULARIDADE_COTA_PDV_ID` bigint(20) NOT NULL,
  `CODIGO_MATERIAL` bigint(20) DEFAULT NULL,
  `DESCRICAO_MATERIAL` varchar(255) DEFAULT NULL,
  KEY `FK1DE198401043021A` (`HISTORICO_TITULARIDADE_COTA_PDV_ID`),
  CONSTRAINT `FK1DE198401043021A` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_PDV_ID`) REFERENCES `historico_titularidade_cota_pdv` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_pdv`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO_AREA_INFLUENCIA` bigint(20) DEFAULT NULL,
  `DESCRICAO_AREA_INFLUENCIA` varchar(255) DEFAULT NULL,
  `ARRENDATARIO` tinyint(1) DEFAULT NULL,
  `BALCAO_CENTRAL` tinyint(1) DEFAULT NULL,
  `PONTO_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `POSSUI_CARTAO_CREDITO` tinyint(1) DEFAULT NULL,
  `POSSUI_COMPUTADOR` tinyint(1) DEFAULT NULL,
  `POSSUI_LUMINOSO` tinyint(1) DEFAULT NULL,
  `TEXTO_LUMINOSO` varchar(255) DEFAULT NULL,
  `CONTATO` varchar(255) DEFAULT NULL,
  `DATA_INCLUSAO` date DEFAULT NULL,
  `DENTRO_OUTRO_ESTABELECIMENTO` tinyint(1) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `EXPOSITOR` tinyint(1) DEFAULT NULL,
  `CODIGO_GERADOR_FLUXO_PRINCIPAL` bigint(20) DEFAULT NULL,
  `DESCRICAO_GERADOR_FLUXO_PRINCIPAL` varchar(255) DEFAULT NULL,
  `IMAGEM` longblob,
  `NOME` varchar(255) NOT NULL,
  `NOME_LICENCA_LICENCA_MUNICIPAL` varchar(255) DEFAULT NULL,
  `NUMERO_LICENCA_MUNICIPAL` varchar(255) DEFAULT NULL,
  `PONTO_REFERENCIA` varchar(255) DEFAULT NULL,
  `PORCENTAGEM_FATURAMENTO` decimal(18,4) DEFAULT NULL,
  `POSSUI_SISTEMA_IPV` tinyint(1) DEFAULT NULL,
  `QTDE_FUNCIONARIOS` int(11) DEFAULT NULL,
  `SITE` varchar(255) DEFAULT NULL,
  `STATUS_PDV` varchar(255) DEFAULT NULL,
  `TAMANHO_PDV` varchar(255) DEFAULT NULL,
  `TIPO_CARACTERISTICA_PDV` varchar(255) DEFAULT NULL,
  `CODIGO_TIPO_ESTABELECIMENTO_PDV` bigint(20) DEFAULT NULL,
  `DESCRICAO_TIPO_ESTABELECIMENTO_PDV` varchar(255) DEFAULT NULL,
  `TIPO_EXPOSITOR` varchar(255) DEFAULT NULL,
  `CODIGO_TIPO_LICENCA_MUNICIPAL` bigint(20) DEFAULT NULL,
  `DESCRICAO_TIPO_LICENCA_MUNICIPAL` varchar(255) DEFAULT NULL,
  `CODIGO_TIPO_PONTO` bigint(20) DEFAULT NULL,
  `DESCRICAO_TIPO_PONTO` varchar(255) DEFAULT NULL,
  `HISTORICO_TITULARIDADE_COTA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK628869CC508A9E07` (`HISTORICO_TITULARIDADE_COTA_ID`),
  CONSTRAINT `FK628869CC508A9E07` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_ID`) REFERENCES `historico_titularidade_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_pdv_endereco`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_pdv_endereco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_pdv_endereco` (
  `HISTORICO_TITULARIDADE_COTA_PDV_ID` bigint(20) NOT NULL,
  `ENDERECO_BAIRRO` varchar(60) DEFAULT NULL,
  `ENDERECO_CEP` varchar(9) DEFAULT NULL,
  `ENDERECO_CIDADE` varchar(60) DEFAULT NULL,
  `ENDERECO_CODIGO_BAIRRO` int(11) DEFAULT NULL,
  `ENDERECO_CODIGO_CIDADE_IBGE` int(11) DEFAULT NULL,
  `ENDERECO_CODIGO_UF` int(11) DEFAULT NULL,
  `ENDERECO_COMPLEMENTO` varchar(60) DEFAULT NULL,
  `ENDERECO_LOGRADOURO` varchar(60) DEFAULT NULL,
  `ENDERECO_NUMERO` varchar(60) DEFAULT NULL,
  `ENDERECO_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `ENDERECO_TIPO_ENDERECO` varchar(255) DEFAULT NULL,
  `ENDERECO_TIPO_LOGRADOURO` varchar(255) DEFAULT NULL,
  `ENDERECO_UF` varchar(2) DEFAULT NULL,
  KEY `FK5B9C64DC1043021A` (`HISTORICO_TITULARIDADE_COTA_PDV_ID`),
  CONSTRAINT `FK5B9C64DC1043021A` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_PDV_ID`) REFERENCES `historico_titularidade_cota_pdv` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_pdv_telefone`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_pdv_telefone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_pdv_telefone` (
  `HISTORICO_TITULARIDADE_COTA_PDV_ID` bigint(20) NOT NULL,
  `DDD` varchar(255) DEFAULT NULL,
  `TELEFONE_NUMERO` varchar(255) DEFAULT NULL,
  `TELEFONE_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `TELEFONE_RAMAL` varchar(255) DEFAULT NULL,
  `TELEFONE_TIPO_TELEFONE` varchar(255) DEFAULT NULL,
  KEY `FKA35767FD1043021A` (`HISTORICO_TITULARIDADE_COTA_PDV_ID`),
  CONSTRAINT `FKA35767FD1043021A` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_PDV_ID`) REFERENCES `historico_titularidade_cota_pdv` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_pgto_caucao_liquida`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_pgto_caucao_liquida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_pgto_caucao_liquida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCONTO_NORMAL_COTA` decimal(18,4) DEFAULT NULL,
  `DESCONTO_REDUZIDO_COTA` decimal(18,4) DEFAULT NULL,
  `PERIODICIDADE_BOLETO` varchar(255) DEFAULT NULL,
  `PERCENTUAL_UTILIZADO_DESCONTO` decimal(18,4) DEFAULT NULL,
  `QTDE_PARCELAS_BOLETO` int(11) DEFAULT NULL,
  `TIPO_COBRANCA` varchar(255) NOT NULL,
  `VALOR_PARCELAS_BOLETO` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_pgto_caucao_liquida_dia_mes`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_pgto_caucao_liquida_dia_mes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_pgto_caucao_liquida_dia_mes` (
  `HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID` bigint(20) NOT NULL,
  `DIA_MES` int(11) DEFAULT NULL,
  KEY `FK945FFD10F244821E` (`HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID`),
  CONSTRAINT `FK945FFD10F244821E` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID`) REFERENCES `historico_titularidade_cota_pgto_caucao_liquida` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_pgto_caucao_liquida_dia_semana`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_pgto_caucao_liquida_dia_semana`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_pgto_caucao_liquida_dia_semana` (
  `HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID` bigint(20) NOT NULL,
  `DIA_SEMANA` int(11) DEFAULT NULL,
  KEY `FK98858024F244821E` (`HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID`),
  CONSTRAINT `FK98858024F244821E` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID`) REFERENCES `historico_titularidade_cota_pgto_caucao_liquida` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_referencia_cota`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_referencia_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_referencia_cota` (
  `HISTORICO_TITULARIDADE_COTA_ID` bigint(20) NOT NULL,
  `NUMERO_COTA` int(11) DEFAULT NULL,
  `PERCENTUAL` decimal(18,4) DEFAULT NULL,
  KEY `FKD4A376F0508A9E07` (`HISTORICO_TITULARIDADE_COTA_ID`),
  CONSTRAINT `FKD4A376F0508A9E07` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_ID`) REFERENCES `historico_titularidade_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_socio`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_socio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_socio` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CARGO` varchar(255) DEFAULT NULL,
  `ENDERECO_BAIRRO` varchar(60) DEFAULT NULL,
  `ENDERECO_CEP` varchar(9) DEFAULT NULL,
  `ENDERECO_CIDADE` varchar(60) DEFAULT NULL,
  `ENDERECO_CODIGO_BAIRRO` int(11) DEFAULT NULL,
  `ENDERECO_CODIGO_CIDADE_IBGE` int(11) DEFAULT NULL,
  `ENDERECO_CODIGO_UF` int(11) DEFAULT NULL,
  `ENDERECO_COMPLEMENTO` varchar(60) DEFAULT NULL,
  `ENDERECO_LOGRADOURO` varchar(60) DEFAULT NULL,
  `ENDERECO_NUMERO` varchar(60) DEFAULT NULL,
  `ENDERECO_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `ENDERECO_TIPO_ENDERECO` varchar(255) DEFAULT NULL,
  `ENDERECO_TIPO_LOGRADOURO` varchar(255) DEFAULT NULL,
  `ENDERECO_UF` varchar(2) DEFAULT NULL,
  `NOME` varchar(255) NOT NULL,
  `PRINCIPAL_SOCIO` tinyint(1) DEFAULT NULL,
  `DDD` varchar(255) DEFAULT NULL,
  `TELEFONE_NUMERO` varchar(255) DEFAULT NULL,
  `TELEFONE_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `TELEFONE_RAMAL` varchar(255) DEFAULT NULL,
  `TELEFONE_TIPO_TELEFONE` varchar(255) DEFAULT NULL,
  `HISTORICO_TITULARIDADE_COTA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKE2442F37508A9E07` (`HISTORICO_TITULARIDADE_COTA_ID`),
  CONSTRAINT `FKE2442F37508A9E07` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_ID`) REFERENCES `historico_titularidade_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historico_titularidade_cota_telefone`
--

DROP TABLE IF EXISTS `historico_titularidade_cota_telefone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_titularidade_cota_telefone` (
  `HISTORICO_TITULARIDADE_COTA_ID` bigint(20) NOT NULL,
  `DDD` varchar(255) DEFAULT NULL,
  `TELEFONE_NUMERO` varchar(255) DEFAULT NULL,
  `TELEFONE_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `TELEFONE_RAMAL` varchar(255) DEFAULT NULL,
  `TELEFONE_TIPO_TELEFONE` varchar(255) DEFAULT NULL,
  KEY `FK42B5A840508A9E07` (`HISTORICO_TITULARIDADE_COTA_ID`),
  CONSTRAINT `FK42B5A840508A9E07` FOREIGN KEY (`HISTORICO_TITULARIDADE_COTA_ID`) REFERENCES `historico_titularidade_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historicofechamentodiariomovimentovenda`
--

DROP TABLE IF EXISTS `historicofechamentodiariomovimentovenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historicofechamentodiariomovimentovenda` (
  `ID` bigint(20) NOT NULL,
  `DATA_VENCIMENTO` date DEFAULT NULL,
  `QUANTIDADE` decimal(18,4) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK534CD760D2FE34B7` (`ID_PRODUTO_EDICAO`),
  CONSTRAINT `FK534CD760D2FE34B7` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historiconumerocota`
--

DROP TABLE IF EXISTS `historiconumerocota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historiconumerocota` (
  `DATA_ALTERACAO` datetime NOT NULL,
  `ID_COTA` bigint(20) NOT NULL,
  `NUMERO_COTA` int(11) DEFAULT NULL,
  PRIMARY KEY (`DATA_ALTERACAO`,`ID_COTA`),
  KEY `FKE5CAF605F1916CB0` (`ID_COTA`),
  CONSTRAINT `FKE5CAF605F1916CB0` FOREIGN KEY (`ID_COTA`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hvnd`
--

DROP TABLE IF EXISTS `hvnd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hvnd` (
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
  `cota_id` bigint(20) DEFAULT NULL,
  KEY `hvnd_cota_id` (`COD_COTA_HVCT`,`cod_produto`,`num_edicao`,`produto_edicao_id`),
  KEY `hvnd_cota_status` (`STATUS`),
  KEY `cota_id` (`cota_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hvnd_produto_edicao_zero`
--

DROP TABLE IF EXISTS `hvnd_produto_edicao_zero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hvndaux`
--

DROP TABLE IF EXISTS `hvndaux`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hvndaux` (
  `produto_edicao_id` int(11) NOT NULL,
  PRIMARY KEY (`produto_edicao_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hvndaux2`
--

DROP TABLE IF EXISTS `hvndaux2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hvndaux2` (
  `produto_edicao_id` int(11) NOT NULL,
  `data_rec` date DEFAULT NULL,
  PRIMARY KEY (`produto_edicao_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `imovel`
--

DROP TABLE IF EXISTS `imovel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `imovel` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ENDERECO` varchar(255) NOT NULL,
  `NUMERO_REGISTRO` varchar(255) NOT NULL,
  `OBSERVACAO` varchar(255) NOT NULL,
  `PROPRIETARIO` varchar(255) NOT NULL,
  `VALOR` double NOT NULL,
  `GARANTIA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK80F40BF2851B41F2` (`GARANTIA_ID`),
  CONSTRAINT `FK80F40BF2851B41F2` FOREIGN KEY (`GARANTIA_ID`) REFERENCES `cota_garantia` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interface_execucao`
--

DROP TABLE IF EXISTS `interface_execucao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interface_execucao` (
  `ID` bigint(20) NOT NULL,
  `NOME` varchar(7) NOT NULL,
  `MASCARA_ARQUIVO` varchar(50) DEFAULT NULL,
  `descricao` varchar(255) NOT NULL,
  `extensao_arquivo` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOME` (`NOME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_chamada_encalhe_fornecedor`
--

DROP TABLE IF EXISTS `item_chamada_encalhe_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_chamada_encalhe_fornecedor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONTROLE` int(11) NOT NULL,
  `DATA_RECOLHIMENTO` date DEFAULT NULL,
  `FORMA_DEVOLUCAO` varchar(255) DEFAULT NULL,
  `NUMERO_DOCUMENTO` bigint(20) NOT NULL,
  `NUEMRO_ITEM` int(11) NOT NULL,
  `NUMERO_NOTA_ENVIO` bigint(20) NOT NULL,
  `PRECO_UNITARIO` decimal(18,4) DEFAULT NULL,
  `QTDE_DEVOLUCAO_APURADA` bigint(20) DEFAULT NULL,
  `QTDE_DEVOLUCAO_INFORMADA` bigint(20) DEFAULT NULL,
  `QTDE_DEVOLUCAO_PARCIAL` bigint(20) DEFAULT NULL,
  `QTDE_ENVIADA` bigint(20) NOT NULL,
  `QTDE_VENDA_APURADA` bigint(20) DEFAULT NULL,
  `QTDE_VENDA_INFORMADA` bigint(20) DEFAULT NULL,
  `REGIME_RECOLHIMENTO` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) NOT NULL,
  `TIPO_PRODUTO` varchar(255) NOT NULL,
  `VALOR_MARGEM_APURADO` decimal(18,4) DEFAULT NULL,
  `VALOR_MARGEM_INFORMADO` decimal(18,4) DEFAULT NULL,
  `VALOR_VENDA_APURADO` decimal(18,4) DEFAULT NULL,
  `VALOR_VENDA_INFORMADO` decimal(18,4) DEFAULT NULL,
  `CHAMADA_ENCALHE_FORNECEDOR_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `CODIGO_NOTA_ENVIO_MULTIPLA` varchar(255) DEFAULT NULL,
  `DIFERENCA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKE9A54E40E26473F4` (`CHAMADA_ENCALHE_FORNECEDOR_ID`),
  KEY `FKE9A54E40A53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FKE9A54E40A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FKE9A54E40E26473F4` FOREIGN KEY (`CHAMADA_ENCALHE_FORNECEDOR_ID`) REFERENCES `chamada_encalhe_fornecedor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_nota_fiscal_entrada`
--

DROP TABLE IF EXISTS `item_nota_fiscal_entrada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_nota_fiscal_entrada` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CFOP_PRODUTO` varchar(255) DEFAULT NULL,
  `CSOSN_PRODUTO` varchar(255) DEFAULT NULL,
  `CST_PRODUTO` varchar(255) DEFAULT NULL,
  `NCM_PRODUTO` varchar(255) DEFAULT NULL,
  `ALIQUOTA_ICMS_PRODUTO` decimal(18,4) DEFAULT NULL,
  `ALIQUOTA_IPI_PRODUTO` decimal(18,4) DEFAULT NULL,
  `BASE_CALCULO_PRODUTO` decimal(18,4) DEFAULT NULL,
  `DATA_LANCAMENTO` date NOT NULL,
  `DATA_RECOLHIMENTO` date NOT NULL,
  `DESCONTO` decimal(18,4) DEFAULT NULL,
  `ORIGEM` varchar(255) DEFAULT NULL,
  `PRECO` decimal(18,4) DEFAULT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `TIPO_LANCAMENTO` int(11) DEFAULT NULL,
  `UNIDADE_PRODUTO` bigint(20) DEFAULT NULL,
  `VALOR_ICMS_PRODUTO` decimal(18,4) DEFAULT NULL,
  `VALOR_IPI_PRODUTO` decimal(18,4) DEFAULT NULL,
  `NOTA_FISCAL_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3EDE8DFB7FFF790E` (`USUARIO_ID`),
  KEY `FK3EDE8DFBA53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `FK3EDE8DFBC74D9881` (`NOTA_FISCAL_ID`),
  CONSTRAINT `FK3EDE8DFB7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK3EDE8DFBA53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK3EDE8DFBC74D9881` FOREIGN KEY (`NOTA_FISCAL_ID`) REFERENCES `nota_fiscal_entrada` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_nota_fiscal_saida`
--

DROP TABLE IF EXISTS `item_nota_fiscal_saida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_nota_fiscal_saida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CFOP_PRODUTO` varchar(255) DEFAULT NULL,
  `CSOSN_PRODUTO` varchar(255) DEFAULT NULL,
  `CST_PRODUTO` varchar(255) DEFAULT NULL,
  `NCM_PRODUTO` varchar(255) DEFAULT NULL,
  `ALIQUOTA_ICMS_PRODUTO` decimal(18,4) DEFAULT NULL,
  `ALIQUOTA_IPI_PRODUTO` decimal(18,4) DEFAULT NULL,
  `BASE_CALCULO_PRODUTO` decimal(18,4) DEFAULT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `UNIDADE_PRODUTO` bigint(20) DEFAULT NULL,
  `VALOR_ICMS_PRODUTO` decimal(18,4) DEFAULT NULL,
  `VALOR_IPI_PRODUTO` decimal(18,4) DEFAULT NULL,
  `NOTA_FISCAL_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8FDD023CA53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `FK8FDD023CA05BE382` (`NOTA_FISCAL_ID`),
  CONSTRAINT `FK8FDD023CA05BE382` FOREIGN KEY (`NOTA_FISCAL_ID`) REFERENCES `nota_fiscal_saida` (`ID`),
  CONSTRAINT `FK8FDD023CA53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_receb_fisico`
--

DROP TABLE IF EXISTS `item_receb_fisico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_receb_fisico` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QTDE_FISICO` decimal(18,4) DEFAULT NULL,
  `DIFERENCA_ID` bigint(20) DEFAULT NULL,
  `ITEM_NF_ENTRADA_ID` bigint(20) NOT NULL,
  `RECEBIMENTO_FISICO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ITEM_NF_ENTRADA_ID` (`ITEM_NF_ENTRADA_ID`),
  KEY `FK1217CDA35A0FFAA9` (`DIFERENCA_ID`),
  KEY `FK1217CDA3B7A04D34` (`RECEBIMENTO_FISICO_ID`),
  KEY `FK1217CDA3749B54C1` (`ITEM_NF_ENTRADA_ID`),
  CONSTRAINT `FK1217CDA35A0FFAA9` FOREIGN KEY (`DIFERENCA_ID`) REFERENCES `diferenca` (`id`),
  CONSTRAINT `FK1217CDA3749B54C1` FOREIGN KEY (`ITEM_NF_ENTRADA_ID`) REFERENCES `item_nota_fiscal_entrada` (`ID`),
  CONSTRAINT `FK1217CDA3B7A04D34` FOREIGN KEY (`RECEBIMENTO_FISICO_ID`) REFERENCES `recebimento_fisico` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lancamento`
--

DROP TABLE IF EXISTS `lancamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lancamento` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ALTERADO_INTERFACE` tinyint(1) NOT NULL,
  `DATA_CRIACAO` date NOT NULL,
  `DATA_LCTO_DISTRIBUIDOR` date NOT NULL,
  `DATA_LCTO_PREVISTA` date NOT NULL,
  `DATA_REC_DISTRIB` date NOT NULL,
  `DATA_REC_PREVISTA` date NOT NULL,
  `DATA_FIN_MAT_DISTRIB` date DEFAULT NULL,
  `DATA_STATUS` datetime NOT NULL,
  `REPARTE` decimal(18,4) DEFAULT NULL,
  `REPARTE_PROMOCIONAL` decimal(18,4) DEFAULT NULL,
  `SEQUENCIA_MATRIZ` int(11) DEFAULT NULL,
  `STATUS` varchar(255) NOT NULL,
  `TIPO_LANCAMENTO` varchar(255) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) DEFAULT NULL,
  `EXPEDICAO_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  `NUMERO_LANCAMENTO` int(11) DEFAULT NULL,
  `PERIODO_LANCAMENTO_PARCIAL_ID` bigint(20) DEFAULT NULL,
  `NUMERO_REPROGRAMACOES` int(11) DEFAULT NULL,
  `ESTUDO_ID` bigint(20) DEFAULT NULL,
  `juramentado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK1D53917A73249C49` (`EXPEDICAO_ID`),
  KEY `FK1D53917AA53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `INDEX_DATA_LCTO_PREVISTA` (`DATA_LCTO_PREVISTA`),
  KEY `INDEX_DATA_LCTO_DISTRIBUIDOR` (`DATA_LCTO_DISTRIBUIDOR`),
  KEY `FK1D53917A7FFF790E` (`USUARIO_ID`),
  KEY `estudo_lancamento_id` (`ESTUDO_ID`),
  KEY `ndx_data_lcto_distribuidor` (`DATA_LCTO_DISTRIBUIDOR`),
  CONSTRAINT `FK1D53917A73249C49` FOREIGN KEY (`EXPEDICAO_ID`) REFERENCES `expedicao` (`ID`),
  CONSTRAINT `FK1D53917A7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK1D53917AA53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `lancamento_ibfk_1` FOREIGN KEY (`ESTUDO_ID`) REFERENCES `estudo` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER trigger_historico_lancamento_insert
AFTER INSERT ON lancamento
FOR EACH ROW
BEGIN

INSERT INTO historico_lancamento
	SET STATUS_NOVO = NEW.STATUS,
	LANCAMENTO_ID = NEW.ID,
	DATA_EDICAO = SYSDATE(),
	TIPO_EDICAO = 'INCLUSAO',
	USUARIO_ID = NEW.USUARIO_ID;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER trigger_historico_lancamento_update
AFTER UPDATE ON lancamento
FOR EACH ROW
BEGIN

INSERT INTO historico_lancamento
	SET STATUS_ANTIGO = OLD.STATUS,
	STATUS_NOVO = NEW.STATUS,
	LANCAMENTO_ID = NEW.ID,
	DATA_EDICAO = SYSDATE(),
	TIPO_EDICAO = 'ALTERACAO',
	USUARIO_ID = NEW.USUARIO_ID;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `lancamento_diferenca`
--

DROP TABLE IF EXISTS `lancamento_diferenca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lancamento_diferenca` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_PROCESSAMENTO` datetime DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `MOVIMENTO_ESTOQUE_ID` bigint(20) DEFAULT NULL,
  `MOTIVO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKE9CA6C10232FFB12` (`MOVIMENTO_ESTOQUE_ID`),
  CONSTRAINT `FKE9CA6C10232FFB12` FOREIGN KEY (`MOVIMENTO_ESTOQUE_ID`) REFERENCES `movimento_estoque` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lancamento_diferenca_movimento_estoque_cota`
--

DROP TABLE IF EXISTS `lancamento_diferenca_movimento_estoque_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lancamento_diferenca_movimento_estoque_cota` (
  `LANCAMENTO_DIFERENCA_ID` bigint(20) NOT NULL,
  `MOVIMENTO_ESTOQUE_COTA_ID` bigint(20) NOT NULL,
  UNIQUE KEY `MOVIMENTO_ESTOQUE_COTA_ID` (`MOVIMENTO_ESTOQUE_COTA_ID`),
  KEY `FKE95125D2C4673234` (`LANCAMENTO_DIFERENCA_ID`),
  KEY `FKE95125D2BBE20E9D` (`MOVIMENTO_ESTOQUE_COTA_ID`),
  CONSTRAINT `FKE95125D2BBE20E9D` FOREIGN KEY (`MOVIMENTO_ESTOQUE_COTA_ID`) REFERENCES `movimento_estoque_cota` (`ID`),
  CONSTRAINT `FKE95125D2C4673234` FOREIGN KEY (`LANCAMENTO_DIFERENCA_ID`) REFERENCES `lancamento_diferenca` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lancamento_genesio`
--

DROP TABLE IF EXISTS `lancamento_genesio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lancamento_genesio` (
  `codigoProduto` varchar(8) DEFAULT NULL,
  `edicaoProduto` int(11) DEFAULT NULL,
  `produto_edicao_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lancamento_item_receb_fisico`
--

DROP TABLE IF EXISTS `lancamento_item_receb_fisico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lancamento_item_receb_fisico` (
  `LANCAMENTO_ID` bigint(20) NOT NULL,
  `recebimentos_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`LANCAMENTO_ID`,`recebimentos_ID`),
  UNIQUE KEY `recebimentos_ID` (`recebimentos_ID`),
  KEY `FK175E9E1ECA768D72` (`recebimentos_ID`),
  KEY `FK175E9E1E45C07ACF` (`LANCAMENTO_ID`),
  CONSTRAINT `FK175E9E1E45C07ACF` FOREIGN KEY (`LANCAMENTO_ID`) REFERENCES `lancamento` (`ID`),
  CONSTRAINT `FK175E9E1ECA768D72` FOREIGN KEY (`recebimentos_ID`) REFERENCES `item_receb_fisico` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lancamento_parcial`
--

DROP TABLE IF EXISTS `lancamento_parcial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lancamento_parcial` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LANCAMENTO_INICIAL` date NOT NULL,
  `RECOLHIMENTO_FINAL` date NOT NULL,
  `STATUS` varchar(255) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PRODUTO_EDICAO_ID` (`PRODUTO_EDICAO_ID`),
  KEY `FKDDFB94DA53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FKDDFB94DA53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lancamentos_genesio`
--

DROP TABLE IF EXISTS `lancamentos_genesio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lancamentos_genesio` (
  `codigoProduto` varchar(8) DEFAULT NULL,
  `edicaoProduto` int(11) DEFAULT NULL,
  `produto_edicao_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_bairro`
--

DROP TABLE IF EXISTS `log_bairro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_bairro` (
  `BAI_NU` bigint(20) NOT NULL,
  `BAI_NO_ABREV` varchar(72) DEFAULT NULL,
  `BAI_NO` varchar(144) DEFAULT NULL,
  `UFE_SG` varchar(4) DEFAULT NULL,
  `LOC_NU` bigint(20) NOT NULL,
  PRIMARY KEY (`BAI_NU`),
  UNIQUE KEY `BAI_NU` (`BAI_NU`),
  KEY `FK2C25836044246A2B` (`LOC_NU`),
  KEY `FK2C258360C35A8410` (`LOC_NU`),
  CONSTRAINT `FK2C25836044246A2B` FOREIGN KEY (`LOC_NU`) REFERENCES `log_localidade` (`LOC_NU`),
  CONSTRAINT `FK2C258360C35A8410` FOREIGN KEY (`LOC_NU`) REFERENCES `log_localidade` (`LOC_NU`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_execucao`
--

DROP TABLE IF EXISTS `log_execucao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_execucao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_FIM` datetime DEFAULT NULL,
  `DATA_INICIO` datetime NOT NULL,
  `NOME_LOGIN_USUARIO` varchar(20) NOT NULL,
  `COD_DISTRIBUIDOR` varchar(20) DEFAULT NULL,
  `STATUS` varchar(1) DEFAULT NULL,
  `INTERFACE_EXECUCAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKA97F7A088D8C2EB` (`INTERFACE_EXECUCAO_ID`),
  CONSTRAINT `FKA97F7A088D8C2EB` FOREIGN KEY (`INTERFACE_EXECUCAO_ID`) REFERENCES `interface_execucao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_execucao_arquivo`
--

DROP TABLE IF EXISTS `log_execucao_arquivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_execucao_arquivo` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CAMINHO_ARQUIVO` varchar(350) DEFAULT NULL,
  `DISTRIBUIDOR_ID` int(11) NOT NULL,
  `MENSAGEM` varchar(500) DEFAULT NULL,
  `STATUS` varchar(1) DEFAULT NULL,
  `LOG_EXECUCAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK85183756502B1201` (`LOG_EXECUCAO_ID`),
  CONSTRAINT `FK85183756502B1201` FOREIGN KEY (`LOG_EXECUCAO_ID`) REFERENCES `log_execucao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_execucao_mensagem`
--

DROP TABLE IF EXISTS `log_execucao_mensagem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_execucao_mensagem` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MENSAGEM` varchar(500) DEFAULT NULL,
  `MENSAGEM_INFO` varchar(500) DEFAULT NULL,
  `NOME_ARQUIVO` varchar(50) NOT NULL,
  `NUMERO_LINHA` int(11) DEFAULT NULL,
  `EVENTO_EXECUCAO_ID` bigint(20) NOT NULL,
  `LOG_EXECUCAO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK478D3682189EC167` (`EVENTO_EXECUCAO_ID`),
  KEY `FK478D3682502B1201` (`LOG_EXECUCAO_ID`),
  CONSTRAINT `FK478D3682189EC167` FOREIGN KEY (`EVENTO_EXECUCAO_ID`) REFERENCES `evento_execucao` (`ID`),
  CONSTRAINT `FK478D3682502B1201` FOREIGN KEY (`LOG_EXECUCAO_ID`) REFERENCES `log_execucao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_faixa_uf`
--

DROP TABLE IF EXISTS `log_faixa_uf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_faixa_uf` (
  `UFE_SG` varchar(4) NOT NULL,
  `UFE_CEP_FIM` varchar(16) DEFAULT NULL,
  `UFE_CEP_INI` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`UFE_SG`),
  UNIQUE KEY `UFE_SG` (`UFE_SG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_localidade`
--

DROP TABLE IF EXISTS `log_localidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_localidade` (
  `LOC_NU` bigint(20) NOT NULL,
  `LOC_NO_ABREV` varchar(72) DEFAULT NULL,
  `CEP` varchar(16) DEFAULT NULL,
  `LOC_NU_SUB` bigint(20) DEFAULT NULL,
  `MUN_NU` bigint(20) DEFAULT NULL,
  `LOC_NO` varchar(144) DEFAULT NULL,
  `LOC_IN_SIT` varchar(2) DEFAULT NULL,
  `LOC_IN_TIPO_LOC` varchar(2) DEFAULT NULL,
  `UFE_SG` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`LOC_NU`),
  UNIQUE KEY `LOC_NU` (`LOC_NU`),
  KEY `FK768E9917B31F2A81` (`UFE_SG`),
  CONSTRAINT `FK768E9917B31F2A81` FOREIGN KEY (`UFE_SG`) REFERENCES `log_faixa_uf` (`UFE_SG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_logradouro`
--

DROP TABLE IF EXISTS `log_logradouro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_logradouro` (
  `LOG_NU` bigint(20) NOT NULL,
  `LOG_NO_ABREV` varchar(72) DEFAULT NULL,
  `CEP` varchar(16) DEFAULT NULL,
  `BAI_NU_FIM` bigint(20) DEFAULT NULL,
  `BAI_NU_INI` bigint(20) DEFAULT NULL,
  `LOG_COMPLEMENTO` varchar(200) DEFAULT NULL,
  `LOG_NO` varchar(144) DEFAULT NULL,
  `LOG_STA_TLO` varchar(2) DEFAULT NULL,
  `TLO_TX` varchar(72) DEFAULT NULL,
  `UFE_SG` varchar(4) DEFAULT NULL,
  `LOC_NU` bigint(20) NOT NULL,
  PRIMARY KEY (`LOG_NU`),
  UNIQUE KEY `LOG_NU` (`LOG_NU`),
  KEY `FK8656C46FC35A8410` (`LOC_NU`),
  CONSTRAINT `FK8656C46FC35A8410` FOREIGN KEY (`LOC_NU`) REFERENCES `log_localidade` (`LOC_NU`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `material_promocional`
--

DROP TABLE IF EXISTS `material_promocional`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `material_promocional` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mix_cota_produto`
--

DROP TABLE IF EXISTS `mix_cota_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mix_cota_produto` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATAHORA` datetime DEFAULT NULL,
  `REPARTE_MAX` bigint(20) DEFAULT NULL,
  `REPARTE_MED` bigint(20) DEFAULT NULL,
  `REPARTE_MIN` bigint(20) DEFAULT NULL,
  `ULTIMO_REPARTE` bigint(20) DEFAULT NULL,
  `VENDA_MED` bigint(20) DEFAULT NULL,
  `ID_COTA` bigint(20) DEFAULT NULL,
  `ID_PRODUTO` bigint(20) DEFAULT NULL,
  `ID_USUARIO` bigint(20) DEFAULT NULL,
  `CODIGO_ICD` varchar(255) DEFAULT NULL,
  `TIPO_CLASSIFICACAO_PRODUTO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7C18196F1916CB0` (`ID_COTA`),
  KEY `FK7C1819693864D4C` (`ID_USUARIO`),
  KEY `FK7C18196F34F8834` (`ID_PRODUTO`),
  KEY `FK7C1819610C84C95` (`TIPO_CLASSIFICACAO_PRODUTO_ID`),
  CONSTRAINT `FK7C1819610C84C95` FOREIGN KEY (`TIPO_CLASSIFICACAO_PRODUTO_ID`) REFERENCES `tipo_classificacao_produto` (`ID`),
  CONSTRAINT `FK7C1819693864D4C` FOREIGN KEY (`ID_USUARIO`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK7C18196F1916CB0` FOREIGN KEY (`ID_COTA`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FK7C18196F34F8834` FOREIGN KEY (`ID_PRODUTO`) REFERENCES `produto` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `motorista`
--

DROP TABLE IF EXISTS `motorista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `motorista` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CNH` varchar(255) DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `TRANSPORTADOR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8E127EECD90B1440` (`TRANSPORTADOR_ID`),
  CONSTRAINT `FK8E127EECD90B1440` FOREIGN KEY (`TRANSPORTADOR_ID`) REFERENCES `transportador` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mov_cred`
--

DROP TABLE IF EXISTS `mov_cred`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mov_cred` (
  `linha_inicial` varchar(1) DEFAULT NULL,
  `data` varchar(8) DEFAULT NULL,
  `numero_cota` int(11) DEFAULT NULL,
  `tipo_credito` int(11) DEFAULT NULL,
  `desc_credito` varchar(255) DEFAULT NULL,
  `valor` varchar(11) DEFAULT NULL,
  `valor_decimal` decimal(11,2) DEFAULT NULL,
  `data_real` date DEFAULT NULL,
  `cota_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mov_deb`
--

DROP TABLE IF EXISTS `mov_deb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mov_deb` (
  `linha_inicial` varchar(1) DEFAULT NULL,
  `data` varchar(8) DEFAULT NULL,
  `numero_cota` int(11) DEFAULT NULL,
  `tipo_debito` int(11) DEFAULT NULL,
  `desc_debito` varchar(255) DEFAULT NULL,
  `valor` varchar(11) DEFAULT NULL,
  `valor_decimal` decimal(11,2) DEFAULT NULL,
  `data_real` date DEFAULT NULL,
  `cota_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movimento_estoque`
--

DROP TABLE IF EXISTS `movimento_estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movimento_estoque` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `APROVADO_AUTOMATICAMENTE` tinyint(1) DEFAULT NULL,
  `DATA_APROVACAO` date DEFAULT NULL,
  `MOTIVO` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `DATA` date NOT NULL,
  `DATA_CRIACAO` date NOT NULL,
  `APROVADOR_ID` bigint(20) DEFAULT NULL,
  `TIPO_MOVIMENTO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `ESTOQUE_PRODUTO_ID` bigint(20) DEFAULT NULL,
  `ITEM_REC_FISICO_ID` bigint(20) DEFAULT NULL,
  `DATA_INTEGRACAO` date DEFAULT NULL,
  `STATUS_INTEGRACAO` varchar(255) DEFAULT NULL,
  `COD_ORIGEM_MOTIVO` varchar(255) DEFAULT NULL,
  `DAT_EMISSAO_DOC_ACERTO` datetime DEFAULT NULL,
  `NUM_DOC_ACERTO` bigint(20) DEFAULT NULL,
  `ORIGEM` varchar(50) DEFAULT 'MANUAL',
  `FURO_PRODUTO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBEB397FCB0C2856Cef448115` (`APROVADOR_ID`),
  KEY `FKBEB397FC7FFF790Eef448115` (`USUARIO_ID`),
  KEY `FKBEB397FC4E3C0541ef448115` (`TIPO_MOVIMENTO_ID`),
  KEY `FKEF44811545A3E9FA` (`ESTOQUE_PRODUTO_ID`),
  KEY `FKEF448115A53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `FKEF44811573BDE210` (`ITEM_REC_FISICO_ID`),
  KEY `NDX_STATUS` (`STATUS`),
  KEY `FK_movimento_estoque_furo_produto` (`FURO_PRODUTO_ID`),
  KEY `data_movimento_estoque` (`DATA`),
  CONSTRAINT `FKBEB397FC4E3C0541ef448115` FOREIGN KEY (`TIPO_MOVIMENTO_ID`) REFERENCES `tipo_movimento` (`ID`),
  CONSTRAINT `FKBEB397FC7FFF790Eef448115` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKBEB397FCB0C2856Cef448115` FOREIGN KEY (`APROVADOR_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKEF44811545A3E9FA` FOREIGN KEY (`ESTOQUE_PRODUTO_ID`) REFERENCES `estoque_produto` (`ID`),
  CONSTRAINT `FKEF44811573BDE210` FOREIGN KEY (`ITEM_REC_FISICO_ID`) REFERENCES `item_receb_fisico` (`ID`),
  CONSTRAINT `FKEF448115A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK_movimento_estoque_furo_produto` FOREIGN KEY (`FURO_PRODUTO_ID`) REFERENCES `furo_produto` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movimento_estoque_cota`
--

DROP TABLE IF EXISTS `movimento_estoque_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movimento_estoque_cota` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `APROVADO_AUTOMATICAMENTE` tinyint(1) DEFAULT NULL,
  `DATA_APROVACAO` date DEFAULT NULL,
  `MOTIVO` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `DATA` date NOT NULL,
  `DATA_CRIACAO` date NOT NULL,
  `APROVADOR_ID` bigint(20) DEFAULT NULL,
  `TIPO_MOVIMENTO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `STATUS_ESTOQUE_FINANCEIRO` varchar(255) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `ESTOQUE_PROD_COTA_ID` bigint(20) DEFAULT NULL,
  `ESTUDO_COTA_ID` bigint(20) DEFAULT NULL,
  `LANCAMENTO_ID` bigint(20) DEFAULT NULL,
  `DATA_INTEGRACAO` date DEFAULT NULL,
  `STATUS_INTEGRACAO` varchar(255) DEFAULT NULL,
  `MOVIMENTO_ESTOQUE_COTA_FURO_ID` bigint(20) DEFAULT NULL,
  `ORIGEM` varchar(50) DEFAULT 'MANUAL',
  `LANCAMENTO_DIFERENCA_ID` bigint(20) DEFAULT NULL,
  `DATA_LANCAMENTO_ORIGINAL` date DEFAULT NULL,
  `MOVIMENTO_FINANCEIRO_COTA_ID` bigint(20) DEFAULT NULL,
  `PRECO_COM_DESCONTO` decimal(18,4) DEFAULT NULL,
  `PRECO_VENDA` decimal(18,4) DEFAULT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  `NOTA_ENVIO_ITEM_NOTA_ENVIO_ID` bigint(20) DEFAULT NULL,
  `NOTA_ENVIO_ITEM_SEQUENCIA` int(11) DEFAULT NULL,
  `MOVIMENTO_ESTOQUE_COTA_JURAMENTADO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBEB397FCB0C2856C459444c3` (`APROVADOR_ID`),
  KEY `FKBEB397FC7FFF790E459444c3` (`USUARIO_ID`),
  KEY `FK459444C3714FA744` (`ESTUDO_COTA_ID`),
  KEY `FK459444C3C8181F74` (`COTA_ID`),
  KEY `FK459444C345C07ACF` (`LANCAMENTO_ID`),
  KEY `FK459444C362506D6B` (`ESTOQUE_PROD_COTA_ID`),
  KEY `FKBEB397FC4E3C0541459444c3` (`TIPO_MOVIMENTO_ID`),
  KEY `FK459444C3A53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `FK459444C370D7DF78` (`MOVIMENTO_ESTOQUE_COTA_FURO_ID`),
  KEY `FK459444C3C4673234` (`LANCAMENTO_DIFERENCA_ID`),
  KEY `FK459444C35644B37C` (`PRODUTO_EDICAO_ID`),
  KEY `FK459444C322F9188B` (`MOVIMENTO_FINANCEIRO_COTA_ID`),
  KEY `NDX_STATUS_ESTOQUE_FINANCEIRO` (`STATUS_ESTOQUE_FINANCEIRO`),
  KEY `NDX_DATA` (`DATA`),
  KEY `FKFDSA89F7CD8979AF` (`NOTA_ENVIO_ITEM_NOTA_ENVIO_ID`,`NOTA_ENVIO_ITEM_SEQUENCIA`),
  KEY `MOVIMENTO_ESTOQUE_COTA_JURAMENTADO_ID` (`MOVIMENTO_ESTOQUE_COTA_JURAMENTADO_ID`),
  KEY `NDX_MEC_NOTA_ENVIO` (`COTA_ID`,`ESTUDO_COTA_ID`,`NOTA_ENVIO_ITEM_NOTA_ENVIO_ID`,`NOTA_ENVIO_ITEM_SEQUENCIA`),
  CONSTRAINT `FK459444C322F9188B` FOREIGN KEY (`MOVIMENTO_FINANCEIRO_COTA_ID`) REFERENCES `movimento_financeiro_cota` (`ID`),
  CONSTRAINT `FK459444C345C07ACF` FOREIGN KEY (`LANCAMENTO_ID`) REFERENCES `lancamento` (`ID`),
  CONSTRAINT `FK459444C362506D6B` FOREIGN KEY (`ESTOQUE_PROD_COTA_ID`) REFERENCES `estoque_produto_cota` (`ID`),
  CONSTRAINT `FK459444C370D7DF78` FOREIGN KEY (`MOVIMENTO_ESTOQUE_COTA_FURO_ID`) REFERENCES `movimento_estoque_cota` (`id`),
  CONSTRAINT `FK459444C3714FA744` FOREIGN KEY (`ESTUDO_COTA_ID`) REFERENCES `estudo_cota` (`ID`),
  CONSTRAINT `FK459444C3A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK459444C3C4673234` FOREIGN KEY (`LANCAMENTO_DIFERENCA_ID`) REFERENCES `lancamento_diferenca` (`ID`),
  CONSTRAINT `FK459444C3C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FKBEB397FC4E3C0541459444c3` FOREIGN KEY (`TIPO_MOVIMENTO_ID`) REFERENCES `tipo_movimento` (`ID`),
  CONSTRAINT `FKBEB397FC7FFF790E459444c3` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKBEB397FCB0C2856C459444c3` FOREIGN KEY (`APROVADOR_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKFDSA89F7CD8979AF` FOREIGN KEY (`NOTA_ENVIO_ITEM_NOTA_ENVIO_ID`, `NOTA_ENVIO_ITEM_SEQUENCIA`) REFERENCES `nota_envio_item` (`NOTA_ENVIO_ID`, `SEQUENCIA`),
  CONSTRAINT `movimento_estoque_cota_ibfk_1` FOREIGN KEY (`MOVIMENTO_ESTOQUE_COTA_JURAMENTADO_ID`) REFERENCES `movimento_estoque_cota` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER ID_POPULATOR  
BEFORE INSERT ON movimento_estoque_cota  
FOR EACH ROW  
BEGIN  
    IF (NEW.id = -1) THEN  
        SET @new_id = (SELECT sequence_next_hi_value FROM SEQ_GENERATOR WHERE sequence_name = 'Movimento' FOR UPDATE);
        SET NEW.id = @new_id;
        UPDATE SEQ_GENERATOR SET sequence_next_hi_value = (@new_id + 1) WHERE sequence_next_hi_value = @new_id AND sequence_name = 'Movimento';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `movimento_financeiro_cota`
--

DROP TABLE IF EXISTS `movimento_financeiro_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movimento_financeiro_cota` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `APROVADO_AUTOMATICAMENTE` tinyint(1) DEFAULT NULL,
  `DATA_APROVACAO` date DEFAULT NULL,
  `MOTIVO` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `DATA` date NOT NULL,
  `DATA_CRIACAO` date NOT NULL,
  `APROVADOR_ID` bigint(20) DEFAULT NULL,
  `TIPO_MOVIMENTO_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `PARCELAS` int(11) DEFAULT NULL,
  `PRAZO` int(11) DEFAULT NULL,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `LANCAMENTO_MANUAL` tinyint(1) NOT NULL,
  `OBSERVACAO` varchar(255) DEFAULT NULL,
  `BAIXA_COBRANCA_ID` bigint(20) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `DATA_INTEGRACAO` date DEFAULT NULL,
  `STATUS_INTEGRACAO` varchar(255) DEFAULT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBEB397FCB0C2856Cc67ee7a9` (`APROVADOR_ID`),
  KEY `FKBEB397FC7FFF790Ec67ee7a9` (`USUARIO_ID`),
  KEY `FKC67EE7A9C8181F74` (`COTA_ID`),
  KEY `FKC67EE7A9E7EBBF1A` (`BAIXA_COBRANCA_ID`),
  KEY `FKBEB397FC4E3C0541c67ee7a9` (`TIPO_MOVIMENTO_ID`),
  KEY `FK_MOVIMENTO_FINANCEIRO_COTA_FORNECEDOR_ID` (`FORNECEDOR_ID`),
  CONSTRAINT `FKBEB397FC4E3C0541c67ee7a9` FOREIGN KEY (`TIPO_MOVIMENTO_ID`) REFERENCES `tipo_movimento` (`ID`),
  CONSTRAINT `FKBEB397FC7FFF790Ec67ee7a9` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKBEB397FCB0C2856Cc67ee7a9` FOREIGN KEY (`APROVADOR_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKC67EE7A9C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FKC67EE7A9E7EBBF1A` FOREIGN KEY (`BAIXA_COBRANCA_ID`) REFERENCES `baixa_cobranca` (`ID`),
  CONSTRAINT `FK_MOVIMENTO_FINANCEIRO_COTA_FORNECEDOR_ID` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER `ID_POPULATOR_FINANCEIRO` 
BEFORE INSERT ON `movimento_financeiro_cota` 
FOR EACH ROW BEGIN  
    IF (NEW.id = -1) THEN  
        SET @new_id = (SELECT sequence_next_hi_value FROM SEQ_GENERATOR WHERE sequence_name = 'Movimento' FOR UPDATE);
        SET NEW.id = @new_id;
        UPDATE SEQ_GENERATOR SET sequence_next_hi_value = (@new_id + 1) WHERE sequence_next_hi_value = @new_id AND sequence_name = 'Movimento';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ncm`
--

DROP TABLE IF EXISTS `ncm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ncm` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  `UNIDADE_MEDIDA` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `negociacao`
--

DROP TABLE IF EXISTS `negociacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `negociacao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATIVAR_PAGAMENTO_APOS_PARCELA` int(11) DEFAULT NULL,
  `COMISSAO_PARA_SALDO_DIVIDA` decimal(18,4) DEFAULT NULL,
  `ISENTA_ENCARGOS` tinyint(1) DEFAULT NULL,
  `NEGOCIACAO_AVULSA` tinyint(1) DEFAULT NULL,
  `VALOR_DIVIDA_PAGA_COMISSAO` decimal(18,4) DEFAULT NULL,
  `FORMA_COBRANCA_ID` bigint(20) DEFAULT NULL,
  `DATA_CRIACAO` datetime DEFAULT NULL,
  `VALOR_ORIGINAL` decimal(18,4) DEFAULT NULL,
  `COMISSAO_ORIGINAL_COTA` decimal(18,4) DEFAULT NULL,
  `TIPO_NEGOCIACAO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKFDCA12D5E34F875B` (`FORMA_COBRANCA_ID`),
  CONSTRAINT `FKFDCA12D5E34F875B` FOREIGN KEY (`FORMA_COBRANCA_ID`) REFERENCES `forma_cobranca` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `negociacao_cobranca_originaria`
--

DROP TABLE IF EXISTS `negociacao_cobranca_originaria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `negociacao_cobranca_originaria` (
  `NEGOCIACAO_ID` bigint(20) NOT NULL,
  `COBRANCA_ID` bigint(20) NOT NULL,
  UNIQUE KEY `COBRANCA_ID` (`COBRANCA_ID`),
  KEY `FKDFAF66FDD89C75C1` (`COBRANCA_ID`),
  KEY `FKDFAF66FDFCD23D41` (`NEGOCIACAO_ID`),
  CONSTRAINT `FKDFAF66FDD89C75C1` FOREIGN KEY (`COBRANCA_ID`) REFERENCES `cobranca` (`ID`),
  CONSTRAINT `FKDFAF66FDFCD23D41` FOREIGN KEY (`NEGOCIACAO_ID`) REFERENCES `negociacao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `negociacao_mov_finan`
--

DROP TABLE IF EXISTS `negociacao_mov_finan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `negociacao_mov_finan` (
  `NEGOCIACAO_ID` bigint(20) DEFAULT NULL,
  `MOV_FINAN_ID` bigint(20) DEFAULT NULL,
  KEY `fk_mvto_finan_id` (`MOV_FINAN_ID`),
  KEY `fk_negociacao_id` (`NEGOCIACAO_ID`),
  CONSTRAINT `fk_mvto_finan_id` FOREIGN KEY (`MOV_FINAN_ID`) REFERENCES `movimento_financeiro_cota` (`id`),
  CONSTRAINT `fk_negociacao_id` FOREIGN KEY (`NEGOCIACAO_ID`) REFERENCES `negociacao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_envio`
--

DROP TABLE IF EXISTS `nota_envio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_envio` (
  `numero` bigint(20) NOT NULL AUTO_INCREMENT,
  `CHAVE_ACESSO` varchar(44) DEFAULT NULL,
  `CODIGO_NATUREZA_OPERACAO` int(11) DEFAULT NULL,
  `dataEmissao` date DEFAULT NULL,
  `DESCRICAO_NATUREZA_OPERACAO` varchar(255) DEFAULT NULL,
  `CODIGO_BOX` int(11) DEFAULT NULL,
  `CODIGO_ROTA` varchar(255) DEFAULT NULL,
  `DESCRICAO_ROTA` varchar(255) DEFAULT NULL,
  `DOCUMENTO_DESTINATARIO` varchar(14) NOT NULL,
  `IE_DESTINATARIO` varchar(20) DEFAULT NULL,
  `NOME_DESTINATARIO` varchar(60) NOT NULL,
  `NOME_BOX` varchar(255) DEFAULT NULL,
  `NUMERO_COTA` int(11) NOT NULL,
  `DOCUMENTO_EMITENTE` varchar(14) NOT NULL,
  `IE_EMITENTE` varchar(14) NOT NULL,
  `NOME_EMITENTE` varchar(60) NOT NULL,
  `ENDERECO_ID_DESTINATARIO` bigint(20) NOT NULL,
  `PESSOA_DESTINATARIO_ID_REFERENCIA` bigint(20) DEFAULT NULL,
  `TELEFONE_ID_DESTINATARIO` bigint(20) DEFAULT NULL,
  `ENDERECO_ID_EMITENTE` bigint(20) NOT NULL,
  `PESSOA_EMITENTE_ID_REFERENCIADA` bigint(20) DEFAULT NULL,
  `TELEFONE_ID_EMITENTE` bigint(20) DEFAULT NULL,
  `NOTA_IMPRESSA` tinyint(1) NOT NULL,
  PRIMARY KEY (`numero`),
  KEY `FK59E3F72278441BC1` (`PESSOA_DESTINATARIO_ID_REFERENCIA`),
  KEY `FK59E3F722E7D38522` (`ENDERECO_ID_DESTINATARIO`),
  KEY `FK59E3F722B281499A` (`PESSOA_EMITENTE_ID_REFERENCIADA`),
  KEY `FK59E3F7224C92964` (`TELEFONE_ID_DESTINATARIO`),
  KEY `FK59E3F7228B188640` (`TELEFONE_ID_EMITENTE`),
  KEY `FK59E3F722742B297E` (`ENDERECO_ID_EMITENTE`),
  CONSTRAINT `FK59E3F7224C92964` FOREIGN KEY (`TELEFONE_ID_DESTINATARIO`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK59E3F72278441BC1` FOREIGN KEY (`PESSOA_DESTINATARIO_ID_REFERENCIA`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FK59E3F7228B188640` FOREIGN KEY (`TELEFONE_ID_EMITENTE`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK59E3F722B281499A` FOREIGN KEY (`PESSOA_EMITENTE_ID_REFERENCIADA`) REFERENCES `pessoa` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_envio_item`
--

DROP TABLE IF EXISTS `nota_envio_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_envio_item` (
  `SEQUENCIA` int(11) NOT NULL,
  `CODIGO_PRODUTO` varchar(60) NOT NULL,
  `DESCONTO` decimal(18,4) DEFAULT NULL,
  `NUMERO_EDICAO` bigint(20) NOT NULL,
  `PRECO_CAPA` decimal(18,4) DEFAULT NULL,
  `PUBLICACAO` varchar(120) NOT NULL,
  `REPARTE` decimal(18,4) DEFAULT NULL,
  `NOTA_ENVIO_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  `ESTUDO_COTA_ID` bigint(20) DEFAULT NULL,
  `SEQ_MATRIZ_LANCAMENTO` int(11) DEFAULT NULL,
  `FURO_PRODUTO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`NOTA_ENVIO_ID`,`SEQUENCIA`),
  KEY `FK18605A10401E39D3` (`NOTA_ENVIO_ID`),
  KEY `FK18605A10A53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `FK18605A10714FA744` (`ESTUDO_COTA_ID`),
  KEY `NEI_EC_ID_PE_ID` (`PRODUTO_EDICAO_ID`,`ESTUDO_COTA_ID`),
  CONSTRAINT `FK18605A10401E39D3` FOREIGN KEY (`NOTA_ENVIO_ID`) REFERENCES `nota_envio` (`numero`),
  CONSTRAINT `FK18605A10714FA744` FOREIGN KEY (`ESTUDO_COTA_ID`) REFERENCES `estudo_cota` (`ID`),
  CONSTRAINT `FK18605A10A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK_nota_envio_item_estudo_cota` FOREIGN KEY (`ESTUDO_COTA_ID`) REFERENCES `estudo_cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_encargo_financeiro_produto`
--

DROP TABLE IF EXISTS `nota_fiscal_encargo_financeiro_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_encargo_financeiro_produto` (
  `ID` bigint(20) NOT NULL,
  `CST_COFINS` int(11) DEFAULT NULL,
  `PER_ALIQ_COFINS` decimal(18,4) DEFAULT NULL,
  `QTD_VENDIDA_COFINS` decimal(18,4) DEFAULT NULL,
  `VLR_COFINS` decimal(18,4) DEFAULT NULL,
  `VLR_ALIQ_COFINS` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_COFINS` decimal(18,4) DEFAULT NULL,
  `CST_COFINS_ST` int(11) DEFAULT NULL,
  `PER_ALIQ_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `QTD_VENDIDA_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_ALIQ_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `CST_PIS` int(11) DEFAULT NULL,
  `PER_ALIQ_PIS` decimal(18,4) DEFAULT NULL,
  `QTD_VENDIDA_PIS` decimal(18,4) DEFAULT NULL,
  `VLR_PIS` decimal(18,4) DEFAULT NULL,
  `VLR_ALIQ_PIS` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_PIS` decimal(18,4) DEFAULT NULL,
  `CST_PIS_ST` int(11) DEFAULT NULL,
  `PER_ALIQ_PIS_ST` decimal(18,4) DEFAULT NULL,
  `QTD_VENDIDA_PIS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_PIS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_ALIQ_PIS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_PIS_ST` decimal(18,4) DEFAULT NULL,
  `NOTA_FISCAL_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_SERVICO_SEQUENCIA` int(11) DEFAULT NULL,
  `PERCENTUAL_ADCIONADO_ST` decimal(18,4) DEFAULT NULL,
  `MODELIDADE_ST` int(11) DEFAULT NULL,
  `ORIGEM_ST` int(11) DEFAULT NULL,
  `PERCENTUAL_REDUCAO_ST` decimal(18,4) DEFAULT NULL,
  `ALIQUOTA_ICMS_ST` decimal(18,4) DEFAULT NULL,
  `CST_ICMS_ST` varchar(2) DEFAULT NULL,
  `VLR_ICMS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_ICMS_ST` decimal(18,4) DEFAULT NULL,
  `MOTIVO_DESONERACAO` int(11) DEFAULT NULL,
  `MODELIDADE` int(11) DEFAULT NULL,
  `ORIGEM` int(11) NOT NULL,
  `PERCENTUAL_REDUCAO` decimal(18,4) DEFAULT NULL,
  `ALIQUOTA_ICMS` decimal(18,4) DEFAULT NULL,
  `CST_ICMS` varchar(2) NOT NULL,
  `VLR_ICMS` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_ICMS` decimal(18,4) DEFAULT NULL,
  `CLASSE_ENQUADRAMENTO_IPI` varchar(5) DEFAULT NULL,
  `CNPJ_PRODUTOR_IPI` varchar(14) DEFAULT NULL,
  `CODIGO_ENQUADRAMENTO_IPI` varchar(3) DEFAULT NULL,
  `CODIGO_SELO_IPI` varchar(60) DEFAULT NULL,
  `QUANTIDADE_SELO_IPI` bigint(20) DEFAULT NULL,
  `QUANTIDADE_UNIDADES` double DEFAULT NULL,
  `VALOR_UNIDADE_TRIBUTAVEL_IPI` double DEFAULT NULL,
  `ALIQUOTA_IPI` decimal(18,4) DEFAULT NULL,
  `CST_IPI` varchar(2) DEFAULT NULL,
  `VLR_IPI` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_IPI` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKE8D1773F35367812c7153222` (`NOTA_FISCAL_ID`,`PRODUTO_SERVICO_SEQUENCIA`),
  CONSTRAINT `FKE8D1773F35367812c7153222` FOREIGN KEY (`NOTA_FISCAL_ID`, `PRODUTO_SERVICO_SEQUENCIA`) REFERENCES `nota_fiscal_produto_servico` (`NOTA_FISCAL_ID`, `SEQUENCIA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_encargo_financeiro_servico`
--

DROP TABLE IF EXISTS `nota_fiscal_encargo_financeiro_servico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_encargo_financeiro_servico` (
  `ID` bigint(20) NOT NULL,
  `CST_COFINS` int(11) DEFAULT NULL,
  `PER_ALIQ_COFINS` decimal(18,4) DEFAULT NULL,
  `QTD_VENDIDA_COFINS` decimal(18,4) DEFAULT NULL,
  `VLR_COFINS` decimal(18,4) DEFAULT NULL,
  `VLR_ALIQ_COFINS` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_COFINS` decimal(18,4) DEFAULT NULL,
  `CST_COFINS_ST` int(11) DEFAULT NULL,
  `PER_ALIQ_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `QTD_VENDIDA_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_ALIQ_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_COFINS_ST` decimal(18,4) DEFAULT NULL,
  `CST_PIS` int(11) DEFAULT NULL,
  `PER_ALIQ_PIS` decimal(18,4) DEFAULT NULL,
  `QTD_VENDIDA_PIS` decimal(18,4) DEFAULT NULL,
  `VLR_PIS` decimal(18,4) DEFAULT NULL,
  `VLR_ALIQ_PIS` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_PIS` decimal(18,4) DEFAULT NULL,
  `CST_PIS_ST` int(11) DEFAULT NULL,
  `PER_ALIQ_PIS_ST` decimal(18,4) DEFAULT NULL,
  `QTD_VENDIDA_PIS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_PIS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_ALIQ_PIS_ST` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_PIS_ST` decimal(18,4) DEFAULT NULL,
  `NOTA_FISCAL_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_SERVICO_SEQUENCIA` int(11) DEFAULT NULL,
  `COD_MUNICIPIO` int(11) NOT NULL,
  `ITEM_LISTA_SERVICO` int(11) NOT NULL,
  `ALIQUOTA_ISSQN` decimal(18,4) DEFAULT NULL,
  `CST_ISSQN` varchar(1) NOT NULL,
  `VLR_ISSQN` decimal(18,4) DEFAULT NULL,
  `VLR_BASE_CALC_ISSQN` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKE8D1773F353678124fcb29e8` (`NOTA_FISCAL_ID`,`PRODUTO_SERVICO_SEQUENCIA`),
  CONSTRAINT `FKE8D1773F353678124fcb29e8` FOREIGN KEY (`NOTA_FISCAL_ID`, `PRODUTO_SERVICO_SEQUENCIA`) REFERENCES `nota_fiscal_produto_servico` (`NOTA_FISCAL_ID`, `SEQUENCIA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_entrada`
--

DROP TABLE IF EXISTS `nota_fiscal_entrada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_entrada` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ISSQN_BASE` decimal(18,4) DEFAULT NULL,
  `ISSQN_TOTAL` decimal(18,4) DEFAULT NULL,
  `ISSQN_VALOR` decimal(18,4) DEFAULT NULL,
  `AMBIENTE` varchar(255) DEFAULT NULL,
  `CHAVE_ACESSO` varchar(255) DEFAULT NULL,
  `DATA_EMISSAO` datetime NOT NULL,
  `DATA_EXPEDICAO` datetime NOT NULL,
  `EMISSOR_INSCRICAO_ESTADUAL_SUBSTITUTO` varchar(255) DEFAULT NULL,
  `EMISSOR_INSCRICAO_MUNICIPAL` varchar(255) DEFAULT NULL,
  `EMITIDA` tinyint(1) NOT NULL,
  `FORMA_PAGAMENTO` varchar(255) DEFAULT NULL,
  `FRETE` int(11) DEFAULT NULL,
  `HORA_SAIDA` varchar(255) DEFAULT NULL,
  `INFORMACOES_COMPLEMENTARES` varchar(255) DEFAULT NULL,
  `MOVIMENTO_INTEGRACAO` varchar(255) DEFAULT NULL,
  `NATUREZA_OPERACAO` varchar(255) DEFAULT NULL,
  `NUMERO` bigint(20) DEFAULT NULL,
  `NUMERO_FATURA` varchar(255) DEFAULT NULL,
  `PROTOCOLO` varchar(255) DEFAULT NULL,
  `SERIE` varchar(255) DEFAULT NULL,
  `STATUS_EMISSAO` varchar(255) DEFAULT NULL,
  `STATUS_RECEBIMENTO` varchar(255) DEFAULT NULL,
  `STATUS_EMISSAO_NFE` varchar(255) DEFAULT NULL,
  `TIPO_EMISSAO_NFE` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_ANTT` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_CNPJ` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_ENDERECO` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_ESPECIE` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_INSCRICAO_ESTADUAL` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_MARCA` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_MUNICIPIO` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_NOME` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_NUMERACAO` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_PESO_BRUTO` decimal(18,4) DEFAULT NULL,
  `TRANSPORTADORA_PESO_LIQUIDO` decimal(18,4) DEFAULT NULL,
  `TRANSPORTADORA_PLACA_VEICULO` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_PLACA_VEICULO_UF` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_QUANTIDADE` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_UF` varchar(255) DEFAULT NULL,
  `VALOR_BASE_ICMS` decimal(18,4) DEFAULT NULL,
  `VALOR_BASE_ICMS_SUBSTITUTO` decimal(18,4) DEFAULT NULL,
  `VALOR_BRUTO` decimal(18,4) DEFAULT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  `VALOR_FATURA` decimal(18,4) DEFAULT NULL,
  `VALOR_FRETE` decimal(18,4) DEFAULT NULL,
  `VALOR_ICMS` decimal(18,4) DEFAULT NULL,
  `VALOR_ICMS_SUBSTITUTO` decimal(18,4) DEFAULT NULL,
  `VALOR_IPI` decimal(18,4) DEFAULT NULL,
  `VALOR_LIQUIDO` decimal(18,4) DEFAULT NULL,
  `VALOR_NF` decimal(18,4) DEFAULT NULL,
  `VALOR_OUTRO` decimal(18,4) DEFAULT NULL,
  `VALOR_PRODUTOS` decimal(18,4) DEFAULT NULL,
  `VALOR_SEGURO` decimal(18,4) DEFAULT NULL,
  `VERSAO` varchar(255) DEFAULT NULL,
  `ORIGEM` varchar(255) NOT NULL,
  `STATUS_NOTA_FISCAL` varchar(255) NOT NULL,
  `TIPO_NF_ID` bigint(20) NOT NULL,
  `CFOP_ID` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  `CONTROLE_CONFERENCIA_ENCALHE_COTA_ID` bigint(20) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  `pj_id` bigint(20) DEFAULT NULL,
  `DATA_RECEBIMENTO` datetime DEFAULT NULL,
  `VALOR_INFORMADO` decimal(18,4) DEFAULT NULL,
  `NUMERO_NOTA_ENVIO` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK90AD5277FFF790E` (`USUARIO_ID`),
  KEY `FK90AD5277FEEEECC` (`TIPO_NF_ID`),
  KEY `FK90AD527C8181F74` (`COTA_ID`),
  KEY `FK90AD527D3D96F81` (`CONTROLE_CONFERENCIA_ENCALHE_COTA_ID`),
  KEY `FK90AD5279808F874` (`FORNECEDOR_ID`),
  KEY `FK90AD5277AB6324F` (`CFOP_ID`),
  KEY `FK90AD527479FD586` (`pj_id`),
  CONSTRAINT `FK90AD527479FD586` FOREIGN KEY (`pj_id`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FK90AD5277AB6324F` FOREIGN KEY (`CFOP_ID`) REFERENCES `cfop` (`ID`),
  CONSTRAINT `FK90AD5277FEEEECC` FOREIGN KEY (`TIPO_NF_ID`) REFERENCES `tipo_nota_fiscal` (`ID`),
  CONSTRAINT `FK90AD5277FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK90AD5279808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK90AD527C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FK90AD527D3D96F81` FOREIGN KEY (`CONTROLE_CONFERENCIA_ENCALHE_COTA_ID`) REFERENCES `controle_conferencia_encalhe_cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_novo`
--

DROP TABLE IF EXISTS `nota_fiscal_novo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_novo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONDICAO` varchar(255) DEFAULT NULL,
  `DATA_EMISSAO` date NOT NULL,
  `DATA_ENTRADA_CONTIGENCIA` datetime DEFAULT NULL,
  `DATA_SAIDA_ENTRADA` date DEFAULT NULL,
  `DESCRICAO_NATUREZA_OPERACAO` varchar(60) NOT NULL,
  `FINALIDADE_EMISSAO` int(11) DEFAULT NULL,
  `INDICADOR_FORMA_PAGAMENTO` int(11) NOT NULL,
  `HORA_SAIDA_ENTRADA` time DEFAULT NULL,
  `JUSTIFICATIVA_ENTRADA_CONTIGENCIA` varchar(256) DEFAULT NULL,
  `NUMERO_DOCUMENTO_FISCAL` bigint(20) NOT NULL,
  `SERIE` int(11) NOT NULL,
  `TIPO_EMISSAO` int(11) DEFAULT NULL,
  `TIPO_OPERACAO` int(11) NOT NULL,
  `DOCUMENTO_DESTINATARIO` varchar(14) NOT NULL,
  `EMAIL_DESTINATARIO` varchar(60) DEFAULT NULL,
  `IE_DESTINATARIO` varchar(14) DEFAULT NULL,
  `ISUF_DESTINATARIO` varchar(9) DEFAULT NULL,
  `NOME_DESTINATARIO` varchar(60) NOT NULL,
  `NOME_FANTASIA_DESTINATARIO` varchar(60) DEFAULT NULL,
  `CNAE_EMITENTE` varchar(1) DEFAULT NULL,
  `DOCUMENTO_EMITENTE` varchar(14) NOT NULL,
  `IE_EMITENTE` varchar(14) NOT NULL,
  `IE_SUBSTITUTO_TRIBUTARIO_EMITENTE` varchar(14) DEFAULT NULL,
  `IM_EMITENTE` varchar(15) DEFAULT NULL,
  `NOME_EMITENTE` varchar(60) NOT NULL,
  `NOME_FANTASIA_EMITENTE` varchar(60) DEFAULT NULL,
  `CRT_EMITENTE` int(11) DEFAULT NULL,
  `INF_CPL` varchar(5000) DEFAULT NULL,
  `CHAVE_ACESSO` varchar(44) DEFAULT NULL,
  `DATA_RECEBIMENTO` datetime DEFAULT NULL,
  `MOTIVO` varchar(255) DEFAULT NULL,
  `PROTOCOLO` bigint(20) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `DOCUMENTO_TRANS` varchar(14) DEFAULT NULL,
  `IE_TRANS` varchar(14) DEFAULT NULL,
  `MODALIDADE_FRENTE` int(11) NOT NULL,
  `MUN_TRANS` varchar(60) DEFAULT NULL,
  `NOME_TRANS` varchar(60) DEFAULT NULL,
  `UF_TRANS` varchar(2) DEFAULT NULL,
  `PLACA_VEICULO_TRANS` varchar(7) DEFAULT NULL,
  `RNTC_VEICULO_TRANS` varchar(20) DEFAULT NULL,
  `UF_VEICULO_TRANS` varchar(2) DEFAULT NULL,
  `VL_BC_ICMS` decimal(18,4) DEFAULT NULL,
  `VL_BC_ICMS_ST` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_COFINS` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_DESCONTO` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_FRETE` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_ICMS` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_ICMS_ST` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_IPI` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_NF` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_OUTRO` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_PIS` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_PRODUTOS` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_SEGURO` decimal(18,4) DEFAULT NULL,
  `STATUS_PROCESSAMENTO_INTERNO` varchar(255) DEFAULT NULL,
  `TIPO_NOTA_FISCAL_ID` bigint(20) DEFAULT NULL,
  `ENDERECO_ID_DESTINATARIO` bigint(20) NOT NULL,
  `PESSOA_DESTINATARIO_ID_REFERENCIA` bigint(20) DEFAULT NULL,
  `TELEFONE_ID_DESTINATARIO` bigint(20) DEFAULT NULL,
  `ENDERECO_ID_EMITENTE` bigint(20) NOT NULL,
  `PESSOA_EMITENTE_ID_REFERENCIADA` bigint(20) DEFAULT NULL,
  `TELEFONE_ID_EMITENTE` bigint(20) DEFAULT NULL,
  `ENDERECO_ID_TRANS` bigint(20) DEFAULT NULL,
  `NOTA_IMPRESSA` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ENDERECO_ID_DESTINATARIO` (`ENDERECO_ID_DESTINATARIO`),
  UNIQUE KEY `ENDERECO_ID_EMITENTE` (`ENDERECO_ID_EMITENTE`),
  KEY `FK933C2B4A78441BC1` (`PESSOA_DESTINATARIO_ID_REFERENCIA`),
  KEY `FK933C2B4AE7D38522` (`ENDERECO_ID_DESTINATARIO`),
  KEY `FK933C2B4AB281499A` (`PESSOA_EMITENTE_ID_REFERENCIADA`),
  KEY `FK933C2B4A5CF4E32B` (`TIPO_NOTA_FISCAL_ID`),
  KEY `FK933C2B4A4C92964` (`TELEFONE_ID_DESTINATARIO`),
  KEY `FK933C2B4A8B188640` (`TELEFONE_ID_EMITENTE`),
  KEY `FK933C2B4A742B297E` (`ENDERECO_ID_EMITENTE`),
  KEY `FK933C2B4A7A98715D` (`ENDERECO_ID_TRANS`),
  CONSTRAINT `FK933C2B4A4C92964` FOREIGN KEY (`TELEFONE_ID_DESTINATARIO`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK933C2B4A5CF4E32B` FOREIGN KEY (`TIPO_NOTA_FISCAL_ID`) REFERENCES `tipo_nota_fiscal` (`ID`),
  CONSTRAINT `FK933C2B4A742B297E` FOREIGN KEY (`ENDERECO_ID_EMITENTE`) REFERENCES `endereco` (`ID`),
  CONSTRAINT `FK933C2B4A78441BC1` FOREIGN KEY (`PESSOA_DESTINATARIO_ID_REFERENCIA`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FK933C2B4A7A98715D` FOREIGN KEY (`ENDERECO_ID_TRANS`) REFERENCES `endereco` (`ID`),
  CONSTRAINT `FK933C2B4A8B188640` FOREIGN KEY (`TELEFONE_ID_EMITENTE`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK933C2B4AB281499A` FOREIGN KEY (`PESSOA_EMITENTE_ID_REFERENCIADA`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FK933C2B4AE7D38522` FOREIGN KEY (`ENDERECO_ID_DESTINATARIO`) REFERENCES `endereco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_processo`
--

DROP TABLE IF EXISTS `nota_fiscal_processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_processo` (
  `NOTA_FISCAL_ID` bigint(20) NOT NULL,
  `PROCESSO` varchar(255) DEFAULT NULL,
  KEY `FK593C1AF077E1AF62` (`NOTA_FISCAL_ID`),
  CONSTRAINT `FK593C1AF077E1AF62` FOREIGN KEY (`NOTA_FISCAL_ID`) REFERENCES `nota_fiscal_novo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_produto_servico`
--

DROP TABLE IF EXISTS `nota_fiscal_produto_servico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_produto_servico` (
  `SEQUENCIA` int(11) NOT NULL,
  `CFOP` int(11) NOT NULL,
  `CODIGO_BARRAS` bigint(20) NOT NULL,
  `CODIGO_PRODUTO` varchar(60) NOT NULL,
  `DESCRICAO_PRODUTO` varchar(120) NOT NULL,
  `EXTIPI` bigint(20) DEFAULT NULL,
  `NCM` bigint(20) NOT NULL,
  `QUANTIDADE_COMERCIAL` decimal(18,4) DEFAULT NULL,
  `UNIDADE_COMERCIAL` varchar(6) NOT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  `VALOR_FRETE` decimal(18,4) DEFAULT NULL,
  `VALOR_OUTROS` decimal(18,4) DEFAULT NULL,
  `VALOR_SERGURO` decimal(18,4) DEFAULT NULL,
  `VALOR_TOTAL_BRUTO` decimal(18,4) DEFAULT NULL,
  `VALOR_UNITARIO_COMERCIAL` decimal(18,4) DEFAULT NULL,
  `NOTA_FISCAL_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`NOTA_FISCAL_ID`,`SEQUENCIA`),
  KEY `FK65435E29A53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `FK65435E2977E1AF62` (`NOTA_FISCAL_ID`),
  CONSTRAINT `FK65435E2977E1AF62` FOREIGN KEY (`NOTA_FISCAL_ID`) REFERENCES `nota_fiscal_novo` (`id`),
  CONSTRAINT `FK65435E29A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_produto_servico_movimento_estoque_cota`
--

DROP TABLE IF EXISTS `nota_fiscal_produto_servico_movimento_estoque_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_produto_servico_movimento_estoque_cota` (
  `NOTA_FISCAL_ID` bigint(20) NOT NULL,
  `PRODUTO_SERVICO_SEQUENCIA` int(11) NOT NULL,
  `MOVIMENTO_ESTOQUE_COTA_ID` bigint(20) NOT NULL,
  KEY `FK1C0EE79935367812` (`NOTA_FISCAL_ID`,`PRODUTO_SERVICO_SEQUENCIA`),
  KEY `FK1C0EE799BBE20E9D` (`MOVIMENTO_ESTOQUE_COTA_ID`),
  CONSTRAINT `FK1C0EE79935367812` FOREIGN KEY (`NOTA_FISCAL_ID`, `PRODUTO_SERVICO_SEQUENCIA`) REFERENCES `nota_fiscal_produto_servico` (`NOTA_FISCAL_ID`, `SEQUENCIA`),
  CONSTRAINT `FK1C0EE799BBE20E9D` FOREIGN KEY (`MOVIMENTO_ESTOQUE_COTA_ID`) REFERENCES `movimento_estoque_cota` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_referenciada`
--

DROP TABLE IF EXISTS `nota_fiscal_referenciada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_referenciada` (
  `CHAVE_ACESSO` decimal(18,4) NOT NULL DEFAULT '0.0000',
  `CHAVE_ACESSO_CTE` decimal(18,4) DEFAULT NULL,
  `CNPJ` varchar(14) NOT NULL,
  `CODIGO_UF` int(11) NOT NULL,
  `DATA_EMISSAO` date NOT NULL,
  `MODELO_DOCUMENTO_FISCAL` int(11) NOT NULL,
  `NUMERO_DOCUMENTO_FISCAL` bigint(20) NOT NULL,
  `SERIE` int(11) NOT NULL,
  `NOTA_FISCAL_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`CHAVE_ACESSO`,`NOTA_FISCAL_ID`),
  KEY `FK41581DFF77E1AF62` (`NOTA_FISCAL_ID`),
  CONSTRAINT `FK41581DFF77E1AF62` FOREIGN KEY (`NOTA_FISCAL_ID`) REFERENCES `nota_fiscal_novo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_retencao_icms_transporte`
--

DROP TABLE IF EXISTS `nota_fiscal_retencao_icms_transporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_retencao_icms_transporte` (
  `id` bigint(20) NOT NULL,
  `CFOP` int(11) NOT NULL,
  `CODIGO_MUNICIPIO` bigint(20) NOT NULL,
  `PER_ALIQUOTA` decimal(18,4) DEFAULT NULL,
  `VL_BC_RET_ICMS` decimal(18,4) DEFAULT NULL,
  `VL_ICMS` decimal(18,4) DEFAULT NULL,
  `VL_SERVICO` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4B41D11350D82C52` (`id`),
  CONSTRAINT `FK4B41D11350D82C52` FOREIGN KEY (`id`) REFERENCES `nota_fiscal_novo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_saida`
--

DROP TABLE IF EXISTS `nota_fiscal_saida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_saida` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ISSQN_BASE` decimal(18,4) DEFAULT NULL,
  `ISSQN_TOTAL` decimal(18,4) DEFAULT NULL,
  `ISSQN_VALOR` decimal(18,4) DEFAULT NULL,
  `AMBIENTE` varchar(255) DEFAULT NULL,
  `CHAVE_ACESSO` varchar(255) DEFAULT NULL,
  `DATA_EMISSAO` datetime NOT NULL,
  `DATA_EXPEDICAO` datetime NOT NULL,
  `EMISSOR_INSCRICAO_ESTADUAL_SUBSTITUTO` varchar(255) DEFAULT NULL,
  `EMISSOR_INSCRICAO_MUNICIPAL` varchar(255) DEFAULT NULL,
  `EMITIDA` tinyint(1) NOT NULL,
  `FORMA_PAGAMENTO` varchar(255) DEFAULT NULL,
  `FRETE` int(11) DEFAULT NULL,
  `HORA_SAIDA` varchar(255) DEFAULT NULL,
  `INFORMACOES_COMPLEMENTARES` varchar(255) DEFAULT NULL,
  `MOVIMENTO_INTEGRACAO` varchar(255) DEFAULT NULL,
  `NATUREZA_OPERACAO` varchar(255) DEFAULT NULL,
  `NUMERO` bigint(20) NOT NULL,
  `NUMERO_FATURA` varchar(255) DEFAULT NULL,
  `PROTOCOLO` varchar(255) DEFAULT NULL,
  `SERIE` varchar(255) NOT NULL,
  `STATUS_EMISSAO` varchar(255) DEFAULT NULL,
  `STATUS_EMISSAO_NFE` varchar(255) DEFAULT NULL,
  `TIPO_EMISSAO_NFE` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_ANTT` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_CNPJ` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_ENDERECO` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_ESPECIE` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_INSCRICAO_ESTADUAL` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_MARCA` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_MUNICIPIO` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_NOME` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_NUMERACAO` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_PESO_BRUTO` decimal(18,4) DEFAULT NULL,
  `TRANSPORTADORA_PESO_LIQUIDO` decimal(18,4) DEFAULT NULL,
  `TRANSPORTADORA_PLACA_VEICULO` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_PLACA_VEICULO_UF` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_QUANTIDADE` varchar(255) DEFAULT NULL,
  `TRANSPORTADORA_UF` varchar(255) DEFAULT NULL,
  `VALOR_BASE_ICMS` decimal(18,4) DEFAULT NULL,
  `VALOR_BASE_ICMS_SUBSTITUTO` decimal(18,4) DEFAULT NULL,
  `VALOR_BRUTO` decimal(18,4) DEFAULT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  `VALOR_FATURA` decimal(18,4) DEFAULT NULL,
  `VALOR_FRETE` decimal(18,4) DEFAULT NULL,
  `VALOR_ICMS` decimal(18,4) DEFAULT NULL,
  `VALOR_ICMS_SUBSTITUTO` decimal(18,4) DEFAULT NULL,
  `VALOR_IPI` decimal(18,4) DEFAULT NULL,
  `VALOR_LIQUIDO` decimal(18,4) DEFAULT NULL,
  `VALOR_NF` decimal(18,4) DEFAULT NULL,
  `VALOR_OUTRO` decimal(18,4) DEFAULT NULL,
  `VALOR_PRODUTOS` decimal(18,4) DEFAULT NULL,
  `VALOR_SEGURO` decimal(18,4) DEFAULT NULL,
  `VERSAO` varchar(255) DEFAULT NULL,
  `TIPO_NF_ID` bigint(20) NOT NULL,
  `CFOP_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  `PJ_ID` bigint(20) NOT NULL,
  `VALOR_INFORMADO` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKD48924687FEEEECC` (`TIPO_NF_ID`),
  KEY `FKD48924689808F874` (`FORNECEDOR_ID`),
  KEY `FKD48924687AB6324F` (`CFOP_ID`),
  KEY `FKD4892468479FD586` (`PJ_ID`),
  CONSTRAINT `FKD4892468479FD586` FOREIGN KEY (`PJ_ID`) REFERENCES `pessoa` (`ID`),
  CONSTRAINT `FKD48924687AB6324F` FOREIGN KEY (`CFOP_ID`) REFERENCES `cfop` (`ID`),
  CONSTRAINT `FKD48924687FEEEECC` FOREIGN KEY (`TIPO_NF_ID`) REFERENCES `tipo_nota_fiscal` (`ID`),
  CONSTRAINT `FKD48924689808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_serie`
--

DROP TABLE IF EXISTS `nota_fiscal_serie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_serie` (
  `id` int(11) NOT NULL,
  `CURRENT_VALUE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_tributacao`
--

DROP TABLE IF EXISTS `nota_fiscal_tributacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_tributacao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ALIQUOTA_COFINS` decimal(18,4) DEFAULT NULL,
  `ALIQUOTA_ICMS` decimal(18,4) DEFAULT NULL,
  `ALIQUOTA_IPI` decimal(18,4) DEFAULT NULL,
  `ALIQUOTA_PIS` decimal(18,4) DEFAULT NULL,
  `COD_EMPRESA` varchar(9) DEFAULT NULL,
  `COD_NBM` varchar(10) DEFAULT NULL,
  `COD_NOP` varchar(20) DEFAULT NULL,
  `CST_COFINS` varchar(2) DEFAULT NULL,
  `CST_ICMS` varchar(3) DEFAULT NULL,
  `CST_IPI` varchar(3) DEFAULT NULL,
  `CST_PIS` varchar(3) DEFAULT NULL,
  `DAT_VIGENCIA` date DEFAULT NULL,
  `FLG_BSC_CRE_COFINS` varchar(1) DEFAULT NULL,
  `FLG_BSC_CRE_PIS` varchar(1) DEFAULT NULL,
  `FLG_BSC_DEB_COFINS` varchar(1) DEFAULT NULL,
  `FLG_BSC_DEB_PIS` varchar(1) DEFAULT NULL,
  `FLG_BSC_ICMS` varchar(1) DEFAULT NULL,
  `FLG_BSC_IPI` varchar(1) DEFAULT NULL,
  `FLG_VLR_CRE_COF` varchar(1) DEFAULT NULL,
  `FLG_VLR_CRE_PIS` varchar(1) DEFAULT NULL,
  `FLG_VLR_DEB_COF` varchar(1) DEFAULT NULL,
  `FLG_VLR_DEB_PIS` varchar(1) DEFAULT NULL,
  `FLG_VLR_ICMS` varchar(1) DEFAULT NULL,
  `FLG_VLR_IPI` varchar(1) DEFAULT NULL,
  `FLG_VLR_ISE_ICMS` varchar(1) DEFAULT NULL,
  `FLG_VLR_ISE_IPI` varchar(1) DEFAULT NULL,
  `FLG_VLR_OUT_ICMS` varchar(1) DEFAULT NULL,
  `FLG_VLR_OUT_IPI` varchar(1) DEFAULT NULL,
  `NAT_OPERACAO` int(11) DEFAULT NULL,
  `TIP_BSC_ICMS` varchar(1) DEFAULT NULL,
  `TIP_OPERACAO` varchar(1) DEFAULT NULL,
  `TRIBUTACAO_COFINS` varchar(1) DEFAULT NULL,
  `TRIBUTACAO_ICMS` varchar(1) DEFAULT NULL,
  `TRIBUTACAO_IPI` varchar(1) DEFAULT NULL,
  `TRIBUTACAO_PIS` varchar(1) DEFAULT NULL,
  `UF_DESTINO` varchar(2) DEFAULT NULL,
  `UF_ORIGEM` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_valores_retencoes_tributos`
--

DROP TABLE IF EXISTS `nota_fiscal_valores_retencoes_tributos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_valores_retencoes_tributos` (
  `id` bigint(20) NOT NULL,
  `VL_BC_IRRF` decimal(18,4) DEFAULT NULL,
  `VL_BC_PREV` decimal(18,4) DEFAULT NULL,
  `VL_RET_COFINS` decimal(18,4) DEFAULT NULL,
  `VL_RET_CSLL` decimal(18,4) DEFAULT NULL,
  `VL_RET_IRRF` decimal(18,4) DEFAULT NULL,
  `VL_RET_PIS` decimal(18,4) DEFAULT NULL,
  `VL_RET_PREV` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5A341D5250D82C52` (`id`),
  CONSTRAINT `FK5A341D5250D82C52` FOREIGN KEY (`id`) REFERENCES `nota_fiscal_novo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_fiscal_valores_totais_issqn`
--

DROP TABLE IF EXISTS `nota_fiscal_valores_totais_issqn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_fiscal_valores_totais_issqn` (
  `id` bigint(20) NOT NULL,
  `VL_TOTAL_BC_ISS` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_COFINS` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_ISS` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_PIS` decimal(18,4) DEFAULT NULL,
  `VL_TOTAL_SERVICOS` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAD8928F650D82C52` (`id`),
  CONSTRAINT `FKAD8928F650D82C52` FOREIGN KEY (`id`) REFERENCES `nota_fiscal_novo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nota_promissoria`
--

DROP TABLE IF EXISTS `nota_promissoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nota_promissoria` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VALOR` double NOT NULL,
  `VALOR_EXTENSO` varchar(255) NOT NULL,
  `VENCIMENTO` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pagamento_caucao_liquida`
--

DROP TABLE IF EXISTS `pagamento_caucao_liquida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagamento_caucao_liquida` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VALOR` decimal(18,4) DEFAULT NULL,
  `QNT_PARCELAS` int(11) DEFAULT NULL,
  `VALOR_PARCELA` decimal(18,4) DEFAULT NULL,
  `DESCONTO_ATUAL` decimal(18,4) DEFAULT NULL,
  `DESCONTO_COTA` decimal(18,4) DEFAULT NULL,
  `PORCENTAGEM_UTILIZADA` decimal(18,4) DEFAULT NULL,
  `FORMA_COBRANCA_CAUCAO_LIQUIDA_ID` bigint(20) DEFAULT NULL,
  `BANCO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK403E6639EA41BD85` (`FORMA_COBRANCA_CAUCAO_LIQUIDA_ID`),
  KEY `FK403E6639E44516C0` (`BANCO_ID`),
  CONSTRAINT `FK403E6639E44516C0` FOREIGN KEY (`BANCO_ID`) REFERENCES `banco` (`ID`),
  CONSTRAINT `FK403E6639EA41BD85` FOREIGN KEY (`FORMA_COBRANCA_CAUCAO_LIQUIDA_ID`) REFERENCES `forma_cobranca_caucao_liquida` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametro_cobranca_cota`
--

DROP TABLE IF EXISTS `parametro_cobranca_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametro_cobranca_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FATOR_VENCIMENTO` int(11) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `FORNECEDOR_ID` bigint(20) DEFAULT NULL,
  `UNIFICA_COBRANCA` bigint(20) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `COTA_ID` (`COTA_ID`),
  KEY `FK53B1036BC8181F74` (`COTA_ID`),
  KEY `FK_PARAMETRO_COBRANCA_COTA_FORNECEDOR_ID` (`FORNECEDOR_ID`),
  CONSTRAINT `FK53B1036BC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FK_PARAMETRO_COBRANCA_COTA_FORNECEDOR_ID` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametro_cobranca_distribuicao_cota`
--

DROP TABLE IF EXISTS `parametro_cobranca_distribuicao_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametro_cobranca_distribuicao_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DIA_COBRANCA` int(11) DEFAULT NULL,
  `MODELIDADE_COBRANCA` varchar(255) DEFAULT NULL,
  `PERIODICIDADE_COBRANCA` varchar(255) DEFAULT NULL,
  `POR_ENTREGA` tinyint(1) DEFAULT NULL,
  `BASE_CALCULO` int(11) DEFAULT NULL,
  `PERCENTUAL_FATURAMENTO` decimal(18,4) DEFAULT NULL,
  `TAXA_FIXA` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `DIA_SEMANA` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametro_contrato_cota`
--

DROP TABLE IF EXISTS `parametro_contrato_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametro_contrato_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COMPLEMENTO_CONTRATO` longtext,
  `CONDICOES_CONTRATACAO` longtext,
  `DIAS_AVISO_RESCISAO` int(11) NOT NULL,
  `DURACAO_CONTRATO_COTA` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametro_emissao_nota_fiscal`
--

DROP TABLE IF EXISTS `parametro_emissao_nota_fiscal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametro_emissao_nota_fiscal` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `GRUPO_NOTA_FISCAL` varchar(255) NOT NULL,
  `SERIE_NF` varchar(255) NOT NULL,
  `CFOP_DENTRO_UF` bigint(20) NOT NULL,
  `CFOP_FORA_UF` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `GRUPO_NOTA_FISCAL` (`GRUPO_NOTA_FISCAL`),
  KEY `FK5EE04579CC126856` (`CFOP_FORA_UF`),
  KEY `FK5EE045794189EDCA` (`CFOP_DENTRO_UF`),
  CONSTRAINT `FK5EE045794189EDCA` FOREIGN KEY (`CFOP_DENTRO_UF`) REFERENCES `cfop` (`ID`),
  CONSTRAINT `FK5EE04579CC126856` FOREIGN KEY (`CFOP_FORA_UF`) REFERENCES `cfop` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametro_sistema`
--

DROP TABLE IF EXISTS `parametro_sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametro_sistema` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TIPO_PARAMETRO_SISTEMA` varchar(255) NOT NULL,
  `VALOR` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametro_usuario_box`
--

DROP TABLE IF EXISTS `parametro_usuario_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametro_usuario_box` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) DEFAULT NULL,
  `BOX_ID` bigint(20) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK94C9BED47FFF790E` (`USUARIO_ID`),
  KEY `FK94C9BED4BA6EBE40` (`BOX_ID`),
  CONSTRAINT `FK94C9BED47FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK94C9BED4BA6EBE40` FOREIGN KEY (`BOX_ID`) REFERENCES `box` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametros_distribuidor_emissao_documentos`
--

DROP TABLE IF EXISTS `parametros_distribuidor_emissao_documentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametros_distribuidor_emissao_documentos` (
  `TIPO_PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS` varchar(255) NOT NULL,
  `UTILIZA_EMAIL` tinyint(1) NOT NULL,
  `UTILIZA_IMPRESSAO` tinyint(1) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`TIPO_PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS`),
  KEY `FKF2CA661B56501954` (`DISTRIBUIDOR_ID`),
  CONSTRAINT `FKF2CA661B56501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametros_distribuidor_faltas_sobras`
--

DROP TABLE IF EXISTS `parametros_distribuidor_faltas_sobras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parametros_distribuidor_faltas_sobras` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FALTA_DE` tinyint(1) NOT NULL,
  `FALTA_EM` tinyint(1) NOT NULL,
  `SOBRA_DE` tinyint(1) NOT NULL,
  `SOBRA_EM` tinyint(1) NOT NULL,
  `TIPO_PARAMETROS_DISTRIBUIDOR_EMISSAO_DOCUMENTOS` varchar(255) DEFAULT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8547839C56501954` (`DISTRIBUIDOR_ID`),
  CONSTRAINT `FK8547839C56501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parcela_negociacao`
--

DROP TABLE IF EXISTS `parcela_negociacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parcela_negociacao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_VENCIMENTO` datetime DEFAULT NULL,
  `ENCARGOS` decimal(18,4) DEFAULT NULL,
  `NUMERO_CHEQUE` varchar(255) DEFAULT NULL,
  `MOVIMENTO_FINANCEIRO_ID` bigint(20) DEFAULT NULL,
  `NEGOCIACAO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK904E941CEEC3D845` (`MOVIMENTO_FINANCEIRO_ID`),
  KEY `FK904E941CFCD23D41` (`NEGOCIACAO_ID`),
  CONSTRAINT `FK904E941CEEC3D845` FOREIGN KEY (`MOVIMENTO_FINANCEIRO_ID`) REFERENCES `movimento_financeiro_cota` (`id`),
  CONSTRAINT `FK904E941CFCD23D41` FOREIGN KEY (`NEGOCIACAO_ID`) REFERENCES `negociacao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pdv`
--

DROP TABLE IF EXISTS `pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ARRENDATARIO` tinyint(1) DEFAULT NULL,
  `BALCAO_CENTRAL` tinyint(1) DEFAULT NULL,
  `PONTO_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `POSSUI_CARTAO_CREDITO` tinyint(1) DEFAULT NULL,
  `POSSUI_COMPUTADOR` tinyint(1) DEFAULT NULL,
  `POSSUI_LUMINOSO` tinyint(1) DEFAULT NULL,
  `TEXTO_LUMINOSO` varchar(255) DEFAULT NULL,
  `CONTATO` varchar(255) DEFAULT NULL,
  `dataInclusao` date DEFAULT NULL,
  `DENTRO_OUTRO_ESTABELECIMENTO` tinyint(1) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `EXPOSITOR` tinyint(1) DEFAULT NULL,
  `NOME_LICENCA` varchar(255) DEFAULT NULL,
  `NUMERO_LICENCA` varchar(255) DEFAULT NULL,
  `NOME` varchar(255) NOT NULL,
  `PONTO_REFERENCIA` varchar(255) DEFAULT NULL,
  `PORCENTAGEM_FATURAMENTO` decimal(18,4) DEFAULT NULL,
  `POSSUI_SISTEMA_IPV` tinyint(1) DEFAULT NULL,
  `QTDE_FUNCIONARIOS` int(11) DEFAULT NULL,
  `TIPO_CARACTERISTICA_PDV` varchar(255) DEFAULT NULL,
  `SITE` varchar(255) DEFAULT NULL,
  `STATUS_PDV` varchar(255) DEFAULT NULL,
  `TAMANHO_PDV` varchar(255) DEFAULT NULL,
  `TIPO_EXPOSITOR` varchar(255) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `TIPO_LICENCA_MUNICIPAL_ID` bigint(20) DEFAULT NULL,
  `AREA_INFLUENCIA_PDV_ID` bigint(20) DEFAULT NULL,
  `TIPO_CLUSTER_PDV_ID` bigint(20) DEFAULT NULL,
  `TIPO_PONTO_PDV_ID` bigint(20) DEFAULT NULL,
  `TIPO_ESTABELECIMENTO_PDV_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK134E23007BE32` (`TIPO_ESTABELECIMENTO_PDV_ID`),
  KEY `FK134E24E24300A` (`TIPO_CLUSTER_PDV_ID`),
  KEY `FK134E2C8181F74` (`COTA_ID`),
  KEY `FK134E2DCF1938` (`TIPO_LICENCA_MUNICIPAL_ID`),
  KEY `FK134E23D683EA4` (`AREA_INFLUENCIA_PDV_ID`),
  KEY `FK134E2B076932A` (`TIPO_PONTO_PDV_ID`),
  CONSTRAINT `FK134E23007BE32` FOREIGN KEY (`TIPO_ESTABELECIMENTO_PDV_ID`) REFERENCES `tipo_estabelecimento_associacao_pdv` (`ID`),
  CONSTRAINT `FK134E23D683EA4` FOREIGN KEY (`AREA_INFLUENCIA_PDV_ID`) REFERENCES `area_influencia_pdv` (`ID`),
  CONSTRAINT `FK134E24E24300A` FOREIGN KEY (`TIPO_CLUSTER_PDV_ID`) REFERENCES `tipo_cluster_pdv` (`ID`),
  CONSTRAINT `FK134E2B076932A` FOREIGN KEY (`TIPO_PONTO_PDV_ID`) REFERENCES `tipo_ponto_pdv` (`ID`),
  CONSTRAINT `FK134E2C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`),
  CONSTRAINT `FK134E2DCF1938` FOREIGN KEY (`TIPO_LICENCA_MUNICIPAL_ID`) REFERENCES `tipo_licenca_municipal` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pdv_material_promocional`
--

DROP TABLE IF EXISTS `pdv_material_promocional`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pdv_material_promocional` (
  `PDV_ID` bigint(20) NOT NULL,
  `MATERIAL_PROMOCIONAL_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PDV_ID`,`MATERIAL_PROMOCIONAL_ID`),
  KEY `FKE8BEF5842B57B031` (`MATERIAL_PROMOCIONAL_ID`),
  KEY `FKE8BEF584A65B70F4` (`PDV_ID`),
  CONSTRAINT `FKE8BEF5842B57B031` FOREIGN KEY (`MATERIAL_PROMOCIONAL_ID`) REFERENCES `material_promocional` (`ID`),
  CONSTRAINT `FKE8BEF584A65B70F4` FOREIGN KEY (`PDV_ID`) REFERENCES `pdv` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `perfil_usuario`
--

DROP TABLE IF EXISTS `perfil_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perfil_usuario` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `periodo_funcionamento_pdv`
--

DROP TABLE IF EXISTS `periodo_funcionamento_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `periodo_funcionamento_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `HORARIO_FIM` time DEFAULT NULL,
  `HORARIO_INICIO` time DEFAULT NULL,
  `FUNCIONAMENTO_PDV` varchar(255) DEFAULT NULL,
  `PDV_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKB3048186A65B70F4` (`PDV_ID`),
  CONSTRAINT `FKB3048186A65B70F4` FOREIGN KEY (`PDV_ID`) REFERENCES `pdv` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `periodo_lancamento_parcial`
--

DROP TABLE IF EXISTS `periodo_lancamento_parcial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `periodo_lancamento_parcial` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STATUS` varchar(255) NOT NULL,
  `TIPO` varchar(255) NOT NULL,
  `LANCAMENTO_PARCIAL_ID` bigint(20) NOT NULL,
  `NUMERO_PERIODO` int(11) NOT NULL,
  `DATA_CRIACAO` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKCC84267E4927B490` (`LANCAMENTO_PARCIAL_ID`),
  CONSTRAINT `FKCC84267E4927B490` FOREIGN KEY (`LANCAMENTO_PARCIAL_ID`) REFERENCES `lancamento_parcial` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pessoa`
--

DROP TABLE IF EXISTS `pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pessoa` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EMAIL` varchar(255) DEFAULT NULL,
  `APELIDO` varchar(25) DEFAULT NULL,
  `CPF` varchar(255) DEFAULT NULL,
  `DATA_NASCIMENTO` date DEFAULT NULL,
  `ESTADO_CIVIL` varchar(255) DEFAULT NULL,
  `NACIONALIDADE` varchar(255) DEFAULT NULL,
  `NATURALIDADE` varchar(255) DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `ORGAO_EMISSOR` varchar(255) DEFAULT NULL,
  `RG` varchar(255) DEFAULT NULL,
  `SEXO` varchar(255) DEFAULT NULL,
  `SOCIO_PRINCIPAL` tinyint(1) DEFAULT NULL,
  `UF_ORGAO_EMISSOR` varchar(255) DEFAULT NULL,
  `CNPJ` varchar(255) DEFAULT NULL,
  `INSC_ESTADUAL` varchar(20) DEFAULT NULL,
  `INSC_MUNICIPAL` varchar(15) DEFAULT NULL,
  `NOME_FANTASIA` varchar(60) DEFAULT NULL,
  `RAZAO_SOCIAL` varchar(255) DEFAULT NULL,
  `PESSOA_ID_CONJUGE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CPF` (`CPF`),
  UNIQUE KEY `CNPJ` (`CNPJ`),
  KEY `FK8C7703A7D236B5B7` (`PESSOA_ID_CONJUGE`),
  KEY `NDX_NOME` (`NOME`),
  CONSTRAINT `FK8C7703A7D236B5B7` FOREIGN KEY (`PESSOA_ID_CONJUGE`) REFERENCES `pessoa` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `politica_cobranca`
--

DROP TABLE IF EXISTS `politica_cobranca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `politica_cobranca` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACUMULA_DIVIDA` tinyint(1) NOT NULL,
  `ATIVO` tinyint(1) NOT NULL,
  `FORMA_EMISSAO` varchar(255) DEFAULT NULL,
  `NUM_DIAS_POSTERGADO` int(11) DEFAULT NULL,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `UNIFICA_COBRANCA` tinyint(1) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) DEFAULT NULL,
  `FORMA_COBRANCA_ID` bigint(20) DEFAULT NULL,
  `COBRANCA_BO` tinyint(1) DEFAULT NULL,
  `FORNECEDOR_PADRAO_ID` bigint(20) DEFAULT NULL,
  `FATOR_VENCIMENTO` int(11) DEFAULT NULL,
  `UNIFICA_COBRANCA_POR_COTA` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK5743FFF7E34F875B` (`FORMA_COBRANCA_ID`),
  KEY `FK5743FFF756501954` (`DISTRIBUIDOR_ID`),
  KEY `FK5743FFF76051BBD2` (`FORNECEDOR_PADRAO_ID`),
  CONSTRAINT `FK5743FFF756501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`),
  CONSTRAINT `FK5743FFF76051BBD2` FOREIGN KEY (`FORNECEDOR_PADRAO_ID`) REFERENCES `fornecedor` (`ID`),
  CONSTRAINT `FK5743FFF7E34F875B` FOREIGN KEY (`FORMA_COBRANCA_ID`) REFERENCES `forma_cobranca` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `processo_nfe`
--

DROP TABLE IF EXISTS `processo_nfe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `processo_nfe` (
  `PROCESSO_NFE_ID` bigint(20) NOT NULL,
  `PROCESSO` int(11) DEFAULT NULL,
  KEY `FK1D60CAEC0B404C1` (`PROCESSO_NFE_ID`),
  CONSTRAINT `FK1D60CAEC0B404C1` FOREIGN KEY (`PROCESSO_NFE_ID`) REFERENCES `tipo_nota_fiscal` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `procuracao_entregador`
--

DROP TABLE IF EXISTS `procuracao_entregador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `procuracao_entregador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ENDERECO` varchar(255) DEFAULT NULL,
  `ESTADO_CIVIL` varchar(255) NOT NULL,
  `NACIONALIDADE` varchar(255) DEFAULT NULL,
  `NUMERO_PERMISSAO` varchar(255) NOT NULL,
  `PROCURADOR` varchar(255) NOT NULL,
  `PROFISSAO` varchar(255) DEFAULT NULL,
  `RG` varchar(255) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `ENTREGADOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ENTREGADOR_ID` (`ENTREGADOR_ID`),
  KEY `FKB848C78BC8181F74` (`COTA_ID`),
  KEY `FKB848C78B83B33034` (`ENTREGADOR_ID`),
  CONSTRAINT `FKB848C78B83B33034` FOREIGN KEY (`ENTREGADOR_ID`) REFERENCES `entregador` (`ID`),
  CONSTRAINT `FKB848C78BC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `produto`
--

DROP TABLE IF EXISTS `produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produto` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_CRIACAO` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ATIVO` tinyint(1) DEFAULT NULL,
  `CODIGO` varchar(30) DEFAULT NULL,
  `COD_CONTEXTO` int(11) DEFAULT NULL,
  `DATA_DESATIVACAO` date DEFAULT NULL,
  `desconto` decimal(18,4) DEFAULT NULL,
  `COMPRIMENTO` float DEFAULT NULL,
  `ESPESSURA` float DEFAULT NULL,
  `LARGURA` float DEFAULT NULL,
  `fase` varchar(255) DEFAULT NULL,
  `FORMA_COMERCIALIZACAO` varchar(255) DEFAULT NULL,
  `LANCAMENTO_IMEDIATO` tinyint(1) DEFAULT NULL,
  `NOME` varchar(60) NOT NULL,
  `NOME_COMERCIAL` varchar(255) DEFAULT NULL,
  `numeroLancamento` bigint(20) DEFAULT NULL,
  `ORIGEM` varchar(255) NOT NULL,
  `PACOTE_PADRAO` int(11) NOT NULL,
  `PEB` int(11) NOT NULL,
  `PERCENTUAl_ABRANGENCIA` double DEFAULT NULL,
  `PERC_LIMITE_COTA_FIXACAO` double DEFAULT NULL,
  `PERC_LIMITE_REPARTE_FIXACAO` double DEFAULT NULL,
  `PERIODICIDADE` varchar(255) NOT NULL,
  `PESO` bigint(20) NOT NULL,
  `CLASSE_SOCIAL` varchar(255) DEFAULT NULL,
  `FAIXA_ETARIA` varchar(255) DEFAULT NULL,
  `FORMATO_PRODUTO` varchar(255) DEFAULT NULL,
  `SEXO` varchar(255) DEFAULT NULL,
  `TEMA_PRINCIPAL` varchar(255) DEFAULT NULL,
  `TEMA_SECUNDARIO` varchar(255) DEFAULT NULL,
  `SITUACAO_TRIBUTARIA` varchar(255) DEFAULT NULL,
  `SLOGAN` varchar(50) DEFAULT NULL,
  `TRIBUTACAO_FISCAL` varchar(255) DEFAULT NULL,
  `ALGORITMO_ID` bigint(20) DEFAULT NULL,
  `DESCONTO_LOGISTICA_ID` bigint(20) DEFAULT NULL,
  `EDITOR_ID` bigint(20) DEFAULT NULL,
  `TIPO_PRODUTO_ID` bigint(20) NOT NULL,
  `DESCRICAO_DESCONTO` varchar(255) DEFAULT NULL,
  `TIPO_SEGMENTO_PRODUTO_ID` bigint(20) DEFAULT NULL,
  `DESCONTO_ID` bigint(20) DEFAULT NULL,
  `codigo_icd` varchar(255) DEFAULT NULL,
  `GRUPO_EDITORIAL` varchar(25) DEFAULT NULL,
  `geracao_automatica` tinyint(4) DEFAULT '0',
  `FORMA_FISICA` varchar(255) DEFAULT NULL,
  `SUB_GRUPO_EDITORIAL` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`),
  KEY `FK18595AD946FC8AA9` (`DESCONTO_LOGISTICA_ID`),
  KEY `FK18595AD999DDFD97` (`TIPO_PRODUTO_ID`),
  KEY `FK18595AD9B2A11874` (`EDITOR_ID`),
  KEY `FK18595AD9FEBD8620` (`ALGORITMO_ID`),
  KEY `FK18595AD929E8DFE3` (`DESCONTO_ID`),
  KEY `FK_TIPO_SEGMENTO_PRODUTO` (`TIPO_SEGMENTO_PRODUTO_ID`),
  KEY `NDX_NOME` (`NOME`),
  CONSTRAINT `FK18595AD929E8DFE3` FOREIGN KEY (`DESCONTO_ID`) REFERENCES `desconto` (`ID`),
  CONSTRAINT `FK18595AD946FC8AA9` FOREIGN KEY (`DESCONTO_LOGISTICA_ID`) REFERENCES `desconto_logistica` (`ID`),
  CONSTRAINT `FK18595AD999DDFD97` FOREIGN KEY (`TIPO_PRODUTO_ID`) REFERENCES `tipo_produto` (`ID`),
  CONSTRAINT `FK18595AD9B2A11874` FOREIGN KEY (`EDITOR_ID`) REFERENCES `editor` (`ID`),
  CONSTRAINT `FK18595AD9FEBD8620` FOREIGN KEY (`ALGORITMO_ID`) REFERENCES `algoritmo` (`ID`),
  CONSTRAINT `FK_TIPO_SEGMENTO_PRODUTO` FOREIGN KEY (`TIPO_SEGMENTO_PRODUTO_ID`) REFERENCES `tipo_segmento_produto` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `produto_edicao`
--

DROP TABLE IF EXISTS `produto_edicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produto_edicao` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATIVO` tinyint(1) NOT NULL,
  `BOLETIM_INFORMATIVO` varchar(2048) DEFAULT NULL,
  `DESCRICAO_BRINDE` varchar(255) DEFAULT NULL,
  `VENDE_BRINDE_SEPARADO` tinyint(1) DEFAULT NULL,
  `CHAMADA_CAPA` varchar(255) DEFAULT NULL,
  `CODIGO_DE_BARRAS_CORPORATIVO` varchar(25) DEFAULT NULL,
  `CODIGO_DE_BARRAS` varchar(18) DEFAULT NULL,
  `DATA_DESATIVACAO` date DEFAULT NULL,
  `COMPRIMENTO` float DEFAULT NULL,
  `ESPESSURA` float DEFAULT NULL,
  `LARGURA` float DEFAULT NULL,
  `EXPECTATIVA_VENDA` decimal(18,4) DEFAULT NULL,
  `NOME_COMERCIAL` varchar(60) DEFAULT NULL,
  `NUMERO_EDICAO` bigint(20) NOT NULL,
  `ORIGEM` varchar(255) NOT NULL,
  `PACOTE_PADRAO` int(11) NOT NULL,
  `PARCIAL` tinyint(1) NOT NULL,
  `PEB` int(11) NOT NULL,
  `PERMITE_VALE_DESCONTO` tinyint(1) NOT NULL,
  `PESO` bigint(20) NOT NULL,
  `POSSUI_BRINDE` tinyint(1) NOT NULL,
  `PRECO_CUSTO` decimal(18,4) DEFAULT NULL,
  `PRECO_PREVISTO` decimal(18,4) DEFAULT NULL,
  `PRECO_VENDA` decimal(18,4) DEFAULT NULL,
  `REPARTE_DISTRIBUIDO` decimal(18,4) DEFAULT NULL,
  `DESCONTO_LOGISTICA_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) DEFAULT NULL,
  `BRINDE_ID` bigint(20) DEFAULT NULL,
  `CLASSE_SOCIAL` varchar(255) DEFAULT NULL,
  `FAIXA_ETARIA` varchar(255) DEFAULT NULL,
  `FORMATO_PRODUTO` varchar(255) DEFAULT NULL,
  `SEXO` varchar(255) DEFAULT NULL,
  `TEMA_PRINCIPAL` varchar(255) DEFAULT NULL,
  `TEMA_SECUNDARIO` varchar(255) DEFAULT NULL,
  `TIPO_LANCAMENTO` varchar(255) DEFAULT NULL,
  `CARACTERISTICA_PRODUTO` varchar(255) DEFAULT NULL,
  `DESCONTO` decimal(18,4) DEFAULT NULL,
  `DESCRICAO_DESCONTO` varchar(255) DEFAULT NULL,
  `GRUPO_PRODUTO` varchar(255) DEFAULT NULL,
  `DESCONTO_ID` bigint(20) DEFAULT NULL,
  `FORMA_FISICA` varchar(255) DEFAULT NULL,
  `TIPO_CLASSIFICACAO_PRODUTO_ID` bigint(20) DEFAULT NULL,
  `HISTORICO` varchar(255) DEFAULT NULL,
  `VINCULAR_RECOLHIMENTO` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKD43B400D46FC8AA9` (`DESCONTO_LOGISTICA_ID`),
  KEY `FKD43B400DC5C16480` (`PRODUTO_ID`),
  KEY `FKD43B400DA53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `FKD43B400D94ECE7D4` (`BRINDE_ID`),
  KEY `FKD43B400D29E8DFE3` (`DESCONTO_ID`),
  KEY `FKD43B400D10C84C95` (`TIPO_CLASSIFICACAO_PRODUTO_ID`),
  CONSTRAINT `FKD43B400D29E8DFE3` FOREIGN KEY (`DESCONTO_ID`) REFERENCES `desconto` (`ID`),
  CONSTRAINT `FKD43B400D46FC8AA9` FOREIGN KEY (`DESCONTO_LOGISTICA_ID`) REFERENCES `desconto_logistica` (`ID`),
  CONSTRAINT `FKD43B400D94ECE7D4` FOREIGN KEY (`BRINDE_ID`) REFERENCES `brinde` (`ID`),
  CONSTRAINT `FKD43B400DA53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FKD43B400DC5C16480` FOREIGN KEY (`PRODUTO_ID`) REFERENCES `produto` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `produto_edicao_slip`
--

DROP TABLE IF EXISTS `produto_edicao_slip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produto_edicao_slip` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_OPERACAO` date DEFAULT NULL,
  `DATA_RECOLHIMENTO` date DEFAULT NULL,
  `DATA_RECOLHIMENTO_DISTRIBUIDOR` date DEFAULT NULL,
  `DIA` int(11) DEFAULT NULL,
  `ENCALHE` decimal(19,2) DEFAULT NULL,
  `ID_CHAMADA_ENCALHE` bigint(20) DEFAULT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) DEFAULT NULL,
  `NOME_PRODUTO` varchar(255) DEFAULT NULL,
  `NUMERO_EDICAO` bigint(20) DEFAULT NULL,
  `ORDINAL_DIA_CONFERENCIA_ENCALHE` varchar(255) DEFAULT NULL,
  `PRECO_VENDA` decimal(18,4) DEFAULT NULL,
  `QTDE_TOTAL_PRODUTOS` varchar(255) DEFAULT NULL,
  `QUANTIDADE_EFETIVA` decimal(19,2) DEFAULT NULL,
  `REPARTE` decimal(19,2) DEFAULT NULL,
  `VALOR_TOTAL` decimal(18,4) DEFAULT NULL,
  `VALOR_TOTAL_ENCALHE` varchar(255) DEFAULT NULL,
  `SLIP_ID` bigint(20) NOT NULL,
  `CODIGO_PRODUTO` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKEA5D5A32ADA6DD0A` (`SLIP_ID`),
  CONSTRAINT `FKEA5D5A32ADA6DD0A` FOREIGN KEY (`SLIP_ID`) REFERENCES `slip` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `produto_fornecedor`
--

DROP TABLE IF EXISTS `produto_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produto_fornecedor` (
  `PRODUTO_ID` bigint(20) NOT NULL,
  `fornecedores_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PRODUTO_ID`,`fornecedores_ID`),
  KEY `FKBA6768BC5C16480` (`PRODUTO_ID`),
  KEY `FKBA6768BFC845246` (`fornecedores_ID`),
  CONSTRAINT `FKBA6768BC5C16480` FOREIGN KEY (`PRODUTO_ID`) REFERENCES `produto` (`ID`),
  CONSTRAINT `FKBA6768BFC845246` FOREIGN KEY (`fornecedores_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_blob_triggers`
--

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_fired_triggers`
--

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_job_details`
--

DROP TABLE IF EXISTS `qrtz_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_locks`
--

DROP TABLE IF EXISTS `qrtz_locks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_scheduler_state`
--

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_simprop_triggers`
--

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_triggers`
--

DROP TABLE IF EXISTS `qrtz_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ranking_faturamento`
--

DROP TABLE IF EXISTS `ranking_faturamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ranking_faturamento` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COTA_ID` bigint(20) NOT NULL,
  `FATURAMENTO` decimal(18,4) DEFAULT NULL,
  `DATA_GERACAO_RANK` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK2BF1CB3DC8181F74` (`COTA_ID`),
  CONSTRAINT `FK2BF1CB3DC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ranking_faturamento_gerados`
--

DROP TABLE IF EXISTS `ranking_faturamento_gerados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ranking_faturamento_gerados` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_GERACAO_RANK` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ranking_segmento`
--

DROP TABLE IF EXISTS `ranking_segmento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ranking_segmento` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COTA_ID` bigint(20) NOT NULL,
  `TIPO_SEGMENTO_PRODUTO_ID` bigint(20) NOT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `DATA_GERACAO_RANK` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8614C9A54C709C67` (`TIPO_SEGMENTO_PRODUTO_ID`),
  KEY `FK8614C9A5C8181F74` (`COTA_ID`),
  CONSTRAINT `FK8614C9A54C709C67` FOREIGN KEY (`TIPO_SEGMENTO_PRODUTO_ID`) REFERENCES `tipo_segmento_produto` (`ID`),
  CONSTRAINT `FK8614C9A5C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ranking_segmento_gerados`
--

DROP TABLE IF EXISTS `ranking_segmento_gerados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ranking_segmento_gerados` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_GERACAO_RANK` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rateio_cota_ausente`
--

DROP TABLE IF EXISTS `rateio_cota_ausente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rateio_cota_ausente` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `COTA_AUSENTE_ID` bigint(20) DEFAULT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK379AD72CC8181F74` (`COTA_ID`),
  KEY `FK379AD72C70E707D7` (`COTA_AUSENTE_ID`),
  KEY `FK379AD72CA53173D3` (`PRODUTO_EDICAO_ID`),
  CONSTRAINT `FK379AD72C70E707D7` FOREIGN KEY (`COTA_AUSENTE_ID`) REFERENCES `cota_ausente` (`ID`),
  CONSTRAINT `FK379AD72CA53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK379AD72CC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rateio_diferenca`
--

DROP TABLE IF EXISTS `rateio_diferenca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rateio_diferenca` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_NOTA_ENVIO` date DEFAULT NULL,
  `QTDE` decimal(18,4) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `DIFERENCA_ID` bigint(20) NOT NULL,
  `ESTUDO_COTA_ID` bigint(20) DEFAULT NULL,
  `DATA_MOVIMENTO` date NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK35BC319C714FA744` (`ESTUDO_COTA_ID`),
  KEY `FK35BC319CC8181F74` (`COTA_ID`),
  KEY `FK35BC319C5A0FFAA9` (`DIFERENCA_ID`),
  CONSTRAINT `FK35BC319C5A0FFAA9` FOREIGN KEY (`DIFERENCA_ID`) REFERENCES `diferenca` (`id`),
  CONSTRAINT `FK35BC319C714FA744` FOREIGN KEY (`ESTUDO_COTA_ID`) REFERENCES `estudo_cota` (`ID`),
  CONSTRAINT `FK35BC319CC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recebimento_fisico`
--

DROP TABLE IF EXISTS `recebimento_fisico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recebimento_fisico` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_CONFIRMACAO` datetime DEFAULT NULL,
  `DATA_RECEBIMENTO` date NOT NULL,
  `STATUS_CONFIRMACAO` varchar(255) NOT NULL,
  `conferente_ID` bigint(20) DEFAULT NULL,
  `NOTA_FISCAL_ID` bigint(20) NOT NULL,
  `recebedor_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOTA_FISCAL_ID` (`NOTA_FISCAL_ID`),
  KEY `FKE6D238AF36A347F1` (`conferente_ID`),
  KEY `FKE6D238AF68C2472D` (`recebedor_ID`),
  KEY `FKE6D238AFC74D9881` (`NOTA_FISCAL_ID`),
  CONSTRAINT `FKE6D238AF36A347F1` FOREIGN KEY (`conferente_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKE6D238AF68C2472D` FOREIGN KEY (`recebedor_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKE6D238AFC74D9881` FOREIGN KEY (`NOTA_FISCAL_ID`) REFERENCES `nota_fiscal_entrada` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `referencia_cota`
--

DROP TABLE IF EXISTS `referencia_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `referencia_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PERCENTUAL` decimal(18,4) DEFAULT NULL,
  `BASE_REFERENCIA_COTA_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `BASE_REFERENCIA_COTA_ID` (`BASE_REFERENCIA_COTA_ID`,`COTA_ID`),
  KEY `FK6386510654DAA518` (`BASE_REFERENCIA_COTA_ID`),
  KEY `FK63865106C8181F74` (`COTA_ID`),
  CONSTRAINT `FK6386510654DAA518` FOREIGN KEY (`BASE_REFERENCIA_COTA_ID`) REFERENCES `base_referencia_cota` (`ID`),
  CONSTRAINT `FK63865106C8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `regiao`
--

DROP TABLE IF EXISTS `regiao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `regiao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_REGIAO` datetime NOT NULL,
  `NOME_REGIAO` varchar(255) NOT NULL,
  `REGIAO_IS_FIXA` tinyint(1) NOT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK8FDB19437FFF790E` (`USUARIO_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `registro_cota_regiao`
--

DROP TABLE IF EXISTS `registro_cota_regiao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registro_cota_regiao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USUARIO_ID` bigint(20) NOT NULL,
  `REGIAO_ID` bigint(20) DEFAULT NULL,
  `COTA_ID` bigint(20) DEFAULT NULL,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK24C9625D7FFF790E` (`USUARIO_ID`),
  KEY `FK24C9625DAE907F55` (`REGIAO_ID`),
  KEY `FK24C9625DC8181F74` (`COTA_ID`),
  CONSTRAINT `FK24C9625D7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK24C9625DAE907F55` FOREIGN KEY (`REGIAO_ID`) REFERENCES `regiao` (`ID`),
  CONSTRAINT `FK24C9625DC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reparte_pdv`
--

DROP TABLE IF EXISTS `reparte_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reparte_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `REPARTE` int(11) DEFAULT NULL,
  `MIX_COTA_PRODUTO_ID` bigint(20) DEFAULT NULL,
  `PDV_ID` bigint(20) NOT NULL,
  `PRODUTO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKC3AA9D0238A5EAC3` (`MIX_COTA_PRODUTO_ID`),
  KEY `FKC3AA9D02C5C16480` (`PRODUTO_ID`),
  KEY `FKC3AA9D02A65B70F4` (`PDV_ID`),
  CONSTRAINT `FKC3AA9D0238A5EAC3` FOREIGN KEY (`MIX_COTA_PRODUTO_ID`) REFERENCES `mix_cota_produto` (`ID`),
  CONSTRAINT `FKC3AA9D02A65B70F4` FOREIGN KEY (`PDV_ID`) REFERENCES `pdv` (`ID`),
  CONSTRAINT `FKC3AA9D02C5C16480` FOREIGN KEY (`PRODUTO_ID`) REFERENCES `produto` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rota`
--

DROP TABLE IF EXISTS `rota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRICAO_ROTA` varchar(255) DEFAULT NULL,
  `ORDEM` int(11) NOT NULL,
  `ROTEIRO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK26796A7E316820` (`ROTEIRO_ID`),
  CONSTRAINT `FK26796A7E316820` FOREIGN KEY (`ROTEIRO_ID`) REFERENCES `roteiro` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rota_pdv`
--

DROP TABLE IF EXISTS `rota_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rota_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ORDEM` int(11) DEFAULT NULL,
  `PDV_ID` bigint(20) DEFAULT NULL,
  `ROTA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK2C44188DE19C69D4` (`ROTA_ID`),
  KEY `FK2C44188DA65B70F4` (`PDV_ID`),
  CONSTRAINT `FK2C44188DA65B70F4` FOREIGN KEY (`PDV_ID`) REFERENCES `pdv` (`ID`),
  CONSTRAINT `FK2C44188DE19C69D4` FOREIGN KEY (`ROTA_ID`) REFERENCES `rota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roteirizacao`
--

DROP TABLE IF EXISTS `roteirizacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roteirizacao` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BOX_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `BOX_ID` (`BOX_ID`),
  KEY `FK9A9E8A58BA6EBE40` (`BOX_ID`),
  CONSTRAINT `FK9A9E8A58BA6EBE40` FOREIGN KEY (`BOX_ID`) REFERENCES `box` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roteiro`
--

DROP TABLE IF EXISTS `roteiro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roteiro` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRICAO_ROTEIRO` varchar(255) DEFAULT NULL,
  `ORDEM` int(11) NOT NULL,
  `TIPO_ROTEIRO` varchar(255) NOT NULL,
  `ROTEIRIZACAO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK7D4E04183B7BA114` (`ROTEIRIZACAO_ID`),
  CONSTRAINT `FK7D4E04183B7BA114` FOREIGN KEY (`ROTEIRIZACAO_ID`) REFERENCES `roteirizacao` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `segmento_nao_recebido`
--

DROP TABLE IF EXISTS `segmento_nao_recebido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `segmento_nao_recebido` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COTA_ID` bigint(20) NOT NULL,
  `TIPO_SEGMENTO_PRODUTO_ID` bigint(20) NOT NULL,
  `DATA_ALTERACAO` datetime DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK6551CA0DC8181F74` (`COTA_ID`),
  KEY `FK6551CA0D4C709C67` (`TIPO_SEGMENTO_PRODUTO_ID`),
  KEY `FK6551CA0D7FFF790E` (`USUARIO_ID`),
  CONSTRAINT `FK6551CA0D4C709C67` FOREIGN KEY (`TIPO_SEGMENTO_PRODUTO_ID`) REFERENCES `tipo_segmento_produto` (`ID`),
  CONSTRAINT `FK6551CA0D7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK6551CA0DC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `semaforo`
--

DROP TABLE IF EXISTS `semaforo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `semaforo` (
  `NUMERO_COTA` int(11) NOT NULL,
  `STATUS_PROCESSO_ENCALHE` varchar(255) DEFAULT NULL,
  `DATA_OPERACAO` date DEFAULT NULL,
  `DATA_INICIO` datetime DEFAULT NULL,
  `DATA_FIM` datetime DEFAULT NULL,
  `ERROR_LOG` varchar(255) DEFAULT NULL,
  `USUARIO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`NUMERO_COTA`),
  KEY `FK1_USUARIO_SEMAFORO` (`USUARIO_ID`),
  CONSTRAINT `FK1_USUARIO_SEMAFORO` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seq_generator`
--

DROP TABLE IF EXISTS `seq_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seq_generator` (
  `sequence_name` varchar(255) NOT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL,
  UNIQUE KEY `NDX_SEQ_NAME_UNIQUE` (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slip`
--

DROP TABLE IF EXISTS `slip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slip` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CE_JORNALEIRO` bigint(20) DEFAULT NULL,
  `CODIGO_BOX` varchar(255) DEFAULT NULL,
  `DATA_CONFERENCIA` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `DESCRICAO_ROTA` varchar(255) DEFAULT NULL,
  `DESCRICAO_ROTEIRO` varchar(255) DEFAULT NULL,
  `NOME_COTA` varchar(255) DEFAULT NULL,
  `NUM_SLIP` bigint(20) DEFAULT NULL,
  `NUMERO_COTA` int(11) NOT NULL,
  `PAGAMENTO_PENDENTE` decimal(18,4) DEFAULT NULL,
  `TOTAL_PRODUTO_DIA` decimal(19,2) DEFAULT NULL,
  `TOTAL_PRODUTOS` decimal(19,2) DEFAULT NULL,
  `VALOR_CREDITO_DIF` decimal(18,4) DEFAULT NULL,
  `VALOR_DEVIDO` decimal(18,4) DEFAULT NULL,
  `VALOR_ENCALHE_DIA` decimal(18,4) DEFAULT NULL,
  `VALOR_LIQUIDO_DEVIDO` decimal(18,4) DEFAULT NULL,
  `VALOR_SLIP` decimal(18,4) DEFAULT NULL,
  `VALOR_TOTAL_DESCONTO` decimal(18,4) DEFAULT NULL,
  `VALOR_TOTAL_ENCALHE` decimal(18,4) DEFAULT NULL,
  `VALOR_TOTAL_PAGAR` decimal(18,4) DEFAULT NULL,
  `VALOR_TOTAL_REPARTE` decimal(18,4) DEFAULT NULL,
  `VALOR_TOTAL_SEM_DESCONTO` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `socio_cota`
--

DROP TABLE IF EXISTS `socio_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `socio_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CARGO` varchar(255) DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `PRINCIPAL` tinyint(1) DEFAULT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  `ENDERECO_ID` bigint(20) NOT NULL,
  `TELEFONE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKC6ACCC0BAE761C74` (`ENDERECO_ID`),
  KEY `FKC6ACCC0BC8181F74` (`COTA_ID`),
  KEY `FKC6ACCC0B52023CD4` (`TELEFONE_ID`),
  CONSTRAINT `FKC6ACCC0B52023CD4` FOREIGN KEY (`TELEFONE_ID`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FKC6ACCC0BAE761C74` FOREIGN KEY (`ENDERECO_ID`) REFERENCES `endereco` (`ID`),
  CONSTRAINT `FKC6ACCC0BC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `telefone`
--

DROP TABLE IF EXISTS `telefone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `telefone` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DDD` varchar(255) DEFAULT NULL,
  `NUMERO` varchar(255) NOT NULL,
  `RAMAL` varchar(255) DEFAULT NULL,
  `PESSOA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKDD8E5AEA9B8CB634` (`PESSOA_ID`),
  CONSTRAINT `FKDD8E5AEA9B8CB634` FOREIGN KEY (`PESSOA_ID`) REFERENCES `pessoa` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `telefone_cota`
--

DROP TABLE IF EXISTS `telefone_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `telefone_cota` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_TELEFONE` varchar(255) NOT NULL,
  `TELEFONE_ID` bigint(20) NOT NULL,
  `COTA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK9402480EC8181F74` (`COTA_ID`),
  KEY `FK9402480E52023CD4` (`TELEFONE_ID`),
  CONSTRAINT `FK9402480E52023CD4` FOREIGN KEY (`TELEFONE_ID`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK9402480EC8181F74` FOREIGN KEY (`COTA_ID`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `telefone_distribuidor`
--

DROP TABLE IF EXISTS `telefone_distribuidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `telefone_distribuidor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_TELEFONE` varchar(255) NOT NULL,
  `TELEFONE_ID` bigint(20) NOT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK354FBEE356501954` (`DISTRIBUIDOR_ID`),
  KEY `FK354FBEE352023CD4` (`TELEFONE_ID`),
  CONSTRAINT `FK354FBEE352023CD4` FOREIGN KEY (`TELEFONE_ID`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK354FBEE356501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `telefone_editor`
--

DROP TABLE IF EXISTS `telefone_editor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `telefone_editor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_TELEFONE` varchar(255) NOT NULL,
  `TELEFONE_ID` bigint(20) NOT NULL,
  `EDITOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK9F5A7C6252023CD4` (`TELEFONE_ID`),
  KEY `FK9F5A7C62B2A11874` (`EDITOR_ID`),
  CONSTRAINT `FK9F5A7C6252023CD4` FOREIGN KEY (`TELEFONE_ID`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK9F5A7C62B2A11874` FOREIGN KEY (`EDITOR_ID`) REFERENCES `editor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `telefone_entregador`
--

DROP TABLE IF EXISTS `telefone_entregador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `telefone_entregador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_TELEFONE` varchar(255) NOT NULL,
  `TELEFONE_ID` bigint(20) NOT NULL,
  `ENTREGADOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK121650452023CD4` (`TELEFONE_ID`),
  KEY `FK121650483B33034` (`ENTREGADOR_ID`),
  CONSTRAINT `FK121650452023CD4` FOREIGN KEY (`TELEFONE_ID`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK121650483B33034` FOREIGN KEY (`ENTREGADOR_ID`) REFERENCES `entregador` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `telefone_fiador`
--

DROP TABLE IF EXISTS `telefone_fiador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `telefone_fiador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_TELEFONE` varchar(255) NOT NULL,
  `TELEFONE_ID` bigint(20) NOT NULL,
  `FIADOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKA151EB7E52023CD4` (`TELEFONE_ID`),
  KEY `FKA151EB7E8DC372F4` (`FIADOR_ID`),
  CONSTRAINT `FKA151EB7E52023CD4` FOREIGN KEY (`TELEFONE_ID`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FKA151EB7E8DC372F4` FOREIGN KEY (`FIADOR_ID`) REFERENCES `fiador` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `telefone_fornecedor`
--

DROP TABLE IF EXISTS `telefone_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `telefone_fornecedor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_TELEFONE` varchar(255) NOT NULL,
  `TELEFONE_ID` bigint(20) NOT NULL,
  `FORNECEDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKE61D71DA52023CD4` (`TELEFONE_ID`),
  KEY `FKE61D71DA9808F874` (`FORNECEDOR_ID`),
  CONSTRAINT `FKE61D71DA52023CD4` FOREIGN KEY (`TELEFONE_ID`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FKE61D71DA9808F874` FOREIGN KEY (`FORNECEDOR_ID`) REFERENCES `fornecedor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `telefone_pdv`
--

DROP TABLE IF EXISTS `telefone_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `telefone_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_TELEFONE` varchar(255) NOT NULL,
  `TELEFONE_ID` bigint(20) NOT NULL,
  `PDV_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK67DF3A0D52023CD4` (`TELEFONE_ID`),
  KEY `FK67DF3A0DA65B70F4` (`PDV_ID`),
  CONSTRAINT `FK67DF3A0D52023CD4` FOREIGN KEY (`TELEFONE_ID`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK67DF3A0DA65B70F4` FOREIGN KEY (`PDV_ID`) REFERENCES `pdv` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `telefone_transportador`
--

DROP TABLE IF EXISTS `telefone_transportador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `telefone_transportador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_TELEFONE` varchar(255) NOT NULL,
  `TELEFONE_ID` bigint(20) NOT NULL,
  `TRANSPORTADOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK86E2BB3AD90B1440` (`TRANSPORTADOR_ID`),
  KEY `FK86E2BB3A52023CD4` (`TELEFONE_ID`),
  CONSTRAINT `FK86E2BB3A52023CD4` FOREIGN KEY (`TELEFONE_ID`) REFERENCES `telefone` (`ID`),
  CONSTRAINT `FK86E2BB3AD90B1440` FOREIGN KEY (`TRANSPORTADOR_ID`) REFERENCES `transportador` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temp_script_12_target`
--

DROP TABLE IF EXISTS `temp_script_12_target`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temp_script_12_target` (
  `data_recolhimento` date DEFAULT NULL,
  `produto_edicao_id` bigint(20) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  KEY `ndx_produto_edicao_id` (`produto_edicao_id`),
  KEY `ndx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temp_script_12_target_2`
--

DROP TABLE IF EXISTS `temp_script_12_target_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temp_script_12_target_2` (
  `data_lancamento` date DEFAULT NULL,
  `produto_edicao_id` bigint(20) DEFAULT NULL,
  KEY `ndx_produto_edicao_id` (`produto_edicao_id`),
  KEY `ndx_data_lancamento` (`data_lancamento`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_classificacao_produto`
--

DROP TABLE IF EXISTS `tipo_classificacao_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_classificacao_produto` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_cluster_pdv`
--

DROP TABLE IF EXISTS `tipo_cluster_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_cluster_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_entrega`
--

DROP TABLE IF EXISTS `tipo_entrega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_entrega` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BASE_CALCULO` varchar(255) DEFAULT NULL,
  `DESCRICAO_TIPO_ENTREGA` varchar(255) NOT NULL,
  `DIA_MES` int(11) DEFAULT NULL,
  `DIA_SEMANA` varchar(255) DEFAULT NULL,
  `PERCENTUAL_FATURAMENTO` float DEFAULT NULL,
  `PERIODICIDADE` varchar(255) NOT NULL,
  `TAXA_FIXA` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_estabelecimento_associacao_pdv`
--

DROP TABLE IF EXISTS `tipo_estabelecimento_associacao_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_estabelecimento_associacao_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_fornecedor`
--

DROP TABLE IF EXISTS `tipo_fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_fornecedor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRICAO` varchar(255) NOT NULL,
  `GRUPO_FORNECEDOR` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_garantia_aceita`
--

DROP TABLE IF EXISTS `tipo_garantia_aceita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_garantia_aceita` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tipoGarantia` varchar(255) DEFAULT NULL,
  `VALOR` int(11) DEFAULT NULL,
  `DISTRIBUIDOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8EB4B40056501954` (`DISTRIBUIDOR_ID`),
  CONSTRAINT `FK8EB4B40056501954` FOREIGN KEY (`DISTRIBUIDOR_ID`) REFERENCES `distribuidor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_gerador_fluxo_pdv`
--

DROP TABLE IF EXISTS `tipo_gerador_fluxo_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_gerador_fluxo_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_licenca_municipal`
--

DROP TABLE IF EXISTS `tipo_licenca_municipal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_licenca_municipal` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_movimento`
--

DROP TABLE IF EXISTS `tipo_movimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_movimento` (
  `TIPO` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `APROVACAO_AUTOMATICA` tinyint(1) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  `GRUPO_MOVIMENTO_ESTOQUE` varchar(255) DEFAULT NULL,
  `INCIDE_DIVIDA` tinyint(1) DEFAULT NULL,
  `INCIDE_JURAMENTADO` tinyint(1) DEFAULT NULL,
  `OPERACAO_ESTOQUE` varchar(255) DEFAULT NULL,
  `GRUPO_MOVIMENTO_FINANCEIRO` varchar(255) DEFAULT NULL,
  `OPERACAO_FINANCEIRA` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `NDX_GRUPO_MOVIMENTO_ESTOQUE` (`GRUPO_MOVIMENTO_ESTOQUE`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_nota_fiscal`
--

DROP TABLE IF EXISTS `tipo_nota_fiscal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_nota_fiscal` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONTRIBUINTE` tinyint(1) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  `DESTINATARIO` varchar(255) NOT NULL,
  `EMITENTE` varchar(255) NOT NULL,
  `GRUPO_NOTA_FISCAL` varchar(255) NOT NULL,
  `NOP_CODIGO` bigint(20) NOT NULL,
  `NOP_DESCRICAO` varchar(255) DEFAULT NULL,
  `SERIE_NF` int(11) NOT NULL,
  `TIPO_ATIVIDADE` varchar(255) NOT NULL,
  `TIPO_OPERACAO` varchar(255) NOT NULL,
  `CFOP_ESTADO` bigint(20) DEFAULT NULL,
  `CFOP_OUTROS_ESTADOS` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK922056A4A662870A` (`CFOP_OUTROS_ESTADOS`),
  KEY `FK922056A44B86991A` (`CFOP_ESTADO`),
  CONSTRAINT `FK922056A44B86991A` FOREIGN KEY (`CFOP_ESTADO`) REFERENCES `cfop` (`ID`),
  CONSTRAINT `FK922056A4A662870A` FOREIGN KEY (`CFOP_OUTROS_ESTADOS`) REFERENCES `cfop` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_ponto_pdv`
--

DROP TABLE IF EXISTS `tipo_ponto_pdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_ponto_pdv` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_produto`
--

DROP TABLE IF EXISTS `tipo_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_produto` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODIGO` bigint(20) NOT NULL,
  `CODIGO_NBM` varchar(255) DEFAULT NULL,
  `DESCRICAO` varchar(25) NOT NULL,
  `GRUPO_PRODUTO` varchar(255) NOT NULL,
  `NCM_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`),
  UNIQUE KEY `DESCRICAO` (`DESCRICAO`),
  KEY `FKB87BD8CE3FD9B8E5` (`NCM_ID`),
  CONSTRAINT `FKB87BD8CE3FD9B8E5` FOREIGN KEY (`NCM_ID`) REFERENCES `ncm` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_produto_editor`
--

DROP TABLE IF EXISTS `tipo_produto_editor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_produto_editor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PRINCIPAL` tinyint(1) NOT NULL,
  `TIPO_PRODUTO_ID` bigint(20) NOT NULL,
  `EDITOR_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK874255FE99DDFD97` (`TIPO_PRODUTO_ID`),
  KEY `FK874255FEB2A11874` (`EDITOR_ID`),
  CONSTRAINT `FK874255FE99DDFD97` FOREIGN KEY (`TIPO_PRODUTO_ID`) REFERENCES `tipo_produto` (`ID`),
  CONSTRAINT `FK874255FEB2A11874` FOREIGN KEY (`EDITOR_ID`) REFERENCES `editor` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_segmento_produto`
--

DROP TABLE IF EXISTS `tipo_segmento_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_segmento_produto` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRICAO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transportador`
--

DROP TABLE IF EXISTS `transportador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transportador` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `RESPONSAVEL` varchar(255) DEFAULT NULL,
  `PESSOA_JURIDICA_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PESSOA_JURIDICA_ID` (`PESSOA_JURIDICA_ID`),
  KEY `FK629CEFCF40C343A3` (`PESSOA_JURIDICA_ID`),
  CONSTRAINT `FK629CEFCF40C343A3` FOREIGN KEY (`PESSOA_JURIDICA_ID`) REFERENCES `pessoa` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CEP` varchar(255) DEFAULT NULL,
  `CIDADE` varchar(255) DEFAULT NULL,
  `CONTA_ATIVA` tinyint(1) DEFAULT NULL,
  `DDD` varchar(255) DEFAULT NULL,
  `EMAIL` varchar(255) NOT NULL,
  `ENDERECO` varchar(255) DEFAULT NULL,
  `LEMBRETE_SENHA` varchar(255) DEFAULT NULL,
  `LOGIN` varchar(255) NOT NULL,
  `NOME` varchar(255) NOT NULL,
  `PAIS` varchar(255) DEFAULT NULL,
  `SENHA` varchar(255) NOT NULL,
  `SOBRENOME` varchar(255) DEFAULT NULL,
  `TELEFONE` varchar(255) DEFAULT NULL,
  `BOX_ID` bigint(20) DEFAULT NULL,
  `SYS` tinyint(1) DEFAULT '0',
  `SUPERVISOR` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKSHYE54DS545R5R4` (`BOX_ID`),
  KEY `NDX_LOGIN` (`LOGIN`),
  CONSTRAINT `FKS5444TGTG4HJ5456Y` FOREIGN KEY (`BOX_ID`) REFERENCES `box` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario_grupo_permissao`
--

DROP TABLE IF EXISTS `usuario_grupo_permissao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_grupo_permissao` (
  `USUARIO_ID` bigint(20) NOT NULL,
  `gruposPermissoes_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`USUARIO_ID`,`gruposPermissoes_ID`),
  KEY `FKC8F99C07FFF790E` (`USUARIO_ID`),
  KEY `FKC8F99C0EE8B8DB6` (`gruposPermissoes_ID`),
  CONSTRAINT `FKC8F99C07FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKC8F99C0EE8B8DB6` FOREIGN KEY (`gruposPermissoes_ID`) REFERENCES `grupo_permissao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario_perfil_usuario`
--

DROP TABLE IF EXISTS `usuario_perfil_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_perfil_usuario` (
  `USUARIO_ID` bigint(20) NOT NULL,
  `perfilUsuario_ID` bigint(20) NOT NULL,
  UNIQUE KEY `perfilUsuario_ID` (`perfilUsuario_ID`),
  KEY `FK355A28EC7FFF790E` (`USUARIO_ID`),
  KEY `FK355A28ECDC85056E` (`perfilUsuario_ID`),
  CONSTRAINT `FK355A28EC7FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FK355A28ECDC85056E` FOREIGN KEY (`perfilUsuario_ID`) REFERENCES `perfil_usuario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario_permissao`
--

DROP TABLE IF EXISTS `usuario_permissao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_permissao` (
  `USUARIO_ID` bigint(20) NOT NULL,
  `PERMISSAO_ID` varchar(255) DEFAULT NULL,
  KEY `FK19B419767FFF790E` (`USUARIO_ID`),
  CONSTRAINT `FK19B419767FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vale_desconto_produto`
--

DROP TABLE IF EXISTS `vale_desconto_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vale_desconto_produto` (
  `VALE_DESCONTO_ID` bigint(20) NOT NULL,
  `PRODUTO_EDICAO_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`VALE_DESCONTO_ID`,`PRODUTO_EDICAO_ID`),
  UNIQUE KEY `PRODUTO_EDICAO_ID` (`PRODUTO_EDICAO_ID`),
  KEY `FK1A20C6C0A53173D3` (`PRODUTO_EDICAO_ID`),
  KEY `FK1A20C6C041EC3F5D` (`VALE_DESCONTO_ID`),
  CONSTRAINT `FK1A20C6C041EC3F5D` FOREIGN KEY (`VALE_DESCONTO_ID`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FK1A20C6C0A53173D3` FOREIGN KEY (`PRODUTO_EDICAO_ID`) REFERENCES `produto_edicao` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `veiculo`
--

DROP TABLE IF EXISTS `veiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `veiculo` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PLACA` varchar(255) DEFAULT NULL,
  `TIPO_VEICULO` varchar(255) DEFAULT NULL,
  `TRANSPORTADOR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3F3ABBEFD90B1440` (`TRANSPORTADOR_ID`),
  CONSTRAINT `FK3F3ABBEFD90B1440` FOREIGN KEY (`TRANSPORTADOR_ID`) REFERENCES `transportador` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `venda_produto`
--

DROP TABLE IF EXISTS `venda_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `venda_produto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATA_VENCIMENTO_DEBITO` date DEFAULT NULL,
  `DATA_VENDA` date DEFAULT NULL,
  `HORARIO_VENDA` time DEFAULT NULL,
  `QNT_PRODUTO` decimal(18,4) DEFAULT NULL,
  `TIPO_COMERCIALIZACAO_VENDA` varchar(255) DEFAULT NULL,
  `TIPO_VENDA_ENCALHE` varchar(255) NOT NULL,
  `VALOR_TOTAL_VENDA` decimal(18,4) DEFAULT NULL,
  `ID_COTA` bigint(20) DEFAULT NULL,
  `ID_PRODUTO_EDICAO` bigint(20) DEFAULT NULL,
  `USUARIO_ID` bigint(20) NOT NULL,
  `PRECO_VENDA` decimal(18,4) DEFAULT NULL,
  `PRECO_COM_DESCONTO` decimal(18,4) DEFAULT NULL,
  `VALOR_DESCONTO` decimal(18,4) DEFAULT NULL,
  `DATA_OPERACAO` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBE5F75D67FFF790E` (`USUARIO_ID`),
  KEY `FKBE5F75D6F1916CB0` (`ID_COTA`),
  KEY `FKBE5F75D6D2FE34B7` (`ID_PRODUTO_EDICAO`),
  CONSTRAINT `FKBE5F75D67FFF790E` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuario` (`ID`),
  CONSTRAINT `FKBE5F75D6D2FE34B7` FOREIGN KEY (`ID_PRODUTO_EDICAO`) REFERENCES `produto_edicao` (`ID`),
  CONSTRAINT `FKBE5F75D6F1916CB0` FOREIGN KEY (`ID_COTA`) REFERENCES `cota` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `venda_produto_movimento_estoque`
--

DROP TABLE IF EXISTS `venda_produto_movimento_estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `venda_produto_movimento_estoque` (
  `ID_VENDA_PRODUTO` bigint(20) NOT NULL,
  `ID_MOVIMENTO_ESTOQUE` bigint(20) NOT NULL,
  PRIMARY KEY (`ID_VENDA_PRODUTO`,`ID_MOVIMENTO_ESTOQUE`),
  UNIQUE KEY `ID_MOVIMENTO_ESTOQUE` (`ID_MOVIMENTO_ESTOQUE`),
  KEY `FK312156EC1EDD2580` (`ID_VENDA_PRODUTO`),
  KEY `FK312156ECEC4995FE` (`ID_MOVIMENTO_ESTOQUE`),
  CONSTRAINT `FK312156EC1EDD2580` FOREIGN KEY (`ID_VENDA_PRODUTO`) REFERENCES `venda_produto` (`id`),
  CONSTRAINT `FK312156ECEC4995FE` FOREIGN KEY (`ID_MOVIMENTO_ESTOQUE`) REFERENCES `movimento_estoque` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `venda_produto_movimento_financeiro`
--

DROP TABLE IF EXISTS `venda_produto_movimento_financeiro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `venda_produto_movimento_financeiro` (
  `ID_VENDA_PRODUTO` bigint(20) NOT NULL,
  `ID_MOVIMENTO_FINANCEIRO` bigint(20) NOT NULL,
  PRIMARY KEY (`ID_VENDA_PRODUTO`,`ID_MOVIMENTO_FINANCEIRO`),
  UNIQUE KEY `ID_MOVIMENTO_FINANCEIRO` (`ID_MOVIMENTO_FINANCEIRO`),
  KEY `FK2665ED381EDD2580` (`ID_VENDA_PRODUTO`),
  KEY `FK2665ED3829F6062D` (`ID_MOVIMENTO_FINANCEIRO`),
  CONSTRAINT `FK2665ED381EDD2580` FOREIGN KEY (`ID_VENDA_PRODUTO`) REFERENCES `venda_produto` (`id`),
  CONSTRAINT `FK2665ED3829F6062D` FOREIGN KEY (`ID_MOVIMENTO_FINANCEIRO`) REFERENCES `movimento_financeiro_cota` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `verificar`
--

DROP TABLE IF EXISTS `verificar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `verificar` (
  `prod_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `view_consolidado_movimento_estoque_cota`
--

DROP TABLE IF EXISTS `view_consolidado_movimento_estoque_cota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `view_consolidado_movimento_estoque_cota` (
  `COTA_ID` tinyint(4) NOT NULL,
  `PRODUTO_ID` tinyint(4) NOT NULL,
  `EDITOR_ID` tinyint(4) NOT NULL,
  `PRODUTO_EDICAO_ID` tinyint(4) NOT NULL,
  `NUMERO_EDICAO` tinyint(4) NOT NULL,
  `DATA_MOVIMENTO` tinyint(4) NOT NULL,
  `PRECO_VENDA` tinyint(4) NOT NULL,
  `QNT_ENTRADA_PRODUTO` tinyint(4) NOT NULL,
  `QNT_SAIDA_PRODUTO` tinyint(4) NOT NULL,
  `VALOR_TOTAL_VENDA` tinyint(4) NOT NULL,
  `DESCONTO_PRODUTO` tinyint(4) NOT NULL,
  `VALOR_TOTAL_VENDA_COM_DESCONTO` tinyint(4) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `view_desconto_cota_fornecedor_produtos_edicoes`
--

DROP TABLE IF EXISTS `view_desconto_cota_fornecedor_produtos_edicoes`;
/*!50001 DROP VIEW IF EXISTS `view_desconto_cota_fornecedor_produtos_edicoes`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `view_desconto_cota_fornecedor_produtos_edicoes` AS SELECT 
 1 AS `DESCONTO_ID`,
 1 AS `COTA_ID`,
 1 AS `NUMERO_COTA`,
 1 AS `NOME_COTA`,
 1 AS `VALOR`,
 1 AS `DATA_ALTERACAO`,
 1 AS `NOME_USUARIO`,
 1 AS `PRODUTO_ID`,
 1 AS `CODIGO_PRODUTO`,
 1 AS `NOME_PRODUTO`,
 1 AS `PRODUTO_EDICAO_ID`,
 1 AS `NUMERO_EDICAO`,
 1 AS `FORNECEDOR_ID`,
 1 AS `NOME_FORNECEDOR`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_desconto_produtos_edicoes`
--

DROP TABLE IF EXISTS `view_desconto_produtos_edicoes`;
/*!50001 DROP VIEW IF EXISTS `view_desconto_produtos_edicoes`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `view_desconto_produtos_edicoes` AS SELECT 
 1 AS `DESCONTO_ID`,
 1 AS `PRODUTO_ID`,
 1 AS `CODIGO_PRODUTO`,
 1 AS `NOME_PRODUTO`,
 1 AS `PRODUTO_EDICAO_ID`,
 1 AS `NUMERO_EDICAO`,
 1 AS `VALOR`,
 1 AS `DATA_ALTERACAO`,
 1 AS `NOME_USUARIO`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'db_09795816'
--
/*!50003 DROP PROCEDURE IF EXISTS `ROLLOUT` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE  PROCEDURE `ROLLOUT`(IN p_in_cod_distribuidor VARCHAR(8), OUT p_out_saida TEXT)
BEGIN

 SET p_out_saida = 'Procedure Rollout executada com sucesso.';

 SELECT p_out_saida;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `SP_AtualizaEstoqueProduto` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE  PROCEDURE `SP_AtualizaEstoqueProduto`( in id_produto_edicao int, in qtde_fila bigint(20), in operacao_estoque varchar(255), in tipo_estoque varchar(255)  )
BEGIN 

	declare contador int(11); 
	
	SELECT count(*) into contador FROM estoque_produto WHERE produto_edicao_id = id_produto_edicao; 
	
	IF (operacao_estoque = 'SAIDA') THEN
		set qtde_fila = (qtde_fila * -1);
	END IF;
	
	IF (contador > 0) THEN 

		IF (tipo_estoque = 'DEVOLUCAO_ENCALHE') THEN 
			UPDATE estoque_produto SET qtde_devolucao_encalhe=COALESCE(qtde_devolucao_encalhe, 0) + qtde_fila WHERE produto_edicao_id = id_produto_edicao; 
		END IF;

		IF (tipo_estoque = 'SUPLEMENTAR') THEN 
			UPDATE estoque_produto SET qtde_suplementar=COALESCE(qtde_suplementar, 0) + qtde_fila WHERE produto_edicao_id = id_produto_edicao; 
		END IF;

		IF (tipo_estoque = 'JURAMENTADO') THEN 
			UPDATE estoque_produto SET qtde_juramentado=COALESCE(qtde_juramentado, 0) + qtde_fila WHERE produto_edicao_id = id_produto_edicao; 
		END IF;
	
	ELSE 

		IF (tipo_estoque = 'DEVOLUCAO_ENCALHE') THEN 
			INSERT INTO estoque_produto  (qtde_devolucao_encalhe, produto_edicao_id) values (qtde_fila, id_produto_edicao); 
		END IF;

		IF (tipo_estoque = 'SUPLEMENTAR') THEN 
			INSERT INTO estoque_produto (qtde_suplementar, produto_edicao_id) values (qtde_fila, id_produto_edicao); 
		END IF;

		IF (tipo_estoque = 'JURAMENTADO') THEN 
			INSERT INTO estoque_produto (qtde_juramentado, produto_edicao_id) values (qtde_fila, id_produto_edicao); 
		END IF;
	
	END IF; 
	
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `view_desconto_cota_fornecedor_produtos_edicoes`
--

/*!50001 DROP VIEW IF EXISTS `view_desconto_cota_fornecedor_produtos_edicoes`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `view_desconto_cota_fornecedor_produtos_edicoes` AS select `d`.`ID` AS `DESCONTO_ID`,`c`.`ID` AS `COTA_ID`,`c`.`NUMERO_COTA` AS `NUMERO_COTA`,(case when (`p`.`TIPO` = 'F') then `p`.`NOME` else `p`.`RAZAO_SOCIAL` end) AS `NOME_COTA`,`d`.`valor` AS `VALOR`,`d`.`DATA_ALTERACAO` AS `DATA_ALTERACAO`,`u`.`NOME` AS `NOME_USUARIO`,NULL AS `PRODUTO_ID`,NULL AS `CODIGO_PRODUTO`,NULL AS `NOME_PRODUTO`,NULL AS `PRODUTO_EDICAO_ID`,NULL AS `NUMERO_EDICAO`,`f`.`ID` AS `FORNECEDOR_ID`,(case when (`p2`.`TIPO` = 'F') then `p2`.`NOME` else `p2`.`RAZAO_SOCIAL` end) AS `NOME_FORNECEDOR` from ((((((`desconto_cota_produto_excessoes` `hdcpe` join `cota` `c` on((`hdcpe`.`COTA_ID` = `c`.`ID`))) join `pessoa` `p` on((`c`.`PESSOA_ID` = `p`.`ID`))) join `fornecedor` `f` on((`hdcpe`.`FORNECEDOR_ID` = `f`.`ID`))) join `pessoa` `p2` on((`f`.`JURIDICA_ID` = `p2`.`ID`))) join `desconto` `d` on((`hdcpe`.`DESCONTO_ID` = `d`.`ID`))) join `usuario` `u` on((`d`.`USUARIO_ID` = `u`.`ID`))) where (isnull(`hdcpe`.`PRODUTO_ID`) and isnull(`hdcpe`.`PRODUTO_EDICAO_ID`)) union select `d`.`ID` AS `DESCONTO_ID`,`c`.`ID` AS `COTA_ID`,`c`.`NUMERO_COTA` AS `NUMERO_COTA`,(case when (`p`.`TIPO` = 'F') then `p`.`NOME` else `p`.`RAZAO_SOCIAL` end) AS `NOME_COTA`,`d`.`valor` AS `VALOR`,`d`.`DATA_ALTERACAO` AS `DATA_ALTERACAO`,`u`.`NOME` AS `NOME_USUARIO`,`pr`.`ID` AS `PRODUTO_ID`,`pr`.`CODIGO` AS `CODIGO_PRODUTO`,`pr`.`NOME` AS `NOME_PRODUTO`,NULL AS `PRODUTO_EDICAO_ID`,NULL AS `NUMERO_EDICAO`,`f`.`ID` AS `FORNECEDOR_ID`,(case when (`p2`.`TIPO` = 'F') then `p2`.`NOME` else `p2`.`RAZAO_SOCIAL` end) AS `NOME_FORNECEDOR` from (((((((`desconto_cota_produto_excessoes` `hdcpe` join `cota` `c` on((`hdcpe`.`COTA_ID` = `c`.`ID`))) join `produto` `pr` on((`hdcpe`.`PRODUTO_ID` = `pr`.`ID`))) join `pessoa` `p` on((`c`.`PESSOA_ID` = `p`.`ID`))) join `fornecedor` `f` on((`hdcpe`.`FORNECEDOR_ID` = `f`.`ID`))) join `pessoa` `p2` on((`f`.`JURIDICA_ID` = `p2`.`ID`))) join `desconto` `d` on((`hdcpe`.`DESCONTO_ID` = `d`.`ID`))) join `usuario` `u` on((`d`.`USUARIO_ID` = `u`.`ID`))) where isnull(`hdcpe`.`PRODUTO_EDICAO_ID`) union select `d`.`ID` AS `DESCONTO_ID`,`c`.`ID` AS `COTA_ID`,`c`.`NUMERO_COTA` AS `NUMERO_COTA`,(case when (`p`.`TIPO` = 'F') then `p`.`NOME` else `p`.`RAZAO_SOCIAL` end) AS `NOME_COTA`,`d`.`valor` AS `VALOR`,`d`.`DATA_ALTERACAO` AS `DATA_ALTERACAO`,`u`.`NOME` AS `NOME_USUARIO`,`pr`.`ID` AS `PRODUTO_ID`,`pr`.`CODIGO` AS `CODIGO_PRODUTO`,`pr`.`NOME` AS `NOME_PRODUTO`,`pe`.`ID` AS `PRODUTO_EDICAO_ID`,`pe`.`NUMERO_EDICAO` AS `NUMERO_EDICAO`,`f`.`ID` AS `FORNECEDOR_ID`,(case when (`p2`.`TIPO` = 'F') then `p2`.`NOME` else `p2`.`RAZAO_SOCIAL` end) AS `NOME_FORNECEDOR` from ((((((((`desconto_cota_produto_excessoes` `hdcpe` join `cota` `c` on((`hdcpe`.`COTA_ID` = `c`.`ID`))) join `produto` `pr` on((`hdcpe`.`PRODUTO_ID` = `pr`.`ID`))) join `produto_edicao` `pe` on((`hdcpe`.`PRODUTO_EDICAO_ID` = `pe`.`ID`))) join `pessoa` `p` on((`c`.`PESSOA_ID` = `p`.`ID`))) join `fornecedor` `f` on((`hdcpe`.`FORNECEDOR_ID` = `f`.`ID`))) join `pessoa` `p2` on((`f`.`JURIDICA_ID` = `p2`.`ID`))) join `desconto` `d` on((`hdcpe`.`DESCONTO_ID` = `d`.`ID`))) join `usuario` `u` on((`d`.`USUARIO_ID` = `u`.`ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_desconto_produtos_edicoes`
--

/*!50001 DROP VIEW IF EXISTS `view_desconto_produtos_edicoes`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50001 VIEW `view_desconto_produtos_edicoes` AS select `d`.`ID` AS `DESCONTO_ID`,`p`.`ID` AS `PRODUTO_ID`,`p`.`CODIGO` AS `CODIGO_PRODUTO`,`p`.`NOME` AS `NOME_PRODUTO`,`pe`.`ID` AS `PRODUTO_EDICAO_ID`,`pe`.`NUMERO_EDICAO` AS `NUMERO_EDICAO`,`d`.`valor` AS `VALOR`,`d`.`DATA_ALTERACAO` AS `DATA_ALTERACAO`,`u`.`NOME` AS `NOME_USUARIO` from ((((`historico_desconto_produto_edicoes` `hdpe` join `produto_edicao` `pe` on((`hdpe`.`PRODUTO_EDICAO_ID` = `pe`.`ID`))) join `produto` `p` on((`pe`.`PRODUTO_ID` = `p`.`ID`))) join `desconto` `d` on((`hdpe`.`DESCONTO_ID` = `d`.`ID`))) join `usuario` `u` on((`d`.`USUARIO_ID` = `u`.`ID`))) union select `d`.`ID` AS `DESCONTO_ID`,`p`.`ID` AS `PRODUTO_ID`,`p`.`CODIGO` AS `CODIGO_PRODUTO`,`p`.`NOME` AS `NOME_PRODUTO`,NULL AS `PRODUTO_EDICAO_ID`,NULL AS `NUMERO_EDICAO`,`d`.`valor` AS `VALOR`,`d`.`DATA_ALTERACAO` AS `DATA_ALTERACAO`,`u`.`NOME` AS `NOME_USUARIO` from (((`historico_desconto_produtos` `hdp` join `produto` `p` on((`hdp`.`PRODUTO_ID` = `p`.`ID`))) join `desconto` `d` on((`hdp`.`DESCONTO_ID` = `d`.`ID`))) join `usuario` `u` on((`d`.`USUARIO_ID` = `u`.`ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-08-28 13:34:17
