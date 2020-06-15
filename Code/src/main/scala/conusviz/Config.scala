package conusviz

import upickle.default._
import ujson.{Obj, Value}
import upickle.default.{ReadWriter => RW}

sealed trait ConfigOption {
  def createConfig: Value
}

object ConfigOption {
  implicit def config_rw: RW[Config] = macroRW

  case class Config(responsive: Boolean, scrollZoom: Boolean) extends ConfigOption {
    def createConfig(): Value = {
      val config = Obj("responsive" -> responsive, "scrollZoom" -> scrollZoom)
      transform(config).to(Value)
    }
  }
}
