package instruction

import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class CompilerSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  Feature("Compiler") {
    Scenario("Compiles") {
      val program = Seq(
        "MOV B,A",
        "MOV A,B",
        "MOV A,123",
        "ADD A,B",
        "SUB A,B",
        "MOV B,123",
      )
      val instructions: Seq[Int] = bit8.instruction.Compiler.compile(program)

      assert(instructions == Seq(1, 2, 3, 123, 4, 5, 6, 123))
    }
  }

}
