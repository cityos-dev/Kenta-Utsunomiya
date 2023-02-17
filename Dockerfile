FROM gradle:7.6.0-jdk17-alpine

RUN mkdir /var/deploy
COPY . /var/deploy
WORKDIR /var/deploy
RUN mkdir /var/storage
ENTRYPOINT ["gradle", "bootRun"]