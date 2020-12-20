package bit8.simulation.modules

import bit8.simulation.components.DoubleAlu
import bit8.simulation.components.Eeprom
import bit8.simulation.components.Inverter
import bit8.simulation.components.Register4Bit
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection._
import bit8.simulation.components.wire.Wire

class AluModule(
                 val clk: Connection,
                 val ce: Connection,
                 val oe: Connection,
                 val op0: Connection,
                 val op1: Connection,
                 val op2: Connection,
                 val a0: Connection,
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
                 val out0: Connection,
                 val out1: Connection,
                 val out2: Connection,
                 val out3: Connection,
                 val out4: Connection,
                 val out5: Connection,
                 val out6: Connection,
                 val out7: Connection,
                 val carryFlag: Connection,
                 val equalsFlag: Connection
               ) {

  val (aluOut0, registerIn0) = Connection.wire().connections
  val (aluOut1, registerIn1) = Connection.wire().connections
  val (aluOut2, registerIn2) = Connection.wire().connections
  val (aluOut3, registerIn3) = Connection.wire().connections
  val (aluOut4, registerIn4) = Connection.wire().connections
  val (aluOut5, registerIn5) = Connection.wire().connections
  val (aluOut6, registerIn6) = Connection.wire().connections
  val (aluOut7, registerIn7) = Connection.wire().connections

  val (s0Out, s0In) = Connection.wire().connections
  val (s1Out, s1In) = Connection.wire().connections
  val (s2Out, s2In) = Connection.wire().connections
  val (s3Out, s3In) = Connection.wire().connections
  val (mOut, mIn) = Connection.wire().connections
  val (cnOut, cnIn) = Connection.wire().connections
  val (cn4Out, cn4In) = Connection.wire().connections
  val (eqOut, eqIn) = Connection.wire().connections

  val ceInverted = Inverter(ce)
  val oeInverted = Inverter(oe)
  val clkInverted = Inverter(clk)

  val eeprom = new Eeprom(
    LOW, LOW, LOW, LOW, LOW, LOW, LOW, LOW, op0, op1, op2,
    LOW, LOW, s0Out, s1Out, s2Out, s3Out, mOut, cnOut,
    LOW, clkInverted, HIGH
  )

//  Eeprom.writeData(eeprom, Map(0 -> 37, 1 -> 24))
  Eeprom.writeData(eeprom, Map(
    0 -> Integer.parseInt("100101", 2),
    1 -> Integer.parseInt("011000", 2),
    2 -> Integer.parseInt("011001", 2)
  ))

  val doubleAlu = new DoubleAlu(
    a0, a1, a2, a3, a4, a5, a6, a7,
    b0, b1, b2, b3, b4, b5, b6, b7,
    aluOut0, aluOut1, aluOut2, aluOut3, aluOut4, aluOut5, aluOut6, aluOut7,
    s0In, s1In, s2In, s3In, mIn, cnIn, cn4Out, eqOut
  )

  val r0 = new Register4Bit(oeInverted, ceInverted, clk, registerIn0, registerIn1, registerIn2, registerIn3, out0, out1, out2, out3)
  val r1 = new Register4Bit(oeInverted, ceInverted, clk, registerIn4, registerIn5, registerIn6, registerIn7, out4, out5, out6, out7)
  val flags = new Register4Bit(LOW, ceInverted, clk, cn4In, eqIn, LOW, LOW, carryFlag, equalsFlag, LOW, LOW)
}
