FROM maven:3-jdk-8 as scratch

# Let's pull plugins and dependencies only to make use of efficient image layer caching
COPY cas-overlay/pom.xml /tmp/app/pom.xml

WORKDIR /tmp/app

RUN mvn clean package

# Let's build for real (unless the pom.xml was updated then this should be a quick build.
COPY cas-overlay/ /tmp/app/

RUN mvn clean package


FROM openjdk:8-jdk

COPY --from=scratch /tmp/app/target/cas.war /opt/cas/cas.war
COPY etc/cas/ /etc/cas/

RUN keytool -import -alias tomcat -keystore /etc/ssl/certs/java/cacerts -file /etc/cas/tomcat.der -storepass changeit -noprompt

WORKDIR /opt/cas

EXPOSE 8443

CMD ["java", "-jar", "cas.war"]