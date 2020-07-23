package org.carbonateresearch.picta.options.histogram

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid._
import ujson.{Obj, Value}

/** A trait that specifies how the Xbins and Ybins case classes should behave. */
private[picta] trait HistBins extends Component {

  /** Starting value for the axis bin */
  val start: Opt[Double]

  /** ending value for the axis bin */
  val end: Opt[Double]

  /** size of each bin */
  val size: Opt[Double]

  private[picta] def serialize: Value = {
    val start_ = start.option match {
      case Some(x) => Obj("start" -> x)
      case None => jsonMonoid.empty
    }

    val end_ = end.option match {
      case Some(x) => Obj("end" -> x)
      case None => jsonMonoid.empty
    }

    val size_ = size.option match {
      case Some(x) => Obj("size" -> x)
      case None => jsonMonoid.empty
    }

    List(start_, end_, size_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}

/** A case class that represents the Xbins option.
 *
 * @param start : The starting value for the bin. If not provided, rendering will default to the minimum data value.
 * @param end   : The ending value for the bin. If not provided, rendering will default to the maximum data value.
 * @param size  : Sets the size of each bin.
 */
final case class Xbins(start: Opt[Double] = Blank, end: Opt[Double] = Blank, size: Opt[Double] = Blank) extends HistBins {
  def setStart(new_start: Double) = this.copy(start = new_start)

  def setEnd(new_end: Double) = this.copy(end = new_end)

  def setSize(new_size: Double) = this.copy(size = new_size)
}


/** A case class that represents the Ybins option.
 *
 * @param start : The starting value for the bin. If not provided, rendering will default to the minimum data value.
 * @param end   : The ending value for the bin. If not provided, rendering will default to the maximum data value.
 * @param size  : Sets the size of each bin.
 */
final case class Ybins(start: Opt[Double] = Blank, end: Opt[Double] = Blank, size: Opt[Double] = Blank) extends HistBins {
  def setStart(new_start: Double) = this.copy(start = new_start)

  def setEnd(new_end: Double) = this.copy(end = new_end)

  def setSize(new_size: Double) = this.copy(size = new_size)
}