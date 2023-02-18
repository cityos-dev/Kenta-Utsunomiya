FROM gradle:7.6.0-jdk17-alpine

RUN mkdir /var/deploy
COPY . /var/deploy
WORKDIR /var/deploy
RUN gradle clean
RUN gradle bootJar

ENTRYPOINT ["java", "-jar", "build/libs/movie_uploader-0.0.1-SNAPSHOT.jar"]