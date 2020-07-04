package picta.series

import picta.common.Component

trait Series extends Component {
  import picta.series.ModeType.ModeType
  val series_name: String
  val series_mode: Option[ModeType]
}

object ModeType extends Enumeration {
  type ModeType = Value
  val NONE, LINES, MARKERS, TEXT = Value
  val LINES_MARKERS = Value("lines+markers")
  val LINES_TEXT = Value("lines+text")
  val MARKERS_TEXT = Value("markers+text")
  val LINES_MARKERS_TEXT = Value("lines+markers+text")
}