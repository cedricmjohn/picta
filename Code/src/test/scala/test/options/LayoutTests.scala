package org.carbonateresearch.picta

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.options.{RIGHT, XAxis, YAxis}
import org.scalatest.funsuite.AnyFunSuite

class LayoutTests extends AnyFunSuite {

  val plotFlag = false

  /** Empty Chart Test -> Simply displays an empty set of axis, with a generic title */
  test("XY.Empty") {
    val chart = Chart()
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  /** Test multiple series plotting and axis composition */
  test("XY.Axis.Composition") {
    // 1. define the axis to plot on the chart
    val ax0 = XAxis(position = 1, title = " x axis 1")
    val ax1 = YAxis(position = 1, title = "y axis 1")
    val ax2 = YAxis(position = 2, title = "y axis 2", side = RIGHT, overlaying = "y")

    // 2. add the axis to the layout to display them on the xy chart
    val layout = picta.Layout("XY.Axis.Composition") setAxes List(ax0, ax1, ax2)

    // 3. define the data to display on the chart
    val trace1 = XY(x = x_int, y = y_double) asType SCATTER drawSymbol MARKERS setAxis YAxis(2)
    val trace2 = XY(x = x_double, y = y_int) asType SCATTER drawSymbol MARKERS

    // 4. combine elements into a single chart
    val chart = Chart() addSeries(trace1, trace2) setLayout layout setConfig config
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }
}
