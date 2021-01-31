package bit8.instruction

import bit8.instruction.Instruction.InstructionDescriptor
import bit8.instruction.micro.MicroInstruction

object MicroCompiler {

  val InstructionAddrNoBits = 6
  val InstructionAddrLengthBits = 4
  val InstructionAddrSize = Math.pow(2, InstructionAddrLengthBits).intValue
  val MaxInstructions: Int = Math.pow(2, InstructionAddrNoBits).intValue

  def compile(instructions: Seq[InstructionDescriptor]): Seq[Map[Int, Int]] = {
    val instructionLengthBytes = microInstructionMaxBytes(instructions)
    instructions.iterator
      .map(inst => {
        val r = compile(inst, instructionLengthBytes)
        r
      })
      .reduce((a, b) => a.zip(b).map { case (c, d) => c ++ d })
  }

  private def compile(descriptor: InstructionDescriptor, instructionLengthBytes: Int): Seq[Map[Int, Int]] = {
    val micros: Seq[MicroInstruction] = descriptor.instruction
    val emptyMicros: Seq[MicroInstruction] = (micros.size until InstructionAddrSize).map(_ => MicroInstruction.Empty)

    (micros ++ emptyMicros).iterator
      .map(microInstructionToBytes(_, instructionLengthBytes))
      .zipWithIndex
      .map { case (microBytes, idx) =>
          val microInstructionAddress = concatInts(descriptor.no, InstructionAddrNoBits, idx, InstructionAddrLengthBits)
          microBytes.map(b => Map(microInstructionAddress -> b))
      }
      .reduce((a, b) => a.zip(b).map { case (c, d) => c ++ d })
  }

  private def microInstructionMaxBytes(instructions: Seq[InstructionDescriptor]): Int = {
    val maxBitsCount = instructions.iterator
      .flatten(_.instruction)
      .map(_.activeBits.length)
      .max

    Math.ceil(maxBitsCount / 8.0).toInt
  }

  private def microInstructionToBytes(inst: MicroInstruction, bytesPerInstruction: Int): Seq[Int] = {
    (0 until (8 * bytesPerInstruction))
      .map(bitNo => {
        if (inst.isActiveAt(bitNo)) {
          MicroInstruction.activeStateAt(bitNo)
        } else {
          MicroInstruction.inactiveStateAt(bitNo)
        }
      })
      .map(bitState => if (bitState) "1" else "0")
      .sliding(8, 8)
      .map(bits => Integer.parseInt(bits.mkString, 2))
      .toSeq
  }

  private def concatInts(a: Int, aLength: Int, b: Int, bLength: Int): Int = {
    def toStringBits(i: Int, length: Int): String = {
      val bs = i.toBinaryString

      if (bs.length < length) {
        val prepend = "0" * (length - bs.length)
        prepend + bs
      } else if (bs.length == length) {
        bs
      } else {
        throw new RuntimeException(s"Binary representation length of [$i] is bigger than [$length]")
      }
    }

    val binary = toStringBits(a, aLength) ++ toStringBits(b, bLength)
    Integer.parseInt(binary, 2)
  }

}