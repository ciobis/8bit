package bit8

import bit8.simulation.Computer
import bit8.instruction.Compiler
import bit8.instruction.Instruction
import bit8.instruction.MicroCompiler
import bit8.instruction.micro.MicroInstruction
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire

object Simulation {

  def main(args: Array[String]): Unit = {
    val (clkIn, clkOut) = Connection.wire().connections
    val (outEnabled, outEnabledC) = Connection.wire().connections
    val program = Compiler.compile(Seq(
      "DB result",

      "CALL add3",
      "MOV A,result",
      "HLT",

      "add3:",
      "MOV B,3",
      "ADD result,B",
      "RET"
    )).zipWithIndex.map(t => t._2 -> t._1).toMap

    val microCode = MicroCompiler.compile(Instruction.fullInstructionSet(MicroCompiler.maxInstructions()))

    val computer = new Computer(clkOut, outEnabledC, null, program, microCode(0), microCode(1), microCode(2), microCode(3))

    println(program)
    println(microCode)

    (0 until 128).foreach(c => {
      clkIn.updateState(Low)
      if (c >= 52) {
        val a= 5
      }


      clkIn.updateState(High)
      describe(computer, c)
      if (c >= 52) {
        val a= 5
      }
    })
  }

  private def describe(c: Computer, cycleNo: Int): Unit = {
    println(s"------------")
    println(s"Cycle: $cycleNo")
    c.describe().foreach(println)
  }

}
