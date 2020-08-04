package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.options.{RIGHT => _, _}
import org.carbonateresearch.picta.{MARKERS, Map, SCATTER, _}
import org.scalatest.funsuite.AnyFunSuite

class ChartMethodTests extends AnyFunSuite {

  val plotFlag = false

  test("Chart.SetTitle") {
    val series = XY(x_int, y_int) asType SCATTER drawStyle MARKERS
    val chart = Chart() addSeries series setTitle "Chart.SetTitle"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("Chart.Axes") {
    val series1 = XY(x_int, y_int) asType SCATTER drawStyle MARKERS
    val series2 = XY(x_int, y_double) asType SCATTER drawStyle MARKERS
    val series3 = XY(x_int, z_int) asType SCATTER drawStyle MARKERS setAxis Axis(Y, 2)

    val chart = (
      Chart()
        setTitle "Chart.Axes"
        addSeries(series1, series2, series3)
        addAxes Axis(Y,position = 2, title = "second y axis", overlaying = Axis(Y), side = RIGHT_SIDE)
      )

    val canvas = Canvas() setChart(0, 0, chart)
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("Chart.ShowLegend") {
    val series1 = XY(x_int, y_int) asType SCATTER drawStyle MARKERS setName "First Series"
    val series2 = XY(y_int, x_int) asType SCATTER drawStyle MARKERS setName "Second Series"
    val chart = Chart() addSeries(series1, series2) setTitle "Chart.ShowLegend" showLegend true
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("Chart.setLegend") {
    val series1 = XY(x_int, y_int) asType SCATTER drawStyle MARKERS setName "First Series"
    val series2 = XY(y_int, x_int) asType SCATTER drawStyle MARKERS setName "Second Series"
    val chart = Chart()
      .addSeries(series1, series2)
      .setTitle("Chart.setLegend")
      .showLegend(true)
      .setLegend(orientation = VERTICAL)

    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("Chart.Axis.Composition") {
    // create the axes
    val xaxis1 = Axis(X) setTitle "x axis 1"
    val yaxis1 = Axis(Y)

    val xaxis2 = Axis(X, 2) setTitle "x axis 2"
    val yaxis2 = Axis(Y,2)

    val series1 = (
      XY(x = List(1, 2, 3), y = List(2, 4, 5))
        asType SCATTER
        drawStyle MARKERS
      )

    val series2 = (
      XY(x = x_double, y = y_double)
        .asType(SCATTER)
        .drawStyle(MARKERS)
        .setAxes(xaxis2, yaxis2)
      )

    val chart1 = Chart()
      .addSeries(series1, series2)
      .setTitle("Chart.Axis.Composition")
      .addAxes(xaxis1, yaxis1, xaxis2, yaxis2)
      .setConfig(false, false)
      .asMultiChart(1, 2)

    val canvas = Canvas() addCharts chart1

    if (plotFlag) canvas.plot

    assert(validateJson(chart1.serialize.toString))
  }

  test("Chart.SetGeo") {
    val color = List("red")
    val line = Line(width = 2) setColor color
    val series = Map(List(40.7127, 51.5072), List(-74.0059, 0.1275)) drawSymbol LINES drawLine line

    val geo = MapOptions(landcolor = "rgb(204, 204, 204)", lakecolor = "rgb(255, 255, 255)")
      .setMapAxes(LatAxis(List(20, 60)), LongAxis(List(-100, 20)))

    val chart =
      Chart()
        .addSeries(series)
        .setConfig(false, false)
        .setMapOptions(geo)
        .setMargin(0, 0, 100, 0)
        .setDimensions(height = 400, width = 400)
        .setTitle("Chart.SetGeo")

    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("Chart.XYZ.SetAxesTitle") {
    val series = XYZ(x_double, y_double, z_double) asType SCATTER3D drawStyle MARKERS setName "XYZ series"

    val xaxis = Axis(X, title="my x axis")
    val yaxis = Axis(Y, title="my y axis")
    val zaxis = Axis(Z, title="my z axis")

    val chart = (
      Chart()
        addSeries series
        setTitle "XYZ.SetAxesTitle"
        setConfig(false, false)
        addAxes(xaxis, yaxis, zaxis)
      )

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("Chart.XYZ.SetAxesRange") {
    val series = XYZ(x_double, y_double, z_double) asType SCATTER3D drawStyle MARKERS setName "XYZ series"

    val xaxis = Axis(X, title="my x axis")
    val yaxis = Axis(Y, title="my y axis")
    val zaxis = Axis(Z, title="my z axis")

    val chart = (
      Chart()
        addSeries series
        setTitle "XYZ.SetAxesTitle"
        setConfig(false, false)
        addAxes(xaxis, yaxis, zaxis)
        setZAxisTitle "setting Z-axis title"
        setZAxisLimits(0, 50)
        setZAxisTickGap 25
      )

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}