FROM gradle:6.7-jdk14 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
#RUN gradle --no-daemon bootJar
RUN gradle --no-daemon explodeBootJar

FROM openjdk:14-jdk-alpine
VOLUME /tmp
EXPOSE 8181
RUN mkdir /app
#COPY --from=build /home/gradle/src/build/libs/*.jar /app/ledger.jar
COPY --from=build /home/gradle/src/build/boot_jar_exploded/ /app/
COPY --from=build /home/gradle/src/scripts/healthcheck.sh /healthcheck.sh
RUN chmod +x /healthcheck.sh
ENV JDK_JAVA_OPTIONS=""
#ENTRYPOINT exec java -jar /app/ledger.jar
WORKDIR /app/BOOT-INF/classes
ENTRYPOINT exec java -cp .:../../org/springframework/boot/loader/:../lib/* net.flyingfishflash.ledger.Application
