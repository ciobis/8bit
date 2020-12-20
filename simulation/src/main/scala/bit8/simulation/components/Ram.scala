package bit8.simulation.components

import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.Connections16ToIntOverflow
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.wire.Connection

//https://pdf1.alldatasheet.com/datasheet-pdf/view/47639/WINBOND/W24512AK-15.html


class Ram(val a0: Connection, val a1: Connection, val a2: Connection, val a3: Connection,
          val a4: Connection, val a5: Connection, val a6: Connection, val a7: Connection,
          val a8: Connection, val a9: Connection, val a10: Connection, val a11: Connection,
          val a12: Connection, val a13: Connection, val a14: Connection, val a15: Connection,
          val io0: Connection, val io1: Connection, val io2: Connection, val io3: Connection,
          val io4: Connection, val io5: Connection, val io6: Connection, val io7: Connection,
          val cs1: Connection, val cs2: Connection,
          val we: Connection,
          val oe: Connection
         ) {

  private val addressConns = (a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)
  private val dataConns = (io0, io1, io2, io3, io4, io5, io6, io7)
  var data = (0 to maxValue[Bit16].value).map(_ => 0).toVector

  oe.wire.onNewState { case _ =>
    updateState()
  }
  we.wire.onNewState { case _ =>
    updateState()
  }
  cs1.wire.onNewState { case _ =>
    updateState()
  }
  cs2.wire.onNewState { case _ =>
    updateState()
  }

  private def updateState(): Unit = {
    if (enabled) {
      if (oe.wire.isHigh && we.wire.isHigh) {
        updateOutput(0)
      } else if (oe.wire.isLow) {
        updateOutput(data(addressValue))
      } else if (we.wire.isLow) {
        data = data.updated(addressValue, dataValue)
      }
    } else {
      updateOutput(0)
    }
  }

  private def updateOutput(value: Int): Unit = overflowInt[Bit8](value).setConn(dataConns)

  private def addressValue: Int = addressConns.toInt.value

  private def dataValue: Int = dataConns.toInt.value

  private def enabled: Boolean = cs1.wire.isLow && cs2.wire.isHigh
}
