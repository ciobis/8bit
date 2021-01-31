package simulation.modules

import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Wire
import bit8.simulation.modules.AluModule
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.wire.Connection.LOW

class AluModuleSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var clk, clkC: Connection = _
  var ce, ceC: Connection = _
  var oe, oeC: Connection = _
  var op0, op0C: Connection = _
  var op1, op1C: Connection = _
  var op2, op2C: Connection = _
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
  var out0, out0C: Connection = _
  var out1, out1C: Connection = _
  var out2, out2C: Connection = _
  var out3, out3C: Connection = _
  var out4, out4C: Connection = _
  var out5, out5C: Connection = _
  var out6, out6C: Connection = _
  var out7, out7C: Connection = _
  var carryFlag, carryFlagC: Connection = _
  var equalsFlag, equalsFlagC: Connection = _

  var inputAConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var inputBConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var outputConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var functionSelectConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _

  var aluModule: AluModule = _

  private val sumFunction = overflowInt[Bit8](0)
  private val minusFunction = overflowInt[Bit8](1)
  private val compareFunction = overflowInt[Bit8](2)

  before {
    val clkWire = Connection.wire()
    val ceWire = Connection.wire()
    val oeWire = Connection.wire()
    val op0Wire = Connection.wire()
    val op1Wire = Connection.wire()
    val op2Wire = Connection.wire()
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
    val out0Wire = Connection.wire()
    val out1Wire = Connection.wire()
    val out2Wire = Connection.wire()
    val out3Wire = Connection.wire()
    val out4Wire = Connection.wire()
    val out5Wire = Connection.wire()
    val out6Wire = Connection.wire()
    val out7Wire = Connection.wire()
    val carryFlagWire = Connection.wire()
    val equalsFlagWire = Connection.wire()

    clk = clkWire.left
    ce = ceWire.left
    oe = oeWire.left
    op0 = op0Wire.left
    op1 = op1Wire.left
    op2 = op2Wire.left
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
    out0 = out0Wire.left
    out1 = out1Wire.left
    out2 = out2Wire.left
    out3 = out3Wire.left
    out4 = out4Wire.left
    out5 = out5Wire.left
    out6 = out6Wire.left
    out7 = out7Wire.left
    carryFlag = carryFlagWire.left
    equalsFlag = equalsFlagWire.left

    clkC = clkWire.right
    ceC = ceWire.right
    oeC = oeWire.right
    op0C = op0Wire.right
    op1C = op1Wire.right
    op2C = op2Wire.right
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
    out0C = out0Wire.right
    out1C = out1Wire.right
    out2C = out2Wire.right
    out3C = out3Wire.right
    out4C = out4Wire.right
    out5C = out5Wire.right
    out6C = out6Wire.right
    out7C = out7Wire.right
    carryFlagC = carryFlagWire.right
    equalsFlagC = equalsFlagWire.right

    inputAConnections = (a0, a1, a2, a3, a4, a5, a6, a7)
    inputBConnections = (b0, b1, b2, b3, b4, b5, b6, b7)
    outputConnections = (out0, out1, out2, out3, out4, out5, out6, out7)
    functionSelectConnections = (LOW, LOW, LOW, LOW, LOW, op0C, op1C, op2C)

    aluModule = new AluModule(
      clkC, ceC, oeC,
      op0C, op1C, op2C,
      a0C, a1C, a2C, a3C, a4C, a5C, a6C, a7C,
      b0C, b1C, b2C, b3C, b4C, b5C, b6C, b7C,
      out0C, out1C, out2C, out3C, out4C, out5C, out6C, out7C,
      carryFlagC, equalsFlagC)
  }

  Feature("Alu Module") {

    Scenario("Calculate/output enable works") {
      ce.updateState(High)
      oe.updateState(High)
      sumFunction.setConn(functionSelectConnections)
      overflowInt[Bit8](15).setConn(inputAConnections)
      overflowInt[Bit8](22).setConn(inputBConnections)

      ce.updateState(Low)
      clk.updateState(High)
      clk.updateState(Low)
      ce.updateState(High)

      overflowInt[Bit8](0).setConn(inputAConnections)
      overflowInt[Bit8](0).setConn(inputBConnections)
      assert(outputConnections.toInt.value == 0)

      oe.updateState(Low)
      clk.updateState(High)
      clk.updateState(Low)
      assert(outputConnections.toInt.value == 37)
    }

    Scenario("Sum works") {
      sumFunction.setConn(functionSelectConnections)

      for {
        inputA <- (0 to 255)
        inputB <- (0 to 255)
      } {
        val a = overflowInt[Bit8](inputA)
        val b = overflowInt[Bit8](inputB)

        a.setConn(inputAConnections)
        b.setConn(inputBConnections)

        ce.updateState(Low)
        clk.updateState(High)
        clk.updateState(Low)
        ce.updateState(High)

        overflowInt[Bit8](0).setConn(inputAConnections)
        overflowInt[Bit8](0).setConn(inputBConnections)

        oe.updateState(Low)
        clk.updateState(High)
        clk.updateState(Low)

        val (expectedValue, carry) = a.plusWithCarry(b)
        assert(outputConnections.toInt.value == expectedValue.value)
      }
    }

    Scenario("Minus works") {
      minusFunction.setConn(functionSelectConnections)

      for {
        inputA <- (0 to 255)
        inputB <- (0 to 255)
      } {
        val a = overflowInt[Bit8](inputA)
        val b = overflowInt[Bit8](inputB)

        a.setConn(inputAConnections)
        b.setConn(inputBConnections)

        ce.updateState(Low)
        clk.updateState(High)
        clk.updateState(Low)
        ce.updateState(High)

        overflowInt[Bit8](0).setConn(inputAConnections)
        overflowInt[Bit8](0).setConn(inputBConnections)

        oe.updateState(Low)
        clk.updateState(High)
        clk.updateState(Low)

        val (expectedValue, carry) = a.minusWithCarry(b)
        assert(outputConnections.toInt.value == expectedValue.value)
      }
    }

    Scenario("Carry true") {
      val a = overflowInt[Bit8](255)
      val b = overflowInt[Bit8](1)

      a.setConn(inputAConnections)
      b.setConn(inputBConnections)

      ce.updateState(Low)
      clk.updateState(High)
      clk.updateState(Low)
      ce.updateState(High)

      assert(carryFlag.wire.isHigh)
    }

    Scenario("Carry false") {
      val a = overflowInt[Bit8](254)
      val b = overflowInt[Bit8](1)

      a.setConn(inputAConnections)
      b.setConn(inputBConnections)

      ce.updateState(Low)
      clk.updateState(High)
      clk.updateState(Low)
      ce.updateState(High)

      assert(carryFlag.wire.isLow)
    }

    Scenario("Equals") {
      compareFunction.setConn(functionSelectConnections)

      for {
        inputA <- (0 to 255)
        inputB <- (0 to 255)
      } {
        val a = overflowInt[Bit8](inputA)
        val b = overflowInt[Bit8](inputB)

        a.setConn(inputAConnections)
        b.setConn(inputBConnections)

        ce.updateState(Low)
        clk.updateState(High)
        clk.updateState(Low)
        ce.updateState(High)

        assert((a == b) == equalsFlag.wire.isHigh)
      }
    }

  }


}
