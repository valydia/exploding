package example

import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

trait ExplodingGen extends ScalaCheckDrivenPropertyChecks {

  val cardGen: Gen[Card] = Gen.frequency((1, Explosive), (9, Blank))
  val turnGen: Gen[Turn] = Gen.nonEmptyListOf(cardGen).map(Turn.apply)

  def playerGen(gen: Gen[Option[Card]] = cardGen.map(Option.apply)): Gen[Player] =
    for {
      name <- Gen.alphaNumStr
      hand <- gen
    } yield Player(name, hand)

}
