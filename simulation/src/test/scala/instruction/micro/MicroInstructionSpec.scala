package instruction.micro

import bit8.instruction.micro.MicroInstruction
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class MicroInstructionSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  Feature("MicroInstruction") {
    Scenario("Works") {
      import bit8.instruction.micro.MicroInstruction._

      assert(RegAIn.activeBits == Vector(true))
      assert(RegAOut.activeBits == Vector(false, true))
      assert(RegBIn.activeBits == Vector(false, false, true))
      assert(RegBOut.activeBits == Vector(false, false, false, true))

      assert((RegAOut + RegBIn).activeBits == Vector(false, true, true))
      assert((RegBIn + RegAOut).activeBits == Vector(false, true, true))
      assert((RegBIn + RegBIn).activeBits == Vector(false, false, true))
    }

    Scenario("Contains") {
      assert(MicroInstruction(true, false, false).contains(MicroInstruction(true, false, false)))
      assert(MicroInstruction(true, true, false).contains(MicroInstruction(true, false, false)))
      assert(!MicroInstruction(false, false, false).contains(MicroInstruction(true, false, false)))
      assert(!MicroInstruction(false, false, false).contains(MicroInstruction(false, false, false)))
    }
  }

}
