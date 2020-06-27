package conusviz.options

import conusviz.common.Component
import conusviz.options.LineOptions.Line
import ujson.{Obj, Value}
import conusviz.Utils._

sealed trait MarkerOptions extends Component

object MarkerOptions {
  case class Marker(symbol: Option[String] = None, color: Option[String] = None, line: Option[Line] = None) {
    def serialize(): Value = {
      val s = symbol match {
        case Some(_) => Obj("symbol" -> symbol)
        case None => emptyObject
      }

      val c = color match {
        case Some(_) => Obj("color" -> color)
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