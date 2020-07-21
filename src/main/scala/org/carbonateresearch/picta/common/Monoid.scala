package org.carbonateresearch.picta.common

import ujson.{Value, read}

/** A trait which specifies a generic Semigroup. */
private[picta] trait Semigroup[T] {
  def combine(x: T, y: T): T
}

/** A trait which extends the Semigroup trait to specify a generic Monoid. */
private[picta] trait Monoid[T] extends Semigroup[T] {
  def empty: T
}

/** This object holds the implicit json Monoid used in the library to aggregate components using the combine method. */
private[picta] object Monoid {
  implicit val jsonMonoid: Monoid[Value] = new Monoid[Value] {
    def combine(x: Value, y: Value): Value = x.obj ++ y.obj
    def empty: Value = read("{}")
  }

  /** implicit class adding syntactic sugar for combine method.
   *
   * @param x : this is the original Value type object
   */
  implicit class jsonMonoid[T](val x: T)(implicit m: Monoid[T]) {
    /** syntactic sugar for the combine property of a Monoid
     *
     * @param y : this is the other Value type object we wish to combine 'x' with
     */
    def |+|(y: T): T = m.combine(x, y)
  }
}