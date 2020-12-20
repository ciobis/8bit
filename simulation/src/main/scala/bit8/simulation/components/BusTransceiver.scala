package bit8.simulation.components

import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils._
import bit8.simulation.components.wire.Connection


//https://pdf1.alldatasheet.com/datasheet-pdf/view/177340/TI/LS245.html
/**
 *
 * @param dir
 * @param oe - output enable. active low
 */
class BusTransceiver(val dir: Connection,
                     val oe: Connection,
                     val ioA1: Connection, val ioA2: Connection, val ioA3: Connection, val ioA4: Connection, val ioA5: Connection, val ioA6: Connection, val ioA7: Connection, val ioA8: Connection,
                     val ioB1: Connection, val ioB2: Connection, val ioB3: Connection, val ioB4: Connection, val ioB5: Connection, val ioB6: Connection, val ioB7: Connection, val ioB8: Connection
                    ) {

  private val ioAConnections = (ioA1, ioA2, ioA3, ioA4, ioA5, ioA6, ioA7, ioA8)
  private val ioBConnections = (ioB1, ioB2, ioB3, ioB4, ioB5, ioB6, ioB7, ioB8)
  private val neutral = minValue[Bit8]

  oe.wire.onNewState { case _ => update() }
  dir.wire.onNewState { case _ => update() }

  private def update(): Unit =
    if (oe.wire.isHigh) {
      neutral.setConn(ioAConnections)
      neutral.setConn(ioBConnections)
    } else {
      if (dir.wire.isHigh) {
        val aValue = ioAConnections.toInt
        aValue.setConn(ioBConnections)
      } else {
        val bValue = ioBConnections.toInt
        bValue.setConn(ioAConnections)
      }
    }


}
