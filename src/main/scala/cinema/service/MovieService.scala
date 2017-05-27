package cinema.service

import cinema.model.Command
import cinema.model.Protocol.MovieInfo

import scala.concurrent.Future

/**
  * Created by vans239 on 26/05/17.
  */
trait MovieService {
  def register(req: Command.RegisterMovie): Future[Unit]

  def getMovie(req: Command.GetMovieInfo): Future[MovieInfo]

  def reserve(req: Command.ReserveSeat): Future[Unit]
}
