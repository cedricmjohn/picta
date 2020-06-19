import conusviz.traces._
import conusviz.options.LayoutOptions._
import conusviz.options.ConfigOptions._
import conusviz.charts.{XYChart, XYZChart}
import ujson.Value

object Example extends App {
    val x = List.range(1, 100)
    val y = List.range(1, 100)
    val z = List.range(1, 100).map(e => e + scala.util.Random.nextDouble()*100).grouped(10).toList
    val x_double = List(1.5, 2.5, 3.5)

    val trace4 = XYTrace(x_double, trace_name="test", trace_type="histogram", trace_mode="vertical")
    val config = Config(responsive=false, scrollZoom=true)

    val layout = Layout(title="Contour",
        xaxis="x_series",
        yaxis="y_series",
        showlegend=true)

    val chart4 = new XYChart(List(trace4), layout, config)

    chart4.plot()


//    /*
//    * Example 0 - Scatter Chart
//    * */
//    val trace0 = XYTrace(x, y, "trace0", "scatter", "markers")
//    val layout0 = Layout(title="ConusViz Scatter Chart Example", x_label="x_series", y_label="y_series", show_legend=true)
//    val config = Config(responsive=true, scrollZoom=true)
//    val chart0 = new XYChart(List(trace0), layout0, config)
//
//    /*
//    * Example 1 - Line Chart
//    * */
//    val trace1 = XYTrace(x, y, "trace1", "scatter", "lines")
//    val layout1 = Layout(title="ConusViz Line Chart Example", x_label="x_series", y_label="y_series", show_legend=true)
//    val chart1 = new XYChart(List(trace1), layout1, config)
//
//    /*
//    * Example 2 - Line+Marker Chart
//    * */
//    val trace2 = XYTrace(x, y, "trace2", "scatter", "lines+markers")
//    val layout2 = Layout(title="ConusViz Line+Markers Chart Example", x_label="x_series", y_label="y_series", show_legend=true)
//    val chart2 = new XYChart(List(trace2), layout2, config)
//
//    /*
//    * Example 3 - Bar Chart - ungrouped
//    * */
//    val trace3 = XYTrace(x, y, "trace0", "bar")
//    val layout3 = Layout(title="ConusViz Bar Chart Example", x_label="x_series", y_label="y_series", show_legend=true)
//    val chart3 = new XYChart(List(trace3), layout3, config)
//
//    /*
//    * Example 3 - Bar Chart - grouped
//    * */
//    val trace4 = XYTrace(x, y, "trace0", "bar")
//    val layout4 = Layout(title="ConusViz Grouped Bar Chart Example", x_label="x_series", y_label="y_series", show_legend=true, bar_mode="grouped")
//    val chart4 = new XYChart(List(trace3), layout4, config)

//    case class Test(title: ujson.Value, showlegend: Boolean)
//    implicit val authorRW = upickle.default.macroRW[Test]
//    val is = getClass.getClassLoader.getResourceAsStream("layout.json")
//    val k = ujson.read(scala.io.Source.fromInputStream(is).mkString)
//    println(k)
//    val t = ujson.Bool(false)
//    conusviz.Utils.update(k, "legend", "example", false)
//    println(k)
}

//    val engine = new ScriptEngineManager().getEngineByMimeType("text/javascript")
//    val result = engine.eval("1 + 1")
//    println(result)

//    val engine: ScriptEngine  = new ScriptEngineManager().getEngineByName("nashorn");
//    val is = getClass.getClassLoader.getResourceAsStream("plotly.min.js")
//    engine.eval(scala.io.Source.fromInputStream(is).mkString)
//
//    val invocable: Invocable = engine.asInstanceOf[Invocable]
//
//    val result: Object = invocable.invokeFunction("Plotly.validate", "{}");
//    System.out.println(result);

//  val x = List.range(1, 100)
//  val y = x.map(x => x + scala.util.Random.nextDouble()*100)
//
//  val trace0 = XYTrace(x, y, "trace0", "scatter", "marker")
//  val layout = Layout(title="ConusViz Scatter Chart Example", showlegend=true)
//  val config = Config(true, true)
//  val chart_surface = new XYChart(List(trace0), layout, config)
//  chart_surface.plot()

//  val trace: Trace[Any] = Trace(List(x, y), trace_name = "test", trace_type = "surface", mode = "lines")

//val layout = Layout("Chart", true)
//val config = Config(true, true)

//  val layout = Layout("Chart", true)
//  val config = Config(true, true)
//  val trace = Trace(List(x, y), trace_name = "test", trace_type = "surface", mode = "lines")
//
//  println(trace)
//  val xychart = new XYChart(List(trace), layout, config)
//  xychart.plot()

//  val sum: ModelVariable[Double] = ModelVariable("Sum", 1.0)
//  val interestRate: ModelVariable[Double] = ModelVariable("Interest Rate", 0.1)
//
//  def interestCalculator = (s: Step) => sum(s - 1) + sum(s - 1) * interestRate(s)
//
//  val model = new SteppedModel(10, "Finance").setGrid(3)
//    .defineMathematicalModel(sum =>> interestCalculator)
//    .defineInitialModelConditions(
//      PerCell(sum, List(
//        (List(1.0), Seq(0)),
//        (List(10.0), Seq(1)),
//        (List(100.0), Seq(2))
//      )),
//      AllCells(interestRate, List(0.10))
//    )

//  val runnedModel = model.run

//  Thread.sleep(1000)