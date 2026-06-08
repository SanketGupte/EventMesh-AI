# =====================================================
# Build Stage
# =====================================================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy parent and module pom files
COPY pom.xml .

COPY common-lib/pom.xml common-lib/
COPY ingestion-service/pom.xml ingestion-service/
COPY routing-service/pom.xml routing-service/

# Copy source code
COPY common-lib/src common-lib/src
COPY ingestion-service/src ingestion-service/src
COPY routing-service/src routing-service/src

# Build all modules
RUN mvn clean package -DskipTests

# =====================================================
# Runtime Stage - Ingestion Service
# =====================================================
FROM eclipse-temurin:17-jre AS ingestion-runtime

WORKDIR /app

COPY --from=build /app/ingestion-service/target/ingestion-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

# =====================================================
# Runtime Stage - Routing Service
# =====================================================
FROM eclipse-temurin:17-jre AS routing-runtime

WORKDIR /app

COPY --from=build /app/routing-service/target/routing-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]