package picta.options

import picta.common.Component
import ujson.{Obj, Value}

object GridOptions {
  case class Grid(rows: Int = 1, columns: Int = 1, pattern: String = "independent") extends Component {
    def serialize(): Value = Obj("rows" -> rows, "columns" -> columns, "pattern" -> pattern)
  }
}