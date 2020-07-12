package picta

import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.options.{Axis, Layout}
import test.UnitTestUtils.{createXYSeries, createXYZSeries, validateJson}

class AnimationTests extends AnyFunSuite {

  val plotFlag = false

  test("Animation.XY") {
    val xaxis = Axis(position = "xaxis", title = "X Variable", range = (0.0, 10.0))
    val yaxis = Axis(position = "yaxis", title = "Y Variable", range = (0.0, 10.0))
    val layout = Layout("Animation.XY") setAxes(xaxis, yaxis)
    val data = createXYSeries(numberToCreate = 50, length = 30)
    val chart = Chart(animated = true) setLayout layout setData data
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }

  test("Animation.XYZ") {
    val data = createXYZSeries(numberToCreate = 3, length = 3)
    val layout = Layout("Animation.XYZ")
    val chart = Chart(animated = true) setLayout layout setData data
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString, "dynamic"))
  }
}
