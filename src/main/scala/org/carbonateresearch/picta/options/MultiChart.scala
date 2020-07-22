package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import ujson.{Obj, Value}

case class MultiChart(rows: Int = 1, columns: Int = 1) extends Component {
  private[picta] val pattern: String = "independent"
  private[picta] def serialize(): Value = Obj("rows" -> rows, "columns" -> columns, "pattern" -> pattern)
}