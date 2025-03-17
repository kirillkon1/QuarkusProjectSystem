FROM openjdk:17-slim
WORKDIR /app
COPY build/quarkus-app/ ./
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]