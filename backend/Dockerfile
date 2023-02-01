FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8181
RUN mkdir /app
#RUN apt-get update && apt-get -y upgrade && apt-get -y install curl
#COPY ./build/libs/ledger.jar /app/ledger.jar
COPY ./build/boot_jar_exploded/ /app/
ENV JDK_JAVA_OPTIONS=""
#ENTRYPOINT exec java -jar /app/ledger.jar
WORKDIR /app/BOOT-INF/classes
ENTRYPOINT exec java -cp .:../../org/springframework/boot/loader/:../lib/* net.flyingfishflash.ledger.Application

