/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta

import org.carbonateresearch.picta.ColorOptions.Color
import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.SymbolShape.SymbolShape
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.common.Serializer
import org.carbonateresearch.picta.common.Utils._
import org.carbonateresearch.picta.options._
import org.carbonateresearch.picta.options.histogram._
import org.carbonateresearch.picta.options.histogram2d.Hist2dOptions
import ujson.{Obj, Value}

import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

/** ENUM for the XY chart series types */
private[picta] sealed trait XYType

case object SCATTER extends XYType

case object SCATTERGL extends XYType

case object BAR extends XYType

case object HISTOGRAM2DCONTOUR extends XYType

case object HISTOGRAM extends XYType

case object PIE extends XYType

/**
 *
 * @param x              : The data series for the first dimension.
 * @param y              : The data series for the second dimension.
 * @param name           : The name for the series.
 * @param `type`         : The type for the series, which determines how it is rendered.
 * @param style          : This specifies the style for the marker.
 * @param xaxis          : This specifies the xaxis that the series maps to.
 * @param yaxis          : This specifies the yaxis that the series maps to.
 * @param marker         : This is a Marker component that sets a range of marker-specific options.
 * @param hist_options   : This is a HistOptions component that sets a range of histogram specific options
 * @param hist2d_options : This is a Hist2dOptions component that sets 2D histogram specific options.
 * @param xerror         : This specifies the behaviour of the x error bar.
 * @param yerror         : This specifies the behaviour of the y error bar.
 * @tparam T0
 * @tparam T1
 * @tparam T2
 * @tparam T3
 */
