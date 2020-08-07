package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.ColorOptions.Color
import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid._
import ujson.{Obj, Value}

/** This case class specifies how a line is rendered for a plot.
 *
 * @param width : Sets the line width.
 * @param color : Sets the color for the line.
 */
final case class Line[T: Color](width: Double = 0.5, color: Opt[List[T]] = Empty) extends Component {

  private val c = implicitly[Color[T]]

  def setWidth(new_width: Double): Line[T] = this.copy(width = new_width)

  def setColor[T: Color](new_color: List[T]): Line[T] = this.copy(color = new_color)

  def setColor(new_color: String): Line[String] = this.copy(color = List(new_color))

  def setColor(new_color: String*): Line[String] = this.copy(color = new_color.toList)

  def setColor(new_color: Double): Line[Double] = this.copy(color = List(new_color))

  private[picta] def serialize(): Value = {
    val acc = Obj("width" -> width)

    val color_ = color.option match {
      case Some(lst) if (lst != Nil) => Obj("color" -> c.serialize(lst))
      case _ => jsonMonoid.empty
    }

    List(acc, color_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}