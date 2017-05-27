package cinema.util

import scala.concurrent.ExecutionContext

/**
  * Created by vans239 on 27/05/17.
  */
object Threads {
  val callingThreadEC: ExecutionContext = new ExecutionContext {
    override def execute(runnable: Runnable): Unit = runnable.run()

    override def reportFailure(cause: Throwable): Unit = {}
  }

  object Implicits {
    implicit val ec: ExecutionContext = callingThreadEC
  }
}
