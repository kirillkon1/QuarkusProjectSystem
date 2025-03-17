#!/bin/bash

# Завершаем скрипт при любой ошибке
set -e

# User Management Service
echo "Building ApiObjects..."
cd ../ApiObjects
./gradlew clean build -x test
./gradlew publishToMavenLocal

# User Management Service
echo "Building UserManagementService..."
cd ../UserService
./gradlew clean build -x test

# User Management Service
echo "Building TaskManagementService..."
cd ../TaskService
./gradlew clean build -x test

# User Management Service
echo "Building ProjectManagementService..."
cd ../ProjectService
./gradlew clean build -x test

# User Management Service
echo "Building AnalyticsManagementService..."
cd ../AnalyticsService
./gradlew clean build -x test

# API Gateway
echo "Building ApiGateway..."
cd ../ApiGateway
./gradlew clean build -x test

