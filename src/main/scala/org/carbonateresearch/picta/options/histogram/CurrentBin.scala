package org.carbonateresearch.picta.options.histogram

/** this trait acts as an ENUM for the current bin option */
sealed trait CurrentBin

case object INCLUDE extends CurrentBin

case object EXCLUDE extends CurrentBin

case object HALF extends CurrentBin
