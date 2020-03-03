# Deploy to Docker

Create a deployable uberjar:

    lein uberjar 

### Testing the Uberjar

To run the uberjar locally you first must set your environment variables

If you are on **Windows** run;

    .\dev\Invoke-CmdScript.ps1 .\dev\envVars.cmd

After the script executes, run `Get-ChildItem Env:` to verify environment variables are set, then run

    java -jar target/uberjar/vanilla.jar

which will run the application from the uberjar. go to http://localhost:5000

If you are on **Mac OS / Linux** run;

    ./dev/run.uberjar.sh
    
which will set env variables and run the uberjar.  go to http://locahost:5000

## Creating the Docker image
>NOTE: Recommended way of running is with **docker-compose**, detailed below

Build the Docker image (NOTE: the dot _is_ important):

    docker build -t vanilla_doc .

Then, run the image in Docker:

    docker run -d -p 3500:5000 vanilla_doc 

The `-d` flag tells docker to disconnect form the running 
process, so you will be sent back to the command prompt and
the `-p` flag sets up a port mapping from 3000 inside the 
container (where we designed our app to listen) and 3500 on 
your local machine, so you can connect to the app in the 
container

Check to see that it is running. run:

    docker ps 

Then look for your image in the list.

Finally, connect to the running image, but point the browser at:

    localhost:3500 
    
Notice that we use port 3500 to connect to the containerized app, 
and not 5000 like when we are developing on your local machine

##Running with Docker-compose

Build the docker image with:

    docker build -t vanilla_doc .

Build the rabbitmq image with:

    docker build -t my-rabbit .\rabbitmq\.
    
Then to run both containers together:

    docker-compose up
    
You should then be able to hit the vanilla app at http://locahost:5000 , 
and the rabbitMQ admin console at http://localhost:15672 .

The rabbitMQ console should be fully configured with the necessary exchanges and queues already set up.
To send a message to the edn queue widget on the vanilla dashboard, select the 'Queues' tab from the
rabbit admin console, then select 'some.queue'.  There should be a dropdown there titled 'publish message',
where you can input a message in simple json format:

    {"Message":"This is a test"}
    
clicking 'publish message' should send the message to the queue, and you should see it appear in the edn queue widget in vanilla.
