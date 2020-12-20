package bit8.simulation.modules

import bit8.simulation.components.Eeprom
import bit8.simulation.components.Inverter
import bit8.simulation.components.Socket.Socket
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection.LOW
import bit8.simulation.components.wire.Connection.HIGH

/**
 * Read only module
 * @param oe - output enable. active high
 */
class EepromModule(
                    val a0: Connection, val a1: Connection, val a2: Connection, val a3: Connection, val a4: Connection, val a5: Connection, val a6: Connection, val a7: Connection, val a8: Connection, val a9: Connection, val a10: Connection,
                    val io0: Connection, val io1: Connection, val io2: Connection, val io3: Connection, val io4: Connection, val io5: Connection, val io6: Connection, val io7: Connection,
                    val oe: Connection
                  ) {

  private val oeInverted = Inverter(oe)

  val eeprom = new Eeprom(
    a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10,
    io0, io1, io2, io3, io4, io5, io6, io7,
    LOW, oeInverted, HIGH
  )

  def withData(data: Map[Int, Int]): Unit = {
    Eeprom.writeData(eeprom, data)
  }

}

object EepromModule {

  def apply(a0: Connection, a1: Connection, a2: Connection, a3: Connection, a4: Connection, a5: Connection, a6: Connection, a7: Connection, a8: Connection, a9: Connection, a10: Connection,
            ioSocket: Socket, oe: Connection): EepromModule = {

    new EepromModule(
      a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10,
      ioSocket._1, ioSocket._2, ioSocket._3, ioSocket._4, ioSocket._5, ioSocket._6, ioSocket._7, ioSocket._8,
      oe
    )

  }

}
