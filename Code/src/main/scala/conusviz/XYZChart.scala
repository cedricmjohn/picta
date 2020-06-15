package conusviz

import ujson.{Value}
import upickle.default._
import Trace._
import LayoutOption._
import ConfigOption._

sealed trait XYZ {
  val traces: List[Value]
  val layout: Value
  val config: Value
}

final case class XYZChart[T0, T1, T2](val data: List[XYZTrace[T0, T1, T2]],
                                l: Layout = Layout("Chart", true),
                                c: Config = Config(true, true)) extends XYZ {

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

object XYZChart {
  val compatibleChartSet = Set(
    "contour",
    "heatmap",
    "scatter3d",
  )
}
