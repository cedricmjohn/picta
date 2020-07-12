package picta

import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.options.{Axis, Config, Layout, Legend}
import picta.series.ModeType.MARKERS
import picta.series.XY
import picta.series.XYChartType.SCATTER
import test.UnitTestUtils.{validateJson, x_int, y_double, y_int, z_int, config}

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
    val layout = (Layout("XY.Chart.Add.Config") setLegend Legend()
      setAxes Axis(position = "y2", title = "second y axis", overlaying = "y", side = "right"))

    val chart = Chart() setConfig Config(false, false) setData(series1, series2, series3) setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
