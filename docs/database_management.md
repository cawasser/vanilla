## Database Management

This project has two copies of a SQL database, 
```vanilla_db``` and ```vanilla_default```.

### vanilla_db

This database is the locally stored database that our projects codebase
interacts with. For example, when creating a new user, the new user is
saved in ```vanilla_db```.

We will not push this database to our repository as it will change nearly
all the time as we create new layouts/users. This is our development database
that we use to explore, test and develop features with.

This database is 100% needed to run our application locally, and will be 
created when starting the applications server for the first time (lein run).
If somehow this database gets deleted, or if you create a change that
breaks this database, do not worry. Restart the server to create a fresh
copy of ```vanilla_db``` and your application should resume normal 
functionality.


### vanilla_default

This is the database that is a cleanly initialized database, and is kept
in our repository. This database is never used explicitly, but copied over
as a "baseline" or "known-good" database when we deploy our application.

When this project is deployed, it packages up ```vanilla_default``` and
changes the name to ```vanilla_db``` so the project can use a basic
version of a clean and initialized database.

Whenever we update the tables structure or add new tables to our database
we must push out an updated version of ```vanilla_default``` to our
repository. 

 
 


### Rationale

Having two databases prevents developers from pushing their local database
changes to the repository every time they change their users layout. These
changes can be excessive and not productive so we bypassed this with our
current solution of a local database and a deployment database.


These two databases exists to give developers freedom to use their local
copy of the database to explore things with, while keeping the deployed 
database clean and freshly initialized.