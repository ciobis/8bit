package bit8.simulation.components.utils

import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.State

object Utils {

  type Conn4 = (Connection, Connection, Connection, Connection)
  type Conn8 = (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection)
  type Conn16 = (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection)

  implicit class IntOverflowUtils[BL](value: IntegerWithOverflow[BL]) {
    def setConn(c0: Connection, c1: Connection, c2: Connection, c3: Connection)
               (implicit evidence: BL =:= Bit4, c: IntOverflowConverter[BL]): Unit = {
      val bits: Bit4 = value.toBits
      c0.updateState(State.fromBoolean(bits._1))
      c1.updateState(State.fromBoolean(bits._2))
      c2.updateState(State.fromBoolean(bits._3))
      c3.updateState(State.fromBoolean(bits._4))
    }

    def setConn(con: Conn4)
               (implicit evidence: BL =:= Bit4, c: IntOverflowConverter[BL]): Unit =
      setConn(con._1, con._2, con._3, con._4)

    def setConn(c0: Connection, c1: Connection, c2: Connection, c3: Connection,
                c4: Connection, c5: Connection, c6: Connection, c7: Connection)
               (implicit evidence: BL =:= Bit8, c: IntOverflowConverter[BL]): Unit = {
      val bits: Bit8 = value.toBits
      c0.updateState(State.fromBoolean(bits._1))
      c1.updateState(State.fromBoolean(bits._2))
      c2.updateState(State.fromBoolean(bits._3))
      c3.updateState(State.fromBoolean(bits._4))
      c4.updateState(State.fromBoolean(bits._5))
      c5.updateState(State.fromBoolean(bits._6))
      c6.updateState(State.fromBoolean(bits._7))
      c7.updateState(State.fromBoolean(bits._8))
    }

    def setConn(con: Conn8)
               (implicit evidence: BL =:= Bit8, c: IntOverflowConverter[BL]): Unit =
      setConn(con._1, con._2, con._3, con._4, con._5, con._6, con._7, con._8)

    def setConn(c0: Connection, c1: Connection, c2: Connection, c3: Connection,
                c4: Connection, c5: Connection, c6: Connection, c7: Connection,
                c8: Connection, c9: Connection, c10: Connection, c11: Connection,
                c12: Connection, c13: Connection, c14: Connection, c15: Connection)
               (implicit evidence: BL =:= Bit16, c: IntOverflowConverter[BL]): Unit = {
      val bits: Bit16 = value.toBits
      c0.updateState(State.fromBoolean(bits._1))
      c1.updateState(State.fromBoolean(bits._2))
      c2.updateState(State.fromBoolean(bits._3))
      c3.updateState(State.fromBoolean(bits._4))
      c4.updateState(State.fromBoolean(bits._5))
      c5.updateState(State.fromBoolean(bits._6))
      c6.updateState(State.fromBoolean(bits._7))
      c7.updateState(State.fromBoolean(bits._8))
      c8.updateState(State.fromBoolean(bits._9))
      c9.updateState(State.fromBoolean(bits._10))
      c10.updateState(State.fromBoolean(bits._11))
      c11.updateState(State.fromBoolean(bits._12))
      c12.updateState(State.fromBoolean(bits._13))
      c13.updateState(State.fromBoolean(bits._14))
      c14.updateState(State.fromBoolean(bits._15))
      c15.updateState(State.fromBoolean(bits._16))
    }

    def setConn(con: Conn16)
               (implicit evidence: BL =:= Bit16, c: IntOverflowConverter[BL]): Unit =
      setConn(con._1, con._2, con._3, con._4, con._5, con._6, con._7, con._8, con._9, con._10, con._11, con._12, con._13, con._14, con._15, con._16)
  }

  implicit class Connections4ToIntOverflow(con: Conn4) {
    def toInt: IntegerWithOverflow[Bit4] = fromBits[Bit4](con._1.wire.isHigh, con._2.wire.isHigh, con._3.wire.isHigh, con._4.wire.isHigh)
  }

  implicit class Connections8ToIntOverflow(con: Conn8) {
    def toInt: IntegerWithOverflow[Bit8] = fromBits[Bit8](con._1.wire.isHigh, con._2.wire.isHigh, con._3.wire.isHigh, con._4.wire.isHigh, con._5.wire.isHigh, con._6.wire.isHigh, con._7.wire.isHigh, con._8.wire.isHigh)
  }

  implicit class Connections16ToIntOverflow(con: Conn16) {
    def toInt: IntegerWithOverflow[Bit16] = fromBits[Bit16](con._1.wire.isHigh, con._2.wire.isHigh, con._3.wire.isHigh, con._4.wire.isHigh, con._5.wire.isHigh, con._6.wire.isHigh, con._7.wire.isHigh, con._8.wire.isHigh,
      con._9.wire.isHigh, con._10.wire.isHigh, con._11.wire.isHigh, con._12.wire.isHigh, con._13.wire.isHigh, con._14.wire.isHigh, con._15.wire.isHigh, con._16.wire.isHigh)
  }

  implicit class IntFromBinaryString(str: String) {
    //4369.toBinaryString
    def binaryInt: Int = Integer.parseInt(str.replaceAll(" ", ""), 2)
  }
}
