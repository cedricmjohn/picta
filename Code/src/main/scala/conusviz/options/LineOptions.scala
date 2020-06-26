package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}
import conusviz.Utils._

sealed trait LineOptions extends Component

object LineOptions {
  case class Line(width: List[Double] = List(0.5), color: Option[String] = None) {
    def serialize(): Value = {
      var acc = emptyObject.obj ++ Obj("width" -> width).obj
      color match {
        case Some(color) =>  acc ++ Obj("color" -> color).obj
        case None => acc
      }
    }
  }
}