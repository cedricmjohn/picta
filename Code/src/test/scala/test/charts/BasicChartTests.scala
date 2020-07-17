package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta._
import org.carbonateresearch.picta.{SCATTER, MARKERS}
import org.scalatest.funsuite.AnyFunSuite

class BasicChartTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Scatter.Int") {
    val series = XY(x_int, y_int) asType SCATTER drawSymbol MARKERS
    val chart = Chart() addSeries series setLayout Layout("XY.Scatter.Int")
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Bar") {
    val series = XY(y_str, y_double) asType BAR
    val chart = Chart() addSeries series setLayout Layout("XY.Bar")
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Multiple") {
    val series1 = XY(x_int, y_int) asType BAR
    val series2 = XY(x_int, y_double) asType SCATTER drawSymbol MARKERS setName "test"
    val chart = Chart() addSeries(series1, series2) setLayout Layout("XY.Multiple")
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Scatter.Simplest") {
    val series = XY(x = x_int, y = y_int) asType SCATTER
    val chart = Chart() addSeries series setLayout Layout("XY.Scatter.Simplest")
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }
}



