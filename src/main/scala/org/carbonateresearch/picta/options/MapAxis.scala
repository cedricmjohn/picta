package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import ujson.{Obj, Value}

/**
 * @constructor: component that configures the axis for a Map chart.
 * @param showgrid : Specifies whether the grid is shown.
 * @param dtick    :
 */

private[picta] trait MapAxis extends Component {
  val range: List[Double]
  val showgrid: Boolean
  val dtick: Int

  def serialize(): Value = Obj(
    "range" -> range,
    "showgrid" -> showgrid,
    "dtick" -> dtick
  )
}

final case class LatAxis(range: List[Double], showgrid: Boolean = true, dtick: Int = 10) extends MapAxis {
  def setRange(new_range: List[Double]) = this.copy(range = new_range)
}

final case class LongAxis(range: List[Double], showgrid: Boolean = true, dtick: Int = 10) extends MapAxis {
  def setRange(new_range: List[Double]) = this.copy(range = new_range)
}