package picta.common


object Opt {

  case class Opt[A](asOption: Option[A])

  def Blank[A]: Opt[A] = Opt[A](None)

  implicit def fromValue[A](x: A): Opt[A] = new Opt(Some(x))

  implicit def toOption[A](arg: Opt[A]): Option[A] = arg.asOption
}

