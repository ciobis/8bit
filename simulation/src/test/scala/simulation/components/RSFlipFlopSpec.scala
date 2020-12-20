package simulation.components

import bit8.simulation.components.RSFlipFlop
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class RSFlipFlopSpec  extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var setC, set: Connection = _
  var resetC, reset: Connection = _
  var output: Connection = _

  before {
    val setWire = Connection.wire()
    val resetWire = Connection.wire()

    setC = setWire.left
    resetC = resetWire.left

    set = setWire.right
    reset = resetWire.right

    output = RSFlipFlop(setC, resetC)
  }

  Feature("RS Flip Flop") {
    Scenario("Works") {
      assert(output.wire.isLow)

      set.updateState(High)
      assert(output.wire.isHigh)
      set.updateState(Low)
      assert(output.wire.isHigh)

      reset.updateState(High)
      assert(output.wire.isLow)
      reset.updateState(Low)
      assert(output.wire.isLow)
    }
  }

}
