FROM java:8
WORKDIR /
ADD server/target/fat.jar server.jar
ADD frontend/dist frontend
ADD https://repository.sonatype.org/service/local/artifact/maven/content?r=central-proxy&g=mysql&a=mysql-connector-java&e=jar&v=8.0.17 mysql-driver.jar
EXPOSE 8080
CMD java -cp server.jar:mysql-driver.jar com.github.healthweb.server.Server
