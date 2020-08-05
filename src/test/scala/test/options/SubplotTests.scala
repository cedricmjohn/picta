package org.carbonateresearch.picta

import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.options.MultiChart
import org.scalatest.funsuite.AnyFunSuite

case class SubplotTests() extends AnyFunSuite {
  val plotFlag = false

  test("Subplot.Grid") {
    val ax1 = Axis(X) setTitle "x axis 1"
    val ax2 = Axis(X, 2) setTitle "x axis 2"

    val series1 = XY(x = List(1, 2, 3), y = List(2, 4, 5)) asType SCATTER drawStyle MARKERS
    val series2 = XY(x = List(10, 12, 13), y = List(20, 31)) asType SCATTER drawStyle MARKERS setAxes(Axis(X, 2), Axis(Y, 2))

    val layout = ChartLayout(title = "XY.Axis.Composition") setAxes(ax1, ax2) setMultiChart MultiChart(1, 2)

    val chart1 = Chart() addSeries(series1, series2) setChartLayout layout setConfig(false, false)
    val chart2 = Chart() addSeries(series2, series1) setChartLayout layout setConfig(false, false)

    val canvas = Canvas(1, 2) addCharts(chart1, chart2)

    if (plotFlag) canvas.plot

    assert(validateJson(chart1.serialize.toString))
    assert(validateJson(chart2.serialize.toString))
  }

  test("Subplot.Multiple") {
    val ax1 = Axis(X, title = "x axis 1")
    val ax2 = Axis(X, title = "x axis 2")
    val ax3 = Axis(X, title = "x axis 3")
    val ax4 = Axis(X, title = "x axis 4")

    val ax6 = Axis(Y, title = "y axis 1")
    val ax7 = Axis(Y, title = "y axis 2")
    val ax8 = Axis(Y, title = "y axis 3")
    val ax9 = Axis(Y, title = "y axis 4")

    val chart1 = Chart() addSeries XY(x_double, y_double).setName("a") addAxes(ax1, ax6)
    val chart2 = Chart() addSeries XY(y_int, y_double).setName("b") addAxes(ax2, ax7)
    val chart3 = Chart() addSeries XY(x_int, y_double).setName("c") addAxes(ax3, ax8)
    val chart4 = Chart() addSeries XY(z_int, z_double).setName("d") addAxes(ax4, ax9)

    val canvas =
      Canvas(2, 2)
        .setChart(0, 1, chart1)
        .setChart(1, 1, chart2)
        .setChart(0, 0, chart3)
        .setChart(1, 0, chart4)

    if (plotFlag) canvas.plot

    assert(validateJson(chart1.serialize.toString))
    assert(validateJson(chart2.serialize.toString))
    assert(validateJson(chart3.serialize.toString))
    assert(validateJson(chart4.serialize.toString))
  }

  test("Subplot.Margins") {
    val ax1 = Axis(X, title = "x axis 1")
    val ax2 = Axis(X, title = "x axis 2")
    val ax3 = Axis(X, title = "x axis 3")

    val ax6 = Axis(Y, title = "y axis 1")
    val ax7 = Axis(Y, title = "y axis 2")
    val ax8 = Axis(Y, title = "y axis 3")

    // it may be necessary to play around with the chart dimensions and margin  in order to ensure a good fit on screen.
    val dim = 500

    val chart1 = (
      Chart()
        setDimensions(width = dim, height = dim)
        addSeries XY(x_double, y_double).setName("a")
        addAxes(ax1, ax6)
        setMargin(l = 50, r = 30, t = 50, b = 50)
      )

    val chart2 = (
      Chart()
        setDimensions(width = dim, height = dim)
        addSeries XY(x_double, y_double).setName("b")
        addAxes(ax2, ax7)
        setMargin(l = 50, r = 50, t = 50, b = 50)
      )

    val chart3 = (
      Chart()
        setDimensions(width = dim, height = dim)
        addSeries XY(x_double, y_double).setName("c")
        addAxes(ax3, ax8)
        setMargin(l = 50, r = 30, t = 50, b = 50)
      )

    val chart4 = (
      Chart()
        setDimensions(width = dim, height = dim)
        addSeries XY(x_double, y_double).setName("d")
        setTitle "A generic title"
        addAxes(ax1, ax6, ax2, ax7) // these axes come from above
        setConfig(false, false)
      )

    // The canvas has an underlying grid. By default the underlying grid is 1x1, but we can pass in the dimensions we
    // require by passing in parameters in the constructor.
    val canvas = Canvas(2, 2)
      .setChart(0, 0, chart1)
      .setChart(0, 1, chart2)
      .setChart(1, 0, chart3)
      .setChart(1, 1, chart4)

    if (plotFlag) canvas.plot

    assert(validateJson(chart1.serialize.toString))
    assert(validateJson(chart2.serialize.toString))
    assert(validateJson(chart3.serialize.toString))
    assert(validateJson(chart4.serialize.toString))
  }
}