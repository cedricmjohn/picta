import ujson.{Null, Value}
import upickle.default._


object Example extends App {

  println(transform(List(Null)).to(Value))

}

