package conusviz.traces

import conusviz.Serializer
import conusviz.charts.XYChart
import ujson.Value

/*
  * XY Trace: Line, Scatter, Bar Chart, Histogram
  * */
final case class XYTrace[T0 : Serializer, T1: Serializer]
(x: List[T0], y: List[T1], val trace_name: String, val trace_type: String, val trace_mode: String) extends Trace {

  if (!(XYChart.compatibleChartSet contains trace_type))
    throw new IllegalArgumentException(s"chart type '${trace_type}' is not compatible with XYChart")

  val value: Value = {
    val series: Value = Serializer.createSeriesXY(x, y)
    Serializer.createTrace(series, trace_name, trace_type, trace_mode)
  }
}

/*
* Alternative constructors
* */
object XYTrace {
  // bar chart
  def apply[T0 : Serializer, T1: Serializer]
  (x: List[T0], y: List[T1], trace_name: String, trace_type: String): XYTrace[T0, T1] = {
    if (trace_type != "bar") throw new IllegalArgumentException("if no marker passed in, trace_type must be 'bar'")
    XYTrace(x, y, trace_name, trace_type, "")
  }

  // histogram chart
  def apply[T0 : Serializer]
  (x: List[T0], trace_name: String, trace_type: String, trace_mode: String): XYTrace[T0, T0] = {
    if (trace_type != "histogram") throw new IllegalArgumentException("trace_type must be 'histogram'")
    XYTrace(x, Nil, trace_name, trace_type, trace_mode)
  }
}

/*
  * Histogram Trace
  * */
//final case class HistogramTrace[T: Serializer]
//(x: List[T], val trace_name: String, val trace_mode: String = "vertical") extends Trace {
//  val trace_type = "histogram"
//  val value: Value = {
//    val name = trace_mode match {
//      case "vertical" => "x"
//      case "horizontal" => "y"
//      case _ => throw new IllegalArgumentException("invalid argument for 'trace_mode'")
//    }
//    val series: Value = Serializer.createSeriesX(x, name)
//    Serializer.createTrace(series, trace_name, trace_type, trace_mode)
//  }
