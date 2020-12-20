package simulation.components

import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class UtilsSpec extends AnyFeatureSpec with GivenWhenThen {

  Feature("Utils") {
    Scenario("IntOverflow 4 Bits set value to connections") {
      import bit8.simulation.components.utils.Utils.IntOverflowUtils

      val w1 = Connection.wire()
      val w2 = Connection.wire()
      val w3 = Connection.wire()
      val w4 = Connection.wire()

      fromBits[Bit4](true, false, true, false).setConn(w1.left, w2.left, w3.left, w4.left)

      assert(w1.isHigh)
      assert(w2.isLow)
      assert(w3.isHigh)
      assert(w4.isLow)
    }

    Scenario("IntOverflow 4 Bits from connections") {
      import bit8.simulation.components.utils.Utils.Connections4ToIntOverflow

      val w1 = Connection.wire()
      val w2 = Connection.wire()
      val w3 = Connection.wire()
      val w4 = Connection.wire()
      w1.left.updateState(High)
      w2.left.updateState(Low)
      w3.left.updateState(High)
      w4.left.updateState(Low)

      val v: IntegerWithOverflow[Bit4] = (w1.right, w2.right, w3.right, w4.right).toInt

      assert(v.value == 10)
    }

    Scenario("IntOverflow 8 Bits set value to connections") {
      import bit8.simulation.components.utils.Utils.IntOverflowUtils

      val w1 = Connection.wire()
      val w2 = Connection.wire()
      val w3 = Connection.wire()
      val w4 = Connection.wire()
      val w5 = Connection.wire()
      val w6 = Connection.wire()
      val w7 = Connection.wire()
      val w8 = Connection.wire()

      fromBits[Bit8](true, false, true, false, true, false, true, false)
        .setConn(w1.left, w2.left, w3.left, w4.left, w5.left, w6.left, w7.left, w8.left)

      assert(w1.isHigh)
      assert(w2.isLow)
      assert(w3.isHigh)
      assert(w4.isLow)
      assert(w5.isHigh)
      assert(w6.isLow)
      assert(w7.isHigh)
      assert(w8.isLow)
    }

    Scenario("IntOverflow 8 Bits from connections") {
      import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow

      val w1 = Connection.wire()
      val w2 = Connection.wire()
      val w3 = Connection.wire()
      val w4 = Connection.wire()
      val w5 = Connection.wire()
      val w6 = Connection.wire()
      val w7 = Connection.wire()
      val w8 = Connection.wire()
      w1.left.updateState(High)
      w2.left.updateState(Low)
      w3.left.updateState(High)
      w4.left.updateState(Low)
      w5.left.updateState(High)
      w6.left.updateState(Low)
      w7.left.updateState(High)
      w8.left.updateState(Low)

      val v: IntegerWithOverflow[Bit8] = (w1.right, w2.right, w3.right, w4.right, w5.right, w6.right, w7.right, w8.right).toInt

      assert(v.value == 170)
    }

    Scenario("IntOverflow 16 Bits set value to connections") {
      import bit8.simulation.components.utils.Utils.IntOverflowUtils

      val w1 = Connection.wire()
      val w2 = Connection.wire()
      val w3 = Connection.wire()
      val w4 = Connection.wire()
      val w5 = Connection.wire()
      val w6 = Connection.wire()
      val w7 = Connection.wire()
      val w8 = Connection.wire()
      val w9 = Connection.wire()
      val w10 = Connection.wire()
      val w11 = Connection.wire()
      val w12 = Connection.wire()
      val w13 = Connection.wire()
      val w14 = Connection.wire()
      val w15 = Connection.wire()
      val w16 = Connection.wire()

      fromBits[Bit16](true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false)
        .setConn(w1.left, w2.left, w3.left, w4.left, w5.left, w6.left, w7.left, w8.left,
          w9.left, w10.left, w11.left, w12.left, w13.left, w14.left, w15.left, w16.left)

      assert(w1.isHigh)
      assert(w2.isLow)
      assert(w3.isHigh)
      assert(w4.isLow)
      assert(w5.isHigh)
      assert(w6.isLow)
      assert(w7.isHigh)
      assert(w8.isLow)
      assert(w9.isHigh)
      assert(w10.isLow)
      assert(w11.isHigh)
      assert(w12.isLow)
      assert(w13.isHigh)
      assert(w14.isLow)
      assert(w15.isHigh)
      assert(w16.isLow)
    }

    Scenario("IntOverflow 16 Bits from connections") {
      import bit8.simulation.components.utils.Utils.Connections16ToIntOverflow

      val w1 = Connection.wire()
      val w2 = Connection.wire()
      val w3 = Connection.wire()
      val w4 = Connection.wire()
      val w5 = Connection.wire()
      val w6 = Connection.wire()
      val w7 = Connection.wire()
      val w8 = Connection.wire()
      val w9 = Connection.wire()
      val w10 = Connection.wire()
      val w11 = Connection.wire()
      val w12 = Connection.wire()
      val w13 = Connection.wire()
      val w14 = Connection.wire()
      val w15 = Connection.wire()
      val w16 = Connection.wire()
      w1.left.updateState(High)
      w2.left.updateState(Low)
      w3.left.updateState(High)
      w4.left.updateState(Low)
      w5.left.updateState(High)
      w6.left.updateState(Low)
      w7.left.updateState(High)
      w8.left.updateState(Low)
      w9.left.updateState(High)
      w10.left.updateState(Low)
      w11.left.updateState(High)
      w12.left.updateState(Low)
      w13.left.updateState(High)
      w14.left.updateState(Low)
      w15.left.updateState(High)
      w16.left.updateState(Low)

      val v: IntegerWithOverflow[Bit16] = (w1.right, w2.right, w3.right, w4.right, w5.right, w6.right, w7.right, w8.right,
        w9.right, w10.right, w11.right, w12.right, w13.right, w14.right, w15.right, w16.right).toInt

      assert(v.value == 43690)
    }
  }

}
