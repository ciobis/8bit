package simulation.components

import bit8.simulation.components.utils.IntegerWithOverflow
import bit8.simulation.components.utils.IntegerWithOverflow._
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class IntegerWithOverflowSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  Feature("Integer with overflow") {

    Scenario("Converts from binary") {
      var expected = 0
      for {
        b0 <- Seq(false, true)
        b1 <- Seq(false, true)
        b2 <- Seq(false, true)
        b3 <- Seq(false, true)
      } yield {
        assert(fromBits(b0, b1, b2, b3 ).value == expected)
        expected += 1
      }

      expected = 0
      for {
        b0 <- Seq(false, true)
        b1 <- Seq(false, true)
        b2 <- Seq(false, true)
        b3 <- Seq(false, true)
        b4 <- Seq(false, true)
        b5 <- Seq(false, true)
        b6 <- Seq(false, true)
        b7 <- Seq(false, true)
      } yield {
        assert(fromBits(b0, b1, b2, b3, b4, b5, b6, b7).value == expected)
        expected += 1
      }

      expected = 0
      for {
        b0 <- Seq(false, true)
        b1 <- Seq(false, true)
        b2 <- Seq(false, true)
        b3 <- Seq(false, true)
        b4 <- Seq(false, true)
        b5 <- Seq(false, true)
        b6 <- Seq(false, true)
        b7 <- Seq(false, true)
        b8 <- Seq(false, true)
        b9 <- Seq(false, true)
        b10 <- Seq(false, true)
        b11 <- Seq(false, true)
        b12 <- Seq(false, true)
        b13 <- Seq(false, true)
        b14 <- Seq(false, true)
        b15 <- Seq(false, true)
      } yield {
        assert(fromBits(b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15).value == expected)
        expected += 1
      }
    }

    Scenario("Converts from Int") {
      assert(overflowInt[Bit4](16).value == 0)
      assert(overflowInt[Bit4](17).value == 1)
      assert(overflowInt[Bit4](31).value == 15)
      assert(overflowInt[Bit4](32).value == 0)

      assert(overflowInt[Bit8](256).value == 0)
      assert(overflowInt[Bit8](257).value == 1)
      assert(overflowInt[Bit8](271).value == 15)
      assert(overflowInt[Bit8](512).value == 0)

      assert(overflowInt[Bit16](65536).value == 0)
      assert(overflowInt[Bit16](65537).value == 1)
      assert(overflowInt[Bit16](65550).value == 14)
      assert(overflowInt[Bit16](131072).value == 0)

      (0 to maxValue[Bit4].value).map(v => {
        assert(overflowInt[Bit4](v).value == v)
      })

      (0 to maxValue[Bit8].value).map(v => {
        assert(overflowInt[Bit8](v).value == v)
      })

      (0 to maxValue[Bit16].value).map(v => {
        assert(overflowInt[Bit16](v).value == v)
      })
    }

    Scenario("Max/Min value") {
      assert(maxValue[Bit4].value == 15)
      assert(minValue[Bit4].value == 0)

      assert(maxValue[Bit8].value == 255)
      assert(minValue[Bit8].value == 0)

      assert(maxValue[Bit16].value == 65535)
      assert(minValue[Bit16].value == 0)
    }

    Scenario("Increment") {
      assert(minValue[Bit4].increment.value == 1)
      assert(minValue[Bit4].increment.increment.value == 2)
      assert(maxValue[Bit4].increment.value == 0)

      assert(minValue[Bit8].increment.value == 1)
      assert(minValue[Bit8].increment.increment.value == 2)
      assert(maxValue[Bit8].increment.value == 0)

      assert(minValue[Bit16].increment.value == 1)
      assert(minValue[Bit16].increment.increment.value == 2)
      assert(maxValue[Bit16].increment.value == 0)
    }

    Scenario("To bits") {
      assert(minValue[Bit4].toBits == (false, false, false, false))
      assert(overflowInt[Bit4](2).toBits == (false, false, true, false))
      assert(maxValue[Bit4].toBits == (true, true, true, true))

      assert(minValue[Bit8].toBits == (false, false, false, false, false, false, false, false))
      assert(overflowInt[Bit8](2).toBits == (false, false, false, false, false, false, true, false))
      assert(maxValue[Bit8].toBits == (true, true, true, true, true, true, true, true))

      assert(minValue[Bit16].toBits == (false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false))
      assert(overflowInt[Bit16](2).toBits == (false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false))
      assert(maxValue[Bit16].toBits == (true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true))
    }

    Scenario("Plus") {
      assert((overflowInt[Bit8](7) plus overflowInt[Bit8](1)).value == 8)
      assert((overflowInt[Bit8](255) plus overflowInt[Bit8](1)).value == 0)
      assert((overflowInt[Bit8](255) plus overflowInt[Bit8](2)).value == 1)
      assert((overflowInt[Bit8](255) plus overflowInt[Bit8](255)).value == 254)
    }

    Scenario("Plus with carry") {
      assert((overflowInt[Bit8](7) plusWithCarry overflowInt[Bit8](1)) == (overflowInt[Bit8](8), false))
      assert((overflowInt[Bit8](255) plusWithCarry overflowInt[Bit8](1)) == (overflowInt[Bit8](0), true))
      assert((overflowInt[Bit8](255) plusWithCarry overflowInt[Bit8](2)) == (overflowInt[Bit8](1), true))
      assert((overflowInt[Bit8](255) plusWithCarry overflowInt[Bit8](255)) == (overflowInt[Bit8](254), true))
    }

    Scenario("Minus") {
      assert((overflowInt[Bit8](3) minus overflowInt[Bit8](1)).value == 2)
      assert((overflowInt[Bit8](3) minus overflowInt[Bit8](3)).value == 0)
      assert((overflowInt[Bit8](255) minus overflowInt[Bit8](255)).value == 0)
      assert((overflowInt[Bit8](0) minus overflowInt[Bit8](1)).value == 255)
      assert((overflowInt[Bit8](0) minus overflowInt[Bit8](255)).value == 1)
    }

    Scenario("Minus with carry") {
      assert((overflowInt[Bit8](3) minusWithCarry overflowInt[Bit8](1)) == (overflowInt[Bit8](2), false))
      assert((overflowInt[Bit8](3) minusWithCarry overflowInt[Bit8](3)) == (overflowInt[Bit8](0), false))
      assert((overflowInt[Bit8](255) minusWithCarry overflowInt[Bit8](255)) == (overflowInt[Bit8](0), false))
      assert((overflowInt[Bit8](0) minusWithCarry overflowInt[Bit8](1)) == (overflowInt[Bit8](255), true))
      assert((overflowInt[Bit8](0) minusWithCarry overflowInt[Bit8](255)) == (overflowInt[Bit8](1), true))
    }

  }

}
