package bit8.instruction

import bit8.instruction.Instruction.Instruction
import bit8.instruction.Instruction.InstructionDescriptor
import bit8.instruction.micro.MicroInstruction

object Compiler {

  type LabelRef = Map[String, Int]
  type VarRef = Map[String, Int]
  private case class CompilableInstruction(instruction: InstructionDescriptor, data:(LabelRef, VarRef) => Seq[Int])

  private val variablesStartAddress = 256

  private val MovRegToReg = "MOV (A|B|OUT),(A|B)".r
  private val MovConstToReg = "MOV (A|B|ML|MH),(\\d+)".r
  private val MovConstToMemory = "MOV \\[MX\\],(\\d+)".r
  private val MovMemoryToRegister = "MOV (A|B),\\[MX\\]".r
  private val AddConstToReg = "ADD (ML|MH),(\\d+)".r
  private val AddRamToReg = "ADD (A|B),\\[MX\\]".r
  private val AddRegToVar = "ADD (.*),(A|B)".r
  private val AddRegisters = "ADD (A),(B)".r
  private val SubRegisters = "SUB (A),(B)".r
  private val Label = "^(.+):".r
  private val Jmp = "JMP (.+)".r
  private val Call = "CALL (.+)".r
  private val Ret = "RET"
  private val DeclareByte = "DB (.*)".r
  private val MovConstToVar = "MOV (.*),(\\d+)".r
  private val MovVarToReg = "MOV (A|B|OUT),(.*)".r
  private val MovRegToVar = "MOV (.*),(A|B)".r
  private val Hlt = "HLT"
  private val Cmp = "CMP (A),(B)".r
  private val Jne = "JNE (.*)".r
  private val Je = "JE (.*)".r

  def compile(lines: Seq[String]): Seq[Int] = {

    validateDuplicateLabels(lines)
    val labelRef: LabelRef = labelReferences(lines)
    val varRef: VarRef = variableReferences(lines)

    lines.iterator.flatMap(line => {
      getCompilable(line) match {
        case Some(CompilableInstruction(instr, data)) => instr.no +: data(labelRef, varRef)
        case None => Seq.empty
      }
    }).toSeq
  }

  private def instructionLength(line: String): Int = {
    getCompilable(line) match {
      case Some(CompilableInstruction(instr, _)) =>
        instr.instruction.count(micro => micro.contains(MicroInstruction.CounterCount))
      case None => 0
    }
  }

