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
    val yaxis = YAxis() setTickFormat("0.3f")

    val series = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setAxis yaxis
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "XY.Scatter.Int"
        addAxes yaxis
      )

    if (plotFlag) chart.plot

    assert(validateJson(chart.serialize.toString))
  }
}
