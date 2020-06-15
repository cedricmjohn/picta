package conusviz

import ujson.Value
import upickle.default._
import Trace._
import LayoutOption._
import ConfigOption._
import almond.interpreter.api.{DisplayData, OutputHandler}

sealed trait Surface {
  val traces: List[Value]
  val layout: Value
  val config: Value
}

case class SurfaceChart[T0](val data: List[SurfaceTrace[T0]],
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

  //(implicit kernel: almond.api.JupyterApi)
  def plotInline()(implicit publish: OutputHandler) = {

    val html: String = Chart.generatePlotlyFunction(traces, layout, config)//Chart.generateHTMLChart(Chart.generatePlotlyFunction(traces, layout, config))
    Chart.writeHTMLToJupyter(html)
  }
}

object SurfaceChart {
  val compatibleChartSet = Set(
    "surface"
  )
}