//package conusviz
//import io.circe.generic.semiauto._
//import io.circe.syntax._
//import io.circe.{Encoder, Json, KeyEncoder}
//
//sealed trait RawList
//case class IntList(values: List[Int]) extends RawList
//case class DoubleList(values: List[Double]) extends RawList
//case class StringList(values: List[String]) extends RawList
//
//object Traces extends App {
//  implicit val IntListEncoder: Encoder[IntList] = deriveEncoder
//  implicit val DoubleListEncoder: Encoder[DoubleList] = deriveEncoder
//  implicit val StringListEncoder: Encoder[StringList] = deriveEncoder
//
//  implicit val encodeRawList: Encoder[RawList] = Encoder.instance {
//    case i @ IntList(_) => i.values.asJson
//    case d @ DoubleList(_) => d.values.asJson
//    case s @ StringList(_) => s.values.asJson
//  }
//
//  def createTraceSet[T](data: List[RawList]): Json = {
//    val l = data.length
//    l match {
//        case 2 => Map("x" -> data(0)).asJson.deepMerge(Map("y" -> data(1)).asJson)
//        case 3 => Map("x" -> data(0)).asJson.deepMerge(Map("y" -> data(1)).asJson).deepMerge(Map("z" -> data(2)).asJson)
//        case _ => throw new IllegalArgumentException("Length of list must be 2 or 3")
//      }
//  }
//
//  def createDataField[T](data: List[List[RawList]], `type`: String): Json = {
//      Json.obj(
//        ("data",  data.map(createTraceSet).asJson),
//        ("type", `type`.asJson)
//      )
//  }
//
//  val x = new IntList(List(1, 2))
//  val l = List(x, x)
//  val lst = List(l, l)
//
//  println(createDataField(lst, "bar"))
//
//}

import io.circe.syntax._
import io.circe.{Json, Encoder}
//import io.circe.generic._

sealed trait RawList
case class IntList(values: List[Int]) extends RawList
case class DoubleList(values: List[Double]) extends RawList
case class StringList(values: List[String]) extends RawList

object JsonFactory extends App {
  //  implicit val rawListEncoder: Encoder[RawList] = deriveEncoder[RawList]

  implicit val rawListEncoder: Encoder[RawList] = new Encoder[RawList] {
    final def apply(a: RawList): Json = a.asJson
  }

  def createData(data: List[RawList]): Json = {
    val l = data.length
    data(0).asJson
  }

  val x = new DoubleList(List(1.5, 2.5, 3.5))
  val y = new StringList(List("1.5", "2.5", "3.5"))

  val ob = createData(List(x, y))
  println(ob.asJson)
}
