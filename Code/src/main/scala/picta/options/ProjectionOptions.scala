package picta.options

import picta.common.Component
import ujson.{Obj, Value}

object ProjectionOptions {
  case class Projection(projection_type: String = "equirectangular") extends Component {
    override def serialize: Value = Obj("projection_type" -> projection_type)
  }
}
