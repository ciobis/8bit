package bit8.simulation.components

import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.State

object Or {

  def apply(in1: Connection, in2: Connection): Connection = {
    val wire = Connection.wire()

    val calclulateNewState: PartialFunction[State, Unit] = {
      case _ => wire.left.updateState(calculateState(in1.wire.getState(), in2.wire.getState()))
    }

    wire.left.updateState(calculateState(in1.wire.getState(), in2.wire.getState()))
    in1.wire.onNewState(calclulateNewState)
    in2.wire.onNewState(calclulateNewState)

    wire.right
  }

  def calculateState(state1: State, state2: State): State = (state1, state2) match {
    case (High, High) => High
    case (High, Low) => High
    case (Low, High) => High
    case (Low, Low) => Low
  }

}
