Veon technical assignment

[Task description](Task.md)

Technology stack:
- scala
- akka-http
- akka-actors
- scalatest, scalacheck


Inmemory storage is used for storing current state.

Should be done in future:
- swagger documentation https://github.com/swagger-akka-http/swagger-akka-http
- use akka persistence for each screen
- add request id for idempotence
- sharding by screen and movieId
- timeout handler for http routes


API:
- GET /ping
Ping/pong

- POST /api/v0.1/movie/create
Create movie

- PUT /api/v0.1/movie/info
Get movie information
GET method would be better here, but i am limited with requirements

- POST /api/v0.1/movie/reserve
Reserve seat for specified movie

API examples:

`curl -v -X POST -d "{\"screenId\":\"ekrxv\",\"imdbId\":\"ziaab\",\"availableSeats\":1}" "http://localhost:9999/api/v0.1/movie/create" -H "Content-Type: application/json"`
`curl -v -X PUT -d "{\"screenId\":\"ekrxv\",\"imdbId\":\"ziaab\"}" "http://localhost:9999/api/v0.1/movie/info" -H "Content-Type: application/json"`
`curl -v -X POST -d "{\"screenId\":\"ekrxv\",\"imdbId\":\"ziaab\"}" "http://localhost:9999/api/v0.1/movie/reserve" -H "Content-Type: application/json"`

How to run:

You can use either your favourite ide as launcher,
either run `sbt assembly` and then run application `java -jar target/scala-2.12/veon-tech-assignment-assembly-1.0.jar
`
