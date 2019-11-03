package example

trait Console[F[_]] {

  def putStrLn(output: String): F[Unit]

  def getStrLn(): F[String]
}

trait Shuffler[F[_]] {

  def shuffle(x: (Int, Card), y: (Int, Card)): F[List[Card]]

}
