package example

import cats.effect._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    implicit val console = new LiveConsole[IO]()
    val program          = new Program[IO]()
    program.run().guaranteeCase(_ => IO.unit)
  }
}
