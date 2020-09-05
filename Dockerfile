#FROM maven:3.6-openjdk-14-slim AS build
#COPY . /ledger/
#WORKDIR /ledger/
#RUN mvn --batch-mode --show-version clean package && ls -l target/

#FROM openjdk:14-slim
#VOLUME /tmp
#COPY --from=build /ledger/target/ledger-0.0.1-SNAPSHOT.jar /
#ENV JAVA_OPTS=""
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar ledger-0.0.1-SNAPSHOT.jar" ]

FROM gradle:6.6-jdk14 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x integrationTests

FROM openjdk:14-slim
VOLUME /tmp
#EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ledger-0.0.1-SNAPSHOT.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/ledger-0.0.1-SNAPSHOT.jar"]