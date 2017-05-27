package cinema.actor

import akka.Done
import akka.actor.{Actor, Props, Status}
import cinema.exception.NoEmptySeatsException
import cinema.model.Command.{GetMovieInfo, RegisterMovie, ReserveSeat}
import cinema.model.Protocol.MovieInfo

/**
  * Created by vans239 on 27/05/17.
  */
class MovieManager extends Actor {

  private def registerReceive: Receive = {
    case e: RegisterMovie =>
      val state = MovieInfo(e.screenId, e.imdbId, e.availableSeats, 0)
      context.become(actionsReceive(state))
      sender() ! Done
  }

  private def actionsReceive(movieInfo: MovieInfo): Receive = {
    case _: GetMovieInfo =>
      sender() ! movieInfo
    case _: ReserveSeat if movieInfo.availableSeats > movieInfo.reservedSeats =>
      context.become(actionsReceive(movieInfo.withReserved))
      sender() ! Done
    case _: ReserveSeat =>
      sender() ! Status.Failure(NoEmptySeatsException)

  }

  override def receive: Receive = registerReceive
}

object MovieManager {
  def props(): Props = Props(new MovieManager)
}