package bit8.simulation.modules

import bit8.simulation.components.Socket.Socket
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.wire.{Connection, High, Low}

class OutputWatcher(clk: Connection,
                    outEnabled: Connection,
                    outputRead: Connection,
                    busValue: Socket) {

  private var result = 0
  clk.wire.onNewState({
    case High if outEnabled.wire.isHigh => {
      result = busValue.toInt.value
      outputRead.updateState(High)
      outputRead.updateState(Low)
    }
  })

  def getResult() = result

}
