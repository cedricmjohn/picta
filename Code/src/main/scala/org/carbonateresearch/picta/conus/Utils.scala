package org.carbonateresearch.picta.conus

import org.carbonateresearch.conus.ModelVariable
import org.carbonateresearch.conus.common.SingleModelResults
import org.carbonateresearch.picta.{Series, XY}
import org.carbonateresearch.picta.common.Serializer

import scala.collection.mutable.ListBuffer

object Utils {

  def getDataFromSingleModel[T: Serializer]
  (model: SingleModelResults, variable: ModelVariable[T], coordinate: Seq[Int], n: Int): List[T] = {
    val r = for {
      t <- 0 until n
    } yield model.getStepResult(t, variable).getValueAtCell(coordinate)
    r.asInstanceOf[Seq[T]].toList
  }

  def getXYSeriesFromSingleModel[T0: Serializer, T1: Serializer]
    (model: SingleModelResults, xy: (ModelVariable[T0], ModelVariable[T1]), coordinate: Seq[Int], n: Int): Series = {

      val x = ListBuffer[T0]()
      val y = ListBuffer[T1]()

      for (t <- 0 until n) {
        x += model.getStepResult(t, xy._1).getValueAtCell(coordinate).asInstanceOf[T0]
        y +=  model.getStepResult(t, xy._2).getValueAtCell(coordinate).asInstanceOf[T1]
      }

      XY(x.toList, y.toList)
    }
  }