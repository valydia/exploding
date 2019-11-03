package example

import cats.effect.IO
import org.scalacheck.Gen
import org.scalatest.{ FlatSpecLike, MustMatchers }

class RandomShufflerSpec extends FlatSpecLike with MustMatchers with ExplodingGen {

  val shuffler = new RandomShuffler[IO]()
  "RandomShuffler" should "shuffle deck" in {
    forAll(Gen.chooseNum(1, 32), Gen.chooseNum(1, 23)) { (i1: Int, i2: Int) =>
      val res = shuffler.shuffle((i1, Explosive), (i2, Blank)).unsafeRunSync()
      res.length mustBe i1 + i2
      res.count(_ == Explosive) mustBe i1
      res.count(_ == Blank) mustBe i2
    }
  }

}
