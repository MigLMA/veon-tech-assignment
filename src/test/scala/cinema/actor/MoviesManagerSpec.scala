package cinema.actor

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Status}
import akka.testkit.{TestActors, TestKit, TestProbe}
import cinema.Generators
import org.scalatest.{Matchers, WordSpecLike}
import cinema.exception.{MovieAlreadyExistsException, UnknownMovieException}
import cinema.model.Command
import cinema.util.GeneratorUtils._

/**
  * Created by vans239 on 27/05/17.
  */
class MoviesManagerSpec extends TestKit(ActorSystem()) with WordSpecLike with Matchers  {

  trait Context {
    val probe = TestProbe()
    implicit val sender: ActorRef = probe.ref

    val manager: ActorRef = system.actorOf(MoviesManager.props(TestActors.forwardActorProps(probe.ref)))
  }

  "MoviesManager" should {
    "register and forward messages" in new Context {
      val req: Command.RegisterMovie = Generators.RegisterMovieGen.next
      manager ! req
      probe.expectMsg(req)
    }

    "fail to register same movie second time" in new Context {
      val req: Command.RegisterMovie = Generators.RegisterMovieGen.next
      manager ! req
      probe.expectMsg(req)
      manager ! req
      probe.expectMsg(Status.Failure(MovieAlreadyExistsException))
    }

    "fail to get movie info for non existent movie" in new Context {
      val req: Command.GetMovieInfo = Generators.GetMovieInfoGen.next
      manager ! req
      probe.expectMsg(Status.Failure(UnknownMovieException))
    }
  }
}

