import upickle.default._
import ujson.{Value, Null}


object Example extends App {

  println(transform(List(Null)).to(Value))

}

