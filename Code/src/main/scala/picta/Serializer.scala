package picta

import ujson.Value
import upickle.default._

/** A type class that serializes scala data structures to a valid Value format for JSON. */
sealed trait Serializer[T] {
  def serialize(lst: List[T]): Value
}

/** Traits are prioritised to allow the compiler to compile without any ambiguity errors */
trait SerializerA {

  implicit object BigDecimalSerializer extends Serializer[BigDecimal] {
    def serialize(lst: List[BigDecimal]): Value = transform(lst).to(Value)
  }

}

trait SerializerB extends SerializerA {

  implicit object LongSerializer extends Serializer[Long] {
    def serialize(lst: List[Long]): Value = transform(lst).to(Value)
  }

}

trait SerializerC extends SerializerB {

  implicit object FloatSerializer extends Serializer[Float] {
    def serialize(lst: List[Float]): Value = transform(lst).to(Value)
  }

}

trait SerializerD extends SerializerC {

  implicit object StringSerializer extends Serializer[String] {
    def serialize(lst: List[String]): Value = transform(lst).to(Value)
  }

}

trait SerializerE extends SerializerD {

  implicit object IntSerializer extends Serializer[Int] {
    def serialize(lst: List[Int]): Value = transform(lst).to(Value)
  }

}

object Serializer extends SerializerE {

  implicit object DoubleSerializer extends Serializer[Double] {
    def serialize(lst: List[Double]): Value = transform(lst).to(Value)
  }

}