package conusviz.traces

import conusviz.Serializer
import conusviz.charts.XYZChart
import ujson.Value

/*
* Contour, Heatmap, Scatter3d
* */
final case class XYZTrace[T0 : Serializer, T1: Serializer, T2: Serializer]
(x: List[T0], y: List[T1], z: List[T2], trace_name: String, trace_type: String, trace_mode: String = "", n: Int = 1) extends Trace {

  if (!(XYZChart.compatibleChartSet contains trace_type))
    throw new IllegalArgumentException(s"chart type '${trace_type}' is not compatible with XYChart")

  def serialize(): Value = n match {
    case 1 => Serializer.createSeriesXYZ(x, y, z)
    case _ => (x, y) match {
      case (Nil, Nil)  => Serializer.createSeriesXYZ(z, n)
      case (_, _) => Serializer.createSeriesXYZ(x, y, z, n)
    }
  }

  // TODO - make sure this works
  val value: Value = Serializer.createTrace(serialize(), trace_name, trace_type, trace_mode, "", "")
}

object XYZTrace {
  def apply[T0: Serializer](z: List[List[T0]], trace_name: String, trace_type: String): XYZTrace[T0, T0, T0] = {
    XYZTrace(x=Nil, y=Nil, z=z.flatten, trace_name=trace_name, trace_type=trace_type, n=z.length)
  }

  // TODO - DOUBLE CHECK IF THIS IS CORRECT VERSION OF CONSTRUCTOR
  def apply[T0: Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[List[T2]], trace_name: String, trace_type: String): XYZTrace[T0, T1, T2] = {
//    if (trace_type != "heatmap") throw new IllegalArgumentException("trace must be of type 'heatmap'")
    XYZTrace(x=x, y=y, z=z.flatten,trace_name=trace_name, trace_type=trace_type, n=z.length)
  }
}

