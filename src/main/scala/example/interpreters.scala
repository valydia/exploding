package example

import cats.effect.Sync

import scala.io.StdIn
import scala.util.{ Random => SRandom }

class LiveConsole[F[_]](implicit S: Sync[F]) extends Console[F] {

  override def putStrLn(output: String): F[Unit] = S.delay(println(output))

  override def getStrLn(): F[String] = S.delay(StdIn.readLine())
}

class ScalaRandom[F[_]](implicit S: Sync[F]) extends Random[F] {

  override def nexInt(upper: Int): F[Int] = S.delay(SRandom.nextInt)

}
