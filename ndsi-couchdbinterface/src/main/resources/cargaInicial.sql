CREATE DATABASE  IF NOT EXISTS `nds-server` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `nds-server`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: nds-controle
-- ------------------------------------------------------
-- Server version	5.5.24

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
-- Dumping data for table `interface_execucao`
--

LOCK TABLES `interface_execucao` WRITE;
/*!40000 ALTER TABLE `interface_execucao` DISABLE KEYS */;
INSERT INTO `interface_execucao` (`ID`, `MASCARA_ARQUIVO`, `NOME`) VALUES (109,'([0-9]{8})\\.pub','EMS0109'),(110,'([0-9]{8})\\.prd','EMS0110'),(111,'([0-9]{8})\\.lan','EMS0111'),(112,'([0-9]{10})\\.edi','EMS0112'),(113,'([0-9]{10})\\.dsf','EMS0113'),(114,'([0-9]{8})\\.rec','EMS0114'),(125,'([0-9]{8})\\.chc','EMS0125'),(126,'([0-9]{8})\\.cdb','EMS0126'),(134,'.jpeg','EMS0134'),(135,'([0-9]{8})\\.nre','EMS0135'),(185,'.mdb','EMS0185');
/*!40000 ALTER TABLE `interface_execucao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `parametro_sistema`
--

LOCK TABLES `parametro_sistema` WRITE;
/*!40000 ALTER TABLE `parametro_sistema` DISABLE KEYS */;
INSERT INTO `parametro_sistema` (`ID`, `TIPO_PARAMETRO_SISTEMA`, `VALOR`) VALUES (1,'INBOUND_DIR','c:\\interface\\prodin\\'),(2,'IMAGE_DIR','c:\\interface\\imagens\\'),(3,'CORREIOS_DIR','c:\\interface\\correios\\');
/*!40000 ALTER TABLE `parametro_sistema` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-06-25 13:21:23
