package picta.options

import picta.common.Component
import ujson.{Obj, Value}

/**
 * @constructor: Only used for a Map chart - determines the projection element.
 * @param projection_type: specifies the projection type for a Map chart.
 */
case class Projection(projection_type: String = "equirectangular") extends Component {
  override def serialize: Value = Obj("projection_type" -> projection_type)
}
