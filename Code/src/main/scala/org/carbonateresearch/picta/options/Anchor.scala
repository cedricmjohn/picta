package org.carbonateresearch.picta.options

sealed trait Anchor
case object LEFT extends Anchor
case object RIGHT extends Anchor
case object CENTER extends Anchor
case object AUTO extends Anchor