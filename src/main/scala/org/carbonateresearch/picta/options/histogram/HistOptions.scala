/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta.options.histogram

import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid._
import org.carbonateresearch.picta.{Component, Orientation, VERTICAL}
import ujson.{Obj, Value}

/** Specifies how the histogram is normalized. */
sealed trait HistNorm

/** The length of each bar equals the percentage of occurrences with respect to the total number of points. */
case object PERCENT extends HistNorm

/** The length of each bar equals the number of occurrences in a bin divided by the length of the bin interval. */
case object DENSITY extends HistNorm

/** The area of each bar corresponds to the probability that an event will fall into the corresponding bin (here, the sum of all bin AREAS equals 1). */
case object PROBABILITY_DENSITY extends HistNorm

/** The length of each bar corresponds to the number of occurrences. */
case object NUMBER extends HistNorm {
  override def toString: String = ""
}

/** Specifies the binning function used for the histogram. */
sealed trait HistFunction

/** Histogram values are equal to the number of values lying in the bin.  */
case object COUNT extends HistFunction

/** Histogram values are equal to the sum of the values lying in the bin.  */
case object SUM extends HistFunction

/** Histogram values are equal to the average of the values lying in the bin.  */
case object AVG extends HistFunction

/** Histogram values are equal to the min of the values lying in the bin.  */
case object MIN extends HistFunction

/** Histogram values are equal to the max of the values lying in the bin.  */
case object MAX extends HistFunction

/** This class sets the options for a histogram. Generally this will be manipulated by a component that sits higher up
 *  in the class hierarchy.
 *
 * @param orientation : Specifies whether the histogram is vertical or horizontal.
 * @param cumulative  : This takes in a Cumulative type which specifies how the histogram cumulative function works.
 * @param histnorm    : Specifies the type of normalization for the histogram series.
 * @param histfunc    : Specifies the binning function for the histogram series.
 * @param xbins       : An Xbin specified by the user.
 * @param ybins       : A Ybin specified by the user.
 */
final case class HistOptions(orientation: Orientation = VERTICAL, cumulative: Opt[Cumulative] = Blank,
                             histnorm: Opt[HistNorm] = Blank, histfunc: Opt[HistFunction] = Blank,
                             xbins: Opt[Xbins] = Blank, ybins: Opt[Ybins] = Blank) extends Component {

  def setOrientation(new_orientation: Orientation) = this.copy(orientation = new_orientation)

  def setCumulative(enabled: Opt[Boolean] = Blank, direction: Opt[Direction] = Blank, currentbin: Opt[CurrentBin] = Blank) = {
    this.copy(cumulative = Cumulative(enabled, direction, currentbin))
  }

  def setCumulative(new_cumulative: Cumulative) = this.copy(cumulative = new_cumulative)

  def setHistNorm(new_histnorm: HistNorm) = this.copy(histnorm = new_histnorm)

  def setHistFunc(new_histfunc: HistFunction) = this.copy(histfunc = new_histfunc)

  def setXBins(new_xbins: Xbins): HistOptions = this.copy(xbins = new_xbins)

  def setXbins(start: Opt[Double] = Blank, end: Opt[Double] = Blank, size: Opt[Double] = Blank) = {
    this.copy(xbins = Xbins(start = start, end = end, size = size))
  }

  def setYBins(new_ybins: Ybins): HistOptions = this.copy(ybins = new_ybins)

  def setYbins(start: Opt[Double] = Blank, end: Opt[Double] = Blank, size: Opt[Double] = Blank) = {
    this.copy(ybins = Ybins(start = start, end = end, size = size))
  }

  private[picta] def serialize(): Value = {
    val cumulative_ = cumulative.option match {
      case Some(x) => Obj("cumulative" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val histnorm_ = histnorm.option match {
      case Some(x) => Obj("histnorm" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val histfunc_ = histfunc.option match {
      case Some(x) => Obj("histfunc" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val xbins_ = xbins.option match {
      case Some(x) => Obj("xbins" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val ybins_ = ybins.option match {
      case Some(x) => Obj("ybins" -> x.serialize)
      case None => jsonMonoid.empty
    }

    List(cumulative_, histnorm_, histfunc_, xbins_, ybins_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}