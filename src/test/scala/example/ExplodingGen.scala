package example

import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

trait ExplodingGen extends ScalaCheckDrivenPropertyChecks {

  val cardGen: Gen[Card]           = Gen.frequency((1, Explosive), (2, Defuse), (20, Blank))
  val cardListGen: Gen[List[Card]] = Gen.nonEmptyListOf(cardGen)
  val turnGen: Gen[Turn]           = Gen.nonEmptyListOf(cardGen).map(Turn.apply)

  def playerGen(gen: Gen[List[Card]] = cardListGen): Gen[Player] =
    for {
      name <- Gen.alphaNumStr
      hand <- gen
    } yield Player(name, hand)

}
