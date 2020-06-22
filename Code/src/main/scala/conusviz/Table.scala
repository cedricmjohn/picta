package conusviz

import conusviz.Chart.plotChart
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import ujson.{Arr, Obj, Value}
import upickle.default._

trait TableComponent {
  val values: Value
}

case class Col[T: Serializer](data: List[T])(implicit s0: Serializer[T]) extends TableComponent {
  val values: Value = s0.serialize(data)
}

case class Header[T: Serializer](data: List[T])(implicit s0: Serializer[T]) extends TableComponent {
  val values = data.map(d => s0.serialize(List(d))) //data.grouped(1).toList.map(d => s0.serialize(d))
}

case class Table(header: TableComponent, columns: List[TableComponent], l: Layout = Layout("Chart", true), c: Config = Config(true, true)) {

  val layout: Value = transform(l.createLayout).to(Value)
  val config: Value = transform(c.createConfig).to(Value)

  def getData(): Value = {

    // construct header
    val formatted_header = Obj(
      "values" -> header.values,
      "fill" -> Obj("color" -> "grey"),
      "font" -> Obj("color" -> "white"),
      "line" -> Obj("color" -> "black")
    )

    val data = Obj(
      "type" -> "table",
      "header" -> formatted_header,
      "cells" -> Obj("values" -> columns.map(c => c.values))
    )

    transform(data).to(Value)
  }

  override def toString: String = ujson.write(getData())
  def plot(): Unit = plotChart(List(this.getData()), ujson.read("{}"), ujson.read("{}"))
}

object Table {


}