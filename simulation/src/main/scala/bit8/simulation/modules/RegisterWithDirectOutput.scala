package bit8.simulation.modules

import bit8.simulation.components.BusTransceiver
import bit8.simulation.components.Inverter
import bit8.simulation.components.Register4Bit
import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.wire.Branch
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection.HIGH
import bit8.simulation.components.wire.Connection.LOW


class RegisterWithDirectOutput(val oe: Connection,
                               val de: Connection,
                               val clk: Connection,
                               val io0: Connection,
                               val io1: Connection,
                               val io2: Connection,
                               val io3: Connection,
                               val io4: Connection,
                               val io5: Connection,
                               val io6: Connection,
                               val io7: Connection,
                               val out0: Connection,
                               val out1: Connection,
                               val out2: Connection,
                               val out3: Connection,
                               val out4: Connection,
                               val out5: Connection,
                               val out6: Connection,
                               val out7: Connection,
                              ) {

  val (regOut0, traIn0) = Branch.branch(out0)
  val (regOut1, traIn1) = Branch.branch(out1)
  val (regOut2, traIn2) = Branch.branch(out2)
  val (regOut3, traIn3) = Branch.branch(out3)
  val (regOut4, traIn4) = Branch.branch(out4)
  val (regOut5, traIn5) = Branch.branch(out5)
  val (regOut6, traIn6) = Branch.branch(out6)
  val (regOut7, traIn7) = Branch.branch(out7)

  val (io0In, io0Out) = Branch.branch(io0)
  val (io1In, io1Out) = Branch.branch(io1)
  val (io2In, io2Out) = Branch.branch(io2)
  val (io3In, io3Out) = Branch.branch(io3)
  val (io4In, io4Out) = Branch.branch(io4)
  val (io5In, io5Out) = Branch.branch(io5)
  val (io6In, io6Out) = Branch.branch(io6)
  val (io7In, io7Out) = Branch.branch(io7)

  val r0 = new Register4Bit(LOW, de, clk, io0In, io1In, io2In, io3In, regOut0, regOut1, regOut2, regOut3)
  val r1 = new Register4Bit(LOW, de, clk, io4In, io5In, io6In, io7In, regOut4, regOut5, regOut6, regOut7)

  new BusTransceiver(
    HIGH, oe, traIn0, traIn1, traIn2, traIn3, traIn4, traIn5, traIn6, traIn7,
    io0Out, io1Out, io2Out, io3Out, io4Out, io5Out, io6Out, io7Out
  )

  def getState: IntegerWithOverflow[Bit8] = {
    val s0Bits = r0.getState.toBits
    val s1Bits = r1.getState.toBits

    IntegerWithOverflow.fromBits[Bit8]((s0Bits._1, s0Bits._2, s0Bits._3, s0Bits._4, s1Bits._1, s1Bits._2, s1Bits._3, s1Bits._4))
  }
}
