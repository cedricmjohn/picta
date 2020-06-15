package conusviz

import org.carbonateresearch.conus.common.{ModelVariable, SingleModelResults}

class Utils {
  // grabs the series for a single model and converts it to a sequence that can create traces
  def getSeriesSingleModel[T](results: SingleModelResults, variable: ModelVariable[T], coordinate: Seq[Int], n: Int): Seq[Any] = {
    for {
      i <- 0 until n
    } yield (results.getStepResult(i, variable).getValueAtCell(coordinate))
  }
}
