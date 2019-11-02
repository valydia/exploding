package example

import org.scalatest.{ FlatSpecLike, MustMatchers }
import TestEnv._
import org.scalacheck.Gen

class ProgramSpec extends FlatSpecLike with MustMatchers with ExplodingGen {

  "Program" should "run the program until the end if the user press enough times 'd' " in {
    forAll(Gen.chooseNum(17, 100)) { inputNumber =>
      val initialState = TestEnv(List.fill(inputNumber)("d"), List(), (1 to inputNumber).toList)
      val finalState =
        new Program[Test].run(1, 16).runS(initialState).unsafeRunSync()

      finalState.showLast must startWith("Game finished as Loser(Player1) ")
    }
  }

  // This one is more to show case what IO monad offers
  // in our case we deal with deck cards (so number under 52) so it's not a requirement
  it should "be stack safe" ignore {
    val inputNumber  = 50000
    val initialState = TestEnv(List.fill(inputNumber)("d"), List(), (inputNumber to 1 by -1).toList)
    val finalState =
      new Program[Test].run(1, inputNumber - 1).runS(initialState).unsafeRunSync()

    finalState.showLast must startWith("Game finished as Loser(Player1) ")
  }

}
