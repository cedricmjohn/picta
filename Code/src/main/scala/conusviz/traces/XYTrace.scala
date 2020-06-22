package conusviz.traces

import conusviz.Serializer
import conusviz.charts.XYChart
import ujson.Value

/*
* XY Trace: Line, Scatter, Bar Chart, Histogram
* */
final case class XYTrace[T0 : Serializer, T1: Serializer](x: List[T0], y: List[T1], trace_name: String,
                                                          trace_type: String = "scatter", trace_mode: String = "markers",
                                                          xaxis: String = "x1", yaxis: String = "y1") extends Trace {

  if (!(XYChart.compatibleChartSet contains trace_type))
    throw new IllegalArgumentException(s"chart type '${trace_type}' is not compatible with XYChart")

  /*
  * A function that creates a series of type Value to be passed into the val variable for this class instance
  * */
  def serialize(): Value = trace_type match  {
    case "histogram" => trace_mode match {
        case "vertical" => Serializer.createSeriesHistogram(x, "x")
        case "horizontal" => Serializer.createSeriesHistogram(x, "y")
        case _ => throw new IllegalArgumentException("invalid argument for 'trace_mode'")
      }
    case _ => Serializer.createSeriesXY(x, y)
  }

  val value: Value = Serializer.createTrace(this.serialize(), trace_name, trace_type, trace_mode, xaxis, yaxis)
}

object XYTrace {
  /*
  * Alternative constructors for a histogram chart
  * */
  def apply[T0 : Serializer]
  (x: List[T0], trace_name: String, trace_type: String, trace_mode: String): XYTrace[T0, T0] = {
    if (trace_type != "histogram") throw new IllegalArgumentException("trace_type must be 'histogram'")
    XYTrace(x, Nil, trace_name, trace_type, trace_mode)
  }




}
