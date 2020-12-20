package bit8.instruction

object Utils {

  def instructionOverrides: Map[Int, Int] = instructionOverrideDefaults ++ jneToNopWhenEqualsFlagTrue ++ jeToNopWhenEqualsFlagFalse

  def instructionOverrideDefaults: Map[Int, Int] = {
    (0 to 127).flatMap(value => {
      val keys = (0 to 15).map(variation => {
        (value << 4) | variation
      })

      keys.map(k => k -> value)
    }).toMap
  }

  def jneToNopWhenEqualsFlagTrue: Map[Int, Int] = {
    val nonSignificantOverrideBits: Int = Integer.parseInt("1110", 2)
    val instructionValue = Instruction.Jne.no

    (0 to 15).map(variation => {
      ((instructionValue << 4) | (variation & nonSignificantOverrideBits)) + 1 -> Instruction.Nop3.no
    }).toMap
  }

  def jeToNopWhenEqualsFlagFalse: Map[Int, Int] = {
    val nonSignificantOverrideBits: Int = Integer.parseInt("1110", 2)
    val instructionValue = Instruction.Je.no

    (0 to 15).map(variation => {
      ((instructionValue << 4) | (variation & nonSignificantOverrideBits)) -> Instruction.Nop3.no
    }).toMap
  }

}
