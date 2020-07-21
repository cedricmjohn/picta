package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.{MARKERS, SCATTER, _}
import org.scalatest.funsuite.AnyFunSuite

class BasicChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Scatter") {
    val series = XY(x_int, y_int) asType SCATTER drawSymbol MARKERS
    val chart = Chart() addSeries series setTitle "XY.Scatter"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Bar") {
    val series = XY(y_str, y_double) asType BAR
    val chart = Chart() addSeries series setTitle "XY.Bar"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Multiple") {
    val series1 = XY(x_int, y_int) asType BAR
    val series2 = XY(x_int, y_double) asType SCATTER drawSymbol MARKERS setName "test"
    val chart = Chart() addSeries(series1, series2) setTitle "XY.Multiple"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Scatter.Simplest") {
    val series = XY(x = x_int, y = y_int) asType SCATTER
    val chart = Chart() addSeries series setTitle "XY.Scatter.Simplest"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}



