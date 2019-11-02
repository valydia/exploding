package example

import cats.data.StateT
import cats.effect.IO
import cats.mtl.MonadState
import cats.syntax.flatMap._
import cats.syntax.functor._

case class TestEnv(inputs: List[String], outputs: List[String]) {

  def putString(output: String): TestEnv = copy(outputs = output :: outputs)

  def get(): (TestEnv, String) = (copy(inputs = inputs.tail), inputs.head)

  def showLast: String = outputs.head

}

class TestConsole[M[_]: MonadState[?[_], TestEnv]] extends Console[M] {

  def MS: MonadState[M, TestEnv] = implicitly
  implicit def M                 = MS.monad

  override def putString(output: String): M[Unit] =
    MS.modify(_.putString(output))

  override def get(): M[String] =
    for {
      tuple <- MS.inspect(_.get())
      (st, input) = tuple
      _ <- MS.set(st)
    } yield input

}

object TestEnv {

  implicit val consoleIO: Console[Test] = new Console[Test] {
    override def putString(output: String): Test[Unit] =
      StateT.modify(_.putString(output))

    override def get(): Test[String] =
      for {
        tuple <- StateT.inspect[IO, TestEnv, (TestEnv, String)](_.get())
        (st, input) = tuple
        _ <- StateT.set[IO, TestEnv](st)
      } yield input
  }
}
