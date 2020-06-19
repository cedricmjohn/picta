package conusviz.traces

import conusviz.Serializer
import conusviz.charts.XYZChart
import ujson.Value

/*
* Contour, Heatmap, Scatter3d
* */
final case class XYZTrace[T0 : Serializer, T1: Serializer, T2: Serializer]
(x: List[T0], y: List[T1], z: List[T2], val trace_name: String, val trace_type: String, val trace_mode: String) extends Trace {

  if (!(XYZChart.compatibleChartSet contains trace_type))
    throw new IllegalArgumentException(s"chart type '${trace_type}' is not compatible with XYChart")

  val value: Value = {
    val series: Value = Serializer.createSeriesXYZ(x, y, z)
    Serializer.createTrace(series, trace_name, trace_type, trace_mode)
  }
}

object XYZTrace {
def apply[T0: Serializer](x: List[List[T0]], trace_name: String, trace_type: String): XYZTrace[T0, T0, T0] = {
  val n = x.length
  val trace_mode = ""
  XYZTrace(Nil, Nil, x.flatten, trace_name, trace_type, trace_mode)
}






}

