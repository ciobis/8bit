package bit8.instruction.micro

sealed trait MicroInstruction {
  val value: Vector[Boolean]

  def +(other: MicroInstruction): MicroInstruction = {
    MicroInstruction({
      val correctLengthInstruction = (this.value.size until other.value.size).foldLeft(this.value) { case (acc, _) => acc :+ false }
      other.value.zipWithIndex.foldLeft(correctLengthInstruction) {
        case (acc, (bit, idx)) if bit => acc.updated(idx, bit)
        case (acc, _) => acc
      }
    })
  }

  def contains(other: MicroInstruction): Boolean = {
    val matchingTrueBits = other.value.zipWithIndex.count {
      case (otherBit, idx) if otherBit => this.value.lift(idx) match {
        case Some(thisBit) => thisBit
        case None => false
      }
      case _ => false
    }

    val otherTrueCount = other.value.count(identity)

    (matchingTrueBits == otherTrueCount) && otherTrueCount > 0
  }

  override def equals(obj: Any): Boolean = obj match {
    case other: MicroInstruction => {
      if (this.value.length == other.value.length) {
        this.value.zip(other.value).forall { case (a, b) => a == b }
      } else {
        false
      }
    }
    case _ => false
  }

  override def toString: String = s"MicroInstruction(${value.mkString(", ")})"
}

object MicroInstruction {

  def apply(v: Vector[Boolean]): MicroInstruction = new MicroInstruction {
    override val value: Vector[Boolean] = v
  }

  def apply(v: Boolean*): MicroInstruction = apply(v.toVector)

  private def fromBitNo(bitNo: Int): MicroInstruction =
    apply((0 until bitNo).foldLeft(Vector[Boolean]()) { case (acc, _) => acc :+ false } :+ true)


  val RegAIn: MicroInstruction = fromBitNo(0)
  val RegAOut: MicroInstruction = fromBitNo(1)
  val RegBIn: MicroInstruction = fromBitNo(2)
  val RegBOut: MicroInstruction = fromBitNo(3)
  val CounterCount: MicroInstruction = fromBitNo(4)
  val CounterHigherIn: MicroInstruction = fromBitNo(5)
  val CounterHigherOut: MicroInstruction = fromBitNo(6)
  val CounterLowerIn: MicroInstruction = fromBitNo(7)
  val CounterLowerOut: MicroInstruction = fromBitNo(8)
  val InstructionOut: MicroInstruction = fromBitNo(9)
  val InstructionDecoderIn: MicroInstruction = fromBitNo(10)

  val AluCount: MicroInstruction = fromBitNo(11)
  val AluOut: MicroInstruction = fromBitNo(12)
  val AluOp2: MicroInstruction = fromBitNo(13)

  val RegMLIn: MicroInstruction = fromBitNo(14)
  val RegMLOut: MicroInstruction = fromBitNo(15)
  val RegMHIn: MicroInstruction = fromBitNo(16)
  val RegMHOut: MicroInstruction = fromBitNo(17)

  val MemoryIn: MicroInstruction = fromBitNo(18)
  val MemoryOut: MicroInstruction = fromBitNo(19)

  val StackUp: MicroInstruction = fromBitNo(20)
  val StackDown: MicroInstruction = fromBitNo(21)
  val StackOut: MicroInstruction = fromBitNo(22)
  val Halt: MicroInstruction = fromBitNo(23)
  val RegOutIn: MicroInstruction = fromBitNo(24)

  val AluOp1: MicroInstruction = fromBitNo(25)

}


