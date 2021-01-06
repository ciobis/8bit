package bit8.instruction

object Utils {

  def instructionOverrides: Map[Int, Int] = instructionOverrideDefaults ++
    jneToNopWhenEqualsFlagTrue ++
    jeToNopWhenEqualsFlagFalse ++
    jgtToNop ++
    jgteToNop ++
    jltToNop ++
    jlteToNop

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

  def jgtToNop: Map[Int, Int] = {
    val nonSignificantOverrideBits: Int = Integer.parseInt("1101", 2)
    val significantOverrideBits: Int = Integer.parseInt("0010", 2)
    val instructionValue = Instruction.Jgt.no

    (0 to 15).map(variation => {
      ((instructionValue << 4) | ((variation & nonSignificantOverrideBits) | significantOverrideBits)) -> Instruction.Nop3.no
    }).toMap
  }

  def jgteToNop: Map[Int, Int] = {
    val nonSignificantOverrideBits: Int = Integer.parseInt("1100", 2)
    val significantOverrideBits: Int = Integer.parseInt("0010", 2)
    val instructionValue = Instruction.Jgte.no

    (0 to 15).map(variation => {
      ((instructionValue << 4) | ((variation & nonSignificantOverrideBits) | significantOverrideBits)) -> Instruction.Nop3.no
    }).toMap
  }

  def jltToNop: Map[Int, Int] = {
    val nonSignificantOverrideBits: Int = Integer.parseInt("1101", 2)
    val instructionValue = Instruction.Jlt.no

    (0 to 15).map(variation => {
      ((instructionValue << 4) | (variation & nonSignificantOverrideBits)) -> Instruction.Nop3.no
    }).toMap ++
    (0 to 15).map(variation => {
      ((instructionValue << 4) | (variation & Integer.parseInt("1100", 2))) + Integer.parseInt("0011", 2) -> Instruction.Nop3.no
    }).toMap
  }

  def jlteToNop: Map[Int, Int] = {
    val nonSignificantOverrideBits: Int = Integer.parseInt("1101", 2)
    val instructionValue = Instruction.Jlte.no

    (0 to 15).map(variation => {
      ((instructionValue << 4) | (variation & nonSignificantOverrideBits)) -> Instruction.Nop3.no
    }).toMap
  }

}
