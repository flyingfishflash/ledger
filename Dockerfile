FROM maven:3.6-openjdk-14-slim AS build
COPY . /ledger/
#RUN apk update && apk upgrade
WORKDIR /ledger/
RUN mvn --batch-mode --show-version clean package && ls -l target/

FROM openjdk:14-slim
VOLUME /tmp
COPY --from=build /ledger/target/ledger-0.0.1-SNAPSHOT.jar /
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar ledger-0.0.1-SNAPSHOT.jar" ]