package bit8.simulation.components

import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.Connections16ToIntOverflow
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection.LOW
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire

//http://rtellason.com/chipdata/at28c16.pdf

class Eeprom(val a0: Connection, val a1: Connection, val a2: Connection, val a3: Connection, val a4: Connection, val a5: Connection, val a6: Connection, val a7: Connection, val a8: Connection, val a9: Connection, val a10: Connection,
             val io0: Connection, val io1: Connection, val io2: Connection, val io3: Connection, val io4: Connection, val io5: Connection, val io6: Connection, val io7: Connection,
             val ce: Connection,
             val oe: Connection,
             val we: Connection
            ) {

  private val capacity = 2048
  private val addressConnections = (LOW, LOW, LOW, LOW, LOW, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)
  private val dataConnections = (io0, io1, io2, io3, io4, io5, io6, io7)
  private var data: Vector[Int] = (0 to capacity).map(_ => 0).toVector

  private var address = overflowInt[Bit16]()

  addressConnections.productIterator.foreach {
    case c: Connection => c.wire.onNewState {
      case _ if ce.wire.isLow && oe.wire.isLow => outputData()
    }
  }

  ce.wire.onNewState {
    case Low if we.wire.isLow => latchAddress()
    case High if we.wire.isLow => writeData()
    case Low if oe.wire.isLow => outputData()
    case _ => resetOutput()
  }

  we.wire.onNewState {
    case Low if ce.wire.isLow => latchAddress()
    case High if ce.wire.isLow => writeData()
  }

  oe.wire.onNewState {
    case Low if ce.wire.isLow => outputData()
    case High => resetOutput()
  }

  private def latchAddress(): Unit = address = addressConnections.toInt
  private def writeData(): Unit = writeData(address.value, dataConnections.toInt.value)
  private def writeData(addr: Int, value: Int): Unit = data = data.updated(addr, value)
  private def outputData(): Unit = overflowInt[Bit8](data(addressConnections.toInt.value)).setConn(dataConnections)
  private def resetOutput(): Unit = overflowInt[Bit8]().setConn(dataConnections)
}

object Eeprom {

  def writeData(eeprom: Eeprom, data: Map[Int, Int]): Unit = {
    data.foreach { case (addr, value) =>
      eeprom.writeData(addr, value)
    }

    eeprom.outputData()
  }

}
