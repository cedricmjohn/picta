package conusviz

import conusviz.{Config, Layout, Trace, XYTrace}
import ujson.{Arr, Obj, Value}
import upickle.default._

sealed trait Surface {
  val traces: List[Value]
  val layout: Value
  val config: Value
}

final case class SurfaceChart[T0](val data: List[SurfaceTrace[T0]],
                                 l: Layout = Layout("Chart", true),
                                 c: Config = Config(true, true)) extends Surface {

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

object SurfaceChart {
  val compatibleChartSet = Set(
    "surface"
  )
}