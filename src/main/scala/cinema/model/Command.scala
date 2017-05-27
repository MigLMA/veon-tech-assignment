package cinema.model

import cinema.model.Protocol.{ImdbId, ScreenId}

/**
  * Created by vans239 on 22/05/17.
  */
object Command {
  case class RegisterMovie(screenId: ScreenId, imdbId: ImdbId, availableSeats: Int) {
    require(availableSeats >= 0, "Negative available seats")
  }

  case class ReserveSeat(screenId: ScreenId, imdbId: ImdbId)

  case class GetMovieInfo(screenId: ScreenId, imdbId: ImdbId)
}

object Protocol {
  type ImdbId = String
  type ScreenId = String

  case class MovieInfo(screenId: ScreenId, imdbId: ImdbId, availableSeats: Int, reservedSeats: Int) {
    require(availableSeats >= reservedSeats, "Reserved more than available")
    require(reservedSeats >= 0, "Negative reserved seats")

    def withReserved: MovieInfo = copy(reservedSeats = reservedSeats + 1)
  }
}