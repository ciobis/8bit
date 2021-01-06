package simulation.modules

import bit8.simulation.components.utils.IntegerWithOverflow.Bit8
import bit8.simulation.components.utils.IntegerWithOverflow.Bit16
import bit8.simulation.components.utils.IntegerWithOverflow.overflowInt
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection.LOW
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Wire
import bit8.simulation.modules.InstructionDecoder
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import bit8.simulation.components.utils.Utils.IntFromBinaryString
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.utils.Utils.Connections16ToIntOverflow
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.wire.Low

class InstructionDecoderSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var clkC,clk: Connection = _
  var ieC, ie: Connection = _
  var crC, cr: Connection = _
  var eqFlagC, eqFlag: Connection = _
  var i0C, i0: Connection = _
  var i1C, i1: Connection = _
  var i2C, i2: Connection = _
  var i3C, i3: Connection = _
  var i4C, i4: Connection = _
  var i5C, i5: Connection = _
  var i6C, i6: Connection = _
  var i7C, i7: Connection = _
  var o0C, o0: Connection = _
  var o1C, o1: Connection = _
  var o2C, o2: Connection = _
  var o3C, o3: Connection = _
  var o4C, o4: Connection = _
  var o5C, o5: Connection = _
  var o6C, o6: Connection = _
  var o7C, o7: Connection = _

  var o8C, o8: Connection = _
  var o9C, o9: Connection = _
  var o10C, o10: Connection = _
  var o11C, o11: Connection = _
  var o12C, o12: Connection = _
  var o13C, o13: Connection = _
  var o14C, o14: Connection = _
  var o15C, o15: Connection = _

  var instruction: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var out: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var encodeData: Map[Int, Int] = _
  var flagsEEpromData: Map[Int, Int] = _
  var decoder: InstructionDecoder = _

  before {
    val clkWire = Connection.wire()
    val ieWire = Connection.wire()
    val crWire = Connection.wire()
    val eqWire = Connection.wire()
    val i0Wire = Connection.wire()
    val i1Wire = Connection.wire()
    val i2Wire = Connection.wire()
    val i3Wire = Connection.wire()
    val i4Wire = Connection.wire()
    val i5Wire = Connection.wire()
    val i6Wire = Connection.wire()
    val i7Wire = Connection.wire()
    val o0Wire = Connection.wire()
    val o1Wire = Connection.wire()
    val o2Wire = Connection.wire()
    val o3Wire = Connection.wire()
    val o4Wire = Connection.wire()
    val o5Wire = Connection.wire()
    val o6Wire = Connection.wire()
    val o7Wire = Connection.wire()
    val o8Wire = Connection.wire()
    val o9Wire = Connection.wire()
    val o10Wire = Connection.wire()
    val o11Wire = Connection.wire()
    val o12Wire = Connection.wire()
    val o13Wire = Connection.wire()
    val o14Wire = Connection.wire()
    val o15Wire = Connection.wire()

    clkC = clkWire.left
    ieC = ieWire.left
    crC = crWire.left
    eqFlagC = eqWire.left
    i0C = i0Wire.left
    i1C = i1Wire.left
    i2C = i2Wire.left
    i3C = i3Wire.left
    i4C = i4Wire.left
    i5C = i5Wire.left
    i6C = i6Wire.left
    i7C = i7Wire.left
    o0C = o0Wire.left
    o1C = o1Wire.left
    o2C = o2Wire.left
    o3C = o3Wire.left
    o4C = o4Wire.left
    o5C = o5Wire.left
    o6C = o6Wire.left
    o7C = o7Wire.left
    o8C = o8Wire.left
    o9C = o9Wire.left
    o10C = o10Wire.left
    o11C = o11Wire.left
    o12C = o12Wire.left
    o13C = o13Wire.left
    o14C = o14Wire.left
    o15C = o15Wire.left

    clk = clkWire.right
    ie = ieWire.right
    cr = crWire.right
    eqFlag = eqWire.right
    i0 = i0Wire.right
    i1 = i1Wire.right
    i2 = i2Wire.right
    i3 = i3Wire.right
    i4 = i4Wire.right
    i5 = i5Wire.right
    i6 = i6Wire.right
    i7 = i7Wire.right
    o0 = o0Wire.right
    o1 = o1Wire.right
    o2 = o2Wire.right
    o3 = o3Wire.right
    o4 = o4Wire.right
    o5 = o5Wire.right
    o6 = o6Wire.right
    o7 = o7Wire.right
    o8 = o8Wire.right
    o9 = o9Wire.right
    o10 = o10Wire.right
    o11 = o11Wire.right
    o12 = o12Wire.right
    o13 = o13Wire.right
    o14 = o14Wire.right
    o15 = o15Wire.right

    instruction = (i0, i1, i2, i3, i4, i5, i6, i7)
    out = (o0, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15)

    encodeData = Map(
      "00000000 0000".binaryInt -> 0,
      "00000000 0001".binaryInt -> 2,
      "00000000 0010".binaryInt -> 4,
      "00000000 0011".binaryInt -> 8,
      "00000000 0100".binaryInt -> 16,
      "00000000 0101".binaryInt -> 32,
      "00000000 0110".binaryInt -> 64,
      "00000000 0111".binaryInt -> 128,
      "00000000 1000".binaryInt -> 170,
      "00000000 1001".binaryInt -> 85,
      "00000000 1010".binaryInt -> 255,
      "00000000 1011".binaryInt -> 1,
      "00000000 1100".binaryInt -> 2,
      "00000000 1101".binaryInt -> 3,
      "00000000 1110".binaryInt -> 4,
      "00000000 1111".binaryInt -> 5,

      "00000001 0000".binaryInt -> 123,
      "00000001 0001".binaryInt -> 231,

      "00000010 0000".binaryInt -> 111,
      "00000010 0001".binaryInt -> 222,

      "00000011 0000".binaryInt -> 112,
      "00000100 0000".binaryInt -> 113,
    )

    flagsEEpromData = bit8.instruction.Utils.instructionOverrideDefaults ++ Map(
      "00000011 0000".binaryInt -> "00000000".binaryInt,
      "00000011 0001".binaryInt -> "00000011".binaryInt,
      "00000100 0000".binaryInt -> "00000100".binaryInt,
      "00000100 0001".binaryInt -> "00000000".binaryInt,
    )

    decoder = new InstructionDecoder(
      clkC, ieC, crC, LOW, eqFlagC,
      i0C, i1C, i2C, i3C, i4C, i5C, i6C, i7C,
      o0C, o1C, o2C, o3C, o4C, o5C, o6C, o7C,
      o8C, o9C, o10C, o11C, o12C, o13C, o14C, o15C,
      LOW, LOW, LOW, LOW, LOW, LOW, LOW, LOW,
      LOW, LOW, LOW, LOW, LOW, LOW, LOW, LOW,
      flagsEEpromData, encodeData, encodeData, encodeData, encodeData
    )
  }

  Feature("Instruction decoder") {
    Scenario("Executes instruction") {

      (0 until 16).foreach(i => {
        assert(out.toInt.value == shiftAndMultiply(encodeData(i)))
        clk.updateState(High)
        clk.updateState(Low)
      })

    }

    Scenario("Next instruction") {
      //alter internal counter
      clk.updateState(High)
      clk.updateState(Low)

      overflowInt[Bit8](1).setConn(instruction)
      ie.updateState(High)
      cr.updateState(High)

      clk.updateState(High)
      clk.updateState(Low)
      assert(out.toInt.value == shiftAndMultiply(123))

      cr.updateState(Low)
      clk.updateState(High)
      clk.updateState(Low)
      assert(out.toInt.value == shiftAndMultiply(231))

      overflowInt[Bit8](2).setConn(instruction)
      ie.updateState(High)
      cr.updateState(High)

      clk.updateState(High)
      clk.updateState(Low)
      assert(out.toInt.value == shiftAndMultiply(111))

      cr.updateState(Low)
      clk.updateState(High)
      clk.updateState(Low)
      assert(out.toInt.value == shiftAndMultiply(222))
    }

    Scenario("Instruction enable works on rising clock only") {
      overflowInt[Bit8](1).setConn(instruction)

      ie.updateState(High)
      assert(out.toInt.value == shiftAndMultiply(0))

      clk.updateState(High)
      assert(out.toInt.value == shiftAndMultiply(123))
    }

    Scenario("Counter reset works on falling clock only") {
      clk.updateState(High)
      clk.updateState(Low)

      assert(out.toInt.value == shiftAndMultiply(2))

      cr.updateState(High)
      clk.updateState(High)
      assert(out.toInt.value == shiftAndMultiply(2))

      clk.updateState(Low)
      assert(out.toInt.value == shiftAndMultiply(0))
    }

    Scenario("CMP flag overrides instructions according to flags EEPROM") {
      overflowInt[Bit8]("00000011".binaryInt).setConn(instruction)
      eqFlag.updateState(Low)
      ie.updateState(High)
      cr.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      assert(out.toInt.value == shiftAndMultiply(0))

      eqFlag.updateState(High)
      ie.updateState(High)
      cr.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      assert(out.toInt.value == shiftAndMultiply(112))
    }
  }

  //since only 16 first output bits (2 bytes) are tested, shift by 1 byte is enough
  private def shiftAndMultiply(value: Int): Int = (value << 8) | value

}
