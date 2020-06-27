package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}
import conusviz.Utils._

sealed trait AxisOptions extends Component

object AxisOptions {
  case class Axis(key: String, title: String = "variable", side: Option[String] = None, overlaying: Option[String] = None,
                  showgrid: Boolean = true, zeroline: Boolean = false, showline: Boolean = false) extends AxisOptions {

    def serialize(): Value = {
      val meta = Obj(
        "title" -> Obj("text" -> title),
        "showgrid" -> showgrid,
        "zeroline" -> zeroline,
        "showline" -> showline
      )

      val s = side match {
        case Some(s) => Obj("side" -> s)
        case None => emptyObject
      }

      val o = overlaying match {
        case Some(o) => Obj("overlaying" -> o)
        case None => emptyObject
      }

      val acc = meta.obj ++ s.obj ++ o.obj

      Obj(key -> acc)
    }
  }
}
