### Database Management
To run a local version of this project you will need to start a 
repl and initialize the vanilla_db.
This can be done in 
[vanilla.db.core](src/clj/vanilla/db/core.clj)
on line 132 (As of 02/10/2020), and run the following line of code from the repl  to initialize a local
copy of the database.
```
(initialize-database vanilla-db)
```

When this project is deployed, it packages up vanilla_default (the database 
included this repo) and changes the name to vanilla_db so the project can use 
a basic version of a clean and initialized database.

This is how the project allows the developers to have their own development 
database and the deployed version of this application to have a clean
initialized working database.