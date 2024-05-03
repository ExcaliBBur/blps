FROM openjdk:17-oracle
ARG JAR_FILE=confirmator/target/*.jar
COPY ${JAR_FILE} confirmator.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "/confirmator.jar"]