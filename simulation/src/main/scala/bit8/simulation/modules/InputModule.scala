package bit8.simulation.modules

import bit8.simulation.components.{BusTransceiver, Inverter}
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.Connection.HIGH

class InputModule(val enabled: Connection,
                 val out0: Connection, val out1: Connection, val out2: Connection, val out3: Connection, val out4: Connection, val out5: Connection, val out6: Connection, val out7: Connection,
                 val ext0: Connection, val ext1: Connection, val ext2: Connection, val ext3: Connection, val ext4: Connection, val ext5: Connection, val ext6: Connection, val ext7: Connection,
                 ) {

  new BusTransceiver(
    HIGH, enabled,
    ext0, ext1, ext2, ext3, ext4, ext5, ext6, ext7,
    out0, out1, out2, out3, out4, out5, out6, out7
  )

}
