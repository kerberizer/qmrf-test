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
INSERT INTO `dictionary` VALUES (7,'is_a',1),(8,'is_a',1),(9,'is_a',1),(10,'is_a',1),(11,'is_a',1),(12,'is_a',1),(13,'is_a',1),(14,'is_a',1),(15,'is_a',1),(16,'is_a',1),(17,'is_a',1),(18,'is_a',1),(19,'is_a',1),(20,'is_a',1),(21,'is_a',1),(22,'is_a',1),(23,'is_a',1),(24,'is_a',1),(25,'is_a',1),(26,'is_a',1),(27,'is_a',1),(28,'is_a',1),(161,'is_a',1),(162,'is_a',1),(163,'is_a',1),(164,'is_a',1),(165,'is_a',1),(166,'is_a',1),(168,'is_a',1),(169,'is_a',1),(170,'is_a',1),(172,'is_a',1),(173,'is_a',1),(174,'is_a',1),(175,'is_a',1),(176,'is_a',1),(177,'is_a',1),(178,'is_a',1),(179,'is_a',1),(180,'is_a',1),(181,'is_a',1),(182,'is_a',1),(183,'is_a',1),(185,'is_a',1),(186,'is_a',1),(29,'is_a',2),(30,'is_a',2),(31,'is_a',2),(32,'is_a',2),(33,'is_a',2),(34,'is_a',2),(35,'is_a',2),(36,'is_a',2),(37,'is_a',2),(38,'is_a',2),(39,'is_a',2),(40,'is_a',2),(41,'is_a',2),(42,'is_a',2),(43,'is_a',2),(44,'is_a',2),(45,'is_a',2),(46,'is_a',2),(47,'is_a',2),(48,'is_a',2),(49,'is_a',2),(50,'is_a',2),(51,'is_a',2),(52,'is_a',2),(53,'is_a',2),(54,'is_a',2),(55,'is_a',2),(56,'is_a',2),(57,'is_a',2),(58,'is_a',2),(59,'is_a',2),(60,'is_a',2),(61,'is_a',2),(62,'is_a',2),(63,'is_a',2),(64,'is_a',2),(65,'is_a',2),(66,'is_a',2),(67,'is_a',2),(68,'is_a',2),(69,'is_a',2),(70,'is_a',2),(71,'is_a',2),(72,'is_a',2),(73,'is_a',2),(187,'is_a',2),(188,'is_a',2),(189,'is_a',2),(190,'is_a',2),(191,'is_a',2),(192,'is_a',2),(193,'is_a',2),(194,'is_a',2),(195,'is_a',2),(196,'is_a',2),(197,'is_a',2),(198,'is_a',2),(199,'is_a',2),(200,'is_a',2),(201,'is_a',2),(202,'is_a',2),(203,'is_a',2),(204,'is_a',2),(205,'is_a',2),(206,'is_a',2),(207,'is_a',2),(208,'is_a',2),(209,'is_a',2),(210,'is_a',2),(211,'is_a',2),(212,'is_a',2),(213,'is_a',2),(214,'is_a',2),(215,'is_a',2),(216,'is_a',2),(217,'is_a',2),(218,'is_a',2),(74,'is_a',3),(76,'is_a',3),(77,'is_a',3),(78,'is_a',3),(79,'is_a',3),(80,'is_a',3),(81,'is_a',3),(82,'is_a',3),(83,'is_a',3),(84,'is_a',3),(85,'is_a',3),(86,'is_a',3),(87,'is_a',3),(88,'is_a',3),(89,'is_a',3),(90,'is_a',3),(219,'is_a',3),(220,'is_a',3),(221,'is_a',3),(222,'is_a',3),(223,'is_a',3),(224,'is_a',3),(225,'is_a',3),(226,'is_a',3),(227,'is_a',3),(228,'is_a',3),(229,'is_a',3),(230,'is_a',3),(231,'is_a',3),(232,'is_a',3),(233,'is_a',3),(234,'is_a',3),(91,'is_a',4),(92,'is_a',4),(93,'is_a',4),(94,'is_a',4),(95,'is_a',4),(96,'is_a',4),(97,'is_a',4),(98,'is_a',4),(99,'is_a',4),(100,'is_a',4),(101,'is_a',4),(102,'is_a',4),(103,'is_a',4),(104,'is_a',4),(105,'is_a',4),(106,'is_a',4),(107,'is_a',4),(108,'is_a',4),(109,'is_a',4),(110,'is_a',4),(111,'is_a',4),(112,'is_a',4),(113,'is_a',4),(114,'is_a',4),(115,'is_a',4),(116,'is_a',4),(117,'is_a',4),(118,'is_a',4),(119,'is_a',4),(120,'is_a',4),(121,'is_a',4),(122,'is_a',4),(123,'is_a',4),(124,'is_a',4),(125,'is_a',4),(126,'is_a',4),(127,'is_a',4),(128,'is_a',4),(129,'is_a',4),(130,'is_a',4),(131,'is_a',4),(132,'is_a',4),(133,'is_a',4),(134,'is_a',4),(135,'is_a',4),(136,'is_a',4),(137,'is_a',4),(138,'is_a',4),(139,'is_a',4),(140,'is_a',4),(141,'is_a',4),(142,'is_a',4),(143,'is_a',4),(144,'is_a',4),(145,'is_a',4),(146,'is_a',4),(147,'is_a',4),(148,'is_a',4),(149,'is_a',4),(235,'is_a',4),(236,'is_a',4),(237,'is_a',4),(238,'is_a',4),(239,'is_a',4),(240,'is_a',4),(241,'is_a',4),(242,'is_a',4),(243,'is_a',4),(244,'is_a',4),(245,'is_a',4),(246,'is_a',4),(247,'is_a',4),(248,'is_a',4),(249,'is_a',4),(250,'is_a',4),(251,'is_a',4),(252,'is_a',4),(253,'is_a',4),(254,'is_a',4),(255,'is_a',4),(256,'is_a',4),(257,'is_a',4),(258,'is_a',4),(259,'is_a',4),(260,'is_a',4),(261,'is_a',4),(262,'is_a',4),(263,'is_a',4),(264,'is_a',4),(265,'is_a',4),(266,'is_a',4),(267,'is_a',4),(268,'is_a',4),(269,'is_a',4),(150,'is_a',5),(151,'is_a',5),(152,'is_a',5),(153,'is_a',5),(154,'is_a',5),(155,'is_a',5),(156,'is_a',5),(157,'is_a',5),(158,'is_a',5),(1,'is_part_of',6),(2,'is_part_of',6),(3,'is_part_of',6),(4,'is_part_of',6),(5,'is_part_of',6),(161,'same_as',8),(162,'same_as',9),(163,'same_as',10),(164,'same_as',11),(165,'same_as',12),(166,'same_as',13),(168,'same_as',15),(169,'same_as',17),(170,'same_as',21),(166,'same_as',23),(181,'same_as',24),(182,'same_as',25),(183,'same_as',26),(184,'same_as',27),(187,'same_as',29),(188,'same_as',30),(191,'same_as',33),(192,'same_as',34),(193,'same_as',35),(194,'same_as',36),(195,'same_as',37),(196,'same_as',38),(197,'same_as',39),(198,'same_as',40),(199,'same_as',41),(200,'same_as',42),(201,'same_as',44),(202,'same_as',45),(203,'same_as',46),(204,'same_as',47),(205,'same_as',48),(206,'same_as',49),(207,'same_as',52),(208,'same_as',53),(209,'same_as',56),(210,'same_as',57),(211,'same_as',58),(212,'same_as',59),(213,'same_as',60),(214,'same_as',61),(215,'same_as',69),(216,'same_as',70),(217,'same_as',71),(218,'same_as',72),(219,'same_as',74),(220,'same_as',76),(221,'same_as',77),(222,'same_as',78),(223,'same_as',79),(224,'same_as',80),(225,'same_as',81),(226,'same_as',82),(227,'same_as',83),(228,'same_as',84),(229,'same_as',85),(230,'same_as',86),(231,'same_as',87),(232,'same_as',88),(234,'same_as',89),(235,'same_as',91),(236,'same_as',92),(237,'same_as',93),(238,'same_as',97),(239,'same_as',99),(240,'same_as',101),(241,'same_as',102),(242,'same_as',103),(243,'same_as',104),(244,'same_as',105),(245,'same_as',106),(246,'same_as',107),(247,'same_as',108),(248,'same_as',109),(249,'same_as',110),(250,'same_as',111),(251,'same_as',112),(252,'same_as',122),(253,'same_as',123),(254,'same_as',124),(256,'same_as',126),(257,'same_as',127),(258,'same_as',128),(259,'same_as',130),(260,'same_as',131),(255,'same_as',132),(135,'same_as',134),(136,'same_as',134),(261,'same_as',134),(134,'same_as',135),(136,'same_as',135),(261,'same_as',135),(134,'same_as',136),(135,'same_as',136),(261,'same_as',136),(262,'same_as',138),(265,'same_as',140),(266,'same_as',141),(267,'same_as',142),(268,'same_as',143),(269,'same_as',144),(8,'same_as',161),(9,'same_as',162),(10,'same_as',163),(11,'same_as',164),(12,'same_as',165),(13,'same_as',166),(23,'same_as',166),(15,'same_as',168),(17,'same_as',169),(21,'same_as',170),(24,'same_as',181),(25,'same_as',182),(26,'same_as',183),(27,'same_as',184),(29,'same_as',187),(30,'same_as',188),(33,'same_as',191),(34,'same_as',192),(35,'same_as',193),(36,'same_as',194),(37,'same_as',195),(38,'same_as',196),(39,'same_as',197),(40,'same_as',198),(41,'same_as',199),(42,'same_as',200),(44,'same_as',201),(45,'same_as',202),(46,'same_as',203),(47,'same_as',204),(48,'same_as',205),(49,'same_as',206),(52,'same_as',207),(53,'same_as',208),(56,'same_as',209),(57,'same_as',210),(58,'same_as',211),(59,'same_as',212),(60,'same_as',213),(61,'same_as',214),(69,'same_as',215),(70,'same_as',216),(71,'same_as',217),(72,'same_as',218),(74,'same_as',219),(76,'same_as',220),(77,'same_as',221),(78,'same_as',222),(79,'same_as',223),(80,'same_as',224),(81,'same_as',225),(82,'same_as',226),(83,'same_as',227),(84,'same_as',228),(85,'same_as',229),(86,'same_as',230),(87,'same_as',231),(88,'same_as',232),(89,'same_as',234),(91,'same_as',235),(92,'same_as',236),(93,'same_as',237),(97,'same_as',238),(99,'same_as',239),(101,'same_as',240),(102,'same_as',241),(103,'same_as',242),(104,'same_as',243),(105,'same_as',244),(106,'same_as',245),(107,'same_as',246),(108,'same_as',247),(109,'same_as',248),(110,'same_as',249),(111,'same_as',250),(112,'same_as',251),(122,'same_as',252),(123,'same_as',253),(124,'same_as',254),(132,'same_as',255),(126,'same_as',256),(127,'same_as',257),(128,'same_as',258),(130,'same_as',259),(131,'same_as',260),(134,'same_as',261),(135,'same_as',261),(136,'same_as',261),(138,'same_as',262),(140,'same_as',265),(141,'same_as',266),(142,'same_as',267),(143,'same_as',268),(144,'same_as',269);
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

-- Dump completed on 2012-04-30 20:42:48
