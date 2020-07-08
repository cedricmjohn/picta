package picta.options.animation

import picta.common.Component
import picta.common.Monoid.jsonMonoid
import picta.common.OptionWrapper._
import picta.options.Margin
import ujson.{Obj, Value}
import upickle.default._


case class CurrentValue(visible: Opt[Boolean] = Blank, prefix: Opt[String] = Blank, xanchor: Opt[String] = Blank) extends Component {
  override def serialize: Value = {
    val visible_ = visible.asOption match {
      case Some(x) => Obj("visible" -> x)
      case None => jsonMonoid.empty
    }

    val prefix_ = prefix.asOption match {
      case Some(x) => Obj("prefix" -> x)
      case None => jsonMonoid.empty
    }

    val xanchor_ = xanchor.asOption match {
      case Some(x) => Obj("xanchor" -> x)
      case None => jsonMonoid.empty
    }

    List(visible_, prefix_, xanchor_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}

case class SliderStep(key: String, method: String = "animate", label: String, args: Opt[Args] = Blank) extends Component {
  override def serialize: Value = {
    val meta = Obj("method" -> method, "label" -> label)

    val args_ = args.asOption match {
      case Some(x) => Obj("args" -> List(transform(List(key)).to(Value), x.serialize))
      case None => jsonMonoid.empty
    }
    List(meta, args_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}

case class Slider(pad: Opt[Margin] = Blank, currentvalue: Opt[CurrentValue] = Blank, steps: Opt[List[SliderStep]] = Empty) extends Component {

  def +(new_currentvalue: CurrentValue): Slider = this.copy(currentvalue = new_currentvalue)

  def +(new_steps: List[SliderStep]): Slider = this.copy(steps = new_steps)

  override def serialize: Value = {
    val pad_ = pad.asOption match {
      case Some(x) => Obj("pad" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val currentvalue_ = currentvalue.asOption match {
      case Some(x) => Obj("currentvalue" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val steps_ = steps.asOption match {
      case Some(x) => Obj("steps" -> x.map(slider_step => slider_step.serialize))
      case None => jsonMonoid.empty
    }

    List(pad_, currentvalue_, steps_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}
