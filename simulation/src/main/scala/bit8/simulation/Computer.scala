package bit8.simulation

import bit8.instruction.{Compiler, Instruction, MicroCompiler}
import bit8.simulation.components.Cable
import bit8.simulation.components.wire.Connection
import bit8.simulation.modules.{ClockModule, OutputWatcher}
import scala.concurrent.duration.DurationLong

import scala.concurrent.Await

import scala.language.postfixOps

object Computer {
  def run(code: List[String]): Int = {
    val clk = Connection.wire()
    val outEnabled = Connection.wire()
    val halt = Connection.wire()
    val outputRead = Connection.wire()
    val (busValueIn, busValueOut) = Cable()

    val program = Compiler.compile(code).zipWithIndex.map(t => t._2 -> t._1).toMap
    val microCode = MicroCompiler.compile(Instruction.fullInstructionSet(MicroCompiler.maxInstructions()))

    val clock = new ClockModule(0, clk.left, outEnabled.right, outputRead.left, halt.right)
    val computer = new Cpu(clk.right, outEnabled.left, halt.left, busValueIn, program, microCode(0), microCode(1), microCode(2), microCode(3))
    val output = OutputWatcher(clk.right, halt.right, outEnabled.right, outputRead.right, busValueOut)

    clock.start()
    Await.result(output, 10000 seconds)
  }

  def run(code: String*): Int = run(code.toList)
}
