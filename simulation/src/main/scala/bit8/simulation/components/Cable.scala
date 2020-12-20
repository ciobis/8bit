package bit8.simulation.components

import bit8.simulation.components.Socket.Socket
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Wire

object Cable {

  def apply(): (Socket, Socket) = {
    val (in0, out0) = Connection.wire().connections
    val (in1, out1) = Connection.wire().connections
    val (in2, out2) = Connection.wire().connections
    val (in3, out3) = Connection.wire().connections
    val (in4, out4) = Connection.wire().connections
    val (in5, out5) = Connection.wire().connections
    val (in6, out6) = Connection.wire().connections
    val (in7, out7) = Connection.wire().connections

    (
      (in0, in1, in2, in3, in4, in5, in6, in7),
      (out0, out1, out2, out3, out4, out5, out6, out7)
    )
  }

}
