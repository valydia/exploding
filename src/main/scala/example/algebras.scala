package example

trait Console[F[_]] {

  def putStrLn(output: String): F[Unit]

  def getStrLn(): F[String]
}

trait Random[F[_]] {

  def nexInt(upper: Int): F[Int]

}
