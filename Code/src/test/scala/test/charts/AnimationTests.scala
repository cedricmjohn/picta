package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta.UnitTestUtils.{createXYSeries, createXYZSeries, validateJson}
import org.carbonateresearch.picta.options.{XAxis, YAxis}
import org.carbonateresearch.picta.{Canvas, Chart, Layout}
import org.scalatest.funsuite.AnyFunSuite

class AnimationTests extends AnyFunSuite {

  val plotFlag = false

  test("Animation.XY") {
    val xaxis = XAxis(title = "X Variable") setRange (0.0, 10.0)
    val yaxis = YAxis(title = "Y Variable") setRange (0.0, 10.0)
    val layout = Layout("Animation.XY") setAxes(xaxis, yaxis)
    val series = createXYSeries(numberToCreate = 50, length = 30)
    val chart = Chart(animated = true) setLayout layout addSeries series
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }

  test("Animation.XYZ") {
    val series = createXYZSeries(numberToCreate = 3, length = 3)
    val layout = Layout("Animation.XYZ")
    val chart = Chart(animated = true) setLayout layout addSeries series
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }
}