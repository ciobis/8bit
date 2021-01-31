package bit8.instruction.micro

sealed trait ActiveState {
  val boolValue: Boolean
}
object ActiveHigh extends ActiveState {
  override val boolValue: Boolean = true
}
object ActiveLow extends ActiveState {
  override val boolValue: Boolean = false
}

sealed trait MicroInstruction {
  val activeBits: Vector[Boolean]

  def +(other: MicroInstruction): MicroInstruction = {
    MicroInstruction({
      val correctLengthInstruction = (this.activeBits.size until other.activeBits.size).foldLeft(this.activeBits) { case (acc, _) => acc :+ false }
      other.activeBits.zipWithIndex.foldLeft(correctLengthInstruction) {
        case (acc, (bit, idx)) if bit => acc.updated(idx, bit)
        case (acc, _) => acc
      }
    })
  }

  def contains(other: MicroInstruction): Boolean = {
    val matchingTrueBits = other.activeBits.zipWithIndex.count {
      case (otherBit, idx) if otherBit => this.activeBits.lift(idx) match {
        case Some(thisBit) => thisBit
        case None => false
      }
      case _ => false
    }

    val otherTrueCount = other.activeBits.count(identity)

    (matchingTrueBits == otherTrueCount) && otherTrueCount > 0
  }

  def isActiveAt(bitNo: Int): Boolean = {
    activeBits.lift(bitNo).getOrElse(false)
  }

  override def equals(obj: Any): Boolean = obj match {
    case other: MicroInstruction => {
      if (this.activeBits.length == other.activeBits.length) {
        this.activeBits.zip(other.activeBits).forall { case (a, b) => a == b }
      } else {
        false
      }
    }
    case _ => false
  }

  override def toString: String = s"MicroInstruction(${activeBits.mkString(", ")})"
}

object MicroInstruction {

  def apply(v: Vector[Boolean]): MicroInstruction = new MicroInstruction {
    override val activeBits: Vector[Boolean] = v
  }

  def apply(v: Boolean*): MicroInstruction = apply(v.toVector)

  def activeStateAt(bitNo: Int): Boolean = bitsActiveState.get(bitNo).map(_.boolValue).getOrElse(false)

  def inactiveStateAt(bitNo:Int): Boolean = !activeStateAt(bitNo)

  private var bitsActiveState: Map[Int, ActiveState] = Map()

  private def fromBitNo(bitNo: Int, activeState: ActiveState): MicroInstruction = {
    bitsActiveState = bitsActiveState.updated(bitNo, activeState)
    apply((0 until bitNo).foldLeft(Vector[Boolean]()) { case (acc, _) => acc :+ false } :+ true)
  }

  val Empty: MicroInstruction = apply()

  val RegAIn: MicroInstruction = fromBitNo(0, ActiveLow)
  val RegAOut: MicroInstruction = fromBitNo(1, ActiveLow)
  val RegBIn: MicroInstruction = fromBitNo(2, ActiveLow)
  val RegBOut: MicroInstruction = fromBitNo(3, ActiveLow)
  val CounterCount: MicroInstruction = fromBitNo(4, ActiveHigh)
  val CounterHigherIn: MicroInstruction = fromBitNo(5, ActiveLow)
  val CounterHigherOut: MicroInstruction = fromBitNo(6, ActiveLow)
  val CounterLowerIn: MicroInstruction = fromBitNo(7, ActiveLow)
  val CounterLowerOut: MicroInstruction = fromBitNo(8, ActiveLow)
  val InstructionOut: MicroInstruction = fromBitNo(9, ActiveLow)
  val InstructionDecoderIn: MicroInstruction = fromBitNo(10, ActiveLow)

  val AluCount: MicroInstruction = fromBitNo(11, ActiveLow)
  val AluOut: MicroInstruction = fromBitNo(12, ActiveLow)
  val AluOp1: MicroInstruction = fromBitNo(13, ActiveHigh)
  val AluOp2: MicroInstruction = fromBitNo(14, ActiveHigh)

  val RegMLIn: MicroInstruction = fromBitNo(15, ActiveLow)
  val RegMLOut: MicroInstruction = fromBitNo(16, ActiveLow)
  val RegMHIn: MicroInstruction = fromBitNo(17, ActiveLow)
  val RegMHOut: MicroInstruction = fromBitNo(18, ActiveLow)

  val MemoryIn: MicroInstruction = fromBitNo(19, ActiveHigh)
  val MemoryOut: MicroInstruction = fromBitNo(20, ActiveLow)

  val StackUp: MicroInstruction = fromBitNo(21, ActiveHigh)
  val StackDown: MicroInstruction = fromBitNo(22, ActiveHigh)
  val StackOut: MicroInstruction = fromBitNo(23, ActiveLow)
  val Halt: MicroInstruction = fromBitNo(24, ActiveHigh)
  val RegOutIn: MicroInstruction = fromBitNo(25, ActiveHigh)

  val InputEnable: MicroInstruction = fromBitNo(26, ActiveLow)

}


