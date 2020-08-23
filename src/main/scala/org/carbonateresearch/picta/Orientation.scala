package org.carbonateresearch.picta

/** Specifies the direction of orientation. An example use case is specifying whether a histogram is displayed
 * vertically or horizontally.
 */
sealed trait Orientation

case object VERTICAL extends Orientation {
  override def toString: String = "x"
}

case object HORIZONTAL extends Orientation {
  override def toString: String = "y"
}