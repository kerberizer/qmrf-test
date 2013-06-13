ALTER TABLE `protocol` ADD COLUMN `qmrf_assigned` tinyint(4) DEFAULT 0 AFTER `published_status` ;
update protocol set protocol.updated=protocol.updated, qmrf_assigned=1 where published_status='published';

-- -----------------------------------------------------
-- Create new protocol version
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `createProtocolVersion`;
DELIMITER $$
CREATE PROCEDURE createProtocolVersion(
                IN protocol_qmrf_number VARCHAR(36),
                IN new_qmrf_number VARCHAR(36),
                IN title_new VARCHAR(255),
                IN abstract_new LONGTEXT,
                OUT version_new INT)
begin
    DECLARE no_more_rows BOOLEAN;
    DECLARE pid INT;
    --
    DECLARE protocols CURSOR FOR
    	select max(version)+1,idprotocol from protocol where idprotocol in (select idprotocol from protocol where qmrf_number=protocol_qmrf_number) LIMIT 1;
    	
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_rows = TRUE;
    
    OPEN protocols;
    the_loop: LOOP

	  FETCH protocols into version_new,pid;
	  IF no_more_rows THEN
		  CLOSE protocols;
		  LEAVE the_loop;
  	END IF;
        
    -- update published status of the old version to archived

  	-- create new version
    insert into protocol (idprotocol,version,title,qmrf_number,abstract,iduser,summarySearchable,idproject,idorganisation,filename,status,created,published_status,qmrf_assigned)
    select idprotocol,version_new,ifnull(title_new,title),new_qmrf_number,ifnull(abstract_new,abstract),iduser,summarySearchable,idproject,idorganisation,null,status,now(),published_status,qmrf_assigned 
    from protocol where qmrf_number=protocol_qmrf_number;
	
   	-- copy authors
    insert into protocol_authors (idprotocol,version,iduser)
    select idprotocol,version_new,protocol_authors.iduser from protocol_authors join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;

   	-- copy endpoints
    insert into protocol_endpoints (idprotocol,version,idtemplate)
    select idprotocol,version_new,idtemplate from protocol_endpoints join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;
    
   	-- copy keywords
    insert into keywords (idprotocol,version,keywords)
    select idprotocol,version_new,keywords from keywords join protocol using(idprotocol,version) where  qmrf_number=protocol_qmrf_number;    
    
	-- move the qmrf number to the new version; replace the old one with qmrfnumber-vXX
    update protocol set published_status='archived',qmrf_assigned=0,qmrf_number=substring(concat(idprotocol,"A",version,"-",qmrf_number,"-v",version),1,36) where qmrf_number=protocol_qmrf_number;
    update protocol set qmrf_number=protocol_qmrf_number,qmrf_assigned=1 where idprotocol=pid and version=version_new;


    END LOOP the_loop;

end $$

DELIMITER ;

-- -----------------------------------------------------
-- Only documents with 'deleted' status are physically deleted.
-- Documents with non-deleted status are only assigned 'deleted' status
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS `deleteProtocol`;
DELIMITER $$
CREATE PROCEDURE deleteProtocol(IN protocol_qmrf_number VARCHAR(36))
begin
	delete a from protocol_endpoints a,protocol p where a.idprotocol=p.idprotocol and a.version=p.version and qmrf_number=protocol_qmrf_number and published_status='deleted';
	delete a from keywords a,protocol p where a.idprotocol=p.idprotocol and a.version=p.version and qmrf_number=protocol_qmrf_number and published_status='deleted';
	delete a from protocol_authors a,protocol p where a.idprotocol=p.idprotocol and a.version=p.version and qmrf_number=protocol_qmrf_number and published_status='deleted';
	delete a from attachments a, protocol p where a.idprotocol=p.idprotocol and a.version=p.version and qmrf_number=protocol_qmrf_number and published_status='deleted'; 
   	DELETE from protocol where qmrf_number=protocol_qmrf_number and published_status='deleted';
   
	-- otherwise	
   	UPDATE protocol set published_status='deleted',qmrf_number=substring(concat(idprotocol,"D",version,"-",qmrf_number),1,36) where qmrf_number=protocol_qmrf_number and published_status!='deleted';
end $$

DELIMITER ;


insert into version (idmajor,idminor,comment) values (2,9,"QMRF schema");