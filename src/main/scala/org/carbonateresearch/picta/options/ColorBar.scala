/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.OptionWrapper.{Blank, Opt}
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.{Component, Side}
import ujson.{Obj, Value}

/**
 * This case class represents a color bar for a plot.
 *
 * @param title: The title of the color bar.
 * @param title_side: The side on which the title should should be displayed.
 */
case class ColorBar(title: Opt[String] = Blank, title_side: Opt[Side] = Blank) extends Component {

  def setTitle(new_title: String) = this.copy(title = new_title)

  def setTitleSide(new_side: Side) = this.copy(title_side = new_side)

  def serialize: Value = {
    val title_text = title.option match {
      case Some(x) => Obj("text" -> x)
      case _ => jsonMonoid.empty
    }

    val title_side_ = title_side.option match {
      case Some(x) if x.toString.toLowerCase != "left" => Obj("side" -> x.toString.toLowerCase)
      case _ => jsonMonoid.empty
    }

    val title_ = Obj("title" -> (title_text.obj ++ title_side_.obj))

    List(title_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}