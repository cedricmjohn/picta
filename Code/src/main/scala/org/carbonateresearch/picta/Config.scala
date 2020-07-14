package org.carbonateresearch.picta

import ujson.{Obj, Value}

/**
 * A class that allows the user to specify chart configuration options.
 *
 * @constructor Class for specifying chart config options
 * @param responsive : specify whether the chart should be responsive to window size
 * @param scrollZoom : specify whether mousewheel or two-finger scroll zooms the plot
 */
final case class Config(responsive: Boolean = true, scrollZoom: Boolean = true) extends Component {
  private[picta] def serialize(): Value = Obj("responsive" -> responsive, "scrollZoom" -> scrollZoom, "displaylogo" -> false)
}
