package conusviz.traces

import ujson.Value

trait  Trace {
  val trace_name: String
  val trace_type: String
  val trace_mode: String
  val value: Value
}

object Trace {

}