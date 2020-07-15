package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils.validateJson
import org.carbonateresearch.picta.{Chart, PIE, XY}
import org.scalatest.funsuite.AnyFunSuite

class PieChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Pie") {
    val series = XY(List(19, 26, 55), List("Residential", "Non-Residential", "Utility"), `type` = PIE)
    val chart = Chart() addSeries series setLayout picta.Layout("XY.Pie")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
