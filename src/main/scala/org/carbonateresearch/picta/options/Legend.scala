package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.OptionWrapper.{Opt, Blank}
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import ujson.{Obj, Value}

sealed trait Anchor
case object LEFT extends Anchor
case object RIGHT extends Anchor
case object CENTER extends Anchor
case object AUTO extends Anchor


/** This configures the legend for a chart.
 *
 * @param x           : x-coordinate for positioning legend on the chart.
 * @param y           : y-coordinate for positioning the legend on the chart.
 * @param orientation : Specifies whether the legend is horizontal or vertical.
 * @param xanchor     :
 * @param yanchor     :
 */
final case class Legend(x: Opt[Double] = Blank, y: Opt[Double] = Blank, orientation: Orientation = VERTICAL,
                        xanchor: Opt[Anchor] = Blank, yanchor: Opt[Anchor] = Blank) extends Component {

  def setPosition(new_x: Double, new_y: Double) = this.copy(x = new_x, y = new_y)

  def setOrientation(new_orientation: Orientation) = this.copy(orientation = new_orientation)

  def setXAnchor(new_xanchor: Anchor) = this.copy(xanchor = new_xanchor)

  def setYAnchor(new_yanchor: Anchor) = this.copy(yanchor = new_yanchor)

  private[picta] def serialize: Value = {

    val orientation_ = orientation match {
      case VERTICAL => Obj("orientation" -> "v")
      case HORIZONTAL => Obj("orientation" -> "h")
    }

    val x_ = x.option match {
      case Some(x) => Obj("x" -> x)
      case _ => jsonMonoid.empty
    }

    val y_ = y.option match {
      case Some(x) => Obj("y" -> x)
      case _ => jsonMonoid.empty
    }

    val xanchor_ = xanchor.option match {
      case Some(x) => Obj("xanchor" -> x.toString.toLowerCase)
      case _ => jsonMonoid.empty
    }

    val yanchor_ = yanchor.option match {
      case Some(x) => Obj("yanchor" -> x.toString.toLowerCase)
      case _ => jsonMonoid.empty
    }

    List(orientation_, x_, y_, xanchor_, yanchor_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}