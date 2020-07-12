package picta.common

import picta.series.Mode.MARKERS
import picta.series.XYChart.SCATTER
import picta.series.{XY, XYSeries}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/** This object contains a number of general utility functions that are useful for both users and the library.
 */
object Utils {
  /** This is an alternative constructor that uses tuples instead of lists to limit users entering more than two data series
   *
   * @param categories : This are the per data-point category labels
   * @param data       : This is a tuple of the raw data for both the x and y variables
   */
  def getSeriesbyCategory[T: Serializer](categories: List[String], data: (List[T], List[T])): List[XYSeries] = {
    getSeriesbyCategory(categories, List(data._1, data._2))
  }

  /** This is the base constructor for the alternative constructor above. As this function signature uses lists as opposed to
   * tuples, it is not good from a user interface perspective.
   *
   * @param categories : This are the per data-point category labels
   * @param data       : This is a tuple of the raw data for both the x and y variables
   */
  private def getSeriesbyCategory[T: Serializer](categories: List[String], data: List[List[T]]): List[XYSeries] = {
    /** generate some axis labels - this is just to keep track, the exact value is not important */
    val axis_labels = List(genRandomText, genRandomText)

    /** use the given list of categories, and convert them to a Set of individual labels */
    val category_labels = categories.toSet

    /** use a nested mutable map structure in order to increase efficiency of access; O(1) access time-complexity.
     * Mutable lists are used for memory-efficient in-place appends as otherwise immutable lists would require copy + update.
     */
    var data_map: mutable.Map[String, mutable.Map[String, ListBuffer[T]]] = collection.mutable.Map()

    /** for each series, create a nested map pointing to each category */
    data.zipWithIndex.foreach {
      case (_, index) => {
        var inner_map: mutable.Map[String, ListBuffer[T]] = collection.mutable.Map()

        /** now create a list for each of the different categories */
        category_labels.foreach(c => inner_map += c -> new ListBuffer[T]())

        /** add the name of the series to the nested map */
        data_map += axis_labels(index) -> inner_map
      }
    }

    /** at this stage the structure should be like (axis 1 -> (cat. 1 -> Nil, cat. 2 -> Nil,...), axis 2 -> ...) */
    var series_set = scala.collection.mutable.Set[XYSeries]()

    /** for each label, take the associated data from the series and stick into the correct place in the map */
    for ((label, i) <- categories.zipWithIndex) {
      for ((series, j) <- data.zipWithIndex) {
        data_map(axis_labels(j))(label) +:= series(i)
      }
    }

    /** since we have an efficient, easy to traverse structure, we can now create the individual series */
    category_labels.foreach(category => {
      val x = data_map(axis_labels(0))(category).toList
      val y = data_map(axis_labels(1))(category).toList
      series_set += XY(x = x, y = y, series_type = SCATTER, series_mode = MARKERS, series_name = category)
    })
    series_set.toList
  }

  /** Generates a random 7 digit alphanumeric string. Example use is creating a random, non-conflicting series name */
  private[picta] def genRandomText(): String = java.util.UUID.randomUUID.toString().slice(0, 7)


  //  def getSeriesFromSingleModel[T: Serializer]
  //  (model: SingleModelResults, variable: ModelVariable[T], coordinate: Seq[Int], n: Int): List[T] = {
  //    val r = for {
  //      t <- 0 until n
  //    } yield model.getStepResult(t, variable).getValueAtCell(coordinate)
  //
  //    r.asInstanceOf[Seq[T]].toList
  //  }

  // println(results.getModelVariablesForStep(i))

  /**
   * Creates a list of all the possible co-ordinates
   */
  def createCoordinateRange(dimx: Int, dimy: Int): List[Seq[Int]] = {
    for {
      x <- List.range(0, dimx)
      y <- List.range(0, dimy)
    } yield Seq(x, y)
  }

  // traverse and update a Value object -  assumes no repeated keys - not a pure function as it modifies in place
  def update(v: ujson.Value, parent: String, child: String, z: ujson.Value): Boolean = v match {
    case a: ujson.Arr =>
      a.arr.foreach(e => update(e, parent, child, z))
      true
    case o: ujson.Obj =>
      o.obj.foreachEntry((key, value) => {
        if ((key == parent) && (value.obj.keySet contains child)) value.obj(child) = z
        else update(value, parent, child, z)
      })
      true
    case _ => true
  }
}
