package example

import cats.effect.{ ExitCode, Sync }
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.apply._

class Program[F[_]](implicit S: Sync[F], C: Console[F], SH: Shuffler[F]) {

  val p1 = Player("Player1", List(Defuse))

  def pickCard(turn: Turn, player: Player, command: String): Either[String, (Player, Turn)] =
    command match {
      case "d" => turn.drawCard(player)
      case _ => Left("Enter 'd' to pick a card")
    }

  def shuffle(turn: Turn): F[Turn] = {
    SH.shuffle(turn.drawPile).map(cards => turn.copy(cards))
  }

  def process(command: Option[String], turn: Turn, player: Player): F[ExitCode] = S.suspend {
    command.fold(S.pure(ExitCode.Success)) { cmd =>
      pickCard(turn, player, cmd) match {
        case Left(err) => C.putStrLn(s"Err: $err") *> mainLoop(turn, player)
        case Right((p, t)) if t.gameState(p) == DrawCard =>
          C.putStrLn(t.display(p)) *> mainLoop(t, p)
        case Right((p, t)) if t.gameState(p) == Discard =>
          val (np, nt) = t.handleDiscard(p)
          shuffle(nt).flatMap { shuffledTurn =>
            C.putStrLn(shuffledTurn.display(np)) *> mainLoop(shuffledTurn, np)
          }
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

  def buildDeck(explosiveNum: Int, defuseNum: Int, blankNum: Int): List[Card] =
    List.fill(explosiveNum)(Explosive) ++ List.fill(defuseNum)(Defuse) ++ List.fill(blankNum)(Blank)

  def run(explosiveNum: Int, defuseNum: Int, blankNum: Int): F[ExitCode] = {
    val deck = buildDeck(explosiveNum, defuseNum, blankNum)
    for {
      initialDrawPile <- SH.shuffle(deck)
      exitCode        <- mainLoop(Turn(initialDrawPile), p1)
    } yield exitCode
  }

}
