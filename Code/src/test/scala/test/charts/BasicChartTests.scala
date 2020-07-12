package picta

import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.options.{Layout}
import picta.series.Mode.MARKERS
import picta.series.XY
import picta.series.XYChart.{BAR, SCATTER}
import test.UnitTestUtils.{validateJson, x_int, y_double, y_int, y_str}

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



