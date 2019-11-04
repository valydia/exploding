package example

case class Player(name: String, private val hand: List[Card]) {
  def pickCard(c: Card): Player = copy(hand = c :: hand)
  def displayHand: String =
    hand match {
      case Nil => "Empty"
      case l => l.map(_.display).mkString(", ")
    }
  def newestCard: Option[Card] = hand.headOption
  def hasDefuse: Boolean       = hand.contains(Defuse)

  // Drop explosive card - (that is at the head given the rules) and defuse card
  def drop: Player = {
    // Unit test
    def dropCard(c: Card, list: List[Card]): List[Card] = {
      val (l1, l2) = list.span(_ != c)
      l1 ++ (if (l2.nonEmpty) l2.tail else Nil)
    }

    val newHand = dropCard(Defuse, dropCard(Explosive, hand))
    copy(hand = newHand)

  }

}

sealed trait Card {
  def display: String
}
case object Explosive extends Card {
  val display = "E"
}
case object Defuse extends Card {
  val display = "D"
}
case object Blank extends Card {
  val display = "B"
}

sealed trait State
case object DrawCard extends State
case object Discard extends State
case class Loser(name: String) extends State

case class Turn(drawPile: List[Card]) {

  def drawCard(player: Player): Either[String, (Player, Turn)] =
    drawPile match {
      case h :: tail => Right((player.pickCard(h), Turn(tail)))
      case _ => Left("Empty deck") // should not happen with current set of rules
    }

  def handleDiscard(player: Player): (Player, Turn) =
    (player.drop, copy(Explosive :: drawPile))

  def display(p: Player): String = {

    s"""|Player's Hand:
        |${p.displayHand}
        |Deck:
        |${List.fill(drawPile.length)("X").mkString("")}
     """.stripMargin
  }

  def gameState(p: Player): State = {
    p.newestCard match {
      case Some(Blank) | Some(Defuse) | None => DrawCard
      case Some(Explosive) if p.hasDefuse => Discard
      case Some(Explosive) => Loser(p.name)
    }
  }

}
