package bit8.simulation.components.wire

import bit8.simulation.components.Socket.Socket
import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow.Bit8
import bit8.simulation.components.wire.Connection.LOW
import bit8.simulation.components.utils.Utils._

class Bus {
  var sockets = Seq.empty[Socket]
  private var conns: Socket = (LOW, LOW, LOW, LOW, LOW, LOW, LOW, LOW)

  def connect(socket: Socket): Bus = {
    socket match {
      case (c1: Connection, c2: Connection, c3: Connection, c4: Connection, c5: Connection, c6: Connection, c7: Connection, c8: Connection) => {
        val (in1, out1) = Branch.branch(c1)
        val (in2, out2) = Branch.branch(c2)
        val (in3, out3) = Branch.branch(c3)
        val (in4, out4) = Branch.branch(c4)
        val (in5, out5) = Branch.branch(c5)
        val (in6, out6) = Branch.branch(c6)
        val (in7, out7) = Branch.branch(c7)
        val (in8, out8) = Branch.branch(c8)

        Join(conns._1, in1)
        Join(conns._2, in2)
        Join(conns._3, in3)
        Join(conns._4, in4)
        Join(conns._5, in5)
        Join(conns._6, in6)
        Join(conns._7, in7)
        Join(conns._8, in8)

        conns = (out1, out2, out3, out4, out5, out6, out7, out8)
      }
    }

    sockets = sockets :+ socket

    this
  }

  def getState: IntegerWithOverflow[Bit8] = conns.toInt

}