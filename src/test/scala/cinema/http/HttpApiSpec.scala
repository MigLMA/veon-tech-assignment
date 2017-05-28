package cinema.http

import akka.Done
import akka.actor.{ActorRef, Status}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{TestActor, TestKitBase, TestProbe}
import cinema.Generators._
import cinema.exception.MovieAlreadyExistsException
import cinema.model.Command
import cinema.model.Protocol.MovieInfo
import cinema.service.ActorMovieService
import cinema.util.GeneratorUtils._
import cinema.util.Threads
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._

/**
  * Created by vans239 on 27/05/17.
  */
class HttpApiSpec
  extends WordSpecLike with ScalatestRouteTest with TestKitBase with Matchers {

  import JsonProtocol._

  trait Context {
    val timeout: FiniteDuration = 500.millis
    val probe: TestProbe = TestProbe()
    val routes: Route = new HttpApi(new ActorMovieService(probe.ref)(Threads.callingThreadEC, timeout)).routes

  }

  def autopilot(request: Any, response: Any) = new TestActor.AutoPilot {
    def run(sender: ActorRef, msg: Any): TestActor.AutoPilot =
      msg match {
        case r if r == request =>
          sender ! response
          TestActor.NoAutoPilot
      }
  }

  "HttpApi" should {
    "ping" in new Context {
      Get("/ping") ~> routes ~> check {
        status shouldBe OK
      }
    }

    "get movie info" in new Context {
      val req: Command.GetMovieInfo = GetMovieInfoGen.next
      val res: MovieInfo = MovieInfoGen.next

      probe.setAutoPilot(autopilot(req, res))

      Put("/api/v0.1/movie/info", req) ~> routes ~> check {
        status shouldBe OK
        contentType shouldBe `application/json`
        entityAs[MovieInfo] shouldEqual res
      }
    }
  }

  "register movie" in new Context {
    val req: Command.RegisterMovie = RegisterMovieGen.next

    probe.setAutoPilot(autopilot(req, Done))

    Post("/api/v0.1/movie/create", req) ~> routes ~> check {
      status shouldBe OK
    }
  }

  "reserve seat" in new Context {
    val req: Command.ReserveSeat = ReserveSeatGen.next
    probe.setAutoPilot(autopilot(req, Done))

    Post("/api/v0.1/movie/reserve", req) ~> routes ~> check {
      status shouldBe OK
    }
  }

  "handle already exists cinema.exception" in new Context {
    val req: Command.RegisterMovie = RegisterMovieGen.next
    probe.setAutoPilot(autopilot(req, Status.Failure(MovieAlreadyExistsException)))

    Post("/api/v0.1/movie/create", req) ~> routes ~> check {
      status shouldBe BadRequest
    }
  }

  "handle problems during request parsing" in new Context {

    import spray.json._

    val req: JsValue = """{"screenId":"ekrxv","imdbId":"ziaab","availableSeats":-1}""".parseJson
    Post("/api/v0.1/movie/create", req) ~> Route.seal(routes) ~> check {
      status shouldBe BadRequest
    }
  }

}
