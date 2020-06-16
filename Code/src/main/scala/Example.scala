import conusviz.Trace._
import conusviz.options.LayoutOptions._
import conusviz.options.ConfigOptions._
import conusviz.charts.SurfaceChart

object Example extends App {

  val k = List(
    List(8.83,8.89,8.81,8.87,8.9,8.87),
    List(8.89,8.94,8.85,8.94,8.96,8.92),
    List(8.84,8.9,8.82,8.92,8.93,8.91),
    List(8.79,8.85,8.79,8.9,8.94,8.92),
    List(8.79,8.88,8.81,8.9,8.95,8.92),
    List(8.8,8.82,8.78,8.91,8.94,8.92),
    List(8.75,8.78,8.77,8.91,8.95,8.92),
    List(8.8,8.8,8.77,8.91,8.95,8.94),
    List(8.74,8.81,8.76,8.93,8.98,8.99),
    List(8.89,8.99,8.92,9.1,9.13,9.11),
    List(8.97,8.97,8.91,9.09,9.11,9.11),
    List(9.04,9.08,9.05,9.25,9.28,9.27),
    List(9,9.01,9,9.2,9.23,9.2),
    List(8.99,8.99,8.98,9.18,9.2,9.19),
    List(8.93,8.97,8.97,9.18,9.2,9.18)
  )

  // 3d surface plot
  val trace_surface = SurfaceTrace(k, "trace")
  val layout = Layout("ConusViz Surface Chart Example", true)
  val config = Config(true, true)
  val chart_surface = new SurfaceChart(List(trace_surface), layout, config)
  chart_surface.plot()
}

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