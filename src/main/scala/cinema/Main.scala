package cinema

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import cinema.actor.{MovieManager, MoviesManager}
import cinema.http.HttpApi
import cinema.service.ActorMovieService
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.util.control.NonFatal
import scala.concurrent.duration._

/**
  * Created by vans239 on 27/05/17.
  */
object Main extends App {
  try {
    Thread.setDefaultUncaughtExceptionHandler(new cinema.util.DefaultUncaughtExceptionHandler)

    implicit val system = ActorSystem()

    sys.addShutdownHook {
      import scala.concurrent.duration._
      Await.result(system.terminate(), 5.seconds)
    }

    import system.dispatcher
    implicit val materializer = ActorMaterializer()

    val config = ConfigFactory.load()

    val movieService = {
      implicit val timeout = 1.second
      val managerActor = system.actorOf(MoviesManager.props(MovieManager.props()), "movies")
      new ActorMovieService(managerActor)
    }

    val httpApi = new HttpApi(movieService)

    Http().bindAndHandle(httpApi.routes, config.getString("cinema.http.interface"), config.getInt("cinema.http.port"))
  } catch {
    case NonFatal(e) =>
      System.err.println(s"Got ${e.getMessage} during initialization. Exiting...")
      e.printStackTrace(System.err)
      sys.exit(2)
  }

}
