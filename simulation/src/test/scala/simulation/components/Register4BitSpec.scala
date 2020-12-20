package simulation.components

import bit8.simulation.components.Register4Bit
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class Register4BitSpec  extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  var oeC, oe: Connection = _
  var deC, de: Connection = _
  var clkC, clk: Connection = _
  var q0C, q0: Connection = _
  var q1C, q1: Connection = _
  var q2C, q2: Connection = _
  var q3C, q3: Connection = _
  var d0C, d0: Connection = _
  var d1C, d1: Connection = _
  var d2C, d2: Connection = _
  var d3C, d3: Connection = _

  before {
    val oeWire = Connection.wire()
    val deWire = Connection.wire()
    val clkWire = Connection.wire()
    val q0Wire = Connection.wire()
    val q1Wire = Connection.wire()
    val q2Wire = Connection.wire()
    val q3Wire = Connection.wire()
    val d0Wire = Connection.wire()
    val d1Wire = Connection.wire()
    val d2Wire = Connection.wire()
    val d3Wire = Connection.wire()

    oeC = oeWire.left
    deC = deWire.left
    clkC = clkWire.left
    q0C = q0Wire.left
    q1C = q1Wire.left
    q2C = q2Wire.left
    q3C = q3Wire.left
    d0C = d0Wire.left
    d1C = d1Wire.left
    d2C = d2Wire.left
    d3C = d3Wire.left

    oe = oeWire.right
    de = deWire.right
    clk = clkWire.right
    q0 = q0Wire.right
    q1 = q1Wire.right
    q2 = q2Wire.right
    q3 = q3Wire.right
    d0 = d0Wire.right
    d1 = d1Wire.right
    d2 = d2Wire.right
    d3 = d3Wire.right

    new Register4Bit(oe, de, clk, q0, q1, q2, q3, d0, d1, d2, d3)
  }

  Feature("Register") {
    Scenario("Output should change only on rising clock and when data enable (de) is Low") {
      q0C.updateState(High)
      q1C.updateState(Low)
      q2C.updateState(High)
      q3C.updateState(Low)

      assert(d0C.wire.getState() == Low)
      assert(d1C.wire.getState() == Low)
      assert(d2C.wire.getState() == Low)
      assert(d3C.wire.getState() == Low)

      de.updateState(High)
      clk.updateState(High)
      assert(d0C.wire.getState() == Low)
      assert(d1C.wire.getState() == Low)
      assert(d2C.wire.getState() == Low)
      assert(d3C.wire.getState() == Low)

      de.updateState(Low)
      assert(d0C.wire.getState() == Low)
      assert(d1C.wire.getState() == Low)
      assert(d2C.wire.getState() == Low)
      assert(d3C.wire.getState() == Low)

      clk.updateState(Low)
      assert(d0C.wire.getState() == Low)
      assert(d1C.wire.getState() == Low)
      assert(d2C.wire.getState() == Low)
      assert(d3C.wire.getState() == Low)

      clk.updateState(High)
      assert(d0C.wire.getState() == High)
      assert(d1C.wire.getState() == Low)
      assert(d2C.wire.getState() == High)
      assert(d3C.wire.getState() == Low)
    }

    Scenario("Output data should be provided when output enable (oe) is Low") {
      q0C.updateState(High)
      q1C.updateState(Low)
      q2C.updateState(High)
      q3C.updateState(Low)

      oe.updateState(High)
      //at this point data is latched to register
      de.updateState(Low)
      clk.updateState(High)

      assert(d0C.wire.getState() == Low)
      assert(d1C.wire.getState() == Low)
      assert(d2C.wire.getState() == Low)
      assert(d3C.wire.getState() == Low)

      oe.updateState(Low)
      assert(d0C.wire.getState() == High)
      assert(d1C.wire.getState() == Low)
      assert(d2C.wire.getState() == High)
      assert(d3C.wire.getState() == Low)
    }
  }
  
}
