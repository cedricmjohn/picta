package picta.options

import picta.Utils.emptyObject
import picta.common.Component
import picta.options.ColorOptions.{Color}
import ujson.{Obj, Value}

/**
 * @constructor: This constructs the line for a chart.
 * @param width: Sets the line width.
 * @param color: Sets the color for the line.
 */
case class Line[T: Color](width: Double = 0.5, color: Option[List[T]] = Some(Nil)) extends Component {

  private val c = implicitly[Color[T]]

  def +[T: Color](new_color: List[T]): Line[T] = this.copy(color = Some(new_color))
  def +(new_color: String): Line[String] = this.copy(color = Some(List(new_color)))

  def serialize(): Value = {
    val acc = Obj("width" -> width)

    val color_ = color match {
      case Some(lst) if (lst != Nil) => Obj("color" -> c.serialize(lst))
      case _ =>  emptyObject
    }

    List(acc, color_).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}

object Line {
  def apply(width: Double, color: String): Line[String] = Line(width=width, color = Some(List(color)))
  def apply(color: String): Line[String] = Line(color = Some(List(color)))
  def apply[T: Color](color: List[T]): Line[T] = Line(color = Some(color))
}