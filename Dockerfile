FROM openjdk:17-alpine

COPY . /usr/bin/
WORKDIR /home
ENTRYPOINT ["./gradlew", "bootRun"]