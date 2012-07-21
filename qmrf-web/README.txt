The QMRF repository web service/application

The default maven profile is -P qmrf

The tests assume 
CREATE USER 'guest'@'localhost' IDENTIFIED BY 'guest';
GRANT ALL ON qmrf.* TO 'guest'@'localhost';
GRANT TRIGGER ON qmrf.* TO 'guest'@'localhost';
GRANT execute on `ambit2-qmrf`.* to guest@localhost

To change user name and password, modify the local maven profile.

Test DB
create database `qmrf-test` character set utf8;
GRANT ALL ON `qmrf-test`.* TO 'guest'@'localhost';
GRANT TRIGGER ON qmrf_test.* TO 'guest'@'localhost';
GRANT UPDATE,DROP,CREATE,SELECT,INSERT,EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE on `qmrf-test`.* TO 'guest'@'localhost';
GRANT UPDATE,DROP,CREATE,SELECT,INSERT,EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE on `qmrf-test`.* TO 'guest'@'127.0.0.1';
