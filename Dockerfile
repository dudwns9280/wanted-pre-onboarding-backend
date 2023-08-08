FROM gradle:8.2.1-jdk17-focal as builder
COPY . /builder
WORKDIR /builder
RUN gradle build -x test --parallel


# APP
FROM openjdk:17-jdk

COPY --from=builder /builder/build/libs/dashboard-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]