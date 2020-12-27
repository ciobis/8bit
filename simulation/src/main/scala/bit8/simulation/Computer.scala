package bit8.simulation

import bit8.instruction.{Compiler, Instruction, MicroCompiler}
import bit8.simulation.components.Cable
import bit8.simulation.components.Socket.Socket
import bit8.simulation.components.wire.Connection
import bit8.simulation.modules.{ClockModule, OutputWatcher}
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.IntOverflowUtils

import scala.language.postfixOps

object Computer {
  def run(code: List[String], input: Int = 0): Int = {
    val outReady = Connection.wire()
    val outputRead = Connection.wire()
    val (busValueIn, busValueOut) = Cable()
    val inputCable = Cable()

    overflowInt[Bit8](input).setConn(inputCable._1)

    val output = new OutputWatcher(outReady.right, outputRead.right, busValueOut)
    run(code, 0, outReady.left, outputRead.left, busValueIn, inputCable._2)

    output.getResult()
  }

  def runWithIO(code: List[String]): Int = {
    val outReady = Connection.wire()
    val outputRead = Connection.wire()
    val (busValueIn, busValueOut) = Cable()
    val inputCable = Cable()

    val io = new IO()
    val output = new OutputWatcher(outReady.right, outputRead.right, busValueOut, newValue => {
      io.outputModel.setData(newValue)
    })
    io.inputModel.onNewData(value => overflowInt[Bit8](value).setConn(inputCable._1))
    run(code, 1, outReady.left, outputRead.left, busValueIn, inputCable._2)

    output.getResult()
  }

  def run(code: List[String],
          clockCycleTime: Int,
          outputReady: Connection,
          outputRead: Connection,
          bus: Socket,
          inputCable: Socket): Int = {
    val clk = Connection.wire()
    val outEnabled = Connection.wire()
    val halt = Connection.wire()

    val program = Compiler.compile(code).zipWithIndex.map(t => t._2 -> t._1).toMap
    val microCode = MicroCompiler.compile(Instruction.fullInstructionSet(MicroCompiler.maxInstructions()))

    val clock = new ClockModule(clockCycleTime, clk.left, outEnabled.right, outputReady, outputRead, halt.right)
    val computer = new Cpu(clk.right, outEnabled.left, halt.left, bus, inputCable, program, microCode(0), microCode(1), microCode(2), microCode(3))

    clock.start()
    0
  }

  def run(code: String*): Int = run(code.toList)

  def runWithInput(code: String*)(input: Int) = run(code.toList, input)
}
