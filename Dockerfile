# use heavier version to make sure curl is inside the container
FROM gradle:7.6.0-jdk17-focal
RUN mkdir /var/deploy
COPY . /var/deploy
WORKDIR /var/deploy
RUN gradle clean
RUN gradle bootJar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker" ,"-jar", "build/libs/movie_uploader-0.0.1-SNAPSHOT.jar"]