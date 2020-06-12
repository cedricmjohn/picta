package conusviz

import ujson.{Obj, Value}
import upickle.default._

import scala.reflect.ClassTag


sealed trait XY {
  // trace
  val layout: Value
  val config: Value
}

case class XYChart[T <: AnyVal](val data: List[Trace[T]],
                   l: Layout = Layout("Chart", true),
                   c: Config = Config(true, true))(implicit ev: ClassTag[T]) extends XY {


  val traces: List[Obj] = data.map(t => t.value)
  val layout = transform(l.createLayout).to(Value)
  val config = transform(c.createConfig).to(Value)

  def plot() = {
    val data = Trace.createDataField(traces)
    val html: String = Chart.generateHTMLChart(Chart.generatePlotlyFunction(data, layout, config))
    Chart.writeHTMLChartToFile(html)
  }
}