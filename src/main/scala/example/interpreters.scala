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

  def shuffle(x: (Int, Card), y: (Int, Card)): F[List[Card]] = {
    val (explosiveNum, exCard) = x
    val (blankNum, exBlank)    = y
    val explosive              = List.fill(explosiveNum)(exCard)
    explosive.foldLeft(S.pure(List.fill(blankNum)(exBlank))) {
      case (acc, elem) =>
        for {
          accList <- acc
          idx     <- randomInt(accList.length)
        } yield accList.patch(idx, List(elem), 0)
    }
  }
}
