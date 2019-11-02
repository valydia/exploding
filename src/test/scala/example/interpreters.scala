package example

import cats.data.StateT
import cats.effect.IO
import cats.mtl.MonadState
import cats.syntax.flatMap._
import cats.syntax.functor._

case class TestEnv(inputs: List[String], outputs: List[String], nextInts: List[Int]) {

  def putStrLn(output: String): TestEnv = copy(outputs = output :: outputs)

  def getStrLn(): (TestEnv, String) = (copy(inputs = inputs.tail), inputs.head)

  def nextInt(): (TestEnv, Int) = (copy(nextInts = nextInts.tail), nextInts.head)

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

class TestRandom[M[_]: MonadState[?[_], TestEnv]] extends Random[M] {

  def MS: MonadState[M, TestEnv] = implicitly
  implicit def M                 = MS.monad

  override def nexInt(upper: Int): M[Int] =
    for {
      tuple <- MS.inspect(_.nextInt())
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

  implicit val randomIO: Random[Test] = new Random[Test] {
    override def nexInt(upper: Int): Test[Int] =
      for {
        tuple <- StateT.inspect[IO, TestEnv, (TestEnv, Int)](_.nextInt())
        (st, input) = tuple
        _ <- StateT.set[IO, TestEnv](st)
      } yield input

  }
}
