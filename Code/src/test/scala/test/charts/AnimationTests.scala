package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils.{createXYSeries, createXYZSeries, validateJson}
import org.carbonateresearch.picta.options.{Axis, XAxis, YAxis}
import org.carbonateresearch.picta.{Chart, Layout}
import org.scalatest.funsuite.AnyFunSuite

class AnimationTests extends AnyFunSuite {

  val plotFlag = false

  test("Animation.XY") {
    val xaxis = XAxis(title = "X Variable", range = (0.0, 10.0))
    val yaxis = YAxis(title = "Y Variable", range = (0.0, 10.0))
    val layout = Layout("Animation.XY") setAxes(xaxis, yaxis)
    val data = createXYSeries(numberToCreate = 50, length = 30)
    val chart = Chart(animated = true) setLayout layout addSeries data
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }

  test("Animation.XYZ") {
    val data = createXYZSeries(numberToCreate = 3, length = 3)
    val layout = picta.Layout("Animation.XYZ")
    val chart = Chart(animated = true) setLayout layout addSeries data
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }
}