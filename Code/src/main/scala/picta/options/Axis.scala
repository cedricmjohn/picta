package picta.options

import picta.common.Component
import ujson.{Obj, Value}
import picta.Utils._

case class Axis(key: String, title: String = "variable", side: Option[String] = None, overlaying: Option[String] = None,
                showgrid: Boolean = true, zeroline: Boolean = false, showline: Boolean = false) extends Component {

  def serialize(): Value = {
    val acc = Obj(
      "title" -> Obj("text" -> title),
      "showgrid" -> showgrid,
      "zeroline" -> zeroline,
      "showline" -> showline
    )

    val side_ = side match {
      case Some(s) => Obj("side" -> s)
      case None => emptyObject
    }

    val overlaying_ = overlaying match {
      case Some(o) => Obj("overlaying" -> o)
      case None => emptyObject
    }

    Obj(key -> List(side_, overlaying_).foldLeft(acc)((a, x) => a.obj ++ x.obj))
  }
}


object Axis {

}
