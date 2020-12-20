package bit8.simulation.components

import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.State

object And {

  def apply(in1: Connection, in2: Connection): Connection = {
    val out = Connection.wire()

    val updateOutputState: PartialFunction[State, Unit] = {
      case _ => out.left.updateState(calculateState(in1.wire.getState(), in2.wire.getState()))
    }

    out.left.updateState(calculateState(in1.wire.getState(), in2.wire.getState()))
    in1.wire.onNewState(updateOutputState)
    in2.wire.onNewState(updateOutputState)

    out.right
  }

  def calculateState(state1: State, state2: State): State = (state1, state2) match {
    case (High, High) => High
    case (High, Low) => Low
    case (Low, High) => Low
    case (Low, Low) => Low
  }

}
