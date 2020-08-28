/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import ujson.{Obj, Value}

/** Splits a chart over rows and columns. Used when comparing two series on the same chart, but different axes.
 *  This functionality is independent of the Canvas grid.
 *
 * @param rows: Number of rows in the multichart.
 * @param columns: Number of columns in the multichart.
 */
private[picta] final case class MultiChart(rows: Int = 1, columns: Int = 1) extends Component {
  private[picta] val pattern: String = "independent"

  private[picta] def serialize(): Value = Obj("rows" -> rows, "columns" -> columns, "pattern" -> pattern)
}