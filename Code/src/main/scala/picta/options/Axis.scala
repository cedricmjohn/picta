package picta.options

import picta.common.Component
import picta.common.Monoid._
import picta.common.OptionWrapper._
import ujson.{Obj, Value}

/**
 * @constructor Creates a new Axis that the user can configure with different options
 * @param key        : This is used to give an axis a key. This is used by a Series to map to the corresponding axis.
 * @param title      : This sets the axis title.
 * @param side       : This determines which side the axis will be shown on.
 * @param overlaying : This is used if we have more than one axis, and want to set which base axis it is mirroring.
 * @param showgrid   : Determines whether the grid is shown on the Chart.
 * @param zeroline   : Determines whether the zeroline for each axis are shown.
 * @param showline   : Determines whether the axis is visibly drawn on the chart.
 */
case class Axis(key: String, title: Opt[String] = Blank, side: Opt[String] = Blank, overlaying: Opt[String] = Blank,
                domain: Opt[(Double, Double)] = Blank, showgrid: Boolean = true, zeroline: Boolean = false,
                showline: Boolean = false) extends Component {

  def serialize(): Value = {
    val meta = Obj(
      "showgrid" -> showgrid,
      "zeroline" -> zeroline,
      "showline" -> showline
    )

    val title_ = title.asOption match {
      case Some(x) => Obj("title" -> Obj("text" -> x))
      case None => JsonMonoid.empty
    }

    val side_ = side.asOption match {
      case Some(x) => Obj("side" -> x)
      case None => JsonMonoid.empty
    }

    val overlaying_ = overlaying.asOption match {
      case Some(x) => Obj("overlaying" -> x)
      case None => JsonMonoid.empty
    }

    val domain_ = domain.asOption match {
      case Some(x) => Obj("domain" -> List(x._1, x._2))
      case None => JsonMonoid.empty
    }

    Obj(key -> List(title_, meta, side_, overlaying_, domain_).foldLeft(JsonMonoid.empty)((a, x) => a |+| x))
  }
}