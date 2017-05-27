package cinema.actor

import akka.actor.{Actor, ActorRef, Props, Status}
import cinema.exception.{MovieAlreadyExistsException, UnknownMovieException}
import cinema.model.Command.{GetMovieInfo, RegisterMovie, ReserveSeat}
import cinema.model.Protocol.{ImdbId, ScreenId}

/**
  * Created by vans239 on 27/05/17.
  */
class MoviesManager(childProps: Props) extends Actor {
  private var children = Map.empty[(ScreenId, ImdbId), ActorRef]

  def receive: Receive = {
    case e: RegisterMovie =>
      val id = idExtract(e)
      children.get(id) match {
        case Some(_) =>
          sender() ! Status.Failure(MovieAlreadyExistsException)
        case None =>
          val child = context.actorOf(childProps)
          children = children.updated(id, child)
          child forward e
      }
    case e: Any if idExtract.isDefinedAt(e) =>
      val id = idExtract(e)
      children.get(id) match {
        case Some(actor) =>
          actor forward e
        case None =>
          sender() ! Status.Failure(UnknownMovieException)
      }
  }

  private def idExtract: PartialFunction[Any, (ScreenId, ImdbId)] = {
      case e: RegisterMovie => (e.screenId, e.imdbId)
      case e: ReserveSeat => (e.screenId, e.imdbId)
      case e: GetMovieInfo => (e.screenId, e.imdbId)
  }
}

object MoviesManager {
  def props(childProps: Props): Props = Props(new MoviesManager(childProps))
}

object MessageIdExtractor {

}