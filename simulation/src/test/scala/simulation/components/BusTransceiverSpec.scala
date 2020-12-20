package simulation.components

import bit8.simulation.components.BusTransceiver
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils._
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class BusTransceiverSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter  {

  var dir, dirC: Connection = _
  var oe, oeC: Connection = _
  var ioA1, ioA1C: Connection = _
  var ioA2, ioA2C: Connection = _
  var ioA3, ioA3C: Connection = _
  var ioA4, ioA4C: Connection = _
  var ioA5, ioA5C: Connection = _
  var ioA6, ioA6C: Connection = _
  var ioA7, ioA7C: Connection = _
  var ioA8, ioA8C: Connection = _
  var ioB1, ioB1C: Connection = _
  var ioB2, ioB2C: Connection = _
  var ioB3, ioB3C: Connection = _
  var ioB4, ioB4C: Connection = _
  var ioB5, ioB5C: Connection = _
  var ioB6, ioB6C: Connection = _
  var ioB7, ioB7C: Connection = _
  var ioB8, ioB8C: Connection = _

  var ioAConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var ioBConnections: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _

  before {
    val dirWire = Connection.wire()
    val oeWire = Connection.wire()
    val ioA1Wire = Connection.wire()
    val ioA2Wire = Connection.wire()
    val ioA3Wire = Connection.wire()
    val ioA4Wire = Connection.wire()
    val ioA5Wire = Connection.wire()
    val ioA6Wire = Connection.wire()
    val ioA7Wire = Connection.wire()
    val ioA8Wire = Connection.wire()
    val ioB1Wire = Connection.wire()
    val ioB2Wire = Connection.wire()
    val ioB3Wire = Connection.wire()
    val ioB4Wire = Connection.wire()
    val ioB5Wire = Connection.wire()
    val ioB6Wire = Connection.wire()
    val ioB7Wire = Connection.wire()
    val ioB8Wire = Connection.wire()

    dir = dirWire.left
    oe = oeWire.left
    ioA1 = ioA1Wire.left
    ioA2 = ioA2Wire.left
    ioA3 = ioA3Wire.left
    ioA4 = ioA4Wire.left
    ioA5 = ioA5Wire.left
    ioA6 = ioA6Wire.left
    ioA7 = ioA7Wire.left
    ioA8 = ioA8Wire.left
    ioB1 = ioB1Wire.left
    ioB2 = ioB2Wire.left
    ioB3 = ioB3Wire.left
    ioB4 = ioB4Wire.left
    ioB5 = ioB5Wire.left
    ioB6 = ioB6Wire.left
    ioB7 = ioB7Wire.left
    ioB8 = ioB8Wire.left

    dirC = dirWire.right
    oeC = oeWire.right
    ioA1C = ioA1Wire.right
    ioA2C = ioA2Wire.right
    ioA3C = ioA3Wire.right
    ioA4C = ioA4Wire.right
    ioA5C = ioA5Wire.right
    ioA6C = ioA6Wire.right
    ioA7C = ioA7Wire.right
    ioA8C = ioA8Wire.right
    ioB1C = ioB1Wire.right
    ioB2C = ioB2Wire.right
    ioB3C = ioB3Wire.right
    ioB4C = ioB4Wire.right
    ioB5C = ioB5Wire.right
    ioB6C = ioB6Wire.right
    ioB7C = ioB7Wire.right
    ioB8C = ioB8Wire.right

    ioAConnections = (ioA1, ioA2, ioA3, ioA4, ioA5, ioA6, ioA7, ioA8)
    ioBConnections = (ioB1, ioB2, ioB3, ioB4, ioB5, ioB6, ioB7, ioB8)
    new BusTransceiver(dirC, oeC, ioA1C, ioA2C, ioA3C, ioA4C, ioA5C, ioA6C, ioA7C, ioA8C, ioB1C, ioB2C, ioB3C, ioB4C, ioB5C, ioB6C, ioB7C, ioB8C)
  }

  Feature("Bus transceiver") {
    Scenario("Isolation when oe is high. A to B") {
      val value = overflowInt[Bit8](123)

      dir.updateState(High)
      oe.updateState(Low)
      value.setConn(ioAConnections)
      oe.updateState(High)

      assert(ioBConnections.toInt.value == 0)
    }

    Scenario("Isolation when oe is high. B to A") {
      val value = overflowInt[Bit8](123)

      dir.updateState(Low)
      oe.updateState(Low)
      value.setConn(ioBConnections)
      oe.updateState(High)

      assert(ioAConnections.toInt.value == 0)
    }

    Scenario("From A to B") {
      val neutral = minValue[Bit8]
      val value = overflowInt[Bit8](123)

      dir.updateState(High)
      oe.updateState(High)
      value.setConn(ioAConnections)
      neutral.setConn(ioBConnections)

      oe.updateState(Low)
      assert(ioBConnections.toInt.value == value.value)
    }

    Scenario("From B to A") {
      val neutral = minValue[Bit8]
      val value = overflowInt[Bit8](123)

      dir.updateState(Low)
      oe.updateState(High)
      value.setConn(ioBConnections)
      neutral.setConn(ioAConnections)

      oe.updateState(Low)
      assert(ioAConnections.toInt.value == value.value)
    }
  }


}
