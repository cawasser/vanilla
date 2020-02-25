FROM openjdk:8-alpine

#RUN apk add --no-cache bash

COPY target/uberjar/vanilla.jar /vanilla/app.jar

EXPOSE 5000

ENV DATABASE_URL="jdbc:sqlite:./vanilla_db"

# Copy the default, or fresh, Database over and use that in the new container
# By Copying it to vanilla_db it now can be used by our URI handlers
COPY vanilla_default vanilla_db

CMD ["java", "-jar", "/vanilla/app.jar"]
