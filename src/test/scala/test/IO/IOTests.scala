/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta

import org.carbonateresearch.picta.IO.{getWorkingDirectory, readCSV}
import org.carbonateresearch.picta.common.Utils.getSeriesbyCategory
import org.scalatest.funsuite.AnyFunSuite

/**
 * This tests the IO functions.
 */
class IOTests extends AnyFunSuite {

  val plotFlag = false

  test("Iris.2DCategory") {
    val filepath = getWorkingDirectory + "/src/test/resources/iris_csv.csv"

    val data = readCSV(filepath)

    val sepal_length = data("sepallength").map(_.toDouble)
    val petal_width = data("petalwidth").map(_.toDouble)
    val categories = data("class")

    val result = getSeriesbyCategory(categories, (sepal_length, petal_width))

    val chart = Chart() addSeries result setTitle "Iris.2DCategory" showLegend true
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot()
  }
}
