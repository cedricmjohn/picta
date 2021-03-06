/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import ujson.{Obj, Value}

/**
 * This case class specifies margins (in px). Margins can be used by a number of different components.
 *
 * @param l: left margin.
 * @param r: right margin.
 * @param t: top margin.
 * @param b: bottom margin.
 */
final case class Margin(l: Opt[Int] = Blank, r: Opt[Int] = Blank, t: Opt[Int] = Blank, b: Opt[Int] = Blank) extends Component {
  private[picta] def serialize(): Value = {
    val l_ = l.option match {
      case Some(x) => Obj("l" -> x)
      case None => jsonMonoid.empty
    }

    val r_ = r.option match {
      case Some(x) => Obj("r" -> x)
      case None => jsonMonoid.empty
    }

    val t_ = t.option match {
      case Some(x) => Obj("t" -> x)
      case None => jsonMonoid.empty
    }

    val b_ = b.option match {
      case Some(x) => Obj("b" -> x)
      case None => jsonMonoid.empty
    }

    List(l_, r_, t_, b_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}