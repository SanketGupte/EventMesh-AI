#!/bin/bash

# Build script for EventMesh AI project
# Usage: ./build.sh [clean|build|test|deploy]

set -e

PROJECT_VERSION="0.0.1-SNAPSHOT"
DOCKER_REGISTRY="eventmesh"

echo "🏗️  EventMesh AI Build Script"
echo "================================"

# Function to clean
clean() {
    echo "🗑️  Cleaning previous build artifacts..."
    mvn clean
    echo "✅ Clean completed"
}

# Function to build
build() {
    echo "🔨 Building EventMesh AI project..."
    mvn clean install -DskipTests
    echo "✅ Build completed"
}

# Function to run tests
test() {
    echo "🧪 Running unit tests..."
    mvn test
    echo "✅ Unit tests completed"

    echo "🔬 Running integration tests..."
    mvn verify
    echo "✅ Integration tests completed"
}

# Function to build docker images
docker_build() {
    echo "🐳 Building Docker images..."

    # Build ingestion service image
    echo "Building ingestion-service image..."
    docker build -t $DOCKER_REGISTRY/ingestion-service:$PROJECT_VERSION \
                 -t $DOCKER_REGISTRY/ingestion-service:latest \
                 ./ingestion-service

    # Build routing service image
    echo "Building routing-service image..."
    docker build -t $DOCKER_REGISTRY/routing-service:$PROJECT_VERSION \
                 -t $DOCKER_REGISTRY/routing-service:latest \
                 ./routing-service

    echo "✅ Docker images built successfully"
}

# Function to deploy
deploy() {
    echo "🚀 Deploying to Kubernetes..."

    # Apply namespace and config
    kubectl apply -f k8s/01-namespace-config.yaml

    # Apply postgres and kafka
    kubectl apply -f k8s/02-postgres-kafka.yaml

    # Wait for dependencies
    echo "⏳ Waiting for PostgreSQL and Kafka to be ready..."
    kubectl wait --for=condition=ready pod -l app=postgres -n eventmesh --timeout=300s
    kubectl wait --for=condition=ready pod -l app=kafka -n eventmesh --timeout=300s

    # Apply services
    kubectl apply -f k8s/03-ingestion-service.yaml
    kubectl apply -f k8s/04-routing-service.yaml

    # Wait for services to be ready
    echo "⏳ Waiting for services to be ready..."
    kubectl rollout status deployment/ingestion-service -n eventmesh
    kubectl rollout status deployment/routing-service -n eventmesh

    echo "✅ Deployment completed successfully"
}

# Main script logic
case "${1:-build}" in
    clean)
        clean
        ;;
    build)
        build
        ;;
    test)
        test
        ;;
    docker)
        docker_build
        ;;
    deploy)
        deploy
        ;;
    all)
        clean
        build
        test
        docker_build
        ;;
    *)
        echo "Usage: $0 {clean|build|test|docker|deploy|all}"
        exit 1
        ;;
esac

