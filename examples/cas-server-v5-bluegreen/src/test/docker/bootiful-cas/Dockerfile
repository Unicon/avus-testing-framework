FROM openjdk:8-jdk

RUN wget https://github.com/UniconLabs/bootiful-cas-client/archive/master.zip \
    && unzip master.zip -d /opt \
    && rm master.zip

WORKDIR /opt/bootiful-cas-client-master

COPY application.yaml src/main/resources/application.yml
COPY tomcat.jks /opt/bootiful-cas-client-master/

RUN ./gradlew clean build

EXPOSE 8443

CMD ["./gradlew", "bootRun"]
