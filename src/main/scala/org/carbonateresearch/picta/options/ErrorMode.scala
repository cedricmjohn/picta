package org.carbonateresearch.picta.options

private[picta] trait ErrorMode

case object PERCENT extends ErrorMode

case object CONSTANT extends ErrorMode

case object SQRT extends ErrorMode

case object DATA extends ErrorMode
