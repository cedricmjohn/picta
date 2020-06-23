package conusviz.options

import ujson.{Obj, Value}

object MarkerOptions {

  case class Line(width: List[Int] = List(1), color: String = "") {
    def serialize(): Value = {
      Obj("width" -> width, "color" -> color)
    }
  }

  case class Marker(symbol: String = "", color: String = "", line: Line = Line()) {
    def serialize(): Value = {
      Obj("symbol" -> symbol, "color" -> color, "line" -> line.serialize)
    }
  }
}
