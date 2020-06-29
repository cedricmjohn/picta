package conusviz.options

import conusviz.common.Component
import conusviz.options.LineOptions.Line
import ujson.{Obj, Value}
import conusviz.Utils._

sealed trait MarkerOptions extends Component



object MarkerOptions {

  // Abstract type class for Color
  sealed trait Color
  case class ColorString(value: String) extends Color
  case class ColorList(value: List[Double]) extends Color

  case class Marker(symbol: Option[String] = None, color: Option[Color] = None, line: Option[Line] = None) {

    def +(new_line: Line): Marker = this.copy(line = Some(new_line))
    def +(new_color: Color): Marker = this.copy(color = Some(new_color))

    def serialize(): Value = {
      val s = symbol match {
        case Some(s) => Obj("symbol" -> s)
        case None => emptyObject
      }

      val c = color match {
        case Some(c) => c match {
          case s: ColorString => Obj("color" -> s.value)
          case l: ColorList => Obj("color" -> l.value)
          case _ => emptyObject()
        }
        case None => emptyObject
      }

      val l = line match {
        case Some(l) => Obj("line" -> l.serialize())
        case None => emptyObject
      }

      s.obj ++ c.obj ++ l.obj
    }
  }
}