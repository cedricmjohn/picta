package conusviz

import org.carbonateresearch.conus.common.{ModelVariable, SingleModelResults}

// TODO - Need to add more utility functions
class Utils {
  def getSeriesSingleModel[T](results: SingleModelResults, variable: ModelVariable[T], coordinate: Seq[Int], n: Int): Seq[Any] = {
    for {
      i <- 0 until n
    } yield (results.getStepResult(i, variable).getValueAtCell(coordinate))
  }
}
