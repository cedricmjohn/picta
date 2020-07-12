package picta

import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.options.{Layout, Marker}
import picta.series.ModeType.MARKERS
import picta.series.XY
import picta.series.XYChartType.SCATTER
import test.UnitTestUtils.{validateJson, x_int, y_int, z_double}

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