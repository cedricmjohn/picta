package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.common.Monoid._
import org.carbonateresearch.picta.OptionWrapper._
import ujson.{Obj, Value}


trait Side
case object RIGHT extends Side
case object LEFT extends Side

/**
 * @constructor Creates a new Axis that the user can configure with different options
 * @param position   : This is used to give an axis a key. This is used by a Series to map to the corresponding axis.
 * @param title      : This sets the axis title.
 * @param side       : This determines which side the axis will be shown on.
 * @param overlaying : This is used if we have more than one axis, and want to set which base axis it is mirroring.
 * @param domain     : Determines the domain the Chart.
 * @param range      : Determines the range the Chart.
 * @param showgrid   : Determines whether the grid is shown on the Chart.
 * @param zeroline   : Determines whether the zeroline for each axis are shown.
 * @param showline   : Determines whether the axis is visibly drawn on the chart.
 */

trait Axis extends Component {
  val orientation: String
  val position: Opt[Int]
  val title: Opt[String]
  val side: Opt[Side]
  val overlaying: Opt[Axis]
  val domain: Opt[(Double, Double)]
  val range: Opt[(Double, Double)]
  val showgrid: Boolean
  val zeroline: Boolean
  val showline: Boolean

  def setTitle(new_title: String): Axis

  def setDomain(new_domain: (Double, Double)): Axis

  def setRange(new_range: (Double, Double)): Axis

  private[picta] def getPosition(): String = position.option match {
    case Some(x) if x != 1 =>  x.toString
    case _ => ""
  }

  /** An internal function that converts a user entered key into one that the plotly library can understand */
  private def convertPosition(position: String): String = orientation + position

  private[picta] def serialize(): Value = {
    val meta = Obj(
      "showgrid" -> showgrid,
      "zeroline" -> zeroline,
      "showline" -> showline
    )

    val title_ = title.option match {
      case Some(x) => Obj("title" -> Obj("text" -> x))
      case None => jsonMonoid.empty
    }

    val side_ = side.option match {
      case Some(x) => Obj("side" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val overlaying_ = overlaying.option match {
      case Some(x) if x.getPosition() != "" => Obj("overlaying" -> x.getPosition())
      case Some(x) if x.getPosition() == "" => Obj("overlaying" -> x.orientation.take(1))
      case None => jsonMonoid.empty
    }

    val domain_ = domain.option match {
      case Some(x) => Obj("domain" -> List(x._1, x._2))
      case None => jsonMonoid.empty
    }

    val range_ = range.option match {
      case Some(x) => Obj("range" -> List(x._1, x._2))
      case None => jsonMonoid.empty
    }

    val position_ = getPosition()

    Obj(convertPosition(position_) -> List(title_, meta, side_, overlaying_, domain_, range_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x))
  }
}

final case class XAxis (position: Opt[Int] = Blank, title: Opt[String] = Blank, side: Opt[Side] = Blank, overlaying: Opt[XAxis] = Blank,
                  domain: Opt[(Double, Double)] = Blank, range: Opt[(Double, Double)] = Blank, showgrid: Boolean = true,
                  zeroline: Boolean = false, showline: Boolean = false) extends Axis {

  override val orientation: String = "xaxis"

  def setTitle(new_title: String): XAxis = this.copy(title = new_title)
  def setDomain(new_domain: (Double, Double)): Axis = this.copy(domain = new_domain)
  def setRange(new_range: (Double, Double)): Axis = this.copy(range = new_range)
}

final case class YAxis (position: Opt[Int] = Blank, title: Opt[String] = Blank, side: Opt[Side] = Blank, overlaying: Opt[YAxis] = Blank,
                        domain: Opt[(Double, Double)] = Blank, range: Opt[(Double, Double)] = Blank, showgrid: Boolean = true,
                        zeroline: Boolean = false, showline: Boolean = false) extends Axis {

  override val orientation: String = "yaxis"

  def setTitle(new_title: String): YAxis = this.copy(title = new_title)
  def setDomain(new_domain: (Double, Double)): Axis = this.copy(domain = new_domain)
  def setRange(new_range: (Double, Double)): Axis = this.copy(range = new_range)
}
