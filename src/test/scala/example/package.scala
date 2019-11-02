import cats.data.StateT
import cats.effect.IO

package object example {

  type Test[A] = StateT[IO, TestEnv, A]
}
