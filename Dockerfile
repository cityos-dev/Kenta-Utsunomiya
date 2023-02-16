FROM openjdk:17-alpine

COPY ./movie_uploader /home/
WORKDIR /home
ENTRYPOINT ["./gradlew", "bootRun"]