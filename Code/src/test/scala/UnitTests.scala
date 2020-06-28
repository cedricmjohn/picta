package conusviz

import conusviz.charts.{MapChart, XYChart, XYZChart}
import conusviz.options.AxisOptions.Axis
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import conusviz.Utils._
import conusviz.options.GeoOptions.Geo
import conusviz.options.GridOptions.Grid
import conusviz.options.LegendOptions.Legend
import conusviz.options.LineOptions.Line
import conusviz.options.MapAxisOptions.{LatAxis, LongAxis}
import conusviz.options.MarkerOptions._
import org.scalatest.FunSuite
import upickle.default._
import conusviz.traces.Trace.XYType._
import conusviz.traces.Trace.XYZType._
import conusviz.traces.{MapTrace, Trace, XYTrace, XYZTrace}

class CompositionTests extends FunSuite {
  import UnitTestUtils._

  val trace_a = XYTrace(x_int, y_int, trace_name="int", trace_type=SCATTER, trace_mode=Some("markers"))
  val trace_b = XYTrace(x_double, y_double, trace_name="double", trace_type=SCATTER, trace_mode=Some("markers"))

  test("XY.Chart.Add.Traces") {
    val chart = XYChart() + trace_a + trace_b
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }

  test("XY.Chart.Add.Layout") {
    val chart = XYChart() + Layout(Some("XY.Chart.Add.Layout")) + trace_a + trace_b
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }

  test("XY.Chart.Add.Config") {
    val chart = XYChart() + Config(false, false) + Layout(Some("XY.Chart.Add.Config")) + trace_a + trace_b
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }

  test("XY.Layout.Add.Axis") {
    val trace_c = XYTrace(x_int, y_int.map(x => scala.util.Random.nextInt(100)), trace_name="alt_axis",
      trace_type=SCATTER, trace_mode=Some("markers"), yaxis="y2")

    val layout = Layout(Some("XY.Chart.Add.Config"), legend=Some(Legend())) +
      Axis("yaxis2", overlaying = Some("y"), side = Some("right"))

    val chart = XYChart() + Config(false, false) + trace_a + trace_b + trace_c + layout
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }
}

class LineTests extends FunSuite {
  test("Line.Constructor.Basic") {
    val line = Line(color = Some("rgb(255, 255, 255, 1)"))
    val test = """{"width":[0.5],"color":"rgb(255, 255, 255, 1)"}"""
    assert(test == write(line.serialize))
  }
}

class MarkerTests extends FunSuite {
  test("Marker.Constructor.Basic") {
    val marker = Marker()
    assert(emptyObject.toString ==  write(marker.serialize))
  }
}

class XYTests extends FunSuite {
  import UnitTestUtils._

  test("XY.Scatter.Int") {
    val trace = XYTrace(x_int, y_int, trace_name="test", trace_type=SCATTER, trace_mode=Some("markers"))
    val layout = Layout(Some("XY.Scatter.Int"))
    val chart = XYChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }

  test("XY.Bar") {
    val trace = XYTrace(y_str, y_double, trace_name="test", trace_type=BAR)
    val layout = Layout(Some("XY.Bar"))
    val chart = XYChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }

  test("XY.Multiple") {
    val trace1 = XYTrace(x_int, y_int, trace_name="test", trace_type=BAR)
    val trace2 = XYTrace(x_int, y_double, trace_name="test", trace_type=SCATTER, trace_mode=Some("markers"))
    val layout = Layout(Some("XY.Multiple"))
    val chart = XYChart(List(trace1, trace2), layout, config)
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }
}

/*
* Tests for the Histogram and Histogram 2D Contour chart
* */
class HistogramTests extends FunSuite {
  import UnitTestUtils._

  test("XY.Histogram.Basic") {
    val trace = XYTrace(x=x_int, xkey="x", trace_name="test", trace_type=HISTOGRAM)
    val layout = Layout(Some("XY.Histogram.Basic"))
    val chart = XYChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }

  test("XY.Histogram.Horizontal") {
    // change xkey to y to get a horizontal histogram
    val trace = XYTrace(x = x_random, xkey = "y", trace_name = "test", trace_type = HISTOGRAM)
    val layout = Layout(Some("XY.Histogram.Horizontal"))
    val chart = XYChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString) )
  }

  test("XY.Histogram.Color") {
    val marker: Marker = Marker(color = Some("rgba(255, 100, 102, 0.4)"), line = Some(Line()))

    // change xkey to y to get a horizontal histogram
    val trace = XYTrace(x_random, xkey="y", trace_name="test", trace_type=HISTOGRAM, marker=marker)
    val layout = Layout(Some("XY.Histogram.Color"))
    val chart = XYChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }
}

class Histogram2DContourTests extends FunSuite {
  import UnitTestUtils._

  test("XY.Histogram2dContour") {
    val trace = XYTrace(x_double, y_double, trace_name="test", trace_type=HISTOGRAM2DCONTOUR)
    val layout = Layout(Some("XY.Histogram2dContour"))
    val chart = XYChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }
}

class LayoutTests extends FunSuite {
  import UnitTestUtils._

  /*
  * Empty Chart Test -> Simply displays an empty set of axis, with a generic title
  * */
  test("XY.Empty") {
    val chart = XYChart()
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }

