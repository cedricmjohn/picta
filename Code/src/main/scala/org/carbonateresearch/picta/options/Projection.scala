package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import ujson.{Obj, Value}

/**
 * @constructor: Only used for a Map chart - determines the projection element.
 * @param projection_type : specifies the projection type for a Map chart.
 */
final case class Projection(projection_type: String = "equirectangular") extends Component {
  private[picta] def serialize: Value = Obj("projection_type" -> projection_type)
}
