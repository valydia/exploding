package example

import cats.effect.{ExitCode, Sync}

class Program[F[_]](implicit S: Sync[F]) {

  val p1 = Player("Player1", None)

  def mainLoop(turn: Turn, player: Player): F[ExitCode] = ???

  def run(args: List[String]): F[ExitCode] =
    mainLoop(new Turn(), p1)

}
