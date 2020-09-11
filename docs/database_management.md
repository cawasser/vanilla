# Databases

Vanilla utilizes a dockerized postgres database to store information for it's operation.

The postgresql database currently stores data pertaining to:
 - Users
 - Data Services (refered to as services)
 - Widget Layouts (referred to as layouts)


Almost all database operations are confined to 
[vanilla.db.core](../src/clj/vanilla/db/core.clj).



## Postgres

We use postgresql as our database solution, and run the database in a docker container
alongside our application. For more information about postgres refer to their website:
[postgresql website](https://www.postgresql.org/).

To learn more about our use of the docker deployment of our database check out our
[postgres docker readme](postgres-docker-db.md). 


## Database spec

To interact with a database in our project, you must declare a database spec, which 
contains basic information that allows the application to find and interact with the
database.

Our current database spec looks like:

```
(def vanilla-db
  {:dbtype "postgresql"
   :dbname "vanilla_db"
   :user "postgres"
   :password "Password"
   :host "localhost"
   :port "5432"})
```  

### Next jdbc

The above database spec is then used by jdbc to create a connection to the specified 
database. We use the next-jdbc library to do this in Clojure. 

To learn more visit 
[next-jdbc](https://github.com/seancorfield/next-jdbc).




## Hugsql

For our vanilla project we utilize the [hugsql](https://www.hugsql.org) library to 
call our sql statements like clojure functions. 







# This is now legacy

- We no longer use local database files.


## Database Management

This project has two copies of a SQL database, 
```vanilla_db``` and ```vanilla_default```.

### vanilla_db

This database is locally stored, and is the database that our project's
codebase interacts with. For example, when creating a new user, the new 
user is saved in ```vanilla_db```.

We will not push this database to our repository as it will change rapidly 
as we create new layouts/users. This is our development database
that we use to explore, test and create features with.

This database is 100% needed to run our application locally, and will be 
created when starting the applications server for the first time 
(```lein run```).
If somehow this database gets deleted, you can stop the server and run 
```lein run``` again to create a fresh copy of ```vanilla_db```.
If you add something to your local database that breaks the structure or
causes errors within the application you can freely delete your copy
of ```vanilla_db``` and follow the above instruction to create a new copy.


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


### Updating the deployment database

After making a change to the structure of the database you will need to 
create a new version of the *vanilla_default* database by running
the following commands in the repl.

Navigate to 

[vanilla.db.core](../src/clj/vanilla/db/core.clj)

and look for the first comment block.

Inside a repl, run the following two code blocks:

```
(def vanilla-default
  "SQLite database connection spec."
  {:dbtype db-type :dbname "vanilla_default"})
```
The above code block creates a definition for the *vanilla_default* 
database. Clojure uses this as a database signature, allowing us to access
the database file and know what it's structure is.
```
(initialize-database vanilla-default)  
```
The above line of code drops all the stored information in the
*vanilla_default*, and initializes all the tables that the database uses.
This essentially creates a new database.


To test that this change worked, run the following line of code:

```(get-services vanilla-default)```

This should print out all the services that exist in the *vanilla_default*
database.