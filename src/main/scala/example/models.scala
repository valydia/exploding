package example

import scala.util.Random

case class Player(name: String, hand: Option[Card])

sealed trait Card
case object Explosive extends Card
case object Blank     extends Card

sealed trait State
case object DrawCard           extends State
case class Loser(name: String) extends State

case class Turn(drawPile: List[Card] = Turn.shuffle(1, 16)) {

  def drawCard(player: Player): Either[String, (Player, Turn)] =
    drawPile match {
      case h :: tail => Right((player.copy(hand = Some(h)), Turn(tail)))
      case _         => Left("Empty deck")
    }

  def display(p: Player): String = {
    def display(card: Option[Card]): String =
      card match {
        case None        => "Empty"
        case Some(Blank) => "B"
        case _           => "E"
      }
    s"""|Player's Hand:
        |${display(p.hand)}
        |Deck:
        |${List.fill(drawPile.length)("X").mkString("")}
     """.stripMargin
  }

  def gameState(p: Player): State =
    if (p.hand.contains(Explosive))
      Loser(p.name)
    else
      DrawCard

}

object Turn {

  def shuffle(explosiveNum: Int, blankNum: Int): List[Card] = {
    val explosive = List.fill(explosiveNum)(Explosive)
    explosive.foldLeft(List.fill[Card](blankNum)(Blank)) {
      case (acc, elem) =>
        val randomIndex = Random.nextInt(acc.length)
        acc.patch(randomIndex, List(elem), 0)
    }
  }

}
