package bit8.simulation.components

import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Ground
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.State
import bit8.simulation.components.wire.Wire

object Inverter {

  def apply(input: Connection): Connection = {
    val wire = Connection.wire()
    input.wire.onNewState {
      case newState => wire.left.updateState(negate(newState))
    }
    wire.left.updateState(negate(input.wire.getState()))
    wire.right
  }

  private def negate(state: State): State = state match {
    case High => Low
    case Low => High
    case Ground => High
  }

}
