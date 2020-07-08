import UnitTestUtils._
import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.common.Monoid._
import picta.common.OptionWrapper._
import picta.options.histogram.HistFuncType._
import picta.options.histogram.HistNormType._
import picta.options.histogram.{Cumulative, HistOptions, Xbins}
import picta.options.histogram2d.Hist2dOptions
import picta.options._
import picta.options.animation.{AnimationEngine}
import picta.series.ModeType._
import picta.series.XYChartType._
import picta.series.XYZChartType._
import picta.series.{Map, XY, XYSeries, XYZ, XYZSeries}
import upickle.default.write

class AxisTests extends AnyFunSuite {
  test("Axis.Constructor") {
    val axis = Axis(key = "xaxis2", title = "second x-axis", domain = (0.85, 1.0))
    val test = """{"xaxis2":{"title":{"text":"second x-axis"},"showgrid":true,"zeroline":false,"showline":false,"domain":[0.85,1]}}"""
    assert(test == write(axis.serialize))
  }
}

class LineTests extends AnyFunSuite {
  test("Line.Constructor.Basic") {
    val line = Line() + "rgb(255, 255, 255, 1)"
    val test = """{"width":0.5,"color":"rgb(255, 255, 255, 1)"}"""
    assert(test == write(line.serialize))
  }

  test("Line.Constructor.Advanced") {
    val line = Line(width = 0.1, color = List("rgb(255, 255, 255, 1)", "rgb(255, 255, 255, 1)"))
    val test = """{"width":0.1,"color":["rgb(255, 255, 255, 1)","rgb(255, 255, 255, 1)"]}"""
    assert(test == write(line.serialize))
  }
}

class MarkerTests extends AnyFunSuite {
  test("Marker.Constructor.Default") {
    val marker = Marker()
    assert(jsonMonoid.empty.toString == write(marker.serialize))
  }

  test("Marker.Constructor.Full") {
    val marker = Marker() + "circle" + List("red") + Line()
    val test = """{"symbol":"circle","color":"red","line":{"width":0.5}}"""
    assert(test == write(marker.serialize))
  }

