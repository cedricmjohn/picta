package org.carbonateresearch.picta.options.histogram

/** this trait acts as an ENUM for the current bin option. Only applies if cumulative is enabled. */
sealed trait CurrentBin

/** include current bin from the current cumulative value. */
case object INCLUDE extends CurrentBin

/** exclude current bin from the current cumulative value. */
case object EXCLUDE extends CurrentBin

/** half the value of the current bin is included in the current cumulative value.  */
case object HALF extends CurrentBin