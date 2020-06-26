package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}

trait LegendOptions extends Component

object LegendOptions {
  case class Legend(x: Double = 0, y: Double = 1, orientation: String = "v") extends LegendOptions {
    def serialize: Value = Obj("x" -> x, "y" -> y, "orientation" -> orientation)
  }
}
