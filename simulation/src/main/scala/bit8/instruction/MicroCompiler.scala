package bit8.instruction

import bit8.instruction.Instruction.InstructionDescriptor
import bit8.instruction.micro.MicroInstruction

object MicroCompiler {

  val InstructionAddrNoBits = 6
  val InstructionAddrLengthBits = 4

  def compile(instructions: Seq[InstructionDescriptor]): Seq[Map[Int, Int]] = {
    val instructionLengthBytes = microInstructionMaxBytes(instructions)
    instructions.iterator
      .map(inst => {
        val r = compile(inst, instructionLengthBytes)
        r
      })
      .reduce((a, b) => a.zip(b).map { case (c, d) => c ++ d })
  }

  def maxInstructions(): Int = Math.pow(2, InstructionAddrNoBits).intValue

  private def compile(i: InstructionDescriptor, instructionLengthBytes: Int): Seq[Map[Int, Int]] = {
    i.instruction.iterator
      .map(microInstructionToInts)
      .map(extendBytes(_, instructionLengthBytes))
      .zipWithIndex
      .map {
        case (microBytes, idx) =>
          val microInstructionAddress = concatInts(i.no, InstructionAddrNoBits, idx, InstructionAddrLengthBits)
          microBytes.map(b => Map(microInstructionAddress -> b))
      }
      .reduce((a, b) => a.zip(b).map { case (c, d) => c ++ d })
  }

  private def microInstructionMaxBytes(instructions: Seq[InstructionDescriptor]): Int = {
    val maxBitsCount = instructions.iterator
      .flatten(_.instruction)
      .map(_.value.length)
      .max

    Math.ceil(maxBitsCount / 8.0).toInt
  }

  private def extendBytes(bytes: Seq[Int], size: Int): Seq[Int] = {
    if (bytes.size >= size) {
      bytes
    } else {
      bytes ++ (0 to (size - bytes.size)).map(_ => 0)
    }
  }

  private def microInstructionToInts(inst: MicroInstruction): Seq[Int] = {
    inst.value
      .sliding(8, 8)
      .map(byte => {
        val bits = byte.map(b => if (b) "1" else "0")
        val extendedBits = bits ++ (0 until (8 - bits.size)).map(_ => "0")

        Integer.parseInt(extendedBits.mkString, 2)
      })
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