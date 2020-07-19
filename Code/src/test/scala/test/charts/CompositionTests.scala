package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.options.{Axis, Legend, RIGHT, YAxis}
import org.carbonateresearch.picta._
import org.scalatest.funsuite.AnyFunSuite

class CompositionTests extends AnyFunSuite {

  val plotFlag = false

  val series1 = XY(x_int, y_int) asType SCATTER drawSymbol MARKERS
  val series2 = XY(x_int, y_double) asType SCATTER drawSymbol MARKERS

  test("XY.Chart.Add.Traces") {
    val chart = Chart() addSeries(series1, series2)
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Layout") {
    val chart = Chart() setLayout Layout("XY.Chart.Add.Layout") addSeries(series1, series2)
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Config") {
    val chart = Chart() setConfig config setLayout Layout("XY.Chart.Add.Config") addSeries(series1, series2)
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Layout.Add.Axis") {
    val series3 = XY(x_int, z_int) asType SCATTER drawSymbol MARKERS setAxis YAxis(2)
    val layout = (picta.Layout("XY.Chart.Add.Config") setLegend Legend()
      setAxes YAxis(position = 2, title = "second y axis", overlaying = YAxis(), side = RIGHT))

    val chart = Chart() setConfig Config(false, false) addSeries(series1, series2, series3) setLayout layout
    val canvas = Canvas() setChart(0, 0, chart)
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }
}
