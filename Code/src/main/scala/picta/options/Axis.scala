package picta.options

import picta.common.Component
import ujson.{Obj, Value}
import picta.Utils._

/**
 * @constructor Creates a new Axis that the user can configure with different options
 * @param key: This is used to give an axis a key. This is used by a Series to map to the corresponding axis.
 * @param title: This sets the axis title.
 * @param side: This determines which side the axis will be shown on.
 * @param overlaying: This is used if we have more than one axis, and want to set which base axis it is mirroring.
 * @param showgrid: Determines whether the grid is shown on the Chart.
 * @param zeroline: Determines whether the zeroline for each axis are shown.
 * @param showline: Determines whether the axis is visibly drawn on the chart.
 */
case class Axis(key: String, title: String = "variable", side: Option[String] = None, overlaying: Option[String] = None,
                showgrid: Boolean = true, zeroline: Boolean = false, showline: Boolean = false) extends Component {

  def serialize(): Value = {
    val meta = Obj(
      "title" -> Obj("text" -> title),
      "showgrid" -> showgrid,
      "zeroline" -> zeroline,
      "showline" -> showline
    )

    val side_ = side match {
      case Some(x) => Obj("side" -> x)
      case None => emptyObject
    }

    val overlaying_ = overlaying match {
      case Some(x) => Obj("overlaying" -> x)
      case None => emptyObject
    }

    Obj(key -> List(meta, side_, overlaying_).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj))
  }
}
