FROM gradle:6.7-jdk14 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle --no-daemon bootJar

FROM openjdk:14-jdk-alpine
VOLUME /tmp
EXPOSE 8181
RUN mkdir /app
#RUN apt-get update && apt-get -y upgrade && apt-get -y install curl
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ledger.jar
COPY --from=build /home/gradle/src/scripts/healthcheck.sh /healthcheck.sh
RUN chmod +x /healthcheck.sh
ENV JDK_JAVA_OPTIONS=""
ENTRYPOINT exec java -jar /app/ledger.jar
