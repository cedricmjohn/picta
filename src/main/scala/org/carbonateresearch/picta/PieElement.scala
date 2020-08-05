package org.carbonateresearch.picta

import ujson.Obj

/** Represents a slice of a pie chart, with a value and name pair. Can be composed into a Pie chart.
 *
 * @param value : Value for the slice.
 * @param name  : Name for the particular slice.
 */
final case class PieElement(value: Double, name: String) extends Component {
  private[picta] def serialize = Obj("name" -> value)
}