  test("Marker.Composition.WithTrace") {
    val marker = Marker(symbol = "circle") + "circle" + List("red") + Line()
    val series = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS) + marker
    val chart = Chart() + series + Layout("Marker.Composition.WithTrace")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class BasicChartTests extends AnyFunSuite {
  test("XY.Scatter.Int") {
    val trace = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS)
    val chart = Chart() + trace + Layout("XY.Scatter.Int")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Bar") {
    val trace = XY(y_str, y_double, series_type = BAR)
    val chart = Chart() + trace + Layout("XY.Bar")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Multiple") {
    val trace1 = XY(x_int, y_int, series_type = BAR)
    val trace2 = XY(x_int, y_double, series_name = "test", series_type = SCATTER, series_mode = MARKERS)
    val chart = Chart() + List(trace1, trace2) + Layout("XY.Multiple")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Scatter.Simplest") {
    val trace = XY(x = x_int, y = y_int, series_type = SCATTER)
    val chart = Chart() + trace + Layout("XY.Scatter.Simplest")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class PieChartTests extends AnyFunSuite {
  test("XY.Pie") {
    val trace1 = XY(List(19, 26, 55), List("Residential", "Non-Residential", "Utility"), series_type = PIE)
    val chart = Chart() + trace1 + Layout("XY.Pie")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class ScatterWithColorTests extends AnyFunSuite {
  test("ScatterWithColor.Basic") {
    val marker = Marker() + z_double
    val trace = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS) + marker
    val chart = Chart() + trace + Layout("ScatterWithColor.Basic")
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString))
  }
}

class HistogramTests extends AnyFunSuite {
  test("XY.Histogram.Basic") {
    val trace = XY(x = x_int, series_type = HISTOGRAM)
    val layout = Layout("XY.Histogram.Basic")
    val chart = Chart() + trace + layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Horizontal") {
    // change xkey to y to get a horizontal histogram
    val trace = XY(x = x_int, xkey = "y", series_type = HISTOGRAM)
    val layout = Layout("XY.Histogram.Horizontal")
    val chart = Chart() + trace + layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Color") {
    val marker = Marker() + List("rgba(255, 100, 102, 0.4)") + Line()
    // change xkey to y to get a horizontal histogram
    val trace = XY(x_random, xkey = "y", series_type = HISTOGRAM, marker = marker)
    val chart = Chart() + trace + Layout("XY.Histogram.Color")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Advanced") {
    val marker = Marker() + List("rgba(255, 100, 102, 0.4)") + Line()
    // change xkey to y to get a horizontal histogram
    val trace = XY(x_random, series_type = HISTOGRAM) + marker
    val layout = Layout("XY.Histogram.Advanced")
    val chart = Chart() + trace + layout + config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Cumulative") {
    val hist_options = HistOptions(histnorm = NUMBER) + Cumulative(enabled = true)
    val trace = XY(x_random, series_type = HISTOGRAM) + hist_options
    val chart = Chart() + trace + Layout("XY.Histogram.Cumulative")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.WithOptions") {
    val hist_options = HistOptions(histnorm = NUMBER) + Xbins(start = 0.5, end = 2.8, size = 0.06)
    val trace = XY(x_random, series_type = HISTOGRAM) + hist_options
    val chart = Chart() + trace + Layout("XY.Histogram.Advanced")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.SpecifyBinningFunction") {
    val x = List("Apples", "Apples", "Apples", "Oranges", "Bananas")
    val y = List("5", "10", "3", "10", "5")
    val ho1 = HistOptions(histfunc = COUNT)
    val ho2 = HistOptions(histfunc = SUM)
    val t1 = XY(x = x, y = y, series_type = HISTOGRAM) + ho1
    val t2 = XY(x = x, y = y, series_type = HISTOGRAM) + ho2
    val chart = Chart() + t1 + t2 + Layout("XY.Histogram.SpecifyBinningFunction")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class Histogram2DContourTests extends AnyFunSuite {
  test("XY.Histogram2dContour") {
    val trace = XY(x_double, y_double, series_type = HISTOGRAM2DCONTOUR)
    val layout = Layout("XY.Histogram2dContour")
    val chart = Chart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram2dContour.WithDensity") {
    val marker = Marker(color = List("rgb(102,0,0)"))
    val trace1 = XY(x_double, y_double, series_mode = MARKERS, series_type = SCATTER, series_name = "points") + marker
    val hist2d_options = Hist2dOptions(ncontours = 20, reversescale = true, showscale = true)
    val trace2 = XY(x_double, y_double, series_type = HISTOGRAM2DCONTOUR, series_name = "density") +
      hist2d_options
    val trace3 = XY(x = x_double, xaxis = "x", yaxis = "y2", series_type = HISTOGRAM, series_name = "histogram")
    val trace4 = XY(y_double, xkey = "y", xaxis = "x2", series_type = HISTOGRAM, series_name = "y density") + marker
    val ax1 = Axis(key = "xaxis", domain = (0.0, 0.85), showgrid = false)
    val ax2 = Axis(key = "yaxis", domain = (0.0, 0.85), showgrid = false)
    val ax3 = Axis(key = "xaxis2", domain = (0.85, 1.0), showgrid = false)
    val ax4 = Axis(key = "yaxis2", domain = (0.85, 1.0), showgrid = false)
    val chart = Chart() + List(trace1, trace2, trace3, trace4) +
      (Layout("XY.Histogram2dContour.WithDensity", autosize = false) + List(ax1, ax2, ax3, ax4) + Margin(t = 50))
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class CompositionTests extends AnyFunSuite {
  val trace_a = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS)
  val trace_b = XY(x_int, y_double, series_type = SCATTER, series_mode = MARKERS)

  test("XY.Chart.Add.Traces") {
    val chart = Chart() + trace_a + trace_b
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Layout") {
    val chart = Chart() + Layout("XY.Chart.Add.Layout") + trace_a + trace_b
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Config") {
    val chart = Chart() + config + Layout("XY.Chart.Add.Config") + trace_a + trace_b
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Layout.Add.Axis") {
    val trace_c = XY(x_int, z_int, series_type = SCATTER, series_mode = MARKERS, yaxis = "y2")
    val layout = Layout("XY.Chart.Add.Config") + Legend() + Axis(key = "yaxis2", title = "y #2", overlaying = "y", side = "right")
    val chart = Chart() + Config(false, false) + trace_a + trace_b + trace_c + layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class LayoutTests extends AnyFunSuite {
  /** Empty Chart Test -> Simply displays an empty set of axis, with a generic title */
  test("XY.Empty") {
    val chart = Chart()
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  /** Test multiple series plotting and axis composition */
  test("XY.Axis.Composition") {
    // 1. define the axis to plot on the chart
    val ax0 = Axis(key = "xaxis", title = " x axis 1")
    val ax1 = Axis(key = "yaxis", title = "y axis 1")
    val ax2 = Axis(key = "yaxis2", title = "y axis 2", side = "right", overlaying = "y")
    // 2. add the axis to the layout to display them on the xy chart
    val layout = Layout("XY.Axis.Composition", List(ax0, ax1, ax2))
    // 3. define the data to display on the chart
    val trace1 = XY(x = x_int, y = y_double, series_type = SCATTER, series_mode = MARKERS, yaxis = "y2")
    val trace2 = XY(x = x_double, y = y_int, series_type = SCATTER, series_mode = MARKERS)
    // 4. combine elements into a single chart
    val chart = Chart(List(trace1, trace2), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Grid") {
    // 1. first we define the grid layout - 1 row, 2 columns
    val grid = Grid(1, 2)
    // 2. Now define the axes we want to place on the grid
    val ax1 = Axis("xaxis", title = "x axis 1")
    val ax2 = Axis("xaxis2", title = "x axis 2")
    // 2. define the traces
    val trace1 = XY(x = x_int, y = y_double, series_type = SCATTER, series_mode = MARKERS)
    val trace2 = XY(x = x_double, y = y_int, series_type = SCATTER, series_mode = MARKERS, xaxis = "x2", yaxis = "y2")
    // 3. combine into a layout
    val layout = Layout(title = "XY.Axis.Composition", axs = List(ax1, ax2), grid = grid)
    // 4. construct into a chart
    val chart = Chart(List(trace1, trace2), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class HeatmapTests extends AnyFunSuite {
  test("XYZ.Heatmap") {
    val data = List(List(1, 2, 3), List(4, 5, 6))
    val series = XYZ(data, series_type = HEATMAP)
    val layout = Layout("XYZ.Heatmap")
    val chart = Chart(List(series), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.HeatmapWithXY") {
    val x: List[String] = List("a", "b", "c", "d", "e", "f")
    val y: List[Int] = List.range(1, 16).toList
    val trace = XYZ(x, y, z_surface, HEATMAP)
    val layout = Layout("XYZ.Heatmap")
    val chart = Chart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class XYZTests extends AnyFunSuite {
  test("XYZ.Scatter3D") {
    val trace = XYZ(x_double, y_double, z_double, series_type = SCATTER3D)
    val layout = Layout("XYZ.Scatter3D")
    val chart = Chart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.line3D") {
    val trace = XYZ(x_double, y_double, z_double, series_type = SCATTER3D, series_mode = LINES)
    val layout = Layout("XYZ.line3D")
    val chart = Chart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Surface") {
    val trace = XYZ(z_surface, series_type = SURFACE)
    val layout = Layout("XYZ.Surface")
    val chart = Chart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class MapTests extends AnyFunSuite {

  import UnitTestUtils._

  test("Map.Basic") {
    val lat = List(40.7127, 51.5072)
    val lon = List(-74.0059, 0.1275)
    val trace = Map(lat, lon)
    val chart = Chart() + trace
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString))
  }

  test("Map.Geo") {
    val color = List("red")
    val line = Line(width = 2) + color
    val trace = Map(List(40.7127, 51.5072), List(-74.0059, 0.1275), series_mode = LINES) + line

    val geo = Geo(landcolor = "rgb(204, 204, 204)", lakecolor = "rgb(255, 255, 255)") +
      LatAxis(List(20, 60)) + LongAxis(List(-100, 20))

    val layout = Layout() + geo
    val chart = Chart() + trace + layout + config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class AnimationTests extends AnyFunSuite {
  test("Animation.XY") {
    val xaxis = Axis(key="xaxis", title="X Variable", range = (0.0, 10.0))
    val yaxis = Axis(key="yaxis", title="Y Variable", range = (0.0, 10.0))
    val layout = Layout("Animation.XY")  + xaxis + yaxis + AnimationEngine()
    val traces = createXYSeries(numberToCreate = 5, length=3)
    val chart = Chart(animated=true) + layout + traces
    if (true) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }

  test("Animation.XYZ") {
    val traces = createXYZSeries(numberToCreate=3, length=3)
    val layout = Layout("Animation.XYZ") + AnimationEngine()
    val chart = Chart(animated=true) + layout + traces
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }
}

object UnitTestUtils {

  val x_random = List.range(1, 100).map(x => scala.util.Random.nextDouble() * 100)
  // flag for plotting each test
  val plotFlag: Boolean = false
  val plotHistogramTests: Boolean = false
  // create a common configuration to be used in all the tests
  val config: Config = Config(responsive = false, scrollZoom = true)
  val x_int = List.range(0, 100).map(x => x * scala.util.Random.nextInt(100))
  val y_int = List.range(0, 100).map(x => x * scala.util.Random.nextInt(100))
  val z_int = List.range(0, 100).map(x => x * scala.util.Random.nextInt(100))
  val x_double = List.range(0, 100).map(x => x * scala.util.Random.nextDouble() * 100)
  val y_double = List.range(0, 100).map(x => x * scala.util.Random.nextDouble() * 100)
  val z_double = List.range(0, 100).map(x => x * scala.util.Random.nextDouble() * 100)
  val x_str = List("1", "2", "3")
  val y_str = List("a", "b", "c")
  val z_str = List("my", "name", "is")
  val z_surface = List(
    List(8.83, 8.89, 8.81, 8.87, 8.9, 8.87),
    List(8.89, 8.94, 8.85, 8.94, 8.96, 8.92),
    List(8.84, 8.9, 8.82, 8.92, 8.93, 8.91),
    List(8.79, 8.85, 8.79, 8.9, 8.94, 8.92),
    List(8.79, 8.88, 8.81, 8.9, 8.95, 8.92),
    List(8.8, 8.82, 8.78, 8.91, 8.94, 8.92),
    List(8.75, 8.78, 8.77, 8.91, 8.95, 8.92),
    List(8.8, 8.8, 8.77, 8.91, 8.95, 8.94),
    List(8.74, 8.81, 8.76, 8.93, 8.98, 8.99),
    List(8.89, 8.99, 8.92, 9.1, 9.13, 9.11),
    List(8.97, 8.97, 8.91, 9.09, 9.11, 9.11),
    List(9.04, 9.08, 9.05, 9.25, 9.28, 9.27),
    List(9, 9.01, 9, 9.2, 9.23, 9.2),
    List(8.99, 8.99, 8.98, 9.18, 9.2, 9.19),
    List(8.93, 8.97, 8.97, 9.18, 9.2, 9.18)
  )

  // creates random XY for testing purposes
  def createXYSeries(numberToCreate: Int, count: Int = 0, length: Int = 10): List[XYSeries] = {
    if (count == numberToCreate) Nil
    else {
      val xs = List.range(0, length)
      val ys = xs.map(x => scala.util.Random.nextDouble() * x)
      val trace = XY(x=xs, y = ys, series_name = "trace" + count)
      trace :: createXYSeries(numberToCreate, count + 1, length)
    }
  }

  def createXYZSeries(numberToCreate: Int, count: Int = 0, length: Int = 10): List[XYZSeries] = {
    if (count == numberToCreate) Nil
    else {
      val xs = List.range(0, length)
      val ys = xs.map(x => scala.util.Random.nextDouble() * x)
      val zs = xs.map(x => scala.util.Random.nextDouble() * x * scala.util.Random.nextInt())
      val trace = XYZ(x=xs, y=ys, z=zs, series_name = "trace" + count, series_type = SCATTER3D)
      trace :: createXYZSeries(numberToCreate, count + 1, length)
    }
  }

  def validateJson(json: String, ignore: Opt[String]=Blank): Boolean = {
    val wd = os.pwd / "src" / "test" / "resources" / "javascript"

    // spawn a subprocess
    val sub = os.proc("node", "test.js").spawn(cwd = wd)

    // send the json to node
    sub.stdin.write(json)
    sub.stdin.flush()

    // get the response from node back
    val result = ujson.read(sub.stdout.readLine())

    // end the process
    sub.destroy()

    val errors = ignore.asOption match {
      case Some(x) => result.arr.filter(e => e("code").toString() != s""""${x}"""")
      case None => result.arr
    }

    if (errors.length > 0) errors.foreach(println)

    errors.length == 0
  }
}
