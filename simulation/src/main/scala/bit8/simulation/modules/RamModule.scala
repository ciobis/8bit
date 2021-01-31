package bit8.simulation.modules

import bit8.simulation.components.And
import bit8.simulation.components.Inverter
import bit8.simulation.components.Ram
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection.LOW
import bit8.simulation.components.wire.Connection.HIGH
import bit8.simulation.components.wire.State

class RamModule(val clk: Connection,
                val ramIn: Connection, val ramOut: Connection,
                val lIn: Connection, val lOut: Connection,
                val hIn: Connection, val hOut: Connection,
                val io0: Connection, val io1: Connection, val io2: Connection, val io3: Connection,
                val io4: Connection, val io5: Connection, val io6: Connection, val io7: Connection) {

  ramIn.wire.onNewState {
    case st: State => {
      val s = ramIn
      val asd = 5
    }
  }
  val ramInInvertedAndClock = Inverter(And(ramIn, clk))

  val (a8, rl_0) = Connection.wire().connections()
  val (a9, rl_1) = Connection.wire().connections()
  val (a10, rl_2) = Connection.wire().connections()
  val (a11, rl_3) = Connection.wire().connections()
  val (a12, rl_4) = Connection.wire().connections()
  val (a13, rl_5) = Connection.wire().connections()
  val (a14, rl_6) = Connection.wire().connections()
  val (a15, rl_7) = Connection.wire().connections()

  val (a0, rh_0) = Connection.wire().connections()
  val (a1, rh_1) = Connection.wire().connections()
  val (a2, rh_2) = Connection.wire().connections()
  val (a3, rh_3) = Connection.wire().connections()
  val (a4, rh_4) = Connection.wire().connections()
  val (a5, rh_5) = Connection.wire().connections()
  val (a6, rh_6) = Connection.wire().connections()
  val (a7, rh_7) = Connection.wire().connections()

  val rl = new RegisterWithDirectOutput(
    lOut, lIn, clk,
    io0, io1, io2, io3, io4, io5, io6, io7,
    rl_0, rl_1, rl_2, rl_3, rl_4, rl_5, rl_6, rl_7
  )

  val rh = new RegisterWithDirectOutput(
    hOut, hIn, clk,
    io0, io1, io2, io3, io4, io5, io6, io7,
    rh_0, rh_1, rh_2, rh_3, rh_4, rh_5, rh_6, rh_7
  )

  val ram = new Ram(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15,
    io0, io1, io2, io3, io4, io5, io6, io7, LOW, HIGH, ramInInvertedAndClock, ramOut
  )

}
