package org.carbonateresearch.picta.options.histogram2d

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.common.Monoid._
import org.carbonateresearch.picta.OptionWrapper._
import ujson.{Obj, Value}

/**
 * Specifies 2d Histogram options.
 *
 * @param ncontours    : Specifies the maximum number of contour levels.
 * @param reversescale : Reverse the color mapping if set to true.
 * @param showscale    : Specifies whether the scale is shown for this series or not.
 */
final case class Hist2dOptions(ncontours: Opt[Int] = Blank, reversescale: Opt[Boolean] = Opt(Option(true)),
                         showscale: Opt[Boolean] = Blank) extends Component {

  private[picta] def serialize: Value = {
    val ncountours_ = ncontours.option match {
      case Some(x) => Obj("ncontours" -> x)
      case _ => jsonMonoid.empty
    }

    val reversescale_ = reversescale.option match {
      case Some(x) => Obj("reversescale" -> x)
      case _ => jsonMonoid.empty
    }

    val showscale_ = showscale.option match {
      case Some(x) => Obj("showscale" -> x)
      case _ => jsonMonoid.empty
    }

    List(ncountours_, reversescale_, showscale_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}