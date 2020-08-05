package org.carbonateresearch.picta.options.histogram

/** This trait acts as an ENUM for direction option */
sealed trait Direction

case object INCREASING extends Direction

case object DECREASING extends Direction