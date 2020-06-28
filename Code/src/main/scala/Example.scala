import ujson.Obj

object Example extends App {
  // repetitive case classes A and B - Can they be abstracted to one case class?
  case class A(a: Int, b: String) {
    def f1(): Unit = println(a, b)
  }
  case class B(a: Int, b: String) {
    def f1(): Unit = println(a, b)
  }

  case class C(a: A, b: B) {
    def serialize() = Map("a" -> a, "b" -> b)
    def +(new_a: A): C = C(new_a, b)
    def +(new_b: B): C = C(a, new_b)
  }
}

//// if I use the same type for both A and B, I cannot use + individually anymore
//case class C_alt(a: A, b: A) {
//  def serialize() = Map("a" -> a, "b" -> b)
//  def +(new_a: A): C_alt = C_alt(new_a, b)
//  def +(new_b: A): C_alt = C_alt(a, new_b)
//}




//  // some dummy data
//  val col1 = Col(List("a", "b", "c", "d", "e"))
//  val col2 = Col(List(12, 200, 80900, 201200, 124420000))
//  val col3 = Col(List(121, 12121, 71240000, 44356, 845))
//
//  val header: Col[String] = Col(List("a1", "b1", "c1"))
//  val data = List(col1, col2, col3)
//
//  val table = Table(header, data)
//  table.plot()
//
