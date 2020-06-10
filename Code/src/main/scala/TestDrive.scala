import org.carbonateresearch.conus.common._
import org.carbonateresearch.conus.grids._

object TestDrive {
}




//trait FlatCaseClassCodecs extends HListInstances {
//  implicit def encodeCaseClassFlat[A, Repr <: HList](implicit
//                                                     gen: Generic.Aux[A, Repr],
//                                                     encodeRepr: Encoder[Repr]
//                                                    ): Encoder[A] = encodeRepr.contramap(gen.to)
//
//}
//
//object FlatCaseClassCodecs extends FlatCaseClassCodecs


//  val sum: ModelVariable[Double] = ModelVariable("Sum", 1.0)
//  val interestRate: ModelVariable[Double] = ModelVariable("Interest Rate", 0.1)
//
//  def interestCalculator = (s: Step) => sum(s - 1) + sum(s - 1) * interestRate(s)
//
//  val model = new SteppedModel(10, "Finance").setGrid(3)
//    .defineMathematicalModel(sum =>> interestCalculator)
//    .defineInitialModelConditions(
//      PerCell(sum, List(
//        (List(1.0), Seq(0)),
//        (List(10.0), Seq(1)),
//        (List(100.0), Seq(2))
//      )),
//      AllCells(interestRate, List(0.10))
//    )

//  val runnedModel = model.run

//  Thread.sleep(1000)