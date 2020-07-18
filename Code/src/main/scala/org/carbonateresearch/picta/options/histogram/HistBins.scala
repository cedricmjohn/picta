package org.carbonateresearch.picta.options.histogram

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.common.Monoid._
import org.carbonateresearch.picta.OptionWrapper._
import ujson.{Obj, Value}

private[picta] trait HistBins extends Component {
  /** The starting value for the bin. Defaults to the minimum data value. */
  val start: Opt[Double]
  /** The ending value for the bin. Defaults to the maximum data value. */
  val end: Opt[Double]
  /** Sets the size of each bin. */
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

final case class Xbins(start: Opt[Double] = Blank, end: Opt[Double] = Blank, size: Opt[Double] = Blank) extends HistBins

final case class Ybins(start: Opt[Double] = Blank, end: Opt[Double] = Blank, size: Opt[Double] = Blank) extends HistBins