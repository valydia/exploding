package example

import cats.data.StateT
import cats.effect.IO
import cats.mtl.MonadState
import cats.syntax.flatMap._
import cats.syntax.functor._

case class TestEnv(
  inputs: List[String],
  outputs: List[String],
  shufflers: List[List[Card] => List[Card]]
) {

  def putStrLn(output: String): TestEnv = copy(outputs = output :: outputs)

  def getStrLn(): (TestEnv, String) = (copy(inputs = inputs.tail), inputs.head)

  def shuffle(cardList: List[Card]): (TestEnv, List[Card]) =
    (copy(shufflers = shufflers.tail), shufflers.head(cardList))

  def showLast: String = outputs.head

}

class TestConsole[M[_]: MonadState[?[_], TestEnv]] extends Console[M] {

  def MS: MonadState[M, TestEnv] = implicitly
  implicit def M                 = MS.monad

  override def putStrLn(output: String): M[Unit] =
    MS.modify(_.putStrLn(output))

  override def getStrLn(): M[String] =
    for {
      tuple <- MS.inspect(_.getStrLn())
      (st, input) = tuple
      _ <- MS.set(st)
    } yield input
}

class TestShuffle[M[_]: MonadState[?[_], TestEnv]] extends Shuffler[M] {

  def MS: MonadState[M, TestEnv] = implicitly
  implicit def M                 = MS.monad

  override def shuffle(cardList: List[Card]): M[List[Card]] =
    for {
      tuple <- MS.inspect(_.shuffle(cardList))
      (st, input) = tuple
      _ <- MS.set(st)
    } yield input
}

object TestEnv {

  implicit val consoleIO: Console[Test] = new Console[Test] {
    override def putStrLn(output: String): Test[Unit] =
      StateT.modify(_.putStrLn(output))

    override def getStrLn(): Test[String] =
      for {
        tuple <- StateT.inspect[IO, TestEnv, (TestEnv, String)](_.getStrLn())
        (st, input) = tuple
        _ <- StateT.set[IO, TestEnv](st)
      } yield input
  }

  implicit val shufflerIO: Shuffler[Test] = new Shuffler[Test] {

    override def shuffle(cardList: List[Card]): Test[List[Card]] = {
      for {
        tuple <- StateT.inspect[IO, TestEnv, (TestEnv, List[Card])](_.shuffle(cardList))
        (st, list) = tuple
        _ <- StateT.set[IO, TestEnv](st)
      } yield list
    }
  }

}
