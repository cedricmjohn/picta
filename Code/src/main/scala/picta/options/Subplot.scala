package picta.options

import picta.common.Component
import ujson.{Obj, Value}

/**
 * @constructor: This configures a grid for subplots.
 * @param rows    : Sets the number of rows.
 * @param columns : Sets the number of columns.
 * @param pattern : Specifies the pattern for the grid.
 */
case class Subplot(rows: Int = 1, columns: Int = 1, pattern: String = "independent") extends Component {
  def serialize(): Value = Obj("rows" -> rows, "columns" -> columns, "pattern" -> pattern)
}
