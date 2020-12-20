package bit8.simulation.components

import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low

object RSFlipFlop {

  def apply(set: Connection, reset: Connection): Connection = {
    val output = Connection.wire()

    set.wire.onNewState { case High => output.left.updateState(High)}
    reset.wire.onNewState { case High => output.left.updateState(Low)}

    output.right
  }

}
