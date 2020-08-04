package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils.{createXYSeries, createXYZSeries, validateJson}
import org.carbonateresearch.picta.{Axis, Chart, ChartLayout, X, Y}
import org.scalatest.funsuite.AnyFunSuite

class AnimationTests extends AnyFunSuite {

  val plotFlag = false

  test("Animation.XY") {
    val xaxis = Axis(X, title = "X Variable") setLimits (0.0, 1E4)
    val yaxis = Axis(Y, title = "Y Variable") setLimits (0.0, 1E4)
    val series = createXYSeries(numberToCreate = 5, length = 3)
    val chart = Chart(animated = true) setTitle "Animation.XY" addSeries series addAxes(xaxis, yaxis)
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }

  test("Animation.XYZ") {
    val series = createXYZSeries(numberToCreate = 5, length = 3)
    val chart = (
      Chart(animated = true)
      setTitle "Animation.XYZ"
      addSeries series
      setXAxisLimits(0, 10000)
      setYAxisLimits(0, 10000)
      setZAxisLimits(0, 10000)
    )
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }

  test("Animation.Multiple.XY") {
    val series1 = createXYSeries(numberToCreate = 5, length = 3).map(x => x setName "series1")
    val series2 = createXYSeries(numberToCreate = 5, length = 3).map(x => x setName "series2")

    val chart = (
      Chart(animated = true, animate_multiple_series = true)
        setTitle "Animation.Multiple.XY"
        addSeries series1
        addSeries series2
        setXAxisLimits(0, 10000)
        setYAxisLimits(0, 10000)
        setZAxisLimits(0, 10000)
      )

    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }

  test("Animation.Multiple.XYZ") {
    val series1 = createXYZSeries(numberToCreate = 5, length = 3).map(x => x setName "series1")
    val series2 = createXYZSeries(numberToCreate = 5, length = 3).map(x => x setName "series2")

    val chart = (
      Chart(animated = true, animate_multiple_series = true)
        setTitle "Animation.Multiple.XYZ"
        addSeries series1
        addSeries series2
        setXAxisLimits(0, 10000)
        setYAxisLimits(0, 10000)
        setZAxisLimits(0, 10000)
      )

    if (plotFlag) chart.plot()
//    assert(validateJson(chart.serialize.toString, "dynamic"))
  }
}