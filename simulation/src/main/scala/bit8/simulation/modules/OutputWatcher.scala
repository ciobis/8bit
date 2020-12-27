package bit8.simulation.modules

import bit8.simulation.components.Socket.Socket
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.wire.{Connection, High, Low}

class OutputWatcher(outEnabled: Connection,
                    outputRead: Connection,
                    busValue: Socket,
                    onValue: (Int) => Unit = (_) => ()) {

  private var result = 0
  outEnabled.wire.onNewState({
    case High => {
      result = busValue.toInt.value
      outputRead.updateState(High)
      outputRead.updateState(Low)
      onValue(result)
    }
  })

  def getResult() = result

}
