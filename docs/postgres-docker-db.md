# Postgres


## Postgres in docker

To start you must first pull the official postgres docker image, this will
be used to create and run postgres containers.

`docker pull postgres`

This pulls the official postgres docker image from the repo. Once you have the image
on your machine you don't have to run this again. To check if you already have
this image, use 'docker images' and check if postgres is listed.


Once you have the postgres image, run the following command:

`docker run --name postgres -e POSTGRES_PASSWORD=Password -p 5432:5432 -d postgres`

This will create a container running postgres at port 5432. After this you can always
start the above container with:

`docker start postgres`


## Docker compose:

To deploy our database to AWS or builds that are not running a local container,
run the command

`docker-compose up`

to run the build instructions in the [docker-compose file](../docker-compose.yml).

These commands run the normal docker commands for the vanilla project and also the 
docker commands to start and run a postgres container with the proper 
configuration for our project.





## Helpful links

https://roboloco.net/blog/docker-postgres/

https://hub.docker.com/_/postgres

https://docs.docker.com/compose/compose-file/

https://stackoverflow.com/questions/37694987/connecting-to-postgresql-in-a-docker-container-from-outside

