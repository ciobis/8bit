package bit8.simulation.components

import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.Connections4ToIntOverflow
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Ground
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low

/**
 * http://www.ti.com/lit/ds/symlink/sn54173.pdf
 *
 * @param oe - output enable. active low
 * @param de - data enable. active low
 * @param clk - clock
 * @param q0
 * @param q1
 * @param q2
 * @param q3
 * @param d0
 * @param d1
 * @param d2
 * @param d3
 */
class Register4Bit(val oe: Connection,
                   val de: Connection,
                   val clk: Connection,
                   val q0: Connection,
                   val q1: Connection,
                   val q2: Connection,
                   val q3: Connection,
                   val d0: Connection,
                   val d1: Connection,
                   val d2: Connection,
                   val d3: Connection) {

  private val dataInputs = (q0, q1, q2, q3)
  private val dataOutputs = (d0, d1, d2, d3)

  private val zeroState = minValue[Bit4]
  private var state = zeroState

  oe.wire.onNewState {
    case _ => outputControl()
  }

  clk.wire.onNewState {
    case High if de.wire.isLow =>
      state = dataInputs.toInt
      outputControl()
  }

  private def outputControl(): Unit = oe.wire.getState() match {
    case High => zeroState.setConn(dataOutputs)
    case Low | Ground => state.setConn(dataOutputs)
  }

  def getState: IntegerWithOverflow[Bit4] = state

}
