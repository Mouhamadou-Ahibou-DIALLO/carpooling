FROM gradle:8.13.0-jdk21 AS builder
WORKDIR /app

COPY settings.gradle build.gradle gradlew gradlew.bat ./
COPY gradle gradle

RUN gradle dependencies --no-daemon

COPY src src

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]