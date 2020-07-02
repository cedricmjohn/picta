package picta.series

import picta.Serializer
import picta.options.Marker
import ujson.{Obj, Value}
import picta.Utils._
import picta.options.ColorOptions.Color
import picta.series.Series.XYChartType._

trait XYSeries extends Series

/**
 * TODO - Remove non-common components to another individual component
 * @constructor:
 * @param x:
 * @param y:
 * @param xkey:
 * @param ykey:
 * @param series_name
 * @param series_mode:
 * @param series_type:
 * @param xaxis:
 * @param yaxis:
 * @param marker:
 * @param cumulative:
 * @param histnorm:
 * @param histfunc:
 */
final case class XY[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
(x: List[T0], y: List[T1], xkey: String = "x", ykey: String = "y", series_name: String, series_type: XYChartType = SCATTER,
 series_mode: Option[String] = None, xaxis: String = "x", yaxis: String = "y", marker: Option[Marker[T2, T3]] = None,
 cumulative: Option[Boolean] = None, histnorm: Option[String] = None, histfunc: Option[String] = None) extends XYSeries {

  def +[Z0: Color, Z1: Color](new_marker: Marker[Z0, Z1]): XY[T0, T1, Z0, Z1] = this.copy(marker = Some(new_marker))

  private def createSeriesXY[T0 : Serializer, T1: Serializer]
  (x: List[T0], y: List[T1], xkey: String, ykey: String)(implicit s0: Serializer[T0], s1: Serializer[T1]): Value = {
    Obj(xkey -> s0.serialize(x), ykey -> s1.serialize(y))
  }

  private def createSeriesXY[T0 : Serializer](x: List[T0], xkey: String)(implicit s0: Serializer[T0]): Value = {
    Obj(xkey -> s0.serialize(x))
  }

  private def createSeries(): Value = series_type match  {
    case HISTOGRAM =>  createSeriesXY(x, xkey)
    case PIE => createSeriesXY(x, y, "values", "labels")
    case _ => createSeriesXY(x, y, xkey, ykey)
  }

  def serialize(): Value = {
    val meta = Obj(
      "name" -> series_name,
      "type" -> series_type.toString.toLowerCase,
    )

    val axes = series_type match {
      case PIE => emptyObject()
      case _ => Obj("xaxis" -> xaxis, "yaxis" -> yaxis)
    }

    val t = series_mode match {
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
  def apply[T0 : Serializer, T1: Color, T2: Color](x: List[T0], xkey: String, series_name: String, series_type: XYChartType): XY[T0, T0, T1, T2] = {
    if (series_type != HISTOGRAM) throw new IllegalArgumentException("series_type must be 'histogram'")
    XY(x=x, y=Nil, xkey=xkey, series_name=series_name, series_type=series_type)
  }

  def apply[T0 : Serializer, T1: Color, T2: Color](x: List[T0], xkey: String, series_name: String, series_type: XYChartType, marker: Marker[T1, T2]): XY[T0, T0, T1, T2] = {
    if (series_type != HISTOGRAM) throw new IllegalArgumentException("series_type must be 'histogram'")
    XY(x=x, y=Nil, xkey=xkey, series_name=series_name, series_type=series_type, marker=Some(marker))
  }
}
