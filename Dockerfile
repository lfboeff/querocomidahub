# =============================================================================
# Build stage: Compiles the Java application using Maven. It leverages Docker's
# caching by copying the pom.xml first and downloading dependencies offline to
# speed up builds. The source code is then copied, and the application is
# packaged while skipping tests for faster builds.
# =============================================================================

FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests -B

# =============================================================================
# Runtime stage: Uses a minimal JRE image to run the application. A non-root
# user (fiapuser) is created for security. The compiled JAR from the build
# stage is copied over with appropriate ownership. The heap is capped at 75% of
# the container memory limit. The application runs and listens on port 8080.
# =============================================================================

FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S fiapgroup && adduser -S fiapuser -G fiapgroup

USER fiapuser

WORKDIR /app

COPY --chown=fiapuser:fiapgroup --from=build /app/target/querocomidahub*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
