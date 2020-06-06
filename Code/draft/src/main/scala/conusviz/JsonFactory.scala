package conusviz

import io.circe.syntax._
import io.circe.{Json, Encoder}

sealed trait JsonComponent
case class DataTrace[T](values: Map[String, List[T]]) extends JsonComponent

object JsonFactory extends App {

  implicit val encodeDataSequences_Int: Encoder[DataTrace[Int]] = new Encoder[DataTrace[Int]] {
    final def apply(sequence: DataTrace[Int]): Json = sequence.values.asJson
  }

  implicit val encodeDataSequences_String: Encoder[DataTrace[String]] = new Encoder[DataTrace[String]] {
    final def apply(sequence: DataTrace[String]): Json = sequence.values.asJson
  }

  implicit val encodeDataSequences_Double: Encoder[DataTrace[Double]] = new Encoder[DataTrace[Double]] {
    final def apply(sequence: DataTrace[Double]): Json = sequence.values.asJson
  }
//
//  def constructTraces[T](data: List[List[T]]): DataTraces[T] = {
//    val l = data.length
//    l match {
//      case 0 | 1 => throw new IllegalArgumentException("Number of data traces must be at least 2")
//      case 2 => new DataTraces(Map("x" -> data(0), "y" -> data(1)))
//      case 3 =>new DataTraces(Map("x" -> data(0), "y" -> data(1), "z" -> data(2)))
//    }
//  }

  val x = new DataTrace(Map("x" -> List(1.5, 2.5, 3.5)))
  val y = new DataTrace(Map("y" -> List(1.5, 2.5, 3.5)))

  val data = x.asJson.deepMerge(y.asJson)
  val data2 = x.asJson.deepMerge(y.asJson)

  val l = List(data, data2)

  val obj = Json.obj(
    ("data", Json.fromValues(l))
  )



  println(obj)
}
