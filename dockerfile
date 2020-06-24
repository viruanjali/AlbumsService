FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/AlbumsService-0.0.1-SNAPSHOT.jar AlbumsService.jar
ENTRYPOINT ["java", "-jar", "AlbumsService.jar"]