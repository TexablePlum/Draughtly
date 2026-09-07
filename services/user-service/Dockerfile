# syntax=docker/dockerfile:1

# --- Build stage: JDK 25 + Gradle wrapper, produces the executable boot jar ---
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

# Warm the dependency cache separately from the sources.
COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle build.gradle ./
RUN chmod +x ./gradlew && ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true

COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

# --- Runtime stage: slim JRE 25, runs the jar ---
FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app

COPY --from=build /workspace/build/libs/*.jar /app/app.jar
USER app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
