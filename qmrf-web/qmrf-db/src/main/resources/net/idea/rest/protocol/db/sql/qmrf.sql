-- CREATE DATABASE `tb` /*!40100 DEFAULT CHARACTER SET utf8 */;

-- -----------------------------------------------------
-- Users. If registered, 'username' points to OpenAM user name
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE  `user` (
  `iduser` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL COMMENT 'OpenAM user name',
  `title` varchar(45) DEFAULT NULL,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `institute` varchar(128) DEFAULT NULL,
  `weblog` varchar(45) DEFAULT NULL,
  `homepage` varchar(45) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
   `keywords` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '""',
  `reviewer` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'true if wants to become a reviewer',
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `Index_2` (`username`),
  UNIQUE KEY `Index_3` (`lastname`,`firstname`,`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Organisation, project, linked to OpenAM groups
-- -----------------------------------------------------
DROP TABLE IF EXISTS `organisation`;
CREATE TABLE  `organisation` (
  `idorganisation` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `ldapgroup` varchar(128) DEFAULT NULL,
  `cluster` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`idorganisation`),
  UNIQUE KEY `Index_2` (`name`),
  KEY `Index_3` (`cluster`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Projects
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE  `project` (
  `idproject` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `ldapgroup` varchar(128) DEFAULT NULL,
  `cluster` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`idproject`),
  UNIQUE KEY `Index_2` (`name`),
  KEY `Index_3` (`cluster`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- -----------------------------------------------------
-- User affiliations 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_organisation`;
CREATE TABLE  `user_organisation` (
  `iduser` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idorganisation` int(10) unsigned NOT NULL,
  `priority` int(2) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`iduser`,`idorganisation`),
  KEY `FK_user_organisation_2` (`idorganisation`),
  KEY `Index_3` (`iduser`,`priority`),
  CONSTRAINT `FK_user_organisation_2` FOREIGN KEY (`idorganisation`) REFERENCES `organisation` (`idorganisation`) ON UPDATE CASCADE,
  CONSTRAINT `FK_user_organisation_1` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Projects the user is working on
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_project`;
CREATE TABLE  `user_project` (
  `iduser` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idproject` int(10) unsigned NOT NULL,
  `priority` int(2) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`iduser`,`idproject`),
  KEY `FK_user_project_2` (`idproject`),
  KEY `Index_3` (`iduser`,`priority`),
  CONSTRAINT `FK_user_project_2` FOREIGN KEY (`idproject`) REFERENCES `project` (`idproject`) ON UPDATE CASCADE,
  CONSTRAINT `FK_user_project_1` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Protocols metadata & placeholder for data templates. 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `protocol`;
CREATE TABLE  `protocol` (
  `idprotocol` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `version` int(10) unsigned NOT NULL DEFAULT '1' COMMENT 'Version',
  `title` varchar(255) NOT NULL COMMENT 'Title',
  `abstract` text,
  `summarySearchable` tinyint(1) NOT NULL DEFAULT '1',
  `iduser` int(10) unsigned NOT NULL COMMENT 'Link to user table',
  `idproject` int(10) unsigned NOT NULL COMMENT 'Link to projects table',
  `idorganisation` int(10) unsigned NOT NULL COMMENT 'Link to org table',
  `filename` text COMMENT 'Path to file name',
  `template` text COMMENT 'Data template',
  `status` enum('RESEARCH','SOP') NOT NULL DEFAULT 'RESEARCH' COMMENT 'Research or Standard Operating Procedure',
  `latest` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Is the latest version',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last updated',
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `published` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idprotocol`,`version`) USING BTREE,
  KEY `Index_3` (`title`),
  KEY `FK_protocol_1` (`idproject`),
  KEY `FK_protocol_2` (`idorganisation`),
  KEY `FK_protocol_3` (`iduser`),
  KEY `Index_7` (`latest`),
  KEY `updated` (`updated`),
  CONSTRAINT `FK_protocol_1` FOREIGN KEY (`idproject`) REFERENCES `project` (`idproject`),
  CONSTRAINT `FK_protocol_2` FOREIGN KEY (`idorganisation`) REFERENCES `organisation` (`idorganisation`),
  CONSTRAINT `FK_protocol_3` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- -----------------------------------------------------
-- Protocol authors
-- -----------------------------------------------------
DROP TABLE IF EXISTS `protocol_authors`;
CREATE TABLE  `protocol_authors` (
  `idprotocol` int(10) unsigned NOT NULL,
  `version` int(10) unsigned NOT NULL,
  `iduser` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idprotocol`,`version`,`iduser`) USING BTREE,
  KEY `FK_protocol_authors_2` (`iduser`),
  CONSTRAINT `FK_protocol_authors_1` FOREIGN KEY (`idprotocol`, `version`) REFERENCES `protocol` (`idprotocol`, `version`),
  CONSTRAINT `FK_protocol_authors_2` FOREIGN KEY (`iduser`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Attachments 
-- -----------------------------------------------------
CREATE TABLE  `attachments` (
  `idattachment` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `idprotocol` int(10) unsigned NOT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `description` text NOT NULL,
  `type` enum('data_training','data_validation','document') NOT NULL DEFAULT 'document',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `format` varchar(32) NOT NULL DEFAULT 'txt',
  `original_name` text,
  `imported` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idattachment`) USING BTREE,
  UNIQUE KEY `Index_4` (`idprotocol`,`version`,`name`) USING BTREE,
  KEY `name` (`name`),
  KEY `type` (`type`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Trigger 
-- -----------------------------------------------------
-- DELIMITER $
-- CREATE TRIGGER insert_protocol_id BEFORE UPDATE ON protocol
-- FOR EACH ROW BEGIN
--	IF NEW.idprotocol != null THEN
--		set NEW.version = OLD.version+1;
--	END IF;
-- END $
-- DELIMITER ;

-- -----------------------------------------------------
-- Keywords. Want to do full text search, thus MyISAM. 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `keywords`;
CREATE TABLE  `keywords` (
  `idprotocol` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  `keywords` text NOT NULL COMMENT 'All keywords semicolon delimited',
  PRIMARY KEY (`idprotocol`,`version`) USING BTREE,
  FULLTEXT KEY `Index_2` (`keywords`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- -----------------------------------------------------
-- Endpoints. 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE  `template` (
  `idtemplate` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `code` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`idtemplate`),
  UNIQUE KEY `template_list_index4157` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- -----------------------------------------------------
-- Endpoints hierarchy
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE  `dictionary` (
  `idsubject` int(10) unsigned NOT NULL,
  `relationship` enum('is_a','is_part_of') COLLATE utf8_bin NOT NULL DEFAULT 'is_a',
  `idobject` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idsubject`,`relationship`,`idobject`),
  KEY `FK_dictionary_2` (`idobject`),
  CONSTRAINT `FK_dictionary_1` FOREIGN KEY (`idsubject`) REFERENCES `template` (`idtemplate`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_dictionary_2` FOREIGN KEY (`idobject`) REFERENCES `template` (`idtemplate`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- -----------------------------------------------------
-- DB schema version
-- -----------------------------------------------------
DROP TABLE IF EXISTS `version`;
CREATE TABLE  `version` (
  `idmajor` int(5) unsigned NOT NULL,
  `idminor` int(5) unsigned NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comment` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`idmajor`,`idminor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
insert into version (idmajor,idminor,comment) values (2,1,"QMRF schema");

-- -----------------------------------------------------
-- Create new protocol version
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `createProtocolVersion`;
DELIMITER $$
CREATE PROCEDURE createProtocolVersion(
                IN protocol_id INT,
                IN version_id INT,
                IN title_new VARCHAR(255),
                IN abstract_new TEXT,
                OUT version_new INT)
begin
    DECLARE no_more_rows BOOLEAN;
    DECLARE protocols CURSOR FOR
    select max(version)+1 from protocol where idprotocol=protocol_id group by idprotocol LIMIT 1;
    DECLARE CONTINUE HANDLER FOR NOT FOUND     SET no_more_rows = TRUE;

    OPEN protocols;
    the_loop: LOOP

	  FETCH protocols into version_new;
	  IF no_more_rows THEN
		  CLOSE protocols;
		  LEAVE the_loop;
  	END IF;

    select version_new;
  	-- create new version
    insert into protocol (idprotocol,version,title,abstract,iduser,summarySearchable,idproject,idorganisation,filename,status,created)
    select idprotocol,version_new,title_new,abstract_new,iduser,summarySearchable,idproject,idorganisation,null,status,now() 
    from protocol where idprotocol=protocol_id and version=version_id;
   	-- copy authors
    -- insert into protocol_authors
    -- select idprotocol,version_new,iduser from protocol_authors where idprotocol=protocol_id and version=version_id;

    END LOOP the_loop;

end $$

DELIMITER ;