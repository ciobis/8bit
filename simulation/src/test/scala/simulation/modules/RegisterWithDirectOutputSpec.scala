package simulation.modules

import bit8.simulation.components.utils.IntegerWithOverflow.Bit8
import bit8.simulation.components.utils.IntegerWithOverflow.minValue
import bit8.simulation.components.utils.IntegerWithOverflow.overflowInt
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import bit8.simulation.modules.Register
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils._
import bit8.simulation.modules.RegisterWithDirectOutput

class RegisterWithDirectOutputSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var oe, oeC: Connection = _
  var de, deC: Connection = _
  var clk, clkC: Connection = _
  var io0, io0C: Connection = _
  var io1, io1C: Connection = _
  var io2, io2C: Connection = _
  var io3, io3C: Connection = _
  var io4, io4C: Connection = _
  var io5, io5C: Connection = _
  var io6, io6C: Connection = _
  var io7, io7C: Connection = _
  var out0, out0C: Connection = _
  var out1, out1C: Connection = _
  var out2, out2C: Connection = _
  var out3, out3C: Connection = _
  var out4, out4C: Connection = _
  var out5, out5C: Connection = _
  var out6, out6C: Connection = _
  var out7, out7C: Connection = _

  var ioConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var outConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var register: RegisterWithDirectOutput = _

  before {
    val oeWire = Connection.wire()
    val deWire = Connection.wire()
    val clkWire = Connection.wire()
    val io0Wire = Connection.wire()
    val io1Wire = Connection.wire()
    val io2Wire = Connection.wire()
    val io3Wire = Connection.wire()
    val io4Wire = Connection.wire()
    val io5Wire = Connection.wire()
    val io6Wire = Connection.wire()
    val io7Wire = Connection.wire()
    val out0Wire = Connection.wire()
    val out1Wire = Connection.wire()
    val out2Wire = Connection.wire()
    val out3Wire = Connection.wire()
    val out4Wire = Connection.wire()
    val out5Wire = Connection.wire()
    val out6Wire = Connection.wire()
    val out7Wire = Connection.wire()

    oe = oeWire.left
    de = deWire.left
    clk = clkWire.left
    io0 = io0Wire.left
    io1 = io1Wire.left
    io2 = io2Wire.left
    io3 = io3Wire.left
    io4 = io4Wire.left
    io5 = io5Wire.left
    io6 = io6Wire.left
    io7 = io7Wire.left
    out0 = out0Wire.left
    out1 = out1Wire.left
    out2 = out2Wire.left
    out3 = out3Wire.left
    out4 = out4Wire.left
    out5 = out5Wire.left
    out6 = out6Wire.left
    out7 = out7Wire.left

    oeC = oeWire.right
    deC = deWire.right
    clkC = clkWire.right
    io0C = io0Wire.right
    io1C = io1Wire.right
    io2C = io2Wire.right
    io3C = io3Wire.right
    io4C = io4Wire.right
    io5C = io5Wire.right
    io6C = io6Wire.right
    io7C = io7Wire.right
    out0C = out0Wire.right
    out1C = out1Wire.right
    out2C = out2Wire.right
    out3C = out3Wire.right
    out4C = out4Wire.right
    out5C = out5Wire.right
    out6C = out6Wire.right
    out7C = out7Wire.right

    ioConnections = (io0, io1, io2, io3, io4, io5, io6, io7)
    outConnections = (out0, out1, out2, out3, out4, out5, out6, out7)
    register = new RegisterWithDirectOutput(oeC, deC, clkC, io0C, io1C, io2C, io3C, io4C, io5C, io6C, io7C,out0C, out1C, out2C, out3C, out4C, out5C, out6C, out7C)
  }

  Feature("Register with direct output") {
    Scenario("Writes/reads") {
      val v1 = overflowInt[Bit8](170)
      val v2 = overflowInt[Bit8](85)

      //push value to register
      v1.setConn(ioConnections)
      de.updateState(Low)
      clk.updateState(High)
      clk.updateState(Low)
      de.updateState(High)
      minValue[Bit8].setConn(ioConnections)

      //should be 0 when output disabled
      oe.updateState(High)
      assert(ioConnections.toInt.value == 0)
      //direct should contain value
      assert(outConnections.toInt.value == v1.value)

      oe.updateState(Low)
      assert(ioConnections.toInt.value == v1.value)
      assert(outConnections.toInt.value == v1.value)
      oe.updateState(High)

      de.updateState(Low)
      v2.setConn(ioConnections)
      clk.updateState(High)
      clk.updateState(Low)
      de.updateState(High)
      minValue[Bit8].setConn(ioConnections)

      oe.updateState(Low)
      assert(ioConnections.toInt.value == v2.value)
      assert(outConnections.toInt.value == v2.value)
    }
  }

}
