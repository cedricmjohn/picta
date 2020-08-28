/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta

import ujson.{Obj, Value}

/** Specifies the behaviour of an axis for a Map chart.
 */
private[picta] trait MapAxis extends Component {

  /** Specifies the range of the axis. */
  val range: List[Double]

  /** Specifies whether the grid is shown in this Axis direction. */
  val showgrid: Boolean

  /** Sets the step size between the axis. */
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