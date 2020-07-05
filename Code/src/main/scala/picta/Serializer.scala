package picta

import ujson.Value
import upickle.default._

/** A type class that serializes scala data structures to a valid Value format for JSON. */
sealed trait Serializer[T] {
  def serialize(seq: List[T]): Value
}

/** Traits are prioritised to allow the compiler to compile without any ambiguity errors */
trait SerializerA {

  implicit object BigDecimalSerializer extends Serializer[BigDecimal] {
    def serialize(seq: List[BigDecimal]): Value = transform(seq).to(Value)
  }

}

trait SerializerB extends SerializerA {

  implicit object LongSerializer extends Serializer[Long] {
    def serialize(seq: List[Long]): Value = transform(seq).to(Value)
  }

}

trait SerializerC extends SerializerB {

  implicit object FloatSerializer extends Serializer[Float] {
    def serialize(seq: List[Float]): Value = transform(seq).to(Value)
  }

}

trait SerializerD extends SerializerC {

  implicit object StringSerializer extends Serializer[String] {
    def serialize(seq: List[String]): Value = transform(seq).to(Value)
  }

}

trait SerializerE extends SerializerD {

  implicit object IntSerializer extends Serializer[Int] {
    def serialize(seq: List[Int]): Value = transform(seq).to(Value)
  }

}

object Serializer extends SerializerE {

  implicit object DoubleSerializer extends Serializer[Double] {
    def serialize(seq: List[Double]): Value = transform(seq).to(Value)
  }

}