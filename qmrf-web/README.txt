The QMRF repository web service/application

The default maven profile is -P qmrf

The tests assume the following queries:

GRANT ALL ON qmrf.* TO 'guest'@'localhost' IDENTIFIED BY 'guest';
GRANT ALL ON qmrf.* TO 'guest'@'127.0.0.1' IDENTIFIED BY 'guest';
GRANT ALL ON qmrf.* TO 'guest'@'::1' IDENTIFIED BY 'guest';

GRANT EXECUTE ON FUNCTION qmrf.getAuthorDetails TO 'guest'@'localhost';
GRANT EXECUTE ON FUNCTION qmrf.getAuthorDetails TO 'guest'@'127.0.0.1';
GRANT EXECUTE ON FUNCTION qmrf.getAuthorDetails TO 'guest'@'::1';

GRANT EXECUTE ON PROCEDURE qmrf.createProtocolVersion TO 'guest'@'localhost';
GRANT EXECUTE ON PROCEDURE qmrf.createProtocolVersion TO 'guest'@'127.0.0.1';
GRANT EXECUTE ON PROCEDURE qmrf.createProtocolVersion TO 'guest'@'::1';

GRANT EXECUTE ON PROCEDURE qmrf.deleteProtocol TO 'guest'@'localhost';
GRANT EXECUTE ON PROCEDURE qmrf.deleteProtocol TO 'guest'@'127.0.0.1';
GRANT EXECUTE ON PROCEDURE qmrf.deleteProtocol TO 'guest'@'::1';

GRANT TRIGGER ON `ambit2-qmrf`.* TO 'guest'@'localhost';
GRANT TRIGGER ON `ambit2-qmrf`.* TO 'guest'@'127.0.0.1';
GRANT TRIGGER ON `ambit2-qmrf`.* TO 'guest'@'::1';

GRANT EXECUTE ON `ambit2-qmrf`.* TO 'guest'@'localhost';
GRANT EXECUTE ON `ambit2-qmrf`.* TO 'guest'@'127.0.0.1';
GRANT EXECUTE ON `ambit2-qmrf`.* TO 'guest'@'::1';

GRANT EXECUTE ON PROCEDURE `ambit2-qmrf`.findByProperty to 'guest'@'localhost';
GRANT EXECUTE ON PROCEDURE `ambit2-qmrf`.findByProperty to 'guest'@'127.0.0.1';
GRANT EXECUTE ON PROCEDURE `ambit2-qmrf`.findByProperty to 'guest'@'::1';

To change the default user name and password, modify the local maven profile.

Following are the test databases. Only required if running JUnit tests.

CREATE DATABASE `qmrf-test` CHARACTER SET utf8;

GRANT ALL ON `qmrf-test`.* TO 'guest'@'localhost' IDENTIFIED BY 'guest';
GRANT ALL ON `qmrf-test`.* TO 'guest'@'127.0.0.1' IDENTIFIED BY 'guest';
GRANT ALL ON `qmrf-test`.* TO 'guest'@'::1' IDENTIFIED BY 'guest';

GRANT TRIGGER ON `qmrf-test`.* TO 'guest'@'localhost';
GRANT TRIGGER ON `qmrf-test`.* TO 'guest'@'127.0.0.1';
GRANT TRIGGER ON `qmrf-test`.* TO 'guest'@'::1';

GRANT UPDATE, DROP, CREATE, SELECT, INSERT, EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE ON `qmrf-test`.* TO 'guest'@'localhost';
GRANT UPDATE, DROP, CREATE, SELECT, INSERT, EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE ON `qmrf-test`.* TO 'guest'@'127.0.0.1';
GRANT UPDATE, DROP, CREATE, SELECT, INSERT, EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE ON `qmrf-test`.* TO 'guest'@'::1';

GRANT EXECUTE ON FUNCTION `qmrf-test`.getAuthorDetails TO 'guest'@'localhost';
GRANT EXECUTE ON FUNCTION `qmrf-test`.getAuthorDetails TO 'guest'@'127.0.0.1';
GRANT EXECUTE ON FUNCTION `qmrf-test`.getAuthorDetails TO 'guest'@'::1';

CREATE DATABASE aalocal_test CHARACTER SET utf8;
USE aalocal_test;
source tomcat_users.sql

GRANT UPDATE, DROP, CREATE, SELECT, INSERT, EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE ON aalocal_test.* TO 'guest'@'localhost';
GRANT UPDATE, DROP, CREATE, SELECT, INSERT, EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE ON aalocal_test.* TO 'guest'@'127.0.0.1';
GRANT UPDATE, DROP, CREATE, SELECT, INSERT, EXECUTE, DELETE, CREATE ROUTINE, ALTER ROUTINE ON aalocal_test.* TO 'guest'@'127.0.0.1';
