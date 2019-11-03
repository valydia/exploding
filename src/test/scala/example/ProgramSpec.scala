package example

import org.scalatest.{ FlatSpecLike, MustMatchers }
import TestEnv._
import org.scalacheck.Gen

class ProgramSpec extends FlatSpecLike with MustMatchers with ExplodingGen {

  def shufflerExplosiveLast(ex: (Int, Card), blank: (Int, Card)): List[Card] = {
    val (exNum, exCard)       = ex
    val (blankNum, blankCard) = blank
    List.fill(blankNum)(blankCard) ++ List.fill(exNum)(exCard)
  }

  "Program" should "run the program until the end if the user press enough times 'd' " in {
    forAll(Gen.chooseNum(17, 100)) { inputNumber =>
      val initialState = TestEnv(List.fill(inputNumber)("d"), List(), List(shufflerExplosiveLast))
      val finalState =
        new Program[Test].run(1, 16).runS(initialState).unsafeRunSync()

      finalState.showLast must startWith("Game finished as Loser(Player1) ")
    }
  }

  // This one is more to show case what IO monad offers
  // in our case we deal with deck cards (so number under 52) so it's not a requirement
  it should "be stack safe" ignore {
    val inputNumber  = 50000
    val initialState = TestEnv(List.fill(inputNumber)("d"), List(), List(shufflerExplosiveLast))
    val finalState =
      new Program[Test].run(1, inputNumber - 1).runS(initialState).unsafeRunSync()

    finalState.showLast must startWith("Game finished as Loser(Player1) ")
  }

}
