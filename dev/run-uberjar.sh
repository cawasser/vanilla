#!/bin/bash

export DATABASE_URL="jdbc:sqlite:./vanilla_db"
export RABBIT_HOST="127.0.0.1"
export RABBIT_VHOST="/main"
export RABBIT_PORT="5672"
export RABBIT_USERNAME="guest"
export RABBIT_PASSWORD="guest"

java -jar ../target/uberjar/vanilla.jar
