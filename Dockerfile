FROM openjdk:8-alpine

WORKDIR /vanilla/

COPY target/uberjar/vanilla.jar app.jar

EXPOSE 5000

COPY package.json package.json
COPY package-lock.json package-lock.json
RUN apk add  --no-cache --repository http://dl-cdn.alpinelinux.org/alpine/v3.7/main/ nodejs=8.9.3-r1
RUN npm install

ENV DATABASE_URL="jdbc:sqlite:./vanilla_db"

ENV RABBIT_HOST="127.0.0.1"
ENV RABBIT_PORT=5672
ENV RABBIT_USERNAME="guest"
ENV RABBIT_PASSWORD="guest"
ENV RABBIT_VHOST="/main"

# Copy the default, or fresh, Database over and use that in the new container
# By Copying it to vanilla_db it now can be used by our URI handlers
COPY vanilla_default vanilla_db

ENTRYPOINT ["java", "-jar", "app.jar"]