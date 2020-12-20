package instruction.micro

import bit8.instruction.micro.MicroInstruction
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class MicroInstructionSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  Feature("MicroInstruction") {
    Scenario("Works") {
      import bit8.instruction.micro.MicroInstruction._

      assert(RegAIn.value == Vector(true))
      assert(RegAOut.value == Vector(false, true))
      assert(RegBIn.value == Vector(false, false, true))
      assert(RegBOut.value == Vector(false, false, false, true))

      assert((RegAOut + RegBIn).value == Vector(false, true, true))
      assert((RegBIn + RegAOut).value == Vector(false, true, true))
      assert((RegBIn + RegBIn).value == Vector(false, false, true))
    }

    Scenario("Contains") {
      assert(MicroInstruction(true, false, false).contains(MicroInstruction(true, false, false)))
      assert(MicroInstruction(true, true, false).contains(MicroInstruction(true, false, false)))
      assert(!MicroInstruction(false, false, false).contains(MicroInstruction(true, false, false)))
      assert(!MicroInstruction(false, false, false).contains(MicroInstruction(false, false, false)))
    }
  }

}
