package instruction

import bit8.instruction.MicroCompiler
import bit8.simulation.components.utils.Utils.IntFromBinaryString
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

import scala.collection.SortedMap

class MicroCompilerSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  Feature("Micro compiler") {
    Scenario("Works") {
      import bit8.instruction.Instruction._

      val insts = Seq(
        Nop,
        MovAB,
        MovBA,
        MovConstToA,
        AddAB,
        SubAB,
        MovConstToB,
      )
      val actual = MicroCompiler.compile(insts).iterator.map(SortedMap.from(_)).toSeq
      val expected = Seq(
        SortedMap(
          "00 0000".binaryInt -> "00000011".binaryInt,
          "00 0001".binaryInt -> "00001000".binaryInt,

          "01 0000".binaryInt -> "00000011".binaryInt,
          "01 0001".binaryInt -> "00001000".binaryInt,
          "01 0010".binaryInt -> "01100000".binaryInt,

          "10 0000".binaryInt -> "00000011".binaryInt,
          "10 0001".binaryInt -> "00001000".binaryInt,
          "10 0010".binaryInt -> "10010000".binaryInt,

          "11 0000".binaryInt -> "00000011".binaryInt,
          "11 0001".binaryInt -> "00001000".binaryInt,
          "11 0010".binaryInt -> "10000010".binaryInt,
          "11 0011".binaryInt -> "00001000".binaryInt,

          "100 0000".binaryInt -> "00000011".binaryInt,
          "100 0001".binaryInt -> "00001000".binaryInt,
          "100 0010".binaryInt -> "00010000".binaryInt,
          "100 0011".binaryInt -> "10000000".binaryInt,

          "101 0000".binaryInt -> "00000011".binaryInt,
          "101 0001".binaryInt -> "00001000".binaryInt,
          "101 0010".binaryInt -> "00010000".binaryInt,
          "101 0011".binaryInt -> "10000000".binaryInt,

          "110 0000".binaryInt -> "00000011".binaryInt,
          "110 0001".binaryInt -> "00001000".binaryInt,
          "110 0010".binaryInt -> "00100010".binaryInt,
          "110 0011".binaryInt -> "00001000".binaryInt,
        ),
        SortedMap(
          "00 0000".binaryInt -> "00000000".binaryInt,
          "00 0001".binaryInt -> "00000000".binaryInt,

          "01 0000".binaryInt -> "00000000".binaryInt,
          "01 0001".binaryInt -> "00000000".binaryInt,
          "01 0010".binaryInt -> "00000000".binaryInt,

          "10 0000".binaryInt -> "00000000".binaryInt,
          "10 0001".binaryInt -> "00000000".binaryInt,
          "10 0010".binaryInt -> "00000000".binaryInt,

          "11 0000".binaryInt -> "00000000".binaryInt,
          "11 0001".binaryInt -> "00000000".binaryInt,
          "11 0010".binaryInt -> "00000000".binaryInt,
          "11 0011".binaryInt -> "00000000".binaryInt,

          "100 0000".binaryInt -> "00000000".binaryInt,
          "100 0001".binaryInt -> "00000000".binaryInt,
          "100 0010".binaryInt -> "10000000".binaryInt,
          "100 0011".binaryInt -> "01000000".binaryInt,

          "101 0000".binaryInt -> "00000000".binaryInt,
          "101 0001".binaryInt -> "00000000".binaryInt,
          "101 0010".binaryInt -> "10100000".binaryInt,
          "101 0011".binaryInt -> "01000000".binaryInt,

          "110 0000".binaryInt -> "00000000".binaryInt,
          "110 0001".binaryInt -> "00000000".binaryInt,
          "110 0010".binaryInt -> "00000000".binaryInt,
          "110 0011".binaryInt -> "00000000".binaryInt,
        )
      )

      assert(actual == expected)
    }
  }

}
