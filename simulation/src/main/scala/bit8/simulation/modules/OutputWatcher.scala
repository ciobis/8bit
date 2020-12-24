package bit8.simulation.modules

import bit8.simulation.components.{And, Inverter, RSFlipFlop}
import bit8.simulation.components.Socket.Socket
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.wire.{Connection, High, Low}

import scala.concurrent.Future

object OutputWatcher {

  import scala.concurrent.ExecutionContext.Implicits.global

  def apply(clk: Connection, halt: Connection, outEnabled: Connection, outputRead: Connection, busValue: Socket): Future[Int] = Future {
    var result = 0
    val markReadWire = Connection.wire()
    val notReadYet = RSFlipFlop(Inverter(clk), markReadWire.left)
    val notReadYetAndOutEnabled = And(notReadYet, outEnabled)

    while (halt.wire.isLow) {
      if (notReadYetAndOutEnabled.wire.isHigh) {
        result = busValue.toInt.value
        outputRead.updateState(High)
        outputRead.updateState(Low)
        markReadWire.right.updateState(High)
        markReadWire.right.updateState(Low)
      } else {
        Thread.sleep(1)
      }
    }

    result
  }

}
