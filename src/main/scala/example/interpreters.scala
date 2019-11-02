package example

import cats.effect.Sync

import scala.io.StdIn

class LiveConsole[F[_]](implicit S: Sync[F]) extends Console[F] {

  override def putString(output: String): F[Unit] = S.delay(println(output))

  override def get(): F[String] = S.delay(StdIn.readLine())
}
