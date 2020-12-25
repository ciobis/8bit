package bit8.simulation

import bit8.simulation.components.Cable
import bit8.simulation.components.Socket.Socket
import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.wire.{Bus, Connection, Join}
import bit8.simulation.components.wire.Connection._
import bit8.simulation.modules.{AluModule, Counter16Bit, EepromModule, InputModule, InstructionDecoder, RamModule, Register, RegisterWithDirectOutput, StackCounter}

import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

class Cpu(clk: Connection,
          outEnabled: Connection,
          hlt: Connection,
          busValue: Socket,
          inputValue: Socket,
          program: Map[Int, Int],
          decoderMapping1: Map[Int, Int],
          decoderMapping2: Map[Int, Int],
          decoderMapping3: Map[Int, Int],
          decoderMapping4: Map[Int, Int]
              ) {

  val bus: Bus = new Bus()

  val regAoe = Connection.wire()
  val regAde = Connection.wire()
  val regBoe = Connection.wire()
  val regBde = Connection.wire()
  val regOutDe = Connection.wire()
  val eepToCounter0, eepToCounter1, eepToCounter2, eepToCounter3, eepToCounter4, eepToCounter5, eepToCounter6, eepToCounter7, eepToCounter8, eepToCounter9, eepToCounter10, eepToCounter11 = Connection.wire()
  val regAValue_1, regAValue_2, regAValue_3, regAValue_4, regAValue_5, regAValue_6, regAValue_7, regAValue_8 = Connection.wire()
  val eepOe = Connection.wire()
  val counterHIn, counterHOut, counterLIn, counterLOut, counterCe = Connection.wire()
  val decoderIn = Connection.wire()
  val aluCount = Connection.wire()
  val aluOut = Connection.wire()
  val aluOp1 = Connection.wire()
  val aluOp2 = Connection.wire()
  val ramOut = Connection.wire()
  val ramIn = Connection.wire()
  val ramLowRegisterIn = Connection.wire()
  val ramLowRegisterOut = Connection.wire()
  val ramHighRegisterIn = Connection.wire()
  val ramHighRegisterOut = Connection.wire()
  val stackUp = Connection.wire()
  val stackDown = Connection.wire()
  val stackOut = Connection.wire()
//  val halt = Connection.wire()
  val eqFlag = Connection.wire()
  val inputOut = Connection.wire()

  val regA: RegisterWithDirectOutput = connectToBus(s => {
    new RegisterWithDirectOutput(
      regAoe.left, regAde.left, clk,
      s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8,
      regAValue_1.left, regAValue_2.left, regAValue_3.left, regAValue_4.left, regAValue_5.left, regAValue_6.left, regAValue_7.left, regAValue_8.left
    )
  })

  val regB: Register = connectToBus(s => {
    Register(regBoe.left, regBde.left, clk, s)
  })

  val eeprom: EepromModule = connectToBus(s => {
    val e = EepromModule(
      eepToCounter0.left, eepToCounter1.left, eepToCounter2.left, eepToCounter3.left, eepToCounter4.left, eepToCounter5.left, eepToCounter6.left, eepToCounter7.left, eepToCounter8.left, eepToCounter9.left, eepToCounter10.left,
      s, eepOe.left
    )
    e.withData(program)
    e
  })

  val counter: Counter16Bit = connectToBus(s => {
    new Counter16Bit(
      s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8,
      LOW, LOW, LOW, LOW, LOW, eepToCounter0.left, eepToCounter1.left, eepToCounter2.left, eepToCounter3.left, eepToCounter4.left, eepToCounter5.left, eepToCounter6.left, eepToCounter7.left, eepToCounter8.left, eepToCounter9.left, eepToCounter10.left,
      clk, counterHIn.left, counterHOut.left, counterLIn.left, counterLOut.left, counterCe.left)
  })

  val alu: AluModule = connectToBus(s => {
    new AluModule(
      clk, aluCount.left, aluOut.left,
      LOW, aluOp1.left, aluOp2.left,
      regAValue_1.right, regAValue_2.right, regAValue_3.right, regAValue_4.right, regAValue_5.right, regAValue_6.right, regAValue_7.right, regAValue_8.right,
      s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8,
      s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8,
      LOW, eqFlag.left
    )
  })

  val ram: RamModule = connectToBus(s => {
    new RamModule(
      clk, ramIn.left, ramOut.left, ramLowRegisterIn.left, ramLowRegisterOut.left, ramHighRegisterIn.left, ramHighRegisterOut.left,
      s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8
    )
  })

  val stack: StackCounter = connectToBus(s => {
    new StackCounter(
      clk, stackUp.left, stackDown.left, stackOut.left,
      s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8
    )
  })

  val input: InputModule = connectToBus(s => {
    new InputModule(
      inputOut.left,
//      LOW,
      s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8,
      inputValue._1, inputValue._2, inputValue._3, inputValue._4, inputValue._5, inputValue._6, inputValue._7, inputValue._8,
    )
  })

  val instructionDecoder: InstructionDecoder = connectToBus(s => {
    new InstructionDecoder(
      clk, decoderIn.left, LOW, eqFlag.right,
      s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8,
      regAde.right, regAoe.right, regBde.right, regBoe.right, counterCe.right, counterHIn.right, counterHOut.right, counterLIn.right,
      counterLOut.right, eepOe.right, decoderIn.right, aluCount.right, aluOut.right, aluOp2.right, ramLowRegisterIn.right, ramLowRegisterOut.right,
      ramHighRegisterIn.right, ramHighRegisterOut.right, ramIn.right, ramOut.right, stackUp.right, stackDown.right, stackOut.right, hlt,
      outEnabled, aluOp1.right, inputOut.right, LOW, LOW, LOW, LOW, LOW,
      bit8.instruction.Utils.instructionOverrides, decoderMapping1, decoderMapping2, decoderMapping3, decoderMapping4
    )
  })

  connectToBus(s => {
    Join(s._1, busValue._1)
    Join(s._2, busValue._2)
    Join(s._3, busValue._3)
    Join(s._4, busValue._4)
    Join(s._5, busValue._5)
    Join(s._6, busValue._6)
    Join(s._7, busValue._7)
    Join(s._8, busValue._8)
  })

  //todo refactor describe()
  def describe(): ListBuffer[String] = {
    val lines = new ListBuffer[String]

    lines += s"Clock: ${clk.wire.getState()}"
    lines += s"Program counter: ${counter.getState.value}"
    lines += s"Reg A: ${regA.getState.value}"
    lines += s"Reg A Bits: ${regA.getState.toBits}"

    lines += s"Reg B: ${regB.getState.value}"
    lines += s"Reg B Bits: ${regB.getState.toBits}"

    lines += s"Bus: ${bus.getState.toBits}"




    lines += "Controls:"
    lines += s"regAIn: ${regAde.isHigh}"
    lines += s"regAOut: ${regAoe.isHigh}"
    lines += s"regBIn: ${regBde.isHigh}"
    lines += s"regBOut: ${regBoe.isHigh}"
    lines += s"counterCe: ${counterCe.isHigh}"
    lines += s"counterHIn: ${counterHIn.isHigh}"
    lines += s"counterHOut: ${counterHOut.isHigh}"
    lines += s"counterLIn: ${counterLIn.isHigh}"
    lines += s"counterLOut: ${counterLOut.isHigh}"
    lines += s"eepOe: ${eepOe.isHigh}"
    lines += s"decoderIn: ${decoderIn.isHigh}"
    lines += s"aluCount: ${aluCount.isHigh}"
    lines += s"aluOut: ${aluOut.isHigh}"

    lines += s"ramOut: ${ramOut.isHigh}"
    lines += s"ramIn: ${ramIn.isHigh}"
    lines += s"ramLowRegisterIn: ${ramLowRegisterIn.isHigh}"
    lines += s"ramLowRegisterOut: ${ramLowRegisterOut.isHigh}"
    lines += s"ramHighRegisterIn: ${ramHighRegisterIn.isHigh}"
    lines += s"ramHighRegisterOut: ${ramHighRegisterOut.isHigh}"
    lines += s"RAM H value: ${ram.rh.getState.value}"
    lines += s"RAM L value: ${ram.rl.getState.value}"
//    lines += s"RAM value: ${ram.rl.getState.value}"

    val idr0 = instructionDecoder.reg0.getState.toBits
    val idr1 = instructionDecoder.reg1.getState.toBits
    val referenceBits = (
      false,
      false,
      false,
      false,
      false,
      instructionDecoder.regOut1.isHigh,
      instructionDecoder.regOut2.isHigh,
      instructionDecoder.regOut3.isHigh,
      instructionDecoder.regOut4.isHigh,
      instructionDecoder.regOut5.isHigh,
      instructionDecoder.regOut6.isHigh,
      instructionDecoder.regOut7.isHigh,
      instructionDecoder.counterOut0.isHigh,
      instructionDecoder.counterOut1.isHigh,
      instructionDecoder.counterOut2.isHigh,
      instructionDecoder.counterOut3.isHigh
    )
    val decoderOut = (
      instructionDecoder.o0.wire.isHigh,
      instructionDecoder.o1.wire.isHigh,
      instructionDecoder.o2.wire.isHigh,
      instructionDecoder.o3.wire.isHigh,
      instructionDecoder.o4.wire.isHigh,
      instructionDecoder.o5.wire.isHigh,
      instructionDecoder.o6.wire.isHigh,
      instructionDecoder.o7.wire.isHigh
    )
    lines += s"Decoder Instruction: ${IntegerWithOverflow.fromBits[Bit8]((idr0._1, idr0._2, idr0._3, idr0._4, idr1._1, idr1._2, idr1._3, idr1._4)).value}"
    lines += s"Decoder counter: ${instructionDecoder.counter.getSate.value}"
    lines += s"Decoder reference: ${IntegerWithOverflow.fromBits[Bit16](referenceBits).value}"
    lines += s"Decoder output: ${IntegerWithOverflow.fromBits[Bit8](decoderOut).value}"
    //    lines += "Decoder"

    lines
  }

  private def connectToBus[T](f: Socket => T): T = {
    val cable = Cable()
    bus.connect(cable._1)

    f(cable._2)
  }
}