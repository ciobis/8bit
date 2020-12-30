package bit8.simulation.modules

import bit8.simulation.components.{And, Inverter, RSFlipFlop}
import bit8.simulation.components.wire.{Branch, Connection, High, Join, Low}

class ClockModule(val cycleNanos: Long,
                  val clk: Connection,
                  val outputEnabled: Connection,
                  val outputReady: Connection,
                  val outputRead: Connection,
                  val halt: Connection) {

  def start(onHigh: () => Unit = () => ()): Unit = {
    val (clkSet, clkProbe) = Branch.branch(clk)

    val waitReadWire = Connection.wire()
    val waitingRead = RSFlipFlop(waitReadWire.left, outputRead)
    val clkLow = Inverter(clkProbe)
    Join(waitReadWire.right, clkLow)

    val outputEnabledAndWaitingRead = And(And(outputEnabled, clkProbe), waitingRead)
    Join(outputEnabledAndWaitingRead, outputReady)

    while (halt.wire.isLow) {
      clkSet.updateState(High)
      onHigh()
      if (cycleNanos > 0) {
        waitNanos(cycleNanos)
      }

      while (outputEnabledAndWaitingRead.wire.isHigh) {
        waitNanos(10)
      }

      clkSet.updateState(Low)
      if (cycleNanos > 0) {
        waitNanos(cycleNanos)
      }
    }
  }

  private def waitNanos(nanos: Long): Unit = {
    val finishOn = System.nanoTime() + nanos
    while (finishOn > System.nanoTime()) {}
  }

}
