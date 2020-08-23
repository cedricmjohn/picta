package org.carbonateresearch.picta

import org.carbonateresearch.picta.UnitTestUtils._
import org.scalatest.funsuite.AnyFunSuite
import upickle.default.write

/**
 * This tests the Axis components, and its various methods.
 */
class AxisTests extends AnyFunSuite {

  val plotFlag = false

  test("Axis.Constructor") {
    val axis = Axis(X, position = 2) setTitle "second x-axis" setDomain(0.85, 1.0)
    val test = """{"xaxis2":{"title":{"text":"second x-axis"},"showgrid":true,"zeroline":false,"showline":false,"domain":[0.85,1]}}"""
    assert(test == write(axis.serialize))
  }

  test("Axis.MultipleAxes") {
    val yaxis = Axis(Y) setTickDisplayFormat ("0.3f")

    val series = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setAxis yaxis
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "Axis.MultipleAxes"
        addAxes yaxis
      )

    if (plotFlag) chart.plot

    assert(validateJson(chart.serialize.toString))
  }

  test("Axis.SetAxisRange") {
    val yaxis = Axis(Y) setTickDisplayFormat ("0.3f")

    val series = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setAxis yaxis
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "Axis.SetAxisRange"
        addAxes yaxis
        setXAxisLimits(-100, 100)
        setYAxisLimits(-100, 100)
        setXAxisTitle ("my new x axis")
      )

    if (plotFlag) chart.plot

    assert(validateJson(chart.serialize.toString))
  }

  test("Axis.SetTick") {
    val yaxis = Axis(Y) setTickDisplayFormat ("0.3f")

    val series = (
      XY(List.range(0, 100), List.range(0, 100))
        asType SCATTER
        drawStyle MARKERS
        setAxis yaxis
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "XY.Scatter.Int"
        addAxes yaxis
        setXAxisLimits(0, 100)
        setYAxisLimits(-100, 100)
        setXAxisTitle ("my new x axis")
        setXAxisStartTick 50
        setXAxisTickGap 25
      )

    if (plotFlag) chart.plot

    assert(validateJson(chart.serialize.toString))
  }

  test("Axis.SetAxisLog") {
    val yaxis = Axis(Y) setTickDisplayFormat ("0.3f")

    val series = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setAxis yaxis
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "Axis.SetAxisLog"
        addAxes yaxis
        drawXAxisLog true
        drawYAxisLog true
      )

    if (plotFlag) chart.plot

    assert(validateJson(chart.serialize.toString))
  }

  test("Axis.SetAxisReversed") {
    val yaxis = Axis(Y) setTickDisplayFormat ("0.3f")

    val series = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setAxis yaxis
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "Axis.SetAxisReversed"
        addAxes yaxis
        drawXAxisReversed true
        drawYAxisReversed true
      )

    if (plotFlag) chart.plot

    assert(validateJson(chart.serialize.toString))
  }

  test("Axis.MultipleAxes2") {
    val series1 = XY(x_double, y_double) asType SCATTER drawStyle MARKERS

    // The following maps the series onto the second Y axis.
    val series2 = (
      XY(x_double, z_double)
        asType SCATTER
        drawStyle MARKERS
        setAxis Axis(Y, 2)
        setName ("Series 2 using Y axis 2")
      )

    val series3 = series1.copy() setName ("Series 3 using Y axis 1")

    val chart = (
      Chart()
        setTitle "Using Multiple Axes"
        // the following makes the chart unresponsive
        setConfig(false, false)
        addSeries(series3, series2)
        // the following tells the chart how to render the second Y Axis
        addAxes Axis(Y, position = 2, title = "Second y axis", overlaying = Axis(Y), side = RIGHT_SIDE)
        addAxes Axis(Y, 1, "First Y Axis")
        addAxes Axis(X, title = "X Axis")
      )

    // this is just for illustration purposes, but we can also do the following
    val canvas = Canvas() setChart(0, 0, chart)

    assert(validateJson(chart.serialize.toString))
  }

  test("Axis.SetLimit") {
    val series = createXYZSeries(numberToCreate = 5, length = 3)
    val chart = (
      Chart(animated = true)
        setTitle "Animation 3D"
        addSeries series
        drawZAxisLog true
        setXAxisLimits(0, 1E4)
        setYAxisLimits(0, 1E4)
        setZAxisLimits(0, 1E4)
      )

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("Axis.SetLimit2") {
    val series = createXYZSeries(numberToCreate = 5, length = 3)
    val chart = (
      Chart(animated = true)
        setTitle "Animation 3D"
        addSeries series
        setXAxisLimits(0, 1E4)
        setYAxisLimits(0, 1E4)
        setZAxisLimits(0, 1E4)
        drawZAxisLog true
      )
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}