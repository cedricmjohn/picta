package org.carbonateresearch.picta.common

import ujson.{Value, read}

private[picta] trait Semigroup[T] {
  def combine(x: T, y: T): T
}

private[picta] trait Monoid[T] extends Semigroup[T] {
  def empty: T
}

private[picta] object Monoid {
  /** implicit value that allows Value types to be manipulated as Monoids */
  implicit val jsonMonoid: Monoid[Value] = new Monoid[Value] {
    def combine(x: Value, y: Value): Value = x.obj ++ y.obj

    def empty: Value = read("{}")
  }

  /** implicit class adding syntactic sugar for properties of a Monoid
   *
   * @param x : this is the original Value type object
   */
  implicit class jsonMonoid[T](val x: T)(implicit m: Monoid[T]) {
    /** syntactic sugar for the combine property of a Monoid
     *
     * @param y : this is the other Value type object we wish to combine with
     */
    def |+|(y: T): T = m.combine(x, y)
  }
}