  /*
  * Test multiple series plotting and axis composition
  * */
  test("XY.Axis.Composition") {
    // 1. define the axis to plot on the chart
    val ax0: Axis = Axis(key="xaxis")
    val ax1: Axis = Axis(key="yaxis", title = "y axis 1")
    val ax2: Axis = Axis(key="yaxis2", title = "y axis 2", side = Some("right"), overlaying = Some("y"))
    // 2. add the axis to the layout to display them on the xy chart
    val layout: Layout = Layout(Some("XY.Axis.Composition"), Some(List(ax0, ax1, ax2)))
    // 3. define the data to display on the chart
    val trace1 = XYTrace(x=x_int, y=y_double, trace_name="trace0", trace_type=SCATTER, trace_mode=Some("markers"), yaxis="y2")
    val trace2 = XYTrace(x=x_double, y=y_int, trace_name="trace1", trace_type=SCATTER, trace_mode=Some("markers"))
    // 4. combine elements into a single chart
    val chart: XYChart = XYChart(List(trace1, trace2), layout, config)
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }

  test("XY.Grid") {
    // 1. first we define the grid layout - 1 row, 2 columns
    val grid: Grid = Grid(1, 2)
    // 2. Now define the axes we want to place on the grid
    val ax1: Axis = Axis("xaxis", title = "x axis 1")
    val ax2: Axis = Axis("xaxis2", title = "x axis 2")
    // 2. define the traces
    val trace1 = XYTrace(x=x_int, y=y_double, trace_name="trace1", trace_type=SCATTER, trace_mode=Some("markers"))
    val trace2 = XYTrace(x=x_double, y=y_int, trace_name="trace2", trace_type=SCATTER, trace_mode=Some("markers"), xaxis="x2", yaxis="y2")
    // 3. combine into a layout
    val layout: Layout = Layout(title=Some("XY.Axis.Composition"), axs=Some(List(ax1, ax2)), grid=Some(grid))
    // 4. construct into a chart
    val chart: XYChart = XYChart(List(trace1, trace2), layout, config)
    if (plotFlag) chart.plot
    assert( validateJson(chart.serialize.toString) )
  }
}

class XYZTests extends FunSuite {
  import UnitTestUtils._

  test("XYZ.Scatter3D") {
    val trace = XYZTrace(x_double, y_double, z_double, trace_name="trace", trace_type=SCATTER3D)
    val layout = Layout(Some("XYZ.Scatter3D"))
    val chart = XYZChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.line3D") {
    val trace = XYZTrace(x_double, y_double, z_double, trace_name="trace", trace_type=SCATTER3D, trace_mode=Some("lines"))
    val layout = Layout(Some("XYZ.line3D"))
    val chart = XYZChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Surface") {
    val trace = XYZTrace(z_surface, trace_name="trace", trace_type=SURFACE)
    val layout = Layout(Some("XYZ.Surface"))
    val chart = XYZChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Heatmap") {
    val trace = XYZTrace(z_surface, trace_name="trace", trace_type=HEATMAP)
    val layout = Layout(Some("XYZ.Heatmap"))
    val chart = XYZChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.HeatmapWithXY") {
    val x: List[Int] = List.range(1, 16).toList
    val y: List[String] = List("a", "b", "c", "d", "e", "f")
    val trace = XYZTrace(x, y, z_surface, "trace", HEATMAP)
    val layout = Layout(Some("XYZ.Heatmap"))
    val chart = XYZChart(List(trace), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}


class MapTests extends FunSuite {
  import UnitTestUtils._

  test("Map.Basic") {
    val lat = List(40.7127, 51.5072)
    val lon = List(-74.0059, 0.1275)
    val trace = MapTrace(lat, lon)
    val chart = MapChart(trace)
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString))
  }

  test("Map.Geo") {

    val trace = MapTrace(List(40.7127, 51.5072),  List(-74.0059, 0.1275), trace_mode = Some("lines"))


    val geo = Geo() + LatAxis(List(20, 60)) + LongAxis(List(-100, 20))

    println(geo.serialize())



//    val layout = Layout(geo=Some(geo))
//    val chart = MapChart(trace, layout=layout, config=Config())
//    if (true) chart.plot
//    assert(validateJson(chart.serialize.toString))
  }


}










object UnitTestUtils {

  import os._

  def validateJson(json: String): Boolean = {
    val wd = os.pwd/"src"/"test"/"resources"/"javascript"

    // spawn a subprocess
    val sub = os.proc("node", "test.js").spawn(cwd = wd)

    // send the json to node
    sub.stdin.write(json)
    sub.stdin.flush()

    // get the response from node back
    val result = ujson.read(sub.stdout.readLine())

    // end the process
    sub.destroy()

    //val errors = result.arr.filter(e => e("code").toString() != """"unused"""")

    result.arr.foreach(println)

    result.arr.length == 0
  }

  val x_random = List.range(1, 100).map(x => scala.util.Random.nextDouble() * 100)

  // flag for plotting each test
  val plotFlag: Boolean = false
  val plotHistogramTests: Boolean = false

  // create a common configuration to be used in all the tests
  val config: Config = Config(responsive=false, scrollZoom=true)

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
}
