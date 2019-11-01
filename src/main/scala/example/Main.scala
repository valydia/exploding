package example

import cats.effect._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val program = new Program[IO]()
    program.run(args).guaranteeCase(_ => IO.unit)
  }
}
