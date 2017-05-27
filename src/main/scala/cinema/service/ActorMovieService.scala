package cinema.service

import akka.actor.ActorRef
import cinema.model.Command
import cinema.model.Protocol.MovieInfo

import scala.concurrent.{ExecutionContext, Future}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.duration.FiniteDuration

/**
  * Created by vans239 on 27/05/17.
  */
class ActorMovieService(manager: ActorRef)(implicit ec: ExecutionContext, duration: FiniteDuration)
  extends MovieService {
  private implicit val timeout = Timeout(duration)

  override def getMovie(req: Command.GetMovieInfo): Future[MovieInfo] =
    manager.ask(req).mapTo[MovieInfo]

  override def reserve(req: Command.ReserveSeat): Future[Unit] =
    manager.ask(req).map(_ => ())

  override def register(req: Command.RegisterMovie): Future[Unit] =
    manager.ask(req).map(_ => ())

}
