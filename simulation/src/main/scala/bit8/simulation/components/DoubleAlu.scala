package bit8.simulation.components

import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.State
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.utils.Utils.Connections4ToIntOverflow
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow


class DoubleAlu(val a0: Connection,
                val a1: Connection,
                val a2: Connection,
                val a3: Connection,
                val a4: Connection,
                val a5: Connection,
                val a6: Connection,
                val a7: Connection,
                val b0: Connection,
                val b1: Connection,
                val b2: Connection,
                val b3: Connection,
                val b4: Connection,
                val b5: Connection,
                val b6: Connection,
                val b7: Connection,
                val f0: Connection,
                val f1: Connection,
                val f2: Connection,
                val f3: Connection,
                val f4: Connection,
                val f5: Connection,
                val f6: Connection,
                val f7: Connection,
                val s0: Connection,
                val s1: Connection,
                val s2: Connection,
                val s3: Connection,
                val m: Connection,
                val cn: Connection,
                val cn4: Connection,
                val ab: Connection
               ) {

  private val inputAConnections = (a0, a1, a2, a3, a4, a5, a6, a7)
  private val inputBConnections = (b0, b1, b2, b3, b4, b5, b6, b7)
  private val functionSelectConnections = (s0, s1, s2, s3)
  private val inputs: Seq[Connection] = (inputAConnections.productIterator.toSeq ++ inputBConnections.productIterator.toSeq ++ functionSelectConnections.productIterator.toSeq).asInstanceOf[Seq[Connection]]
  private val outputConnections = (f0, f1, f2, f3, f4, f5, f6, f7)

  private val calculateOutput: PartialFunction[State, Unit] = {
    case _ => functionSelectConnections.toInt.toBits match {
      case (true, false, false, true) if m.wire.isLow && cn.wire.isHigh => {
        val a = inputAConnections.toInt
        val b = inputBConnections.toInt

        val (result, carry) = a.plusWithCarry(b)

        result.setConn(outputConnections)
        cn4.updateState(State.fromBoolean(carry))
      }

      case (false, true, true, false) if m.wire.isLow && cn.wire.isLow => {
        val a = inputAConnections.toInt
        val b = inputBConnections.toInt

        val (result, carry) = a.minusWithCarry(b)

        result.setConn(outputConnections)
        cn4.updateState(State.fromBoolean(carry))
      }

      case (false, true, true, false) if m.wire.isLow && cn.wire.isHigh => {
        val a = inputAConnections.toInt
        val b = inputBConnections.toInt

        val (result, carry) = a.minus(overflowInt[Bit8](1)).minusWithCarry(b)
        result.setConn(outputConnections)

        ab.updateState(State.fromBoolean(a == b))
        cn4.updateState(State.fromBoolean(carry))
      }
      case _ =>
    }
  }

  inputs.foreach(_.wire.onNewState(calculateOutput))
  Seq(m, cn).foreach(_.wire.onNewState(calculateOutput))
}
