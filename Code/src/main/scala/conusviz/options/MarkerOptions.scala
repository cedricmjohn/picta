package conusviz.options

import conusviz.common.Component
import conusviz.options.LineOptions.Line
import ujson.{Obj, Value}
import conusviz.Utils._

sealed trait MarkerOptions extends Component

object MarkerOptions {
  case class Marker(symbol: Option[String] = None, color: Option[String] = None, line: Option[Line] = None) {
    def serialize(): Value = {

      var acc = emptyObject

      symbol match {
        case Some(_) => acc.obj ++ Obj("symbol" -> symbol).obj
        case None => ()
      }

      color match {
        case Some(_) => acc.obj ++ Obj("color" -> color).obj
        case None => ()
      }

      line match {
        case Some(l) => acc.obj ++ Obj("line" -> l.serialize()).obj
        case None => ()
      }

      acc
    }
  }
}