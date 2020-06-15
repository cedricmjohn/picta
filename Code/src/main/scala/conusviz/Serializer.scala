package conusviz

import upickle.default._
import ujson.{Obj, Value}

sealed trait Serializer[T] {
  def serialize(seq: List[T]): Value
}

object Serializer {
  def serialize[T](seq: List[T])(implicit serializer: Serializer[T]): Value = serializer.serialize(seq)

  implicit object StringSerializer extends Serializer[String] {
    def serialize(seq: List[String]): Value = transform(seq).to(Value)
  }

  implicit object IntSerializer extends Serializer[Int] {
    def serialize(seq: List[Int]): Value = transform(seq).to(Value)
  }

  implicit object DoubleSerializer extends Serializer[Double] {
    def serialize(seq: List[Double]): Value = transform(seq).to(Value)
  }

  def createSeriesXY[T0 : Serializer, T1: Serializer]
  (x: List[T0], y: List[T1])
  (implicit s0: Serializer[T0], s1: Serializer[T1]): Value = {
    transform(Obj("x" -> s0.serialize(x), "y" -> s1.serialize(y))).to(Value)
  }

  def createSeriesXYZ[T0 : Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[T2])
  (implicit s0: Serializer[T0], s1: Serializer[T1], s2: Serializer[T2]): Value = {
    transform(Obj("x" -> s0.serialize(x), "y" -> s1.serialize(y), "z" -> s2.serialize(z))).to(Value)
  }

  def createSeriesSurface[T : Serializer]
  (x: List[T], n: Int)
  (implicit s0: Serializer[T]): Value = {
    val list = x.grouped(n).toList.map(e => s0.serialize(e))
    transform(Obj("z" -> list)).to(Value)
  }

  def createTrace(series: Value, trace_name: String, trace_type: String, trace_mode: String): Value = {
    // add the meta information that assigns the name and type of the trace
    val meta_data: Obj = Obj("name" -> trace_name, "type" -> trace_type, "mode" -> trace_mode)
    // combine metadata and trace values
    meta_data.value.addAll(series.obj)
  }
}
