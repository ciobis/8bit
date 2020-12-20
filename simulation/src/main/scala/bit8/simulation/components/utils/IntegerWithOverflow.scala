package bit8.simulation.components.utils

import bit8.simulation.components.utils.IntegerWithOverflow.Bit16
import bit8.simulation.components.utils.IntegerWithOverflow.Bit4
import bit8.simulation.components.utils.IntegerWithOverflow.Bit8


//sealed trait IntegerWithOverflow [BL] {
//  val value: Int
//}

case class IntegerWithOverflow[BL] (value: Int)

sealed trait IntOverflowConverter[BL] {
  val maxValue: Int
  val minValue: Int = 0
  def fromInt(value: Int): Int
  def toBits(value: Int): BL
  def fromBits(value: BL): Int
}

object IntOverflowConverter {
  def intToBitsSeq(v: Int): Seq[Boolean] = v.toBinaryString.map(_ == '1')
  def intToBitsSeq(v: Int, seqSize: Int): Seq[Boolean] = {
    val significantBits = intToBitsSeq(v)
    val frontBitsCount = seqSize - significantBits.size
    val frontBits = (0 until frontBitsCount).map(_ => false).toSeq
    frontBits ++ significantBits
  }
  def bitsSeqToInt(bits: Seq[Boolean]): Int = {
    val strBits = bits.map(if (_) "1" else "0").mkString("")
    Integer.parseInt(strBits, 2)
  }
}

object Bit4Converter extends IntOverflowConverter[Bit4] {
  override def toBits(value: Int): (Boolean, Boolean, Boolean, Boolean) = {
    IntOverflowConverter.intToBitsSeq(value, 4) match {
      case Seq(b0: Boolean, b1: Boolean, b2: Boolean, b3: Boolean) => (b0, b1, b2, b3)
    }
  }

  override val maxValue: Int = 15

  override def fromInt(value: Int): Int = value & maxValue

  override def fromBits(value: (Boolean, Boolean, Boolean, Boolean)): Int = value match {
    case (b0: Boolean, b1: Boolean, b2: Boolean, b3: Boolean) => IntOverflowConverter.bitsSeqToInt(
      Seq(b0, b1, b2, b3)
    )
  }
}

object Bit8Converter extends IntOverflowConverter[Bit8] {
  override def toBits(value: Int): (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) = {
    IntOverflowConverter.intToBitsSeq(value, 8) match {
      case Seq(b0: Boolean, b1: Boolean, b2: Boolean, b3: Boolean, b4: Boolean, b5: Boolean, b6: Boolean, b7: Boolean) =>
        (b0, b1, b2, b3, b4, b5, b6, b7)
    }
  }

  override def fromBits(value: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean)): Int =
    value match {
      case (b0: Boolean, b1: Boolean, b2: Boolean, b3: Boolean, b4: Boolean, b5: Boolean, b6: Boolean, b7: Boolean) =>
        IntOverflowConverter.bitsSeqToInt(
          Seq(b0, b1, b2, b3, b4, b5, b6, b7)
        )
    }

  override val maxValue: Int = 255

  override def fromInt(value: Int): Int = value & maxValue
}

object Bit16Converter extends IntOverflowConverter[Bit16] {
  override def toBits(value: Int): (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) = {
    IntOverflowConverter.intToBitsSeq(value, 16) match {
      case Seq(b0: Boolean, b1: Boolean, b2: Boolean, b3: Boolean, b4: Boolean, b5: Boolean, b6: Boolean, b7: Boolean, b8: Boolean, b9: Boolean, b10: Boolean, b11: Boolean, b12: Boolean, b13: Boolean, b14: Boolean, b15: Boolean) =>
        (b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15)
    }
  }

  override def fromBits(value: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean)): Int =
    value match {
      case (b0: Boolean, b1: Boolean, b2: Boolean, b3: Boolean, b4: Boolean, b5: Boolean, b6: Boolean, b7: Boolean, b8: Boolean, b9: Boolean, b10: Boolean, b11: Boolean, b12: Boolean, b13: Boolean, b14: Boolean, b15: Boolean) =>
        IntOverflowConverter.bitsSeqToInt(
          Seq(b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15)
        )
    }

  override val maxValue: Int = 65535

  override def fromInt(value: Int): Int = value & maxValue
}

object IntegerWithOverflow {
  type Bit4 = (Boolean, Boolean, Boolean, Boolean)
  type Bit8 = (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean)
  type Bit16 = (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean)

  implicit val bit4Converter: IntOverflowConverter[Bit4] = Bit4Converter
  implicit val bit8Converter: IntOverflowConverter[Bit8] = Bit8Converter
  implicit val bit16Converter: IntOverflowConverter[Bit16] = Bit16Converter

  private def apply[BL](value: Int) = new IntegerWithOverflow[BL](value)

  def overflowInt[BL](initValue: Int = 0)(implicit c: IntOverflowConverter[BL]): IntegerWithOverflow[BL] =
    apply[BL](c.fromInt(initValue))

  def maxValue[BL](implicit c: IntOverflowConverter[BL]): IntegerWithOverflow[BL] = overflowInt(c.maxValue)
  def minValue[BL](implicit c: IntOverflowConverter[BL]): IntegerWithOverflow[BL] = overflowInt(c.minValue)

  def fromBits[BL](bits: BL)(implicit c: IntOverflowConverter[BL]): IntegerWithOverflow[BL] = overflowInt(c.fromBits(bits))

  implicit class IntOperations[BL](v: IntegerWithOverflow[BL]) {
    def increment(implicit c: IntOverflowConverter[BL]): IntegerWithOverflow[BL] = overflowInt[BL](v.value + 1)
    def decrement(implicit c: IntOverflowConverter[BL]): IntegerWithOverflow[BL] = overflowInt[BL](v.value - 1)
    def toBits(implicit c: IntOverflowConverter[BL]): BL = c.toBits(v.value)
    def plus(other: IntegerWithOverflow[BL])(implicit c: IntOverflowConverter[BL]): IntegerWithOverflow[BL] =
      overflowInt[BL](v.value + other.value)
    def minus(other: IntegerWithOverflow[BL])(implicit c: IntOverflowConverter[BL]): IntegerWithOverflow[BL] =
      overflowInt[BL](v.value - other.value)

    def plusWithCarry(other: IntegerWithOverflow[BL])(implicit c: IntOverflowConverter[BL]): (IntegerWithOverflow[BL], Boolean) = {
      val value = v.plus(other)
      val carry = v.value + other.value > value.value

      (value, carry)
    }
    def minusWithCarry(other: IntegerWithOverflow[BL])(implicit c: IntOverflowConverter[BL]): (IntegerWithOverflow[BL], Boolean) = {
      val value = v.minus(other)
      val carry = v.value - other.value < value.value

      (value, carry)
    }
  }

}
