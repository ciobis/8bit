package simulation.modules

import bit8.simulation.components.utils.IntegerWithOverflow.Bit16
import bit8.simulation.components.utils.IntegerWithOverflow.Bit8
import bit8.simulation.components.utils.IntegerWithOverflow.maxValue
import bit8.simulation.components.wire.Connection
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.modules.StackCounter

class StackCounterSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var io0, io0C: Connection = _
  var io1, io1C: Connection = _
  var io2, io2C: Connection = _
  var io3, io3C: Connection = _
  var io4, io4C: Connection = _
  var io5, io5C: Connection = _
  var io6, io6C: Connection = _
  var io7, io7C: Connection = _

  var clk, clkC: Connection = _
  var cu, cuC: Connection = _
  var cd, cdC: Connection = _
  var out, outC: Connection = _

  var ioConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var counter: StackCounter = _

  before {
    val io0Wire = Connection.wire()
    val io1Wire = Connection.wire()
    val io2Wire = Connection.wire()
    val io3Wire = Connection.wire()
    val io4Wire = Connection.wire()
    val io5Wire = Connection.wire()
    val io6Wire = Connection.wire()
    val io7Wire = Connection.wire()

    val clkWire = Connection.wire()
    val cuWire = Connection.wire()
    val cdWire = Connection.wire()
    val outWire = Connection.wire()

    io0 = io0Wire.left
    io1 = io1Wire.left
    io2 = io2Wire.left
    io3 = io3Wire.left
    io4 = io4Wire.left
    io5 = io5Wire.left
    io6 = io6Wire.left
    io7 = io7Wire.left

    io0C = io0Wire.right
    io1C = io1Wire.right
    io2C = io2Wire.right
    io3C = io3Wire.right
    io4C = io4Wire.right
    io5C = io5Wire.right
    io6C = io6Wire.right
    io7C = io7Wire.right

    clk = clkWire.left
    cu = cuWire.left
    cd = cdWire.left
    out = outWire.left
    clkC =  clkWire.right
    cuC = cuWire.right
    cdC = cdWire.right
    outC =  outWire.right

    ioConnections = (io0, io1, io2, io3, io4, io5, io6, io7)
    counter = new StackCounter(
      clk, cu, cd, out,
      io0C, io1C, io2C, io3C, io4C, io5C, io6C, io7C
    )
  }

  Feature("Bidirectional counter") {
    Scenario("Increments") {
      val iterations = (1 to maxValue[Bit8].value).toList :+ 0
      iterations.foreach(expected => {
        cu.updateState(High)
        clk.updateState(High)

        clk.updateState(Low)
        cu.updateState(Low)

        out.updateState(High)
        clk.updateState(High)
        assert(ioConnections.toInt.value == expected)
        out.updateState(Low)
        clk.updateState(Low)
      })
    }

    Scenario("Decrements") {
      val iterations = maxValue[Bit8].value to 0 by -1
      iterations.foreach(expected => {
        cd.updateState(High)
        clk.updateState(High)

        clk.updateState(Low)
        cd.updateState(Low)

        out.updateState(High)
        clk.updateState(High)
        assert(ioConnections.toInt.value == expected)
        out.updateState(Low)
        clk.updateState(Low)
      })
    }

    Scenario("Output") {
      cu.updateState(High)
      clk.updateState(High)
      clk.updateState(Low)
      cu.updateState(Low)

      assert(ioConnections.toInt.value == 0)

      out.updateState(High)
      clk.updateState(High)
      assert(ioConnections.toInt.value == 1)

      clk.updateState(Low)
      assert(ioConnections.toInt.value == 0)
    }
  }

}
