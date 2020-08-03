package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper.Opt

/** Specifies the behaviour for a Series component. */
private[picta] trait Series extends Component {
  val style: Opt[Style]
  val name: String
  val classification: String

  def setName(new_name: String): Series
}

/** ENUM for series markers */
sealed trait Style
case object NONE extends Style
case object LINES extends Style
case object MARKERS extends Style
case object TEXT extends Style

case object LINES_MARKERS extends Style {
  override def toString: String = "lines+markers"
}

case object LINES_TEXT extends Style {
  override def toString: String = "lines+text"
}

case object MARKERS_TEXT extends Style {
  override def toString: String = "markers+text"
}

case object LINES_MARKERS_TEXT extends Style {
  override def toString: String = "lines+markers+text"
}