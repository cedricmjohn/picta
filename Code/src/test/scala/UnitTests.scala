package conusviz

import conusviz.charts.{XYChart, XYZChart}
import conusviz.options.AxisOptions.Axis
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import conusviz.Utils._
import conusviz.options.GridOptions.Grid
import conusviz.options.LegendOptions.Legend
import conusviz.options.MarkerOptions._
import org.scalatest.funsuite.AnyFunSuite
import conusviz.traces._
import org.scalatest.FunSuite
import upickle.default._
import upickle.default.{ReadWriter => RW}

object UnitTests {
  val x_int = List(1, 2, 3)
  val y_int = List(1, 2, 3)
  val z_int = List(1, 2, 3)

  val x_double = List(1.5, 2.5, 3.5)
  val y_double = List(15.0, 2.5, 35.9)
  val z_double = List(2,0, 3.5, 5.5)

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

  val x_random = List.range(1, 100).map(x => scala.util.Random.nextDouble() * 100)

  // flag for plotting each test
  val plotFlag: Boolean = false
  val plotHistogramTests: Boolean = false

  // create a common configuration to be used in all the tests
  val config: Config = Config(responsive=false, scrollZoom=true)
}

/*
* Tests for the Histogram and Histogram 2D Contour chart
* */
class HistogramTests extends FunSuite {
  import UnitTests._

  test("XY.Histogram.Basic") {
    val trace = XYTrace(x=x_random, xkey="x", trace_name="test", trace_type="histogram")

    if (plotHistogramTests) {
      val layout = Layout("XY.Histogram.Basic")
      val chart = new XYChart(List(trace), layout, config)
      chart.plot()
    }

    val test = """"""
//    assert(test == write(trace.value))
  }

  test("XY.Histogram.Horizontal") {
    // change xkey to y to get a horizontal histogram
    val trace = XYTrace(x=x_random, xkey="y", trace_name="test", trace_type="histogram")

    if (plotHistogramTests) {
      val layout = Layout("XY.Histogram.Horizontal")
      val chart = new XYChart(List(trace), layout, config)
      chart.plot()
    }

    val test = """"""
//    assert(test == write(trace.value))
  }

  test("XY.Histogram.Color") {

    val marker: Marker = Marker(color = "rgba(255, 100, 102, 0.7)", line = Line())

    // change xkey to y to get a horizontal histogram
    val trace = XYTrace(x_random, xkey="y", trace_name="test", trace_type="histogram", marker=marker)


    if (true) {
      val layout = Layout("XY.Histogram.Horizontal")
      val chart = new XYChart(List(trace), layout, config)
      chart.plot()
    }

    val test = """"""
    //    assert(test == write(trace.value))
  }



}




class LayoutTests extends FunSuite {
  import UnitTests._

  /*
  * Empty Chart Test -> Simply displays an empty set of axis, with a generic title
  * */
  test("XY.Empty") {
    val chart = new XYChart(Nil)
    if (plotFlag) chart.plot()
    val test = s""""""
    assert(test == write(chart.serialize))
  }

  /*
  * Test multiple series plotting and axis composition
  * */
  test("XY.Axis.Composition") {
    // 1. define the axis to plot on the chart
    val ax0: Axis = Axis("xaxis1")
    val ax1: Axis = Axis("yaxis1", title = "y axis 1")
    val ax2: Axis = Axis("yaxis2", title = "y axis 2", side = "right", overlaying = "y")

    // 2. add the axis to the layout to display them on the xy chart
    val layout: Layout = Layout("XY.Axis.Composition", List(ax0, ax1, ax2))

    // 3. define the data to display on the chart
    val trace1 = XYTrace(x=x_int, y=y_double, trace_name="trace0", trace_type="scatter", trace_mode="markers+lines", yaxis="y2")
    val trace2 = XYTrace(x=x_double, y=y_int, trace_name="trace1", trace_type="scatter", trace_mode="markers+lines")

    // 4. combine elements into a single chart
    val chart: XYChart = new XYChart(List(trace1, trace2), layout, config)

    if (plotFlag) chart.plot()

    val test = s""""""

    assert(test == write(chart.serialize))
  }

  test("XY.Grid") {
    // 1. first we define the grid layout - 1 row, 2 columns
    val grid: Grid = Grid(1, 2, "independent")

    // 2. Now define the axes we want to place on the grid
    val ax1: Axis = Axis("xaxis1", title = "x axis 1")
    val ax2: Axis = Axis("xaxis2", title = "x axis 2")

    // 2. define the traces
    val trace1 = XYTrace(x=x_int, y=y_double, trace_name="trace1", trace_type="scatter", trace_mode="markers+lines", xaxis="x1", yaxis="y1")
    val trace2 = XYTrace(x=x_double, y=y_int, trace_name="trace2", trace_type="scatter", trace_mode="markers+lines", xaxis="x2", yaxis="y2")

    // 3. combine into a layout
    val layout: Layout = Layout(title="XY.Axis.Composition", axs=List(ax1, ax2), grid=grid)

    // 4. construct into a chart
    val chart: XYChart = new XYChart(List(trace1, trace2), layout, config)

    if (true) chart.plot()
  }
}