final case class XY[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
(x: List[T0], y: Opt[List[T1]] = Empty, name: String = generateRandomText, `type`: XYType = SCATTER,
 style: Opt[Style] = Blank, xaxis: Opt[Axis] = Blank, yaxis: Opt[Axis] = Blank, marker: Opt[Marker[T2, T3]] = Blank,
 hist_options: Opt[HistOptions] = Blank, hist2d_options: Opt[Hist2dOptions] = Blank, xerror: Opt[XError] = Blank,
 yerror: Opt[YError] = Blank, text: Opt[List[String]] = Empty) extends Series {

  val classification: String = "xy"

  def setName(new_name: String): XY[T0, T1, T2, T3] = this.copy(name = new_name)

  def setMarker[Z0: Color, Z1: Color](new_marker: Marker[Z0, Z1]): XY[T0, T1, Z0, Z1] = this.copy(marker = new_marker)

  def setMarker[Z0: Color, Z1: Color]
  (symbol: Opt[SymbolShape] = Blank, color: Opt[List[Z0]] = Empty, line: Opt[Line[Z1]] = Blank,
   size: Opt[List[Int]] = Empty): XY[T0, T1, Z0, Z1] = {
    val new_marker = options.Marker(symbol, color, line, size)
    this.copy(marker = new_marker)
  }

  def setHistOptions(new_histoption: HistOptions) = this.copy(hist_options = new_histoption)

  def setHistOptions(orientation: Orientation = VERTICAL, cumulative: Opt[Cumulative] = Blank,
                     histnorm: Opt[HistNorm] = Blank, histfunc: Opt[HistFunction] = Blank,
                     xbins: Opt[Xbins] = Blank, ybins: Opt[Ybins] = Blank): XY[T0, T1, T2, T3] = {

    val new_hist_options = HistOptions(orientation, cumulative, histnorm, histfunc, xbins, ybins)
    this.copy(hist_options = new_hist_options)
  }

  def setHist2dOptions(new_hist2doption: Hist2dOptions) = this.copy(hist2d_options = new_hist2doption)

  def setHist2dOptions(ncontours: Opt[Int] = Blank, reversescale: Opt[Boolean] = Opt(Option(true)),
                       showscale: Opt[Boolean] = Blank): XY[T0, T1, T2, T3] = {

    val new_hist2d_options = Hist2dOptions(ncontours, reversescale = reversescale, showscale = showscale)
    this.copy(hist2d_options = new_hist2d_options)
  }

  def setAxes(new_xaxis: Axis, new_yaxis: Axis): XY[T0, T1, T2, T3] = this.copy(xaxis = new_xaxis, yaxis = new_yaxis)

  def setAxis(new_axis: Axis): XY[T0, T1, T2, T3] = new_axis.`type` match {
    case X => this.copy(xaxis = new_axis)
    case Y => this.copy(yaxis = new_axis)
  }

  def setErrorBars(error: ErrorBar) = {
    error match {
      case x: XError => this.copy(xerror = x)
      case y: YError => this.copy(yerror = y)
    }
  }

  def asType(new_type: XYType): XY[T0, T1, T2, T3] = this.copy(`type` = new_type)

  def asScatter(): XY[T0, T1, T2, T3] = this.copy(`type` = SCATTER)

  def asScatterGl(): XY[T0, T1, T2, T3] = this.copy(`type` = SCATTERGL)

  def asBar(): XY[T0, T1, T2, T3] = this.copy(`type` = BAR)

  def asHistogram2dContour(): XY[T0, T1, T2, T3] = this.copy(`type` = HISTOGRAM2DCONTOUR)

  def asHistogram(): XY[T0, T1, T2, T3] = this.copy(`type` = HISTOGRAM)

  def asPIE(): XY[T0, T1, T2, T3] = this.copy(`type` = PIE)

  def drawStyle(new_mode: Style) = this.copy(style = new_mode)

  def drawLines() = this.copy(style = LINES)

  def drawMarkers() = this.copy(style = MARKERS)

  def drawLinesMarkers() = this.copy(style = LINES_MARKERS)

  def drawText() = this.copy(style = TEXT)

  def drawLinesText() = this.copy(style = LINES_TEXT)

  def drawMarkersText() = this.copy(style = MARKERS_TEXT)

  def drawNone() = this.copy(style = NONE)

  def drawLinesMarkersText() = this.copy(style = LINES_MARKERS_TEXT)

  def setColor[Z: Color](color: Z) = {
    val new_marker = marker.option match {
      case Some(x) => x setColor color
      case _ => Marker[Z, T3]() setColor color
    }
    this.copy(marker = new_marker)
  }

  def setColor[Z: Color](color: List[Z]) = {
    val new_marker = marker.option match {
      case Some(x) => x setColor color
      case _ => Marker[Z, T3]() setColor color setColorBar ColorBar()
    }
    this.copy(marker = new_marker)
  }

  def setColorBar(title: String = "", title_side: Opt[Side] = Blank) = {
    val title_side_ = title_side.option match {
      case Some(x) => x
      case _ => RIGHT_SIDE
    }

    val new_colorbar = ColorBar(title, title_side_)

    val new_marker = marker.option match {
      case Some(x) => x setColorBar new_colorbar
      case _ => Marker[T2, T3]() setColorBar ColorBar(title, title_side_)
    }
    this.copy(marker = new_marker)
  }

  def addAnnotations(new_text: List[String]) = this.copy(text=new_text)

  def addAnnotations(new_text: String*) = this.copy(text=new_text.toList)

  private[picta] def serialize: Value = {
    val meta = Obj(
      "name" -> name,
      "type" -> `type`.toString.toLowerCase,
    )

    val mode_ = style.option match {
      case Some(x) => Obj("mode" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val xaxis_ = xaxis.option match {
      case Some(x) =>
        if (`type` != PIE && x.getPosition() != "") Obj("xaxis" -> ("x" + x.getPosition())) else jsonMonoid.empty
      case _ => jsonMonoid.empty
    }

    val yaxis_ = yaxis.option match {
      case Some(x) =>
        if (`type` != PIE && x.getPosition() != "") Obj("yaxis" -> ("y" + x.getPosition())) else jsonMonoid.empty
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

    val xerror_ = xerror.option match {
      case Some(x) => Obj("error_x" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val yerror_ = yerror.option match {
      case Some(x) => Obj("error_y" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val text_ = text.option match {
      case Some(x) => Obj("text" -> x)
      case None => jsonMonoid.empty
    }

    List(meta, mode_, xaxis_, yaxis_, marker_, hist_options_, hist2d_options_, createSeries, xerror_, yerror_, text_)
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

object XY {
  /* Alternative constructor for a Pie chart which utilizes PieElements */
  def apply[T2: Color, T3: Color]
  (x: List[PieElement]): XY[Double, String, T2, T3] = {

    val labels = ListBuffer[String]()
    val values = ListBuffer[Double]()

    for (i <- 0 until x.length) {
      val name = x(i).name
      val value = x(i).value

      labels += name
      values += value
    }

    XY[Double, String, T2, T3](x = values.toList, y = labels.toList, `type` = PIE)
  }
}