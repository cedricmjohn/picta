import UnitTestUtils._
import org.scalatest.funsuite.AnyFunSuite
import picta.IO.IO._
import picta.common.Utils.getSeriesbyCategory
import picta.charts.Chart
import picta.common.Monoid._
import picta.common.OptionWrapper._
import picta.options.histogram.HistFuncType._
import picta.options.histogram.HistNormType._
import picta.options.histogram.{Cumulative, HistOptions, Xbins}
import picta.options.histogram2d.Hist2dOptions
import picta.options._
import picta.options.histogram.HistOrientationType.HORIZONTAL
import picta.series.ModeType._
import picta.series.XYChartType._
import picta.series.XYZChartType._
import picta.series.{Map, XY, XYSeries, XYZ, XYZSeries}
import upickle.default.write

import scala.collection.mutable

class AxisTests extends AnyFunSuite {
  test("Axis.Constructor") {
    val axis = Axis(position = "x2") setTitle "second x-axis" setDomain (0.85, 1.0)
    val test = """{"xaxis2":{"title":{"text":"second x-axis"},"showgrid":true,"zeroline":false,"showline":false,"domain":[0.85,1]}}"""
    assert(test == write(axis.serialize))
  }
}

class LineTests extends AnyFunSuite {
  test("Line.Constructor.Basic") {
    val line = Line() setColor "rgb(255, 255, 255, 1)"
    val test = """{"width":0.5,"color":"rgb(255, 255, 255, 1)"}"""
    assert(test == write(line.serialize))
  }
  test("Line.Constructor.Advanced") {
    val line = Line() setWidth 0.1 setColor List("rgb(255, 255, 255, 1)", "rgb(255, 255, 255, 1)")
    val test = """{"width":0.1,"color":["rgb(255, 255, 255, 1)","rgb(255, 255, 255, 1)"]}"""
    assert(test == write(line.serialize))
  }
}

class MarkerTests extends AnyFunSuite {

  val plotFlag = false

  test("Marker.Constructor.Default") {
    val marker = Marker()
    assert(jsonMonoid.empty.toString == write(marker.serialize))
  }

  test("Marker.Constructor.Full") {
    val marker = Marker() setSymbol "circle" setColors "red" setLine Line()
    val test = """{"symbol":"circle","color":"red","line":{"width":0.5}}"""
    assert(test == write(marker.serialize))
  }

  test("Marker.Composition.WithTrace") {
    val marker = Marker() setSymbol "circle" setColors "red" setLine Line()
    val x_axis = Axis(position = "x") setTitle "x variable"
    val y_axis = Axis(position = "y") setTitle "y variable"

    val series = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS) setMarker marker
    val layout = Layout("Marker.Composition.WithTrace") setAxes(x_axis, y_axis)
    val chart = Chart() setData  series setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class BasicChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Scatter.Int") {
    val series = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS)
    val chart = Chart() setData series setLayout Layout("XY.Scatter.Int")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Bar") {
    val series = XY(y_str, y_double, series_type = BAR)
    val chart = Chart() setData series setLayout Layout("XY.Bar")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Multiple") {
    val series1 = XY(x_int, y_int, series_type = BAR)
    val series2 = XY(x_int, y_double, series_type = SCATTER, series_mode = MARKERS) setName "test"
    val chart = Chart() setData List(series1, series2) setLayout Layout("XY.Multiple")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Scatter.Simplest") {
    val series = XY(x = x_int, y = y_int, series_type = SCATTER)
    val chart = Chart() setData series setLayout Layout("XY.Scatter.Simplest")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class PieChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Pie") {
    val series = XY(List(19, 26, 55), List("Residential", "Non-Residential", "Utility"), series_type = PIE)
    val chart = Chart() setData series setLayout Layout("XY.Pie")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class ScatterWithColorTests extends AnyFunSuite {

  val plotFlag = false

  test("ScatterWithColor.Basic") {
    val marker = Marker() setColors z_double
    val series = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS) setMarker marker
    val chart = Chart() setData series setLayout Layout("ScatterWithColor.Basic")
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString))
  }
}

class HistogramTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Histogram.Basic") {
    val series = XY(x = x_int, series_type = HISTOGRAM)
    val layout = Layout("XY.Histogram.Basic")
    val chart = Chart() setData series setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Horizontal") {
    // change xkey to y to get a horizontal histogram
    val series = XY(x = x_int, series_type = HISTOGRAM) setHistOptions HistOptions(orientation = HORIZONTAL)
    val layout = Layout("XY.Histogram.Horizontal")
    val chart = Chart() setData  series setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Color") {
    val marker = Marker() setColors List("rgba(255, 100, 102, 0.4)") setLine Line()
    // change xkey to y to get a horizontal histogram
    val series = XY(x_random, series_type = HISTOGRAM, marker = marker) setHistOptions HistOptions(orientation = HORIZONTAL)
    val chart = Chart() setData series setLayout Layout("XY.Histogram.Color")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.withLines") {
    val marker = Marker() setColors List("rgba(255, 100, 102, 0.4)") setLine Line()
    val series = XY(x_random, series_type = HISTOGRAM) setMarker marker
    val layout = Layout("XY.Histogram.Advanced")
    val chart = Chart() setData series setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Cumulative") {
    val hist_options = HistOptions(histnorm = NUMBER) setCumulative Cumulative(enabled = true)
    val series = XY(x_random, series_type = HISTOGRAM) setHistOptions hist_options
    val chart = Chart() setData series setLayout Layout("XY.Histogram.Cumulative")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.WithOptions") {
    val hist_options = HistOptions(histnorm = NUMBER) setXbins Xbins(start = 0.5, end = 3.0, size = 0.05)
    val series = XY(x_random, series_type = HISTOGRAM) setHistOptions hist_options
    val chart = Chart() setData series setLayout Layout("XY.Histogram.WithOptions")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.SpecifyBinningFunction") {
    val x = List("Apples", "Apples", "Apples", "Oranges", "Bananas")
    val y = List("5", "10", "3", "10", "5")
    val ho1 = HistOptions(histfunc = COUNT)
    val ho2 = HistOptions(histfunc = SUM)
    val t1 = XY(x = x, y = y, series_type = HISTOGRAM) setHistOptions ho1
    val t2 = XY(x = x, y = y, series_type = HISTOGRAM) setHistOptions ho2
    val chart = Chart() setData(t1, t2) setLayout Layout("XY.Histogram.SpecifyBinningFunction")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class Histogram2DContourTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Histogram2dContour") {
    val series = XY(x_double, y_double, series_type = HISTOGRAM2DCONTOUR)
    val layout = Layout("XY.Histogram2dContour")
    val chart = Chart() setData series setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram2dContour.WithDensity") {
    val marker = Marker() setColors "rgb(102,0,0)"
    val series1 = XY(x_double, y_double, series_mode = MARKERS, series_type = SCATTER) setName "points" setMarker marker

    val hist2d_options = Hist2dOptions(ncontours = 20, reversescale = false, showscale = true)
    val series2 = XY(x_double, y_double, series_type = HISTOGRAM2DCONTOUR, series_name = "density") setHist2dOptions hist2d_options

    val series3 = XY(x = x_double, xaxis = "x", yaxis = "y2", series_type = HISTOGRAM) setName "histogram"
    val series4 = ( XY(y_double, xaxis = "x2", series_type = HISTOGRAM, series_name = "y density") setMarker marker
                    setHistOptions HistOptions(orientation = HORIZONTAL) )

    val ax1 = Axis(position = "x", showgrid = false) setDomain (0.0, 0.85)
    val ax2 = Axis(position = "y", showgrid = false) setDomain (0.0, 0.85)
    val ax3 = Axis(position = "x2", showgrid = false) setDomain (0.85, 1.0)
    val ax4 = Axis(position = "y2", showgrid = false) setDomain (0.85, 1.0)

    val chart = Chart()  setData List(series1, series2, series3, series4) setLayout
      (Layout("XY.Histogram2dContour.WithDensity", autosize = false) setAxes(ax1, ax2, ax3, ax4))

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class CompositionTests extends AnyFunSuite {

  val plotFlag = false

  val series1 = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS)
  val series2 = XY(x_int, y_double, series_type = SCATTER, series_mode = MARKERS)

  test("XY.Chart.Add.Traces") {
    val chart = Chart() setData(series1, series2)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Layout") {
    val chart = Chart() setLayout Layout("XY.Chart.Add.Layout") setData(series1, series2)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Config") {
    val chart = Chart() setConfig config setLayout Layout("XY.Chart.Add.Config") setData(series1, series2)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Layout.Add.Axis") {
    val series3 = XY(x_int, z_int, series_type = SCATTER, series_mode = MARKERS, yaxis = "y2")
    val layout = ( Layout("XY.Chart.Add.Config") setLegend  Legend()
      setAxes Axis(position = "y2", title = "second y axis", overlaying = "y", side = "right") )

    val chart = Chart() setConfig Config(false, false) setData(series1, series2, series3) setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class LayoutTests extends AnyFunSuite {

  val plotFlag = false

  /** Empty Chart Test -> Simply displays an empty set of axis, with a generic title */
  test("XY.Empty") {
    val chart = Chart()
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  /** Test multiple series plotting and axis composition */
  test("XY.Axis.Composition") {
    // 1. define the axis to plot on the chart
    val ax0 = Axis(position = "x", title = " x axis 1")
    val ax1 = Axis(position = "y", title = "y axis 1")
    val ax2 = Axis(position = "y2", title = "y axis 2", side = "right", overlaying = "y")

    // 2. add the axis to the layout to display them on the xy chart
    val layout = Layout("XY.Axis.Composition") setAxes List(ax0, ax1, ax2)

    // 3. define the data to display on the chart
    val trace1 = XY(x = x_int, y = y_double, series_type = SCATTER, series_mode = MARKERS, yaxis = "y2")
    val trace2 = XY(x = x_double, y = y_int, series_type = SCATTER, series_mode = MARKERS)

    // 4. combine elements into a single chart
    val chart = Chart() setData List(trace1, trace2) setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Grid") {
    // 1. first we define the grid layout - 1 row, 2 columns
    val grid = Subplot(1, 2)

    // 2. Now define the axes we want to place on the grid
    val ax1 = Axis("x") setTitle "x axis 1"
    val ax2 = Axis("x2") setTitle "x axis 2"

    // 2. define the traces
    val trace1 = XY(x = x_int, y = y_double, series_type = SCATTER, series_mode = MARKERS)
    val trace2 = XY(x = x_double, y = y_int, series_type = SCATTER, series_mode = MARKERS, xaxis = "x2", yaxis = "y2")

    // 3. combine into a layout
    val layout = Layout(title = "XY.Axis.Composition") setAxes List(ax1, ax2) setSubplot grid

    // 4. construct into a chart
    val chart = Chart(List(trace1, trace2), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class HeatmapTests extends AnyFunSuite {

  val plotFlag = false

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
    val data = XYZ(x, y, z_surface, HEATMAP)
    val layout = Layout("XYZ.Heatmap")
    val chart = Chart(List(data), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class XYZTests extends AnyFunSuite {

  val plotFlag = false

  test("XYZ.Scatter3D") {
    val data = XYZ(x_double, y_double, z_double, series_type = SCATTER3D)
    val layout = Layout("XYZ.Scatter3D")
    val chart = Chart(List(data), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.line3D") {
    val data = XYZ(x_double, y_double, z_double, series_type = SCATTER3D, series_mode = LINES)
    val layout = Layout("XYZ.line3D")
    val chart = Chart(List(data), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Surface") {
    val data = XYZ(z_surface, series_type = SURFACE)
    val layout = Layout("XYZ.Surface")
    val chart = Chart(List(data), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class MapTests extends AnyFunSuite {

  val plotFlag = false

  test("Map.Basic") {
    val lat = List(40.7127, 51.5072)
    val lon = List(-74.0059, 0.1275)
    val data = Map(lat, lon)
    val chart = Chart() setData data
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString))
  }

  test("Map.Geo") {
    val color = List("red")
    val line = Line(width = 2) setColor color
    val data = Map(List(40.7127, 51.5072), List(-74.0059, 0.1275), series_mode = LINES) setLine line

    val geo = Geo(landcolor = "rgb(204, 204, 204)", lakecolor = "rgb(255, 255, 255)")setAxes
      (LatAxis(List(20, 60)), LongAxis(List(-100, 20)))

    val layout = Layout(margin = Margin(0, 0, 0, 0), height = 800, width=800) setGeo geo
    val chart = Chart() setData data setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}

class AnimationTests extends AnyFunSuite {

  val plotFlag = true

  test("Animation.XY") {
    val xaxis = Axis(position="xaxis", title="X Variable", range = (0.0, 10.0))
    val yaxis = Axis(position="yaxis", title="Y Variable", range = (0.0, 10.0))
    val layout = Layout("Animation.XY")  setAxes (xaxis, yaxis)
    val data = createXYSeries(numberToCreate = 50, length=30)
    val chart = Chart(animated=true) setLayout layout setData data
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }

  test("Animation.XYZ") {
    val data = createXYZSeries(numberToCreate=3, length=3)
    val layout = Layout("Animation.XYZ")
    val chart = Chart(animated=true) setLayout layout setData data
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }
}

class IrisTests extends AnyFunSuite {

  val plotFlag = false

  test("Iris.2DCategory") {
    val filepath = getWorkingDirectory + "/src/test/resources/iris_csv.csv"

    val data = readCSV(filepath)

    val sepal_length = data("sepallength").map(_.toDouble)
    val petal_width = data("petalwidth").map(_.toDouble)
    val categories = data("class")

    val result: List[XYSeries] = getSeriesbyCategory(categories, (sepal_length, petal_width))

    val chart = Chart() setData result setLayout Layout(title="Iris", showlegend = true)
    chart.plot()
  }
}

object UnitTestUtils {
  // create a common configuration to be used in all the tests
  val config: Config = Config(responsive = false)
  val x_random = List.range(1, 100).map(x => scala.util.Random.nextDouble() * 100)
  val x_int = List.range(0, 100).map(x => scala.util.Random.nextInt(50))
  val y_int = List.range(0, 100).map(x => scala.util.Random.nextInt(50))
  val z_int = List.range(0, 100).map(x => scala.util.Random.nextInt(50))
  val x_double = List.range(0, 100).map(x => scala.util.Random.nextDouble() * 50)
  val y_double = List.range(0, 100).map(x => scala.util.Random.nextDouble() * 50)
  val z_double = List.range(0, 100).map(x => scala.util.Random.nextDouble() * 50)
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
