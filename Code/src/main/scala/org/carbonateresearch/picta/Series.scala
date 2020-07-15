package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper.Opt

private[picta] trait Series extends Component {
  val mode: Opt[Mode]
  val name: String
}

/** ENUM for series modes */
sealed trait Mode

case object NONE extends Mode

case object LINES extends Mode

case object MARKERS extends Mode

case object TEXT extends Mode

case object LINES_MARKERS extends Mode {
  override def toString: String = "lines+markers"
}

case object LINES_TEXT extends Mode {
  override def toString: String = "lines+text"
}

case object MARKERS_TEXT extends Mode {
  override def toString: String = "markers+text"
}

case object LINES_MARKERS_TEXT extends Mode {
  override def toString: String = "lines+markers+text"
}