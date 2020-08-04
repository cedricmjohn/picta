package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid._
import ujson.{Obj, Value}

sealed trait AxisType
case object X extends AxisType
case object Y extends AxisType
case object Z extends AxisType

final case class Axis (`type`: AxisType, position: Opt[Int] = Blank, title: Opt[String] = Blank, side: Opt[Side] = Blank, overlaying: Opt[Axis] = Blank,
                        domain: Opt[(Double, Double)] = Blank, range: Opt[(Double, Double)] = Blank, tickformat: Opt[String] = Blank,
                        logarithmic: Boolean = false, reversed: Boolean = false, showgrid: Boolean = true, zeroline: Boolean = false,
                        showline: Boolean = false, start_tick: Opt[Double] = Blank, tick_gap: Opt[Double] = Blank) extends Component {

  val orientation: String = `type` match {
    case X => "xaxis"
    case Y => "yaxis"
    case Z => "zaxis"
  }

  def setTitle(new_title: String): Axis = this.copy(title = new_title)

  def setDomain(new_domain: (Double, Double)): Axis = this.copy(domain = new_domain)

  def setLimits(new_range: (Double, Double)): Axis = {
      logarithmic match {
        case true =>
          val lower = if (new_range._1 == 0.0) 0.0 else scala.math.log10(new_range._1)
          val upper = scala.math.log10(new_range._2)
          val range = (lower, upper)
          this.copy(range = range)
        case false => this.copy(range = new_range)
      }
    }

  def setTickDisplayFormat(new_format: String): Axis = this.copy(tickformat = new_format)

  def drawLog(log: Boolean = true): Axis = {
    range.option match {
      case Some(x) =>this.copy(logarithmic = log) setLimits x
      case _ => this.copy(logarithmic = log)
    }
  }

  def drawReverseAxis(reverse: Boolean = true): Axis = this.copy(reversed = reverse)

  def setTickGap(gap: Double) = this.copy(tick_gap = gap)

  def setStartingTick(start: Double) = this.copy(start_tick = start)

  private[picta] def getPosition(): String = position.option match {
    case Some(x) if x != 1 =>  x.toString
    case _ => ""
  }

  /** An internal function that converts a user entered key into one that the plotly library can understand */
  private[picta] def convertPosition(position: String): String = orientation + position

  private[picta] def serialize: Value = {
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

    val tickformat_ = tickformat.option match {
      case Some(x) => Obj("tickformat" ->x)
      case None => jsonMonoid.empty
    }

    val logarthmic_ = logarithmic match {
      case true => Obj("type" -> "log")
      case false => jsonMonoid.empty
    }

    val reversed_ = reversed match {
      case true => Obj("autorange" -> "reversed")
      case false => jsonMonoid.empty
    }

    val start_tick_ = start_tick.option match {
      case Some(x) => Obj("tick0" -> x)
      case _ => jsonMonoid.empty
    }

    val tick_gap_ = tick_gap.option match {
      case Some(x) => Obj("dtick" -> x)
      case _ => jsonMonoid.empty
    }

    val position_ = getPosition()

    Obj(convertPosition(position_) ->
      List(title_, meta, side_, overlaying_, domain_, range_, tickformat_, logarthmic_, reversed_, start_tick_, tick_gap_)
        .foldLeft(jsonMonoid.empty)((a, x) => a |+| x))
  }
}

object Axis {
  def setTitle(new_title: String)(axis: Axis) = axis setTitle new_title
  def setLimits(new_range: (Double, Double))(axis: Axis) = axis setLimits(new_range)
  def setTickDisplayFormat(new_format: String)(axis: Axis) = axis setTickDisplayFormat new_format
  def drawLog(log: Boolean = true)(axis: Axis) = axis drawLog log
  def drawReverseAxis(reverse: Boolean = true)(axis: Axis) = axis drawReverseAxis reverse
  def setTickGap(gap: Double)(axis: Axis) = axis setTickGap gap
  def setStartingTick(start: Double)(axis: Axis)= axis setStartingTick start
}