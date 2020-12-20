package simulation.components

import bit8.simulation.components.Counter
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import bit8.simulation.components.utils.Utils._
import bit8.simulation.components.wire.Connection.HIGH

class CounterSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var tcC, tc: Connection = _
  var cetC, cet: Connection = _
  var peC, pe: Connection = _
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
  var srC, sr: Connection = _

  var outputConnections: (Connection, Connection, Connection, Connection) = _

  var counter: Counter = _
  before {
    val tcWire = Connection.wire()
    val cetWire = Connection.wire()
    val peWire = Connection.wire()
    val cpWire = Connection.wire()
    val cepWire = Connection.wire()
    val srWire = Connection.wire()
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
    cpC = cpWire.left
    cepC = cepWire.left
    srC = srWire.left
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
    cp = cpWire.right
    cep = cepWire.right
    sr = srWire.right
    q0 = q0Wire.right
    q1 = q1Wire.right
    q2 = q2Wire.right
    q3 = q3Wire.right
    p0 = p0Wire.right
    p1 = p1Wire.right
    p2 = p2Wire.right
    p3 = p3Wire.right

    outputConnections = (q0, q1, q2, q3)
    counter = new Counter(tcC, cetC, peC, cpC, cepC, srC, q0C, q1C, q2C, q3C, p0C, p1C, p2C, p3C)
  }

  Feature("4 Bit counter") {
    Scenario("Loads state on PE Low") {
      sr.updateState(High)

      p3.updateState(High)
      p2.updateState(Low)
      p1.updateState(High)
      p0.updateState(Low)

      pe.updateState(Low)
      cp.updateState(High)

      assert(q3.wire.isHigh)
      assert(q2.wire.isLow)
      assert(q1.wire.isHigh)
      assert(q0.wire.isLow)
    }

    Scenario("Reset on RS low") {
      sr.updateState(High)

      p3.updateState(High)
      p2.updateState(Low)
      p1.updateState(High)
      p0.updateState(Low)

      pe.updateState(Low)
      cp.updateState(High)
      cp.updateState(Low)

      assert(q3.wire.isHigh)
      assert(q2.wire.isLow)
      assert(q1.wire.isHigh)
      assert(q0.wire.isLow)

      sr.updateState(Low)
      cp.updateState(High)

      assert(q3.wire.isLow)
      assert(q2.wire.isLow)
      assert(q1.wire.isLow)
      assert(q0.wire.isLow)
    }

    Scenario("Doesnt load state on PE High") {
      sr.updateState(High)

      p0.updateState(High)
      p1.updateState(Low)
      p2.updateState(High)
      p3.updateState(Low)

      pe.updateState(High)
      cp.updateState(High)

      assert(q0.wire.isLow)
      assert(q1.wire.isLow)
      assert(q2.wire.isLow)
      assert(q3.wire.isLow)
    }

    Scenario("Increments when cep and cet are High") {
      sr.updateState(High)

      val iterations = (1 to 15).toList :+ 0

      pe.updateState(High)
      cep.updateState(High)
      cet.updateState(High)
      iterations.foreach(result => {
        cp.updateState(High)
        cp.updateState(Low)

        assert(outputConnections.toInt.value == result)
      })
    }

    Scenario("Increments when cep and cp are high, and cet pulses") {
      sr.updateState(High)

      val iterations = (1 to 15).toList :+ 0

      pe.updateState(High)
      cep.updateState(High)
      cp.updateState(High)
      iterations.foreach(result => {
        cet.updateState(High)
        cet.updateState(Low)

        assert(outputConnections.toInt.value == result)
      })
    }

    Scenario("Terminal count output(tc) High when counter steps from its max state") {
      sr.updateState(High)

      //Loading max state
      p3.updateState(High)
      p2.updateState(High)
      p1.updateState(High)
      p0.updateState(High)

      pe.updateState(Low)
      cp.updateState(High)
      cp.updateState(Low)
      assert(tc.wire.isLow)

      //Checking if tc is highs after counter resets
      pe.updateState(High)
      cep.updateState(High)
      cet.updateState(High)
      cp.updateState(High)
      cp.updateState(Low)
      assert(tc.wire.isHigh)

      //tc should be Low after consequent clocks
      cp.updateState(High)
      assert(tc.wire.isLow)
    }


  }

}
