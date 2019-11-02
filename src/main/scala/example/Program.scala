package example

import cats.effect.{ ExitCode }

class Program[F[_]] {

  val p1 = Player("Player1", None)

  def mainLoop(turn: Turn, player: Player): F[ExitCode] = ???

  def run(): F[ExitCode] =
    mainLoop(new Turn(), p1)

}
