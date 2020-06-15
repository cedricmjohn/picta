package conusviz

// keep the library as generic as possible
// keep it as a wrapper ; easy to use functions; plotmatrix etc multiple levels of complexity
// add more types; float; bigInt; bigDecimal; ordinalValues
// have more general type numbers/ fractional - look in the CoNuS library for this technique; util

import Serializer._
import ujson.Value

sealed trait Trace {
  val trace_name: String
  val trace_type: String
  val trace_mode: String
  val value: Value
}

object Trace {
  case class XYTrace[T0 : Serializer, T1: Serializer]
  (x: List[T0], y: List[T1], val trace_name: String, val trace_type: String, val trace_mode: String) extends Trace {

    if (!(XYChart.compatibleChartSet contains trace_type))
      throw new IllegalArgumentException(s"chart type '${trace_type}' is not compatible with XYChart")

    val value: Value = {
      val series: Value = Serializer.createSeriesXY(x, y)
      Serializer.createTrace(series, trace_name, trace_type, trace_mode)
    }
  }

  case class XYZTrace[T0 : Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[T2], val trace_name: String, val trace_type: String, val trace_mode: String) extends Trace {

    if (!(XYZChart.compatibleChartSet contains trace_type))
      throw new IllegalArgumentException(s"chart type '${trace_type}' is not compatible with XYChart")

    val value: Value = {
      val series: Value = Serializer.createSeriesXYZ(x, y, z)
      Serializer.createTrace(series, trace_name, trace_type, trace_mode)
    }
  }

  case class SurfaceTrace[T0: Serializer]
  (x: List[T0], n: Int, val trace_name: String) extends Trace {
    val trace_type = "surface"
    val trace_mode = ""
    val value: Value = {
      val series = Serializer.createSeriesSurface(x, n)
      Serializer.createTrace(series, trace_name, trace_type, trace_mode)
    }
  }

  object SurfaceTrace {
    def apply[T0: Serializer](x: List[List[T0]], trace_name: String): SurfaceTrace[T0] = {
      val n = x.length
      SurfaceTrace(x.flatten, n, trace_name)
    }
  }
}






