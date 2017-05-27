package cinema

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.BadRequest
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.ExceptionHandler
import cinema.exception.{MovieAlreadyExistsException, NoEmptySeatsException, UnknownMovieException}

/**
  * Created by vans239 on 27/05/17.
  */
package object http {

  case class ErrorResponse(error: String)

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonProtocol._

  val exceptionHandler = ExceptionHandler {
    case MovieAlreadyExistsException =>
      complete(BadRequest -> ErrorResponse("Movie already exists"))
    case NoEmptySeatsException =>
      complete(BadRequest -> ErrorResponse("No empty seats"))
    case UnknownMovieException =>
      complete(BadRequest -> ErrorResponse("Unknown movie"))
  }
}
