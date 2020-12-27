package bit8

import bit8.simulation.Computer

object Simulation {

  def main(args: Array[String]): Unit = {
    val code: List[String] = List(
      "DB x",
      "DB y",
      "DB value",
      "DB direction",
      "MOV x,5",
      "MOV y,0",
      "MOV value,72",
      "MOV direction,0",

      "loop:",
      "MOV OUT,x",
      "MOV OUT,y",
      "MOV OUT,value",
      "MOV direction,INPUT",
      "CMP direction,3",
      "JNE notDown",
      "ADD y,1",
      "notDown:",
      "CMP direction,2",
      "JNE notRight",
      "ADD x,1",
      "notRight:",
      "JMP loop",
      "HLT",
    )
    Computer.runWithIO(code)
  }

}
