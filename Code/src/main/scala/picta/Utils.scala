package picta

// TODO - Need to add more utility functions - can be done after integrating with CoNuS core.
object Utils {

  /*
  * Returns an empty json object, serialized as a Value type
  * */
  //  def emptyObject(): Value = ujson.read("{}")

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