class XYTests extends FunSuite {
  import UnitTests._

  test("XY.Scatter.Int") {
    val trace = XYTrace(x_int, y_int, trace_name="test", trace_type="scatter", trace_mode="markers+lines")
    if (plotFlag) {
      val layout = Layout("XY.Scatter.Int")
      val chart = new XYChart(List(trace), layout, config)
      chart.plot()
    }
    val test = """{"name":"test","type":"scatter","mode":"markers+lines","xaxis":"x1","yaxis":"y1","x":[1,2,3],"y":[1,2,3]}"""
    assert(test == write(trace.value))
  }

  test("XY.Scatter.String") {
    val trace = XYTrace(x_str, y_str, trace_name="test", trace_type="scatter", trace_mode="markers")
    if (plotFlag) {
      val layout = Layout("XY.Scatter.String")
      val chart = new XYChart(List(trace), layout, config)
      chart.plot()
    }
    val test = """{"name":"test","type":"scatter","mode":"markers","xaxis":"x1","yaxis":"y1","x":["1","2","3"],"y":["a","b","c"]}"""
    assert(test == write(trace.value))
  }

  test("XY.Scatter.Double") {
    val trace = XYTrace(x_double, y_double, trace_name="test", trace_type="scatter", trace_mode="lines")
    if (plotFlag) {
      val layout = Layout("XY.Scatter.Double")
      val chart = new XYChart(List(trace), layout, config)
      chart.plot()
    }
    val test = """{"name":"test","type":"scatter","mode":"lines","xaxis":"x1","yaxis":"y1","x":[1.5,2.5,3.5],"y":[15,2.5,35.9]}"""
    assert(test == write(trace.value))
  }



  test("XY.Bar") {
    val trace = XYTrace(y_str, y_double, trace_name="test", trace_type="bar")
    if (plotFlag) {
      val layout = Layout("XY.Bar")
      val chart = new XYChart(List(trace), layout, config)
      chart.plot()
    }

    val test = """{"name":"test","type":"bar","mode":"markers","xaxis":"x1","yaxis":"y1","x":["a","b","c"],"y":[15,2.5,35.9]}"""
    assert(test == write(trace.value))

    assertThrows[IllegalArgumentException] {
      XYTrace(x_int, y_int, trace_name="test", trace_type="not_bar")
    }
  }

  test("XY.Histogram2dContour") {
    val trace = XYTrace(x_double, y_double, trace_name="test", trace_type="histogram2dcontour")
    if (plotFlag) {
      val layout = Layout("XY.Histogram2dContour")
      val chart = new XYChart(List(trace), layout, config)
      chart.plot()
    }
    val test = """{"name":"test","type":"histogram2dcontour","mode":"markers","xaxis":"x1","yaxis":"y1","x":[1.5,2.5,3.5],"y":[15,2.5,35.9]}"""
    assert(test == write(trace.value))
  }

  test("XY.createTrace.Invalid") {
    val chart_types = Seq("contour", "heatmap", "scatter3d")
    chart_types.map(t => {
      assertThrows[IllegalArgumentException] {
        XYTrace(x_int, y_int, trace_name="test", trace_type=t, trace_mode="lines")
      }
    })
  }
}

class XYZTests extends FunSuite {
  import UnitTests._

  test("XYZ.Scatter3D") {
    val trace = XYZTrace(x_double, y_double, z_double, trace_name="trace", trace_type="scatter3d")
    if (plotFlag) {
      val layout = Layout("XYZ.Scatter3D")
      val chart = new XYZChart(List(trace), layout, config)
      chart.plot()
    }
    val test ="""{"name":"trace","type":"scatter3d","mode":"","xaxis":"","yaxis":"","x":[1.5,2.5,3.5],"y":[15,2.5,35.9],"z":[2,0,3.5,5.5]}"""
    assert(test == write(trace.value))
  }

  test("XYZ.line3D") {
    val trace = XYZTrace(x_double, y_double, z_double, trace_name="trace", trace_type="scatter3d", trace_mode="lines")
    if (plotFlag) {
      val layout = Layout("XYZ.line3D")
      val chart = new XYZChart(List(trace), layout, config)
      chart.plot()
    }
    val test ="""{"name":"trace","type":"scatter3d","mode":"lines","xaxis":"","yaxis":"","x":[1.5,2.5,3.5],"y":[15,2.5,35.9],"z":[2,0,3.5,5.5]}"""
    assert(test == write(trace.value))
  }

