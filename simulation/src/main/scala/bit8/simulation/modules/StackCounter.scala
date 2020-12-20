package bit8.simulation.modules

import bit8.simulation.components.And
import bit8.simulation.components.BusTransceiver
import bit8.simulation.components.CounterBidirectional
import bit8.simulation.components.Inverter
import bit8.simulation.components.Or
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection._

/**
 *
 * @param clk - clock
 * @param cu - count up
 * @param cd - count down
 * @param out - output
 */
class StackCounter (val clk: Connection,
                    val cu: Connection ,
                    val cd: Connection ,
                    val out: Connection,
                    val io0: Connection, val io1: Connection, val io2: Connection, val io3: Connection, val io4: Connection, val io5: Connection, val io6: Connection, val io7: Connection
                   ) {

  val io0W, io1W, io2W, io3W, io4W, io5W, io6W, io7W = Connection.wire()
  val count = Or(cu, cd)
  val countAndClockInverted = Inverter(count)
  val carryWire = Connection.wire()
  val outAndClock = And(out, clk)
  val outAnClockInverted = Inverter(outAndClock)
  val outInverted = Inverter(out)

  val c1 = new CounterBidirectional(carryWire.left, countAndClockInverted  , HIGH, cu, clk, LOW, io4W.left, io5W.left, io6W.left, io7W.left, io4, io5, io6, io7)
  val c2 = new CounterBidirectional(GROUND        , carryWire.right, HIGH, cu, HIGH, LOW, io0W.left, io1W.left, io2W.left, io3W.left, io0, io1, io2, io3)


  val bt = new BusTransceiver(
    HIGH, outInverted,
    io0W.right, io1W.right, io2W.right, io3W.right, io4W.right, io5W.right, io6W.right, io7W.right,
    io0, io1, io2, io3, io4, io5, io6, io7
  )

}
