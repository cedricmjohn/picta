package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils.{validateJson, x_int, y_int, z_double}
import org.carbonateresearch.picta._
import org.carbonateresearch.picta.options.Marker
import org.scalatest.funsuite.AnyFunSuite

class ScatterWithColorTests extends AnyFunSuite {

  val plotFlag = false

  test("ScatterWithColor.Basic") {
    val series = (
      XY(x_int, y_int)
      asType SCATTER
      drawStyle MARKERS
      setColor z_double
      setColorBar("My Scale", RIGHT_SIDE)
    )

    val chart = (
      Chart()
      showLegend false
      addSeries series
      setTitle "ScatterWithColor.Basic"
    )

    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString))
  }
}