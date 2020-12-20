package simulation.components

import bit8.simulation.components.DoubleAlu
import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Wire
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.State

class DoubleAluSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var a0, a0C: Connection = _
  var a1, a1C: Connection = _
  var a2, a2C: Connection = _
  var a3, a3C: Connection = _
  var a4, a4C: Connection = _
  var a5, a5C: Connection = _
  var a6, a6C: Connection = _
  var a7, a7C: Connection = _
  var b0, b0C: Connection = _
  var b1, b1C: Connection = _
  var b2, b2C: Connection = _
  var b3, b3C: Connection = _
  var b4, b4C: Connection = _
  var b5, b5C: Connection = _
  var b6, b6C: Connection = _
  var b7, b7C: Connection = _
  var f0, f0C: Connection = _
  var f1, f1C: Connection = _
  var f2, f2C: Connection = _
  var f3, f3C: Connection = _
  var f4, f4C: Connection = _
  var f5, f5C: Connection = _
  var f6, f6C: Connection = _
  var f7, f7C: Connection = _
  var s0, s0C: Connection = _
  var s1, s1C: Connection = _
  var s2, s2C: Connection = _
  var s3, s3C: Connection = _
  var m, mC: Connection = _
  var cn, cnC: Connection = _
  var cn4, cn4C: Connection = _
  var ab, abC: Connection = _

  var inputAConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var inputBConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var outputConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var functionSelectConnections: (Connection, Connection, Connection, Connection) = _

  var doubleAlu: DoubleAlu = _

  val sumFunction: IntegerWithOverflow[(Boolean, Boolean, Boolean, Boolean)] = fromBits((true, false, false, true))
  val minusFunction: IntegerWithOverflow[(Boolean, Boolean, Boolean, Boolean)] = fromBits((false, true, true, false))
  val compareFunction: IntegerWithOverflow[(Boolean, Boolean, Boolean, Boolean)] = fromBits((false, true, true, false))

  before {
    val a0Wire = Connection.wire()
    val a1Wire = Connection.wire()
    val a2Wire = Connection.wire()
    val a3Wire = Connection.wire()
    val a4Wire = Connection.wire()
    val a5Wire = Connection.wire()
    val a6Wire = Connection.wire()
    val a7Wire = Connection.wire()
    val b0Wire = Connection.wire()
    val b1Wire = Connection.wire()
    val b2Wire = Connection.wire()
    val b3Wire = Connection.wire()
    val b4Wire = Connection.wire()
    val b5Wire = Connection.wire()
    val b6Wire = Connection.wire()
    val b7Wire = Connection.wire()
    val f0Wire = Connection.wire()
    val f1Wire = Connection.wire()
    val f2Wire = Connection.wire()
    val f3Wire = Connection.wire()
    val f4Wire = Connection.wire()
    val f5Wire = Connection.wire()
    val f6Wire = Connection.wire()
    val f7Wire = Connection.wire()
    val s0Wire = Connection.wire()
    val s1Wire = Connection.wire()
    val s2Wire = Connection.wire()
    val s3Wire = Connection.wire()
    val mWire = Connection.wire()
    val cnWire = Connection.wire()
    val cn4Wire = Connection.wire()
    val abWire = Connection.wire()

    a0 = a0Wire.left
    a1 = a1Wire.left
    a2 = a2Wire.left
    a3 = a3Wire.left
    a4 = a4Wire.left
    a5 = a5Wire.left
    a6 = a6Wire.left
    a7 = a7Wire.left
    b0 = b0Wire.left
    b1 = b1Wire.left
    b2 = b2Wire.left
    b3 = b3Wire.left
    b4 = b4Wire.left
    b5 = b5Wire.left
    b6 = b6Wire.left
    b7 = b7Wire.left
    f0 = f0Wire.left
    f1 = f1Wire.left
    f2 = f2Wire.left
    f3 = f3Wire.left
    f4 = f4Wire.left
    f5 = f5Wire.left
    f6 = f6Wire.left
    f7 = f7Wire.left
    s0 = s0Wire.left
    s1 = s1Wire.left
    s2 = s2Wire.left
    s3 = s3Wire.left
    m = mWire.left
    cn = cnWire.left
    cn4 = cn4Wire.left
    ab = abWire.left

    a0C = a0Wire.right
    a1C = a1Wire.right
    a2C = a2Wire.right
    a3C = a3Wire.right
    a4C = a4Wire.right
    a5C = a5Wire.right
    a6C = a6Wire.right
    a7C = a7Wire.right
    b0C = b0Wire.right
    b1C = b1Wire.right
    b2C = b2Wire.right
    b3C = b3Wire.right
    b4C = b4Wire.right
    b5C = b5Wire.right
    b6C = b6Wire.right
    b7C = b7Wire.right
    f0C = f0Wire.right
    f1C = f1Wire.right
    f2C = f2Wire.right
    f3C = f3Wire.right
    f4C = f4Wire.right
    f5C = f5Wire.right
    f6C = f6Wire.right
    f7C = f7Wire.right
    s0C = s0Wire.right
    s1C = s1Wire.right
    s2C = s2Wire.right
    s3C = s3Wire.right
    mC = mWire.right
    cnC = cnWire.right
    cn4C = cn4Wire.right
    abC = abWire.right

    inputAConnections = (a0, a1, a2, a3, a4, a5, a6, a7)
    inputBConnections = (b0, b1, b2, b3, b4, b5, b6, b7)
    outputConnections = (f0, f1, f2, f3, f4, f5, f6, f7)
    functionSelectConnections = (s0, s1, s2, s3)

    doubleAlu = new DoubleAlu(a0C, a1C, a2C, a3C, a4C, a5C, a6C, a7C, b0C, b1C, b2C, b3C, b4C, b5C, b6C, b7C, f0C, f1C, f2C, f3C, f4C, f5C, f6C, f7C, s0C, s1C, s2C, s3C, mC, cnC, cn4C, abC)
  }

  Feature("Double Alu") {
    info("Double alu is component made of two 74ls181 to make 8 Bit operations possible")

    Scenario("Sum works") {
      m.updateState(Low)
      cn.updateState(High)
      sumFunction.setConn(functionSelectConnections)

      for {
        inputA <- (0 to 255)
        inputB <- (0 to 255)
      } {
        val a = overflowInt[Bit8](inputA)
        val b = overflowInt[Bit8](inputB)

        a.setConn(inputAConnections)
        b.setConn(inputBConnections)

        val (expectedValue, carry) = a.plusWithCarry(b)

        assert(outputConnections.toInt.value == expectedValue.value)
        assert(cn4.wire.getState() == State.fromBoolean(carry))
      }
    }

    Scenario("Minus works") {
      m.updateState(Low)
      cn.updateState(Low)
      minusFunction.setConn(functionSelectConnections)

      for {
        inputA <- (0 to 255)
        inputB <- (0 to 255)
      } {
        val a = overflowInt[Bit8](inputA)
        val b = overflowInt[Bit8](inputB)

        a.setConn(inputAConnections)
        b.setConn(inputBConnections)

        val (expectedValue, carry) = a.minusWithCarry(b)

        assert(outputConnections.toInt.value == expectedValue.value)
        assert(cn4.wire.getState() == State.fromBoolean(carry))
      }
    }

    Scenario("Equals works") {
      m.updateState(Low)
      cn.updateState(High)
      compareFunction.setConn(functionSelectConnections)

      for {
        inputA <- (0 to 255)
        inputB <- (0 to 255)
      } {
        val a = overflowInt[Bit8](inputA)
        val b = overflowInt[Bit8](inputB)

        a.setConn(inputAConnections)
        b.setConn(inputBConnections)

        assert((a == b) == ab.wire.isHigh)
      }
    }


  }
}