  test("XYZ.Surface") {
    val trace = XYZTrace(z_surface, trace_name="trace", trace_type="surface")
    if (plotFlag) {
      val layout = Layout("XYZ.Surface")
      val chart = new XYZChart(List(trace), layout, config)
      chart.plot()
    }
    val test ="""{"name":"trace","type":"surface","mode":"","xaxis":"","yaxis":"","z":[[8.83,8.89,8.81,8.87,8.9,8.87,8.89,8.94,8.85,8.94,8.96,8.92,8.84,8.9,8.82],[8.92,8.93,8.91,8.79,8.85,8.79,8.9,8.94,8.92,8.79,8.88,8.81,8.9,8.95,8.92],[8.8,8.82,8.78,8.91,8.94,8.92,8.75,8.78,8.77,8.91,8.95,8.92,8.8,8.8,8.77],[8.91,8.95,8.94,8.74,8.81,8.76,8.93,8.98,8.99,8.89,8.99,8.92,9.1,9.13,9.11],[8.97,8.97,8.91,9.09,9.11,9.11,9.04,9.08,9.05,9.25,9.28,9.27,9,9.01,9],[9.2,9.23,9.2,8.99,8.99,8.98,9.18,9.2,9.19,8.93,8.97,8.97,9.18,9.2,9.18]]}""".stripMargin
    assert(test == write(trace.value))
  }

  test("XYZ.Heatmap") {
    val trace = XYZTrace(z_surface, "trace", "heatmap")
    if (plotFlag) {
      val layout = Layout("XYZ.Heatmap")
      val chart = new XYZChart(List(trace), layout, config)
      chart.plot()
    }

    println(trace.value)
    val test ="""{"name":"trace","type":"heatmap","mode":"","xaxis":"","yaxis":"","z":[[8.83,8.89,8.81,8.87,8.9,8.87,8.89,8.94,8.85,8.94,8.96,8.92,8.84,8.9,8.82],[8.92,8.93,8.91,8.79,8.85,8.79,8.9,8.94,8.92,8.79,8.88,8.81,8.9,8.95,8.92],[8.8,8.82,8.78,8.91,8.94,8.92,8.75,8.78,8.77,8.91,8.95,8.92,8.8,8.8,8.77],[8.91,8.95,8.94,8.74,8.81,8.76,8.93,8.98,8.99,8.89,8.99,8.92,9.1,9.13,9.11],[8.97,8.97,8.91,9.09,9.11,9.11,9.04,9.08,9.05,9.25,9.28,9.27,9,9.01,9],[9.2,9.23,9.2,8.99,8.99,8.98,9.18,9.2,9.19,8.93,8.97,8.97,9.18,9.2,9.18]]}"""
    assert(test == write(trace.value))
  }

  test("XYZ.HeatmapWithXY") {
    val x: List[Int] = List.range(1, 16).toList
    val y: List[String] = List("a", "b", "c", "d", "e", "f")
    val trace = XYZTrace(x, y, z_surface, "trace", "heatmap")
    if (plotFlag) {
      val layout = Layout("XYZ.Heatmap")
      val chart = new XYZChart(List(trace), layout, config)
      chart.plot()
    }
    val test ="""{"name":"trace","type":"heatmap","mode":"","xaxis":"","yaxis":"","x":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15],"y":["a","b","c","d","e","f"],"z":[[8.83,8.89,8.81,8.87,8.9,8.87,8.89,8.94,8.85,8.94,8.96,8.92,8.84,8.9,8.82],[8.92,8.93,8.91,8.79,8.85,8.79,8.9,8.94,8.92,8.79,8.88,8.81,8.9,8.95,8.92],[8.8,8.82,8.78,8.91,8.94,8.92,8.75,8.78,8.77,8.91,8.95,8.92,8.8,8.8,8.77],[8.91,8.95,8.94,8.74,8.81,8.76,8.93,8.98,8.99,8.89,8.99,8.92,9.1,9.13,9.11],[8.97,8.97,8.91,9.09,9.11,9.11,9.04,9.08,9.05,9.25,9.28,9.27,9,9.01,9],[9.2,9.23,9.2,8.99,8.99,8.98,9.18,9.2,9.19,8.93,8.97,8.97,9.18,9.2,9.18]]}"""
    assert(test == write(trace.value))
  }

  test("XYZ.InvalidTrace") {
    val chart_types = Seq("scatter", "scattergl", "bar")
    chart_types.map(t => {
      assertThrows[IllegalArgumentException] {
        XYZTrace(x_int, y_int, z_int, "test", t, "lines").value
      }
    })
  }
}


