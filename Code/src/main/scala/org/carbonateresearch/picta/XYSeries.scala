package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.common.Serializer
import org.carbonateresearch.picta.common.Utils._
import org.carbonateresearch.picta.options.ColorOptions.Color
import org.carbonateresearch.picta.options.{Marker, XAxis, YAxis}
import org.carbonateresearch.picta.options.histogram.HistOptions
import org.carbonateresearch.picta.options.histogram2d.Hist2dOptions
import ujson.{Obj, Value}

trait XYSeries[T0, T1, T2, T3] extends Series {
  def setAxes(axes: (XAxis, YAxis)): XY[T0, T1, T2, T3]
}

/** ENUM for the XY chart series types */
private[picta] sealed trait XYType

case object SCATTER extends XYType

case object SCATTERGL extends XYType

case object BAR extends XYType

case object HISTOGRAM2DCONTOUR extends XYType

case object HISTOGRAM extends XYType

case object PIE extends XYType


/**
 * TODO - Remove non-common components to another individual component
 *
 * @constructor:
 * @param x           :
 * @param y           :
 * @param name
 * @param symbol :
 * @param `type` :
 * @param xaxis       :
 * @param yaxis       :
 * @param marker      :
 */
final case class XY[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
(x: List[T0], y: Opt[List[T1]] = Empty, name: String = generateRandomText, `type`: XYType = SCATTER,
 symbol: Opt[Symbol] = Blank, xaxis: Opt[XAxis] = Blank, yaxis: Opt[YAxis] = Blank, marker: Opt[Marker[T2, T3]] = Blank,
 hist_options: Opt[HistOptions] = Blank, hist2d_options: Opt[Hist2dOptions] = Blank) extends XYSeries[T0, T1, T2, T3] {

  def setName(new_name: String): XY[T0, T1, T2, T3] = this.copy(name = new_name)

  def setMarker[Z0: Color, Z1: Color](new_marker: Marker[Z0, Z1]): XY[T0, T1, Z0, Z1] = this.copy(marker = new_marker)

  def setHistOptions(new_hist_options: HistOptions): XY[T0, T1, T2, T3] = this.copy(hist_options = new_hist_options)

  def setHist2dOptions(new_hist2d_options: Hist2dOptions): XY[T0, T1, T2, T3] = this.copy(hist2d_options = new_hist2d_options)

  def setAxes(new_axes: (XAxis, YAxis)): XY[T0, T1, T2, T3] = this.copy(xaxis = new_axes._1, yaxis = new_axes._2)

  def setAxis(new_axis: XAxis): XY[T0, T1, T2, T3] = this.copy(xaxis = new_axis)

  def setAxis(new_axis: YAxis): XY[T0, T1, T2, T3] = this.copy(yaxis = new_axis)

  def asType(new_type: XYType): XY[T0, T1, T2, T3] = this.copy(`type` = new_type)

  def asScatter(): XY[T0, T1, T2, T3] = this.copy(`type` = SCATTER)

  def asScatterGl(): XY[T0, T1, T2, T3] = this.copy(`type` = SCATTERGL)

  def asBar(): XY[T0, T1, T2, T3] = this.copy(`type` = BAR)

  def asHistogram2dContour(): XY[T0, T1, T2, T3] = this.copy(`type` = HISTOGRAM2DCONTOUR)

  def asHistogram(): XY[T0, T1, T2, T3] = this.copy(`type` = HISTOGRAM)

  def asPIE(): XY[T0, T1, T2, T3] = this.copy(`type` = PIE)

  def drawSymbol(new_mode: Symbol) = this.copy(symbol = new_mode)

  def drawLines() = this.copy(symbol = LINES)

  def drawMarkers() = this.copy(symbol = MARKERS)

  def drawLinesMarkers() = this.copy(symbol = LINES_MARKERS)

  def drawText() = this.copy(symbol = TEXT)

  def drawLinesText() = this.copy(symbol = LINES_TEXT)

  def drawMarkersText() = this.copy(symbol = MARKERS_TEXT)

  def drawNone() = this.copy(symbol = NONE)

  def drawLinesMarkersText() = this.copy(symbol = LINES_MARKERS_TEXT)


  private[picta] def serialize: Value = {
    val meta = Obj(
      "name" -> name,
      "type" -> `type`.toString.toLowerCase,
    )

    val mode_ = symbol.option match {
      case Some(x) => Obj("mode" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val xaxis_ = xaxis.option match {
      case Some(x) => if (`type` != PIE && x.getPosition() != "") Obj("xaxis" -> ("x" + x.getPosition())) else jsonMonoid.empty
      case _ => jsonMonoid.empty
    }

    val yaxis_ = yaxis.option match {
      case Some(x) => if (`type` != PIE && x.getPosition() != "") Obj("yaxis" -> ("y" + x.getPosition())) else jsonMonoid.empty
      case _ => jsonMonoid.empty
    }

    val marker_ = marker.option match {
      case Some(x) => Obj("marker" -> x.serialize)
      case None => jsonMonoid.empty
    }

    /** No need to add a key as this object merges directly */
    val hist_options_ : Value = hist_options.option match {
      case Some(x) => if (`type` == HISTOGRAM) x.serialize else jsonMonoid.empty
      case None => jsonMonoid.empty
    }

    /** No need to add a key as this object merges directly */
    val hist2d_options_ : Value = hist2d_options.option match {
      case Some(x) => if (`type` == HISTOGRAM2DCONTOUR) x.serialize else jsonMonoid.empty
      case None => jsonMonoid.empty
    }

    List(meta, mode_, xaxis_, yaxis_, marker_, hist_options_, hist2d_options_, createSeries)
      .foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }

  private def createSeries(): Value = {
    val xkey: String = "x"
    val ykey: String = "y"

    val y_ = y.option match {
      case Some(x) => x
      case _ => Nil
    }

    (x, y_, `type`) match {
      case (x, Nil, HISTOGRAM) => createSeriesXY(x, getHistOrientation(hist_options))
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

  private def getHistOrientation(hist_options: Opt[HistOptions]): String = hist_options.option match {
      case Some(x) => x.orientation.toString
      case _ => "x"
  }
}