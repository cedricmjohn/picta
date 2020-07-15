package org.carbonateresearch.picta.options.histogram

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid._
import ujson.{Obj, Value}

sealed trait Direction

case object INCREASING extends Direction

case object DECREASING extends Direction

sealed trait CurrentBin

case object INCLUDE extends CurrentBin

case object EXCLUDE extends CurrentBin

case object HALF extends CurrentBin

/** This is an option for a histogram chart. If enabled == true, it shows cumulative distribution by summing the bin
 * values.
 *
 * @param enabled    : A boolean that specifies whether the cumulative distribution is enabled.
 * @param direction  : If 'increasing' will sum all prior bins. if 'decreasing' will sum the later bins.
 * @param currentbin : only applies of enabled == true. Sets whether the bin is included, excluded, or has half of its value
 *                   in the current cumulative value.
 *
 */
final case class Cumulative(enabled: Opt[Boolean] = Blank, direction: Opt[Direction] = Blank,
                      currentbin: Opt[CurrentBin] = Blank) extends Component {

  private[picta] def serialize: Value = {
    val enabled_ = enabled.option match {
      case Some(x) => Obj("enabled" -> x)
      case None => jsonMonoid.empty
    }

    val direction_ = direction.option match {
      case Some(x) => Obj("direction" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val currentbin_ = currentbin.option match {
      case Some(x) => Obj("currentbin" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    List(enabled_, direction_, currentbin_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}