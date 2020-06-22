package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}

trait LegendOptions extends Component

object LegendOptions {

  case class Legend(showlegend: Boolean = true, x: Double, y: Double) extends LegendOptions {
    def serialize: Value = {
      Obj("showlegend" -> showlegend, "x" -> x, "y" -> y)
    }
  }

}
