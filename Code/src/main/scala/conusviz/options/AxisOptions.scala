package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}
import upickle.default.{macroRW, ReadWriter => RW, _}

sealed trait AxisOptions extends Component

object AxisOptions {
  case class Axis(key: String, title: String = "title", side: String ="", overlaying: String = "",
                  showgrid: Boolean = true, zeroline: Boolean = false, showline: Boolean = true) extends AxisOptions {

    def serialize(): Value = {
      val raw = Obj(key -> Obj("title" -> title, "side" -> side, "overlaying" -> overlaying, "showgrid" -> showgrid, "zeroline" -> zeroline, "showline" -> showline))
      transform(raw).to(Value)
    }
  }
}
