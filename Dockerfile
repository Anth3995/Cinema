FROM openjdk:17-alpine as builder
WORKDIR /build
COPY . .
RUN ["./gradlew", "bootJar", "-x", "test"]

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /build/build/libs/cinema.jar .
ENTRYPOINT ["java", "-jar", "cinema.jar"]
