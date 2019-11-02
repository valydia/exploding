package example

trait Console[F[_]] {

  def putString(output: String): F[Unit]

  def get(): F[String]
}
