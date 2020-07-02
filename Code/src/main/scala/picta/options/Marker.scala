package picta.options

import picta.common.Component
import picta.options.ColorOptions._
import ujson.{Obj, Value}
import picta.Utils._

/**
 * @constructor: Configures a Marker component for the chart.
 * @param symbol: Specifies what symbol the marker will use on the chart.
 * @param color: Specifies the color for the symbol on a chart.
 * @param line: This configures the line for the marker. This is another Line component.
 *
 */
case class Marker[T0: Color, T1: Color](symbol: Option[String] = None, color: Option[List[T0]] = None,
                                        line: Option[Line[T1]] = None) extends Component {

  private val c0 = implicitly[Color[T0]]
  private val c1 = implicitly[Color[T1]]

  def +(new_symbol: String): Marker[T0, T1] = Marker(symbol = Some(new_symbol))
  def +[Z: Color](new_color: List[Z]): Marker[Z, T1] = Marker(color = Some(new_color))
  def +[Z: Color](new_line: Line[Z]): Marker[T0, Z] = this.copy(line=Some(new_line))

  def serialize(): Value = {
    val symbol_ = symbol match {
      case Some(x) => Obj("symbol" -> x)
      case None => emptyObject
    }

    val color_ = color match {
      case Some(lst: List[T0]) => Obj("color" -> c0.serialize(lst))
      case None => emptyObject
    }

    val line_ = line match {
      case Some(x) => Obj("line" -> x.serialize())
      case None => emptyObject
    }

    List(symbol_, color_, line_).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}

object Marker {
  def apply[T0: Color, T1: Color](symbol: String, color: List[T0], line: Line[T1]): Marker[T0, T1] =
    Marker(Some(symbol), Some(color), Some(line))

  def apply[T0: Color](symbol: String, color: List[T0]): Marker[T0, T0] =
    Marker(symbol=Some(symbol), color=Some(color))

  def apply[T0: Color](symbol: String, line: Line[T0]): Marker[T0, T0] =
    Marker(symbol=Some(symbol), line=Some(line))

  def apply[T0: Color, T1: Color](color: List[T0], line: Line[T1]): Marker[T0, T1] =
    Marker(color=Some(color), line=Some(line))

  def apply[T0: Color](symbol: String): Marker[T0, T0] = Marker(symbol=Some(symbol))

  def apply[T0: Color](color: List[T0]): Marker[T0, T0] = Marker(color=Some(color))

  def apply[T0: Color](line: Line[T0]): Marker[T0, T0] = Marker(line=Some(line))
}