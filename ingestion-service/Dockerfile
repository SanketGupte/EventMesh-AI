# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy pom and download dependencies
COPY pom.xml .
COPY ingestion-service/pom.xml ingestion-service/
COPY routing-service/pom.xml routing-service/
COPY common-lib/pom.xml common-lib/

RUN mvn dependency:go-offline

# Copy source code
COPY ingestion-service ingestion-service
COPY routing-service routing-service
COPY common-lib common-lib

# Build the project
RUN mvn clean package -DskipTests -pl ingestion-service -am

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /build/ingestion-service/target/ingestion-service-0.0.1-SNAPSHOT.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

