package example

case class Player(name: String, hand: Option[Card])

sealed trait Card
case object Explosive extends Card
case object Blank     extends Card

sealed trait State
case object DrawCard           extends State
case class Loser(name: String) extends State

case class Turn(drawPile: List[Card] = Turn.shuffle(1, 16)) {

  def drawCard: Turn = ???

  def display(p: Player): String = ???

  def gameState(p: Player): State = ???

}

object Turn {

  def shuffle(explosiveNum: Int, blankNum: Int): List[Card] = ???

}
