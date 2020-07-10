package picta.common

import picta.Serializer
import picta.series.ModeType.MARKERS
import picta.series.XYChartType.SCATTER
import picta.series.{XY, XYSeries}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

// TODO - Need to add more utility functions - can be done after integrating with CoNuS core.
object Utils {
  private def getSeriesbyCategory[T: Serializer](categories: List[String], data: List[List[T]]): List[XYSeries] = {
    /** generate some axis labels - this is just to keep track of later on, the exact value is not important */
    val axis_labels = List(genRandomText, genRandomText)
    /** we use the given list of labels, and convert them to a set of categories */
    val category_labels = categories.toSet
    /** we use a nested mutable map structure in order to increase efficiency of access
     *  mutable lists are used for memory-efficient in-place appends
     */
    var data_map: mutable.Map[String, mutable.Map[String, ListBuffer[T]]] = collection.mutable.Map()
    /** for each series, create a nested map pointing to each category */
    data.zipWithIndex.foreach{ case (_, index) => {
      var inner_map: mutable.Map[String, ListBuffer[T]] = collection.mutable.Map()
      /** now create a list for each of the different categories */
      category_labels.foreach(c => inner_map += c -> new ListBuffer[T]())
      /** add the name of the series to the nested map */
      data_map += axis_labels(index) -> inner_map
    }}
    /** at this stage the structure should be like (axis1 -> (cat1 -> Nil, cat2 -> Nil,...), axis2 -> ...) */
    var series_set = scala.collection.mutable.Set[XYSeries]()
    /** we iterate each label, take the associated data from the series, and stick into the correct place in the map */
    for ((label, i) <- categories.zipWithIndex) {
      for ((series, j) <- data.zipWithIndex) {
        data_map(axis_labels(j))(label) +:= series(i)
      }
    }
    /** since we have an efficient, easy to traverse structure, we can now create the individual series */
    category_labels.foreach(category => {
      val x = data_map(axis_labels(0))(category).toList
      val y = data_map(axis_labels(1))(category).toList
      series_set += XY(x=x, y=y, series_type = SCATTER, series_mode = MARKERS, series_name = category)
    })
    series_set.toList
  }

  def getSeriesbyCategory[T: Serializer](categories: List[String], data: (List[T], List[T])): List[XYSeries] = {
    getSeriesbyCategory(categories, List(data._1, data._2))
  }

  /*
  * Creates a list of all the possible co-ordinates
  * */
  def createCoordinateRange(dimx: Int, dimy: Int): List[Seq[Int]] = {
    for {
      x <- List.range(0, dimx)
      y <- List.range(0, dimy)
    } yield Seq(x, y)
  }

  //  def getSeriesFromSingleModel[T: Serializer]
  //  (model: SingleModelResults, variable: ModelVariable[T], coordinate: Seq[Int], n: Int): List[T] = {
  //    val r = for {
  //      t <- 0 until n
  //    } yield model.getStepResult(t, variable).getValueAtCell(coordinate)
  //
  //    r.asInstanceOf[Seq[T]].toList
  //  }

  // println(results.getModelVariablesForStep(i))

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

  /** Generates a random 7 digit alphanumeric string */
  def genRandomText(): String = java.util.UUID.randomUUID.toString().slice(0, 7)
}
