package example

import org.scalacheck.Gen
import org.scalatest.{ FlatSpecLike, MustMatchers }

class ModelsSpec extends FlatSpecLike with MustMatchers with ExplodingGen {

  "Models" should "draw card for non-empty Turn" in {
    forAll(turnGen, playerGen()) { (turn, player) =>
      val Right((newPlayer, newTurn)) = turn.drawCard(player)
      newPlayer.name mustBe player.name
      newTurn.drawPile.length mustBe turn.drawPile.length - 1
    }
  }

  // Corner case empty Turn - shouldn't happen
  it should "draw card for empty Turn" in {
    forAll(Gen.const(List.empty[Card]).map(Turn.apply), playerGen()) { (turn, player) =>
      turn.drawCard(player) must be leftSideValue
    }
  }

  // Expect 5 lines
  // Line 1 - Player's Hand:
  // Line 2 - card - Empty / B / E
  // Line 3 - Deck:
  // Line 4 - XXXXXX (1 X per Card)
  // Line 5 - blank line
  it should "display Turn" in {
    forAll(turnGen, playerGen()) { (turn, player) =>
      val display = turn.display(player)
      val lines   = display.split("\n")
      lines must have length 5
      lines.head mustBe "Player's Hand:"
      lines(2) mustBe "Deck:"
      List(lines(1))  must contain oneOf ("Empty", "B", "E")
      lines(3).toList must contain only 'X'
      lines(3).length mustBe turn.drawPile.length
    }
  }

  "State" should "for player with no hand or Blank card should be DrawCard" in {
    forAll(turnGen, playerGen(Gen.oneOf(None, Some(Blank)))) { (turn, player) =>
      turn.gameState(player) mustBe DrawCard
    }
  }

}
