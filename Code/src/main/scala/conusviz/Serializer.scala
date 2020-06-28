package conusviz

import upickle.default._
import ujson.{Value}


/*
  * A type class for a serializer that serializes scala data structures to a valid Value format
  * */
sealed trait Serializer[T] {
  def serialize(seq: List[T]): Value
}

object Serializer {
  implicit object StringSerializer extends Serializer[String] {
    def serialize(seq: List[String]): Value = transform(seq).to(Value)
  }

  implicit object IntSerializer extends Serializer[Int] {
    def serialize(seq: List[Int]): Value = transform(seq).to(Value)
  }

  implicit object DoubleSerializer extends Serializer[Double] {
    def serialize(seq: List[Double]): Value = transform(seq).to(Value)
  }

  implicit object FloatSerializer extends Serializer[Float] {
    def serialize(seq: List[Float]): Value = transform(seq).to(Value)
  }

  implicit object LongSerializer extends Serializer[Long] {
    def serialize(seq: List[Long]): Value = transform(seq).to(Value)
  }

  implicit object BigDecimalSerializer extends Serializer[BigDecimal] {
    def serialize(seq: List[BigDecimal]): Value = transform(seq).to(Value)
  }
}
