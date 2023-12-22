FROM amazoncorretto:17-alpine3.16
ARG JAR_FILE=build/libs/UserRatingAPI-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Xmx1024m", "-Dspring.profiles.active=prod", "/app.jar"]