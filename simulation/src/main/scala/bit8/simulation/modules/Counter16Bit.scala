package bit8.simulation.modules

import bit8.simulation.components.BusTransceiver
import bit8.simulation.components.Counter
import bit8.simulation.components.Inverter
import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow.Bit16
import bit8.simulation.components.wire.Branch
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection._
import bit8.simulation.components.wire.Join
import bit8.simulation.components.wire.Wire
import bit8.simulation.components.utils.IntegerWithOverflow._

/**
 * @param clk - clock. active high
 * @param hIn  - higher byte in
 * @param hOut  - higher byte out
 * @param lIn  - lower byte in
 * @param lOut  - lower byte out
 * @param ce  - counter enable. active high. increments counter.
 */
//noinspection DuplicatedCode
class Counter16Bit(val io0: Connection, val io1: Connection, val io2: Connection, val io3: Connection, val io4: Connection, val io5: Connection, val io6: Connection, val io7: Connection,
                   val directOut0: Connection, val directOut1: Connection, val directOut2: Connection, val directOut3: Connection, val directOut4: Connection, val directOut5: Connection, val directOut6: Connection, val directOut7: Connection, val directOut8: Connection, val directOut9: Connection, val directOut10: Connection, val directOut11: Connection, val directOut12: Connection, val directOut13: Connection, val directOut14: Connection, val directOut15: Connection,
                   val clk: Connection, val hIn: Connection, val hOut: Connection, val lIn: Connection, val lOut: Connection, val ce: Connection) {

  val (io0In, io0Out) = Branch.branch(io0)
  val (io1In, io1Out) = Branch.branch(io1)
  val (io2In, io2Out) = Branch.branch(io2)
  val (io3In, io3Out) = Branch.branch(io3)
  val (io4In, io4Out) = Branch.branch(io4)
  val (io5In, io5Out) = Branch.branch(io5)
  val (io6In, io6Out) = Branch.branch(io6)
  val (io7In, io7Out) = Branch.branch(io7)

  val cbW0, cbW1, cbW2, cbW3, cbW4, cbW5, cbW6, cbW7, cbW8, cbW9, cbW10, cbW11, cbW12, cbW13, cbW14, cbW15 = Connection.wire()
  val t1, t2, t3 = Connection.wire()

  val c1 = new Counter(t1.left, ce, lIn, clk, HIGH, HIGH, cbW12.left, cbW13.left, cbW14.left, cbW15.left, io4In, io5In, io6In, io7In)
  val c2 = new Counter(t2.left, HIGH, lIn, clk, t1.right, HIGH, cbW8.left, cbW9.left, cbW10.left, cbW11.left, io0In, io1In, io2In, io3In)

  val c3 = new Counter(t3.left, HIGH, hIn, clk, t2.right, HIGH, cbW4.left, cbW5.left, cbW6.left, cbW7.left, io4In, io5In, io6In, io7In)
  val c4 = new Counter(GROUND, HIGH, hIn, clk, t3.right, HIGH, cbW0.left, cbW1.left, cbW2.left, cbW3.left, io0In, io1In, io2In, io3In)

  private val cbb0 = Branch.branch(cbW0.right) match {case (cbbR, cbbL) => Join(cbbR, directOut0); cbbL}
  private val cbb1 = Branch.branch(cbW1.right) match {case (cbbR, cbbL) => Join(cbbR, directOut1); cbbL}
  private val cbb2 = Branch.branch(cbW2.right) match {case (cbbR, cbbL) => Join(cbbR, directOut2); cbbL}
  private val cbb3 = Branch.branch(cbW3.right) match {case (cbbR, cbbL) => Join(cbbR, directOut3); cbbL}
  private val cbb4 = Branch.branch(cbW4.right) match {case (cbbR, cbbL) => Join(cbbR, directOut4); cbbL}
  private val cbb5 = Branch.branch(cbW5.right) match {case (cbbR, cbbL) => Join(cbbR, directOut5); cbbL}
  private val cbb6 = Branch.branch(cbW6.right) match {case (cbbR, cbbL) => Join(cbbR, directOut6); cbbL}
  private val cbb7 = Branch.branch(cbW7.right) match {case (cbbR, cbbL) => Join(cbbR, directOut7); cbbL}
  private val cbb8 = Branch.branch(cbW8.right) match {case (cbbR, cbbL) => Join(cbbR, directOut8); cbbL}
  private val cbb9 = Branch.branch(cbW9.right) match {case (cbbR, cbbL) => Join(cbbR, directOut9); cbbL}
  private val cbb10 = Branch.branch(cbW10.right) match {case (cbbR, cbbL) => Join(cbbR, directOut10); cbbL}
  private val cbb11 = Branch.branch(cbW11.right) match {case (cbbR, cbbL) => Join(cbbR, directOut11); cbbL}
  private val cbb12 = Branch.branch(cbW12.right) match {case (cbbR, cbbL) => Join(cbbR, directOut12); cbbL}
  private val cbb13 = Branch.branch(cbW13.right) match {case (cbbR, cbbL) => Join(cbbR, directOut13); cbbL}
  private val cbb14 = Branch.branch(cbW14.right) match {case (cbbR, cbbL) => Join(cbbR, directOut14); cbbL}
  private val cbb15 = Branch.branch(cbW15.right) match {case (cbbR, cbbL) => Join(cbbR, directOut15); cbbL}

  val bt1 = new BusTransceiver(HIGH, hOut, cbb0, cbb1, cbb2, cbb3, cbb4, cbb5, cbb6, cbb7,            io0Out, io1Out, io2Out,  io3Out,  io4Out,  io5Out,  io6Out,  io7Out)
  val bt2 = new BusTransceiver(HIGH, lOut, cbb8, cbb9, cbb10, cbb11, cbb12, cbb13, cbb14, cbb15,      io0Out, io1Out, io2Out,  io3Out,  io4Out,  io5Out,  io6Out,  io7Out)

  def getState: IntegerWithOverflow[Bit16] = {
    val c1Bits = c1.getSate.toBits
    val c2Bits = c2.getSate.toBits
    val c3Bits = c3.getSate.toBits
    val c4Bits = c4.getSate.toBits

    IntegerWithOverflow.fromBits[Bit16](
      c4Bits._1, c4Bits._2, c4Bits._3, c4Bits._4,
      c3Bits._1, c3Bits._2, c3Bits._3, c3Bits._4,
      c2Bits._1, c2Bits._2, c2Bits._3, c2Bits._4,
      c1Bits._1, c1Bits._2, c1Bits._3, c1Bits._4,
    )
  }

}
