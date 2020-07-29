package org.carbonateresearch.picta

import org.carbonateresearch.picta.UnitTestUtils.validateJson
import org.scalatest.funsuite.AnyFunSuite
import upickle.default.write

class AxisTests extends AnyFunSuite {

  val plotFlag = false

  test("Axis.Constructor") {
    val axis = XAxis(position = 2) setTitle "second x-axis" setDomain(0.85, 1.0)
    val test = """{"xaxis2":{"title":{"text":"second x-axis"},"showgrid":true,"zeroline":false,"showline":false,"domain":[0.85,1]}}"""
    assert(test == write(axis.serialize))
  }

  test("Axis.MultipleAxes") {
    val yaxis = YAxis() setTickDisplayFormat("0.3f")

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
    val yaxis = YAxis() setTickDisplayFormat("0.3f")

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
        setXAxisTitle("my new x axis")
      )

    if (plotFlag) chart.plot

    assert(validateJson(chart.serialize.toString))
  }

  test("Axis.SetTick") {
    val yaxis = YAxis() setTickDisplayFormat("0.3f")

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
        setXAxisTitle("my new x axis")
        setXAxisStartTick 50
        setXAxisTickGap 25
      )

    if (plotFlag) chart.plot

    assert(validateJson(chart.serialize.toString))
  }

  test("Axis.SetAxisLog") {
    val yaxis = YAxis() setTickDisplayFormat("0.3f")

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
    val yaxis = YAxis() setTickDisplayFormat("0.3f")

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
}