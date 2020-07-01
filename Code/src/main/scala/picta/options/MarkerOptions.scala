package picta.options

import picta.common.Component
import picta.options.ColorOptions._
import picta.options.LineOptions.Line
import ujson.{Obj, Value}
import picta.Utils._

object MarkerOptions {
  case class Marker(symbol: Option[String] = None, color: Option[Color] = None, line: Option[Line] = None) extends Component {

    def +(new_line: Line): Marker = this.copy(line = Some(new_line))
    def +(new_color: Color): Marker = this.copy(color = Some(new_color))

    def serialize(): Value = {
      val symbol_ = symbol match {
        case Some(s) => Obj("symbol" -> s)
        case None => emptyObject
      }

      val color_ = color match {
        case Some(c) => c match {
          case s: ColorString => s.serialize()
          case l: ColorList => l.serialize()
          case _ => emptyObject
        }
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
    def apply(symbol: String, color: Color, line: Line): Marker = Marker(Some(symbol), Some(color), Some(line))
    def apply(symbol: String, color: Color ): Marker = Marker(symbol=Some(symbol), color=Some(color))
    def apply(symbol: String, line: Line): Marker = Marker(symbol=Some(symbol), line=Some(line))
    def apply(color: Color, line: Line): Marker = Marker(color=Some(color), line=Some(line))
    def apply(symbol: String): Marker = Marker(symbol=Some(symbol))
    def apply(color: Color): Marker = Marker(color=Some(color))
    def apply(line: Line): Marker = Marker(line=Some(line))
  }
}