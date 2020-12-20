package simulation.modules

import bit8.simulation.components.utils.IntegerWithOverflow.Bit8
import bit8.simulation.components.utils.IntegerWithOverflow.overflowInt
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils._
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.modules.OutputModule
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class OutputModuleSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var clkC, clk: Connection = _
  var readC, read: Connection = _
  var enabledC, enabled: Connection = _
  var input1C, input1: Connection = _
  var input2C, input2: Connection = _
  var input3C, input3: Connection = _
  var input4C, input4: Connection = _
  var input5C, input5: Connection = _
  var input6C, input6: Connection = _
  var input7C, input7: Connection = _
  var input8C, input8: Connection = _
  var output1C, output1: Connection = _
  var output2C, output2: Connection = _
  var output3C, output3: Connection = _
  var output4C, output4: Connection = _
  var output5C, output5: Connection = _
  var output6C, output6: Connection = _
  var output7C, output7: Connection = _
  var output8C, output8: Connection = _

  var input: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var output: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _

  before {
    val clkWire = Connection.wire()
    val readWire = Connection.wire()
    val enabledWire = Connection.wire()
    val input1Wire = Connection.wire()
    val input2Wire = Connection.wire()
    val input3Wire = Connection.wire()
    val input4Wire = Connection.wire()
    val input5Wire = Connection.wire()
    val input6Wire = Connection.wire()
    val input7Wire = Connection.wire()
    val input8Wire = Connection.wire()
    val output1Wire = Connection.wire()
    val output2Wire = Connection.wire()
    val output3Wire = Connection.wire()
    val output4Wire = Connection.wire()
    val output5Wire = Connection.wire()
    val output6Wire = Connection.wire()
    val output7Wire = Connection.wire()
    val output8Wire = Connection.wire()

    clk = clkWire.left
    read = readWire.left
    enabled = enabledWire.left
    input1 = input1Wire.left
    input2 = input2Wire.left
    input3 = input3Wire.left
    input4 = input4Wire.left
    input5 = input5Wire.left
    input6 = input6Wire.left
    input7 = input7Wire.left
    input8 = input8Wire.left
    output1 = output1Wire.left
    output2 = output2Wire.left
    output3 = output3Wire.left
    output4 = output4Wire.left
    output5 = output5Wire.left
    output6 = output6Wire.left
    output7 = output7Wire.left
    output8 = output8Wire.left

    clkC = clkWire.right
    readC = readWire.right
    enabledC = enabledWire.right
    input1C = input1Wire.right
    input2C = input2Wire.right
    input3C = input3Wire.right
    input4C = input4Wire.right
    input5C = input5Wire.right
    input6C = input6Wire.right
    input7C = input7Wire.right
    input8C = input7Wire.right
    output1C = output1Wire.right
    output2C = output2Wire.right
    output3C = output3Wire.right
    output4C = output4Wire.right
    output5C = output5Wire.right
    output6C = output6Wire.right
    output7C = output7Wire.right
    output8C = output8Wire.right

    input = (input1, input2, input3, input4, input5, input6, input7, input8)
    output = (output1, output2, output3, output4, output5, output6, output7, output8)

    new OutputModule(
      clkC, readC, enabledC,
      input1C, input2C, input3C, input4C, input5C, input6C, input7C, input8C,
      output1C, output2C, output3C, output4C, output5C, output6C, output7C, output8C
    )
  }

  Feature("Output module") {
    Scenario("Reads value and fires enabled flag") {
      val value = overflowInt[Bit8](127)

      assert(output.toInt.value == 0)
      assert(enabled.wire.isLow)

      value.setConn(input)
      read.updateState(High)
      clk.updateState(High)
      overflowInt[Bit8]().setConn(input)

      read.updateState(Low)
      clk.updateState(Low)
      assert(output.toInt == value)
      assert(enabled.wire.isHigh)

      clk.updateState(High)
      assert(output.toInt == value)
      assert(enabled.wire.isLow)

      clk.updateState(Low)
      assert(output.toInt == value)
      assert(enabled.wire.isLow)
    }

    Scenario("Doesnt read") {
      val value = overflowInt[Bit8](127)

      value.setConn(input)
      read.updateState(Low)

      clk.updateState(High)
      overflowInt[Bit8]().setConn(input)
      clk.updateState(Low)

      assert(output.toInt.value == 0)
    }

  }

}
