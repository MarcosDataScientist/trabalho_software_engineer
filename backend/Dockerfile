FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

# Copy gradle wrapper and config files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Fix line endings and give execute permissions
RUN chmod +x gradlew

# Download dependencies (to cache this layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src

EXPOSE 8080

# Run in dev mode using bootRun
CMD ["./gradlew", "bootRun", "--no-daemon"]
