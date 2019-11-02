package example

import cats.effect.{ ExitCode, Sync }
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.apply._

class Program[F[_]](implicit S: Sync[F], C: Console[F]) {

  val p1 = Player("Player1", None)

  def pickCard(turn: Turn, player: Player, command: String): Either[String, (Player, Turn)] =
    command match {
      case "d" => turn.drawCard(player)
      case _   => Left("Enter d to pick a card")
    }

  def process(command: Option[String], turn: Turn, player: Player): F[ExitCode] = S.suspend {
    command.fold(S.pure(ExitCode.Success)) { cmd =>
      pickCard(turn, player, cmd) match {
        case Left(err) => S.delay(C.putString(s"Err: $err")) *> mainLoop(turn, player)
        case Right((p, t)) if t.gameState(p) == DrawCard =>
          S.delay(println(t.display(p))) *> mainLoop(t, p)
        case Right((p, t)) =>
          S.delay(C.putString(s"Game finished as ${t.gameState(p)} : \n${t.display(p)}")) *> S.pure(
            ExitCode.Success
          )
      }
    }
  }

  def mainLoop(turn: Turn, player: Player): F[ExitCode] = S.suspend {
    for {
      _        <- C.putString(s"Draw a card")
      command  <- C.get()
      exitCode <- process(Option(command), turn, player)
    } yield exitCode
  }

  def run(): F[ExitCode] =
    mainLoop(new Turn(), p1)

}
