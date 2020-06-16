package conusviz.options

import ujson.{Obj, Value}
import upickle.default.{ReadWriter => RW, _}

sealed trait ConfigOptions {
  def createConfig: Value
}

object ConfigOptions {
  implicit def config_rw: RW[Config] = macroRW

  case class Config(responsive: Boolean, scrollZoom: Boolean) extends ConfigOptions {
    def createConfig(): Value = {
      val config = Obj("responsive" -> responsive, "scrollZoom" -> scrollZoom)
      transform(config).to(Value)
    }
  }
}
