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

