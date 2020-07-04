import picta.common.Monoid._


object Example extends App {

  val a = JsonMonoid.empty

  val x = a |+| a

  println(x)

}

