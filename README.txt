Build: 

cd qmrf-web
mvn clean install -DskipTests=true

Run

cd qmrf-war
mvn tomcat:run