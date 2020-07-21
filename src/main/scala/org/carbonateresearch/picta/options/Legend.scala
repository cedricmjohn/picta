package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component

import ujson.{Obj, Value}

sealed trait Anchor
case object LEFT extends Anchor
case object RIGHT extends Anchor
case object CENTER extends Anchor
case object AUTO extends Anchor


/**
 * @constructor: This configures the legend for a chart
 * @param x           : x-coordinate for positioning legend on the chart.
 * @param y           : y-coordinate for positioning the legend on the chart.
 * @param orientation : Specifies whether the legend is horizontal or vertical.
 * @param xanchor     :
 * @param yanchor     :
 */
final case class Legend(x: Double = 0.5, y: Double = -0.5, orientation: Orientation = VERTICAL,
                        xanchor: Anchor = AUTO, yanchor: Anchor = AUTO) extends Component {

  def setPosition(new_x: Double, new_y: Double) = this.copy(x = new_x, y = new_y)

  def setOrientation(new_orientation: Orientation) = this.copy(orientation = new_orientation)

  def setXAnchor(new_xanchor: Anchor) = this.copy(xanchor = new_xanchor)

  def setYAnchor(new_yanchor: Anchor) = this.copy(yanchor = new_yanchor)

  private[picta] def serialize: Value = {
    val orientation_ = orientation match {
      case VERTICAL => "v"
      case HORIZONTAL => "h"
    }
    Obj(
      "x" -> x,
      "y" -> y,
      "orientation" -> orientation_,
      "xanchor" -> xanchor.toString.toLowerCase,
      "yanchor" -> yanchor.toString.toLowerCase)
  }
}