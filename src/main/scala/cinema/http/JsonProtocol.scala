package cinema.http

import cinema.model.Command
import cinema.model.Protocol.MovieInfo
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * Created by vans239 on 27/05/17.
  */
object JsonProtocol extends DefaultJsonProtocol {
  implicit val registerMovieFormat: RootJsonFormat[Command.RegisterMovie] = jsonFormat3(Command.RegisterMovie)
  implicit val reserveSeatFormat: RootJsonFormat[Command.ReserveSeat] = jsonFormat2(Command.ReserveSeat)
  implicit val getMovieFormat: RootJsonFormat[Command.GetMovieInfo] = jsonFormat2(Command.GetMovieInfo)
  implicit val movieFormat: RootJsonFormat[MovieInfo] = jsonFormat4(MovieInfo)

  implicit val errorResponseFormat: RootJsonFormat[ErrorResponse] = jsonFormat1(ErrorResponse)

}
