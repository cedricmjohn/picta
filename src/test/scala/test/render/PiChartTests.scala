package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils.validateJson
import org.carbonateresearch.picta.{Chart, PIE, PieElement, XY}
import org.scalatest.funsuite.AnyFunSuite

/**
 * This tests the Pie Chart and it's various methods.
 */
class PieChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Pie") {
    val series = XY(List(19, 26, 55), List("Residential", "Non-Residential", "Utility")) asType PIE
    val chart = Chart() addSeries series setTitle "XY.Pie"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Pie.PieElements") {
    val a = PieElement(value = 19, name = "Residential")
    val b = PieElement(value = 26, name = "Non-Residential")
    val c = PieElement(value = 26, name = "Utility")
    val series = XY(x = List(a, b, c))
    val chart = Chart() addSeries series setTitle "XY.Pie.PieElements"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
