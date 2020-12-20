package simulation.modules

import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import bit8.simulation.modules.Counter16Bit
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import bit8.simulation.components.utils.Utils._

class Counter16BitSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var io0, io0C: Connection = _
  var io1, io1C: Connection = _
  var io2, io2C: Connection = _
  var io3, io3C: Connection = _
  var io4, io4C: Connection = _
  var io5, io5C: Connection = _
  var io6, io6C: Connection = _
  var io7, io7C: Connection = _
  var io8, io8C: Connection = _
  var io9, io9C: Connection = _
  var io10, io10C: Connection = _
  var io11, io11C: Connection = _
  var io12, io12C: Connection = _
  var io13, io13C: Connection = _
  var io14, io14C: Connection = _
  var io15, io15C: Connection = _
  var directOut0, directOut0C: Connection = _
  var directOut1, directOut1C: Connection = _
  var directOut2, directOut2C: Connection = _
  var directOut3, directOut3C: Connection = _
  var directOut4, directOut4C: Connection = _
  var directOut5, directOut5C: Connection = _
  var directOut6, directOut6C: Connection = _
  var directOut7, directOut7C: Connection = _
  var directOut8, directOut8C: Connection = _
  var directOut9, directOut9C: Connection = _
  var directOut10, directOut10C: Connection = _
  var directOut11, directOut11C: Connection = _
  var directOut12, directOut12C: Connection = _
  var directOut13, directOut13C: Connection = _
  var directOut14, directOut14C: Connection = _
  var directOut15, directOut15C: Connection = _

  var clk, clkC: Connection = _
  var ce, ceC: Connection = _
  var hIn, hInC: Connection = _
  var hOut, hOutC: Connection = _
  var lIn, lInC: Connection = _
  var lOut, lOutC: Connection = _

  var ioConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var directOutConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var counter: Counter16Bit = _

  before {
    val io0Wire = Connection.wire()
    val io1Wire = Connection.wire()
    val io2Wire = Connection.wire()
    val io3Wire = Connection.wire()
    val io4Wire = Connection.wire()
    val io5Wire = Connection.wire()
    val io6Wire = Connection.wire()
    val io7Wire = Connection.wire()
    val io8Wire = Connection.wire()
    val io9Wire = Connection.wire()
    val io10Wire = Connection.wire()
    val io11Wire = Connection.wire()
    val io12Wire = Connection.wire()
    val io13Wire = Connection.wire()
    val io14Wire = Connection.wire()
    val io15Wire = Connection.wire()
    val directOut0Wire = Connection.wire()
    val directOut1Wire = Connection.wire()
    val directOut2Wire = Connection.wire()
    val directOut3Wire = Connection.wire()
    val directOut4Wire = Connection.wire()
    val directOut5Wire = Connection.wire()
    val directOut6Wire = Connection.wire()
    val directOut7Wire = Connection.wire()
    val directOut8Wire = Connection.wire()
    val directOut9Wire = Connection.wire()
    val directOut10Wire = Connection.wire()
    val directOut11Wire = Connection.wire()
    val directOut12Wire = Connection.wire()
    val directOut13Wire = Connection.wire()
    val directOut14Wire = Connection.wire()
    val directOut15Wire = Connection.wire()
    val clkWire = Connection.wire()
    val coWire = Connection.wire()
    val jWire = Connection.wire()
    val ceWire = Connection.wire()
    val hInWire = Connection.wire()
    val hOutWire = Connection.wire()
    val lInWire = Connection.wire()
    val lOutWire = Connection.wire()

    io0 = io0Wire.left
    io1 = io1Wire.left
    io2 = io2Wire.left
    io3 = io3Wire.left
    io4 = io4Wire.left
    io5 = io5Wire.left
    io6 = io6Wire.left
    io7 = io7Wire.left
    io8 = io8Wire.left
    io9 = io9Wire.left
    io10 = io10Wire.left
    io11 = io11Wire.left
    io12 = io12Wire.left
    io13 = io13Wire.left
    io14 = io14Wire.left
    io15 = io15Wire.left
    directOut0 = directOut0Wire.left
    directOut1 = directOut1Wire.left
    directOut2 = directOut2Wire.left
    directOut3 = directOut3Wire.left
    directOut4 = directOut4Wire.left
    directOut5 = directOut5Wire.left
    directOut6 = directOut6Wire.left
    directOut7 = directOut7Wire.left
    directOut8 = directOut8Wire.left
    directOut9 = directOut9Wire.left
    directOut10 = directOut10Wire.left
    directOut11 = directOut11Wire.left
    directOut12 = directOut12Wire.left
    directOut13 = directOut13Wire.left
    directOut14 = directOut14Wire.left
    directOut15 = directOut15Wire.left
    clk = clkWire.left
    ce = ceWire.left
    hIn = hInWire.left
    hOut = hOutWire.left
    lIn = lInWire.left
    lOut = lOutWire.left

    io0C = io0Wire.right
    io1C = io1Wire.right
    io2C = io2Wire.right
    io3C = io3Wire.right
    io4C = io4Wire.right
    io5C = io5Wire.right
    io6C = io6Wire.right
    io7C = io7Wire.right
    io8C = io8Wire.right
    io9C = io9Wire.right
    io10C = io10Wire.right
    io11C = io11Wire.right
    io12C = io12Wire.right
    io13C = io13Wire.right
    io14C = io14Wire.right
    io15C = io15Wire.right
    directOut0C = directOut0Wire.right
    directOut1C = directOut1Wire.right
    directOut2C = directOut2Wire.right
    directOut3C = directOut3Wire.right
    directOut4C = directOut4Wire.right
    directOut5C = directOut5Wire.right
    directOut6C = directOut6Wire.right
    directOut7C = directOut7Wire.right
    directOut8C = directOut8Wire.right
    directOut9C = directOut9Wire.right
    directOut10C = directOut10Wire.right
    directOut11C = directOut11Wire.right
    directOut12C = directOut12Wire.right
    directOut13C = directOut13Wire.right
    directOut14C = directOut14Wire.right
    directOut15C = directOut15Wire.right
    clkC = clkWire.right
    ceC = ceWire.right
    hInC = hInWire.right
    hOutC = hOutWire.right
    lInC = lInWire.right
    lOutC = lOutWire.right

    ioConnections = (io0, io1, io2, io3, io4, io5, io6, io7)
    directOutConnections = (directOut0, directOut1, directOut2, directOut3, directOut4, directOut5, directOut6, directOut7, directOut8, directOut9, directOut10, directOut11, directOut12, directOut13, directOut14, directOut15)
    counter = new Counter16Bit(io0C, io1C, io2C, io3C, io4C, io5C, io6C, io7C, directOut0, directOut1, directOut2, directOut3, directOut4, directOut5, directOut6, directOut7, directOut8, directOut9, directOut10, directOut11, directOut12, directOut13, directOut14, directOut15, clkC, hInC, hOutC, lInC, lOutC, ceC)
  }

  Feature("Counter 16 Bit") {

    Scenario("Increments") {
      val iterations = (1 to maxValue[Bit16].value).toList :+ 0
      iterations.foreach(expected => {
        ce.updateState(High)

        clk.updateState(High)
        clk.updateState(Low)

        ce.updateState(Low)

        assert(directOutConnections.toInt.value == expected)
      })
    }

    Scenario("Writes/Reads lower byte value") {
      val value = overflowInt[Bit8](123)
      value.setConn(ioConnections)

      lIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      lIn.updateState(Low)

      minValue[Bit8].setConn(ioConnections)
      assert(ioConnections.toInt.value == 0)
      assert(directOutConnections.toInt.value == value.value)

      lOut.updateState(High)
      assert(ioConnections.toInt.value == value.value)
      assert(directOutConnections.toInt.value == value.value)
    }

    Scenario("Writes/Reads higher byte value") {
      val value = overflowInt[Bit8](123)
      value.setConn(ioConnections)

      hIn.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      hIn.updateState(Low)

      minValue[Bit8].setConn(ioConnections)
      assert(ioConnections.toInt.value == 0)
      assert(directOutConnections.toInt.value == 31488)

      hOut.updateState(High)
      assert(ioConnections.toInt.value == value.value)
      assert(directOutConnections.toInt.value == 31488)
    }

  }

}
