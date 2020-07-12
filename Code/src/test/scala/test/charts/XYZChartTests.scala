package picta

import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.options.Layout
import picta.series.ModeType.LINES
import picta.series.XYZ
import picta.series.XYZChartType.{SCATTER3D, SURFACE}
import test.UnitTestUtils.{config, validateJson, x_double, y_double, z_double, z_surface}

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
