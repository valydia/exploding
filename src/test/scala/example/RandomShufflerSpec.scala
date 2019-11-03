package example

import cats.effect.IO
import org.scalacheck.Gen
import org.scalatest.{ FlatSpecLike, MustMatchers }

class RandomShufflerSpec extends FlatSpecLike with MustMatchers with ExplodingGen {

  val shuffler = new RandomShuffler[IO]()
  "RandomShuffler" should "shuffle deck" in {
    forAll(Gen.chooseNum(1, 32), Gen.chooseNum(1, 23), Gen.chooseNum(1, 23)) {
      (i1: Int, i2: Int, i3: Int) =>
        val cardsList = List.fill(i1)(Explosive) ++ List.fill(i2)(Defuse) ++ List.fill(i3)(Blank)
        val res       = shuffler.shuffle(cardsList).unsafeRunSync()
        res.length mustBe i1 + i2 + i3
        res.count(_ == Explosive) mustBe i1
        res.count(_ == Defuse) mustBe i2
        res.count(_ == Blank) mustBe i3
    }
  }

}
