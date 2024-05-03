FROM openjdk:17-oracle
ARG JAR_FILE=lab/target/*.jar
COPY ${JAR_FILE} lab.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "/lab.jar"]