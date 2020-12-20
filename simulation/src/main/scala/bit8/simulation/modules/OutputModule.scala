package bit8.simulation.modules

import bit8.simulation.components.And
import bit8.simulation.components.BusTransceiver
import bit8.simulation.components.Inverter
import bit8.simulation.components.RSFlipFlop
import bit8.simulation.components.Register4Bit
import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.IntegerWithOverflow.Bit8
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection.LOW
import bit8.simulation.components.wire.Join

class OutputModule(val clk: Connection,
                   val de: Connection,
                   val enabled: Connection,
                   val in0: Connection,
                   val in1: Connection,
                   val in2: Connection,
                   val in3: Connection,
                   val in4: Connection,
                   val in5: Connection,
                   val in6: Connection,
                   val in7: Connection,
                   val out0: Connection,
                   val out1: Connection,
                   val out2: Connection,
                   val out3: Connection,
                   val out4: Connection,
                   val out5: Connection,
                   val out6: Connection,
                   val out7: Connection,
                  ) {


  private val deInverted = Inverter(de)
  private val notClk = Inverter(clk)
  private val dataInAndClock: Connection = And(de, clk)

  private val dataReadFeedback = Connection.wire()
  private val dataRead = RSFlipFlop(dataInAndClock, dataReadFeedback.left)
  private val dataReadAndNotClk = And(dataRead, notClk)

  private val dataEnabledFeedback = Connection.wire()
  private val dataEnabled = RSFlipFlop(dataReadAndNotClk, dataEnabledFeedback.left)

  private val dataEnabledEnd: Connection = And(dataEnabled, clk)
  private val dataReadEnd: Connection = And(dataEnabledEnd, deInverted)

  Join(dataEnabledEnd, dataEnabledFeedback.right)
  Join(dataReadEnd, dataReadFeedback.right)
  Join(dataEnabled, enabled)

  val r0 = new Register4Bit(LOW, deInverted, clk, in0, in1, in2, in3, out0, out1, out2, out3)
  val r1 = new Register4Bit(LOW, deInverted, clk, in4, in5, in6, in7, out4, out5, out6, out7)

  def getState: IntegerWithOverflow[Bit8] = {
    val s0Bits = r0.getState.toBits
    val s1Bits = r1.getState.toBits

    IntegerWithOverflow.fromBits[Bit8]((s0Bits._1, s0Bits._2, s0Bits._3, s0Bits._4, s1Bits._1, s1Bits._2, s1Bits._3, s1Bits._4))
  }
}