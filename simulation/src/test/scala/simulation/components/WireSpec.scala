package simulation.components

import bit8.simulation.components.wire.Ground
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class WireSpec extends AnyFeatureSpec with GivenWhenThen {

  Feature("Wire") {
    Scenario("Change state") {
//      var lowToHighCount = 0
//      val wire = Connection.wire()
//      wire.onNewState {
//        case High => lowToHighCount += 1
//      }
//
//      wire.left.updateState(Ground)
//      wire.right.updateState(Ground)
//      wire.left.updateState(High)
//      assert(lowToHighCount == 0, "If one of connections is Ground, wire is low")
//
//      wire.right.updateState(Low)
//      assert(lowToHighCount == 1, "If one of connections is High and another is not Ground, then wire switches to high")
    }
  }

}
