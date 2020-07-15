package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.options.{Axis, Legend, YAxis}
import org.carbonateresearch.picta._
import org.scalatest.funsuite.AnyFunSuite

class CompositionTests extends AnyFunSuite {

  val plotFlag = false

  val series1 = XY(x_int, y_int, `type` = SCATTER, mode = MARKERS)
  val series2 = XY(x_int, y_double, `type` = SCATTER, mode = MARKERS)

  test("XY.Chart.Add.Traces") {
    val chart = Chart() addSeries(series1, series2)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Layout") {
    val chart = Chart() setLayout Layout("XY.Chart.Add.Layout") addSeries(series1, series2)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Config") {
    val chart = Chart() setConfig config setLayout picta.Layout("XY.Chart.Add.Config") addSeries(series1, series2)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Layout.Add.Axis") {
    val series3 = XY(x_int, z_int, `type` = SCATTER, mode = MARKERS, yaxis = YAxis(2))
    val layout = (picta.Layout("XY.Chart.Add.Config") setLegend Legend()
      setAxes YAxis(position = 2, title = "second y axis", overlaying = "y", side = "right"))

    val chart = Chart() setConfig Config(false, false) addSeries(series1, series2, series3) setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
