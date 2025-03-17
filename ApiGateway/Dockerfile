FROM openjdk:17-slim
WORKDIR /app
COPY build/quarkus-app/ ./
EXPOSE 8050
ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]