package picta.common
import ujson.Value

/** root trait for every graphic component that creates a chart */
trait Component {
  def serialize: Value
}
