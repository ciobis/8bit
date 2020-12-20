package simulation.components

import bit8.simulation.components.Or
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class OrSpec extends AnyFeatureSpec with GivenWhenThen {

  Feature("Or") {
    Scenario("Works") {
      val (in1, int1Right) = Connection.wire().connections
      val (in2, int2Right) = Connection.wire().connections
      val output = Or(int1Right, int2Right)

      in1.updateState(High)
      in2.updateState(Low)
      assert(output.wire.isHigh)

      in1.updateState(Low)
      in2.updateState(High)
      assert(output.wire.isHigh)

      in1.updateState(High)
      in2.updateState(High)
      assert(output.wire.isHigh)

      in1.updateState(Low)
      in2.updateState(Low)
      assert(output.wire.isLow)
    }

    Scenario("Works with initial state") {
      val (in1, int1Right) = Connection.wire().connections
      val (in2, int2Right) = Connection.wire().connections

      in1.updateState(High)
      in2.updateState(Low)
      val output = Or(int1Right, int2Right)
      assert(output.wire.isHigh)
    }
  }

}
