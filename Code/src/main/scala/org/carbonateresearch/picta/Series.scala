package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper.Opt

private[picta] trait Series extends Component {
  val symbol: Opt[Symbol]
  val name: String
}

/** ENUM for series markers */
sealed trait Symbol

case object NONE extends Symbol

case object LINES extends Symbol

case object MARKERS extends Symbol

case object TEXT extends Symbol

case object LINES_MARKERS extends Symbol {
  override def toString: String = "lines+markers"
}

case object LINES_TEXT extends Symbol {
  override def toString: String = "lines+text"
}

case object MARKERS_TEXT extends Symbol {
  override def toString: String = "markers+text"
}

case object LINES_MARKERS_TEXT extends Symbol {
  override def toString: String = "lines+markers+text"
}