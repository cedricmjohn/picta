package conusviz.traces

import conusviz.common.Component
import ujson.Value

trait  Trace extends Component {
  val trace_name: String
  val trace_mode: Option[String]
}

object Trace {
  object XYType extends Enumeration {
    type XYType = Value
    val SCATTER, SCATTERGL, BAR, HISTOGRAM2DCONTOUR, HISTOGRAM = Value
  }

  object XYZType extends Enumeration {
    type XYZType = Value
    val CONTOUR, HEATMAP, SCATTER3D, SURFACE = Value
  }
}