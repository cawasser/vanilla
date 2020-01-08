FROM openjdk:8-alpine

#RUN apk add --no-cache bash

COPY target/uberjar/vanilla.jar /vanilla/app.jar

EXPOSE 5000

ENV DATABASE_URL="jdbc:sqlite:./vanilla_db"

COPY vanilla_db vanilla_db

CMD ["java", "-jar", "/vanilla/app.jar"]
