package conusviz.traces

import conusviz.Serializer
import ujson.Value

final case class SurfaceTrace[T0: Serializer]
(x: List[T0], n: Int, val trace_name: String, val x_label: String = "x", val y_label: String = "y") extends Trace {
  val trace_type = "surface"
  val trace_mode = ""
  val value: Value = {
    val series = Serializer.createSeriesSurface(x, n)
    Serializer.createTrace(series, trace_name, trace_type, trace_mode)
  }
}

object SurfaceTrace {
  /*
  * Alternative constructors for surface trace which takes in a different shape for the input list
  * */
  def apply[T0: Serializer](x: List[List[T0]], trace_name: String): SurfaceTrace[T0] = {
    val n = x.length
    SurfaceTrace(x.flatten, n, trace_name)
  }

  def apply[T0: Serializer]
  (x: List[List[T0]], trace_name: String, x_label: String, y_label: String): SurfaceTrace[T0] = {
    val n = x.length
    SurfaceTrace(x.flatten, n, trace_name, x_label, y_label)
  }
}