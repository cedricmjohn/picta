package conusviz

import org.carbonateresearch.conus.common.SingleModelResults
import upickle.default._
import ujson.{Obj, Value}


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

  /*
  * This function creates the data for a trace from two-dimensional data
  * */
  def createSeriesXY[T0 : Serializer, T1: Serializer]
  (x: List[T0], y: List[T1], xkey: String, ykey: String)(implicit s0: Serializer[T0], s1: Serializer[T1]): Value = {
    transform(Obj(xkey -> s0.serialize(x), ykey -> s1.serialize(y))).to(Value)
  }

  /*
* This function creates the data for a trace from two-dimensional data
* */
  def createSeriesHistogram[T0 : Serializer](x: List[T0], xkey: String)(implicit s0: Serializer[T0]): Value = {
    transform(Obj(xkey -> s0.serialize(x))).to(Value)
  }

  /*
  * This function creates the data for a trace from three-dimensional data
  * */
  def createSeriesXYZ[T0 : Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[T2])(implicit s0: Serializer[T0], s1: Serializer[T1], s2: Serializer[T2]): Value = {
    transform(Obj("x" -> s0.serialize(x), "y" -> s1.serialize(y), "z" -> s2.serialize(z))).to(Value)
  }

  /*
  * This function creates the data for a trace for a surface plot
  * */
  def createSeriesXYZ[T : Serializer](z: List[T], n: Int)(implicit s0: Serializer[T]): Value = {
    if (z.length % n != 0) throw new IllegalArgumentException("The length of 'z' must be divisible by 'n'")
    val list = z.grouped(n).toList.map(e => s0.serialize(e))
    transform(Obj("z" -> list)).to(Value)
  }

  /* TODO - FIX THE ERROR MESSAGE HERE
  * This function creates the data for a trace for a heatmap plot, with specified by x and y data
  * */
  def createSeriesXYZ[T0 : Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[T2], n: Int)(implicit s0: Serializer[T0], s1: Serializer[T1], s2: Serializer[T2]): Value = {
    if (n != x.length)
      throw new IllegalArgumentException("The number of elements in 'z' must be divisible by length of 'y'")

    if (z.length / n != y.length)
      throw new IllegalArgumentException("The number of elements in 'x' must be divisible by length of an element in 'z'")

    val list: List[Value] = z.grouped(n).toList.map(e => s2.serialize(e))
    transform(Obj("x"-> s0.serialize(x), "y" -> s1.serialize(y), "z" -> list)).to(Value)
  }

  /*
  * This function creates a trace json in value format. This format allows it to easily be interpreted by the Plotly.js
  * library later on
  * */
  def createTrace(series: Value, trace_name: String, trace_type: String, trace_mode: String, xaxis: String, yaxis: String): Value = {
    val meta_data: Obj = Obj("name" -> trace_name, "type" -> trace_type, "mode" -> trace_mode, "xaxis" -> xaxis, "yaxis" -> yaxis)
    meta_data.value.addAll(series.obj)
  }

}
