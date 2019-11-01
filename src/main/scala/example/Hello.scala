package example

import cats.effect._

import scala.io.StdIn

object Hello extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      _    <- IO(println("Welcome to Exploding 💣💥!!!\nPlease enter your name"))
      name <- IO(StdIn.readLine())
      _    <- IO(println(s"Welcome $name"))
    } yield ExitCode.Success
}
