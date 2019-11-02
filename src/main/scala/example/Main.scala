package example

import cats.effect._

object Main extends IOApp {

  // Should go to some conf file or could come from user
  val explosiveCardNumber = 1
  val blankCardNumber     = 16

  def run(args: List[String]): IO[ExitCode] = {
    implicit val console = new LiveConsole[IO]()
    implicit val random  = new ScalaRandom[IO]()
    val program          = new Program[IO]()
    program.run(explosiveCardNumber, blankCardNumber).guaranteeCase(_ => IO.unit)
  }

}
