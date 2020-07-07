package picta.common

import ujson.{Value, read}

trait Semigroup[T] {
  def combine(x: T, y: T): T
}

trait Monoid[T] extends Semigroup[T] {
  def empty: T
}

object Monoid {
  implicit val JsonMonoid: Monoid[Value] = new Monoid[Value] {
    def combine(x: Value, y: Value): Value = x.obj ++ y.obj
    def empty: Value = read("{}")
  }

  implicit class JsonMonoid[T](val x: T)(implicit m: Monoid[T]) {
    def |+|(y: T): T = m.combine(x, y)
  }
}