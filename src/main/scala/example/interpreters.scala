package example

import cats.effect.Sync

import scala.io.StdIn
import scala.util.{ Random => SRandom }
import cats.syntax.flatMap._
import cats.syntax.functor._

class LiveConsole[F[_]](implicit S: Sync[F]) extends Console[F] {

  override def putStrLn(output: String): F[Unit] = S.delay(println(output))

  override def getStrLn(): F[String] = S.delay(StdIn.readLine())
}

class RandomShuffler[F[_]](implicit S: Sync[F]) extends Shuffler[F] {

  private def randomInt(upperInt: Int): F[Int] =
    S.delay(SRandom.nextInt(upperInt))

  private def shuffle(cardList1: List[Card], cardList2: List[Card]): F[List[Card]] = {
    cardList1.foldLeft(S.pure(cardList2)) {
      case (acc, elem) =>
        for {
          accList <- acc
          idx     <- randomInt(accList.length)
        } yield accList.patch(idx, List(elem), 0)
    }
  }

  def shuffle(cardList: List[Card]): F[List[Card]] = {
    val (l1, l2) = cardList.splitAt(cardList.length / 2)
    shuffle(l1, l2)
  }

}
