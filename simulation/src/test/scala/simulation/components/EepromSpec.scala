package simulation.components

import bit8.simulation.components.Eeprom
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils._
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class EepromSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var a0, a0C: Connection = _
  var a1, a1C: Connection = _
  var a2, a2C: Connection = _
  var a3, a3C: Connection = _
  var a4, a4C: Connection = _
  var a5, a5C: Connection = _
  var a6, a6C: Connection = _
  var a7, a7C: Connection = _
  var a8, a8C: Connection = _
  var a9, a9C: Connection = _
  var a10, a10C: Connection = _
  var io0, io0C: Connection = _
  var io1, io1C: Connection = _
  var io2, io2C: Connection = _
  var io3, io3C: Connection = _
  var io4, io4C: Connection = _
  var io5, io5C: Connection = _
  var io6, io6C: Connection = _
  var io7, io7C: Connection = _
  var ce, ceC: Connection = _
  var oe, oeC: Connection = _
  var we, weC: Connection = _

  var addressConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var dataConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _

  var eeprom: Eeprom = _
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
    val io0Wire = Connection.wire()
    val io1Wire = Connection.wire()
    val io2Wire = Connection.wire()
    val io3Wire = Connection.wire()
    val io4Wire = Connection.wire()
    val io5Wire = Connection.wire()
    val io6Wire = Connection.wire()
    val io7Wire = Connection.wire()
    val ceWire = Connection.wire()
    val oeWire = Connection.wire()
    val weWire = Connection.wire()

    a0 = a0Wire.left
    a1 = a1Wire.left
    a2 = a2Wire.left
    a3 = a3Wire.left
    a4 = a4Wire.left
    a5 = a5Wire.left
    a6 = a6Wire.left
    a7 = a7Wire.left
    a8 = a8Wire.left
    a9 = a9Wire.left
    a10 = a10Wire.left
    io0 = io0Wire.left
    io1 = io1Wire.left
    io2 = io2Wire.left
    io3 = io3Wire.left
    io4 = io4Wire.left
    io5 = io5Wire.left
    io6 = io6Wire.left
    io7 = io7Wire.left
    ce = ceWire.left
    oe = oeWire.left
    we = weWire.left

    a0C = a0Wire.right
    a1C = a1Wire.right
    a2C = a2Wire.right
    a3C = a3Wire.right
    a4C = a4Wire.right
    a5C = a5Wire.right
    a6C = a6Wire.right
    a7C = a7Wire.right
    a8C = a8Wire.right
    a9C = a9Wire.right
    a10C = a10Wire.right
    io0C = io0Wire.right
    io1C = io1Wire.right
    io2C = io2Wire.right
    io3C = io3Wire.right
    io4C = io4Wire.right
    io5C = io5Wire.right
    io6C = io6Wire.right
    io7C = io7Wire.right
    ceC = ceWire.right
    oeC = oeWire.right
    weC = weWire.right

    addressConnections = (Connection.wire().left, Connection.wire().left, Connection.wire().left, Connection.wire().left, Connection.wire().left, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)
    dataConnections = (io0, io1, io2, io3, io4, io5, io6, io7)
    eeprom = new Eeprom(a0C, a1C, a2C, a3C, a4C, a5C, a6C, a7C, a8C, a9C, a10C, io0C, io1C, io2C, io3C, io4C, io5C, io6C, io7C, ceC, oeC, weC)
  }

  Feature("EEProm") {

    Scenario("Read/Write") {
      ce.updateState(High)
      oe.updateState(High)
      we.updateState(Low)

      val dataAddress1 = overflowInt[Bit16](1587)
      val data1 = overflowInt[Bit8](127)

      val dataAddress2 = overflowInt[Bit16](123)
      val data2 = overflowInt[Bit8](7)

      dataAddress1.setConn(addressConnections)
      data1.setConn(dataConnections)
      ce.updateState(Low)
      ce.updateState(High)

      dataAddress2.setConn(addressConnections)
      data2.setConn(dataConnections)
      ce.updateState(Low)
      ce.updateState(High)

      //reset input connections
      overflowInt[Bit8]().setConn(dataConnections)
      we.updateState(High)

      //enable output
      oe.updateState(Low)

      dataAddress1.setConn(addressConnections)
      ce.updateState(Low)
      assert(dataConnections.toInt.value == data1.value)
      ce.updateState(High)

      dataAddress2.setConn(addressConnections)
      ce.updateState(Low)
      assert(dataConnections.toInt.value == data2.value)
      ce.updateState(High)


      //output data on high impedence when ce is high
      ce.updateState(High)
      assert(dataConnections.toInt.value == 0)

      //output data on high impedence when oe is high
      oe.updateState(High)
      ce.updateState(Low)
      assert(dataConnections.toInt.value == 0)

      //reacts to address change
      ce.updateState(Low)
      oe.updateState(Low)
      we.updateState(High)

      overflowInt[Bit16]().setConn(addressConnections)
      assert(dataConnections.toInt.value == 0)

      dataAddress1.setConn(addressConnections)
      assert(dataConnections.toInt.value == data1.value)

      dataAddress2.setConn(addressConnections)
      assert(dataConnections.toInt.value == data2.value)
    }

  }

}
