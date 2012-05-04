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
-- Table structure for table `template`
--

DROP TABLE IF EXISTS `template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `template` (
  `idtemplate` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `code` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `uri` text COLLATE utf8_bin,
  PRIMARY KEY (`idtemplate`),
  UNIQUE KEY `template_list_index4157` (`name`,`code`) USING BTREE,
  KEY `Index_3` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=362 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `template`
--

LOCK TABLES `template` WRITE;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;
INSERT INTO `template` VALUES (1,'Physical Chemical Properties','QMRF 1.',NULL),(2,'Human Health Effects','QMRF 4.',NULL),(3,'Genetic Toxicology','QMRF 7.',NULL),(4,'Environmental fate parameters','QMRF 2.',NULL),(5,'Other','QMRF 6.',NULL),(6,NULL,NULL,NULL),(7,'UV-VIS Adsorption Spectra','OECD 101',NULL),(8,'Melting Point/Melting Range','OECD 102',NULL),(9,'Boiling Point','OECD 103',NULL),(10,'Vapour Pressure','OECD 104',NULL),(11,'Water Solubility','OECD 105',NULL),(12,'Adsorption/Desorption Using a Batch Equilibrium Method','OECD 106',NULL),(13,'Partition Coefficient (n-octanol/water); Shake Flask Method','OECD 107',NULL),(14,'Complex Formation Ability in Water','OECD 108',NULL),(15,'Density of Liquids and Solids','OECD 109',NULL),(16,'Particle Size Distribution/Fibre Length and Diameter Distributions','OECD 110',NULL),(17,'Hydrolysis','OECD 111',NULL),(18,'Dissociation Constants in Water','OECD 112',NULL),(19,'Screening Test for Thermal Stability and Stability in Air','OECD 113',NULL),(20,'Viscosity of Liquids','OECD 114',NULL),(21,'Surface Tension of Aqueous Solutions','OECD 115',NULL),(22,'Fat Solubility of Solid and Liquid Substances','OECD 116',NULL),(23,'Partition Coefficient (n-octanol/water) HPLC Method','OECD 117',NULL),(24,'Det Number Average M W and M W Distribution Polymers by Gel Perm Chromatogr','OECD 118',NULL),(25,'Det Low M W Content Polymers by GPC','OECD 119',NULL),(26,'Solution/Extraction Behaviour of Polymers in Water','OECD 120',NULL),(27,'Estimation of the Adsorption Coefficient (Koc) on Soil and on Sewage Sludge Using High Performance Liquid Chromatography (HPLC)','OECD 121',NULL),(28,'Partition Coefficient (n-Octanol/Water): Slow-Stirring Method','OECD 123',NULL),(29,'Acute Oral Toxicity DELETED','OECD 401',NULL),(30,'Acute Dermal Toxicity','OECD 402',NULL),(31,'Acute Inhalation Toxicity','OECD 403',NULL),(32,'Acute Dermal Irritation/Corrosion','OECD 404',NULL),(33,'Acute Eye Irritation/Corrosion','OECD 405',NULL),(34,'Skin Sensitisation','OECD 406',NULL),(35,'Repeated Dose Oral Toxicity-Rodent 28/14-Days','OECD 407',NULL),(36,'Repeated Dose 90-day Oral Toxicity Study in Rodents','OECD 408',NULL),(37,'Repeated Dose 90-day Oral Toxicity Study in Non-Rodents','OECD 409',NULL),(38,'Repeated Dose Dermal Toxicity: 21/28 Day','OECD 410',NULL),(39,'Subchronic Dermal Toxicity: 90-Day','OECD 411',NULL),(40,'Repeated Dose Inhalation Toxicity: 28/14-Day','OECD 412',NULL),(41,'Subchronic Inhalation Toxicity: 90-Day','OECD 413',NULL),(42,'Teratogenicity','OECD 414',NULL),(43,'Prenatal Development Study','OECD ',NULL),(44,'One-Generation Reproduction Toxicity','OECD 415','http://www.oecd.org/dataoecd/18/12/1948458.pdf'),(45,'Two-Generation Reproduction Toxicity','OECD 416',NULL),(46,'Toxicokinetics','OECD 417','http://www.oecd.org/dataoecd/14/8/41690691.pdf'),(47,'Acute Delayed Neurotoxicity of Organophosphorus Substances','OECD 418',NULL),(48,'Subchronic Delayed Neurotoxicity of Organophosphorus Substances: 28-Day','OECD 419',NULL),(49,'Acute Oral Toxicity-Fixed Dose Method','OECD 420',NULL),(50,'Reproduction/Developmental Toxicity Screening Test','OECD 421',NULL),(51,'Combined Repeated Dose Toxicity Study with the Reproduction/Developmental Toxicity Screening Test','OECD 422',NULL),(52,'Acute Oral Toxicity - Acute Toxic Class Method','OECD 423',NULL),(53,'Neurotoxicity Study in Rodents','OECD 424',NULL),(54,'Acute Oral Toxicity: Up-and-Down Procedure','OECD 425',NULL),(55,'Developmental Neurotoxicity Study','OECD 426',NULL),(56,'In vivo Skin absorption','OECD 427',NULL),(57,'In vitro Skin Absorption','OECD 428',NULL),(58,'LLNA','OECD 429',NULL),(59,'In vitro Skin Corrosion (TER)','OECD 430',NULL),(60,'In vitro Skin Corrosion (HSM)','OECD 431',NULL),(61,'In Vitro 3T3 NRU Phototoxicity Test','OECD 432',NULL),(62,'DRAFT Acute Inhalation Toxicity FCP','OECD 433',NULL),(63,'In Vitro Membrane Barrier Test Method for Skin Corrosion','OECD 435',NULL),(64,'DRAFT Acute Inhalation Toxicity - ATC Method','OECD 436',NULL),(65,'The Bovine Corneal Opacity and Permeability (BCOP) Test Mehod for Identifying Eye Corrosives and Severe Irritants','OECD 437',NULL),(66,'The Isolated Chicken Eye (ICE) Test Method for Identifying Ocular Corrosives and Severe Irritants','OECD 438',NULL),(67,'Uterotrophic Bioassay in Rodents: a short-term screening test for oestrogenic properties','OECD 440',NULL),(68,'The Hershberger Bioassay in rats A Short-term Screening Assay for (Anti) Androgenic Properties','OECD 441',NULL),(69,'Carcinogenicity Studies','OECD 451',NULL),(70,'Chronic Toxicity Studies','OECD 452',NULL),(71,'Combined Chronic Toxicity/Carcinogenicity Studies','OECD 453',NULL),(72,'In vitro Skin Irritation Reconstructed Human Epidermis (RhE) Test Method','OECD 4XX',NULL),(73,'Stably Transfected  Human Estrogen Receptor ?-Transcriptional Activation Assay for Detection of Estrogenic Agonist Activity of Chemicals','OECD 455',NULL),(74,'Bacterial Reverse Mutation Test','OECD 471',NULL),(76,'In Vitro Mammalian Chromosome Aberration Test','OECD 473',NULL),(77,'Mamm Erythrocyte Micronucleus Test','OECD 474',NULL),(78,'Mammalian Bone Marrow Chromosome Aberration Test','OECD 475',NULL),(79,'In Vitro Mammalian Cell Gene Mutation Test','OECD 476',NULL),(80,'Sex-Linked recessive Lethal Test in Drosophila Melanogaster','OECD 477',NULL),(81,'Rodent Dominant Lethal test','OECD 478',NULL),(82,'In Vitro Sister Chromatid Exchange Assay in Mammalian Cells','OECD 479',NULL),(83,'Saccharomyces Cerevisiae, Gene Mutation Assay','OECD 480',NULL),(84,'Saccharomyces Cerevisiae, Mitotic Recombination Assay','OECD 481',NULL),(85,'DNA Damage and Repair, Unscheduled DNA Synthesis in Mammalian Cells in Vitro','OECD 482',NULL),(86,'Mammalian Spermatogonial Chromosome Aberration Test','OECD 483',NULL),(87,'Mouse Spot Test','OECD 484',NULL),(88,'Mouse Heritable Translocation Assay','OECD 485',NULL),(89,'Unscheduled DNA Syntesis (UDS) Test with Mammalian Liver Cells In Vivo','OECD 486',NULL),(90,'In vitro Micronucleous Test','OECD 487',NULL),(91,'Alga Growth Inhibition Test','OECD 201',NULL),(92,'Daphnia sp Acute Immobilisation Test','OECD 202',NULL),(93,'Fish, Acute Toxicity Test','OECD 203',NULL),(94,'Fish, Prolonged Toxicity Test: 14-Day Study','OECD 204',NULL),(95,'Avian Dietary Toxicity Test','OECD 205',NULL),(96,'Avian Reproduction Test','OECD 206',NULL),(97,'Earthworm, Acute Toxicity Test','OECD 207',NULL),(98,'Terrestrial Plant Test: Seedling Emergence and Seedling Growth Test','OECD 208',NULL),(99,'Activated Sludge, Respiration Inhibition Test','OECD 209',NULL),(100,'Fish, Early-Life Stage Toxicity Test','OECD 210',NULL),(101,'Daphnia magna Reproduction Test','OECD 211',NULL),(102,'Fish, Short-term Toxicity Test on Embryo and Sac-fry Stages','OECD 212',NULL),(103,'Honeybees-Acute Oral Toxicity','OECD 213',NULL),(104,'Honeybees-Acute Contact Toxicity','OECD 214',NULL),(105,'Fish, Juvenile Growth Test','OECD 215',NULL),(106,'Soil Microorganisms: Nitrogen Transformation Test','OECD 216',NULL),(107,'Soil Microorganisms: Carbon Transformation Test','OECD 217',NULL),(108,'Chironomidae spiked sediment','OECD 218',NULL),(109,'Chironomidae spiked water','OECD 219',NULL),(110,'Enchytraeidae Reproduction Test','OECD 220',NULL),(111,'Lemna growth inhibition test','OECD 221',NULL),(112,'Earthworm reproduction','OECD 222',NULL),(113,'Determination of the Acvtivity of anaerobic bacteria - reduction of gas production from anaerobic sewage sludge','OECD 224',NULL),(114,'Sediment-Water Lumbriculus Toxicity Test Using Spiked Sediment','OECD 225',NULL),(115,'Predatory Mite(Hypoaspis (Geolaelaps) Aculeifer Reproduction Test in Soil','OECD 226',NULL),(116,'Terrestrial Plant Test: Vegetative Vigour Test','OECD 227',NULL),(117,'Determination of Developmental Toxicity of a Test Chemical to Dipteran Dung Flies','OECD 228',NULL),(118,'Collembolan Reproduction Test in Soils','OECD 229',NULL),(119,'Fish Short Term Reproduction Assay','OECD 2XX',NULL),(120,'The 21-day Fish Assay: A short-term screening for oestrogenic and androgenic activity and aromatase inhibition','OECD 2XX',NULL),(121,'The Amphibian Metamorphosis Assay','OECD 2XX',NULL),(122,'Ready Biodegradability','OECD 301',NULL),(123,'DOC Die-Away Test','OECD 301A',NULL),(124,'CO2 Evolution Test','OECD 301B',NULL),(125,'Modified MITI (I) Test','OECD 301C',NULL),(126,'Closed Bottle Test','OECD 301D',NULL),(127,'Modified OECD Screening Test','OECD 301E',NULL),(128,'Monometric Respiratory Test','OECD 301F',NULL),(129,'Inherent Biodegradability:','OECD 302',NULL),(130,'Modified SCAS Test','OECD 302A',NULL),(131,'Zahn-Wellens/EMPA Test','OECD 302B',NULL),(132,'Modified MITI (II) Test','OECD 302C',NULL),(133,'Inherent Biodegradability - Concawe Test','OECD 302D',NULL),(134,'Simulation Test - Aerobic Sewage Treatment','OECD 303',NULL),(135,'Activated Sludge Units','OECD 303A',NULL),(136,'Biofilms','OECD 303B',NULL),(137,'Inherent Biodegradability in Soil','OECD 304A',NULL),(138,'Bioconcentration: Flow-through Fish Test','OECD 305',NULL),(139,'Biodegradability in Seawater','OECD 306',NULL),(140,'Aerobic and anaerobic transformation in soil','OECD 307',NULL),(141,'Aerobic and anaerobic transformation in water-sediment systems','OECD 308',NULL),(142,'Aerobic Mineralisation in Surface Water-Simulation Biodegradation Test','OECD 309',NULL),(143,'Ready Biodegradability - CO2 in Sealed Vessels (Headspace Test)','OECD 310',NULL),(144,'Anaerobic Biodegradability of Organic Compounds in Digested Sludge - Method by Measurement of Gas Production','OECD 311',NULL),(145,'Leaching in soil columns','OECD 312',NULL),(146,'Estimation of Emissions from PreservativeTreated Wood to the Environment:  Laboratory Method for Wooden Commodities that are not covered and are in Contact with Fresh Water or Seawater','OECD 313',NULL),(147,'Simulation Tests to Assess the Biodegradability of Chemicals Discharged in Wastewater','OECD 314',NULL),(148,'Bioaccumulation in Sediment-dwelling Benthic Oligochaetes','OECD 315',NULL),(149,'Phototransformation in Surface water (direct photolysis)','OECD 316',NULL),(150,'Metabolism in Crops','OECD 501',NULL),(151,'Metabolism in Rotational Crops','OECD 502',NULL),(152,'Metabolism in Livestock','OECD 503',NULL),(153,'Residues in Rotational Crops (limited Field Studies)','OECD 504',NULL),(154,'Residues in Livestock','OECD 505',NULL),(155,'Stability of Pesticide Residues in Stored Commodities','OECD 506',NULL),(156,'Nature of Pesticide Residues in Processed Commodities  High Temperature Hydrolysis','OECD 507',NULL),(157,'Magnitude of Pesticide Residues in Processed Commodities','OECD 508',NULL),(158,'Crop Field Trial Test Guideline','OECD 5XX',NULL),(161,'Melting/Freezing Temperature','EC A.1.',NULL),(162,'Boiling Temperature','EC A.2.',NULL),(163,'Vapour Pressure','EC A.4',NULL),(164,'Water Solubility','EC A.6.',NULL),(165,'Adsorption/Desorption Using a Batch Equilibrium Method','EC C.18.',NULL),(166,'Partition Coefficient (EU method includes both shake flask and HPLC)','EC A.8',NULL),(168,'Relative Density','EC A.3.',NULL),(169,'Degradation: Abiotic Degradation: Hydrolysis as a Function of pH','EC C.7',NULL),(170,'Surface Tension','EC A.5.',NULL),(172,'Flash Point','EC A.9.',NULL),(173,'Flammability (Solids)','EC A.10.',NULL),(174,'Flammability (Gases)','EC A.11.',NULL),(175,'Flammability (Contact with Water)','EC A.12.',NULL),(176,'Pyrophoric Properties of Solids and Liquids','EC A.13.',NULL),(177,'Explosive Properties','EC A.14.',NULL),(178,'Auto-Ignition Temperature (Liquid and Gases)','EC A.15.',NULL),(179,'Relative Self-Ignition Temperature for Solids','EC A.16.',NULL),(180,'Oxidizing Properties (Solids)','EC A.17.',NULL),(181,'Number - Average Molecular Weight and Molecular Weight Distribution of Polymers','EC A.18',NULL),(182,'Low Molecular Weight Content of Polymers','EC A.19',NULL),(183,'Solution/Extraction Behaviour of Polymers in Water','EC A.20',NULL),(184,'Estimation of the Adsorption Coefficient (Koc) on Soil and on Sewage Sludge Using High Performance Liquid Chromatography (HPLC)','EC C.19',NULL),(185,'Oxidizing properties (liquids)','EC A.21',NULL),(186,'Length Weighted Geometric Mean Diametre of Fibres (LWGMD)','EC A.22',NULL),(187,'Acute Toxicity (Oral)','EC B.1.',NULL),(188,'Acute Toxicity (Dermal)','EC B.3.',NULL),(189,'Acute Toxicity (Inhalation)','EC B.2',NULL),(190,'Acute Toxicity (Skin Irritation)','EC B.4',NULL),(191,'Acute Toxicity (Eye Irritation)','EC B.5.',NULL),(192,'Skin Sensitisation','EC B.6.',NULL),(193,'Repeated Dose (28 Days) Toxicity (Oral)','EC B.7.',NULL),(194,'Repeated Dose 90-day Oral Toxicity Study in Rodents','EC B.26.',NULL),(195,'Repeated Dose 90-day Oral Toxicity Study in Non-Rodents','EC B.27.',NULL),(196,'Repeated Dose (28 Days) Toxicity (Dermal)','EC B.9.',NULL),(197,'Subchronic Dermal Toxicity Study: 90-Day Repeated Dermal Dose Study Using Rodent Species','EC B.28.',NULL),(198,'Repeated Dose (28 Days) Toxicity Inhalation','EC B.8.',NULL),(199,'Subchronic Inhalation Toxicity Study: 90-Day Repeated Inhalation Dose Study Using Rodent Species','EC B.29.',NULL),(200,'Teratogenicity Test-Rodent and Non-Rodent','EC B.31',NULL),(201,'One-Generation Reproduction Toxicity Test','EC B.34.',NULL),(202,'Two-Generation Reproduction Toxicity Test','EC B.35.',NULL),(203,'Toxicokinetics','EC B.36.',NULL),(204,'Delayed Neurotoxicity of Organophosphorus Substances following Acute Exposure','EC B.37.',NULL),(205,'Delayed Neurotoxicity of Organophosphorus Substances 28 Days Repeated Dose Study','EC B.38.',NULL),(206,'Acute Toxicity (Oral) Fixed Dose Method','EC B.1.bis.',NULL),(207,'Acute Toxicity (Oral) Acute Toxic Class Method','EC B.1.tris.',NULL),(208,'Neurotoxicity Study in Rodents','EC B.43.',NULL),(209,'Skin Absorption: In Vivo Method','EC B.44.',NULL),(210,'Skin Absorption: In Vitro Method','EC B.45.',NULL),(211,'Skin Sensitisation: Local Lymph Node Assay','EC B.42.',NULL),(212,'In Vitro Skin Corrosion: TER','EC B.40.',NULL),(213,'In Vitro Skin Corrosion: HSM','EC B.40 bis.',NULL),(214,'3T3 NRU Phototoxicity Test','EC B.41.',NULL),(215,'Carcinogenicity Test','EC B.32.',NULL),(216,'Chronic Toxicity Test','EC B.30.',NULL),(217,'Combined Chronic Toxicity/Carcinogenicity Test','EC B.33.',NULL),(218,'In vitro Skin Irritation Reconstructed Human Epidermis (RhE) Test Method','EC B.46.',NULL),(219,'Mutagenicity: Reverse Mutation Test Using Bacteria','EC B.13/14.',NULL),(220,'Mutagenicity - In Vitro Mammalian Chromosome Aberration Test','EC B.10.',NULL),(221,'Mutagenicity In Vivo Mamm Erythrocyte Micronucleus Test','EC B.12.',NULL),(222,'Mutagenicity In Vivo Mammalian Bone Marrow Chromosome Aberration Test','EC B.11.',NULL),(223,'In Vitro Mammalian Cell Gene Mutation Test','EC B.17.',NULL),(224,'Sex-Linked recessive Lethal Test in Drosophila Melanogaster','EC B.20.',NULL),(225,'Rodent Dominant Lethal test','EC B.22.',NULL),(226,'Sister Chromatid Exchange Assay In Vitro','EC B.19.',NULL),(227,'Gene Mutation Saccharomyces Cerevisiae','EC B.15.',NULL),(228,'Mitotic Recombination Saccharomyces Cerevisiae','EC B.16.',NULL),(229,'DNA Damage and Repair-Unscheduled DNA Synthesis - Mammalian Cells in Vitro','EC B.18.',NULL),(230,'Mammalian Spermatogonial Chromosome Aberration Test','EC B.23.',NULL),(231,'Mouse Spot Test','EC B.24.',NULL),(232,'Mouse Heritable Translocation','EC B.25.',NULL),(233,'In vitro Mammalian Cell Transformation Tests','EC B.21.',NULL),(234,'Unscheduled DNA Syntesis (UDS) Test with Mammalian Liver Cells In Vivo','EC B.39.',NULL),(235,'Freshwater Algae and Cyanobacteria, Growth Inhibition Test','EC C.3.',NULL),(236,'Daphnia sp Acute Immobilisation Test','EC C.2.',NULL),(237,'Acute Toxicity for Fish','EC C.1.',NULL),(238,'Toxicity for Earthworms: Artificial Soil Test','EC C.8.',NULL),(239,'Biodegradation: Activated Sludge Respiration Inhibition Test','EC C.11.',NULL),(240,'Daphnia magna Reproduction Test','EC C.20',NULL),(241,'Fish, Short-term Toxicity Test on Embryo and Sac-fry Stages','EC C.15.',NULL),(242,'Honeybees-Acute Oral Toxicity','EC C.16.',NULL),(243,'Honeybees-Acute Contact Toxicity','EC C.17.',NULL),(244,'Fish, Juvenile Growth Tes','EC C.14',NULL),(245,'Soil Microorganisms: Nitrogen Transformation Test','EC C.21',NULL),(246,'Soil Microorganisms: Carbon Transformation Test','EC C.22',NULL),(247,'Sediment-Water Chironomid Toxicity Test Using Spiked Sediment','EC ',NULL),(248,'Sediment-Water Chironomid Toxicity Test Using Spiked Water','EC ',NULL),(249,'Enchytraeid Reproduction Test','EC ',NULL),(250,'Lemna sp Growth Inhibition Test','EC C.26',NULL),(251,'Earthworm Reproduction Test (Eisenia fetida / Eisenia andrei)','EC ',NULL),(252,'Biodegradation: Determination of the \"ready\" Biodegradability','EC C.4.',NULL),(253,'Dissolved Organic Carbon (DOC) Die-Away','EC C.4-A',NULL),(254,'Carbon Dioxide (CO2) Evolution','EC C.4-C',NULL),(255,'MITI (Ministry of International Trade and Industry-Japan)','EC C.4-F',NULL),(256,'Closed Bottle','EC C.4-E',NULL),(257,'Modified OECD Screening Test','EC C.4-B',NULL),(258,'Monometric Respirometry','EC C.4-D',NULL),(259,'Biodegradation: Modified SCAS Test','EC C.12.',NULL),(260,'Biodegradation: Zahn-Wellens Test','EC C.9.',NULL),(261,'Biodegradation: Activated Sludge Simulation Tests','EC C.10.',NULL),(262,'Bioconcentration: Flow-through Fish Test','EC C.13',NULL),(263,'Degradation : Biochemical Oxygen Demand','EC C.5.',NULL),(264,'Degradation: Chemical Oxygen Demand','EC C.6.',NULL),(265,'Aerobic and Anaerobic Transformation in Soil','EC C.23',NULL),(266,'Aerobic and Anaerobic Transformation in Aquatic Sediment Systems','EC C.24',NULL),(267,'Aerobic Mineralisation in Surface Water-Simulation Biodegradation Test','EC ',NULL),(268,'Ready Biodegradability - CO2 in Sealed Vessels (Headspace Test)','EC ',NULL),(269,'Anaerobic Biodegradability of Organic Compounds in Digested Sludge: by Measurement of Gas Production','EC ',NULL),(270,'Boiling point','QMRF 1.2.',NULL),(271,'Water solubility','QMRF 1.3.',NULL),(272,'Vapour pressure','QMRF 1.4.',NULL),(273,'Surface tension','QMRF 1.5.',NULL),(274,'Octanol-water partition coefficient (Kow)','QMRF 1.6.',NULL),(275,'Octanol-water distribution coefficient (D)','QMRF 1.7.',NULL),(276,'Octanol-air partition coefficient (Koa)','QMRF 1.8.',NULL),(277,'Air- water partition coefficient (Henry\'s law constant, H)','QMRF 1.9.',NULL),(278,'Dissociation constant (pKa)','QMRF 1.10.',NULL),(279,'Persistence: Abiotic degradation in water. Hydrolysis','QMRF 2.1.a.',NULL),(280,'Persistence: Abiotic degradation in water. Oxidation','QMRF 2.1.b.',NULL),(281,'Persistence: Abiotic degradation in water. Other','QMRF 2.1.c.',NULL),(282,'Persistence: Abiotic degradation in air (Phototransformation). Direct photolysis','QMRF 2.2.a.',NULL),(283,'Persistence: Abiotic degradation in air (Phototransformation). Indirect photolysis (OH-radical reaction, ozone-radical reaction, other)','QMRF 2.2.b.',NULL),(284,'Persistence: Biodegradation. Ready/not ready biodegradability','QMRF 2.3.a.',NULL),(285,'Persistence: Biodegradation. Biodegradation time frame (primary, ultimate degradation)','QMRF 2.3.b.',NULL),(286,'Bioconcentration . BCF fish','QMRF 2.4.a.',NULL),(287,'Bioconcentration . BCF other organisms','QMRF 2.4.b.',NULL),(288,'Bioaccumulation. BAF fish','QMRF 2.5.a.',NULL),(289,'Bioaccumulation. BAF other organisms','QMRF 2.5.b.',NULL),(290,'Partition coefficient. Organic carbon-sorption partition coefficient (organic carbon; Koc)','QMRF 2.6.',NULL),(291,'Adsorption/Desorption in soil','QMRF 2.7.',NULL),(292,'Adsorption/Desorption in sediment','QMRF 2.8.',NULL),(293,'Vegetation-water partition coefficient','QMRF 2.9.',NULL),(294,'Vegetation-air partition coefficient','QMRF 2.10.',NULL),(295,'Vegetation-soil partition coefficient','QMRF 2.11.',NULL),(296,'Short-term toxicity to Daphnia (immobilisation)','QMRF 3.1.',NULL),(297,'Short-term toxicity to algae (inhibition of the exponential growth rate)','QMRF 3.2.',NULL),(298,'Acute toxicity to fish (lethality)','QMRF 3.3.',NULL),(299,'Long-term toxicity to Daphnia (lethality, inhibition of reproduction)','QMRF 3.4.',NULL),(300,'Long-term toxicity to fish (egg/sac fry, growth inhibition of juvenile fish, early life stage, full life cycle)','QMRF 3.5.',NULL),(301,'Microbial inhibition (activated sludge respiration inhibition, inhibition of nitrification, other)','QMRF 3.6.',NULL),(302,'Toxicity to soil microorganisms (inhibition of C-mineralisation, inhibition of N-mineralisation, other)','QMRF 3.7.',NULL),(303,'Toxicity to earthworms (survival, growth, reproduction)','QMRF 3.8.',NULL),(304,'Toxicity to plants (leaves, seed germination, root elongation)','QMRF 3.9.',NULL),(305,'Toxicity to soil invertebrates (survival, growth, reproduction)','QMRF 3.10.',NULL),(306,'Toxicity to sediment organisms (survival, growth, reproduction)','QMRF 3.11.',NULL),(307,'Toxicity to birds. Short term toxicity (feeding, gavage, other)','3.12.a.',NULL),(308,'Toxicity to birds. Long-term toxicity (survival, growth, reproduction)','QMRF 3.12.b.',NULL),(309,'Acute Inhalation toxicity','QMRF 4.1.',NULL),(310,'Acute Oral toxicity','QMRF 4.2.',NULL),(311,'Acute Dermal toxicity','QMRF 4.3.',NULL),(312,'Skin irritation /corrosion','QMRF 4.4.',NULL),(313,'Acute photoirritation','QMRF 4.5.',NULL),(314,'Skin sensitisation','QMRF 4.6.',NULL),(315,'Respiratory sensitisation','QMRF 4.7.',NULL),(316,'Photosensitisation','QMRF 4.8.',NULL),(317,'Eye irritation/corrosion','QMRF 4.9.',NULL),(318,'Mutagenicity','QMRF 4.10.',NULL),(319,'Photomutagenicity','QMRF 4.11.',NULL),(320,'Carcinogenicity','QMRF 4.12.',NULL),(321,'Photocarcinogenicity','QMRF 4.13.',NULL),(322,'Repeated dose toxicity','QMRF 4.14.',NULL),(323,'In vitro reproductive toxicity (e.g. embryotoxic effects in cell culture such as embryo stem cells)','QMRF 4.15.',NULL),(324,'In vivo pre-natal-developmental toxicity','QMRF 4.16.',NULL),(325,'In vivo pre-, peri-, post natal development and / or fertility (1 or 2 gen','QMRF 4.17.',NULL),(326,'Endocrine Activity. Receptor-binding (specify receptor)','QMRF 4.18.a.',NULL),(327,'Receptor binding and gene expression (specify receptor)','QMRF 4.18.b.',NULL),(328,'Endocrine Activity. Other (e.g.  inhibition of specific enzymes involved in hormone synthesis or regulation, specify enzyme(s) and hormone)','QMRF 4.18.c.',NULL),(329,'Toxicokinetics.Skin penetration','QMRF 5.1.',NULL),(330,'Toxicokinetics.Ocular membrane penetration','QMRF 5.2.',NULL),(331,'Toxicokinetics.Gastrointestinal absorption','QMRF 5.3.',NULL),(332,'Toxicokinetics.Blood-brain barrier penetration','QMRF 5.4.',NULL),(333,'Toxicokinetics.Placental barrier penetration','QMRF 5.5.',NULL),(334,'Toxicokinetics.Blood-testis barrier penetration','QMRF 5.6.',NULL),(335,'Toxicokinetics.Blood-lung barrier penetration','QMRF 5.7.',NULL),(336,'Toxicokinetics.Metabolism (including metabolic clearance)','QMRF 5.8.',NULL),(337,'Toxicokinetics.Protein-binding','QMRF 5.9.',NULL),(338,'Toxicokinetics.DNA-binding','QMRF 5.10.',NULL),(339,'Other','QMRF 6.6.',NULL),(340,'Melting point','QMRF 1.1.',NULL),(341,'Adsorption/Desorption','QMRF 1.11',NULL),(342,'Complex Formation Ability in Water','QMRF 1.12.',NULL),(343,'Density','QMRF 1.13.',NULL),(344,'Particle Size Distribution','QMRF 1.14.',NULL),(345,'Hydrolysis','QMRF 1.15.',NULL),(346,'Stability','QMRF 1.16.',NULL),(347,'Viscosity','QMRF 1.17.',NULL),(348,'Fat Solubility','QMRF 1.18.',NULL),(349,'Flammability','QMRF 1.20.',NULL),(350,'Explosive Properties','QMRF 1.21.',NULL),(351,'Auto-Ignition','QMRF 1.22.',NULL),(352,'Oxidizing Properties','QMRF 1.23.',NULL),(353,'Average Molecular Weight of Polymers','QMRF 1.24.',NULL),(354,'Solution/Extraction Behaviour of Polymers in Water','QMRF 1.25.',NULL),(355,'Length Weighted Geometric Mean Diametre of Fibres','QMRF 1.26.',NULL),(356,'Flash point','QMRF 1.19.',NULL),(357,'Neurotoxicity','QMRF 4.19.','http://alttox.org/ttrc/toxicity-tests/neurotoxicity/'),(358,'Toxicokinetics','QMRF 5.',NULL),(359,'Toxicity to honeybees. Acute oral toxicity','QMRF 3.13.a.',NULL),(360,'Toxicity to honeybees. Acute contact toxicity','QMRF 3.13.b.',NULL),(361,'Ecotoxic effects','QMRF 3.',NULL);
/*!40000 ALTER TABLE `template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-05-04 13:28:04
