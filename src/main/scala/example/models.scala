package example

case class Player(name: String, hand: Option[Card])

sealed trait Card
case object Explosive extends Card
case object Blank extends Card

sealed trait State
case object DrawCard extends State
case class Loser(name: String) extends State

case class Turn(drawPile: List[Card]) {

  def this() = this(shuffle(1, 16))

  private def shuffle(explosiveNum: Int, blankNum: Int): List[Card] = ???

  def drawCard(p: Player): Turn = ???

  def display: String = ???

  def gameState(p: Player): State = ???

}