package simulation.components

import bit8.simulation.components.wire.Branch
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.Ignore
import org.scalatest.featurespec.AnyFeatureSpec

class BranchSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var c0: Connection = _
  var c1: Connection = _
  var c2: Connection = _

  before {
    val w = Connection.wire()
    val branch = Branch.branch(w.right)

    c0 = w.left
    c1 = branch._1
    c2 = branch._2
  }

  Feature("Branch") {
    Scenario("State propagates 1") {
      c0.updateState(High)
      assert(c1.wire.isHigh)
      assert(c2.wire.isHigh)

      c0.updateState(Low)
      assert(c1.wire.isLow)
      assert(c2.wire.isLow)
    }

    Scenario("State propagates 2") {
      c1.updateState(High)
      assert(c0.wire.isHigh)
      assert(c2.wire.isHigh)

      c1.updateState(Low)
      assert(c0.wire.isLow)
      assert(c2.wire.isLow)
    }

    Scenario("State propagates 3") {
      c2.updateState(High)
      assert(c0.wire.isHigh)
      assert(c1.wire.isHigh)

      c2.updateState(Low)
      assert(c0.wire.isLow)
      assert(c1.wire.isLow)
    }

    Scenario("State propagates 4") {
      c0.updateState(High)
      c1.updateState(High)
      c2.updateState(High)
      assert(c0.wire.isHigh)
      assert(c1.wire.isHigh)
      assert(c2.wire.isHigh)

      c0.updateState(Low)
      assert(c0.wire.isHigh)
      assert(c1.wire.isHigh)
      assert(c2.wire.isHigh)

      c1.updateState(Low)
      assert(c0.wire.isHigh)
      assert(c1.wire.isHigh)
      assert(c2.wire.isHigh)

      c2.updateState(Low)
      assert(c0.wire.isLow)
      assert(c1.wire.isLow)
      assert(c2.wire.isLow)
    }

    Scenario("Branch with initial state") {
      val w = Connection.wire()
      w.left.updateState(High)
      val branch = Branch.branch(w.right)

      assert(branch._1.wire.isHigh)
      assert(branch._1.wire.isHigh)
    }

  }

}
