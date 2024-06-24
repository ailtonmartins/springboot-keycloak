FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# Estágio de execução
FROM openjdk:17-jdk-slim
RUN apt-get update \
 && apt-get install curl unzip zip -y
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]