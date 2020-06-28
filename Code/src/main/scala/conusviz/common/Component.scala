package conusviz.common
import ujson.Value

/*
* root trait for every graphic component
* */
trait Component {
  def serialize: Value
}
