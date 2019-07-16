# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# The application's jar file
ARG JAR_FILE=target/reactive-oauth-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} reactive-oauth-0.0.1-SNAPSHOT.jar

RUN mkdir /ext

ADD src/main/resources/keystore.jks /ext

# Run the jar file
ENTRYPOINT ["java","-Dloader.path=/ext","-Djava.security.egd=file:/dev/./urandom","-jar","/reactive-oauth-0.0.1-SNAPSHOT.jar"]