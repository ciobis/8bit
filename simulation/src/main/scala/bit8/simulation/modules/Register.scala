package bit8.simulation.modules

import bit8.simulation.components.Inverter
import bit8.simulation.components.Register4Bit
import bit8.simulation.components.Socket.Socket
import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.IntegerWithOverflow.Bit8
import bit8.simulation.components.wire.Branch
import bit8.simulation.components.wire.Connection

/**
 * @param oe - output enable. active low
 * @param de - data enable. active low
 * @param clk - clock
 */
class Register(val oe: Connection,
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
              ) {
  val (io0In, io0Out) = Branch.branch(io0)
  val (io1In, io1Out) = Branch.branch(io1)
  val (io2In, io2Out) = Branch.branch(io2)
  val (io3In, io3Out) = Branch.branch(io3)
  val (io4In, io4Out) = Branch.branch(io4)
  val (io5In, io5Out) = Branch.branch(io5)
  val (io6In, io6Out) = Branch.branch(io6)
  val (io7In, io7Out) = Branch.branch(io7)

  val r0 = new Register4Bit(oe, de, clk, io0In, io1In, io2In, io3In, io0Out, io1Out, io2Out, io3Out)
  val r1 = new Register4Bit(oe, de, clk, io4In, io5In, io6In, io7In, io4Out, io5Out, io6Out, io7Out)

  def getState: IntegerWithOverflow[Bit8] = {
    val s0Bits = r0.getState.toBits
    val s1Bits = r1.getState.toBits

    IntegerWithOverflow.fromBits[Bit8]((s0Bits._1, s0Bits._2, s0Bits._3, s0Bits._4, s1Bits._1, s1Bits._2, s1Bits._3, s1Bits._4))
  }
}

object Register {

  def apply(oe: Connection, de: Connection, clk: Connection, io: Socket): Register =
    new Register(oe, de, clk, io._1, io._2, io._3, io._4, io._5, io._6, io._7, io._8)
}