package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import ujson.{Obj, Value}

/**
 * @constructor: component that configures the axis for a Map chart.
 * @param showgrid : Specifies whether the grid is shown.
 * @param dtick    :
 */
class MapAxis(range: List[Double], showgrid: Boolean, dtick: Int) extends Component {
  private[picta] def serialize(): Value = Obj(
    "range" -> range,
    "showgrid" -> showgrid,
    "dtick" -> dtick
  )
}

final case class LatAxis(range: List[Double], showgrid: Boolean = true, dtick: Int = 10) extends
  MapAxis(range = range, showgrid = showgrid, dtick = dtick)

final case class LongAxis(range: List[Double], showgrid: Boolean = true, dtick: Int = 10) extends
  MapAxis(range = range, showgrid = showgrid, dtick = dtick)