package conusviz
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.{Encoder, Json, KeyEncoder}

// keep the library as generic as possible
// keep it as a wrapper ; easy to use functions; plotmatrix etc multiple levels of complexity

// add more types; float; bigInt; bigDecimal; ordinalValues
sealed trait RawList

// have more general type numbers/ fractional - look in the CoNuS library for this technique; util
case class IntList(values: List[Int]) extends RawList
case class DoubleList(values: List[Double]) extends RawList
case class StringList(values: List[String]) extends RawList

object Traces extends App {
  implicit val IntListEncoder: Encoder[IntList] = deriveEncoder
  implicit val DoubleListEncoder: Encoder[DoubleList] = deriveEncoder
  implicit val StringListEncoder: Encoder[StringList] = deriveEncoder

  implicit val encodeRawList: Encoder[RawList] = Encoder.instance {
    case i@IntList(_) => i.values.asJson
    case d@DoubleList(_) => d.values.asJson
    case s@StringList(_) => s.values.asJson
  }

  def createTraceSet[T](data: List[RawList]): Json = {
    val l = data.length
    l match {
      case 2 => Map("x" -> data(0)).asJson.deepMerge(Map("y" -> data(1)).asJson)
      case 3 => Map("x" -> data(0)).asJson.deepMerge(Map("y" -> data(1)).asJson).deepMerge(Map("z" -> data(2)).asJson)
      case _ => throw new IllegalArgumentException("Length of list must be 2 or 3")
    }
  }

  def createDataField[T](data: List[List[RawList]], `type`: String): Json = {
    Json.obj(
      ("data", data.map(createTraceSet).asJson),
      ("type", `type`.asJson)
    )
  }

  val x = new IntList(List(1, 2))
  val l = List(x, x)
  val lst = List(l, l)

  println(createDataField(lst, "bar"))

}