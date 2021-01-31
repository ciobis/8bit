package simulation.modules

import bit8.simulation.components.Ram
import bit8.simulation.components.utils.IntegerWithOverflow.Bit16
import bit8.simulation.components.utils.IntegerWithOverflow.Bit8
import bit8.simulation.components.utils.IntegerWithOverflow.overflowInt
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import bit8.simulation.modules.RamModule
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils._

class RamModuleSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var a0C, a0: Connection = _
  var a1C, a1: Connection = _
  var a2C, a2: Connection = _
  var a3C, a3: Connection = _
  var a4C, a4: Connection = _
  var a5C, a5: Connection = _
  var a6C, a6: Connection = _
  var a7C, a7: Connection = _
  var a8C, a8: Connection = _
  var a9C, a9: Connection = _
  var a10C, a10: Connection = _
  var a11C, a11: Connection = _
  var a12C, a12: Connection = _
  var a13C, a13: Connection = _
  var a14C, a14: Connection = _
  var a15C, a15: Connection = _
  var io0C, io0: Connection = _
  var io1C, io1: Connection = _
  var io2C, io2: Connection = _
  var io3C, io3: Connection = _
  var io4C, io4: Connection = _
  var io5C, io5: Connection = _
  var io6C, io6: Connection = _
  var io7C, io7: Connection = _
  var weC, we: Connection = _
  var oeC, oe: Connection = _
  var lInC, lIn: Connection = _
  var lOutC, lOut: Connection = _
  var hInC, hIn: Connection = _
  var hOutC, hOut: Connection = _
  var clkC, clk: Connection = _

  var dataConns: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var ramModule: RamModule = _

  before {
    val io0Wire = Connection.wire()
    val io1Wire = Connection.wire()
    val io2Wire = Connection.wire()
    val io3Wire = Connection.wire()
    val io4Wire = Connection.wire()
    val io5Wire = Connection.wire()
    val io6Wire = Connection.wire()
    val io7Wire = Connection.wire()
    val weWire = Connection.wire()
    val oeWire = Connection.wire()
    val lInWire = Connection.wire()
    val lOutWire = Connection.wire()
    val hInWire = Connection.wire()
    val hOutWire = Connection.wire()
    val clkWire = Connection.wire()

    io0C = io0Wire.left
    io1C = io1Wire.left
    io2C = io2Wire.left
    io3C = io3Wire.left
    io4C = io4Wire.left
    io5C = io5Wire.left
    io6C = io6Wire.left
    io7C = io7Wire.left
    weC = weWire.left
    oeC = oeWire.left
    lInC = lInWire.left
    lOutC = lOutWire.left
    hInC = hInWire.left
    hOutC = hOutWire.left
    clkC = clkWire.left

    io0 = io0Wire.right
    io1 = io1Wire.right
    io2 = io2Wire.right
    io3 = io3Wire.right
    io4 = io4Wire.right
    io5 = io5Wire.right
    io6 = io6Wire.right
    io7 = io7Wire.right
    lIn = lInWire.right
    lOut = lOutWire.right
    hIn = hInWire.right
    hOut = hOutWire.right
    clk = clkWire.right

    we = weWire.right
    oe = oeWire.right

    dataConns = (io0, io1, io2, io3, io4, io5, io6, io7)

    ramModule = new RamModule(clkC, weC, oeC, lInC, lOutC, hInC, hOutC, io0C, io1C, io2C, io3C, io4C, io5C, io6C, io7C)
    oe.updateState(High)
    lOut.updateState(High)
    hOut.updateState(High)
  }

  Feature("Ram Module") {

    Scenario("Write/Read") {
      val dataAddress1L = overflowInt[Bit8](5)
      val dataAddress1H = overflowInt[Bit8](5)
      val data1 = overflowInt[Bit8](127)

      val dataAddress2L = overflowInt[Bit8](6)
      val dataAddress2H = overflowInt[Bit8](5)
      val data2 = overflowInt[Bit8](7)

      //write data 1
      dataAddress1L.setConn(dataConns)
      lIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      lIn.updateState(Low)

      dataAddress1H.setConn(dataConns)
      hIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      hIn.updateState(Low)

      data1.setConn(dataConns)
      we.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      we.updateState(Low)

      //write data 2
      dataAddress2L.setConn(dataConns)
      lIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      lIn.updateState(Low)

      dataAddress2H.setConn(dataConns)
      hIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      hIn.updateState(Low)

      data2.setConn(dataConns)
      we.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      we.updateState(Low)

      //reset input connections
      overflowInt[Bit8]().setConn(dataConns)

      //output data on high impedence when oe, we high
      assert(dataConns.toInt.value == 0)

      //read data 1
      dataAddress1L.setConn(dataConns)
      lIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      lIn.updateState(Low)

      dataAddress1H.setConn(dataConns)
      hIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      hIn.updateState(Low)

      overflowInt[Bit8]().setConn(dataConns)
      oe.updateState(Low)
      assert(dataConns.toInt.value == data1.value)
      oe.updateState(High)

      //read data 2
      dataAddress2L.setConn(dataConns)
      lIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      lIn.updateState(Low)

      dataAddress2H.setConn(dataConns)
      hIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      hIn.updateState(Low)

      oe.updateState(Low)
      assert(dataConns.toInt.value == data2.value)
      oe.updateState(High)
    }

    Scenario("Lower register write/read") {
      val value = overflowInt[Bit8](5)
      value.setConn(dataConns)

      lIn.updateState(Low)
      clk.updateState(High)
      lIn.updateState(High)
      clk.updateState(Low)
      overflowInt[Bit8](0).setConn(dataConns)

      assert(dataConns.toInt.value == 0)

      lOut.updateState(Low)
      clk.updateState(High)

      assert(dataConns.toInt.value == 5)

      lOut.updateState(High)
      clk.updateState(Low)

      assert(dataConns.toInt.value == 0)
    }

    Scenario("Higher register write/read") {
      val value = overflowInt[Bit8](5)
      value.setConn(dataConns)

      hIn.updateState(Low)
      clk.updateState(High)
      hIn.updateState(High)
      clk.updateState(Low)
      overflowInt[Bit8](0).setConn(dataConns)
      assert(dataConns.toInt.value == 0)

      hOut.updateState(Low)
      clk.updateState(High)

      assert(dataConns.toInt.value == 5)

      hOut.updateState(High)
      clk.updateState(Low)

      assert(dataConns.toInt.value == 0)
    }

    Scenario("Writes value only on high clock") {
      val data = overflowInt[Bit8](127)
      lIn.updateState(High)
      hIn.updateState(High)

      data.setConn(dataConns)
      we.updateState(High)
      we.updateState(Low)
      overflowInt[Bit8]().setConn(dataConns)
      oe.updateState(Low)
      assert(dataConns.toInt.value == 0)

      oe.updateState(High)
      data.setConn(dataConns)
      we.updateState(High)
      clk.updateState(High)
      we.updateState(Low)
      clk.updateState(Low)
      overflowInt[Bit8]().setConn(dataConns)
      oe.updateState(Low)
      assert(dataConns.toInt.value == 127)
    }
  }

}
