package conusviz

import org.carbonateresearch.conus.common.{ModelVariable, SingleModelResults}

// TODO - Need to add more utility functions
object Utils {
  def getSeriesSingleModel[T](results: SingleModelResults, variable: ModelVariable[T], coordinate: Seq[Int], n: Int): Seq[Any] = {
    for {
      i <- 0 until n
    } yield (results.getStepResult(i, variable).getValueAtCell(coordinate))
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
