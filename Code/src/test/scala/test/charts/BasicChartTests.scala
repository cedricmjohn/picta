package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.{Chart, Layout, XY}
import org.carbonateresearch.picta.series.Mode.MARKERS
import org.carbonateresearch.picta.XYChart.{BAR, SCATTER}
import org.carbonateresearch.picta.UnitTestUtils.{validateJson, x_int, y_double, y_int, y_str}
import org.scalatest.funsuite.AnyFunSuite

class BasicChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Scatter.Int") {
    val series = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS)
    val chart = Chart() setData series setLayout picta.Layout("XY.Scatter.Int")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Bar") {
    val series = XY(y_str, y_double, series_type = BAR)
    val chart = Chart() setData series setLayout picta.Layout("XY.Bar")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Multiple") {
    val series1 = XY(x_int, y_int, series_type = BAR)
    val series2 = XY(x_int, y_double, series_type = SCATTER, series_mode = MARKERS) setName "test"
    val chart = Chart() setData List(series1, series2) setLayout picta.Layout("XY.Multiple")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Scatter.Simplest") {
    val series = XY(x = x_int, y = y_int, series_type = SCATTER)
    val chart = Chart() setData series setLayout picta.Layout("XY.Scatter.Simplest")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}



