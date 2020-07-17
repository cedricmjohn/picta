package org.carbonateresearch.picta

import org.carbonateresearch.picta.common.Utils.genRandomText

import ujson.{Obj, Value}
import upickle.default._

final case class Chart
(data: List[Series] = Nil, layout: Layout = Layout(), config: Config = Config(),
 animated: Boolean = false, transition_duration: Int = 100, private[picta] val id: String = genRandomText()) extends Component {

  private val frames_labels = animated match {
    case false => (Nil, Nil)
    case true => createFramesAndLabels(data)
  }

  private[picta] val frames = transform(frames_labels._1).to(Value)

  private[picta] val labels = frames_labels._2

  private[picta] val data_ : List[Value] = data.map(t => t.serialize)

  private[picta] val layout_ : Value = layout.serialize

  private[picta] val config_ : Value = config.serialize

  def addSeries(new_series: List[Series]): Chart = this.copy(data = new_series ::: data)

  def addSeries(new_series: Series*): Chart = this.copy(data = new_series.toList ::: data)

  def setLayout[Z0, Z1, Z2, Z3](new_layout: Layout): Chart= this.copy(layout = new_layout)

  def setConfig(new_config: Config): Chart = this.copy(config = new_config)

  private[picta] def serialize: Value = Obj("traces" -> data_, "layout" -> layout_, "config" -> config_)

  private def createFramesAndLabels(lst: List[Series], frames: List[Obj] = Nil, labels: List[String] = Nil): (List[Obj], List[String]) = {
    lst match {
      case Nil => (frames, labels)
      case hd :: tl => createFramesAndLabels(tl, frames :+ Obj("name" -> hd.name, "data" -> List(hd.serialize)), labels :+ hd.name)
    }
  }
}