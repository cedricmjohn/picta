package conusviz.traces

import conusviz.common.Component
import ujson.Value

trait  Trace extends Component {
  val trace_name: String
  val trace_type: String
  val trace_mode: Option[String]
}

object Trace {

}