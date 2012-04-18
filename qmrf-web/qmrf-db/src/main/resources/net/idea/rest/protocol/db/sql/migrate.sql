use qmrf;
GRANT ALL ON qmrf.* TO 'guest'@'localhost';
GRANT TRIGGER ON qmrf.* TO 'guest'@'localhost';
GRANT ALL ON qmrf.* TO 'guest'@'127.0.0.1';
GRANT TRIGGER ON qmrf.* TO 'guest'@'127.0.0.1';

-- project
insert into qmrf.project values(1,"Default",null,null);

-- organisation
insert ignore into qmrf.organisation
select null,affiliation,null,null from qmrf_documents.catalog_authors
union
select null,affiliation,null,null from qmrf_documents.users;

-- authors from catalog_authors table
insert into qmrf.`user`
SELECT id_author,null,"",
trim(substring_index(name," ",1)),
trim(substring(name,instr(name," "))),
affiliation,null,webpage,address,email,"",0 FROM qmrf_documents.catalog_authors u;

-- users
insert into qmrf.`user`
SELECT null,user_name,title,firstname,lastname,affiliation,null,webpage,address,email,keywords,reviewer
FROM qmrf_documents.users u
on duplicate key update keywords=values(keywords), reviewer=values(reviewer), username=values(username);

-- user_organisation
insert ignore into qmrf.user_organisation
SELECT iduser,idorganisation,1 FROM `user` join organisation on user.institute=organisation.name;

-- documents
insert into protocol
SELECT ifnull(idqmrf_origin,idqmrf),version,ifnull(qmrf_title,uuid()),ifnull(qmrf_number,uuid()),xml,true,
u.iduser,
1,idorganisation,qmrf_number,null,"RESEARCH",updated,updated,status='published'
FROM qmrf_documents.documents docs, qmrf.`user` u, qmrf.`user_organisation` org
where
docs.user_name=u.username
and
u.iduser=org.iduser;

-- authors
insert into protocol_authors
SELECT ifnull(idqmrf_origin,idqmrf),version, id_author FROM
 qmrf_documents.doc_authors join qmrf_documents.documents using(idqmrf);
 
 
-- keywords 
insert into keywords select idprotocol,version,
trim(
replace(
replace(
replace(
replace(		
replace(
replace(
replace(
replace(
replace(
extractvalue(abstract,'//@text()'),
'&lt;html&gt;',''),
'&lt;head&gt;',''),
'&lt;/head&gt;',''),
'&lt;body&gt;',''),
'&lt;/body&gt;',''),
'&lt;/html&gt;',''),
'&lt;p style=\"margin-top: 0\"&gt',''),
'&lt;/p&gt;',''),
'&#13;',''))
from protocol on duplicate key update keywords=values(keywords);

-- authors
-- SELECT idprotocol,version,abstract,
-- extractvalue(abstract,'count(/QMRF/Catalogs/authors_catalog/author)')
-- FROM protocol p;

-- attachments table : note files with same name but different ext are lost!
-- smth wrong with 'imported' field
insert ignore into qmrf.attachments
SELECT idattachment,ifnull(idqmrf_origin,idqmrf),version,
if(instr(name,'.')=0,name,substr(name,1,instr(name,'.')-1)),
description,type,olda.updated,format,original_name,imported
from qmrf_documents.documents docs
join qmrf_documents.attachments olda using(idqmrf);

update qmrf.attachments set name = replace(name,"#","N");

-- attachments - to retrieve files from 
-- SELECT concat("curl \"http://qsardb.jrc.it/qmrf/download_attachment.jsp?name=",replace(name," ","+"),"\" 1>  \"qmrf\\",type,"\\\"",name,"'")
-- FROM qmrf_documents.attachments a
-- join qmrf_documents.documents using(idqmrf)
-- where status='published'

-- Files with # are not properly uploaded
-- error
-- #221
-- qmrf299_Daphnia_2_194_training
-- qmrf300_logP_#2_196_training



-- no space
-- qmrf153_Molcode rat carc_training_42
-- qmrf155_HAA training_40
-- qmrf176_Molcode acute oral training_45
-- qmrf333_Acute_toxicity_birds_#1_training_126