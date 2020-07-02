package picta.options

import picta.common.Component
import picta.options.ColorOptions._
import ujson.{Obj, Value}
import picta.Utils._

case class Marker[T0: Color, T1: Color](symbol: Option[String] = None, color: Option[List[T0]] = None, line: Option[Line[T1]] = None)
  extends Component {

  val c0 = implicitly[Color[T0]]
  val c1 = implicitly[Color[T1]]

  def +[Z: Color](new_line: Line[Z]): Marker[T0, Z] = this.copy(line=Some(new_line))
  def +[Z: Color](new_color: List[Z]): Marker[Z, T1] = Marker(color = Some(new_color))

  def serialize(): Value = {
    val symbol_ = symbol match {
      case Some(s) => Obj("symbol" -> s)
      case None => emptyObject
    }

    val color_ = color match {
      case Some(lst: List[T0]) => Obj("color" -> c0.serialize(lst))
      case None => emptyObject
    }

    val line_ = line match {
      case Some(l) => Obj("line" -> l.serialize())
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