package simulation.components

import bit8.simulation.components.Ram
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.IntegerWithOverflow.overflowInt
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.utils.Utils._
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class RamSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

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
  var cs1C, cs1: Connection = _
  var cs2C, cs2: Connection = _
  var weC, we: Connection = _
  var oeC, oe: Connection = _

  var addressConns: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var dataConns: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _

  before {
    val a0Wire = Connection.wire()
    val a1Wire = Connection.wire()
    val a2Wire = Connection.wire()
    val a3Wire = Connection.wire()
    val a4Wire = Connection.wire()
    val a5Wire = Connection.wire()
    val a6Wire = Connection.wire()
    val a7Wire = Connection.wire()
    val a8Wire = Connection.wire()
    val a9Wire = Connection.wire()
    val a10Wire = Connection.wire()
    val a11Wire = Connection.wire()
    val a12Wire = Connection.wire()
    val a13Wire = Connection.wire()
    val a14Wire = Connection.wire()
    val a15Wire = Connection.wire()
    val io0Wire = Connection.wire()
    val io1Wire = Connection.wire()
    val io2Wire = Connection.wire()
    val io3Wire = Connection.wire()
    val io4Wire = Connection.wire()
    val io5Wire = Connection.wire()
    val io6Wire = Connection.wire()
    val io7Wire = Connection.wire()
    val cs1Wire = Connection.wire()
    val cs2Wire = Connection.wire()
    val weWire = Connection.wire()
    val oeWire = Connection.wire()

    a0C = a0Wire.left
    a1C = a1Wire.left
    a2C = a2Wire.left
    a3C = a3Wire.left
    a4C = a4Wire.left
    a5C = a5Wire.left
    a6C = a6Wire.left
    a7C = a7Wire.left
    a8C = a8Wire.left
    a9C = a9Wire.left
    a10C = a10Wire.left
    a11C = a11Wire.left
    a12C = a12Wire.left
    a13C = a13Wire.left
    a14C = a14Wire.left
    a15C = a15Wire.left
    io0C = io0Wire.left
    io1C = io1Wire.left
    io2C = io2Wire.left
    io3C = io3Wire.left
    io4C = io4Wire.left
    io5C = io5Wire.left
    io6C = io6Wire.left
    io7C = io7Wire.left
    cs1C = cs1Wire.left
    cs2C = cs2Wire.left
    weC = weWire.left
    oeC = oeWire.left

    a0 = a0Wire.right
    a1 = a1Wire.right
    a2 = a2Wire.right
    a3 = a3Wire.right
    a4 = a4Wire.right
    a5 = a5Wire.right
    a6 = a6Wire.right
    a7 = a7Wire.right
    a8 = a8Wire.right
    a9 = a9Wire.right
    a10 = a10Wire.right
    a11 = a11Wire.right
    a12 = a12Wire.right
    a13 = a13Wire.right
    a14 = a14Wire.right
    a15 = a15Wire.right
    io0 = io0Wire.right
    io1 = io1Wire.right
    io2 = io2Wire.right
    io3 = io3Wire.right
    io4 = io4Wire.right
    io5 = io5Wire.right
    io6 = io6Wire.right
    io7 = io7Wire.right
    cs1 = cs1Wire.right
    cs2 = cs2Wire.right
    we = weWire.right
    oe = oeWire.right

    addressConns = (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)
    dataConns = (io0, io1, io2, io3, io4, io5, io6, io7)

    new Ram(a0C, a1C, a2C, a3C, a4C, a5C, a6C, a7C, a8C, a9C, a10C, a11C, a12C, a13C, a14C, a15C, io0C, io1C, io2C, io3C, io4C, io5C, io6C, io7C, cs1C, cs2C, weC, oeC)
  }

  Feature("Ram") {

    Scenario("Write/Read") {
      cs1.updateState(Low)
      cs2.updateState(High)
      oe.updateState(High)
      we.updateState(High)

      val dataAddress1 = overflowInt[Bit16](3254)
      val data1 = overflowInt[Bit8](127)

      val dataAddress2 = overflowInt[Bit16](123)
      val data2 = overflowInt[Bit8](7)

      //write data 1
      dataAddress1.setConn(addressConns)
      data1.setConn(dataConns)
      we.updateState(Low)
      we.updateState(High)

      //write data 2
      dataAddress2.setConn(addressConns)
      data2.setConn(dataConns)
      we.updateState(Low)
      we.updateState(High)

      //reset input connections
      overflowInt[Bit8]().setConn(dataConns)

      //output data on high impedence when oe, we high
      assert(dataConns.toInt.value == 0)

      //read data 1
      dataAddress1.setConn(addressConns)
      oe.updateState(Low)
      assert(dataConns.toInt.value == data1.value)
      oe.updateState(High)

      //read data 1
      dataAddress2.setConn(addressConns)
      oe.updateState(Low)
      assert(dataConns.toInt.value == data2.value)
      oe.updateState(High)

      //output data on high impedence when cs1 is high
      oe.updateState(Low)
      we.updateState(High)
      cs1.updateState(High)
      assert(dataConns.toInt.value == 0)
      cs1.updateState(Low)

      //output data on high impedence when cs2 is low
      oe.updateState(Low)
      we.updateState(High)
      cs2.updateState(Low)
      assert(dataConns.toInt.value == 0)
      cs2.updateState(High)
    }

  }

}
