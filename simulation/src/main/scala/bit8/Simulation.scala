package bit8

import bit8.simulation.Computer
import org.apache.commons.cli.{CommandLine, DefaultParser, Options}
import org.apache.commons.io.IOUtils
import scala.jdk.CollectionConverters._

object Simulation {

  def parseArgs(args: Array[String]): CommandLine = {
    val options = new Options()
    options.addOption("ef", "embeddedFile", true, "Name of embedded ASM file")

    new DefaultParser().parse(options, args)
  }

  def getEmbeddedFileLines(fileName: String): List[String] = {
    IOUtils.readLines(getClass().getResourceAsStream(s"/asm/$fileName"), "UTF-8").asScala.toList
  }

  def main(args: Array[String]): Unit = {
    val cli = parseArgs(args)
    val embeddedAsmFileName = cli.getOptionValue("ef")
    val code = getEmbeddedFileLines(embeddedAsmFileName)

    Computer.runWithIO(code)
  }

}
