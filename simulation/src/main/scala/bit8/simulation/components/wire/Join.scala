package bit8.simulation.components.wire

object Join {

  def apply(c1: Connection, c2: Connection): Unit = {
    c1.join(c2)
//    c1.wire.onConnectionUpdate {
//      case conn: Connection if c1 != conn => c2.updateState(conn.getState())
//    }
//    c2.wire.onConnectionUpdate {
//      case conn: Connection if c2 != conn => c1.updateState(conn.getState())
//    }

//    c1.updateState(c2.wire.getState())
//    c2.updateState(c1.wire.getState())
  }

}
