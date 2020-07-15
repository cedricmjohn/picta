package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta._
import org.scalatest.funsuite.AnyFunSuite

class BasicChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Scatter.Int") {
    val series = XY(x_int, y_int, `type` = SCATTER, mode = MARKERS)
    val chart = Chart() addSeries series setLayout picta.Layout("XY.Scatter.Int")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Bar") {
    val series = XY(y_str, y_double, `type` = BAR)
    val chart = Chart() addSeries series setLayout picta.Layout("XY.Bar")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Multiple") {
    val series1 = XY(x_int, y_int, `type` = BAR)
    val series2 = XY(x_int, y_double, `type` = SCATTER, mode = MARKERS) setName "test"
    val chart = Chart() addSeries List(series1, series2) setLayout picta.Layout("XY.Multiple")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Scatter.Simplest") {
    val series = XY(x = x_int, y = y_int, `type` = SCATTER)
    val chart = Chart() addSeries series setLayout picta.Layout("XY.Scatter.Simplest")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}



