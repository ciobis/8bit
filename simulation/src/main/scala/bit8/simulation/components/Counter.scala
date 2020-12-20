package bit8.simulation.components

import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.utils.Utils._

//https://pdf1.alldatasheet.com/datasheet-pdf/view/5667/MOTOROLA/SN74LS161N.html
//http://www.ti.com/lit/ds/symlink/sn54ls161a-sp.pdf
//https://www.calstatela.edu/sites/default/files/groups/Department%20of%20Electrical%20and%20Computer%20Engineering/labs/74161.pdf

/**
 *
 * @param tc - terminal count output. outputs high on overflow
 * @param cet - count enable t. active high
 * @param pe - parallel enable. active low. reads value from inputs
 * @param cp - clock. active high.
 * @param cep - count enable p. active high
 * @param sr - reset. active low
 * @param q0 - output
 * @param q1 - output
 * @param q2 - output
 * @param q3 - output
 * @param p0 - input
 * @param p1 - input
 * @param p2 - input
 * @param p3 - input
 */
class Counter(val tc: Connection,
              val cet: Connection,
              val pe: Connection,
              val cp: Connection,
              val cep: Connection,
              val sr: Connection,
              val q0: Connection,
              val q1: Connection,
              val q2: Connection,
              val q3: Connection,
              val p0: Connection,
              val p1: Connection,
              val p2: Connection,
              val p3: Connection) {

  private val inputConnections = (p0, p1, p2, p3)
  private val outputConnections = (q0, q1, q2, q3)
  private var state: IntegerWithOverflow[Bit4] = overflowInt[Bit4]()

  cp.wire.onNewState { case _ => update() }
  cet.wire.onNewState { case _ => update() }

  def update(): Unit = {
    if (cp.wire.isHigh) {
      if (sr.wire.isLow) {
        reset()
        outputState()
      } else if (pe.wire.isLow) {
        loadState()
        outputState()

        if (state.value == minValue[Bit4].value) {
          tc.updateState(Low)
        }
      } else if (cep.wire.isHigh && cet.wire.isHigh) {
        if (state.value == minValue[Bit4].value) {
          tc.updateState(Low)
        }
        if (state.value == maxValue[Bit4].value) {
          tc.updateState(High)
        }

        incrementAndOutput()
      } else if (state.value == minValue[Bit4].value) {
        tc.updateState(Low)
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

  private def reset(): Unit = state = minValue[Bit4]

  private def incrementAndOutput(): Unit = {
    increment()
    outputState()
  }

  def getSate: IntegerWithOverflow[Bit4] = state


}
