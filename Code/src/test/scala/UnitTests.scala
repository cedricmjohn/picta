package conusviz

import conusviz.charts.{XYChart, XYZChart}
import conusviz.options.AxisOptions.Axis
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import org.scalatest.funsuite.AnyFunSuite
import conusviz.traces._
import org.scalatest.FunSuite
import upickle.default._
import upickle.default.{ReadWriter => RW}

class UnitTests extends FunSuite {

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

  // flag for plotting each test
  val plotFlag: Boolean = false

  // create a common configuration to be used in all the tests
  val config: Config = Config(responsive=false, scrollZoom=true)




  




}



//  /*
//  * XY Chart tests
//  * */
//  test("XY.Scatter.Int") {
//    val trace = XYTrace(x_int, y_int, "test", "scatter", "markers+lines")
//    if (true) {
//      val layout = Layout("XY.Scatter.Int", true)
//      val chart = new XYChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test = """{"name":"test","type":"scatter","mode":"markers+lines","xaxis":"x1","yaxis":"y1","x":[1,2,3],"y":[1,2,3]}"""
//    assert(test == write(trace.value))
//  }
//
//  test("XY.Scatter.String") {
//    val trace = XYTrace(x_str, y_str, "test", "scatter", "markers")
//    if (plotFlag) {
//      val layout = Layout("XY.Scatter.String", true)
//      val chart = new XYChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test = """{"name":"test","type":"scatter","mode":"markers","xaxis":"x1","yaxis":"y1","x":["1","2","3"],"y":["a","b","c"]}"""
//    assert(test == write(trace.value))
//  }
//
//  test("XY.Scatter.Double") {
//    val trace = XYTrace(x_double, y_double, "test", "scatter", "lines")
//    if (plotFlag) {
//      val layout = Layout("XY.Scatter.Double", true)
//      val chart = new XYChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test = """{"name":"test","type":"scatter","mode":"lines","xaxis":"x1","yaxis":"y1","x":[1.5,2.5,3.5],"y":[15,2.5,35.9]}"""
//    assert(test == write(trace.value))
//  }
//
//  test("XY.Histogram") {
//    val trace = XYTrace(x_double, "test", "histogram", "vertical")
//    if (plotFlag) {
//      val layout = Layout("XY.Histogram", true)
//      val chart = new XYChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test = """{"name":"test","type":"histogram","mode":"vertical","xaxis":"x1","yaxis":"y1","x":[1.5,2.5,3.5]}"""
//    assert(test == write(trace.value))
//  }
//
//  test("XY.Bar") {
//    val trace = XYTrace(y_str, y_double, "test", "bar")
//    if (plotFlag) {
//      val layout = Layout("XY.Bar", true)
//      val chart = new XYChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test = """{"name":"test","type":"bar","mode":"","xaxis":"x1","yaxis":"y1","x":["a","b","c"],"y":[15,2.5,35.9]}"""
//    assert(test == write(trace.value))
//
//    assertThrows[IllegalArgumentException] {
//      XYTrace(x_int, y_int, "test", "not_bar")
//    }
//  }
//
//  test("XY.Histogram2dContour") {
//    val trace = XYTrace(x_double, y_double, "test", "histogram2dcontour")
//    if (plotFlag) {
//      val layout = Layout("XY.Histogram2dContour", true)
//      val chart = new XYChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test = """{"name":"test","type":"histogram2dcontour","mode":"","xaxis":"x1","yaxis":"y1","x":[1.5,2.5,3.5],"y":[15,2.5,35.9]}"""
//    assert(test == write(trace.value))
//  }
//
//  test("XY.createTrace.Invalid") {
//    val chart_types = Seq("contour", "heatmap", "scatter3d")
//    chart_types.map(t => {
//      assertThrows[IllegalArgumentException] {
//        XYTrace(x_int, y_int, "test", t, "lines")
//      }
//    })
//  }
//
//  /*
//  * XYZ Chart tests
//  * */
//
//  test("XYZ.Scatter3D") {
//    val trace = XYZTrace(x_double, y_double, z_double, "trace", "scatter3d")
//    if (plotFlag) {
//      val layout = Layout("XYZ.Scatter3D", true)
//      val chart = new XYZChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test ="""{"name":"trace","type":"scatter3d","mode":"","xaxis":"","yaxis":"","x":[1.5,2.5,3.5],"y":[15,2.5,35.9],"z":[2,0,3.5,5.5]}"""
//    assert(test == write(trace.value))
//  }
//
//  test("XYZ.line3D") {
//    val trace = XYZTrace(x_double, y_double, z_double, "trace", "scatter3d", "lines")
//    if (plotFlag) {
//      val layout = Layout("XYZ.line3D", true)
//      val chart = new XYZChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test ="""{"name":"trace","type":"scatter3d","mode":"lines","xaxis":"","yaxis":"","x":[1.5,2.5,3.5],"y":[15,2.5,35.9],"z":[2,0,3.5,5.5]}"""
//    assert(test == write(trace.value))
//  }
//
//  test("XYZ.Surface") {
//    val trace = XYZTrace(z_surface, "trace", "surface")
//    if (plotFlag) {
//      val layout = Layout("XYZ.Surface", true)
//      val chart = new XYZChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test ="""{"name":"trace","type":"surface","mode":"","xaxis":"","yaxis":"","z":[[8.83,8.89,8.81,8.87,8.9,8.87,8.89,8.94,8.85,8.94,8.96,8.92,8.84,8.9,8.82],[8.92,8.93,8.91,8.79,8.85,8.79,8.9,8.94,8.92,8.79,8.88,8.81,8.9,8.95,8.92],[8.8,8.82,8.78,8.91,8.94,8.92,8.75,8.78,8.77,8.91,8.95,8.92,8.8,8.8,8.77],[8.91,8.95,8.94,8.74,8.81,8.76,8.93,8.98,8.99,8.89,8.99,8.92,9.1,9.13,9.11],[8.97,8.97,8.91,9.09,9.11,9.11,9.04,9.08,9.05,9.25,9.28,9.27,9,9.01,9],[9.2,9.23,9.2,8.99,8.99,8.98,9.18,9.2,9.19,8.93,8.97,8.97,9.18,9.2,9.18]]}""".stripMargin
//    assert(test == write(trace.value))
//  }
//
//  test("XYZ.Heatmap") {
//    val trace = XYZTrace(z_surface, "trace", "heatmap")
//    if (plotFlag) {
//      val layout = Layout("XYZ.Heatmap", true)
//      val chart = new XYZChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test ="""{"name":"trace","type":"heatmap","mode":"","xaxis":"","yaxis":"","z":[[8.83,8.89,8.81,8.87,8.9,8.87,8.89,8.94,8.85,8.94,8.96,8.92,8.84,8.9,8.82],[8.92,8.93,8.91,8.79,8.85,8.79,8.9,8.94,8.92,8.79,8.88,8.81,8.9,8.95,8.92],[8.8,8.82,8.78,8.91,8.94,8.92,8.75,8.78,8.77,8.91,8.95,8.92,8.8,8.8,8.77],[8.91,8.95,8.94,8.74,8.81,8.76,8.93,8.98,8.99,8.89,8.99,8.92,9.1,9.13,9.11],[8.97,8.97,8.91,9.09,9.11,9.11,9.04,9.08,9.05,9.25,9.28,9.27,9,9.01,9],[9.2,9.23,9.2,8.99,8.99,8.98,9.18,9.2,9.19,8.93,8.97,8.97,9.18,9.2,9.18]]}""".stripMargin
//    assert(test == write(trace.value))
//  }
//
//  test("XYZ.HeatmapWithXY") {
//    val x: List[Int] = List.range(1, 16).toList
//    val y: List[String] = List("a", "b", "c", "d", "e", "f")
//    val trace = XYZTrace(x, y, z_surface, "trace", "heatmap")
//    if (plotFlag) {
//      val layout = Layout("XYZ.Heatmap", true)
//      val chart = new XYZChart(List(trace), layout, config)
//      chart.plot()
//    }
//    val test ="""{"name":"trace","type":"heatmap","mode":"","xaxis":"","yaxis":"","x":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15],"y":["a","b","c","d","e","f"],"z":[[8.83,8.89,8.81,8.87,8.9,8.87,8.89,8.94,8.85,8.94,8.96,8.92,8.84,8.9,8.82],[8.92,8.93,8.91,8.79,8.85,8.79,8.9,8.94,8.92,8.79,8.88,8.81,8.9,8.95,8.92],[8.8,8.82,8.78,8.91,8.94,8.92,8.75,8.78,8.77,8.91,8.95,8.92,8.8,8.8,8.77],[8.91,8.95,8.94,8.74,8.81,8.76,8.93,8.98,8.99,8.89,8.99,8.92,9.1,9.13,9.11],[8.97,8.97,8.91,9.09,9.11,9.11,9.04,9.08,9.05,9.25,9.28,9.27,9,9.01,9],[9.2,9.23,9.2,8.99,8.99,8.98,9.18,9.2,9.19,8.93,8.97,8.97,9.18,9.2,9.18]]}"""
//    assert(test == write(trace.value))
//  }
//
//  test("XYZ.InvalidTrace") {
//    val chart_types = Seq("scatter", "scattergl", "bar")
//    chart_types.map(t => {
//      assertThrows[IllegalArgumentException] {
//        XYZTrace(x_int, y_int, z_int, "test", t, "lines").value
//      }
//    })
//  }