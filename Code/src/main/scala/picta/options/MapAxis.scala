package picta.options

import picta.common.Component
import ujson.{Obj, Value}

/**
 * @constructor: component that configures the axis for a Map chart.
 * @param showgrid : Specifies whether the grid is shown.
 * @param dtick    :
 */
class MapAxis(range: List[Double], showgrid: Boolean, dtick: Int) extends Component {
  def serialize(): Value = Obj(
    "range" -> range,
    "showgrid" -> showgrid,
    "dtick" -> dtick
  )
}

case class LatAxis(range: List[Double], showgrid: Boolean = true, dtick: Int = 10) extends
  MapAxis(range = range, showgrid = showgrid, dtick = dtick)

case class LongAxis(range: List[Double], showgrid: Boolean = true, dtick: Int = 10) extends
  MapAxis(range = range, showgrid = showgrid, dtick = dtick)
