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






## Updating the deployment database

After making a change to the structure of the database you will need to 
create a new version of the database tables by running
the following commands in the repl.

Navigate to 
[vanilla.db.core](../src/clj/vanilla/db/core.clj)
and look for the first comment block.

Inside a repl, run the following code block:

```
(initialize-database vanilla-db)  
```
The above line of code drops all the stored information in the
database, and initializes all the tables that the database uses.
This essentially creates a new database.


To test that this change worked, run the following line of code:

```
(get-services vanilla-default)
```

This should print out all the services that exist in the database.





## Rationale

Instead of having local database files we made the switch to a hosted database
for many reasons. Having a hosted database takes care of having to set up or install
a database on a new machine, just download the docker file. 