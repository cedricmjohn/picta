package conusviz.traces

import conusviz.Serializer
import conusviz.charts.XYChart
import conusviz.options.MarkerOptions._
import ujson.{Obj, Value}
import upickle.default._
import conusviz.Utils._

/*
* XY Trace: Line, Scatter, Bar Chart, Histogram
* */
final case class XYTrace[T0 : Serializer, T1: Serializer](x: List[T0], y: List[T1], xkey: String = "x", ykey: String = "y",
                                                          trace_name: String, trace_type: String = "scatter",
                                                          trace_mode: Option[String] = None, xaxis: String = "x",
                                                          yaxis: String = "y", marker: Option[Marker] = None) extends Trace {

  if (!(XYChart.compatibleChartSet contains trace_type))
    throw new IllegalArgumentException(s"chart type '${trace_type}' is not compatible with XYChart")

  /*
  * This function creates the data for a trace from two-dimensional data
  * */
  def createSeriesXY[T0 : Serializer, T1: Serializer]
  (x: List[T0], y: List[T1], xkey: String, ykey: String)(implicit s0: Serializer[T0], s1: Serializer[T1]): Value = {
    Obj(xkey -> s0.serialize(x), ykey -> s1.serialize(y))
  }

  /*
  * This function creates the data for a trace from two-dimensional data
  * */
  def createSeriesXY[T0 : Serializer](x: List[T0], xkey: String)(implicit s0: Serializer[T0]): Value = {
    Obj(xkey -> s0.serialize(x))
  }

  def createSeries(): Value = trace_type match  {
    case "histogram" =>  createSeriesXY(x, xkey)
    case _ => createSeriesXY(x, y, xkey, ykey)
  }

  /*
  * A function that creates a series of type Value to be passed into the val variable for this class instance
  * */
  def serialize(): Value = {
    var acc = emptyObject.obj ++ Obj(
      "name" -> trace_name,
      "type" -> trace_type,
      "xaxis" -> xaxis,
      "yaxis" -> yaxis
    ).obj

    trace_mode match {
      case Some(t) => acc.obj ++ Obj("mode" -> t).obj
      case None => ()
    }

    marker match {
      case Some(m) => acc.obj ++ Obj("mode" -> m.serialize).obj
      case None => ()
    }

    acc.obj ++ createSeries().obj
  }
}

object XYTrace {
  /*
  * Alternative constructors for a histogram chart
  * */
  def apply[T0 : Serializer](x: List[T0], xkey: String, trace_name: String, trace_type: String): XYTrace[T0, T0] = {
    if (trace_type != "histogram") throw new IllegalArgumentException("trace_type must be 'histogram'")
    XYTrace(x=x, y=Nil, xkey=xkey, trace_name=trace_name, trace_type=trace_type)
  }

  def apply[T0 : Serializer](x: List[T0], xkey: String, trace_name: String, trace_type: String, marker: Marker): XYTrace[T0, T0] = {
    if (trace_type != "histogram") throw new IllegalArgumentException("trace_type must be 'histogram'")
    XYTrace(x=x, y=Nil, xkey=xkey, trace_name=trace_name, trace_type=trace_type, marker=Some(marker))
  }
}
