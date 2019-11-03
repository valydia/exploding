package example

trait Console[F[_]] {

  def putStrLn(output: String): F[Unit]

  def getStrLn(): F[String]
}

trait Shuffler[F[_]] {

  def shuffle(cardList: List[Card]): F[List[Card]]

}
