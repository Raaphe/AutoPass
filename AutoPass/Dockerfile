FROM openjdk:21-oracle
COPY src/main/resources/autopass-414515-f21ce763f523.json /
ENV GOOGLE_APPLICATION_CREDENTIALS=/
ARG JAR_FILE=target/*.jar
COPY ./target/AutoPass-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 9090
