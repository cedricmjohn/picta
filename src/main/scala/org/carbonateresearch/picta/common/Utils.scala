package org.carbonateresearch.picta.common

import org.carbonateresearch.picta.options.ColorOptions.Color
import org.carbonateresearch.picta.{MARKERS, SCATTER, XY}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/** This object contains a number of general utility functions that are useful for both users and maintainers of the
 * library.
 */
object Utils {
  /** This is the alternative constructor for the alternative constructor above. As this function signature uses
   * lists as opposed to tuples, it is not good from a user interface perspective.
   *
   * @param categories : This are the per data-point category labels.
   * @param data       : This is a tuple of the raw data for both the x and y variables.
   * @tparam T  : Context-bounded by Serializer.
   * @tparam T1 : Context-bounded by Color.
   * @tparam T2 : Context-bounded by Color.
   * @return
   */
  def getSeriesbyCategory[T: Serializer, T1: Color, T2: Color]
  (categories: List[String], data: (List[T], List[T])): List[XY[T, T, T1, T2]] = {
    getSeriesbyCategory(categories, List(data._1, data._2))
  }

  /** This is the base constructor for the alternative constructor above. As this function signature uses lists as
   * opposed to tuples, it is not good from a user interface perspective.
   *
   * @param categories : This are the per data-point category labels.
   * @param data       : This is a tuple of the raw data for both the x and y variables.
   */
  private def getSeriesbyCategory[T: Serializer, T1: Color, T2: Color]
  (categories: List[String], data: List[List[T]]): List[XY[T, T, T1, T2]] = {
    /* generate some axis labels - this is just to keep track, the exact value is not important */
    val axis_labels = List(generateRandomText, generateRandomText)

    /* use the given list of categories, and convert them to a Set of individual labels */
    val category_labels = categories.toSet

    /* use a nested mutable map structure in order to increase efficiency of access; O(1) access time-complexity.
     * Mutable lists are used for memory-efficient in-place appends as otherwise immutable lists would require copy + update.
     */
    var data_map: mutable.Map[String, mutable.Map[String, ListBuffer[T]]] = collection.mutable.Map()

    /* for each series, create a nested map pointing to each category */
    data.zipWithIndex.foreach {
      case (_, index) => {
        var inner_map: mutable.Map[String, ListBuffer[T]] = collection.mutable.Map()

        /* now create a list for each of the different categories */
        category_labels.foreach(c => inner_map += c -> new ListBuffer[T]())

        /* add the name of the series to the nested map */
        data_map += axis_labels(index) -> inner_map
      }
    }

    /* at this stage the structure should be like (axis 1 -> (cat. 1 -> Nil, cat. 2 -> Nil,...), axis 2 -> ...) */
    var series_set = scala.collection.mutable.Set[XY[T, T, T1, T2]]()

    /* for each label, take the associated data from the series and stick into the correct place in the map */
    for ((label, i) <- categories.zipWithIndex) {
      for ((series, j) <- data.zipWithIndex) {
        data_map(axis_labels(j))(label) +:= series(i)
      }
    }

    /* since we have an efficient, easy to traverse structure, we can now create the individual series */
    category_labels.foreach(category => {
      val x = data_map(axis_labels(0))(category).toList
      val y = data_map(axis_labels(1))(category).toList
      series_set += XY(x = x, y = y, `type` = SCATTER, style = MARKERS, name = category)
    })
    series_set.toList
  }

  /** Generates a random alphanumeric string.
   *
   * @return: Random alphanumeric string of length 10.
   */
  private[picta] def generateRandomText(): String = scala.util.Random.alphanumeric.take(10).mkString

  /** A utility function that creates a list of all the possible co-ordinates inside a grid.
   *
   * @param dimx : The x-dimension of a grid.
   * @param dimy : The y-dimension of a grid.
   * @return: A list of x-y co-ordinates for the grid.
   */
  def createCoordinateRange(dimx: Int, dimy: Int): List[Seq[Int]] = {
    for {
      x <- List.range(0, dimx)
      y <- List.range(0, dimy)
    } yield Seq(x, y)
  }
}
