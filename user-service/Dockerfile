FROM openjdk:17-alpine
EXPOSE 8081
ARG JAR_FILE=build/libs/user-service-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
