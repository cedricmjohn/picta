package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta._
import org.carbonateresearch.picta.options._

import org.scalatest.funsuite.AnyFunSuite

class CanvasTests extends AnyFunSuite {

  val plotFlag = false

  test("XYZ.Scatter3D") {

    val x = List(-9, -6, -5 , -3, -1)
    val y = List(0, 1, 4, 5, 7)
    val z = List(
      List(10, 10.625, 12.5, 15.625, 20),
      List(5.625, 6.25, 8.125, 11.25, 15.625),
      List(2.5, 3.125, 5.0, 8.125, 12.5),
      List(0.625, 1.25, 3.125, 6.25, 10.625),
      List(0, 0.625, 2.5, 5.625, 10)
    )

    // we flatten the nested list as we pass it into the Series constructor
    val contour = XYZ(x=x, y=y, z=z.flatten, n=z(0).length).asType(CONTOUR)

    // set up the chart
    val chart1 = Chart()
      .addSeries(contour)
      .setTitle("Contour")

    val heatmap = XYZ(z=z.flatten, n=z(0).length) asType HEATMAP

    val chart2 = Chart() addSeries heatmap setTitle "Heatmap"

    val surface = XYZ(x=x, y=y, z=z.flatten, n=z(0).length) asType SURFACE

    val chart3 = Chart() addSeries surface setTitle "Surface"

    val x2 = List.range(1, 100)
    val y2 = List.range(1, 100)
    val z2 = List.range(1, 100).map(e => e + scala.util.Random.nextDouble()*100)

    val line = XYZ(x=x2, y=y2, z=z2) asType SCATTER3D drawStyle LINES

    val chart4 = Chart() addSeries line setTitle "Line"

    val dim = 350

    // The canvas has an underlying grid. By default the underlying grid is 1x1, but we can pass in the dimensions we
    // require by passing in parameters in the constructor.
    val canvas = Canvas(2, 2)
      .setChart(0, 0, chart3.setDimensions(width = dim, height = dim))
      .setChart(0, 1, chart4.setDimensions(width = dim, height = dim))
      .setChart(1, 0, chart1.setDimensions(width = dim, height = dim))
      .setChart(1, 1, chart2.setDimensions(width = dim, height = dim))

    if (plotFlag) canvas.plot

    assert(validateJson(chart1.serialize.toString))
    assert(validateJson(chart2.serialize.toString))
    assert(validateJson(chart3.serialize.toString))
    assert(validateJson(chart4.serialize.toString))
  }
}
