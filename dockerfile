FROM gradle:6.7.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon
FROM openjdk:11.0.3-jdk-slim
EXPOSE 10001
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ip-validation.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar","/app/ip-validation.jar"]
