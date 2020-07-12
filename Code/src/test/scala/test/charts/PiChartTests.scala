package picta

import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.options.Layout
import picta.series.XY
import picta.series.XYChartType.PIE
import test.UnitTestUtils.validateJson

class PieChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Pie") {
    val series = XY(List(19, 26, 55), List("Residential", "Non-Residential", "Utility"), series_type = PIE)
    val chart = Chart() setData series setLayout Layout("XY.Pie")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
