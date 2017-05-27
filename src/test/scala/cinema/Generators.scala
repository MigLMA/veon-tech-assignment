package cinema

import model.Command.{GetMovieInfo, RegisterMovie, ReserveSeat}
import model.Protocol.{ImdbId, MovieInfo, ScreenId}
import org.scalacheck.Gen

/**
  * Created by vans239 on 27/05/17.
  */
object Generators {
  val ImdbIdGen: Gen[ImdbId] = Gen.alphaStr.map(_.take(5))
  val ScreenIdGen: Gen[ScreenId] = Gen.alphaStr.map(_.take(5))

  val RegisterMovieGen: Gen[RegisterMovie] = for {
    imdbId <- ImdbIdGen
    screenId <- ScreenIdGen
    availableSeats <- Gen.choose(1, 100)
  } yield RegisterMovie(imdbId, screenId, availableSeats)

  val ReserveSeatGen: Gen[ReserveSeat] = for {
    imdbId <- ImdbIdGen
    screenId <- ScreenIdGen
  } yield ReserveSeat(imdbId, screenId)

  val GetMovieInfoGen: Gen[GetMovieInfo] = for {
    imdbId <- ImdbIdGen
    screenId <- ScreenIdGen
  } yield GetMovieInfo(imdbId, screenId)

  val MovieInfoGen: Gen[MovieInfo] = for {
    imdbId <- ImdbIdGen
    screenId <- ScreenIdGen
    availableSeats <- Gen.choose(1, 100)
    reserved <- Gen.choose(0, availableSeats)
  } yield MovieInfo(imdbId, screenId, availableSeats, reserved)
}
