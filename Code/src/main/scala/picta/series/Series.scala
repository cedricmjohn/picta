package picta.series

import picta.common.Component

trait Series extends Component {
  val series_name: String
  val series_mode: Option[String]
}

object MarkerType extends Enumeration {
  type MarkerType = Value
  val NONE, LINES, MARKERS, TEXT, LINES_MARKERS, LINES_TEXT, MARKERS_TEXT, LINES_MARKERS_TEXT = Value


  implicit class Wrapper(x: Value) {
    def Stringify(): String = x match {
      case NONE => "none"
      case LINES => "lines"
      case MARKERS => "markers"
      case TEXT => "text"
      case LINES_MARKERS => "lines+markers"
      case LINES_TEXT => "lines+text"
      case MARKERS_TEXT => "markers+text"
      case LINES_MARKERS_TEXT => "lines+markers+text"
    }
  }
}