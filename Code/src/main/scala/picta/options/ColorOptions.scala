package picta.options

import picta.common.Component
import ujson.{Obj, Value}

sealed trait Color extends Component

object ColorOptions {

  case class ColorString(color: String) extends Color {
    def serialize(): Value = Obj("color" -> color)
  }
  case class ColorList(color: List[Double]) extends Color {
    def serialize(): Value = Obj("color" -> color)
  }
}
