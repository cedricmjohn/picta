package picta.traces

import picta.Serializer
import picta.options.MarkerOptions._
import ujson.{Obj, Value}
import picta.Utils._
import picta.traces.Trace.XYChartType._

trait XYTrace extends Trace

object XYTrace {

  /*
  * XY Trace: Line, Scatter, Bar Chart, Histogram
  * */
  final case class XY[T0: Serializer, T1: Serializer]
  (x: List[T0], y: List[T1], xkey: String = "x", ykey: String = "y", trace_name: String, trace_type: XYChartType = SCATTER,
   trace_mode: Option[String] = None, xaxis: String = "x", yaxis: String = "y", marker: Option[Marker] = None,
   cumulative: Option[Boolean] = None, histnorm: Option[String] = None, histfunc: Option[String] = None) extends XYTrace {


    def +(new_marker: Marker): XY[T0, T1] = this.copy(marker = Some(new_marker))

    private def createSeriesXY[T0 : Serializer, T1: Serializer]
    (x: List[T0], y: List[T1], xkey: String, ykey: String)(implicit s0: Serializer[T0], s1: Serializer[T1]): Value = {
      Obj(xkey -> s0.serialize(x), ykey -> s1.serialize(y))
    }

    private def createSeriesXY[T0 : Serializer](x: List[T0], xkey: String)(implicit s0: Serializer[T0]): Value = {
      Obj(xkey -> s0.serialize(x))
    }

    private def createSeries(): Value = trace_type match  {
      case HISTOGRAM =>  createSeriesXY(x, xkey)
      case PIE => createSeriesXY(x, y, "values", "labels")
      case _ => createSeriesXY(x, y, xkey, ykey)
    }

    def serialize(): Value = {
      val meta = Obj(
        "name" -> trace_name,
        "type" -> trace_type.toString.toLowerCase,
      )

      val axes = trace_type match {
        case PIE => emptyObject()
        case _ => Obj("xaxis" -> xaxis, "yaxis" -> yaxis)
      }

      val t = trace_mode match {
        case Some(t) => Obj("mode" -> t)
        case None => emptyObject
      }

      val m = marker match {
        case Some(m) => Obj("marker" -> m.serialize)
        case None => emptyObject
      }

      val c = cumulative match {
        case Some(m) => Obj("cumulative" -> Obj("enabled" -> m))
        case None => emptyObject
      }

      val hn = histnorm match {
        case Some(h) => Obj("histnorm" -> h)
        case None => emptyObject
      }

      val hf = histfunc match {
        case Some(h) => Obj("histfunc" -> h)
        case None => emptyObject
      }

      meta.obj ++ axes.obj ++ t.obj ++ m.obj ++ c.obj ++ hn.obj ++ hf.obj ++ createSeries().obj
    }
  }

  object XY {
    /*
    * Alternative constructors for a histogram chart
    * */
    def apply[T0 : Serializer](x: List[T0], xkey: String, trace_name: String, trace_type: XYChartType): XY[T0, T0] = {
      if (trace_type != HISTOGRAM) throw new IllegalArgumentException("trace_type must be 'histogram'")
      XY(x=x, y=Nil, xkey=xkey, trace_name=trace_name, trace_type=trace_type)
    }

    def apply[T0 : Serializer](x: List[T0], xkey: String, trace_name: String, trace_type: XYChartType, marker: Marker): XY[T0, T0] = {
      if (trace_type != HISTOGRAM) throw new IllegalArgumentException("trace_type must be 'histogram'")
      XY(x=x, y=Nil, xkey=xkey, trace_name=trace_name, trace_type=trace_type, marker=Some(marker))
    }
  }
}
