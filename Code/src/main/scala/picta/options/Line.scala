package picta.options

import picta.common.Component
import picta.common.Monoid._
import picta.common.OptionWrapper._
import picta.options.ColorOptions.Color
import ujson.{Obj, Value}

/**
 * @constructor: This constructs the line for a chart.
 * @param width : Sets the line width.
 * @param color : Sets the color for the line.
 */
case class Line[T: Color](width: Double = 0.5, color: Opt[List[T]] = Empty) extends Component {

  private val c = implicitly[Color[T]]

  def +[T: Color](new_color: List[T]): Line[T] = this.copy(color = new_color)

  def +(new_color: String): Line[String] = this.copy(color = List(new_color))

  def serialize(): Value = {
    val acc = Obj("width" -> width)

    val color_ = color.asOption match {
      case Some(lst) if (lst != Nil) => Obj("color" -> c.serialize(lst))
      case _ => jsonMonoid.empty
    }

    List(acc, color_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}