package example

import org.scalatest.{ FlatSpecLike, MustMatchers }
import TestEnv._
import org.scalacheck.Gen

class ProgramSpec extends FlatSpecLike with MustMatchers with ExplodingGen {

  "Program" should "run the program until the end if the user press enough times 'd' " in {
    forAll(Gen.chooseNum(17, 100)) { inputNumber =>
      val initialState = TestEnv(List.fill(inputNumber)("d"), List())
      val finalState =
        new Program[Test].run().runS(initialState).unsafeRunSync()

      finalState.showLast must startWith("Game finished as Loser(Player1) ")
    }
  }

}
