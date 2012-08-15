/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 50151
 Source Host           : localhost
 Source Database       : nds-client

 Target Server Type    : MySQL
 Target Server Version : 50151
 File Encoding         : utf-8

 Date: 06/19/2012 10:23:36 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `grupo`
-- ----------------------------
DROP TABLE IF EXISTS `grupo`;
CREATE TABLE `grupo` (
  `grupo_id` int(11) NOT NULL AUTO_INCREMENT,
  `grupo_nome` varchar(100) NOT NULL,
  PRIMARY KEY (`grupo_id`),
  UNIQUE KEY `grupo_uk` (`grupo_nome`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `grupo`
-- ----------------------------
BEGIN;
INSERT INTO `grupo` VALUES ('1', 'Admin'), ('3', 'Conferente'), ('2', 'Financeiro');
COMMIT;

-- ----------------------------
--  Table structure for `grupo_permissao`
-- ----------------------------
DROP TABLE IF EXISTS `grupo_permissao`;
CREATE TABLE `grupo_permissao` (
  `grupo_id` int(11) NOT NULL,
  `permissao_id` int(11) NOT NULL,
  PRIMARY KEY (`grupo_id`,`permissao_id`),
  KEY `permissao_id` (`permissao_id`),
  KEY `permissao_id_2` (`permissao_id`,`grupo_id`),
  CONSTRAINT `grupo_permissao_ibfk_1` FOREIGN KEY (`grupo_id`) REFERENCES `grupo` (`grupo_id`),
  CONSTRAINT `grupo_permissao_ibfk_2` FOREIGN KEY (`permissao_id`) REFERENCES `permissao` (`permissao_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `grupo_permissao`
-- ----------------------------
BEGIN;
INSERT INTO `grupo_permissao` VALUES ('2', '0'), ('3', '0'), ('3', '1'), ('3', '2'), ('3', '4'), ('2', '6'), ('2', '7');
COMMIT;

-- ----------------------------
--  Table structure for `grupo_usuario`
-- ----------------------------
DROP TABLE IF EXISTS `grupo_usuario`;
CREATE TABLE `grupo_usuario` (
  `grupo_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  PRIMARY KEY (`grupo_id`,`usuario_id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `usuario_id_2` (`usuario_id`,`grupo_id`),
  CONSTRAINT `grupo_usuario_ibfk_1` FOREIGN KEY (`grupo_id`) REFERENCES `grupo` (`grupo_id`),
  CONSTRAINT `grupo_usuario_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `grupo_usuario`
-- ----------------------------
BEGIN;
INSERT INTO `grupo_usuario` VALUES ('3', '2'), ('2', '3'), ('2', '4'), ('2', '5'), ('2', '6'), ('3', '7');
COMMIT;

-- ----------------------------
--  Table structure for `permissao`
-- ----------------------------
DROP TABLE IF EXISTS `permissao`;
CREATE TABLE `permissao` (
  `permissao_id` int(11) NOT NULL AUTO_INCREMENT,
  `permissao` varchar(100) NOT NULL,
  `permissao_descricao` varchar(200) NOT NULL,
  PRIMARY KEY (`permissao_id`),
  UNIQUE KEY `permissao_uk` (`permissao`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `permissao`
-- ----------------------------
BEGIN;
INSERT INTO `permissao` VALUES ('0', 'SEM_RESTRICAO', 'Não há restrição de acesso'), ('1', 'Estoque.RecebimentoFisico.VER_REPARTE_PREVISTO', 'Pode ver a coluna Reparte Previsto'), ('2', 'Estoque.RecebimentoFisico.PODE_CONFIRMAR', 'Pode acessar o botão Confirmar'), ('4', 'Estoque', 'Pode acessar tudo do estoque'), ('6', 'Financeiro.Cadastro.FIADOR', 'Pode acessar cadastro de Fiadores'), ('7', 'Financeiro.Cadastro.Banco', 'Pode Acessar cadastro de Bancos');
COMMIT;

-- ----------------------------
--  Table structure for `usuario`
-- ----------------------------
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `usuario_id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_login` varchar(100) NOT NULL,
  `usuario_senha` varchar(32) NOT NULL,
  `usuario_nome` varchar(100) NOT NULL,
  PRIMARY KEY (`usuario_id`),
  UNIQUE KEY `usuario_uk` (`usuario_login`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `usuario`
-- ----------------------------
BEGIN;
INSERT INTO `usuario` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Administrador'), ('2', 'conferente', 'f62b219d91a2381d23993fa36c5b77f8', 'Conferente'), ('3', 'financeiro', '7348cba539160cf399993a4b23832856', 'Financeiro'), ('4', 'financeiro-cota', '7348cba539160cf399993a4b23832856', 'Financeiro Cota'), ('5', 'financeiro-fiador', '7348cba539160cf399993a4b23832856', 'Financeiro Fiador'), ('6', 'financeiro-banco', '7348cba539160cf399993a4b23832856', 'Financeiro Banco'), ('7', 'conferente-reparte', 'f62b219d91a2381d23993fa36c5b77f8', 'Conferente Reparte');
COMMIT;

-- ----------------------------
--  Table structure for `usuario_excecao_permissao`
-- ----------------------------
DROP TABLE IF EXISTS `usuario_excecao_permissao`;
CREATE TABLE `usuario_excecao_permissao` (
  `usuario_id` int(11) NOT NULL,
  `grupo_id` int(11) NOT NULL,
  `permissao_id` int(11) NOT NULL,
  PRIMARY KEY (`usuario_id`,`permissao_id`,`grupo_id`),
  KEY `permissao_id` (`permissao_id`,`grupo_id`),
  KEY `permissao_id_2` (`permissao_id`,`grupo_id`),
  KEY `usuario_id` (`usuario_id`,`grupo_id`),
  CONSTRAINT `usuario_excecao_permissao_ibfk_2` FOREIGN KEY (`permissao_id`, `grupo_id`) REFERENCES `grupo_permissao` (`permissao_id`, `grupo_id`),
  CONSTRAINT `usuario_excecao_permissao_ibfk_1` FOREIGN KEY (`usuario_id`, `grupo_id`) REFERENCES `grupo_usuario` (`usuario_id`, `grupo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `usuario_excecao_permissao`
-- ----------------------------
BEGIN;
INSERT INTO `usuario_excecao_permissao` VALUES ('3', '2', '6');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
