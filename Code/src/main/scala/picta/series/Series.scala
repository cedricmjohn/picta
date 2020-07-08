package picta.series

import picta.common.Component
import picta.common.OptionWrapper.Opt

trait Series extends Component {
  import picta.series.ModeType.ModeType
  val series_mode: Opt[ModeType]
  val series_name: String
}

object ModeType extends Enumeration {
  type ModeType = Value
  val NONE, LINES, MARKERS, TEXT = Value
  val LINES_MARKERS = Value("lines+markers")
  val LINES_TEXT = Value("lines+text")
  val MARKERS_TEXT = Value("markers+text")
  val LINES_MARKERS_TEXT = Value("lines+markers+text")
}