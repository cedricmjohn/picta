package picta.series

import picta.common.Component

trait Series extends Component {
  val series_name: String
  val series_mode: Option[String]
}

/** Defines an Enumeration for the different chart types */
object Series {
//  object XYChartType extends Enumeration {
//    type XYChartType = Value
//    val SCATTER, SCATTERGL, BAR, HISTOGRAM2DCONTOUR, HISTOGRAM, PIE = Value
//  }
//
//  object XYZChartType extends Enumeration {
//    type XYZChartType = Value
//    val CONTOUR, HEATMAP, SCATTER3D, SURFACE = Value
//  }
}