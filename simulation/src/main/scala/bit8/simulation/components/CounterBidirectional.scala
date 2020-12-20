package bit8.simulation.components

import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.IntegerWithOverflow.Bit4
import bit8.simulation.components.utils.IntegerWithOverflow.overflowInt
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.utils.Utils._
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low

/**
 * https://pdf1.alldatasheet.com/datasheet-pdf/view/5666/MOTOROLA/SN54LS168.html
 * @param tc - Terminal Count (Active LOW) Output
 * @param cet - Count Enable Trickle (Active LOW) Input
 * @param pe - Parallel Enable (Active LOW) Input
 * @param ud - Up-Down Count Control Input
 * @param cp - Clock Pulse (Active positive going edge) Input
 * @param cep - Count Enable Parallel (Active LOW) Input
 * @param q0 - output
 * @param q1 - output
 * @param q2 - output
 * @param q3 - output
 * @param p0 - input
 * @param p1 - input
 * @param p2 - input
 * @param p3 - input
 */
class CounterBidirectional(val tc: Connection,
                           val cet: Connection,
                           val pe: Connection,
                           val ud: Connection,
                           val cp: Connection,
                           val cep: Connection,
                           val q0: Connection,
                           val q1: Connection,
                           val q2: Connection,
                           val q3: Connection,
                           val p0: Connection,
                           val p1: Connection,
                           val p2: Connection,
                           val p3: Connection,
                          ) {

  private val inputConnections = (p0, p1, p2, p3)
  private val outputConnections = (q0, q1, q2, q3)
  private var state: IntegerWithOverflow[Bit4] = overflowInt[Bit4]()

  cp.wire.onNewState { case _ => update() }
  cet.wire.onNewState { case _ => update() }

  tc.updateState(High)

  def update(): Unit = {
    if (cp.wire.isHigh) {
      if (pe.wire.isLow) {
        loadState()
        outputState()
        tc.updateState(High)
      } else if (cep.wire.isLow && cet.wire.isLow) {
        if (ud.wire.isHigh) {
          increment()
          if (state.value == minValue[Bit4].value) {
            tc.updateState(Low)
          } else {
            tc.updateState(High)
          }
          outputState()
        } else {
          decrement()
          if (state.value == maxValue[Bit4].value) {
            tc.updateState(Low)
          } else {
            tc.updateState(High)
          }
          outputState()
        }
      } else {
        tc.updateState(High)
      }
    }
  }

  private def loadState(): Unit = {
    state = inputConnections.toInt
  }

  private def outputState(): Unit = {
    state.setConn(outputConnections)
  }

  private def increment(): Unit = state = state.increment

  private def decrement(): Unit = state = state.decrement

}
