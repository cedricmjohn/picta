package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.{Chart, Layout, XY}
import org.carbonateresearch.picta.XYChart.PIE
import org.carbonateresearch.picta.UnitTestUtils.validateJson
import org.scalatest.funsuite.AnyFunSuite

class PieChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Pie") {
    val series = XY(List(19, 26, 55), List("Residential", "Non-Residential", "Utility"), series_type = PIE)
    val chart = Chart() setData series setLayout picta.Layout("XY.Pie")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
