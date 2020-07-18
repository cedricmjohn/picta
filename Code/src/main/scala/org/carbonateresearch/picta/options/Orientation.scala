package org.carbonateresearch.picta.options

private[picta] sealed trait Orientation

case object VERTICAL extends Orientation {
  override def toString: String = "x"
}

case object HORIZONTAL extends Orientation {
  override def toString: String = "y"
}