  private def getCompilable(line: String): Option[CompilableInstruction] = line match {
    case MovRegToReg("B", "A") => Some(CompilableInstruction(Instruction.MovAB, (_, _) => Seq.empty))
    case MovRegToReg("A", "B") => Some(CompilableInstruction(Instruction.MovBA, (_, _) => Seq.empty))
    case MovRegToReg("OUT", "A") => Some(CompilableInstruction(Instruction.MovOutA, (_, _) => Seq.empty))
    case MovRegToReg("OUT", "B") => Some(CompilableInstruction(Instruction.MovOutB, (_, _) => Seq.empty))
    case MovConstToReg("A", value: String) => Some(CompilableInstruction(Instruction.MovConstToA, (_, _) => Seq(value.toInt)))
    case MovConstToReg("B", value: String) => Some(CompilableInstruction(Instruction.MovConstToB, (_, _) => Seq(value.toInt)))
    case MovConstToReg("ML", value: String) => Some(CompilableInstruction(Instruction.MovConstToML, (_, _) => Seq(value.toInt)))
    case MovConstToReg("MH", value: String) => Some(CompilableInstruction(Instruction.MovConstToMH, (_, _) => Seq(value.toInt)))
    case MovConstToMemory(value: String) => Some(CompilableInstruction(Instruction.MovConstToMemory, (_, _) => Seq(value.toInt)))
    case MovMemoryToRegister("A") => Some(CompilableInstruction(Instruction.MovMemoryToRegisterA, (_, _) => Seq.empty))
    case MovMemoryToRegister("B") => Some(CompilableInstruction(Instruction.MovMemoryToRegisterB, (_, _) => Seq.empty))
    case AddRegisters("A", "B") => Some(CompilableInstruction(Instruction.AddAB, (_, _) => Seq.empty))
    case SubRegisters("A", "B") => Some(CompilableInstruction(Instruction.SubAB, (_, _) => Seq.empty))
    case AddRamToReg("A") => Some(CompilableInstruction(Instruction.AddARam, (_, _) => Seq.empty))
    case AddRamToReg("B") => Some(CompilableInstruction(Instruction.AddBRam, (_, _) => Seq.empty))
    case AddConstToReg("ML", value: String) => Some(CompilableInstruction(Instruction.AddMlConst, (_, _) => Seq(value.toInt)))
    case AddConstToReg("MH", value: String) => Some(CompilableInstruction(Instruction.AddMhConst, (_, _) => Seq(value.toInt)))
    case AddRegToVar(label, "B") => Some(CompilableInstruction(Instruction.AddBToRam, (_, vars) => splitIntToBytes(vars(label)) ))
    case AddRegToVar(label, "A") => Some(CompilableInstruction(Instruction.AddAToRam, (_, vars) => splitIntToBytes(vars(label)) ))
    case Jmp(label) => Some(CompilableInstruction(Instruction.Jmp, (labels, _) => splitIntToBytes(labels(label))))
    case Call(label) => Some(CompilableInstruction(Instruction.Call, (labels, _) => 0 +: splitIntToBytes(labels(label))))
    case Ret => Some(CompilableInstruction(Instruction.Ret, (_, _) => Seq(0)))
    case MovConstToVar(label, value) => Some(CompilableInstruction(Instruction.MovConstToVar, (_, vars) => splitIntToBytes(vars(label)) :+ value.toInt))
    case MovVarToReg("A", label) => Some(CompilableInstruction(Instruction.MovVarToA, (_, vars) => splitIntToBytes(vars(label))))
    case MovVarToReg("B", label) => Some(CompilableInstruction(Instruction.MovVarToB, (_, vars) => splitIntToBytes(vars(label))))
    case MovVarToReg("OUT", label) => Some(CompilableInstruction(Instruction.MovVarToOut, (_, vars) => splitIntToBytes(vars(label))))
    case MovRegToVar(label, "A") => Some(CompilableInstruction(Instruction.MovAToVar, (_, vars) => splitIntToBytes(vars(label))))
    case MovRegToVar(label, "B") => Some(CompilableInstruction(Instruction.MovBToVar, (_, vars) => splitIntToBytes(vars(label))))
    case Cmp("A", "B") => Some(CompilableInstruction(Instruction.CmpAB, (_, _) => Seq.empty))
    case Jne(label) => Some(CompilableInstruction(Instruction.Jne, (labels, _) => splitIntToBytes(labels(label))))
    case Je(label) => Some(CompilableInstruction(Instruction.Je, (labels, _) => splitIntToBytes(labels(label))))
    case Hlt => Some(CompilableInstruction(Instruction.Hlt, (_, _) => Seq.empty))
    case Label(_) => None
    case DeclareByte(_) => None
  }

  private def labelReferences(lines: Seq[String]): Map[String, Int] = {
    case class LabelContext(currentAddr: Int, result: Map[String, Int])

    val fullContext = lines.foldLeft(LabelContext(0, Map.empty)) {
      case (ctx, Label(l)) => ctx.copy(result = ctx.result ++ Map(l -> ctx.currentAddr))
      case (ctx, instr) => ctx.copy(currentAddr = ctx.currentAddr + instructionLength(instr))
    }

    fullContext.result
  }

  private def variableReferences(lines: Seq[String]): Map[String, Int] = {
    lines.collect {
      case DeclareByte(label) => label
    }.zipWithIndex.map { case (label, idx) =>
      (label, idx + variablesStartAddress)
    }.toMap
  }

  private def splitIntToBytes(value: Int): Seq[Int] = {
    val numberBits = value.toBinaryString
    val remainingHigherBits = numberBits.length % 8
    val wholeBytesNumberBits = numberBits.substring(remainingHigherBits)
    val wholeBytes = wholeBytesNumberBits.toSeq.sliding(8).map(s => Integer.parseInt(s.toString(), 2)).toSeq

    val remainingByteBits = numberBits.substring(0, remainingHigherBits)
    val remainingByte =
      if (remainingByteBits.nonEmpty) Seq(Integer.parseInt(remainingByteBits, 2))
      else Seq.empty


    val bytes = remainingByte ++ wholeBytes

    if (bytes.size == 1) Seq(0) ++ bytes
    else bytes
  }

  private def validateDuplicateLabels(lines: Seq[String]): Unit = {
    val allLabels = lines.iterator
      .collect {
        case Label(l) => l
      }.toSeq

    val duplicateLabels = allLabels.diff(allLabels.distinct).distinct
    if (duplicateLabels.nonEmpty) {
      throw new RuntimeException(s"Program contains duplicate labels: [${duplicateLabels.mkString(", ")}]")
    }
  }

}
