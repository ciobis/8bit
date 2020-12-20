package bit8.simulation.modules

import bit8.simulation.components.Counter
import bit8.simulation.components.Eeprom
import bit8.simulation.components.Inverter
import bit8.simulation.components.Register4Bit
import bit8.simulation.components.wire.Branch
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection._
import bit8.simulation.components.wire.Wire

/**
 *
 * @param clk - clock
 * @param ie - instruction enable. active high
 * @param cr - counter reset. active high
 */
//noinspection DuplicatedCode
class InstructionDecoder(
                          val clk: Connection, ie: Connection, cr: Connection, eqFlag: Connection,
                          val i0: Connection, val i1: Connection, val i2: Connection, val i3: Connection, val i4: Connection, val i5: Connection, val i6: Connection, val i7: Connection,
                          val o0: Connection, val o1: Connection, val o2: Connection, val o3: Connection, val o4: Connection, val o5: Connection, val o6: Connection, val o7: Connection,
                          val o8: Connection, val o9: Connection, val o10: Connection, val o11: Connection, val o12: Connection, val o13: Connection, val o14: Connection, val o15: Connection,
                          val o16: Connection, val o17: Connection, val o18: Connection, val o19: Connection, val o20: Connection, val o21: Connection, val o22: Connection, val o23: Connection,
                          val o24: Connection, val o25: Connection, val o26: Connection, val o27: Connection, val o28: Connection, val o29: Connection, val o30: Connection, val o31: Connection,
                          val instructionOverrideData: Map[Int, Int], val eepromData1: Map[Int, Int], val eepromData2: Map[Int, Int], val eepromData3: Map[Int, Int], val eepromData4: Map[Int, Int]
                        ) {


  val counterOut0 = Connection.wire()
  val counterOut1 = Connection.wire()
  val counterOut2 = Connection.wire()
  val counterOut3 = Connection.wire()
  val (counterOut0_1, counterOut0_2, counterOut0_3, counterOut0_4) = Branch.branch4(counterOut0.right)
  val (counterOut1_1, counterOut1_2, counterOut1_3, counterOut1_4) = Branch.branch4(counterOut1.right)
  val (counterOut2_1, counterOut2_2, counterOut2_3, counterOut2_4) = Branch.branch4(counterOut2.right)
  val (counterOut3_1, counterOut3_2, counterOut3_3, counterOut3_4) = Branch.branch4(counterOut3.right)

  val regOut1 = Connection.wire()
  val regOut2 = Connection.wire()
  val regOut3 = Connection.wire()
  val regOut4 = Connection.wire()
  val regOut5 = Connection.wire()
  val regOut6 = Connection.wire()
  val regOut7 = Connection.wire()
  val override1 = Connection.wire()
  val override2 = Connection.wire()
  val override3 = Connection.wire()
  val override4 = Connection.wire()
  val override5 = Connection.wire()
  val override6 = Connection.wire()
  val override7 = Connection.wire()
  val (regOut1_1, regOut1_2, regOut1_3, regOut1_4) = Branch.branch4(override1.right)
  val (regOut2_1, regOut2_2, regOut2_3, regOut2_4) = Branch.branch4(override2.right)
  val (regOut3_1, regOut3_2, regOut3_3, regOut3_4) = Branch.branch4(override3.right)
  val (regOut4_1, regOut4_2, regOut4_3, regOut4_4) = Branch.branch4(override4.right)
  val (regOut5_1, regOut5_2, regOut5_3, regOut5_4) = Branch.branch4(override5.right)
  val (regOut6_1, regOut6_2, regOut6_3, regOut6_4) = Branch.branch4(override6.right)
  val (regOut7_1, regOut7_2, regOut7_3, regOut7_4) = Branch.branch4(override7.right)

  val invertedClk = Inverter(clk)
  val ieInverted = Inverter(ie)
  val crinverted = Inverter(cr)

  val reg0 = new Register4Bit(LOW, ieInverted, clk, i0, i1, i2, i3, LOW, regOut1.left, regOut2.left, regOut3.left)
  val reg1 = new Register4Bit(LOW, ieInverted, clk, i4, i5, i6, i7, regOut4.left, regOut5.left, regOut6.left, regOut7.left)

  val instructionOverride = new Eeprom(
    regOut1.right, regOut2.right, regOut3.right, regOut4.right, regOut5.right, regOut6.right, regOut7.right, LOW, LOW, LOW, eqFlag,
    LOW, override1.left, override2.left, override3.left, override4.left, override5.left, override6.left, override7.left,
    LOW, LOW, HIGH
  )

  val counter = new Counter(HIGH, HIGH, HIGH, invertedClk, HIGH, crinverted, counterOut0.left, counterOut1.left, counterOut2.left, counterOut3.left, LOW, LOW, LOW, LOW)

  val eeprom1 = new Eeprom(
    regOut1_1, regOut2_1, regOut3_1, regOut4_1, regOut5_1, regOut6_1, regOut7_1,
    counterOut0_1, counterOut1_1, counterOut2_1, counterOut3_1,
    o0, o1, o2, o3, o4, o5, o6, o7,
    LOW, LOW, HIGH
  )
  val eeprom2 = new Eeprom(
    regOut1_2, regOut2_2, regOut3_2, regOut4_2, regOut5_2, regOut6_2, regOut7_2,
    counterOut0_2, counterOut1_2, counterOut2_2, counterOut3_2,
    o8, o9, o10, o11, o12, o13, o14, o15,
    LOW, LOW, HIGH
  )

  val eeprom3 = new Eeprom(
    regOut1_3, regOut2_3, regOut3_3, regOut4_3, regOut5_3, regOut6_3, regOut7_3,
    counterOut0_3, counterOut1_3, counterOut2_3, counterOut3_3,
    o16, o17, o18, o19, o20, o21, o22, o23,
    LOW, LOW, HIGH
  )

  val eeprom4 = new Eeprom(
    regOut1_4, regOut2_4, regOut3_4, regOut4_4, regOut5_4, regOut6_4, regOut7_4,
    counterOut0_4, counterOut1_4, counterOut2_4, counterOut3_4,
    o24, o25, o26, o27, o28, o29, o30, o31,
    LOW, LOW, HIGH
  )

  Eeprom.writeData(instructionOverride, instructionOverrideData)
  Eeprom.writeData(eeprom1, eepromData1)
  Eeprom.writeData(eeprom2, eepromData2)
  Eeprom.writeData(eeprom3, eepromData3)
  Eeprom.writeData(eeprom4, eepromData4)
}
