package org.carbonateresearch.picta.common

import ujson.Value
import upickle.default._

/** A type class that serializes lists to a valid array compatible JSON.
 *
 * Implicits are prioritised to allow the compiler to compile without any ambiguity errors.
 * Implicit priority for serialization: BigDecimal < Long < Float < String < Int < Double.
 */
private[picta] sealed trait Serializer[T] {
  def serialize(lst: List[T]): Value
}

/** serializes a list of BigDecimals. */
private[picta] trait Big_Decimal_Serializer {

  implicit object BigDecimalSerializer extends Serializer[BigDecimal] {
    def serialize(lst: List[BigDecimal]): Value = transform(lst).to(Value)
  }

}

/** serializes a list of Longs. */
private[picta] trait Long_Serializer extends Big_Decimal_Serializer {

  implicit object LongSerializer extends Serializer[Long] {
    def serialize(lst: List[Long]): Value = transform(lst).to(Value)
  }

}

/** serializes a list of Floats. */
private[picta] trait Float_Serializer extends Long_Serializer {

  implicit object FloatSerializer extends Serializer[Float] {
    def serialize(lst: List[Float]): Value = transform(lst).to(Value)
  }

}

/** serializes a list of Strings. */
private[picta] trait String_Serializer extends Float_Serializer {

  implicit object StringSerializer extends Serializer[String] {
    def serialize(lst: List[String]): Value = transform(lst).to(Value)
  }

}

/** serializes a list of Ints. */
private[picta] trait Int_Serializer extends String_Serializer {

  implicit object IntSerializer extends Serializer[Int] {
    def serialize(lst: List[Int]): Value = transform(lst).to(Value)
  }

}

/** serializes a list of Doubles. */
private[picta] object Serializer extends Int_Serializer {

  implicit object DoubleSerializer extends Serializer[Double] {
    def serialize(lst: List[Double]): Value = transform(lst).to(Value)
  }

}