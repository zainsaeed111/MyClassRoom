FROM gradle:8-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle installDist --no-daemon

FROM openjdk:17-slim
EXPOSE 8080
mkdir /app
COPY --from=build /home/gradle/src/build/install/MyClassRoom /app/
WORKDIR /app/bin
CMD ["./MyClassRoom"]
