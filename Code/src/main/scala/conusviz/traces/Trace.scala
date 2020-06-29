package conusviz.traces

import conusviz.common.Component

trait Trace extends Component {
  val trace_name: String
  val trace_mode: Option[String]
}

object Trace {
  object XYChartType extends Enumeration {
    type XYChartType = Value
    val SCATTER, SCATTERGL, BAR, HISTOGRAM2DCONTOUR, HISTOGRAM, PIE = Value
  }

  object XYZChartType extends Enumeration {
    type XYZChartType = Value
    val CONTOUR, HEATMAP, SCATTER3D, SURFACE = Value
  }
}