package org.carbonateresearch.picta

/** Enum type for specifying a side. */
sealed trait Side

case object RIGHT_SIDE extends Side {
  override def toString: String = "right"
}

case object LEFT_SIDE extends Side {
  override def toString: String = "left"
}

case object TOP_SIDE extends Side {
  override def toString: String = "top"
}

case object BOTTOM_SIDE extends Side {
  override def toString: String = "bottom"
}
