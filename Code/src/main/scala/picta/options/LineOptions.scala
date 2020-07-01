package picta.options

import picta.common.Component
import ujson.{Obj, Value}

object LineOptions {
  case class Line(width: Double = 0.5, color: Option[String] = None) extends Component {
    def serialize(): Value = {
      val acc = Obj("width" -> width)
      color match {
        case Some(color) =>  acc.obj ++ Obj("color" -> color).obj
        case None => acc
      }
    }
  }
}
