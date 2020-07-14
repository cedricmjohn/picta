package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import ujson.{Obj, Value}

/**
 * @constructor: This configures the legend for a chart
 * @param x           : x-coordinate for positioning legend on the chart.
 * @param y           : y-coordinate for positioning the legend on the chart.
 * @param orientation : Specifies whether the legend is horizontal or vertical.
 * @param xanchor     :
 * @param yanchor     :
 */
final case class Legend(x: Double = 0.5, y: Double = -0.2, orientation: String = "h", xanchor: String = "auto",
                  yanchor: String = "auto") extends Component {
  private[picta] def serialize: Value = Obj("x" -> x, "y" -> y, "orientation" -> orientation, "xanchor" -> xanchor, "yanchor" -> yanchor)
}