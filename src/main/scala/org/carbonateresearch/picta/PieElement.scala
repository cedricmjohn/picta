package org.carbonateresearch.picta
import ujson.{Obj}

final case class PieElement(value: Double, name: String) extends Component {
  private[picta] def serialize = Obj("name" -> value)
}