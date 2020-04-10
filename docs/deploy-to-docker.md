# Deploy to Docker

Create a deployable uberjar:

    lein uberjar 

### Testing the Uberjar

    java -jar target/uberjar/vanilla.jar

will run the application from the uberjar. go to http://localhost:5000

## Creating the Docker image

Build the Docker image (NOTE: the dot _is_ important):

    docker build -t vanilla . 


Then, run the image in Docker:

    docker run -d -p 3500:5000 vanilla 

The `-d` flag tells docker to disconnect from the running 
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
