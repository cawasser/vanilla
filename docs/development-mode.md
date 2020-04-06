# Running the Code in 'Development Mode' 

Before running the application, several environment variable must be set in whichever terminal
application you are running leiningen from.

    DATABASE_URL="jdbc:sqlite:./vanilla_db"
    RABBIT_HOST="127.0.0.1"
    RABBIT_VHOST="/main"
    RABBIT_PORT="5672"
    RABBIT_USERNAME="guest"
    RABBIT_PASSWORD="guest"

The /dev directory at the root level of the project contains scripts that can auto populate these
environment variables for you.

If you are using Powershell, navigate to the `/dev` directory and run

    Invoke-CmdScript.ps1 envVars.cmd

You can check that the env vars were set correctly by running this command and viewing the output

     Get-childitem Env:

If you are on MacOS Terminal, you can run `run-uberjar.sh` and simply comment
the "java -jar" line out if you dont want to run the uberjar.

If running from the IntelliJ terminal, env vars can be set from the settings.

File > Settings.  Then search 'terminal' or Tools > Terminal and you will see a box
to set environment variables.  It can be expanded for easier entry.

Once you've set the environment variables to connect to rabbit, run:

- Start server `lein run`
- Start figwheel `lein figwheel`
- Go to http://localhost:5000

