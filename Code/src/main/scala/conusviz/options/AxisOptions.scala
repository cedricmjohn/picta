package conusviz.options

import ujson.{Obj, Value}
import upickle.default.{macroRW, ReadWriter => RW, _}

sealed trait AxisOptions

object AxisOptions {
  case class Axis
  (name: String, title: String, showgrid: Boolean = true, zeroline: Boolean = true, showline: Boolean = true) extends AxisOptions {

    if ( !(Axis.acceptableSetofNames contains name) )
      throw new IllegalArgumentException("name must be one of: 'xaxis', 'yaxis', 'xaxis2', 'yaxis2")

    def createAxis(): Value = {
      val raw = Obj(name -> Obj("title" -> title, "showgrid" -> showgrid, "zeroline" -> zeroline, "showline" -> showline))
      transform(raw).to(Value)
    }

  }

  object Axis {
    val acceptableSetofNames = Set(
      "xaxis",
      "yaxis",
      "xaxis2",
      "yaxis2"
    )
  }
}
