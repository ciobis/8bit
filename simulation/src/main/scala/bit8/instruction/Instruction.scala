package bit8.instruction

import bit8.instruction.micro.MicroInstruction

object Instruction {

  case class InstructionDescriptor(no: Int, instruction: Instruction)
  type Instruction = Vector[MicroInstruction]

  def apply(micros: MicroInstruction*): Instruction = micros.toVector

  def apply(): Instruction = Vector.empty

  import MicroInstruction._

  private val FetchCycle: Instruction = Instruction(InstructionDecoderIn + InstructionOut, CounterCount)
  private val Jump: Instruction = Instruction(
    RegAIn + InstructionOut,
    CounterCount,
    RegBIn + InstructionOut,
    CounterCount,
    CounterHigherIn + RegAOut,
    CounterLowerIn + RegBOut,
  )

  private var instructionSet: Seq[InstructionDescriptor] = Seq.empty

  val Nop: InstructionDescriptor = addDescriptor()
  val MovAB: InstructionDescriptor = addDescriptor(
    RegBIn + RegAOut
  )
  val MovBA: InstructionDescriptor = addDescriptor(
    RegAIn + RegBOut
  )
  val MovConstToA: InstructionDescriptor = addDescriptor(
    RegAIn + InstructionOut,
    CounterCount
  )
  val AddAB: InstructionDescriptor = addDescriptor(
    RegBOut + AluCount,
    RegAIn + AluOut
  )
  val SubAB: InstructionDescriptor = addDescriptor(
    RegBOut + AluOp2 + AluCount,
    RegAIn + AluOut
  )
  val SubVarConst: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    RegAIn + MemoryOut,
    InstructionOut + AluCount + AluOp2,
    CounterCount,
    MemoryIn + AluOut
  )
  val SubVarVar: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    RegBIn + MemoryOut,
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    RegAIn + MemoryOut,
    RegBOut + AluCount + AluOp2,
    MemoryIn + AluOut
  )
  val MovConstToB: InstructionDescriptor = addDescriptor(
    RegBIn + InstructionOut,
    CounterCount
  )
  val Jmp: InstructionDescriptor = addDescriptor(Jump: _*)
  val MovConstToML: InstructionDescriptor = addDescriptor(
    RegMLIn + InstructionOut,
    CounterCount
  )
  val MovConstToMH: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount
  )
  val MovConstToOut: InstructionDescriptor = addDescriptor(
    RegOutIn + InstructionOut,
    CounterCount
  )
  val MovConstToMemory: InstructionDescriptor = addDescriptor(
    MemoryIn + InstructionOut,
    CounterCount
  )
  val MovMemoryToRegisterA: InstructionDescriptor = addDescriptor(
    MemoryOut + RegAIn,
  )
  val MovMemoryToRegisterB: InstructionDescriptor = addDescriptor(
    MemoryOut + RegBIn,
  )
  val AddMlConst: InstructionDescriptor = addDescriptor(
    InstructionOut + RegAIn,
    RegMLOut + AluCount,
    RegMLIn + AluOut,
    CounterCount
  )
  val AddMhConst: InstructionDescriptor = addDescriptor(
    RegMHOut + RegAIn,
    InstructionOut + AluCount,
    RegMHIn + AluOut,
    CounterCount
  )
  val AddARam: InstructionDescriptor = addDescriptor(
    MemoryOut + AluCount,
    RegAIn + AluOut
  )
  val AddBRam: InstructionDescriptor = addDescriptor(
    RegBOut + RegAIn,
    MemoryOut + AluCount,
    RegBIn + AluOut
  )
  val Call: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegAIn + InstructionOut,
    CounterCount,
    RegBIn + InstructionOut,
    CounterCount,
    RegMLIn + StackOut,
    MemoryIn + CounterHigherOut + StackUp,
    RegMLIn + StackOut,
    MemoryIn + CounterLowerOut + StackUp,
    CounterHigherIn + RegAOut,
    CounterLowerIn + RegBOut
  )
  val Ret: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount + StackDown,
    RegMLIn + StackOut,
    CounterLowerIn + MemoryOut + StackDown,
    RegMLIn + StackOut,
    CounterHigherIn + MemoryOut
  )

  val MovConstToVar: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    MemoryIn + InstructionOut,
    CounterCount
  )

  val MovVarToA: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    RegAIn + MemoryOut + CounterCount
  )

  val MovVarToB: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    RegBIn + MemoryOut + CounterCount
  )

  val MovAToVar: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    RegAOut + MemoryIn + CounterCount
  )

  val MovBToVar: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    RegBOut + MemoryIn + CounterCount
  )

  val AddAToRam: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    RegBIn + RegAOut + CounterCount,
    RegAIn + MemoryOut,
    RegBOut + AluCount,
    MemoryIn + AluOut
  )

  val AddVarToVar: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    RegAIn + MemoryOut,
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    MemoryOut + AluCount,
    MemoryIn + AluOut
  )

  val AddVarToMh: InstructionDescriptor = addDescriptor(
    RegAIn + RegMHOut,
    RegBIn + RegMLOut,
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    MemoryOut + AluCount,
    RegMHIn + AluOut,
    RegMLIn + RegBOut
  )

  val AddVarToMl: InstructionDescriptor = addDescriptor(
    RegAIn + RegMLOut,
    RegBIn + RegMHOut,
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    MemoryOut + AluCount,
    RegMLIn + AluOut,
    RegMHIn + RegBOut
  )

  val AddConstToRam: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    RegAIn + InstructionOut,
    CounterCount,
    MemoryOut + AluCount,
    MemoryIn + AluOut,
  )

  val AddBToRam: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    RegAIn + MemoryOut + CounterCount,
    RegBOut + AluCount,
    MemoryIn + AluOut
  )

  val Hlt: InstructionDescriptor = addDescriptor(
    Halt
  )

  val MovOutA: InstructionDescriptor = addDescriptor(
    RegOutIn + RegAOut
  )

  val MovOutB: InstructionDescriptor = addDescriptor(
    RegOutIn + RegBOut
  )

  val MovInputToA: InstructionDescriptor = addDescriptor(
    RegAIn + InputEnable
  )

  val MovVarToOut: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    RegOutIn + MemoryOut + CounterCount
  )

  val MovVarToMh: InstructionDescriptor = addDescriptor(
    RegBIn + RegMLOut,
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    RegAIn + MemoryOut + CounterCount,
    RegMHIn + RegAOut,
    RegMLIn + RegBOut
  )

  val MovVarToMl: InstructionDescriptor = addDescriptor(
    RegBIn + RegMHOut,
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    RegAIn + MemoryOut + CounterCount,
    RegMLIn + RegAOut,
    RegMHIn + RegBOut
  )

  val MovVarToMemory: InstructionDescriptor = addDescriptor(
    RegAIn + InstructionOut,
    CounterCount,
    RegMHOut + AluCount, //use ALU register to current store MH
    RegMLOut + RegBIn,

    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    RegAIn + MemoryOut,

    RegBOut + RegMLIn,
    AluOut + RegMHIn,

    MemoryIn + RegAOut
  )

  val MovMemoryToVar: InstructionDescriptor = addDescriptor(
    RegAIn + InstructionOut,
    CounterCount,
    RegMHOut + AluCount, //use ALU register to current store MH
    RegMLOut + RegBIn,

    RegAIn + MemoryOut,
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    MemoryIn + RegAOut,

    RegBOut + RegMLIn,
    AluOut + RegMHIn,
  )

  val VarToVar: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    RegAIn + MemoryOut,
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    MemoryIn + RegAOut
  )

  val InputToVar: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    MemoryIn + InputEnable,
  )

  val CmpAB: InstructionDescriptor = addDescriptor(
    RegBOut + AluOp1 + AluCount
  )

  val CmpVarToVar: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    RegAIn + MemoryOut,
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    MemoryOut + AluOp1 + AluCount
  )

  val CmpConstToVar: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + InstructionOut,
    CounterCount,
    RegAIn + MemoryOut,
    RegBIn + InstructionOut,
    CounterCount,
    RegBOut + AluOp1 + AluCount
  )

  val PushConst: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount,
    RegMLIn + StackOut,
    MemoryIn + InstructionOut + StackUp,
    CounterCount
  )

  val PopRegOut: InstructionDescriptor = addDescriptor(
    RegMHIn + InstructionOut,
    CounterCount + StackDown,
    RegMLIn + StackOut,
    MemoryOut + RegOutIn,
  )

  val Jne: InstructionDescriptor = addDescriptor(Jump: _*)

  val Je: InstructionDescriptor = addDescriptor(Jump: _*)

  val Nop3: InstructionDescriptor = addDescriptor(CounterCount, CounterCount)

  private def addDescriptor(micros: MicroInstruction*): InstructionDescriptor = {
    val descriptor = InstructionDescriptor(instructionSet.size, FetchCycle ++ Instruction(micros: _*))
    instructionSet = instructionSet :+ descriptor

    descriptor
  }

  def fullInstructionSet(maxCount: Int): Seq[InstructionDescriptor] = {
    val definedInstructionNos = instructionSet.map(_.no)
    val notDefinedInstructionNos: Seq[Int] = (0 until maxCount).filterNot(definedInstructionNos.contains)
    val restNoOps = notDefinedInstructionNos.map(InstructionDescriptor(_, FetchCycle ++ Instruction()))

    instructionSet ++ restNoOps
  }

}
