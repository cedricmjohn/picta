package conusviz.traces

import conusviz.Serializer
import conusviz.charts.XYChart
import conusviz.options.MarkerOptions._
import ujson.{Obj, Value}

/*
* XY Trace: Line, Scatter, Bar Chart, Histogram
* */
final case class XYTrace[T0 : Serializer, T1: Serializer](x: List[T0], y: List[T1], xkey: String = "x", ykey: String = "y",
                                                          trace_name: String, trace_type: String = "scatter",
                                                          trace_mode: String = "markers", xaxis: String = "x",
                                                          yaxis: String = "y", marker: Marker = Marker()) extends Trace {

  if (!(XYChart.compatibleChartSet contains trace_type))
    throw new IllegalArgumentException(s"chart type '${trace_type}' is not compatible with XYChart")

  def createSeries(): Value = trace_type match  {
    case "histogram" =>  Serializer.createSeriesHistogram(x, xkey)
    case _ => Serializer.createSeriesXY(x, y, xkey, ykey)
  }

  val value: Value = Serializer.createTrace(this.createSeries(), trace_name, trace_type, trace_mode, xaxis, yaxis)

  /*
  * A function that creates a series of type Value to be passed into the val variable for this class instance
  * */
  def serialize(): Value = value.obj ++ Obj("marker" -> marker.serialize).obj
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
    XYTrace(x=x, y=Nil, xkey=xkey, trace_name=trace_name, trace_type=trace_type, marker=marker)
  }
}
