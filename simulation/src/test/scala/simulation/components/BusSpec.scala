package simulation.components

import bit8.simulation.components.Cable
import bit8.simulation.components.utils.IntegerWithOverflow.Bit8
import bit8.simulation.components.utils.IntegerWithOverflow.overflowInt
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils.Connections8ToIntOverflow
import bit8.simulation.components.utils.Utils.IntOverflowUtils
import bit8.simulation.components.wire.Branch
import bit8.simulation.components.wire.Bus
import bit8.simulation.components.wire.Connection
import bit8.simulation.components.wire.High
import bit8.simulation.components.wire.Join
import bit8.simulation.components.wire.Low
import bit8.simulation.components.wire.Wire
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class BusSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  Feature("Bus") {
    Scenario("Works") {
      val (ioA1, ioA2) = Cable()
      val (ioB1, ioB2) = Cable()
      val (ioC1, ioC2) = Cable()

      new Bus()
        .connect(ioA1)
        .connect(ioB1)
        .connect(ioC1)

      overflowInt[Bit8](123).setConn(ioA2)
      assert(ioB2.toInt.value == 123)
      assert(ioC2.toInt.value == 123)

      overflowInt[Bit8](170).setConn(ioA2)
      overflowInt[Bit8](85).setConn(ioB2)
      assert(ioC2.toInt.value == 255)
    }

    Scenario("Works with value reset") {
      val (ioA1, ioA2) = Cable()
      val (ioB1, ioB2) = Cable()
      val (ioC1, ioC2) = Cable()

      val bus = new Bus()
        .connect(ioA1)
        .connect(ioB1)
        .connect(ioC1)

      overflowInt[Bit8](128).setConn(ioA2)
      assert(ioB2.toInt.value == 128)
      assert(ioC2.toInt.value == 128)

      overflowInt[Bit8](0).setConn(ioA2)
      assert(ioB2.toInt.value == 0)
      assert(ioC2.toInt.value == 0)
    }

    Scenario("Stuff") {
      val (aIn, aOut) = Connection.wire().connections
      val (branchAIn, branchAOut) = Branch.branch(aOut)

      val (bIn, bOut) = Connection.wire().connections
      val (branchBIn, branchBOut) = Branch.branch(bOut)

      Join(branchAOut, branchBIn)

      aIn.updateState(High)
      assert(bIn.wire.isHigh)

      aIn.updateState(Low)
      assert(bIn.wire.isLow)
    }
  }

}
