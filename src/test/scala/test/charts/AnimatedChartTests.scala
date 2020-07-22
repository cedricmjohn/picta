package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils.{createXYSeries, createXYZSeries, validateJson}
import org.carbonateresearch.picta.{Chart, ChartLayout, XAxis, YAxis}
import org.scalatest.funsuite.AnyFunSuite

class AnimationTests extends AnyFunSuite {

  val plotFlag = false

  test("Animation.XY") {
    val xaxis = XAxis(title = "X Variable") setRange (0.0, 10.0)
    val yaxis = YAxis(title = "Y Variable") setRange (0.0, 10.0)
    val series = createXYSeries(numberToCreate = 50, length = 30)
    val chart = Chart(animated = true) setTitle "Animation.XY" addSeries series addAxes(xaxis, yaxis)
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }

  test("Animation.XYZ") {
    val series = createXYZSeries(numberToCreate = 3, length = 3)
    val layout = ChartLayout()
    val chart = Chart(animated = true) setTitle "Animation.XYZ"  addSeries series
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }
}