FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ ./src/
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/simplepayroll-0.0.1-SNAPSHOT.jar simplepayroll.jar
COPY ./data/sheets/dummy.txt /data/sheets/dummy.txt

EXPOSE 8042:8042
ENTRYPOINT ["java", "-jar", "simplepayroll.jar"]
