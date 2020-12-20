package simulation.components

import bit8.simulation.components.Inverter
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Ground
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class InverterSpec extends AnyFeatureSpec with GivenWhenThen  {

  Feature("Inverter") {
    Scenario("Inverter works") {
      val (input, inverterInput) = Connection.wire().connections
      val output = Inverter(inverterInput)
      val output2 = Inverter(output)

      assert(output.wire.getState() == High)
      assert(output2.wire.getState() == Low)

      input.updateState(High)
      assert(output.wire.getState() == Low)
      assert(output2.wire.getState() == High)

      input.updateState(Low)
      assert(output.wire.getState() == High)
      assert(output2.wire.getState() == Low)

      input.updateState(Ground)
      assert(output.wire.getState() == High)
      assert(output2.wire.getState() == Low)
    }

    Scenario("Inverter with initial value") {
      val (input, inverterInput) = Connection.wire().connections
      val output = Inverter(inverterInput)

      assert(input.wire.isLow)
      assert(output.wire.isHigh)
    }
  }

}
