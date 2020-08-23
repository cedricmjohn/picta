package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.OptionWrapper.{Blank, Opt}
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.{Component, HORIZONTAL, Orientation, VERTICAL}
import ujson.{Obj, Value}

/** This case class specifies the legend for a chart.
 *
 * @param x           : x-coordinate for positioning legend on the chart.
 * @param y           : y-coordinate for positioning the legend on the chart.
 * @param orientation : Specifies whether the legend is horizontal or vertical.
 * @param xanchor     : Specifies what point on the bottom of the chart to use as the x-coordinate.
 * @param yanchor     : Specifies what point on the side of the chart to use as the y-coordinate.
 */
final case class Legend(x: Opt[Double] = Blank, y: Opt[Double] = Blank, orientation: Opt[Orientation] = Blank,
                        xanchor: Opt[Anchor] = Blank, yanchor: Opt[Anchor] = Blank) extends Component {

  def setPosition(new_x: Double, new_y: Double) = this.copy(x = new_x, y = new_y)

  def setOrientation(new_orientation: Orientation) = this.copy(orientation = new_orientation)

  def setXAnchor(new_xanchor: Anchor) = this.copy(xanchor = new_xanchor)

  def setYAnchor(new_yanchor: Anchor) = this.copy(yanchor = new_yanchor)

  private[picta] def serialize: Value = {

    val orientation_ = orientation.option match {
      case Some(x) if x == VERTICAL => Obj("orientation" -> "v")
      case Some(x) if x == HORIZONTAL => Obj("orientation" -> "h")
      case _ => jsonMonoid.empty
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