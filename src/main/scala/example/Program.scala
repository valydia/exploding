package example

import cats.effect.{ ExitCode, Sync }
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.apply._

class Program[F[_]](implicit S: Sync[F], C: Console[F], R: Random[F]) {

  val p1 = Player("Player1", None)

  def shuffle(explosiveNum: Int, blankNum: Int): F[List[Card]] = {
    val explosive = List.fill(explosiveNum)(Explosive)
    explosive.foldLeft(S.pure(List.fill[Card](blankNum)(Blank))) {
      case (acc, elem) =>
        for {
          accList <- acc
          idx     <- R.nexInt(accList.length)
        } yield accList.patch(idx, List(elem), 0)
    }
  }

  def pickCard(turn: Turn, player: Player, command: String): Either[String, (Player, Turn)] =
    command match {
      case "d" => turn.drawCard(player)
      case _   => Left("Enter 'd' to pick a card")
    }

  def process(command: Option[String], turn: Turn, player: Player): F[ExitCode] = S.suspend {
    command.fold(S.pure(ExitCode.Success)) { cmd =>
      pickCard(turn, player, cmd) match {
        case Left(err) => C.putStrLn(s"Err: $err") *> mainLoop(turn, player)
        case Right((p, t)) if t.gameState(p) == DrawCard =>
          C.putStrLn(t.display(p)) *> mainLoop(t, p)
        case Right((p, t)) =>
          C.putStrLn(s"Game finished as ${t.gameState(p)} : \n${t.display(p)}") *> S.pure(
            ExitCode.Success
          )
      }
    }
  }

  def mainLoop(turn: Turn, player: Player): F[ExitCode] = S.suspend {
    for {
      _        <- C.putStrLn(s"Draw a card")
      command  <- C.getStrLn()
      exitCode <- process(Option(command), turn, player)
    } yield exitCode
  }

  def run(explosiveNum: Int, blankNum: Int): F[ExitCode] =
    for {
      initialDrawPile <- shuffle(explosiveNum, blankNum)
      exitCode        <- mainLoop(new Turn(initialDrawPile), p1)
    } yield exitCode

}
