package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.UnitTestUtils.validateJson
import org.carbonateresearch.picta.options._
import org.scalatest.funsuite.AnyFunSuite

/**
 * This tests the Error Bar component, and it's various methods.
 */
class ErrorBarTests extends AnyFunSuite {

  val plotFlag = false

  test("ErrorBar.Data") {
    val series = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setErrorBars YError(mode = DATA, array = List(0.5, 0.5, 0.5))
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "ErrorBar.Data"
      )

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("ErrorBar.Percent") {
    val series = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setErrorBars YError(mode = PERCENT, value = 10.0)
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "ErrorBar.Percent"
      )

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("ErrorBar.Constant") {
    val series = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setErrorBars YError(mode = CONSTANT, value = 10.0)
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "ErrorBar.Constant"
      )

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("ErrorBar.Sqrt") {
    val series = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setErrorBars YError(mode = SQRT)
      )

    val chart = (
      Chart()
        addSeries series
        setTitle "ErrorBar.Sqrt"
      )

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("ErrorBar.Canvas") {
    val dim = 400

    val series1 = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setErrorBars YError(mode = DATA, array = List(0.5, 0.5, 0.5))
      )

    val chart1 = (
      Chart()
        addSeries series1
        setTitle "Per Point Specified Error"
        setDimensions(width = dim, height = dim)
      )


    val series2 = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setErrorBars YError(mode = PERCENT, value = 10.0)
      )

    val chart2 = (
      Chart()
        addSeries series2
        setTitle "Percentage Error"
        setDimensions(width = dim, height = dim)
      )


    val series3 = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setErrorBars YError(mode = CONSTANT, value = 10.0)
      )

    val chart3 = (
      Chart()
        addSeries series3
        setTitle "Constant Error"
        setDimensions(width = dim, height = dim)
      )


    val series4 = (
      XY(List(1, 2, 3), List(1.234, 5.2112, 2.44332))
        asType SCATTER
        drawStyle MARKERS
        setErrorBars YError(mode = SQRT)
      )

    val chart4 = (
      Chart()
        addSeries series4
        setTitle "Sqrt Error"
        setDimensions(width = dim, height = dim)
      )

    val canvas = Canvas(2, 2) addCharts(chart1, chart2, chart3, chart4)

    if (plotFlag) canvas.plot

    assert(validateJson(chart1.serialize.toString))
    assert(validateJson(chart2.serialize.toString))
    assert(validateJson(chart3.serialize.toString))
    assert(validateJson(chart4.serialize.toString))
  }


}
