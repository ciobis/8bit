package bit8.simulation.modules

import bit8.simulation.components.{And, Inverter, RSFlipFlop}
import bit8.simulation.components.wire.{Connection, High, Low}

class ClockModule(val timeMs: Int,
                  val clk: Connection,
                  val outputEnabled: Connection,
                  val outputRead: Connection,
                  val halt: Connection) {

  def start(onHigh: () => Unit = () => ()): Unit = {

    val waitReadWire = Connection.wire()
    val waitingRead = RSFlipFlop(waitReadWire.left, outputRead)
    val outputEnabledAndWaitingRead = And(outputEnabled, waitingRead)

    while (halt.wire.isLow) {
      waitReadWire.right.updateState(Low)
      clk.updateState(High)
      onHigh()
      if (timeMs > 0) {
        Thread.sleep(timeMs)
      }

      while (outputEnabledAndWaitingRead.wire.isHigh) {
        Thread.sleep(1)
      }

      clk.updateState(Low)
      waitReadWire.right.updateState(High)
      if (timeMs > 0) {
        Thread.sleep(timeMs)
      }
    }
  }

}
