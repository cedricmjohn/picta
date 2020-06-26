package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}
import upickle.default.{macroRW, ReadWriter => RW, _}
import conusviz.Utils._

sealed trait AxisOptions extends Component

object AxisOptions {
  case class Axis(key: String, title: String = "variable", side: Option[String] = None, overlaying: Option[String] = None,
                  showgrid: Boolean = true, zeroline: Boolean = false, showline: Boolean = false) extends AxisOptions {

    def serialize(): Value = {
      var acc = emptyObject.obj ++ Obj(
        "title" -> Obj("text" -> title),
        "showgrid" -> showgrid,
        "zeroline" -> zeroline,
        "showline" -> showline
      ).obj

      side match {
        case Some(_) => acc.obj ++ Obj("side" -> side).obj
        case None => ()
      }

      overlaying match {
        case Some(_) => acc.obj ++ Obj("overlaying" -> overlaying).obj
        case None => ()
      }

      Obj(key -> acc)
    }
  }
}
