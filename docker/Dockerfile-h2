FROM java:8
WORKDIR /
ADD server/target/fat.jar server.jar
ADD frontend/dist frontend
ADD https://repository.sonatype.org/service/local/artifact/maven/content?r=central-proxy&g=com.h2database&a=h2&e=jar&v=1.4.199 h2-driver.jar
EXPOSE 8080
CMD java -cp server.jar:h2-driver.jar com.github.healthweb.server.Server
