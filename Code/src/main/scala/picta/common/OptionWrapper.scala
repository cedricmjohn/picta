package picta.common


object OptionWrapper {
  case class Opt[+A](asOption: Option[A])

  def Blank[A]: Opt[A] = Opt[A](None)
  def Empty[A]: Opt[List[A]] = Opt[List[A]](None)

  implicit def liftToOpt[A](x: A): Opt[A] = Opt(Some(x))
  implicit def liftToOption[A](arg: Opt[A]): Option[A] = arg.asOption
}

