package conusviz

// keep the library as generic as possible
// keep it as a wrapper ; easy to use functions; plotmatrix etc multiple levels of complexity
// add more types; float; bigInt; bigDecimal; ordinalValues
// have more general type numbers/ fractional - look in the CoNuS library for this technique; util
import org.carbonateresearch.conus.common._

import scala.reflect.ClassTag
import upickle.default._
import upickle.default.{ReadWriter => RW}
import ujson.{Obj, Value}

sealed trait Data {
//  val value: Value
}

case class Trace[T <: AnyVal](xyz: List[Seq[T]], trace_name: String, trace_type: String, mode: String)
                             (implicit ev: ClassTag[T], rw: RW[Seq[T]]) extends Data {

  val value = Trace.createTrace(xyz, trace_name, trace_type, mode)
}


object Trace {
  def createSeries[T <: AnyVal](xyz: List[Seq[T]])(implicit ev: ClassTag[T], rw: RW[Seq[T]]): Value = {
    xyz.length match {
      case 2 => transform(Map("x" -> xyz(0), "y" -> xyz(1))).to(ujson.Value)
      case 3 => transform(Map("x" -> xyz(0), "y" -> xyz(1), "z" -> xyz(2))).to(ujson.Value)
      case _ => throw new IllegalArgumentException("Invalid dimension for xyz")
    }
  }

  def createTrace[T <: AnyVal](xyz: List[Seq[T]], trace_name: String, trace_type: String, mode: String)
                              (implicit ev: ClassTag[T], rw: RW[Seq[T]]): Obj = {

    // create trace
    val series = createSeries(xyz)

    // add the meta information that assigns the name and type of the trace
    val meta_data: Obj = Obj("name" -> trace_name, "type" -> trace_type, "mode" -> mode)

    // combine metadata and trace values
    meta_data.value.addAll(series.obj)
  }

  // combine into a properly formatted data field
  def createDataField(lst: List[Obj]): Obj = Obj("data" -> lst)

  // helper function to transform an input series
  def zipSeries[T <: AnyVal](xyz: List[Seq[T]])(implicit ev: ClassTag[T]): Seq[Serializable] = {
    xyz.length match {
      case 2 => xyz(0) zip xyz(1)
      case 3 => (xyz(0) zip xyz(1) zip xyz(2)).map(x => (x._1._1, x._1._2, x._2))
    }
  }

  // grabs the series for a single model and converts it to a sequence that can create traces
  def getSeriesSingleModel[T](results: SingleModelResults, variable: ModelVariable[T], coordinate: Seq[Int], n: Int): Seq[Any] = {
    for {
      i <- 0 until n
    } yield (results.getStepResult(i, variable).getValueAtCell(coordinate))
  }
}