package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}

trait LegendOptions extends Component

object LegendOptions {
  case class Legend(x: Double = 0.5, y: Double = -0.2, orientation: String = "h", xanchor: String = "auto",
                    yanchor: String = "auto") extends LegendOptions {
    def serialize: Value = Obj("x" -> x, "y" -> y, "orientation" -> orientation, "xanchor" -> xanchor, "yanchor" -> yanchor)
  }
}
