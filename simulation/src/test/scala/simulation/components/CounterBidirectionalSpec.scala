package simulation.components

import bit8.simulation.components.CounterBidirectional
import bit8.simulation.components.utils.Utils.Connections4ToIntOverflow
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class CounterBidirectionalSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var tcC, tc: Connection = _
  var cetC, cet: Connection = _
  var peC, pe: Connection = _
  var udC, ud: Connection = _
  var cpC, cp: Connection = _
  var cepC, cep: Connection = _
  var q0C, q0: Connection = _
  var q1C, q1: Connection = _
  var q2C, q2: Connection = _
  var q3C, q3: Connection = _
  var p0C, p0: Connection = _
  var p1C, p1: Connection = _
  var p2C, p2: Connection = _
  var p3C, p3: Connection = _

  var outputConnections: (Connection, Connection, Connection, Connection) = _
  var counter: CounterBidirectional = _

  before {
    val tcWire = Connection.wire()
    val cetWire = Connection.wire()
    val peWire = Connection.wire()
    val udWire = Connection.wire()
    val cpWire = Connection.wire()
    val cepWire = Connection.wire()
    val q0Wire = Connection.wire()
    val q1Wire = Connection.wire()
    val q2Wire = Connection.wire()
    val q3Wire = Connection.wire()
    val p0Wire = Connection.wire()
    val p1Wire = Connection.wire()
    val p2Wire = Connection.wire()
    val p3Wire = Connection.wire()

    tcC = tcWire.left
    cetC = cetWire.left
    peC = peWire.left
    udC = udWire.left
    cpC = cpWire.left
    cepC = cepWire.left
    q0C = q0Wire.left
    q1C = q1Wire.left
    q2C = q2Wire.left
    q3C = q3Wire.left
    p0C = p0Wire.left
    p1C = p1Wire.left
    p2C = p2Wire.left
    p3C = p3Wire.left

    tc = tcWire.right
    cet = cetWire.right
    pe = peWire.right
    ud = udWire.right
    cp = cpWire.right
    cep = cepWire.right
    q0 = q0Wire.right
    q1 = q1Wire.right
    q2 = q2Wire.right
    q3 = q3Wire.right
    p0 = p0Wire.right
    p1 = p1Wire.right
    p2 = p2Wire.right
    p3 = p3Wire.right

    outputConnections = (q0, q1, q2, q3)
    counter = new CounterBidirectional(tc, cet, pe, ud, cp, cep, q0, q1, q2, q3, p0, p1, p2, p3)
  }

  Feature("4 bit bidirectional counter") {
    Scenario("Loads state on PE Low") {
      p3.updateState(High)
      p2.updateState(Low)
      p1.updateState(High)
      p0.updateState(Low)

      cep.updateState(High)
      cet.updateState(High)

      pe.updateState(Low)
      cp.updateState(High)

      assert(q3.wire.isHigh)
      assert(q2.wire.isLow)
      assert(q1.wire.isHigh)
      assert(q0.wire.isLow)
    }

    Scenario("Doesnt load state on PE High") {
      p0.updateState(High)
      p1.updateState(Low)
      p2.updateState(High)
      p3.updateState(Low)

      cep.updateState(High)
      cet.updateState(High)

      pe.updateState(High)
      cp.updateState(High)

      assert(q3.wire.isLow)
      assert(q2.wire.isLow)
      assert(q1.wire.isLow)
      assert(q0.wire.isLow)
    }

    Scenario("Increments when cep=LOW and cet=LOW and ud=HIGH") {
      val iterations = (1 to 15).toList :+ 0

      cep.updateState(Low)
      cet.updateState(Low)
      ud.updateState(High)
      pe.updateState(High)
      iterations.foreach(result => {
        cp.updateState(High)
        cp.updateState(Low)

        assert(outputConnections.toInt.value == result)
      })
    }

    Scenario("Decrements when cep=LOW and cet=LOW and ud=LOW") {
      val iterations = 0 to 15 by -1

      cep.updateState(Low)
      cet.updateState(Low)
      ud.updateState(Low)
      pe.updateState(High)
      iterations.foreach(result => {
        cp.updateState(High)
        cp.updateState(Low)

        assert(outputConnections.toInt.value == result)
      })
    }

    Scenario("Counts when cep=LOW and cp=HIGH, and cet pulses") {
      val iterations = (1 to 15).toList :+ 0

      cep.updateState(Low)
      cet.updateState(High)
      cp.updateState(High)
      pe.updateState(High)
      ud.updateState(High)

      iterations.foreach(result => {
        cet.updateState(Low)
        cet.updateState(High)

        assert(outputConnections.toInt.value == result)
      })
    }

    Scenario("tc=LOW when counter overflows max state") {
      cep.updateState(High)
      cet.updateState(High)

      p0.updateState(High)
      p1.updateState(High)
      p2.updateState(High)
      p3.updateState(High)

      pe.updateState(Low)
      cp.updateState(High)
      cp.updateState(Low)

      assert(tc.wire.isHigh)

      pe.updateState(High)
      cep.updateState(Low)
      cet.updateState(Low)
      ud.updateState(High)
      cp.updateState(High)
      cp.updateState(Low)
      assert(tc.wire.isLow)

      cp.updateState(High)
      assert(tc.wire.isHigh)
    }

    Scenario("tc=LOW when counter overflows min state") {
      pe.updateState(High)
      cep.updateState(Low)
      cet.updateState(Low)
      ud.updateState(Low)
      cp.updateState(High)
      cp.updateState(Low)
      assert(tc.wire.isLow)

      cp.updateState(High)
      assert(tc.wire.isHigh)
    }
  }

}
