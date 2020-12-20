package simulation.components

import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Join
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class JoinSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  Feature("Join") {
    Scenario("Change state propagates") {
      val w1 = Connection.wire()
      val w2 = Connection.wire()

      Join(w1.right, w2.left)

      w1.left.updateState(High)
      assert(w2.isHigh)

      w1.left.updateState(Low)
      assert(w2.isLow)

      w2.right.updateState(High)
      assert(w1.isHigh)

      w2.right.updateState(Low)
      assert(w1.isLow)

      w1.left.updateState(High)
      w2.right.updateState(High)
      assert(w1.isHigh)
      assert(w2.isHigh)

      w1.left.updateState(Low)
      assert(w2.isHigh)

      w1.left.updateState(High)
      w2.right.updateState(Low)
      assert(w1.isHigh)
    }

    Scenario("Initial state propagates") {
      val w1 = Connection.wire()
      val w2 = Connection.wire()

      w1.left.updateState(High)
      Join(w1.right, w2.left)

      assert(w2.isHigh)
    }
  }

}
