FROM openjdk:8-alpine

#RUN apk add --no-cache bash

COPY target/uberjar/vanilla.jar /vanilla/app.jar

EXPOSE 5000

CMD ["java", "-jar", "/vanilla/app.jar"]
