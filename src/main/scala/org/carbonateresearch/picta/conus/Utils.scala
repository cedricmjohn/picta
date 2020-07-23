package org.carbonateresearch.picta.conus

import org.carbonateresearch.conus.ModelVariable
import org.carbonateresearch.conus.common.SingleModelResults
import org.carbonateresearch.picta.common.Serializer
import org.carbonateresearch.picta.{Series, XY}

import scala.collection.mutable.ListBuffer

/** An object containing various utility functions used to wrangle the Conus library. */
object Utils {
  /** This function grabs a single series specified from the user from a model that has been run.
   *
   * @param model_results: Results from a run of the Simlulator.
   * @param variable: The variable we wish to extract.
   * @param coordinate: The co-ordinate for which we wish to extract the variable.
   * @param number_of_steps: The number of time-steps we wish to extract the variable for.
   * @tparam T: Context-bounded by Serializer.
   * @return: A list containing the values for the variable at the specified coordinate for each time step.
   */
  def getDataFromSingleModel[T: Serializer]
  (model_results: SingleModelResults, variable: ModelVariable[T], coordinate: Seq[Int], number_of_steps: Int): List[T] = {
    val r = for {
      t <- 0 until number_of_steps
    } yield model_results.getStepResult(t, variable).getValueAtCell(coordinate)
    r.asInstanceOf[Seq[T]].toList
  }

  /** This function grabs a pair of series specified from the user from a model that has been run.
   *
   * @param model_results: Results from a run of the Simlulator.
   * @param xy: The x and y variables we wish to extract.
   * @param coordinate: The co-ordinate for which we wish to extract the variables.
   * @param number_of_steps: The number of time-steps we wish to extract the variables for.
   * @tparam T0: Context-bounded by Serializer.
   * @tparam T1: Context-bounded by Serializer.
   * @return: A tuple of lists containing the values for the variables at the specified coordinate for each time step.
   */
  def getXYSeriesFromSingleModel[T0: Serializer, T1: Serializer]
    (model_results: SingleModelResults, xy: (ModelVariable[T0], ModelVariable[T1]), coordinate: Seq[Int], number_of_steps: Int): Series = {

      val x = ListBuffer[T0]()
      val y = ListBuffer[T1]()

      for (t <- 0 until number_of_steps) {
        x += model_results.getStepResult(t, xy._1).getValueAtCell(coordinate).asInstanceOf[T0]
        y +=  model_results.getStepResult(t, xy._2).getValueAtCell(coordinate).asInstanceOf[T1]
      }

      XY(x.toList, y.toList)
    }
}