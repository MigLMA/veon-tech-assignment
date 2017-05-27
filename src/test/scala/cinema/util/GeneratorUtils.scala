package cinema.util

import org.scalacheck.Gen

/**
  * Created by vans239 on 27/05/17.
  */
object GeneratorUtils {

  implicit class RichGen[T](g: Gen[T]) {
    def next: T = g.sample.get
  }

}
