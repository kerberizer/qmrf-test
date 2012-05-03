-- MySQL dump 10.13  Distrib 5.5.13, for Win64 (x86)
--
-- Host: localhost    Database: qmrf
-- ------------------------------------------------------
-- Server version	5.5.13

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
-- Table structure for table `dictionary`
--

DROP TABLE IF EXISTS `dictionary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dictionary` (
  `idsubject` int(10) unsigned NOT NULL,
  `relationship` enum('is_a','is_part_of','same_as') COLLATE utf8_bin NOT NULL DEFAULT 'is_a',
  `idobject` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idsubject`,`relationship`,`idobject`),
  KEY `FK_dictionary_2` (`idobject`),
  CONSTRAINT `FK_dictionary_1` FOREIGN KEY (`idsubject`) REFERENCES `template` (`idtemplate`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_dictionary_2` FOREIGN KEY (`idobject`) REFERENCES `template` (`idtemplate`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dictionary`
--

LOCK TABLES `dictionary` WRITE;
/*!40000 ALTER TABLE `dictionary` DISABLE KEYS */;
INSERT INTO `dictionary` VALUES (270,'is_part_of',1),(271,'is_part_of',1),(272,'is_part_of',1),(273,'is_part_of',1),(274,'is_part_of',1),(275,'is_part_of',1),(276,'is_part_of',1),(277,'is_part_of',1),(278,'is_part_of',1),(340,'is_part_of',1),(341,'is_part_of',1),(342,'is_part_of',1),(343,'is_part_of',1),(344,'is_part_of',1),(345,'is_part_of',1),(346,'is_part_of',1),(347,'is_part_of',1),(348,'is_part_of',1),(349,'is_part_of',1),(350,'is_part_of',1),(351,'is_part_of',1),(352,'is_part_of',1),(353,'is_part_of',1),(354,'is_part_of',1),(355,'is_part_of',1),(356,'is_part_of',1),(309,'is_part_of',2),(310,'is_part_of',2),(311,'is_part_of',2),(312,'is_part_of',2),(313,'is_part_of',2),(314,'is_part_of',2),(315,'is_part_of',2),(316,'is_part_of',2),(317,'is_part_of',2),(318,'is_part_of',2),(319,'is_part_of',2),(320,'is_part_of',2),(321,'is_part_of',2),(322,'is_part_of',2),(323,'is_part_of',2),(324,'is_part_of',2),(325,'is_part_of',2),(326,'is_part_of',2),(327,'is_part_of',2),(328,'is_part_of',2),(329,'is_part_of',2),(330,'is_part_of',2),(331,'is_part_of',2),(332,'is_part_of',2),(333,'is_part_of',2),(334,'is_part_of',2),(335,'is_part_of',2),(336,'is_part_of',2),(337,'is_part_of',2),(338,'is_part_of',2),(357,'is_part_of',2),(358,'is_part_of',2),(74,'is_a',3),(76,'is_a',3),(77,'is_a',3),(78,'is_a',3),(79,'is_a',3),(80,'is_a',3),(81,'is_a',3),(82,'is_a',3),(83,'is_a',3),(84,'is_a',3),(85,'is_a',3),(86,'is_a',3),(87,'is_a',3),(88,'is_a',3),(89,'is_a',3),(90,'is_a',3),(219,'is_a',3),(220,'is_a',3),(221,'is_a',3),(222,'is_a',3),(223,'is_a',3),(224,'is_a',3),(225,'is_a',3),(226,'is_a',3),(227,'is_a',3),(228,'is_a',3),(229,'is_a',3),(230,'is_a',3),(231,'is_a',3),(232,'is_a',3),(233,'is_a',3),(234,'is_a',3),(117,'is_a',4),(279,'is_part_of',4),(280,'is_part_of',4),(281,'is_part_of',4),(282,'is_part_of',4),(283,'is_part_of',4),(284,'is_part_of',4),(285,'is_part_of',4),(286,'is_part_of',4),(287,'is_part_of',4),(288,'is_part_of',4),(289,'is_part_of',4),(290,'is_part_of',4),(291,'is_part_of',4),(292,'is_part_of',4),(293,'is_part_of',4),(294,'is_part_of',4),(295,'is_part_of',4),(296,'is_part_of',4),(297,'is_part_of',4),(298,'is_part_of',4),(299,'is_part_of',4),(300,'is_part_of',4),(301,'is_part_of',4),(302,'is_part_of',4),(303,'is_part_of',4),(304,'is_part_of',4),(305,'is_part_of',4),(306,'is_part_of',4),(307,'is_part_of',4),(308,'is_part_of',4),(359,'is_part_of',4),(360,'is_part_of',4),(339,'is_part_of',5),(1,'is_part_of',6),(2,'is_part_of',6),(3,'is_part_of',6),(4,'is_part_of',6),(5,'is_part_of',6),(161,'same_as',8),(162,'same_as',9),(163,'same_as',10),(164,'same_as',11),(165,'same_as',12),(166,'same_as',13),(168,'same_as',15),(169,'same_as',17),(170,'same_as',21),(166,'same_as',23),(181,'same_as',24),(182,'same_as',25),(183,'same_as',26),(184,'same_as',27),(187,'same_as',29),(188,'same_as',30),(191,'same_as',33),(192,'same_as',34),(193,'same_as',35),(194,'same_as',36),(195,'same_as',37),(196,'same_as',38),(197,'same_as',39),(198,'same_as',40),(199,'same_as',41),(200,'same_as',42),(201,'same_as',44),(202,'same_as',45),(203,'same_as',46),(204,'same_as',47),(205,'same_as',48),(206,'same_as',49),(207,'same_as',52),(208,'same_as',53),(209,'same_as',56),(210,'same_as',57),(211,'same_as',58),(212,'same_as',59),(213,'same_as',60),(214,'same_as',61),(215,'same_as',69),(216,'same_as',70),(217,'same_as',71),(218,'same_as',72),(219,'same_as',74),(220,'same_as',76),(221,'same_as',77),(222,'same_as',78),(223,'same_as',79),(224,'same_as',80),(225,'same_as',81),(226,'same_as',82),(227,'same_as',83),(228,'same_as',84),(229,'same_as',85),(230,'same_as',86),(231,'same_as',87),(232,'same_as',88),(234,'same_as',89),(235,'same_as',91),(236,'same_as',92),(237,'same_as',93),(238,'same_as',97),(239,'same_as',99),(240,'same_as',101),(241,'same_as',102),(242,'same_as',103),(243,'same_as',104),(244,'same_as',105),(245,'same_as',106),(246,'same_as',107),(247,'same_as',108),(248,'same_as',109),(249,'same_as',110),(250,'same_as',111),(251,'same_as',112),(252,'same_as',122),(253,'same_as',123),(254,'same_as',124),(256,'same_as',126),(257,'same_as',127),(258,'same_as',128),(259,'same_as',130),(260,'same_as',131),(255,'same_as',132),(135,'same_as',134),(136,'same_as',134),(261,'same_as',134),(134,'same_as',135),(136,'same_as',135),(261,'same_as',135),(134,'same_as',136),(135,'same_as',136),(261,'same_as',136),(262,'same_as',138),(265,'same_as',140),(266,'same_as',141),(267,'same_as',142),(268,'same_as',143),(269,'same_as',144),(8,'same_as',161),(9,'same_as',162),(10,'same_as',163),(11,'same_as',164),(12,'same_as',165),(13,'same_as',166),(23,'same_as',166),(15,'same_as',168),(17,'same_as',169),(21,'same_as',170),(24,'same_as',181),(25,'same_as',182),(26,'same_as',183),(27,'same_as',184),(29,'same_as',187),(30,'same_as',188),(33,'same_as',191),(34,'same_as',192),(35,'same_as',193),(36,'same_as',194),(37,'same_as',195),(38,'same_as',196),(39,'same_as',197),(40,'same_as',198),(41,'same_as',199),(42,'same_as',200),(44,'same_as',201),(45,'same_as',202),(46,'same_as',203),(47,'same_as',204),(48,'same_as',205),(49,'same_as',206),(52,'same_as',207),(53,'same_as',208),(56,'same_as',209),(57,'same_as',210),(58,'same_as',211),(59,'same_as',212),(60,'same_as',213),(61,'same_as',214),(69,'same_as',215),(70,'same_as',216),(71,'same_as',217),(72,'same_as',218),(74,'same_as',219),(76,'same_as',220),(77,'same_as',221),(78,'same_as',222),(79,'same_as',223),(80,'same_as',224),(81,'same_as',225),(82,'same_as',226),(83,'same_as',227),(84,'same_as',228),(85,'same_as',229),(86,'same_as',230),(87,'same_as',231),(88,'same_as',232),(89,'same_as',234),(91,'same_as',235),(92,'same_as',236),(93,'same_as',237),(97,'same_as',238),(99,'same_as',239),(101,'same_as',240),(102,'same_as',241),(103,'same_as',242),(104,'same_as',243),(105,'same_as',244),(106,'same_as',245),(107,'same_as',246),(108,'same_as',247),(109,'same_as',248),(110,'same_as',249),(111,'same_as',250),(112,'same_as',251),(122,'same_as',252),(123,'same_as',253),(124,'same_as',254),(132,'same_as',255),(126,'same_as',256),(127,'same_as',257),(128,'same_as',258),(130,'same_as',259),(131,'same_as',260),(134,'same_as',261),(135,'same_as',261),(136,'same_as',261),(138,'same_as',262),(140,'same_as',265),(141,'same_as',266),(142,'same_as',267),(143,'same_as',268),(144,'same_as',269),(9,'is_part_of',270),(162,'is_part_of',270),(11,'is_part_of',271),(164,'is_part_of',271),(10,'is_part_of',272),(163,'is_part_of',272),(21,'is_part_of',273),(170,'is_part_of',273),(13,'is_part_of',274),(23,'is_part_of',274),(28,'is_part_of',274),(166,'is_part_of',274),(18,'is_part_of',278),(169,'is_part_of',279),(169,'is_part_of',280),(149,'is_part_of',282),(122,'is_part_of',284),(123,'is_part_of',284),(124,'is_part_of',284),(125,'is_part_of',284),(126,'is_part_of',284),(127,'is_part_of',284),(128,'is_part_of',284),(143,'is_part_of',284),(252,'is_part_of',284),(253,'is_part_of',284),(254,'is_part_of',284),(255,'is_part_of',284),(256,'is_part_of',284),(257,'is_part_of',284),(258,'is_part_of',284),(268,'is_part_of',284),(113,'is_part_of',285),(129,'is_part_of',285),(130,'is_part_of',285),(131,'is_part_of',285),(132,'is_part_of',285),(133,'is_part_of',285),(134,'is_part_of',285),(135,'is_part_of',285),(136,'is_part_of',285),(137,'is_part_of',285),(139,'is_part_of',285),(141,'is_part_of',285),(142,'is_part_of',285),(144,'is_part_of',285),(146,'is_part_of',285),(147,'is_part_of',285),(239,'is_part_of',285),(259,'is_part_of',285),(260,'is_part_of',285),(261,'is_part_of',285),(263,'is_part_of',285),(264,'is_part_of',285),(266,'is_part_of',285),(267,'is_part_of',285),(269,'is_part_of',285),(138,'is_part_of',286),(262,'is_part_of',286),(145,'is_part_of',291),(92,'is_part_of',296),(236,'is_part_of',296),(91,'is_part_of',297),(235,'is_part_of',297),(93,'is_part_of',298),(237,'is_part_of',298),(101,'is_part_of',299),(240,'is_part_of',299),(94,'is_part_of',300),(100,'is_part_of',300),(102,'is_part_of',300),(105,'is_part_of',300),(119,'is_part_of',300),(120,'is_part_of',300),(241,'is_part_of',300),(244,'is_part_of',300),(99,'is_part_of',301),(239,'is_part_of',301),(106,'is_part_of',302),(107,'is_part_of',302),(140,'is_part_of',302),(245,'is_part_of',302),(246,'is_part_of',302),(265,'is_part_of',302),(97,'is_part_of',303),(112,'is_part_of',303),(238,'is_part_of',303),(251,'is_part_of',303),(98,'is_part_of',304),(111,'is_part_of',304),(116,'is_part_of',304),(250,'is_part_of',304),(115,'is_part_of',305),(118,'is_part_of',305),(108,'is_part_of',306),(109,'is_part_of',306),(110,'is_part_of',306),(114,'is_part_of',306),(247,'is_part_of',306),(248,'is_part_of',306),(249,'is_part_of',306),(95,'is_part_of',307),(96,'is_part_of',308),(31,'is_part_of',309),(62,'is_part_of',309),(64,'is_part_of',309),(189,'is_part_of',309),(29,'is_part_of',310),(49,'is_part_of',310),(52,'is_part_of',310),(54,'is_part_of',310),(103,'is_part_of',310),(187,'is_part_of',310),(206,'is_part_of',310),(207,'is_part_of',310),(242,'is_part_of',310),(30,'is_part_of',311),(188,'is_part_of',311),(32,'is_part_of',312),(59,'is_part_of',312),(60,'is_part_of',312),(63,'is_part_of',312),(72,'is_part_of',312),(190,'is_part_of',312),(212,'is_part_of',312),(213,'is_part_of',312),(218,'is_part_of',312),(61,'is_part_of',313),(214,'is_part_of',313),(34,'is_part_of',314),(58,'is_part_of',314),(192,'is_part_of',314),(211,'is_part_of',314),(33,'is_part_of',317),(65,'is_part_of',317),(66,'is_part_of',317),(191,'is_part_of',317),(74,'is_part_of',318),(76,'is_part_of',318),(77,'is_part_of',318),(78,'is_part_of',318),(79,'is_part_of',318),(80,'is_part_of',318),(81,'is_part_of',318),(82,'is_part_of',318),(83,'is_part_of',318),(84,'is_part_of',318),(85,'is_part_of',318),(86,'is_part_of',318),(87,'is_part_of',318),(88,'is_part_of',318),(89,'is_part_of',318),(90,'is_part_of',318),(219,'is_part_of',318),(220,'is_part_of',318),(221,'is_part_of',318),(222,'is_part_of',318),(223,'is_part_of',318),(224,'is_part_of',318),(225,'is_part_of',318),(226,'is_part_of',318),(227,'is_part_of',318),(228,'is_part_of',318),(229,'is_part_of',318),(230,'is_part_of',318),(231,'is_part_of',318),(232,'is_part_of',318),(233,'is_part_of',318),(234,'is_part_of',318),(69,'is_part_of',320),(71,'is_part_of',320),(215,'is_part_of',320),(217,'is_part_of',320),(35,'is_part_of',322),(36,'is_part_of',322),(37,'is_part_of',322),(38,'is_part_of',322),(39,'is_part_of',322),(40,'is_part_of',322),(41,'is_part_of',322),(48,'is_part_of',322),(51,'is_part_of',322),(70,'is_part_of',322),(71,'is_part_of',322),(193,'is_part_of',322),(194,'is_part_of',322),(195,'is_part_of',322),(196,'is_part_of',322),(197,'is_part_of',322),(198,'is_part_of',322),(199,'is_part_of',322),(205,'is_part_of',322),(216,'is_part_of',322),(217,'is_part_of',322),(42,'is_part_of',324),(43,'is_part_of',324),(200,'is_part_of',324),(44,'is_part_of',325),(45,'is_part_of',325),(50,'is_part_of',325),(55,'is_part_of',325),(201,'is_part_of',325),(202,'is_part_of',325),(67,'is_part_of',326),(68,'is_part_of',326),(73,'is_part_of',327),(121,'is_part_of',328),(56,'is_part_of',329),(57,'is_part_of',329),(209,'is_part_of',329),(210,'is_part_of',329),(150,'is_part_of',336),(151,'is_part_of',336),(152,'is_part_of',336),(153,'is_part_of',339),(154,'is_part_of',339),(155,'is_part_of',339),(156,'is_part_of',339),(157,'is_part_of',339),(158,'is_part_of',339),(8,'is_part_of',340),(161,'is_part_of',340),(7,'is_part_of',341),(12,'is_part_of',341),(27,'is_part_of',341),(165,'is_part_of',341),(184,'is_part_of',341),(14,'is_part_of',342),(15,'is_part_of',343),(168,'is_part_of',343),(16,'is_part_of',344),(17,'is_part_of',345),(156,'is_part_of',345),(169,'is_part_of',345),(19,'is_part_of',346),(20,'is_part_of',347),(22,'is_part_of',348),(173,'is_part_of',349),(174,'is_part_of',349),(175,'is_part_of',349),(176,'is_part_of',349),(177,'is_part_of',350),(178,'is_part_of',351),(179,'is_part_of',351),(180,'is_part_of',352),(185,'is_part_of',352),(24,'is_part_of',353),(25,'is_part_of',353),(181,'is_part_of',353),(182,'is_part_of',353),(26,'is_part_of',354),(183,'is_part_of',354),(186,'is_part_of',355),(172,'is_part_of',356),(47,'is_part_of',357),(48,'is_part_of',357),(53,'is_part_of',357),(55,'is_part_of',357),(204,'is_part_of',357),(205,'is_part_of',357),(208,'is_part_of',357),(46,'is_part_of',358),(203,'is_part_of',358),(103,'is_part_of',359),(242,'is_part_of',359),(104,'is_part_of',360),(243,'is_part_of',360);
/*!40000 ALTER TABLE `dictionary` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-05-03 14:17:10
