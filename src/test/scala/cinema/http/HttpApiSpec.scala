package cinema.http

import akka.Done
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{TestKitBase, TestProbe}
import org.scalatest.{Matchers, WordSpecLike}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import cinema.util.Threads

import scala.concurrent.duration._
import akka.http.scaladsl.model.ContentTypes._

import scala.concurrent.Future
import akka.actor.Status
import cinema.Generators._
import cinema.exception.MovieAlreadyExistsException
import cinema.model.Command
import cinema.model.Protocol.MovieInfo
import cinema.service.ActorMovieService
import cinema.util.GeneratorUtils._

/**
  * Created by vans239 on 27/05/17.
  */
class HttpApiSpec
  extends WordSpecLike with ScalatestRouteTest with TestKitBase with Matchers {
  import JsonProtocol._

  trait Context {
    val timeout: FiniteDuration = 1.second
    val probe: TestProbe = TestProbe()
    val routes: Route = new HttpApi(new ActorMovieService(probe.ref)(Threads.callingThreadEC, timeout)).routes

  }

  "HttpApi" should {
    "ping" in new Context {
      Get("/ping") ~> routes ~> check {
        status shouldBe OK
      }
    }

    //todo fix race conditions with futures

    "get movie info" in new Context {
      val req: Command.GetMovieInfo = GetMovieInfoGen.next
      val res: MovieInfo = MovieInfoGen.next
      Future {
        probe.expectMsg(500.millis, req)
        probe.reply(res)
      }(system.dispatcher)
      Put("/api/v0.1/movie/info", req) ~> routes ~> check {
        status shouldBe OK
        contentType shouldBe `application/json`
        entityAs[MovieInfo] shouldEqual res
      }
    }

    "register movie" in new Context {
      val req: Command.RegisterMovie = RegisterMovieGen.next
      Future {
        probe.expectMsg(500.millis, req)
        probe.reply(Done)
      }(system.dispatcher)
      Post("/api/v0.1/movie/create", req) ~> routes ~> check {
        status shouldBe OK
      }
    }

    "reserve seat" in new Context {
      val req: Command.ReserveSeat = ReserveSeatGen.next
      Future {
        probe.expectMsg(500.millis, req)
        probe.reply(Done)
      }(system.dispatcher)
      Post("/api/v0.1/movie/reserve", req) ~> routes ~> check {
        status shouldBe OK
      }
    }

    "handle already exists cinema.exception" in new Context {
      val req: Command.RegisterMovie = RegisterMovieGen.next
      Future {
        probe.expectMsg(500.millis, req)
        probe.reply(Status.Failure(MovieAlreadyExistsException))
      }(system.dispatcher)
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

}
