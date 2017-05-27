package cinema.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives.{as, complete, entity, logRequestResult, path, pathPrefix, post, _}
import akka.http.scaladsl.server.Route
import cinema.model.Command
import cinema.service.MovieService

import scala.concurrent.ExecutionContext

/**
  * Created by vans239 on 27/05/17.
  */
class HttpApi(movieService: MovieService)(implicit ec: ExecutionContext) {

  import JsonProtocol._

  private val movieRoute: Route = {
    pathPrefix("movie") {
      (post & path("create") & entity(as[Command.RegisterMovie])) { request =>
        complete {
          movieService.register(request).map(_ => OK)
        }
      } ~
        (put & path("info") & entity(as[Command.GetMovieInfo])) { request =>
          complete {
            movieService.getMovie(request)
          }
        } ~ (post & path("reserve") & entity(as[Command.ReserveSeat])) { request =>
        complete {
          movieService.reserve(request).map(_ => OK)
        }
      }
    }
  }

  val routes: Route = handleExceptions(cinema.http.exceptionHandler) {
    logRequestResult("akka-cinema.http-microservice") {
      path("ping") {
        complete(OK)
      } ~
        pathPrefix("api" / "v0.1") {
          movieRoute
        }
    }
  }
}
