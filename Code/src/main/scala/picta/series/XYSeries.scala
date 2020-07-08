package picta.series

import picta.Serializer
import picta.common.Monoid._
import picta.common.OptionWrapper._
import picta.common.Utils._
import picta.options.ColorOptions.Color
import picta.options.Marker
import picta.options.histogram.HistOptions
import picta.options.histogram2d.Hist2dOptions
import picta.series.ModeType.ModeType
import ujson.{Obj, Value}

trait XYSeries extends Series

object XYChartType extends Enumeration {
  type XYChartType = Value
  val SCATTER, SCATTERGL, BAR, HISTOGRAM2DCONTOUR, HISTOGRAM, PIE = Value
}

import picta.series.XYChartType._

/**
 * TODO - Remove non-common components to another individual component
 *
 * @constructor:
 * @param x           :
 * @param y           :
 * @param xkey        :
 * @param ykey        :
 * @param series_name
 * @param series_mode :
 * @param series_type :
 * @param xaxis       :
 * @param yaxis       :
 * @param marker      :
 */
final case class XY[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
(x: List[T0], y: Opt[List[T1]] = Empty, xkey: String = "x", ykey: String = "y", series_name: String = genRandomText,
 series_type: XYChartType = SCATTER, series_mode: Opt[ModeType] = Blank, xaxis: Opt[String] = Blank, yaxis: Opt[String] = Blank,
 marker: Opt[Marker[T2, T3]] = Blank, hist_options: Opt[HistOptions] = Blank, hist2d_options: Opt[Hist2dOptions] = Blank) extends XYSeries {

  def withMarker[Z0: Color, Z1: Color](new_marker: Marker[Z0, Z1]): XY[T0, T1, Z0, Z1] = this.copy(marker = new_marker)

  def +(new_hist_options: HistOptions): XY[T0, T1, T2, T3] = this.copy(hist_options = new_hist_options)

  def +(new_hist2d_options: Hist2dOptions): XY[T0, T1, T2, T3] = this.copy(hist2d_options = new_hist2d_options)

  def serialize: Value = {
    val meta = Obj(
      "name" -> series_name,
      "type" -> series_type.toString.toLowerCase,
    )

    val series_mode_ = series_mode.asOption match {
      case Some(x) => Obj("mode" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val xaxis_ = xaxis.asOption match {
      case Some(x) => if (series_type != PIE) Obj("xaxis" -> x) else jsonMonoid.empty
      case _ => jsonMonoid.empty
    }

    val yaxis_ = yaxis.asOption match {
      case Some(x) => if (series_type != PIE) Obj("yaxis" -> x) else jsonMonoid.empty
      case _ => jsonMonoid.empty
    }

    val marker_ = marker.asOption match {
      case Some(x) => Obj("marker" -> x.serialize)
      case None => jsonMonoid.empty
    }

    /** No need to add a key as this object merges directly */
    val hist_options_ : Value = hist_options.asOption match {
      case Some(x) => if (series_type == HISTOGRAM) x.serialize else jsonMonoid.empty
      case None => jsonMonoid.empty
    }

    /** No need to add a key as this object merges directly */
    val hist2d_options_ : Value = hist2d_options.asOption match {
      case Some(x) => if (series_type == HISTOGRAM2DCONTOUR) x.serialize else jsonMonoid.empty
      case None => jsonMonoid.empty
    }

    List(meta, series_mode_, xaxis_, yaxis_, marker_, hist_options_, hist2d_options_, createSeries)
      .foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }

  private def createSeries(): Value = {
    val y_ = y.asOption match {
      case Some(x) => x
      case _ => Nil
    }

    (x, y_, series_type) match {
      case (x, Nil, HISTOGRAM) => createSeriesXY(x, xkey)
      case (_, _, PIE) => createSeriesXY(x, y_, "values", "labels")
      case (_, _, _) => createSeriesXY(x, y_, xkey, ykey)
    }
  }

  private def createSeriesXY[T0: Serializer, T1: Serializer]
  (x: List[T0], y: List[T1], xkey: String, ykey: String)(implicit s0: Serializer[T0], s1: Serializer[T1]): Value = {
    Obj(xkey -> s0.serialize(x), ykey -> s1.serialize(y))
  }

  private def createSeriesXY[T0: Serializer](x: List[T0], xkey: String)(implicit s0: Serializer[T0]): Value = {
    Obj(xkey -> s0.serialize(x))
  }
}