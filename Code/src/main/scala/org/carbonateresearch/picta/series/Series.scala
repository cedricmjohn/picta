package org.carbonateresearch.picta.series

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.OptionWrapper.Opt

trait Series extends Component {
  import Mode.Mode
  val series_mode: Opt[Mode]
  val series_name: String
}

object Mode extends Enumeration {
  type Mode = Value
  val NONE, LINES, MARKERS, TEXT = Value
  val LINES_MARKERS = Value("lines+markers")
  val LINES_TEXT = Value("lines+text")
  val MARKERS_TEXT = Value("markers+text")
  val LINES_MARKERS_TEXT = Value("lines+markers+text")
}