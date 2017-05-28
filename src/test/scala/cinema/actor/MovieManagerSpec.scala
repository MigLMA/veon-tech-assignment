package cinema.actor

import akka.Done
import akka.actor.{ActorRef, ActorSystem, Status}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import cinema.Generators
import org.scalatest.{Matchers, WordSpecLike}
import cinema.exception.NoEmptySeatsException
import cinema.model.Command.{GetMovieInfo, ReserveSeat}
import cinema.model.Protocol.MovieInfo
import cinema.util.GeneratorUtils._

/**
  * Created by vans239 on 27/05/17.
  */
class MovieManagerSpec extends TestKit(ActorSystem()) with WordSpecLike with Matchers with ImplicitSender {

  "Movie manager" should {
    "handle standard workflow" in {
      val req = Generators.RegisterMovieGen.next.copy(availableSeats = 1)
      val manager = system.actorOf(MovieManager.props())
      manager ! req
      expectMsg(Done)

      manager ! GetMovieInfo(req.screenId, req.imdbId)
      expectMsg(MovieInfo(req.screenId, req.imdbId, 1, 0))

      manager ! ReserveSeat(req.screenId, req.imdbId)
      expectMsg(Done)

      manager ! GetMovieInfo(req.screenId, req.imdbId)
      expectMsg(MovieInfo(req.screenId, req.imdbId, 1, 1))

      manager ! ReserveSeat(req.screenId, req.imdbId)
      expectMsg(Status.Failure(NoEmptySeatsException))
    }

  }

}
