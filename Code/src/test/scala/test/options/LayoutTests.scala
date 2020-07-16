package org.carbonateresearch.picta

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.options.{Axis, Grid, XAxis, YAxis}
import org.scalatest.funsuite.AnyFunSuite

class LayoutTests extends AnyFunSuite {

  val plotFlag = false

  /** Empty Chart Test -> Simply displays an empty set of axis, with a generic title */
  test("XY.Empty") {
    val chart = Chart()
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  /** Test multiple series plotting and axis composition */
  test("XY.Axis.Composition") {
    // 1. define the axis to plot on the chart
    val ax0 = XAxis(position = 1, title = " x axis 1")
    val ax1 = YAxis(position = 1, title = "y axis 1")
    val ax2 = YAxis(position = 2, title = "y axis 2", side = "right", overlaying = "y")

    // 2. add the axis to the layout to display them on the xy chart
    val layout = picta.Layout("XY.Axis.Composition") setAxes List(ax0, ax1, ax2)

    // 3. define the data to display on the chart
    val trace1 = XY(x = x_int, y = y_double, `type` = SCATTER, mode = MARKERS, yaxis = YAxis(2))
    val trace2 = XY(x = x_double, y = y_int, `type` = SCATTER, mode = MARKERS)

    // 4. combine elements into a single chart
    val chart = Chart() addSeries List(trace1, trace2) setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Grid") {
    // 1. first we define the grid layout - 1 row, 2 columns
    val grid = Grid(1, 2)

    // 2. Now define the axes we want to place on the grid
    val ax1 = XAxis() setTitle "x axis 1"
    val ax2 = XAxis(2) setTitle "x axis 2"

    // 2. define the traces
    val trace1 = XY(x = x_int, y = y_double, `type` = SCATTER, mode = MARKERS)
    val trace2 = XY(x = x_double, y = y_int, `type` = SCATTER, mode = MARKERS, xaxis = XAxis(2), yaxis = YAxis(2))

    // 3. combine into a layout
    val layout = picta.Layout(title = "XY.Axis.Composition") setAxes List(ax1, ax2) setSubplot grid

    // 4. construct into a chart
    val chart = Chart(List(trace1, trace2), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
