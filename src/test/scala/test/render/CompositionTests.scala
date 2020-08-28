/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta.render

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta._
import org.carbonateresearch.picta.options.Legend
import org.scalatest.funsuite.AnyFunSuite

/**
 * This tests chart composition; how charts can be assembled from smaller pieces.
 */
class CompositionTests extends AnyFunSuite {

  val plotFlag = false

  val series1 = XY(x_int, y_int) asType SCATTER drawStyle MARKERS
  val series2 = XY(x_int, y_double) asType SCATTER drawStyle MARKERS

  test("XY.Chart.Add.Traces") {
    val chart = Chart() addSeries(series1, series2)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Layout") {
    val chart = Chart() setChartLayout ChartLayout("XY.Chart.Add.Layout") addSeries(series1, series2)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Chart.Add.Config") {
    val chart = Chart() setConfig(false, false) setTitle "XY.Chart.Add.Config" addSeries(series1, series2)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Layout.Add.Axis") {
    val series3 = XY(x_int, z_int) asType SCATTER drawStyle MARKERS setAxis Axis(Y, 2)

    val layout = (
      picta.ChartLayout("XY.Chart.Add.Config")
        setLegend Legend()
        setAxes Axis(Y, position = 2, title = "second y axis", overlaying = Axis(Y), side = RIGHT_SIDE)
      )

    val chart = Chart() setConfig(false, false) addSeries(series1, series2, series3) setChartLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
