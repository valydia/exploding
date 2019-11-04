package example

import org.scalatest.{ FlatSpecLike, MustMatchers }
import TestEnv._
import org.scalacheck.Gen

class ProgramSpec extends FlatSpecLike with MustMatchers with ExplodingGen {

  // We could provide more exhausting testing with different shuffling stub
  // We put the Explosive card before the Defuse one, otherwise we end up with a
  // very very ... long game
  def shufflerExplosiveSecondLast(cardList: List[Card]): List[Card] = {
    val sortedCards = cardList.groupBy(identity)
    sortedCards.getOrElse(Blank, Nil) ++ sortedCards
      .getOrElse(Explosive, Nil) ++ sortedCards.getOrElse(Defuse, Nil)
  }

  "Program" should "run the program until the end if the user press enough times 'd' " in {
    forAll(Gen.chooseNum(60, 100)) { inputNumber =>
      val initialState =
        TestEnv(List.fill(inputNumber)("d"), List(), List.fill(100)(shufflerExplosiveSecondLast))
      val finalState =
        new Program[Test].run(1, 2, 16).runS(initialState).unsafeRunSync()

      finalState.showLast must startWith("Game finished as Loser(Player1) ")
    }
  }

  // This one is more to show case what IO monad offers
  // in our case we deal with deck cards (so number under 52) so it's not a requirement
  it should "be stack safe" ignore {
    val inputNumber = 50000
    val initialState =
      TestEnv(
        List.fill(inputNumber)("d"),
        List(),
        List.fill(inputNumber)(shufflerExplosiveSecondLast)
      )
    val finalState =
      new Program[Test].run(1, 2, inputNumber - 3).runS(initialState).unsafeRunSync()

    finalState.showLast must startWith("Game finished as Loser(Player1) ")
  }

}
