package org.carbonateresearch.picta

import org.carbonateresearch.picta
import org.carbonateresearch.picta.options.{Axis, Subplot}
import org.scalatest.funsuite.AnyFunSuite
import org.carbonateresearch.picta.series.Mode.MARKERS
import XYChart.SCATTER
import org.carbonateresearch.picta.UnitTestUtils.{config, validateJson, x_double, x_int, y_double, y_int}

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
    val ax0 = Axis(position = "x", title = " x axis 1")
    val ax1 = Axis(position = "y", title = "y axis 1")
    val ax2 = Axis(position = "y2", title = "y axis 2", side = "right", overlaying = "y")

    // 2. add the axis to the layout to display them on the xy chart
    val layout = picta.Layout("XY.Axis.Composition") setAxes List(ax0, ax1, ax2)

    // 3. define the data to display on the chart
    val trace1 = XY(x = x_int, y = y_double, series_type = SCATTER, series_mode = MARKERS, yaxis = "y2")
    val trace2 = XY(x = x_double, y = y_int, series_type = SCATTER, series_mode = MARKERS)

    // 4. combine elements into a single chart
    val chart = Chart() setData List(trace1, trace2) setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Grid") {
    // 1. first we define the grid layout - 1 row, 2 columns
    val grid = Subplot(1, 2)

    // 2. Now define the axes we want to place on the grid
    val ax1 = Axis("x") setTitle "x axis 1"
    val ax2 = Axis("x2") setTitle "x axis 2"

    // 2. define the traces
    val trace1 = XY(x = x_int, y = y_double, series_type = SCATTER, series_mode = MARKERS)
    val trace2 = XY(x = x_double, y = y_int, series_type = SCATTER, series_mode = MARKERS, xaxis = "x2", yaxis = "y2")

    // 3. combine into a layout
    val layout = picta.Layout(title = "XY.Axis.Composition") setAxes List(ax1, ax2) setSubplot grid

    // 4. construct into a chart
    val chart = Chart(List(trace1, trace2), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
