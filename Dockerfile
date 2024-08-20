
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/Gestion-de-produit-0.0.1-SNAPSHOT.jar Gestion-de-produit.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Gestion-de-produit.jar", "--debug"]