package picta.common

import ujson.{Value, read}

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  implicit val JsonMonoid: Monoid[Value] = new Monoid[Value] {
    def combine(x: Value, y: Value): Value = x.obj ++ y.obj
    def empty: Value = read("{}")
  }

  implicit class JsonMonoid[A](val x: A) {
    def |+|(y: A)(implicit m: Monoid[A]): A = m.combine(x, y)
  }
}



