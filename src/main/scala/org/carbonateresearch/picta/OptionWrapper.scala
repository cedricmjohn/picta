/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta

/** This object provides a wrapper for the Scala built-in 'Option' type. Within it are defined various things that
 * make the user interface a lot simpler. A wrapper is created so as not to pollute non-Picta code with the implicit
 * Option conversion.
 */
private[picta] object OptionWrapper {
  /** The Opt type equivalent of an Option type 'None' */
  def Blank[A]: Opt[A] = Opt[A](None)

  /** The Opt type equivalent for an Option[List[T]] type 'None'. This is to get around type erasure for the nested
   * generic type, 'T'.
   */
  def Empty[A]: Opt[List[A]] = Opt[List[A]](None)

  /** case class that defines the Option wrapper */
  case class Opt[+A](option: Option[A])

  /** this implicitly lifts a naked value to an Opt */
  implicit def liftToOpt[A](x: A): Opt[A] = Opt(Some(x))

  /** this implicitly lifts an Option to an Opt */
  implicit def liftToOption[A](arg: Opt[A]): Option[A] = arg.option
}