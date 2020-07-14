package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import ujson.{Obj, Value}

/**
 * @constructor: This configures a grid for subplots.
 * @param rows    : Sets the number of rows.
 * @param columns : Sets the number of columns.
 * @param pattern : Specifies the pattern for the grid.
 */
final case class Subplot(rows: Int = 1, columns: Int = 1, pattern: String = "independent") extends Component {
  private[picta] def serialize(): Value = Obj("rows" -> rows, "columns" -> columns, "pattern" -> pattern)
}
