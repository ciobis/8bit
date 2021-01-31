package simulation.modules

import bit8.simulation.components.wire.{Connection, High, Low}
import bit8.simulation.modules.InputModule
import org.scalatest.{BeforeAndAfter, GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import bit8.simulation.components.utils.IntegerWithOverflow._
import bit8.simulation.components.utils.Utils._

class InputModuleSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {
  var out0, out1, out2, out3, out4, out5, out6, out7: Connection = _
  var out0C, out1C, out2C, out3C, out4C, out5C, out6C, out7C: Connection = _

  var ext0, ext1, ext2, ext3, ext4, ext5, ext6, ext7: Connection = _
  var ext0C, ext1C, ext2C, ext3C, ext4C, ext5C, ext6C, ext7C: Connection = _

  var enabled, enabledC: Connection = _

  var outConns: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _
  var extConns: (Connection, Connection, Connection, Connection, Connection, Connection, Connection, Connection) = _

  var inputModule: InputModule = _

  before {
    val out0Wire = Connection.wire()
    val out1Wire = Connection.wire()
    val out2Wire = Connection.wire()
    val out3Wire = Connection.wire()
    val out4Wire = Connection.wire()
    val out5Wire = Connection.wire()
    val out6Wire = Connection.wire()
    val out7Wire = Connection.wire()
    val ext0Wire = Connection.wire()
    val ext1Wire = Connection.wire()
    val ext2Wire = Connection.wire()
    val ext3Wire = Connection.wire()
    val ext4Wire = Connection.wire()
    val ext5Wire = Connection.wire()
    val ext6Wire = Connection.wire()
    val ext7Wire = Connection.wire()
    val enabledWire = Connection.wire()

    out0 = out0Wire.left
    out1 = out1Wire.left
    out2 = out2Wire.left
    out3 = out3Wire.left
    out4 = out4Wire.left
    out5 = out5Wire.left
    out6 = out6Wire.left
    out7 = out7Wire.left
    ext0 = ext0Wire.left
    ext1 = ext1Wire.left
    ext2 = ext2Wire.left
    ext3 = ext3Wire.left
    ext4 = ext4Wire.left
    ext5 = ext5Wire.left
    ext6 = ext6Wire.left
    ext7 = ext7Wire.left
    enabled = enabledWire.left

    out0C = out0Wire.right
    out1C = out1Wire.right
    out2C = out2Wire.right
    out3C = out3Wire.right
    out4C = out4Wire.right
    out5C = out5Wire.right
    out6C = out6Wire.right
    out7C = out7Wire.right
    ext0C = ext0Wire.right
    ext1C = ext1Wire.right
    ext2C = ext2Wire.right
    ext3C = ext3Wire.right
    ext4C = ext4Wire.right
    ext5C = ext5Wire.right
    ext6C = ext6Wire.right
    ext7C = ext7Wire.right
    enabledC = enabledWire.right
    
    outConns = (out0, out1, out2, out3, out4, out5, out6, out7)
    extConns = (ext0, ext1, ext2, ext3, ext4, ext5, ext6, ext7)

    inputModule = new InputModule(
      enabledC,
      out0C, out1C, out2C, out3C, out4C, out5C, out6C, out7C,
      ext0C, ext1C, ext2C, ext3C, ext4C, ext5C, ext6C, ext7C
    )

    enabled.updateState(High)
  }

  Feature("Input Module") {
    Scenario("Reads input from external connections") {
      val data = overflowInt[Bit8](127)
      data.setConn(extConns)

      enabled.updateState(Low)
      assert(outConns.toInt.value == 127)

      enabled.updateState(High)
      assert(outConns.toInt.value == 0)

      enabled.updateState(Low)
      assert(outConns.toInt.value == 127)
    }
  }
  
}
