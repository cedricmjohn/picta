package conusviz.options

import ujson.{Obj, Value}

trait GridOptions

object GridOptions {
  case class Grid(rows: Int = 1, columns: Int = 1, pattern: String = "independent") extends GridOptions {
    def serialize(): Value = Obj("rows" -> rows, "columns" -> columns, "pattern" -> pattern)
  }
}