# SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
#
# SPDX-License-Identifier: CC0-1.0

FROM amazoncorretto:17-alpine3.16
ARG JAR_FILE=build/libs/UserRatingAPI-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Xmx1024m", "-Dspring.profiles.active=prod", "/app.jar"]