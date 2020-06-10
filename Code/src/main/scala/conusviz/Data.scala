package conusviz

import org.carbonateresearch.conus.common._
//import io.circe.generic.semiauto._

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}
import io.circe.literal._

sealed trait Attribute
case class Line(color: String, width: Double) extends Attribute
case class Marker(line: Line, color: String) extends Attribute

class Trace(traces: List[Tuple2[String, Json]]) {
  val data = traces.foldLeft(scala.collection.mutable.Map.empty[String, Json])( (map, x) => map += (x._1 -> x._2) )
}

object Trace extends App {

  // construct a Json object from a key value pair
  def singleField(key: String, value: String): Json = {
    Json.obj((key, Json.fromString(value)))
  }

  // convert an arbitrary sequence to json
  def seqToJson[T](lst: Seq[T])(implicit encoder: Encoder[T]): Json = {
    lst.asJson
  }

  // create trace data set
  def createSingleTrace[T](lst: List[Seq[T]])(implicit encoder: Encoder[T]): Json = {
    val x = lst.length match {
      case 2 => Map("x" -> seqToJson(lst(0)), "y" -> seqToJson(lst(1)))
      case 3 => Map("x" -> seqToJson(lst(0)), "y" -> seqToJson(lst(1)), "z" -> seqToJson(lst(2)))
      case _ => throw new IllegalArgumentException("Invalid number of sequences")
    }
    x.asJson
  }

  def constructJson(lst: List[Json]) = {
    val fields = lst.fold(Json.obj())((acc, x) => acc.deepMerge(x))
    Json.obj(("data", List(fields).asJson))
  }

  val x = Seq(1, 2, 3, 4)
  val y = Seq(2, 3, 4, 5)

  val x1 = Seq(10, 20, 3, 40)
  val y1 = Seq(10, 20, 3, 40)

  val mode = singleField("mode", "lines+markers")
  val chartype = singleField("type", "bar")
  val trace = createSingleTrace(List(x, y))

  val json = constructJson(List(mode, chartype, trace))

  val html = Chart.generateHTMLChart(Chart.generatePlotlyFunction(json))

  Chart.writeHTMLChartToFile(html)

}






//  val z = new myClass(Some(Json.fromString()))
//  val z = Json.obj(("test", Json.fromString("me")))

//sealed trait Something
//case class Name(val name: String) extends Something

//sealed trait SingleChartAttribute
//case class Name(val name: String) extends SingleChartAttribute
//case class Type(val `type`: String) extends SingleChartAttribute

//case class Line(color: String, width: Double)
//case class Marker(line: Line, color: String) extends ChartAttribute

//implicit val fooDecoder: Decoder[SingleChartAttribute] = deriveDecoder[SingleChartAttribute]
//  val trace_name: Json = name.get.asJson

//  def createJson() = {
//    Json.obj(
//      ("data", attributes.fold(Json.obj())((acc, x) => acc.deepMerge(x)))
//    )
//  }




//  def getSeriesSingleModel[T](results: SingleModelResults,
//                              variable: ModelVariable[T],
//                              coordinate: Seq[Int],
//                              n: Int): Seq[Any] = {
//    for {
//      i <- 0 until n
//    } yield (results.getStepResult(i, variable).getValueAtCell(coordinate))
//  }


//  val name = new Name("johnny")
//  val tpe = new Type("bar")
//  val line = new Line("rgba(55, 128, 191, 1.0)", 1.0)
//  val color = "rgba(55, 128, 191, 1.0)"
//  val marker = new Marker(line, color)

//  val atts = List(name.asJson, tpe.asJson, Json.obj(("marker", marker.asJson)))

//  implicit val encodeFoo: Encoder[Trace] = new Encoder[Trace] {
//    final def apply(t: Trace): Json = Json.obj(
//      ("name", t.name.asJson)
//    )
//  }

//  val x = atts.foldLeft(Json.obj())( (z, x) => z.deepMerge(x) )
//val g = new Trace()
//  g.printName

// keep the library as generic as possible
// keep it as a wrapper ; easy to use functions; plotmatrix etc multiple levels of complexity
// add more types; float; bigInt; bigDecimal; ordinalValues
// have more general type numbers/ fractional - look in the CoNuS library for this technique; util