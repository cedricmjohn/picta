package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}

sealed trait LineOptions extends Component

object LineOptions {
  case class Line(width: List[Double] = List(0.5), color: Option[String] = None) {
    def serialize(): Value = {
      val acc = Obj("width" -> width)
      color match {
        case Some(color) =>  acc.obj ++ Obj("color" -> color).obj
        case None => acc
      }
    }
  }
}
