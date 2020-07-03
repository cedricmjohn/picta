package picta.series

import picta.Serializer
import picta.options.Marker
import ujson.{Obj, Value}
import picta.Utils._
import picta.options.ColorOptions.Color
import picta.options.histogram.HistOptions
import picta.series.ModeType.ModeType

trait XYSeries extends Series

object XYChartType extends Enumeration {
  type XYChartType = Value
  val SCATTER, SCATTERGL, BAR, HISTOGRAM2DCONTOUR, HISTOGRAM, PIE = Value
}

import XYChartType._

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
 */
final case class XY[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
(x: List[T0], y: List[T1], xkey: String = "x", ykey: String = "y", series_name: String = genRandomText, series_type: XYChartType=SCATTER,
 series_mode: Option[ModeType] = None, xaxis: String = "x", yaxis: String = "y", marker: Option[Marker[T2, T3]] = None,
 hist_options: Option[HistOptions] = None) extends XYSeries {

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

  def serialize: Value = {
    val meta = Obj(
      "name" -> series_name,
      "type" -> series_type.toString.toLowerCase,
    )

    val axes = series_type match {
      case PIE => emptyObject()
      case _ => Obj("xaxis" -> xaxis, "yaxis" -> yaxis)
    }

    val series_mode_ = series_mode match {
      case Some(x) => Obj("mode" -> x.Stringify)
      case None => emptyObject
    }

    val marker_ = marker match {
      case Some(x) => Obj("marker" -> x.serialize)
      case None => emptyObject
    }

    /** No need to add a key as this object merges directly */
    val hist_options_ = hist_options match {
      case Some(x) => if (series_type == HISTOGRAM) x.serialize
      case None => emptyObject
    }

    List(meta, axes, series_mode_, marker_, createSeries).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}

object XY {
  def apply[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
  (x: List[T0], y: List[T1], series_type: XYChartType, series_mode: ModeType): XY[T0, T1, T2, T3] = {
    XY(x=x, y=y, series_type=series_type, series_mode=Some(series_mode))
  }

  def apply[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
  (x: List[T0], y: List[T1], series_type: XYChartType, series_mode: ModeType, xaxis: String, yaxis: String): XY[T0, T1, T2, T3] = {
    XY(x=x, y=y, series_type=series_type, series_mode=Some(series_mode), xaxis=xaxis, yaxis=yaxis)
  }

  def apply[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
  (x: List[T0], y: List[T1], series_name: String, series_type: XYChartType, series_mode: ModeType, xaxis: String,
   yaxis: String, marker: Marker[T2, T3]): XY[T0, T1, T2, T3] = {
    XY(x=x, y=y, series_name=series_name, series_type=series_type, series_mode=Some(series_mode), xaxis=xaxis,
      yaxis=yaxis, marker=Some(marker))
  }

  def apply[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
  (x: List[T0], y: List[T1], xkey: String, ykey: String, series_name: String, series_type: XYChartType,
   series_mode: ModeType, xaxis: String, yaxis: String, marker: Marker[T2, T3]): XY[T0, T1, T2, T3] = {
    XY(x=x, y=y, xkey=xkey, ykey=ykey, series_name=series_name, series_type=series_type, series_mode=Some(series_mode),
      xaxis=xaxis, yaxis=yaxis, marker=Some(marker))
  }

  def apply[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
  (x: List[T0], y: List[T1], series_name: String, series_type: XYChartType, series_mode: ModeType):  XY[T0, T1, T2, T3] = {
    XY(x=x, y=y, series_name=series_name, series_type=series_type, series_mode=Some(series_mode))
  }

  /** Alternative constructor that constructs a histogram series with no marker specified */
  def apply[T0 : Serializer, T1: Color, T2: Color]
  (x: List[T0], xkey: String, series_type: XYChartType): XY[T0, T0, T1, T2] = {
    if (series_type != HISTOGRAM) throw new IllegalArgumentException("series_type must be 'histogram'")
    XY(x=x, y=Nil, xkey=xkey, series_type=series_type)
  }

  /** Alternative constructor that constructs a histogram series, with X, Y, Z specified */
  def apply[T0 : Serializer, T1: Color, T2: Color]
  (x: List[T0], xkey: String, series_name: String, series_type: XYChartType): XY[T0, T0, T1, T2] = {
    if (series_type != HISTOGRAM) throw new IllegalArgumentException("series_type must be 'histogram'")
    XY(x=x, y=Nil, xkey=xkey, series_name=series_name, series_type=series_type)
  }

  /** Alternative constructor that constructs a histogram series, with X, Y, Z specified */
  def apply[T0 : Serializer, T1: Color, T2: Color]
  (x: List[T0], xkey: String, series_name: String, series_type: XYChartType, marker: Marker[T1, T2]): XY[T0, T0, T1, T2] = {
    if (series_type != HISTOGRAM) throw new IllegalArgumentException("series_type must be 'histogram'")
    XY(x=x, y=Nil, xkey=xkey, series_name=series_name, series_type=series_type, marker=Some(marker))
  }
}