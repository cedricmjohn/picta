package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}

trait ProjectionOptions extends Component

object ProjectionOptions {
  case class Projection(projection_type: String = "equirectangular") extends ProjectionOptions {
    override def serialize: Value = Obj("projection_type" -> projection_type)
  }
}
