package conusviz

import conusviz.{Config, Layout, Trace, XYTrace}
import ujson.{Arr, Obj, Value}
import upickle.default._

import scala.reflect.ClassTag

sealed trait XY {
  val traces: List[Value]
  val layout: Value
  val config: Value
}

final case class XYChart[T0, T1](val data: List[XYTrace[T0, T1]],
                   l: Layout = Layout("Chart", true),
                   c: Config = Config(true, true)) extends XY {

  // convert the traces, layout and config arguments to a Json value format
  val traces: List[Value] = data.map(t => t.value)
  val layout: Value = transform(l.createLayout).to(Value)
  val config: Value = transform(c.createConfig).to(Value)

  // simply inject traces, layout and config into the the function and generate the HTML
  def plot(): Unit = {
    val html: String = Chart.generateHTMLChart(Chart.generatePlotlyFunction(traces, layout, config))
    Chart.writeHTMLChartToFile(html)
  }
}

object XYChart {
  val compatibleChartSet = Set(
    "scatter",
    "scattergl",
    "bar",
    "surface"
  )
}