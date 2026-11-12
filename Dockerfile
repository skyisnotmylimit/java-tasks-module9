FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
COPY micro-sender/pom.xml micro-sender/
COPY micro-recipient/pom.xml micro-recipient/
COPY micro-collector/pom.xml micro-collector/
COPY micro-visualizer/pom.xml micro-visualizer/
RUN mvn -q -B dependency:go-offline -DskipTests || true
COPY . .
ARG MODULE
RUN mvn -q -B -pl ${MODULE} -am package -DskipTests

FROM eclipse-temurin:21-jre
ARG MODULE
WORKDIR /app
COPY --from=build /src/${MODULE}/target/${MODULE}-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
