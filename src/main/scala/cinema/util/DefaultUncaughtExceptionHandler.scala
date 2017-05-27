package cinema.util

import java.lang.Thread.UncaughtExceptionHandler

/**
  * Created by vans239 on 27/05/17.
  */
class DefaultUncaughtExceptionHandler extends UncaughtExceptionHandler {
  def uncaughtException(t: Thread, e: Throwable) {
    System.err.println(s"Received uncaught error from thread <$t>")
    e.printStackTrace()
    e match {
      case _: OutOfMemoryError =>
        System.err.println("OOM. Exiting...")
        sys.runtime.halt(1)
      case _ =>
      // do nothing
    }
  }
}
