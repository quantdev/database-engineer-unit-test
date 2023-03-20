## start docker
docker compose up
docker-compose up --detach
docker-compose logs
docker compose down

## psql - using docker
docker exec -it flyway-db-1 bash
psql -U postgres


## psql - using commandline
password is set in docker compose

`/opt/homebrew/Cellar/libpq/15.2/bin/psql -h localhost -U postgres`

## flyway - configuration

configure the connection etc.  

`flyway.conf`

```
flyway.url=jdbc:postgresql://localhost:5432/fwtest
flyway.user=postgres
flyway.password=mypassword
```

```
flyway clean
flyway migrate
flyway info
flyway --help
```

## PostgresSQL

https://www.geeksforgeeks.org/postgresql-psql-commands/

```
 Command	Description
\du	List all users and their roles.
\dt	List all tables in the current database.
\l	List all databases.
\c	Connect to a database.
\i	Execute commands from a file.
\e	Open an editor to write a query.
\timing	Show the time it takes to execute queries.
\df	List all functions.
\dv	List all views.
\dn	List all schemas.
\d	List information about a specific table or other database object.
\x	Toggle expanded display mode.
\q	Quit psql.
```

