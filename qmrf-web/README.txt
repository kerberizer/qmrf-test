The QMRF repository web service/application

The default maven profile is -P qmrf

The tests assume 
CREATE USER 'guest'@'localhost' IDENTIFIED BY 'guest';
GRANT ALL ON qmrf.* TO 'guest'@'localhost';

Test DB
GRANT ALL ON `qmrf-test`.* TO 'guest'@'localhost';
