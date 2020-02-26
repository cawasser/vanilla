FROM openjdk:8-alpine

#RUN apk add --no-cache bash

WORKDIR /vanilla/

COPY target/uberjar/vanilla.jar app.jar

EXPOSE 5000

ENV DATABASE_URL="jdbc:sqlite:./vanilla_db"

ENV RABBIT_HOST="127.0.0.1"
ENV RABBIT_PORT=5672
ENV RABBIT_USERNAME="guest"
ENV RABBIT_PASSWORD="guest"
ENV RABBIT_VHOST="/main"

# Copy the default, or fresh, Database over and use that in the new container
# By Copying it to vanilla_db it now can be used by our URI handlers
COPY vanilla_default vanilla_db

CMD ["java", "-jar", "app.jar"]
